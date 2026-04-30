package com.example.people.dto.campaing;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class CampaniaRequestDTO {
    @NotBlank(message = "El título no puede estar vacío")
    private String titulo;

    @NotBlank(message = "La descripción es obligatoria")
    private String descripcionLarga;

    @NotNull(message = "El monto objetivo es requerido")
    @Min(value = 100, message = "El monto mínimo de la campaña debe ser 100")
    private BigDecimal montoObjetivo;

    @NotNull(message = "La fecha de fin es requerida")
    @Future(message = "La fecha de fin debe ser en el futuro")
    private LocalDate fechaFin;

    @NotNull(message = "Debe asignar una categoría")
    private Integer idCategoria;
}
