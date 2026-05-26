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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/campanias/{idCampania}/comentarios")
public class ComentarioController {

    @Autowired private ComentariosService comentariosService;
    @Autowired private CampaniaService campaniaService;
    @Autowired private UsuarioService usuarioService;

    // GET: listar comentarios de una campaña (público)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(@PathVariable Integer idCampania) {
        List<Map<String, Object>> lista = comentariosService.obtenerPorCampania(idCampania)
                .stream()
                .map(c -> Map.<String, Object>of(
                        "id", c.getId(),
                        "contenido", c.getContenido(),
                        "fecha", c.getFecha() != null ? c.getFecha().toString() : "",
                        "nombreUsuario", c.getUsuario() != null ? c.getUsuario().getNombre() : "Anónimo",
                        "inicial", c.getUsuario() != null ? String.valueOf(c.getUsuario().getNombre().charAt(0)).toUpperCase() : "A"
                ))
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    // POST: crear comentario (requiere login)
    @PostMapping
    public ResponseEntity<?> crear(
            @PathVariable Integer idCampania,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        String contenido = body.get("contenido");
        if (contenido == null || contenido.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("El comentario no puede estar vacío");
        }

        CampaniaEntity campania = campaniaService.obtenerPorId(idCampania);
        if (campania == null) return ResponseEntity.notFound().build();

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        ComentariosEntity comentario = new ComentariosEntity();
        comentario.setContenido(contenido.trim());
        comentario.setFecha(LocalDate.now());
        comentario.setCampaniaEntity(campania);
        comentario.setUsuario(usuario);

        ComentariosEntity guardado = comentariosService.guardar(comentario);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "id", guardado.getId(),
                "contenido", guardado.getContenido(),
                "fecha", guardado.getFecha().toString(),
                "nombreUsuario", usuario.getNombre(),
                "inicial", String.valueOf(usuario.getNombre().charAt(0)).toUpperCase()
        ));
    }
}