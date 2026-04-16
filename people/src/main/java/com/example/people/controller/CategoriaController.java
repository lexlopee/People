package com.example.people.controller;

import com.example.people.DTO.CategoriaDTO;
import com.example.people.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        // Retornar la lista de categorías mapeadas desde tu base de datos
        return ResponseEntity.ok(new ArrayList<>());
    }
}//
