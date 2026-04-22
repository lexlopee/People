package com.example.people.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // ─────────────────────────────────────────────
    // 1. Errores de validación (@Valid en los DTOs)
    // ─────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> erroresCampos = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            erroresCampos.put(error.getField(), error.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(construirRespuesta(
                        HttpStatus.BAD_REQUEST,
                        "Error de validación en los datos enviados",
                        erroresCampos
                ));
    }

    // ─────────────────────────────────────────────
    // 2. Argumento ilegal (ej: email ya registrado)
    // ─────────────────────────────────────────────
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(construirRespuesta(
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        null
                ));
    }

    // ─────────────────────────────────────────────
    // 3. Recurso no encontrado
    // ─────────────────────────────────────────────
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(
            ResourceNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(construirRespuesta(
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        null
                ));
    }

    // ─────────────────────────────────────────────
    // 4. Acceso denegado (rol insuficiente)
    // ─────────────────────────────────────────────
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDenied(
            AccessDeniedException ex) {

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(construirRespuesta(
                        HttpStatus.FORBIDDEN,
                        "No tienes permisos para realizar esta acción",
                        null
                ));
    }

    // ─────────────────────────────────────────────
    // 5. Tipo de parámetro incorrecto en la URL
    // ─────────────────────────────────────────────
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, Object>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(construirRespuesta(
                        HttpStatus.BAD_REQUEST,
                        "El parámetro '" + ex.getName() + "' tiene un formato incorrecto",
                        null
                ));
    }

    // ─────────────────────────────────────────────
    // 6. Cualquier otro error no controlado
    // ─────────────────────────────────────────────
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneric(Exception ex) {

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(construirRespuesta(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Ha ocurrido un error interno. Por favor, inténtalo más tarde",
                        null
                ));
    }

    // ─────────────────────────────────────────────
    // Método auxiliar para construir la respuesta
    // ─────────────────────────────────────────────
    private Map<String, Object> construirRespuesta(HttpStatus status,
                                                   String mensaje,
                                                   Object detalles) {
        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("timestamp", LocalDateTime.now().toString());
        respuesta.put("status", status.value());
        respuesta.put("error", status.getReasonPhrase());
        respuesta.put("mensaje", mensaje);
        if (detalles != null) {
            respuesta.put("detalles", detalles);
        }
        return respuesta;
    }
}