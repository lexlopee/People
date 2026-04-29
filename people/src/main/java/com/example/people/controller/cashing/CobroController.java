package com.example.people.controller.cashing;

import com.example.people.dto.cashing.CobroRequestDTO;
import com.example.people.service.cashing.CobroService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cobros")
public class CobroController {

    @Autowired
    private CobroService cobroService;

    @PostMapping("/solicitar")
    public ResponseEntity<String> solicitarCobro(@Valid @RequestBody CobroRequestDTO cobroDTO) {
        // Lógica para validar que la campaña tiene fondos suficientes,
        // pertenece al usuario que lo solicita, y crear el registro de COBRO.
        return ResponseEntity.ok("Solicitud de cobro registrada. En proceso de transferencia.");
    }
}