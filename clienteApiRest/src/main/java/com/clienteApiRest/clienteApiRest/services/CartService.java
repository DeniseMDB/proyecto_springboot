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

//    public Long findByClientIdAndProductId(Long clientId, Long productId){
//        Optional<Cart> opCart = cartRepository.findByClientIdAndProductId(clientId,productId);
//        return opCart.get().getId();
//
//    }

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

        if (optClient.isPresent() && optProduct.isPresent()) {
            Client client = optClient.get();
            Product product = optProduct.get();

            if (product.getStock() >= amount) {
                // busca un carrito no entregado para el cliente
                Optional<Cart> optionalCart = cartRepository.findByClientIdAndDelivered(client.getId(), false);
                Cart cart;
                if (optionalCart.isPresent()) {
                    cart = optionalCart.get();
                } else {
                    // si no se encuentra un carrito no entregado, se crea uno nuevo
                    cart = new Cart();
                    cart.setPrice(0.0);
                    cart.setAmount(0);
                    cart.setProducts(new ArrayList<>());
                    cart.setClient(client);
                    cart.setDelivered(false);
                }
                // agrega el producto al carrito y actualiza el precio y la cantidad
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

    public Cart removeProduct(Long idClient, Long idProduct) {
        Optional<Client> optClient = clientService.readOne(idClient);
        Optional<Product> optProduct = productService.readOne(idProduct);

        if (optClient.isPresent() && optProduct.isPresent()) {
            Client client = optClient.get();
            Product product = optProduct.get();
            Optional<Cart> optionalCart = cartRepository.findByClientIdAndDelivered(client.getId(), false);
            if (optionalCart.isPresent()) {
                Cart cart = optionalCart.get();
                List<Product> products = cart.getProducts();
                boolean productFound = false;
                for (Product p : products) {
                    if (p.getId().equals(idProduct)) {
                        productFound = true;
                        products.remove(p);
                        cart.setProducts(products);
                        cart.setPrice(cart.getPrice() - product.getPrice());
                        cart.setAmount(cart.getAmount() - 1);
                        productService.addStock(idProduct, 1);
                        return cartRepository.save(cart);
                    }
                }
                if (!productFound) {
                    throw new RuntimeException("Producto no encontrado en el carrito");
                }
            } else {
                throw new RuntimeException("Carrito no encontrado para el cliente");
            }
        } else {
            throw new RuntimeException("Cliente o producto no encontrado");
        }
        throw new RuntimeException("Error desconocido al eliminar producto del carrito");}
}
