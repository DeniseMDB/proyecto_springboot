package com.clienteApiRest.clienteApiRest.controllers;

import com.clienteApiRest.clienteApiRest.entities.Client;
import com.clienteApiRest.clienteApiRest.services.ClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/clients")
@Tag(name = "Client", description = "API for Client requests")
public class ClientController {

    @Autowired
    private ClientService service;

    @PostMapping
    @Operation(summary = "Create a new client", description = "Creates a new client in the system",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body to create new client",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"name\": \"Pedrito\", \"lastName\": \"Juarez\", \"docnumber\": 123456789}")
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Client created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Client> createClient(@RequestBody Client client){
        try{
            return new ResponseEntity<>(service.save((client)), HttpStatus.CREATED);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all clients", description = "Retrieves all clients from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clients retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "204", description = "No content", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<List<Client>> getAllClients() {
        try {
            List<Client> clients = service.read();
            if (!clients.isEmpty()) {
                return ResponseEntity.ok(clients);
            } else {
                return ResponseEntity.noContent().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get client by ID", description = "Retrieves a client by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client retrieved successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Object> getClientById(@PathVariable Long id) {
        try {
            Optional<Client> client = service.readOne(id);
            if (client.isPresent()) {
                return ResponseEntity.ok(client.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update client", description = "Updates the details of an existing client",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Request body to update client details",
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"name\": \"Jane\", \"lastName\": \"Smith\", \"docnumber\": \"87654321\"}")
                    )
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client updated successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Client.class))),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client data) {
        try {
            Optional<Client> optionalClient = service.readOne(id);
            if (optionalClient.isPresent()) {
                Client user = optionalClient.get();
                user.setName(data.getName());
                user.setLastName(data.getLastName());
                user.setDocnumber(data.getDocnumber());
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
    @Operation(summary = "Delete client", description = "Deletes a client by their ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Client deleted successfully", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Client not found", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
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
