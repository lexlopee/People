package com.example.people.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class DonacionResponseDTO {
    private Integer idDonacion;
    private String nombreCampania;
    private BigDecimal monto;
    private LocalDateTime fechaPago;
    private String estadoPago;
}
