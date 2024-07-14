package com.clienteApiRest.clienteApiRest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "products")
@Data
@JsonIgnoreProperties({"carts"}) // para evitar recursion infinita
@Schema(description = "Represents a product")
public class Product {
    @Id
    @Column(name="product_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique product identifier", example = "1", required = false)
    private Long id;

    @Schema(description = "Product description", example = "Laptop", required = true)
    private String description;

    @Schema(description = "Product code", example = "12345", required = true)
    private Integer code;

    @Schema(description = "Product stock quantity", example = "50", required = true)
    private Integer stock;

    @Schema(description = "Product price", example = "999.99", required = true)
    private Double price;

    @ManyToMany(mappedBy = "products", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts;
}
