package com.example.people.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UsuarioRequestDTO {
    
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Email(message = "Debe ser un correo electrónico válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "El rol es obligatorio")
    private String rol;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;
}