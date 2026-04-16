package com.example.people.DTO;

import lombok.Data;
import java.time.LocalDate;

@Data
public class UsuarioResponseDTO {
    private Integer idUsuario;
    private String nombre;
    private String email;
    private String rol;
    private LocalDate fechaAlta;
    private String nombreRango;
}