package com.clienteApiRest.clienteApiRest.entities;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table (name ="clients")
@Data
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String lastName;
    private Integer docnumber;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts;
}
