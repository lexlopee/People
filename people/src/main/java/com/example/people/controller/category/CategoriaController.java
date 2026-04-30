package com.example.people.controller.category;

import com.example.people.dto.category.CategoriaDTO;
import com.example.people.service.category.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
