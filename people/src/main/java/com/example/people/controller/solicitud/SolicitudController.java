package com.example.people.controller.solicitud;

import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.category.CategoriaEntity;
import com.example.people.entity.solicitud.SolicitudEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.category.CategoriaService;
import com.example.people.service.email.EmailService;
import com.example.people.service.user.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/solicitudes")
public class SolicitudController {

    @PersistenceContext private EntityManager em;
    @Autowired private UsuarioService usuarioService;
    @Autowired private CategoriaService categoriaService;
    @Autowired private CampaniaService campaniaService;
    @Autowired private EmailService emailService;

    // POST: crear solicitud (creador o admin). NO crea la campaña, solo la solicitud.
    @PostMapping
    @Transactional
    public ResponseEntity<?> crear(@RequestBody Map<String, Object> body, Authentication auth) {
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        String titulo = (String) body.get("titulo");
        String descripcion = (String) body.get("descripcion");
        String motivo = (String) body.get("motivo");
        String organizacion = (String) body.get("organizacion");

        if (titulo == null || descripcion == null || motivo == null || organizacion == null)
            return ResponseEntity.badRequest().body("Faltan campos obligatorios");

        String tipo = body.get("tipo") != null ? body.get("tipo").toString() : "CAMPANA";
        SolicitudEntity s = new SolicitudEntity();
        s.setTipo(tipo);
        s.setEstado("PENDIENTE");
        s.setUsuario(usuario);
        s.setTitulo(titulo);
        s.setDescripcion(descripcion);
        s.setMotivo(motivo);
        s.setOrganizacion(organizacion);
        s.setFechaSolicitud(LocalDateTime.now());

        if (body.get("montoObjetivo") != null)
            s.setMontoObjetivo(new BigDecimal(body.get("montoObjetivo").toString()));
        if (body.get("fechaFin") != null)
            s.setFechaFin(LocalDate.parse(body.get("fechaFin").toString()));
        if (body.get("categoriasIds") != null)
            s.setCategoriasIds(body.get("categoriasIds").toString());
        if (body.get("imagenUrl") != null)
            s.setImagenUrl(body.get("imagenUrl").toString());

        em.persist(s);
        em.flush();

        try {
            emailService.notificarAdminNuevaSolicitud(
                    usuario.getNombre(), usuario.getEmail(), titulo, organizacion, s.getId());
        } catch (Exception e) {
            System.err.println("Error email: " + e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(toMap(s));
    }

    // GET: mis solicitudes
    @GetMapping("/mias")
    public ResponseEntity<List<Map<String, Object>>> getMias(Authentication auth) {
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        List<SolicitudEntity> lista = em.createQuery(
                "SELECT s FROM SolicitudEntity s WHERE s.usuario.id = :id ORDER BY s.fechaSolicitud DESC",
                SolicitudEntity.class).setParameter("id", usuario.getId()).getResultList();
        return ResponseEntity.ok(lista.stream().map(this::toMap).collect(Collectors.toList()));
    }

    // GET: pendientes (admin)
    @GetMapping("/pendientes")
    public ResponseEntity<List<Map<String, Object>>> getPendientes() {
        List<SolicitudEntity> lista = em.createQuery(
                "SELECT s FROM SolicitudEntity s WHERE s.estado = 'PENDIENTE' ORDER BY s.fechaSolicitud ASC",
                SolicitudEntity.class).getResultList();
        return ResponseEntity.ok(lista.stream().map(this::toMap).collect(Collectors.toList()));
    }

    // GET: todas (admin)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTodas() {
        List<SolicitudEntity> lista = em.createQuery(
                "SELECT s FROM SolicitudEntity s ORDER BY s.fechaSolicitud DESC",
                SolicitudEntity.class).getResultList();
        return ResponseEntity.ok(lista.stream().map(this::toMap).collect(Collectors.toList()));
    }

    // PUT: aprobar (admin) — crea la campaña O la categoría según el tipo
    @PutMapping("/{id}/aprobar")
    @Transactional
    public ResponseEntity<?> aprobar(@PathVariable Integer id) {
        SolicitudEntity s = em.find(SolicitudEntity.class, id);
        if (s == null) return ResponseEntity.notFound().build();
        if (!"PENDIENTE".equals(s.getEstado()))
            return ResponseEntity.badRequest().body("La solicitud ya fue resuelta");

        Integer idCreado = null;

        if ("CATEGORIA".equals(s.getTipo())) {
            // Crear la categoría
            CategoriaEntity cat = new CategoriaEntity();
            cat.setNombre(s.getTitulo());
            cat.setDescripcion(s.getDescripcion());
            CategoriaEntity creada = categoriaService.guardar(cat);
            idCreado = creada.getId();
        } else {
            // Crear la campaña
            CampaniaEntity campania = new CampaniaEntity();
            campania.setTitulo(s.getTitulo());
            campania.setDescripcionLarga(s.getDescripcion());
            campania.setMontoObjetivo(s.getMontoObjetivo());
            campania.setFechaInicio(LocalDate.now());
            campania.setFechaFin(s.getFechaFin());
            campania.setUsuarioEntity(s.getUsuario());
            if (s.getImagenUrl() != null) campania.setImagenUrl(s.getImagenUrl());

            List<CategoriaEntity> categorias = new ArrayList<>();
            if (s.getCategoriasIds() != null && !s.getCategoriasIds().isEmpty()) {
                for (String idCat : s.getCategoriasIds().split(",")) {
                    try {
                        CategoriaEntity cat = categoriaService.obtenerPorId(Integer.parseInt(idCat.trim()));
                        if (cat != null) categorias.add(cat);
                    } catch (NumberFormatException ignored) {}
                }
            }
            campania.setCategoriaEntities(categorias);

            CampaniaEntity creada = campaniaService.crearCampania(campania);
            idCreado = creada.getId();
        }

        s.setEstado("APROBADA");
        s.setIdCampanaCreada(idCreado);
        s.setFechaResolucion(LocalDateTime.now());
        em.merge(s);

        try {
            emailService.notificarAprobacion(s.getUsuario().getEmail(), s.getUsuario().getNombre(), s.getTitulo());
        } catch (Exception e) {
            System.err.println("Error email: " + e.getMessage());
        }

        Map<String, Object> resp = toMap(s);
        resp.put("idCampanaCreada", idCreado);
        return ResponseEntity.ok(resp);
    }

    // PUT: rechazar (admin)
    @PutMapping("/{id}/rechazar")
    @Transactional
    public ResponseEntity<?> rechazar(@PathVariable Integer id, @RequestBody Map<String, String> body) {
        SolicitudEntity s = em.find(SolicitudEntity.class, id);
        if (s == null) return ResponseEntity.notFound().build();
        if (!"PENDIENTE".equals(s.getEstado()))
            return ResponseEntity.badRequest().body("La solicitud ya fue resuelta");

        String motivoRechazo = body.getOrDefault("motivoRechazo", "No especificado");
        s.setEstado("RECHAZADA");
        s.setMotivoRechazo(motivoRechazo);
        s.setFechaResolucion(LocalDateTime.now());
        em.merge(s);

        try {
            emailService.notificarRechazo(s.getUsuario().getEmail(), s.getUsuario().getNombre(), s.getTitulo(), motivoRechazo);
        } catch (Exception e) {
            System.err.println("Error email: " + e.getMessage());
        }

        return ResponseEntity.ok(toMap(s));
    }

    private Map<String, Object> toMap(SolicitudEntity s) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getId());
        m.put("tipo", s.getTipo());
        m.put("estado", s.getEstado());
        m.put("titulo", s.getTitulo());
        m.put("descripcion", s.getDescripcion());
        m.put("montoObjetivo", s.getMontoObjetivo());
        m.put("fechaFin", s.getFechaFin() != null ? s.getFechaFin().toString() : null);
        m.put("categoriasIds", s.getCategoriasIds());
        m.put("motivo", s.getMotivo());
        m.put("organizacion", s.getOrganizacion());
        m.put("motivoRechazo", s.getMotivoRechazo());
        m.put("idCampanaCreada", s.getIdCampanaCreada());
        m.put("fechaSolicitud", s.getFechaSolicitud() != null ? s.getFechaSolicitud().toString() : null);
        m.put("fechaResolucion", s.getFechaResolucion() != null ? s.getFechaResolucion().toString() : null);
        if (s.getUsuario() != null) {
            m.put("nombreSolicitante", s.getUsuario().getNombre());
            m.put("emailSolicitante", s.getUsuario().getEmail());
        }
        return m;
    }
}