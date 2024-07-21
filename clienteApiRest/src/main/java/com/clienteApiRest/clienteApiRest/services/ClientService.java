package com.clienteApiRest.clienteApiRest.services;

import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.repositories.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClientService {
    @Autowired
    private ClientRepository repository;

    public List<Client> read(){
        return repository.findAll();
    }

    public Optional<Client> readOne(Long id){
        return repository.findById(id);
    }

    public void deleteClient(Long id){
        repository.deleteById(id);
    }
    public Client save(Client client){
        return repository.save(client);
    }

}
