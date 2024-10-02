package com.nuevaeps.contrato.api.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Maneja excepciones de tipo ResponseStatusException.
     * @param ex la excepción ResponseStatusException.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado y detalles del error.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex, WebRequest request) {
        logger.error("ResponseStatusException: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", ex.getStatusCode().value());
        body.put("error", ((HttpStatus) ex.getStatusCode()).getReasonPhrase());
        body.put("message", "Error específico del estado de respuesta: " + ex.getReason());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, ex.getStatusCode());
    }

    /**
     * Maneja excepciones de validación de argumentos de método no válidos.
     * @param ex la excepción MethodArgumentNotValidException.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado 400 y detalles de los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request) {
        logger.error("Validation error: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(),
                        fieldError -> fieldError.getDefaultMessage(),
                        (existing, replacement) -> existing));
        body.put("message", "Errores de validación en los campos: " + errors);
        body.put("path", request.getDescription(false));
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Maneja excepciones de violación de integridad en la base de datos.
     * @param ex la excepción DataIntegrityViolationException.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado 409 y detalles del error de integridad.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex, WebRequest request) {
        logger.error("Database error: {}", ex.getMostSpecificCause().getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.CONFLICT.value());
        body.put("error", HttpStatus.CONFLICT.getReasonPhrase());
        body.put("message", "Error de integridad en la base de datos: " + ex.getMostSpecificCause().getMessage());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.CONFLICT);
    }

    /**
     * Maneja excepciones de violación de restricciones de validación.
     * @param ex la excepción ConstraintViolationException.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado 400 y detalles de la violación de restricciones.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        logger.error("Constraint violation: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", "Violación de restricción: " + ex.getMessage());
        body.put("path", request.getDescription(false));
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Maneja excepciones de solicitudes que no contienen multipart.
     * @param ex la excepción MultipartException.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado 400 y detalles del error.
     */
    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<Object> handleMultipartException(MultipartException ex, WebRequest request) {
        logger.error("Multipart request error: {}", ex.getMessage());
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.BAD_REQUEST.value());
        body.put("error", HttpStatus.BAD_REQUEST.getReasonPhrase());
        body.put("message", "La solicitud no es una solicitud multipart: " + ex.getMessage());
        body.put("path", request.getDescription(false));
        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Maneja cualquier otra excepción inesperada.
     * @param ex la excepción genérica.
     * @param request la solicitud web.
     * @return una respuesta HTTP con el estado 500 y detalles del error inesperado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAllExceptions(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        body.put("error", HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        body.put("message", "Error interno del servidor: " + ex.getMessage());
        body.put("path", request.getDescription(false));
        return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}