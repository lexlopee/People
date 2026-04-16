package com.example.people.DTO;

import lombok.Data;

@Data
public class AuthResponseDTO {
    private String token; // Aquí irá el JWT (Json Web Token)
    private String tipoToken = "Bearer";
    private Integer idUsuario;
    private String email;
    private String rol;

    // Un constructor útil para cuando lo implementes
    public AuthResponseDTO(String token, Integer idUsuario, String email, String rol) {
        this.token = token;
        this.idUsuario = idUsuario;
        this.email = email;
        this.rol = rol;
    }
}
