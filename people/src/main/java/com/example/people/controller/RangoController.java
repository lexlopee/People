package com.example.people.controller;

import com.example.people.dto.RangoDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/rangos")
public class RangoController {

    @GetMapping
    public ResponseEntity<List<RangoDTO>> listarRangos() {
        // Devuelve los niveles disponibles: Bronce, Plata, Oro...
        return ResponseEntity.ok(new ArrayList<>());
    }
}