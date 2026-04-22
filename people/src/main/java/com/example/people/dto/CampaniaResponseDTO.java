package com.example.people.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CampaniaResponseDTO {
    private Integer idCampania;
    private String titulo;
    private String descripcionLarga;
    private BigDecimal montoObjetivo;
    private BigDecimal montoActual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;

    // Datos cruzados para no tener que hacer múltiples peticiones desde el frontend
    private String nombreCreador;
    private String nombreCategoria;
    private Double porcentajeCompletado;
}
