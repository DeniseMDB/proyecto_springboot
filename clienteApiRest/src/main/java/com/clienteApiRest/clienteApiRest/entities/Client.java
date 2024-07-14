package com.clienteApiRest.clienteApiRest.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table (name ="clients")
@Data
@JsonIgnoreProperties({"carts"}) //para evitar recursion infinita
@Schema(description = "Represents a client")
public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique client identifier", example = "1", required = false)
    private Long id;
    @Schema(description = "Client's name", example = "Mario", required = true)
    private String name;
    @Schema(description = "Client's lastname", example = "Perez", required = true)
    private String lastName;
    @Schema(description = "Client's docnumber", example = "24567498", required = true)
    private Integer docnumber;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Invoice> invoices;

    @OneToMany(mappedBy = "client", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Cart> carts;
}
