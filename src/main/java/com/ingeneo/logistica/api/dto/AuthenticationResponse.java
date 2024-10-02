package com.ingeneo.logistica.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data               // Genera automáticamente getters y setters, además de toString, equals y hashCode.
@NoArgsConstructor  // Genera un constructor sin argumentos.
@AllArgsConstructor // Genera un constructor con todos los argumentos.
public class AuthenticationResponse {
    
	private String token;
}