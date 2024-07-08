package com.clienteApiRest.clienteApiRest.repositories;

import com.clienteApiRest.clienteApiRest.entities.Invoice;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvoiceRepository extends CrudRepository<Invoice, Long> {
}
