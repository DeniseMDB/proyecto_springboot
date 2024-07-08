package com.clienteApiRest.clienteApiRest.repositories;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends CrudRepository <Cart, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM carts WHERE carts.client_id = :clientId")
    List<Cart> findByClientId(@Param("clientId") Long clientid);
}