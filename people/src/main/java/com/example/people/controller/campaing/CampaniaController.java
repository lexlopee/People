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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campanias")
public class CampaniaController {

    @Autowired private CampaniaService campaniaService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private UsuarioService usuarioService;

    // Carpeta donde se guardan las imágenes (configurable en application.properties)
    @Value("${upload.dir:uploads/campanias}")
    private String uploadDir;

    // GET /api/campanias
    @GetMapping
    public ResponseEntity<List<CampaniaResponseDTO>> getCampaniasActivas() {
        List<CampaniaResponseDTO> lista = campaniaService.listarActivas()
                .stream().map(this::toDTO).collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // GET /api/campanias/{id}
    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponseDTO> getDetalleCampania(@PathVariable Integer id) {
        CampaniaEntity c = campaniaService.obtenerPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(c));
    }

    // POST /api/campanias/crear — crea campaña con datos JSON
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

    // POST /api/campanias/{id}/imagen — sube imagen a una campaña existente
    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> subirImagen(
            @PathVariable Integer id,
            @RequestParam("imagen") MultipartFile archivo,
            Authentication auth) {

        CampaniaEntity campania = campaniaService.obtenerPorId(id);
        if (campania == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campaña no encontrada");
        }

        // Verificar que el usuario es el dueño o admin
        String email = auth.getName();
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(email);
        boolean esAdmin = usuario.getRol().equals("administrador");
        boolean esDueno = campania.getUsuarioEntity().getId().equals(usuario.getId());
        if (!esAdmin && !esDueno) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso");
        }

        // Validar tipo de archivo
        String contentType = archivo.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Solo se permiten imágenes");
        }

        // Validar tamaño (máximo 5MB)
        if (archivo.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La imagen no puede superar 5MB");
        }

        try {
            // Crear directorio si no existe
            Path dirPath = Paths.get(uploadDir);
            Files.createDirectories(dirPath);

            // Nombre único para evitar colisiones
            String extension = archivo.getOriginalFilename() != null
                    ? archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf("."))
                    : ".jpg";
            String nombreArchivo = "campana_" + id + "_" + UUID.randomUUID() + extension;

            // Guardar archivo
            Path rutaArchivo = dirPath.resolve(nombreArchivo);
            Files.write(rutaArchivo, archivo.getBytes());

            // Guardar URL en la BD
            String imagenUrl = "/uploads/campanias/" + nombreArchivo;
            campania.setImagenUrl(imagenUrl);
            campaniaService.actualizarCampania(campania);

            return ResponseEntity.ok(imagenUrl);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al guardar la imagen");
        }
    }

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
        dto.setImagenUrl(c.getImagenUrl());

        if (c.getUsuarioEntity() != null) dto.setNombreCreador(c.getUsuarioEntity().getNombre());
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