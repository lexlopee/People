package com.example.people.controller.campaing;

import com.example.people.dto.campaing.CampaniaRequestDTO;
import com.example.people.dto.campaing.CampaniaResponseDTO;
import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.category.CategoriaEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.category.CategoriaService;
import com.example.people.service.user.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/campanias")
public class CampaniaController {

    @Autowired private CampaniaService campaniaService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<List<CampaniaResponseDTO>> getCampaniasActivas() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponseDTO> getDetalleCampania(@PathVariable Integer id) {
        return ResponseEntity.ok(new CampaniaResponseDTO());
    }

    @PostMapping("/crear")
    public ResponseEntity<String> crearCampania(
            @Valid @RequestBody CampaniaRequestDTO dto,
            Authentication auth  // Spring inyecta el usuario autenticado del JWT
    ) {
        // 1. Obtenemos el usuario logueado desde el token JWT
        String email = auth.getName();
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(email);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        // 2. Obtenemos la categoría elegida
        CategoriaEntity categoria = categoriaService.obtenerPorId(dto.getIdCategoria());
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categoría no válida");
        }

        // 3. Construimos la entidad
        CampaniaEntity entidad = new CampaniaEntity();
        entidad.setTitulo(dto.getTitulo());
        entidad.setDescripcionLarga(dto.getDescripcionLarga());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaInicio(LocalDate.now());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setUsuarioEntity(usuario);
        entidad.setCategoriaEntities(List.of(categoria));

        // 4. Guardamos
        campaniaService.crearCampania(entidad);

        return ResponseEntity.status(HttpStatus.CREATED).body("Campaña creada con éxito");
    }
}