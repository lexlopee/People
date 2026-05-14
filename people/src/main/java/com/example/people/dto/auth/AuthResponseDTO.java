package com.example.people.dto.auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token;
    private String tipoToken = "Bearer";
    private Integer id;
    private String nombre;
    private String email;
    private String rol;

    public AuthResponseDTO(String token, Integer id, String nombre, String email, String rol) {
        this.token  = token;
        this.id     = id;
        this.nombre = nombre;
        this.email  = email;
        this.rol    = rol;
    }
}