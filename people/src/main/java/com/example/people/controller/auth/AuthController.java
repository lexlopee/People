package com.example.people.controller.auth;

import com.example.people.config.jwt.JwtUtil;
import com.example.people.dto.auth.AuthResponseDTO;
import com.example.people.dto.login.LoginRequestDTO;
import com.example.people.dto.user.UsuarioRequestDTO;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.service.user.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginDTO) {

        UsuarioEntity usuario = usuarioService.obtenerPorEmail(loginDTO.getEmail());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        if (!passwordEncoder.matches(loginDTO.getContrasenia(), usuario.getContrasenia())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        AuthResponseDTO response = new AuthResponseDTO(
                token,
                usuario.getId(),
                usuario.getEmail(),
                usuario.getRol()
        );

        return ResponseEntity.ok(response);
    }

    @PostMapping("/registro")
    public ResponseEntity<?> registro(@Valid @RequestBody UsuarioRequestDTO registroDTO) {

        // 👇 LOG para ver exactamente qué llega del frontend
        System.out.println("=== REGISTRO RECIBIDO ===");
        System.out.println("Nombre: " + registroDTO.getNombre());
        System.out.println("Email:  " + registroDTO.getEmail());
        System.out.println("Rol:    " + registroDTO.getRol());
        System.out.println("========================");

        try {
            UsuarioEntity existente = usuarioService.obtenerPorEmail(registroDTO.getEmail());
            if (existente != null) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body("El email ya está registrado");
            }

            UsuarioEntity nuevo = new UsuarioEntity();
            nuevo.setNombre(registroDTO.getNombre());
            nuevo.setEmail(registroDTO.getEmail());
            nuevo.setRol(registroDTO.getRol().toLowerCase()); // ✅ siempre minúsculas
            nuevo.setFechaAlt(LocalDate.now());
            nuevo.setContrasenia(passwordEncoder.encode(registroDTO.getContrasenia()));

            usuarioService.registrarUsuario(nuevo);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Usuario registrado correctamente");

        } catch (Exception e) {
            // 👇 Devuelve el error real al frontend para que puedas verlo
            System.err.println("=== ERROR EN REGISTRO ===");
            System.err.println(e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error detallado: " + e.getMessage() +
                            (e.getCause() != null ? " | Causa: " + e.getCause().getMessage() : ""));
        }
    }
}