package com.nuevaeps.contrato.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Schema(description = "Representa un Contrato en el sistema de Nueva EPS")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del contrato", example = "1")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Modalidad del contrato", example = "Evento, Capita")
    private String modalidad;

    @Column(nullable = false)
    @Schema(description = "Numero del contrato", example = "1")
    private String numero;

    @Column(nullable = false)
    @Schema(description = "Regimen del contrato", example = "Contributivo, Subsidiado")
    private String regimen;

    @Column(nullable = false)
    @Schema(description = "Nombre del archivo contrato cargado", example = "archivo")
    private String archivo;

}
