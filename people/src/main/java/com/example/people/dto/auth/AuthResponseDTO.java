package com.example.people.dto.auth;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token; // Aquí irá el JWT (Json Web Token)
    private String tipoToken = "Bearer";
    private Integer idUsuario;
    private String email;
    private String rol;

    public AuthResponseDTO(String token, Integer idUsuario, String email, String rol) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.email = email;
        this.rol = rol;
    }
}
