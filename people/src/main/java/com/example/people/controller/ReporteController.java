package com.example.people.controller;

import com.example.people.dto.ReporteRequestDTO;
import com.example.people.service.ReporteService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reportes")
public class ReporteController {

    @Autowired
    private ReporteService reporteService;

    @PostMapping("/enviar")
    public ResponseEntity<String> enviarReporte(@Valid @RequestBody ReporteRequestDTO reporteDTO) {
        return ResponseEntity.ok("Reporte enviado al equipo de moderación.");
    }
}