package com.clienteApiRest.clienteApiRest.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "carts")
@Data
@Schema(description = "Represents a cart containing products")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique cart identifier", example = "1", required = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @Schema(description = "Client associated with the cart", required = true)
    private Client client;

    @Schema(description = "Total price of the cart", example = "150.50", required = true)
    private Double price;

    @Schema(description = "Total amount of products in the cart", example = "3", required = true)
    private Integer amount;

    @ManyToMany
    @JoinTable(
            name = "cart_products",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    @Schema(description = "List of products in the cart")
    private List<Product> products;
}
