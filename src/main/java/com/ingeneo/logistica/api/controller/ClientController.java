package com.ingeneo.logistica.api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.ingeneo.logistica.api.dto.ClientDTO;
import com.ingeneo.logistica.api.dto.TruckLogisticDTO;
import com.ingeneo.logistica.service.interfaces.IClientService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/client")
@Tag(name = "Client", description = "API para la gestión de logística de Clientes")
public class ClientController {

    private final IClientService service;

    public ClientController(IClientService service) {
        this.service = service;
    }
    
    @GetMapping
    @Operation(summary = "Lista todas las logísticas de Clientes",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                                content = @Content(mediaType = "application/json", 
                                array = @ArraySchema(schema = @Schema(implementation = ClientDTO.class))))
               })
    public ResponseEntity<List<ClientDTO>> getAllClient() {
        List<ClientDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/save")
    @Operation(summary = "Crea una nueva logística de Cliente", 
               description = "Guarda un nuevo registro de Cliente de camión o envio maritimo",
               responses = {
                    @ApiResponse(responseCode = "201", description = "Logística de Cliente creada exitosamente", 
                                 content = @Content(schema = @Schema(implementation = ClientDTO.class)))
               })
    public ResponseEntity<ClientDTO> createClient(@Valid @RequestBody ClientDTO client) {
        
    	ClientDTO savedClient = service.save(client);
        return new ResponseEntity<>(savedClient, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una logística de Cliente por su ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de Cliente encontrada", 
                                content = @Content(schema = @Schema(implementation = TruckLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de Cliente no encontrada")
               })
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
    	ClientDTO client = service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(client);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una logística de Cliente",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de Cliente actualizada exitosamente", 
                                content = @Content(schema = @Schema(implementation = TruckLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de Cliente no encontrada")
               })
    public ResponseEntity<ClientDTO> updateClient(@PathVariable Long id, @Valid @RequestBody ClientDTO clientDTO) {
    	clientDTO.setId(id);
    	ClientDTO updatedClient = service.update(id, clientDTO);
        return ResponseEntity.ok(updatedClient);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una logística de camión",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Logística de Cliente eliminada exitosamente"),
                   @ApiResponse(responseCode = "404", description = "Logística de Cliente no encontrada")
               })
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
