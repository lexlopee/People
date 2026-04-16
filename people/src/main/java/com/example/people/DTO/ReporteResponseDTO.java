package com.example.people.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ReporteResponseDTO {
    private Integer idReporte;
    private String motivo;
    private String descripcion;
    private LocalDateTime fechaReporte;
    private String estado; // Ej: "PENDIENTE", "REVISADO"

    // Datos útiles para el administrador
    private Integer idCampania;
    private String tituloCampania;
    private String emailUsuarioDenunciante;
}
