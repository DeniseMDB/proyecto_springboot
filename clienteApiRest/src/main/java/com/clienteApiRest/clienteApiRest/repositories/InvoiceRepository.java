package com.clienteApiRest.clienteApiRest.repositories;

import com.clienteApiRest.clienteApiRest.entities.Invoice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
    @Query(nativeQuery = true, value = "SELECT * FROM invoices WHERE client_id = :clientId")
    Optional<List<Invoice>> findInvoicesByClientId(@Param("clientId") Long clientId);
}
