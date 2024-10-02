package com.ingeneo.logistica.api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDTO {

	@NotBlank(message = "El nombre de usuario no puede estar vacío.")
    private String username;

    @NotBlank(message = "La contraseña no puede estar vacía.")
    private String password;
}