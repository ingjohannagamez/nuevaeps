package com.ingeneo.logistica.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ingeneo.logistica.api.dto.LoginDTO;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    @SuppressWarnings("unused")
	private final UserDetailsService userDetailsService;  // Añadido para uso futuro si es necesario
    private ObjectMapper objectMapper = new ObjectMapper();
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;  // Asegúrate de utilizarlo si es necesario en el futuro
        //setFilterProcessesUrl("/api/authenticate");  // Configura la URL específica si es necesario
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            // Reading username and password from JSON body
            LoginDTO credentials = objectMapper.readValue(request.getInputStream(), LoginDTO.class);
            String username = credentials.getUsername();
            String password = credentials.getPassword();

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            return authenticationManager.authenticate(authenticationToken);
        } catch (IOException e) {
            logger.error("Error parsing the user login credentials", e);
            throw new RuntimeException("Error parsing the user login credentials", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        try {
            logger.info("Inicio de la autenticación exitosa");

            // Generación del token JWT.
            String token = jwtTokenProvider.generateToken(authResult);
            logger.info("Token JWT generado con éxito");

            // Agregando el token a la cabecera de la respuesta.
            response.addHeader("Authorization", "Bearer " + token);
            logger.info("Token JWT añadido a la cabecera de la respuesta");

            // Continuar con la ejecución de la cadena de filtros (si es necesario).
            chain.doFilter(request, response);
            logger.info("Fin de la autenticación exitosa");

        } catch (Exception e) {
            logger.error("Error durante la autenticación exitosa", e);
            throw e;
        }
    }
}
