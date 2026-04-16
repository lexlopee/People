package com.example.people.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UpdateResponseDTO {
    private Integer idUpdate;
    private String titulo;
    private String descripcion;
    private LocalDateTime fechaPublicacion;
}
