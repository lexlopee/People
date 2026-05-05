package com.example.people.controller;

import com.example.people.config.JwtUtil;
import com.example.people.dto.AuthResponseDTO;
import com.example.people.dto.LoginRequestDTO;
import com.example.people.dto.UsuarioRequestDTO;
import com.example.people.entity.UsuarioEntity;
import com.example.people.service.UsuarioService;
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

        // 1. Buscamos el usuario por email
        UsuarioEntity usuario = usuarioService.obtenerPorEmail(loginDTO.getEmail());

        if (usuario == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        // 2. Verificamos la contraseña contra el hash BCrypt
        if (!passwordEncoder.matches(loginDTO.getContrasenia(), usuario.getContrasenia())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Email o contraseña incorrectos");
        }

        // 3. Generamos el token JWT
        String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol());

        // 4. Devolvemos el token y los datos básicos del usuario
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

        // Verificamos que el email no esté ya registrado
        UsuarioEntity existente = usuarioService.obtenerPorEmail(registroDTO.getEmail());
        if (existente != null) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El email ya está registrado");
        }

        // Creamos el usuario con los datos del Frontend
        UsuarioEntity nuevo = new UsuarioEntity();
        nuevo.setNombre(registroDTO.getNombre()); // Ahora sí guardamos el nombre
        nuevo.setEmail(registroDTO.getEmail());
        nuevo.setRol(registroDTO.getRol()); // Ahora sí guardamos el rol elegido
        nuevo.setFechaAlt(LocalDate.now()); // Guardamos la fecha de registro
        nuevo.setContrasenia(passwordEncoder.encode(registroDTO.getContrasenia()));

        usuarioService.registrarUsuario(nuevo);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Usuario registrado correctamente");
    }
}