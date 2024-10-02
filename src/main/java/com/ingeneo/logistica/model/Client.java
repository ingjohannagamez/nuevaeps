package com.ingeneo.logistica.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Schema(description = "Representa un cliente en el sistema de logística")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del cliente", example = "1")
    private Long id;

    @Schema(description = "Nombre del cliente", example = "ACME Inc.")
    private String name;

}
