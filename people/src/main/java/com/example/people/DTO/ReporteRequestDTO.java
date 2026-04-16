package com.example.people.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReporteRequestDTO {
    @NotNull(message = "El ID de la campaña a reportar es obligatorio")
    private Integer idCampania;

    @NotBlank(message = "El motivo del reporte es obligatorio")
    private String motivo; // Ej: "Fraude", "Contenido inapropiado"

    @NotBlank(message = "Debe proporcionar una descripción detallada")
    private String descripcion;
}
