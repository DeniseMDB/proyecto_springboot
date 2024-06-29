package com.clienteApiRest.clienteApiRest.controllers;


import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/clients")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        try{
            return new ResponseEntity<>(service.save((client)), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Client>> getAllClients(){
        try{
            List<Client> clients = service.read();
            if(!clients.isEmpty()){
                return ResponseEntity.ok(clients);
            }else{
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getClientById(@PathVariable Long id){
        try{
            Optional<Client> client = service.readOne(id);
            if(client.isPresent()){
                return ResponseEntity.ok(client.get());
            }else{
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client data) {
        try {
            Optional<Client> optionalClient = service.readOne(id);
            if (optionalClient.isPresent()) {
                Client user = optionalClient.get();
                user.setName(data.getName());
                user.setLastName(data.getLastName());
                user.setDocnumber(user.getDocnumber());
                return ResponseEntity.ok(service.save(user));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteClient(@PathVariable Long id) {
        try {
            Optional<Client> optionalClient = service.readOne(id);
            if (optionalClient.isPresent()) {
                service.deleteClient(id);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
