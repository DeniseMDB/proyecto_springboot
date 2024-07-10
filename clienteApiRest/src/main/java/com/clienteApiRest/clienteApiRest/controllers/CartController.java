package com.clienteApiRest.clienteApiRest.controllers;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.services.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/carts")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    public ResponseEntity<Cart> addProduct(@RequestBody Map<String, Object> payload) {
        Long idClient = Long.parseLong(payload.get("idClient").toString());
        Long idProduct = Long.parseLong(payload.get("idProduct").toString());
        Integer amount = Integer.parseInt(payload.get("amount").toString());

        try {
            return new ResponseEntity<>(cartService.addProduct(idClient, idProduct, amount), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Cart>> findCartByClientId(@RequestBody Map<String, Object> payload){
        Long clientId = Long.parseLong(payload.get("clientId").toString());
        try{
            List<Cart> carts = cartService.findByClientId(clientId);
            if(!carts.isEmpty()){
                return ResponseEntity.ok(carts);
            } else{
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteCart(@PathVariable Long id){
        try{
            Optional<Cart> opCart = cartService.readOne(id);
            if(opCart.isPresent()) {
                cartService.deleteCart(id);
                return ResponseEntity.ok().build();
            } else{
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idClient}/products/{idProduct}")
    public ResponseEntity<Cart> removeProduct(@PathVariable Long idClient, @PathVariable Long idProduct) {
        try {
            Cart updatedCart = cartService.removeProduct(idClient, idProduct);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            System.out.println("Error removing product from cart for client ID " + idClient + ": " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
