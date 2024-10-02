package com.ingeneo.logistica.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.ingeneo.logistica.api.dto.AuthenticationResponse;
import com.ingeneo.logistica.api.dto.LoginDTO;
import com.ingeneo.logistica.security.JwtTokenProvider;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api")
@Tag(name = "Authentication", description = "API para la autenticación de usuarios")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "Autenticar usuario y obtener token",
               responses = {
                   @ApiResponse(responseCode = "200", description = "Autenticación exitosa",
                                content = @Content(mediaType = "application/json", 
                                schema = @Schema(implementation = AuthenticationResponse.class))),
                   @ApiResponse(responseCode = "401", description = "Autenticación fallida")
               })
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody LoginDTO loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword())
            );
            String token = jwtTokenProvider.generateToken(authentication);
            return ResponseEntity.ok(new AuthenticationResponse(token));
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Las credenciales proporcionadas son incorrectas");
        }
    }
}