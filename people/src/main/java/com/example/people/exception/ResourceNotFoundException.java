package com.example.people.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public ResourceNotFoundException(String recurso, Integer id) {
        super(recurso + " con id " + id + " no encontrado");
    }
}