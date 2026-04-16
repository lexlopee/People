package com.example.people.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class CobroRequestDTO {
    @NotNull(message = "Se requiere el ID de la campaña")
    private Integer idCampania;

    @NotNull(message = "El monto a retirar es obligatorio")
    private BigDecimal montoNeto;

    @NotBlank(message = "La cuenta de destino es obligatoria")
    private String cuentaDestino; // IBAN o cuenta bancaria
}