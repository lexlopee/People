package com.example.people.controller.organizacion;

import com.example.people.dto.organizacion.OrganizacionRequestDTO;
import com.example.people.service.organizacion.OrganizacionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/organizaciones")
public class OrganizacionController {

    @Autowired
    private OrganizacionService organizacionService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarOrganizacion(@Valid @RequestBody OrganizacionRequestDTO dto) {
        return ResponseEntity.ok("Organización registrada. Pendiente de verificación de documentos.");
    }
}