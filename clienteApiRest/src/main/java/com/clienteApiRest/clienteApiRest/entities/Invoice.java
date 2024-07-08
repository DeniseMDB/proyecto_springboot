package com.clienteApiRest.clienteApiRest.entities;

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
    private Client clientId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private Double total;
}
