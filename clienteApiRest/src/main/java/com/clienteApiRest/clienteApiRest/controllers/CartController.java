package com.clienteApiRest.clienteApiRest.controllers;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:8080")
@RestController
@RequestMapping("api/carts")
@Tag(name = "Cart", description = "Api for Cart requests")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping
    @Operation(summary = "Add product to cart", description = "Adds a product to the cart of a client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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
    @Operation(summary = "Find cart by client ID", description = "Finds the cart associated with a specific client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<List<Cart>> findCartByClientId(@RequestBody Map<String, Object> payload) {
        Long clientId = Long.parseLong(payload.get("clientId").toString());
        try {
            List<Cart> carts = cartService.findByClientId(clientId);
            if (!carts.isEmpty()) {
                return ResponseEntity.ok(carts);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete cart", description = "Deletes a cart by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart deleted"),
            @ApiResponse(responseCode = "404", description = "Cart not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<Map<String, Boolean>> deleteCart(@PathVariable Long id) {
        try {
            Optional<Cart> opCart = cartService.readOne(id);
            if (opCart.isPresent()) {
                cartService.deleteCart(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{idClient}/products/{idProduct}")
    @Operation(summary = "Remove product from cart", description = "Removes a product from the cart of a client by product ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Product removed from cart"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
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