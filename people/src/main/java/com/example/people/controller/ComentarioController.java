package com.example.people.controller;

import com.example.people.dto.ComentarioRequestDTO;
import com.example.people.dto.ComentarioResponseDTO;
import com.example.people.service.ComentariosService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/comentarios")
public class ComentarioController {

    @Autowired
    private ComentariosService comentariosService;

    @PostMapping("/publicar")
    public ResponseEntity<String> publicarComentario(@Valid @RequestBody ComentarioRequestDTO comentarioDTO) {
        // Lógica para guardar el comentario
        return ResponseEntity.ok("Comentario publicado");
    }

    @GetMapping("/campania/{idCampania}")
    public ResponseEntity<List<ComentarioResponseDTO>> listarPorCampania(@PathVariable Integer idCampania) {
        // Obtener comentarios de una campaña y mapearlos a ResponseDTO
        return ResponseEntity.ok(new ArrayList<>());
    }
}