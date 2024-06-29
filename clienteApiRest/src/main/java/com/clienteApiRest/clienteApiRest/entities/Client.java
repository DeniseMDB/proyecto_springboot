package com.clienteApiRest.clienteApiRest.entities;

import jakarta.persistence.*;
import lombok.Data;

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

}
