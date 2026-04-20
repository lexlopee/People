package com.example.people.controller;

import com.example.people.dto.AuthResponseDTO;
import com.example.people.dto.LoginRequestDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // @Autowired
    // private AuthService authService; // Necesitarás crear este servicio para la lógica de JWT

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginDTO) {
        // Aquí iría la lógica para verificar email y contraseña en la BD
        // y generar el token JWT. Por ahora devolvemos un DTO simulado:
        AuthResponseDTO response = new AuthResponseDTO("token_simulado_123", 1, loginDTO.getEmail(), "DONANTE");
        return ResponseEntity.ok(response);
    }
}
