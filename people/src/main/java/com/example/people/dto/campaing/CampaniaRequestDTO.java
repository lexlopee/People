package com.example.people.dto.campaing;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class CampaniaRequestDTO {
    @NotBlank(message = "El titulo no puede estar vacio")
    private String titulo;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcionLarga;

    @NotNull(message = "El monto objetivo es requerido")
    @Min(value = 100, message = "El monto minimo de la campana debe ser 100")
    private BigDecimal montoObjetivo;

    @NotNull(message = "La fecha de fin es requerida")
    @Future(message = "La fecha de fin debe ser en el futuro")
    private LocalDate fechaFin;

    // Acepta una o varias categorías
    private Integer idCategoria;           // retrocompatibilidad
    private List<Integer> idCategorias;    // ← nuevo: múltiples categorías
}