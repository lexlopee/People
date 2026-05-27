package com.example.people.controller.comments;

import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.comments.ComentariosEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.comment.ComentariosService;
import com.example.people.service.user.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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

    // GET: listar comentarios (público)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(@PathVariable Integer idCampania) {
        List<Map<String, Object>> resultado = comentariosService.obtenerPorCampania(idCampania)
                .stream().map(c -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", c.getId());
                    dto.put("contenido", c.getContenido() != null ? c.getContenido() : "");
                    dto.put("fecha", c.getFecha() != null ? c.getFecha().toString() : "");
                    dto.put("likes", c.getLikes() != null ? c.getLikes() : 0);
                    String nombre = "Anonimo";
                    String inicial = "A";
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
        respuesta.put("nombreUsuario", usuario.getNombre());
        respuesta.put("inicial", String.valueOf(usuario.getNombre().charAt(0)).toUpperCase());

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // POST: dar like a un comentario (cualquier usuario autenticado)
    @PostMapping("/{idComentario}/like")
    public ResponseEntity<?> darLike(
            @PathVariable Integer idCampania,
            @PathVariable Integer idComentario) {

        ComentariosEntity comentario = comentariosService.obtenerPorId(idComentario);
        if (comentario == null) return ResponseEntity.notFound().build();

        int likesActuales = comentario.getLikes() != null ? comentario.getLikes() : 0;
        comentario.setLikes(likesActuales + 1);
        comentariosService.guardar(comentario);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", comentario.getId());
        respuesta.put("likes", comentario.getLikes());
        return ResponseEntity.ok(respuesta);
    }
}