package com.example.people.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComentarioRequestDTO {
    @NotNull(message = "El ID de la campaña es obligatorio")
    private Integer idCampania;

    @NotBlank(message = "El comentario no puede estar vacío")
    private String contenido;
}
