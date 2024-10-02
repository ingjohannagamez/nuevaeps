package com.nuevaeps.contrato.api.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.service.interfaces.IContractService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/contracts")
@Tag(name = "Contract", description = "API para la gestión de Contratos de Nueva EPS")
public class ContractController {

    private final IContractService service;

    public ContractController(IContractService service) {
        this.service = service;
    }

    // Obtener todos los contratos con paginación
    @GetMapping
    @Operation(summary = "Lista todos los contratos con paginación",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Operación exitosa", 
                                content = @Content(mediaType = "application/json", 
                                array = @ArraySchema(schema = @Schema(implementation = ContractDTO.class))))
               })
    public ResponseEntity<Page<ContractDTO>> getAllContracts(PageRequest pageable) {
        Page<ContractDTO> page = service.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    // Crear un nuevo contrato con carga de archivo .txt
    @PostMapping("/upload")
    @Operation(summary = "Crea un nuevo contrato con archivo .txt", 
               description = "Permite cargar un archivo .txt junto con los detalles del contrato",
               responses = {
                    @ApiResponse(responseCode = "201", description = "Contrato y archivo guardados exitosamente", 
                                 content = @Content(schema = @Schema(implementation = ContractDTO.class))),
                    @ApiResponse(responseCode = "400", description = "Error en la carga de archivo")
               })
    public ResponseEntity<ContractDTO> uploadContract(@RequestParam("modalidad") String modalidad,
                                                      @RequestParam("numero") String numero,
                                                      @RequestParam("regimen") String regimen,
                                                      @RequestParam("archivo") MultipartFile file) {
        // Validar si el archivo tiene un nombre válido
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe tener un nombre válido");
        }

        // Validar la extensión del archivo
        if (!originalFilename.endsWith(".txt")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El archivo debe ser un archivo .txt");
        }

        // Generar un nombre único para guardar el archivo
        String uniqueFilename = UUID.randomUUID().toString() + ".txt";
        Path filePath = Paths.get("uploads/" + uniqueFilename);

        try {
            // Guardar el archivo en el servidor
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error al guardar el archivo");
        }

        // Crear el DTO del contrato
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setModalidad(modalidad);
        contractDTO.setNumero(numero);
        contractDTO.setRegimen(regimen);
        contractDTO.setArchivo(filePath.toString());

        // Guardar el contrato
        ContractDTO savedContract = service.save(contractDTO);

        return new ResponseEntity<>(savedContract, HttpStatus.CREATED);
    }

    // Obtener un contrato por su ID
    @GetMapping("/{id}")
    @Operation(summary = "Obtiene un contrato por su ID",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Contrato encontrado", 
                                content = @Content(schema = @Schema(implementation = ContractDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
               })
    public ResponseEntity<ContractDTO> getContractById(@PathVariable Long id) {
        return service.findById(id)
            .map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Actualizar un contrato existente
    @PutMapping("/{id}")
    @Operation(summary = "Actualiza un contrato existente",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Contrato actualizado exitosamente", 
                                content = @Content(schema = @Schema(implementation = ContractDTO.class))),
                   @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
               })
    public ResponseEntity<ContractDTO> updateContract(@PathVariable Long id, @Valid @RequestBody ContractDTO contractDTO) {
        ContractDTO updatedContract = service.update(id, contractDTO);
        return ResponseEntity.ok(updatedContract);
    }

    // Eliminar un contrato por su ID
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina un contrato",
               responses = {
                   @ApiResponse(responseCode = "204", description = "Contrato eliminado exitosamente"),
                   @ApiResponse(responseCode = "404", description = "Contrato no encontrado")
               })
    public ResponseEntity<Void> deleteContract(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}