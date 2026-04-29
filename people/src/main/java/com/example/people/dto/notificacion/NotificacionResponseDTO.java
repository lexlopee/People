package com.example.people.dto.notificacion;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NotificacionResponseDTO {
    private Integer idNotificacion;
    private String tipoNotificacion; // Ej: "NUEVA_DONACION", "UPDATE_CAMPANIA"
    private String titulo;
    private String mensaje;
    private LocalDateTime fecha;
    private Boolean leida;
}