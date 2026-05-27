package com.example.people.controller.comments;

import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.comments.ComentariosEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.comment.ComentariosService;
import com.example.people.service.user.UsuarioService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campanias/{idCampania}/comentarios")
public class ComentarioController {

    @Autowired private ComentariosService comentariosService;
    @Autowired private CampaniaService campaniaService;
    @Autowired private UsuarioService usuarioService;

    @PersistenceContext
    private EntityManager entityManager;

    // GET: listar comentarios con likes (público)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(
            @PathVariable Integer idCampania,
            Authentication auth) {

        // Si hay usuario logueado, marcamos qué comentarios ya le dio like
        Integer idUsuarioActual = null;
        if (auth != null && auth.isAuthenticated()) {
            UsuarioEntity u = usuarioService.obtenerPorEmail(auth.getName());
            if (u != null) idUsuarioActual = u.getId();
        }

        final Integer idUsuarioFinal = idUsuarioActual;

        List<Map<String, Object>> resultado = comentariosService.obtenerPorCampania(idCampania)
                .stream().map(c -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", c.getId());
                    dto.put("contenido", c.getContenido() != null ? c.getContenido() : "");
                    dto.put("fecha", c.getFecha() != null ? c.getFecha().toString() : "");
                    dto.put("likes", c.getLikes() != null ? c.getLikes() : 0);

                    // ¿Ya dio like este usuario?
                    boolean yaLeDioLike = false;
                    if (idUsuarioFinal != null) {
                        Long count = (Long) entityManager.createQuery(
                                        "SELECT COUNT(cl) FROM ComentarioLikeEntity cl WHERE cl.idComentario = :idC AND cl.idUsuario = :idU"
                                ).setParameter("idC", c.getId())
                                .setParameter("idU", idUsuarioFinal)
                                .getSingleResult();
                        yaLeDioLike = count > 0;
                    }
                    dto.put("yaLeDioLike", yaLeDioLike);

                    String nombre = "Anonimo"; String inicial = "A";
                    try {
                        if (c.getUsuario() != null) {
                            nombre = c.getUsuario().getNombre();
                            inicial = nombre.length() > 0 ? String.valueOf(nombre.charAt(0)).toUpperCase() : "A";
                        }
                    } catch (Exception ignored) {}
                    dto.put("nombreUsuario", nombre);
                    dto.put("inicial", inicial);
                    return dto;
                }).collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // POST: crear comentario
    @PostMapping
    public ResponseEntity<?> crear(
            @PathVariable Integer idCampania,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        String contenido = body.get("contenido");
        if (contenido == null || contenido.trim().isEmpty())
            return ResponseEntity.badRequest().body("El comentario no puede estar vacio");

        CampaniaEntity campania = campaniaService.obtenerPorId(idCampania);
        if (campania == null) return ResponseEntity.notFound().build();

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        ComentariosEntity comentario = new ComentariosEntity();
        comentario.setContenido(contenido.trim());
        comentario.setFecha(LocalDate.now());
        comentario.setCampaniaEntity(campania);
        comentario.setUsuario(usuario);
        comentario.setLikes(0);

        ComentariosEntity guardado = comentariosService.guardar(comentario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", guardado.getId());
        respuesta.put("contenido", guardado.getContenido());
        respuesta.put("fecha", guardado.getFecha().toString());
        respuesta.put("likes", 0);
        respuesta.put("yaLeDioLike", false);
        respuesta.put("nombreUsuario", usuario.getNombre());
        respuesta.put("inicial", String.valueOf(usuario.getNombre().charAt(0)).toUpperCase());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // POST: dar like — solo 1 por usuario por comentario
    @PostMapping("/{idComentario}/like")
    @Transactional
    public ResponseEntity<?> darLike(
            @PathVariable Integer idCampania,
            @PathVariable Integer idComentario,
            Authentication auth) {

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        ComentariosEntity comentario = comentariosService.obtenerPorId(idComentario);
        if (comentario == null) return ResponseEntity.notFound().build();

        // Comprobar si ya dio like
        Long yaExiste = (Long) entityManager.createQuery(
                        "SELECT COUNT(cl) FROM ComentarioLikeEntity cl WHERE cl.idComentario = :idC AND cl.idUsuario = :idU"
                ).setParameter("idC", idComentario)
                .setParameter("idU", usuario.getId())
                .getSingleResult();

        if (yaExiste > 0) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ya diste like a este comentario", "likes", comentario.getLikes()));
        }

        // Guardar el like en la tabla de control
        entityManager.createNativeQuery(
                        "INSERT INTO people.comentario_likes (id_comentario, id_usuario, fecha) VALUES (?, ?, ?)"
                ).setParameter(1, idComentario)
                .setParameter(2, usuario.getId())
                .setParameter(3, LocalDate.now())
                .executeUpdate();

        // Incrementar contador
        int likesActuales = comentario.getLikes() != null ? comentario.getLikes() : 0;
        comentario.setLikes(likesActuales + 1);
        comentariosService.guardar(comentario);

        return ResponseEntity.ok(Map.of(
                "id", comentario.getId(),
                "likes", comentario.getLikes(),
                "yaLeDioLike", true
        ));
    }

    // DELETE: solo administrador puede borrar comentarios
    @DeleteMapping("/{idComentario}")
    @Transactional
    public ResponseEntity<?> borrar(
            @PathVariable Integer idCampania,
            @PathVariable Integer idComentario,
            Authentication auth) {

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // Solo admin puede borrar
        if (!"administrador".equals(usuario.getRol()))
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Solo el administrador puede eliminar comentarios");

        ComentariosEntity comentario = comentariosService.obtenerPorId(idComentario);
        if (comentario == null) return ResponseEntity.notFound().build();

        comentariosService.eliminar(idComentario);
        return ResponseEntity.ok("Comentario eliminado");
    }
}