package com.example.people.controller;

import com.example.people.DTO.CampaniaRequestDTO;
import com.example.people.DTO.CampaniaResponseDTO;
import com.example.people.entity.CampaniaEntity;
import com.example.people.service.CampaniaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/campanias")
public class CampaniaController {

    @Autowired
    private CampaniaService campaniaService;

    @GetMapping
    public ResponseEntity<List<CampaniaResponseDTO>> getCampaniasActivas() {
        // Listar campañas, mapear a CampaniaResponseDTO y devolver
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponseDTO> getDetalleCampania(@PathVariable Integer id) {
        // Obtener una campaña por ID y mapearla al ResponseDTO completo
        return ResponseEntity.ok(new CampaniaResponseDTO());
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearCampania(@Valid @RequestBody CampaniaRequestDTO dto) {
        CampaniaEntity entidad = new CampaniaEntity();
        entidad.setTitulo(dto.getTitulo());
        entidad.setDescripcionLarga(dto.getDescripcionLarga());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setFechaInicio(LocalDate.now());

        campaniaService.crearCampania(entidad);
        return ResponseEntity.ok("Campaña creada con éxito");
    }
}