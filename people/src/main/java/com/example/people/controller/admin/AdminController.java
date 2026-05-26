package com.example.people.controller.admin;

import com.example.people.dto.campaing.CampaniaResponseDTO;
import com.example.people.dto.user.UsuarioResponseDTO;
import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.user.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private CampaniaService campaniaService;
    @Autowired private UsuarioService usuarioService;

    // ── ESTADISTICAS ───────────────────────────────────────────────────────
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<CampaniaEntity> campanias = campaniaService.listarActivas();
        List<UsuarioEntity> usuarios = usuarioService.listarTodos();

        BigDecimal totalRecaudado = campanias.stream()
                .map(c -> c.getMontoActual() != null ? c.getMontoActual() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalCampanias", campanias.size());
        stats.put("totalUsuarios", usuarios.size());
        stats.put("totalCreadores", usuarios.stream().filter(u -> "creador".equals(u.getRol())).count());
        stats.put("totalDonantes", usuarios.stream().filter(u -> "donante".equals(u.getRol())).count());
        stats.put("totalRecaudado", totalRecaudado);
        return ResponseEntity.ok(stats);
    }

    // ── CAMPANAS ───────────────────────────────────────────────────────────
    @GetMapping("/campanias")
    public ResponseEntity<List<CampaniaResponseDTO>> getCampanias() {
        return ResponseEntity.ok(campaniaService.listarActivas()
                .stream().map(this::toCampaniaDTO).collect(Collectors.toList()));
    }

    @DeleteMapping("/campanias/{id}")
    public ResponseEntity<String> borrarCampania(@PathVariable Integer id) {
        CampaniaEntity c = campaniaService.obtenerPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        campaniaService.cerrarCampania(id);
        return ResponseEntity.ok("Campana eliminada");
    }

    @PutMapping("/campanias/{id}/estado")
    public ResponseEntity<String> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        CampaniaEntity c = campaniaService.obtenerPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        c.setEstado(estado.toUpperCase());
        campaniaService.actualizarCampania(c);
        return ResponseEntity.ok("Estado actualizado");
    }

    // ── USUARIOS ───────────────────────────────────────────────────────────
    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> getUsuarios() {
        return ResponseEntity.ok(usuarioService.listarTodos()
                .stream().map(this::toUsuarioDTO).collect(Collectors.toList()));
    }

    @PutMapping("/usuarios/{id}/rol")
    public ResponseEntity<String> cambiarRol(@PathVariable Integer id, @RequestParam String rol) {
        UsuarioEntity u = usuarioService.obtenerPorId(id);
        if (u == null) return ResponseEntity.notFound().build();
        if (!List.of("administrador", "creador", "donante").contains(rol))
            return ResponseEntity.badRequest().body("Rol no valido");
        u.setRol(rol);
        usuarioService.actualizarUsuario(u);
        return ResponseEntity.ok("Rol actualizado");
    }

    @PutMapping("/usuarios/{id}/baja")
    public ResponseEntity<String> darDeBaja(@PathVariable Integer id) {
        UsuarioEntity u = usuarioService.obtenerPorId(id);
        if (u == null) return ResponseEntity.notFound().build();
        usuarioService.darDeBaja(id);
        return ResponseEntity.ok("Usuario dado de baja");
    }

    // ── MAPEOS ─────────────────────────────────────────────────────────────
    private CampaniaResponseDTO toCampaniaDTO(CampaniaEntity c) {
        CampaniaResponseDTO dto = new CampaniaResponseDTO();
        dto.setIdCampania(c.getId());
        dto.setTitulo(c.getTitulo());
        dto.setMontoObjetivo(c.getMontoObjetivo());
        dto.setMontoActual(c.getMontoActual() != null ? c.getMontoActual() : BigDecimal.ZERO);
        dto.setFechaInicio(c.getFechaInicio());
        dto.setFechaFin(c.getFechaFin());
        dto.setEstado(c.getEstado());
        dto.setImagenUrl(c.getImagenUrl());
        if (c.getUsuarioEntity() != null) dto.setNombreCreador(c.getUsuarioEntity().getNombre());
        if (c.getCategoriaEntities() != null && !c.getCategoriaEntities().isEmpty())
            dto.setNombreCategoria(c.getCategoriaEntities().get(0).getNombre());
        if (c.getMontoObjetivo() != null && c.getMontoActual() != null
                && c.getMontoObjetivo().compareTo(BigDecimal.ZERO) > 0)
            dto.setPorcentajeCompletado(Math.min(
                    c.getMontoActual().doubleValue() / c.getMontoObjetivo().doubleValue() * 100, 100.0));
        else dto.setPorcentajeCompletado(0.0);
        if (c.getFechaFin() != null)
            dto.setDiasRestantes((int) Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), c.getFechaFin())));
        return dto;
    }

    private UsuarioResponseDTO toUsuarioDTO(UsuarioEntity u) {
        UsuarioResponseDTO dto = new UsuarioResponseDTO();
        dto.setIdUsuario(u.getId());
        dto.setNombre(u.getNombre());
        dto.setEmail(u.getEmail());
        dto.setRol(u.getRol());
        dto.setFechaAlta(u.getFechaAlt());
        dto.setActivo(u.getFechaBaja() == null);
        return dto;
    }
}