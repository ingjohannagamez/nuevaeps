package com.ingeneo.logistica.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClientDTO {

	private Long id;

    @NotBlank(message = "El nombre del cliente no puede estar vacío.")
    @Size(min = 2, max = 100, message = "El nombre del cliente debe tener entre 2 y 100 caracteres.")
    private String name;
}
