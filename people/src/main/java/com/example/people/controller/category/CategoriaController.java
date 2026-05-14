package com.example.people.controller.category;

import com.example.people.dto.category.CategoriaDTO;
import com.example.people.entity.category.CategoriaEntity;
import com.example.people.service.category.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    // GET /api/categorias — público, no necesita token
    @GetMapping
    public ResponseEntity<List<CategoriaDTO>> listar() {
        List<CategoriaDTO> lista = categoriaService.obtenerTodas()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // POST /api/categorias — requiere estar autenticado
    @PostMapping
    public ResponseEntity<CategoriaDTO> crear(@RequestBody CategoriaDTO dto) {
        CategoriaEntity entity = new CategoriaEntity();
        entity.setNombre(dto.getNombre());
        entity.setDescripcion(dto.getDescripcion());
        CategoriaEntity guardada = categoriaService.guardar(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(guardada));
    }

    private CategoriaDTO toDTO(CategoriaEntity e) {
        CategoriaDTO dto = new CategoriaDTO();
        dto.setIdCategoria(e.getId());
        dto.setNombre(e.getNombre());
        dto.setDescripcion(e.getDescripcion());
        return dto;
    }
}