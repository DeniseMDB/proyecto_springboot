package com.clienteApiRest.clienteApiRest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.CustomLog;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

@Entity
@Table (name = "invoices")
@Data
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn (name ="client_id")
    @JsonIgnoreProperties({"invoices", "carts"}) // Evitar bucles infinitos en la serialización
    private Client client;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Double total;
}
