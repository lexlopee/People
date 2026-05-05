package com.example.people.controller.notification;

import com.example.people.dto.notificacion.NotificacionResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionResponseDTO>> misNotificaciones(@PathVariable Integer idUsuario) {
        // En una app real, el idUsuario se saca del Token JWT de sesión, no de la URL por seguridad.
        return ResponseEntity.ok(new ArrayList<>());
    }
}