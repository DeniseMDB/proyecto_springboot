package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.entities.Invoice;
import com.clienteApiRest.clienteApiRest.repositories.InvoiceRepository;
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

    public Invoice saveByClientId(Long clientId) {
        Invoice invoice = new Invoice();
        Client client;
        List<Cart> carts = cartService.findByClientId(clientId);
        if (!carts.isEmpty()) {
            Cart cart = carts.get(0);
            invoice.setTotal(cart.getPrice());
        } else {
            throw new RuntimeException("Cart not found");
        }
        invoice.setCreatedAt(LocalDateTime.now());
        Optional<Client> optionalClient = clientService.readOne(clientId);
        if (optionalClient.isPresent()){
            client = optionalClient.get();
            invoice.setClient(client);
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
