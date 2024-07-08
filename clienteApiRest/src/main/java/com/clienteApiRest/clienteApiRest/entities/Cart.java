package com.clienteApiRest.clienteApiRest.entities;


import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "carts")
@Data
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name ="client_id")
    private Client clientId;

    private Double price;
    private Integer amount;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product productId;
}
