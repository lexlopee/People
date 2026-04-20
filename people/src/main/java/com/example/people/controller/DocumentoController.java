package com.example.people.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/documentos")
public class DocumentoController {

    @PostMapping("/subir")
    public ResponseEntity<String> subirDocumento(
            @RequestParam("file") MultipartFile file,
            @RequestParam("idOrganizacion") Integer idOrganizacion,
            @RequestParam("tipoDocumento") String tipoDocumento) {

        // Aquí iría la lógica para guardar el archivo en el servidor o AWS S3
        // y luego guardar el registro en DocumentoValidacionEntity
        return ResponseEntity.ok("Documento subido correctamente para revisión.");
    }
}