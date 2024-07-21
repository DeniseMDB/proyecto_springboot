package com.clienteApiRest.clienteApiRest.controllers;

import com.clienteApiRest.clienteApiRest.entities.Cart;
import com.clienteApiRest.clienteApiRest.entities.Invoice;
import com.clienteApiRest.clienteApiRest.services.InvoiceService;
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
@RequestMapping("api/invoices")
@Tag(name = "Invoice", description = "Api for Invoice requests")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/client/{clientId}")
    @Operation(summary = "Create a new invoice", description = "Creates a new invoice for a specified client")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Invoice created successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Invoice.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Invoice> createInvoice(@PathVariable Long clientId) {
        try {
            Invoice invoice = invoiceService.saveByClientId(clientId);
            return new ResponseEntity<>(invoice, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((Invoice) Map.of("error", "Internal Server Error"));
        }
    }

    @GetMapping
    @Operation(summary = "Get all invoices", description = "Retrieves all invoices from the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Invoice.class))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        try{List<Invoice> invoices = invoiceService.read();
        if (!invoices.isEmpty()) {
            return ResponseEntity.ok(invoices);
        } else { return ResponseEntity.noContent().build(); }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get invoice by ID", description = "Retrieves an invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Invoice.class))),
            @ApiResponse(responseCode = "404", description = "Invoice not found",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable Long id) {
        try {
            Optional<Invoice> invoice = invoiceService.readOne(id);
            if (invoice.isPresent()) {
                return new ResponseEntity<>(invoice.get(), HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body((Invoice) Map.of("error", "Invoice not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body((Invoice) Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Get invoices by client ID", description = "Retrieves all invoices for a specified client ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoices retrieved successfully",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(type = "array", implementation = Invoice.class))),
            @ApiResponse(responseCode = "204", description = "No content",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<List<Invoice>> getInvoicesByClientId(@PathVariable Long clientId) {
        try{
            Optional<List<Invoice>> invoices = invoiceService.findByClientId(clientId);
        if (invoices.isPresent() && !invoices.get().isEmpty()) {
            return ResponseEntity.ok(invoices.get());
        } else {return ResponseEntity.noContent().build();}
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete invoice", description = "Deletes an invoice by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Invoice deleted successfully",
                    content = @Content(mediaType = "application/json",
                    examples = @ExampleObject(value = "{\"message\": \"Invoice deleted successfully\"}"))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Internal Server Error\"}")))
    })
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
