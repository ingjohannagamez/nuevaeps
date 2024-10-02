package com.nuevaeps.contrato.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ContractDTO {

	private Long id;

    @NotBlank(message = "La modalidad no puede estar vacía.")
    @Size(min = 1, max = 100, message = "La modalidad debe tener entre 1 y 100 caracteres.")
    private String modalidad;

    @NotBlank(message = "El número de contrato no puede estar vacío.")
    @Size(min = 1, max = 100, message = "El número de contrato debe tener entre 1 y 100 caracteres.")
    private String numero;

    @NotBlank(message = "El régimen no puede estar vacío.")
    @Size(min = 1, max = 100, message = "El régimen debe tener entre 1 y 100 caracteres.")
    private String regimen;

    @NotBlank(message = "El archivo no puede estar vacío.")
    private String archivo;
}
