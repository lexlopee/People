package com.example.people.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequestDTO {
    @Email(message = "Debe ser un correo válido")
    @NotBlank(message = "El email es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    private String contrasenia;
}
