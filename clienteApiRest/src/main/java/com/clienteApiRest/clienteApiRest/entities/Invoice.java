package com.clienteApiRest.clienteApiRest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "invoices")
@Data
@Schema(description = "Represents an invoice")
public class Invoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique invoice identifier", example = "1", required = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    @JsonIgnoreProperties({"invoices", "carts"}) // Evitar bucles infinitos en la serialización
    @Schema(description = "Client associated with the invoice", required = true)
    private Client client;

    @Column(name = "created_at")
    @Schema(description = "Date and time when the invoice was created", example = "2023-07-09T12:00:00", required = false)
    private LocalDateTime createdAt;

    @Schema(description = "Total amount of the invoice", example = "250.75", required = true)
    private Double total;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Payment method used for the invoice", example = "CREDIT_CARD", required = true)
    private PaymentMethod paymentMethod;
}
