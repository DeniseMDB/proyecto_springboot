package com.clienteApiRest.clienteApiRest.controllers;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
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
    @Operation(summary = "Add product to cart", description = "Adds a product to the cart of a client",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body to add product to cart",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"idClient\": 1, \"idProduct\": 2, \"amount\": 3}")
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Product added to cart",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
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

    @GetMapping("/{id}")
    @Operation(summary = "Find cart by client ID", description = "Finds the cart associated with a specific client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cart found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<List<Cart>> findCartByClientId(@PathVariable Long id) {
        try {
            List<Cart> carts = cartService.findByClientId(id);
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
            @ApiResponse(responseCode = "200", description = "Cart deleted",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Cart deleted successfully\"}"))),
            @ApiResponse(responseCode = "404", description = "Cart not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Cart not found\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Map<String, Boolean>> deleteCart(@PathVariable Long id) {
        try {
            Optional<Cart> opCart = cartService.readOne(id);
            if (opCart.isPresent()) {
                cartService.deleteCart(id);
                Map<String, Boolean> response = new HashMap<>();
                response.put("deleted", Boolean.TRUE);
                return ResponseEntity.ok(response);
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
            @ApiResponse(responseCode = "200", description = "Product removed from cart",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Cart.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
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
