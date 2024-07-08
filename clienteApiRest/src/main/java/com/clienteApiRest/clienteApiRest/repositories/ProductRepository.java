package com.clienteApiRest.clienteApiRest.repositories;

import com.clienteApiRest.clienteApiRest.entities.Product;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository <Product, Long> {
}
