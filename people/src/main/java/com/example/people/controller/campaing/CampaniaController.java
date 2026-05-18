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
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campanias")
public class CampaniaController {

    @Autowired private CampaniaService campaniaService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private UsuarioService usuarioService;

    // GET /api/campanias — devuelve todas las activas desde la BD
    @GetMapping
    public ResponseEntity<List<CampaniaResponseDTO>> getCampaniasActivas() {
        List<CampaniaResponseDTO> lista = campaniaService.listarActivas()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // GET /api/campanias/{id} — detalle de una campaña
    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponseDTO> getDetalleCampania(@PathVariable Integer id) {
        CampaniaEntity c = campaniaService.obtenerPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(c));
    }

    // POST /api/campanias/crear — guarda en BD y devuelve DTO con id
    @PostMapping("/crear")
    public ResponseEntity<?> crearCampania(
            @Valid @RequestBody CampaniaRequestDTO dto,
            Authentication auth) {

        String email = auth.getName();
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(email);
        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        CategoriaEntity categoria = categoriaService.obtenerPorId(dto.getIdCategoria());
        if (categoria == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Categoría no válida");
        }

        CampaniaEntity entidad = new CampaniaEntity();
        entidad.setTitulo(dto.getTitulo());
        entidad.setDescripcionLarga(dto.getDescripcionLarga());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaInicio(LocalDate.now());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setUsuarioEntity(usuario);
        entidad.setCategoriaEntities(List.of(categoria));

        CampaniaEntity guardada = campaniaService.crearCampania(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(guardada));
    }

    // Mapeo Entity → DTO
    private CampaniaResponseDTO toDTO(CampaniaEntity c) {
        CampaniaResponseDTO dto = new CampaniaResponseDTO();
        dto.setIdCampania(c.getId());
        dto.setTitulo(c.getTitulo());
        dto.setDescripcionLarga(c.getDescripcionLarga());
        dto.setMontoObjetivo(c.getMontoObjetivo());
        dto.setMontoActual(c.getMontoActual() != null ? c.getMontoActual() : java.math.BigDecimal.ZERO);
        dto.setFechaInicio(c.getFechaInicio());
        dto.setFechaFin(c.getFechaFin());
        dto.setEstado(c.getEstado());

        if (c.getUsuarioEntity() != null) {
            dto.setNombreCreador(c.getUsuarioEntity().getNombre());
        }
        if (c.getCategoriaEntities() != null && !c.getCategoriaEntities().isEmpty()) {
            dto.setNombreCategoria(c.getCategoriaEntities().get(0).getNombre());
        }

        if (c.getMontoObjetivo() != null && c.getMontoActual() != null
                && c.getMontoObjetivo().compareTo(java.math.BigDecimal.ZERO) > 0) {
            double pct = c.getMontoActual().doubleValue() / c.getMontoObjetivo().doubleValue() * 100;
            dto.setPorcentajeCompletado(Math.min(pct, 100.0));
        } else {
            dto.setPorcentajeCompletado(0.0);
        }

        if (c.getFechaFin() != null) {
            long dias = ChronoUnit.DAYS.between(LocalDate.now(), c.getFechaFin());
            dto.setDiasRestantes((int) Math.max(0, dias));
        }

        return dto;
    }
}