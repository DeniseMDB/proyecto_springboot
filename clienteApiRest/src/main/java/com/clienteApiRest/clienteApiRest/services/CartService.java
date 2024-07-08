package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.entities.Product;
import com.clienteApiRest.clienteApiRest.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private ClientService clientService;
    @Autowired
    private ProductService productService;

    public Cart save(Cart cart){
        return cartRepository.save(cart);
    }

    public List<Cart> findByClientId(Long clientId){
        List<Cart> carts = null;
        try {
            carts = cartRepository.findByClientId(clientId);
        } catch (Exception e) {
            System.out.println("Error fetching carts for client ID " + clientId + ": " + e.getMessage());
        } finally {
            return carts != null ? carts : Collections.emptyList();
        }
    }

    public void deleteCart(Long id){
        cartRepository.deleteById(id);
    }

    public Cart addProduct(Long idClient, Long idProduct, Integer amount){
        Optional<Client> optClient = clientService.readOne(idClient);
        Optional<Product> optProduct = productService.readOne(idProduct);


            Client client = optClient.get();
            Product product = optProduct.get();
            List <Cart> cartList= findByClientId(client.getId());
            Cart cart;
            if (cartList.isEmpty()){
                cart = new Cart();
            }else{
                cart = findByClientId(client.getId()).get(0);
            }
            cart.setProductId(product);
            cart.setClientId(client);
            cart.setAmount(cart.getAmount() + amount);
            cart.setPrice(product.getPrice() * cart.getAmount());

            return cartRepository.save(cart);

    }
}
