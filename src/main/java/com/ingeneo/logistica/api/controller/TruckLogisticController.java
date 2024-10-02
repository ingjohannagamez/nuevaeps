package com.ingeneo.logistica.api.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import com.ingeneo.logistica.api.dto.TruckLogisticDTO;
import com.ingeneo.logistica.core.util.CalculateShippingPrice;
import com.ingeneo.logistica.service.interfaces.ITruckLogisticService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import org.springframework.web.bind.annotation.GetMapping;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/truckLogistics")
@Tag(name = "TruckLogistics", description = "API para la gestión de logística de camiones")
public class TruckLogisticController {

    private final ITruckLogisticService service;

    public TruckLogisticController(ITruckLogisticService service) {
        this.service = service;
    }
    
    @GetMapping
    @Operation(summary = "Lista todas las logísticas de camiones",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                                content = @Content(mediaType = "application/json", 
                                array = @ArraySchema(schema = @Schema(implementation = TruckLogisticDTO.class))))
               })
    public ResponseEntity<List<TruckLogisticDTO>> getAllTruckLogistics() {
        List<TruckLogisticDTO> list = service.findAll();
        return ResponseEntity.ok(list);
    }

    @PostMapping("/save")
    @Operation(summary = "Crea una nueva logística de camión", 
               description = "Guarda un nuevo registro de logística de camión. Valida que el precio de envío sea adecuado para productos electrónicos.",
               responses = {
                    @ApiResponse(responseCode = "201", description = "Logística de camión creada exitosamente", 
                                 content = @Content(schema = @Schema(implementation = TruckLogisticDTO.class))),
                    @ApiResponse(responseCode = "422", description = "Precio de envío fuera del rango permitido para productos electrónicos")
               })
    public ResponseEntity<TruckLogisticDTO> createTruckLogistic(@Valid @RequestBody TruckLogisticDTO truckLogistic) {
        if (!CalculateShippingPrice.isValidShippingPriceForElectronics(truckLogistic)) {
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "El precio de envío para productos electrónicos debe estar entre $2000.0 y $5000.0.");
        }
    	TruckLogisticDTO savedTruckLogistic = service.save(truckLogistic);
        return new ResponseEntity<>(savedTruckLogistic, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtiene una logística de camión por su ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de camión encontrada", 
                                content = @Content(schema = @Schema(implementation = TruckLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
	public ResponseEntity<TruckLogisticDTO> getTruckLogisticById(@PathVariable Long id) {
		TruckLogisticDTO truckLogistic = service.findById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		return ResponseEntity.ok(truckLogistic);
	}
    
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza una logística de camión",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Logística de camión actualizada exitosamente", 
                                content = @Content(schema = @Schema(implementation = TruckLogisticDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
    public ResponseEntity<TruckLogisticDTO> updateTruckLogistic(@PathVariable Long id, @Valid @RequestBody TruckLogisticDTO truckLogisticDTO) {
    	truckLogisticDTO.setId(id);
        TruckLogisticDTO updatedTruckLogistic = service.update(id, truckLogisticDTO);
        return ResponseEntity.ok(updatedTruckLogistic);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una logística de camión",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Logística de camión eliminada exitosamente"),
                   @ApiResponse(responseCode = "404", description = "Logística de camión no encontrada")
               })
    public ResponseEntity<Void> deleteTruckLogistic(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
