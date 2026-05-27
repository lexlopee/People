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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campanias")
public class CampaniaController {

    @Autowired private CampaniaService campaniaService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private UsuarioService usuarioService;

    @Value("${SUPABASE_URL}")
    private String supabaseUrl;

    @Value("${SUPABASE_SERVICE_KEY}")
    private String supabaseServiceKey;

    // GET todas las activas
    @GetMapping
    public ResponseEntity<List<CampaniaResponseDTO>> getCampaniasActivas() {
        return ResponseEntity.ok(campaniaService.listarActivas()
                .stream().map(this::toDTO).collect(Collectors.toList()));
    }

    // GET por categoría — filtra en BD directamente
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<CampaniaResponseDTO>> getPorCategoria(@PathVariable Integer idCategoria) {
        return ResponseEntity.ok(campaniaService.listarPorCategoria(idCategoria)
                .stream().map(this::toDTO).collect(Collectors.toList()));
    }

    // GET detalle
    @GetMapping("/{id}")
    public ResponseEntity<CampaniaResponseDTO> getDetalleCampania(@PathVariable Integer id) {
        CampaniaEntity c = campaniaService.obtenerPorId(id);
        if (c == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(toDTO(c));
    }

    // POST crear
    @PostMapping("/crear")
    public ResponseEntity<?> crearCampania(
            @Valid @RequestBody CampaniaRequestDTO dto,
            Authentication auth) {

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");

        List<CategoriaEntity> categorias = new ArrayList<>();
        if (dto.getIdCategorias() != null && !dto.getIdCategorias().isEmpty()) {
            for (Integer id : dto.getIdCategorias()) {
                CategoriaEntity cat = categoriaService.obtenerPorId(id);
                if (cat != null) categorias.add(cat);
            }
        } else if (dto.getIdCategoria() != null) {
            CategoriaEntity cat = categoriaService.obtenerPorId(dto.getIdCategoria());
            if (cat != null) categorias.add(cat);
        }

        if (categorias.isEmpty())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Selecciona al menos una categoria");

        CampaniaEntity entidad = new CampaniaEntity();
        entidad.setTitulo(dto.getTitulo());
        entidad.setDescripcionLarga(dto.getDescripcionLarga());
        entidad.setMontoObjetivo(dto.getMontoObjetivo());
        entidad.setFechaInicio(LocalDate.now());
        entidad.setFechaFin(dto.getFechaFin());
        entidad.setUsuarioEntity(usuario);
        entidad.setCategoriaEntities(categorias);

        CampaniaEntity guardada = campaniaService.crearCampania(entidad);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(guardada));
    }

    @PostMapping("/{id}/imagen")
    public ResponseEntity<?> subirImagen(
            @PathVariable Integer id,
            @RequestParam("imagen") MultipartFile archivo,
            Authentication auth) {

        CampaniaEntity campania = campaniaService.obtenerPorId(id);
        if (campania == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Campana no encontrada");

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        boolean esAdmin = "administrador".equals(usuario.getRol());
        boolean esDueno = campania.getUsuarioEntity().getId().equals(usuario.getId());
        if (!esAdmin && !esDueno)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permiso");

        if (archivo.getContentType() == null || !archivo.getContentType().startsWith("image/"))
            return ResponseEntity.badRequest().body("Solo imagenes");
        if (archivo.getSize() > 5 * 1024 * 1024)
            return ResponseEntity.badRequest().body("Maximo 5MB");

        try {
            String ext = archivo.getOriginalFilename() != null
                    ? archivo.getOriginalFilename().substring(archivo.getOriginalFilename().lastIndexOf("."))
                    : ".jpg";
            String nombre = "campana_" + id + "_" + UUID.randomUUID() + ext;

            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(supabaseUrl + "/storage/v1/object/Images/campania/" + nombre))
                    .header("Authorization", "Bearer " + supabaseServiceKey)
                    .header("Content-Type", archivo.getContentType())
                    .header("x-upsert", "true")
                    .PUT(java.net.http.HttpRequest.BodyPublishers.ofByteArray(archivo.getBytes()))
                    .build();

            java.net.http.HttpResponse<String> response =
                    client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Error al subir imagen a Supabase: " + response.body());
            }

            String imagenUrl = supabaseUrl + "/storage/v1/object/public/Images/campania/" + nombre;
            campania.setImagenUrl(imagenUrl);
            campaniaService.actualizarCampania(campania);
            return ResponseEntity.ok(imagenUrl);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar imagen");
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
        if (c.getUsuarioEntity() != null) {
            dto.setIdCreador(c.getUsuarioEntity().getId());
            dto.setNombreCreador(c.getUsuarioEntity().getNombre());
        }
        if (c.getCategoriaEntities() != null && !c.getCategoriaEntities().isEmpty()) {
            dto.setNombreCategoria(c.getCategoriaEntities().get(0).getNombre());
            dto.setCategorias(c.getCategoriaEntities().stream()
                    .map(CategoriaEntity::getNombre).collect(Collectors.toList()));
        }
        if (c.getMontoObjetivo() != null && c.getMontoActual() != null
                && c.getMontoObjetivo().compareTo(java.math.BigDecimal.ZERO) > 0)
            dto.setPorcentajeCompletado(Math.min(
                    c.getMontoActual().doubleValue() / c.getMontoObjetivo().doubleValue() * 100, 100.0));
        else dto.setPorcentajeCompletado(0.0);
        if (c.getFechaFin() != null)
            dto.setDiasRestantes((int) Math.max(0, ChronoUnit.DAYS.between(LocalDate.now(), c.getFechaFin())));
        return dto;
    }
}