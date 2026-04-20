package com.example.people.controller;

import com.example.people.dto.DonacionRequestDTO;
import com.example.people.dto.DonacionResponseDTO;
import com.example.people.service.PagoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/donaciones")
public class DonacionController {

    @Autowired
    private PagoService pagoService;

    @PostMapping("/realizar")
    public ResponseEntity<String> realizarDonacion(@Valid @RequestBody DonacionRequestDTO donacionDTO) {
        // 1. Procesar el pago con Stripe/PayPal usando el token
        // 2. Guardar el registro en la tabla DONACION / TRANSACCION
        // 3. Actualizar el "monto_actual" de la campaña
        return ResponseEntity.ok("Donación procesada con éxito. ¡Gracias!");
    }

    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<DonacionResponseDTO>> historialDonacionesUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}