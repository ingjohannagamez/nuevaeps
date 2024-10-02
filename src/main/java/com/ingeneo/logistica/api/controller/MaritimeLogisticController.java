package com.ingeneo.logistica.api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ingeneo.logistica.api.dto.MaritimeLogisticDTO;
import com.ingeneo.logistica.core.util.CalculateShippingPrice;
import com.ingeneo.logistica.service.interfaces.IMaritimeLogisticService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/maritimeLogistic")
@Tag(name = "MaritimeLogistic", description = "API para la gestión de logística de envios Maritimos")
public class MaritimeLogisticController {

    private final IMaritimeLogisticService service;

    public MaritimeLogisticController(IMaritimeLogisticService service) {
        this.service = service;
    }
    
    @GetMapping
    @Operation(summary = "Lista todas las logísticas de envios Maritimos",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                                content = @Content(mediaType = "application/json", 
                                array = @ArraySchema(schema = @Schema(implementation = MaritimeLogisticDTO.class))))
               })
    public ResponseEntity<List<MaritimeLogisticDTO>> getAllMaritimeLogistic() {
        List<MaritimeLogisticDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/save")
    @Operation(summary = "Crea una nueva logística de envios Maritimos", 
               description = "Guarda un nuevo registro de logística de envios Maritimos. Valida que el precio de envío sea adecuado para productos electrónicos.",
               responses = {
                    @ApiResponse(responseCode = "201", description = "Logística de envios Maritimos creada exitosamente", 
                                 content = @Content(schema = @Schema(implementation = MaritimeLogisticDTO.class))),
                    @ApiResponse(responseCode = "422", description = "Precio de envío fuera del rango permitido para productos electrónicos")
               })
    public ResponseEntity<MaritimeLogisticDTO> createTMaritimeLogistic(@Valid @RequestBody MaritimeLogisticDTO maritimeLogistic) {
        if (!CalculateShippingPrice.isValidShippingPriceForElectronics(maritimeLogistic)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "El precio de envío para productos electrónicos debe estar entre $2000.0 y $5000.0.");
        }
    	MaritimeLogisticDTO savedTruckLogistic = service.save(maritimeLogistic);
        return new ResponseEntity<>(savedTruckLogistic, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una logística de camión por su ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de camión encontrada", 
                                content = @Content(schema = @Schema(implementation = MaritimeLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
    public ResponseEntity<MaritimeLogisticDTO> getMaritimeLogisticById(@PathVariable Long id) {
    	MaritimeLogisticDTO maritimeLogistic = service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(maritimeLogistic);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una logística de camión",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de camión actualizada exitosamente", 
                                content = @Content(schema = @Schema(implementation = MaritimeLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
    public ResponseEntity<MaritimeLogisticDTO> updateMaritimeLogistic(@PathVariable Long id, @Valid @RequestBody MaritimeLogisticDTO maritimeLogisticDTO) {
    	maritimeLogisticDTO.setId(id);
        MaritimeLogisticDTO updatedMaritimeLogistic = service.update(id, maritimeLogisticDTO);
        return ResponseEntity.ok(updatedMaritimeLogistic);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una logística de camión",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Logística de camión eliminada exitosamente"),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
    public ResponseEntity<Void> deleteMaritimeLogistic(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
