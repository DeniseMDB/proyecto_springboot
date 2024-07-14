package com.clienteApiRest.clienteApiRest.repositories;

import com.clienteApiRest.clienteApiRest.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository <Product, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock - :amount WHERE p.id = :productId AND p.stock >= :amount")
    void decrementStock(@Param("productId") Long productId, @Param("amount") Integer amount);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.stock = p.stock + :amount WHERE p.id = :productId AND p.stock >= :amount")
    void incrementStock(@Param("productId") Long productId, @Param("amount") Integer amount);

}
