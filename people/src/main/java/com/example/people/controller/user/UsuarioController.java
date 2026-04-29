package com.example.people.controller.user;

import com.example.people.dto.user.UsuarioRequestDTO;
import com.example.people.dto.user.UsuarioResponseDTO;
import com.example.people.service.user.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsuario(@Valid @RequestBody UsuarioRequestDTO requestDTO) {
        // Convertir RequestDTO a UsuarioEntity y pasarlo a usuarioService.registrar()
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil(@PathVariable Integer id) {
        // Buscar en la BD con usuarioService.obtenerPorId(id)
        // Convertir Entity a ResponseDTO y devolverlo
        UsuarioResponseDTO response = new UsuarioResponseDTO();
        // response.setIdUsuario(...); etc.
        return ResponseEntity.ok(response);
    }
}