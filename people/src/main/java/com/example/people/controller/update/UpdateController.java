package com.example.people.controller.update;

import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.campaingUdapte.CampaingUpdateEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.campaing.CampaniaService;
import com.example.people.service.campaingUpdate.CampaingUpdateService;
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
@RequestMapping("/api/campanias/{idCampania}/updates")
public class UpdateController {

    @Autowired private CampaingUpdateService updateService;
    @Autowired private CampaniaService campaniaService;
    @Autowired private UsuarioService usuarioService;

    // GET: listar actualizaciones de una campaña (público)
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> listar(@PathVariable Integer idCampania) {
        List<Map<String, Object>> resultado = updateService.obtenerPorCampania(idCampania)
                .stream()
                .map(u -> {
                    Map<String, Object> dto = new HashMap<>();
                    dto.put("id", u.getId());
                    dto.put("titulo", u.getTitulo() != null ? u.getTitulo() : "");
                    dto.put("descripcion", u.getDescripcion() != null ? u.getDescripcion() : "");
                    dto.put("fechaPublicacion", u.getFechaPublicacion() != null ? u.getFechaPublicacion().toString() : "");
                    dto.put("version", u.getVersion() != null ? u.getVersion() : "");
                    String nombre = "Equipo";
                    try {
                        if (u.getUsuario() != null) nombre = u.getUsuario().getNombre();
                    } catch (Exception ignored) {}
                    dto.put("nombreAutor", nombre);
                    return dto;
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(resultado);
    }

    // POST: publicar actualización (solo creador dueño o admin)
    @PostMapping
    public ResponseEntity<?> publicar(
            @PathVariable Integer idCampania,
            @RequestBody Map<String, String> body,
            Authentication auth) {

        String titulo = body.get("titulo");
        String descripcion = body.get("descripcion");

        if (titulo == null || titulo.trim().isEmpty())
            return ResponseEntity.badRequest().body("El título no puede estar vacío");
        if (descripcion == null || descripcion.trim().isEmpty())
            return ResponseEntity.badRequest().body("La descripción no puede estar vacía");

        CampaniaEntity campania = campaniaService.obtenerPorId(idCampania);
        if (campania == null) return ResponseEntity.notFound().build();

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        if (usuario == null) return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

        // Solo el creador de la campaña o un admin puede publicar actualizaciones
        boolean esAdmin = "administrador".equals(usuario.getRol());
        boolean esDueno = campania.getUsuarioEntity() != null
                && campania.getUsuarioEntity().getId().equals(usuario.getId());

        if (!esAdmin && !esDueno)
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Solo el creador de la campaña puede publicar actualizaciones");

        CampaingUpdateEntity update = new CampaingUpdateEntity();
        update.setTitulo(titulo.trim());
        update.setDescripcion(descripcion.trim());
        update.setFechaPublicacion(LocalDate.now());
        update.setFechaSolicitud(LocalDate.now());
        update.setCampaniaEntity(campania);
        update.setUsuario(usuario);

        CampaingUpdateEntity guardado = updateService.guardar(update);

        Map<String, Object> respuesta = new HashMap<>();
        respuesta.put("id", guardado.getId());
        respuesta.put("titulo", guardado.getTitulo());
        respuesta.put("descripcion", guardado.getDescripcion());
        respuesta.put("fechaPublicacion", guardado.getFechaPublicacion().toString());
        respuesta.put("nombreAutor", usuario.getNombre());
        respuesta.put("version", guardado.getVersion() != null ? guardado.getVersion() : "");

        return ResponseEntity.status(HttpStatus.CREATED).body(respuesta);
    }

    // DELETE: borrar actualización (solo dueño o admin)
    @DeleteMapping("/{idUpdate}")
    public ResponseEntity<?> borrar(
            @PathVariable Integer idCampania,
            @PathVariable Integer idUpdate,
            Authentication auth) {

        CampaingUpdateEntity update = updateService.obtenerPorId(idUpdate);
        if (update == null) return ResponseEntity.notFound().build();

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(auth.getName());
        boolean esAdmin = "administrador".equals(usuario.getRol());
        boolean esDueno = update.getUsuario() != null
                && update.getUsuario().getId().equals(usuario.getId());

        if (!esAdmin && !esDueno)
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Sin permisos");

        updateService.eliminar(idUpdate);
        return ResponseEntity.ok("Actualización eliminada");
    }
}