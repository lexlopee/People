package com.example.people.dto.donations;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class DonacionRequestDTO {
    @NotNull(message = "Se requiere el ID de la campaña")
    private Integer idCampania;

    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1.0", message = "La donación mínima es 1.0")
    private BigDecimal monto;

    @NotNull(message = "Se requiere el ID del usuario donante")
    private Integer idDonante;

    private String tokenPasarela; // El token que te devuelve Stripe/PayPal
}