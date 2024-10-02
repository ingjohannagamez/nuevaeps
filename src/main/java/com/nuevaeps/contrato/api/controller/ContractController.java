package com.nuevaeps.contrato.api.controller;

import com.nuevaeps.contrato.api.dto.ContractDTO;
import com.nuevaeps.contrato.core.util.FileUtils;
import com.nuevaeps.contrato.service.interfaces.IContractService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<Page<ContractDTO>> getAllContracts(
            @RequestParam(defaultValue = "0") int page, 
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<ContractDTO> pageResult = service.findAll(pageable);
        return ResponseEntity.ok(pageResult);
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
                                                      @RequestParam("numero") Integer numero,
                                                      @RequestParam("regimen") String regimen,
                                                      @RequestParam("archivo") MultipartFile file) {

        // Uso de FileUtils para validación y manejo de archivos
        FileUtils.validarArchivoTxt(file);
        FileUtils.crearDirectorioDeCargaSiNoExiste();

        // Generar un nombre único basado en el nombre original del archivo
        String nuevoNombreArchivo = FileUtils.generarNombreDeArchivoUnico(file.getOriginalFilename());
        FileUtils.guardarArchivo(file, nuevoNombreArchivo);

        // Crear el DTO del contrato y guardar el contrato
        ContractDTO contractDTO = new ContractDTO();
        contractDTO.setModalidad(modalidad);
        contractDTO.setNumero(numero);
        contractDTO.setRegimen(regimen);
        contractDTO.setArchivo(nuevoNombreArchivo);

        ContractDTO contratoGuardado = service.save(contractDTO);
        return new ResponseEntity<>(contratoGuardado, HttpStatus.CREATED);
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