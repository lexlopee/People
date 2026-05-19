package com.example.people.controller.donations;

import com.example.people.dto.donations.DonacionRequestDTO;
import com.example.people.dto.donations.DonacionResponseDTO;
import com.example.people.service.payment.PagoService;
import com.paypal.orders.Order;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
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

    @PostMapping("/crear-orden")
    public ResponseEntity<String> crearOrden(@RequestBody DonacionRequestDTO dto) throws IOException {
        String orderId = pagoService.crearOrden(dto.getMonto()); // 👈 minúscula
        return ResponseEntity.ok(orderId);
    }

    @PostMapping("/capturar")
    public ResponseEntity<?> capturarPago(@RequestParam String orderId,
                                          @RequestBody DonacionRequestDTO dto) throws IOException {
        Order order = pagoService.capturarPago(orderId); // 👈 minúscula
        if ("COMPLETED".equals(order.status())) {
            return ResponseEntity.ok("Donación registrada");
        }
        return ResponseEntity.badRequest().body("El pago no se completó");
    }
}