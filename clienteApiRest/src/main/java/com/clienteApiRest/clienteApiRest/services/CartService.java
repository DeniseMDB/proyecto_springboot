package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.entities.Product;
import com.clienteApiRest.clienteApiRest.repositories.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    public Long findByClientIdAndProductId(Long clientId, Long productId){
        Optional<Cart> opCart = cartRepository.findByClientIdAndProductId(clientId,productId);
        return opCart.get().getId();

    }

    public void deleteCart(Long id){
        cartRepository.deleteById(id);
    }

    public List<Cart> findAllCarts(){
        return (List<Cart>) cartRepository.findAll();
    }
    public Optional<Cart> readOne(Long id){
        return cartRepository.findById(id);
    }

    public Cart addProduct(Long idClient, Long idProduct, Integer amount) {
        Optional<Client> optClient = clientService.readOne(idClient);
        Optional<Product> optProduct = productService.readOne(idProduct);
        //se revisa que exista cliente y producto
        if (optClient.isPresent() && optProduct.isPresent()) {
            Client client = optClient.get();
            Product product = optProduct.get();
            //se revisa que la cantidad de producto a ingresar sea menor que el stock del mismo
            if (product.getStock() >= amount) {
                List<Cart> cartList = findByClientId(client.getId());
                Cart cart;
                if (cartList.isEmpty()) {
                    cart = new Cart();
                    cart.setPrice(0.0);
                    cart.setAmount(0);
                    cart.setProducts(new ArrayList<>());
                    cart.setClient(client);
                } else {
                    cart = cartList.get(0);
                }
                cart.getProducts().add(product);
                cart.setPrice(cart.getPrice() + (product.getPrice() * amount));
                cart.setAmount(cart.getAmount() + amount);
                productService.subtractStock(idProduct, amount);
                return cartRepository.save(cart);

            } else {
                throw new RuntimeException("Stock insuficiente para el producto");
            }
        } else {
            throw new RuntimeException("Cliente o producto no encontrado");
        }
    }

    public Cart removeProduct(Long idClient, Long idProduct){
        Optional<Client> optClient = clientService.readOne(idClient);
        Optional<Product> optProduct = productService.readOne(idProduct);

        if (optClient.isPresent() && optProduct.isPresent()) {
            Client client = optClient.get();
            Product product = optProduct.get();
            List<Cart> cartList = findByClientId(client.getId());
            if (!cartList.isEmpty()) {
                Cart cart = cartList.get(0);
                List<Product> products = cart.getProducts();
                if (products.remove(product)) {
                    cart.setProducts(products);
                    cart.setPrice(cart.getPrice() - product.getPrice());
                    return cartRepository.save(cart);
                }
            }
        }
        throw new RuntimeException("Product or Client not found in cart");
    }


}
