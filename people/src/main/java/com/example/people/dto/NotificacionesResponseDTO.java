package com.example.people.dto;

import lombok.Data;

@Data
public class NotificacionesResponseDTO {
    private Integer idNotificacion;
    private String tipoNotificacion;
    private String titulo;
    private String mensaje;
    private boolean leida;
}
