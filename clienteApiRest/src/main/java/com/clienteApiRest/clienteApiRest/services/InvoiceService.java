package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.entities.Invoice;
import com.clienteApiRest.clienteApiRest.entities.PaymentMethod;
import com.clienteApiRest.clienteApiRest.repositories.InvoiceRepository;
import org.apache.catalina.valves.rewrite.InternalRewriteMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class InvoiceService {
    @Autowired
    private InvoiceRepository repository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ClientService clientService;

    public Invoice saveByClientId(Long clientId, String paymentMethod) {
        Invoice invoice = new Invoice();
        Client client;
        List<Cart> carts = cartService.findByClientId(clientId);
        if (carts.isEmpty()) {
            throw new RuntimeException("Cart not found");
        }
        boolean cartFound = false;
        for (Cart cart : carts) {
            if (!cart.getDelivered()) {
                invoice.setTotal(cart.getPrice());
                cart.setDelivered(true);
                cartFound = true;
                break;
            }
        }
        if (!cartFound) {
            throw new RuntimeException("No carts to be delivered found");
        }
        invoice.setCreatedAt(LocalDateTime.now());
        String paymentMethod1 = paymentMethod.toUpperCase();
        switch (paymentMethod1) {
            case "CREDIT":
                invoice.setPaymentMethod(PaymentMethod.CREDIT_CARD);
                break;
            case "DEBIT":
                invoice.setPaymentMethod(PaymentMethod.DEBIT_CARD);
                break;
            case "CASH":
                invoice.setPaymentMethod(PaymentMethod.CASH);
                break;
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
        Optional<Client> optionalClient = clientService.readOne(clientId);
        if (optionalClient.isPresent()){
            client = optionalClient.get();
            invoice.setClient(client);
        }else {
            throw new RuntimeException("Client does not exist");
        }
        return repository.save(invoice);
    }

    public List<Invoice> read(){
        return (List<Invoice>) repository.findAll();
    }

    public Optional<Invoice> readOne(Long id){
        return repository.findById(id);
    }

    public Optional<List<Invoice>> findByClientId(Long clientId){
        return repository.findInvoicesByClientId(clientId);
    }

    public void deleteInvoice(Long id){
        repository.deleteById(id);
    }


}
