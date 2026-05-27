package com.example.people.dto.campaing;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CampaniaResponseDTO {
    private Integer idCampania;
    private Integer idCreador;        // ← nuevo: para comparar en frontend
    private String titulo;
    private String descripcionLarga;
    private BigDecimal montoObjetivo;
    private BigDecimal montoActual;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado;
    private String nombreCreador;
    private String nombreCategoria;   // primera categoría (retrocompatibilidad)
    private List<String> categorias;  // ← nuevo: todas las categorías
    private Double porcentajeCompletado;
    private Integer diasRestantes;
    private String imagenUrl;
}