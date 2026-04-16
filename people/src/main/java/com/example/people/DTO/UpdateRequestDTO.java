package com.example.people.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRequestDTO {
    @NotNull(message = "El ID de la campaña es obligatorio")
    private Integer idCampania;

    @NotBlank(message = "El título de la actualización es obligatorio")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcion;
}
