package com.example.people.controller;

import com.example.people.dto.UpdateRequestDTO;
import com.example.people.dto.UpdateResponseDTO;
import com.example.people.service.CampaingUpdateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/updates")
public class UpdateController {

    @Autowired
    private CampaingUpdateService updateService;

    @PostMapping("/publicar")
    public ResponseEntity<String> publicarActualizacion(@Valid @RequestBody UpdateRequestDTO requestDTO) {
        return ResponseEntity.ok("Actualización publicada para los donantes");
    }

    @GetMapping("/campania/{idCampania}")
    public ResponseEntity<List<UpdateResponseDTO>> listarUpdatesCampania(@PathVariable Integer idCampania) {
        return ResponseEntity.ok(new ArrayList<>());
    }
}