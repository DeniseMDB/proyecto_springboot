package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Invoice;
import com.clienteApiRest.clienteApiRest.repositories.InvoiceRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

public class InvoiceService {
    @Autowired
    private InvoiceRepository repository;

    public Invoice save(Invoice invoice){
        return repository.save(invoice);
    }

    public List<Invoice> read(){
        return (List<Invoice>) repository.findAll();
    }

    public Optional<Invoice> readOne(Long id){
        return repository.findById(id);
    }

    public void deleteInvoice(Long id){
        repository.deleteById(id);
    }


}
