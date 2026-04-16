package com.example.people.DTO;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CobroResponseDTO {
    private Integer idCobro;
    private BigDecimal montoNeto;
    private LocalDateTime fechaSolicitud;
    private String cuentaDestino;
}
