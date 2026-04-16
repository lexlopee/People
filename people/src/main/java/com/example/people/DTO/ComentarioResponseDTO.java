package com.example.people.DTO;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ComentarioResponseDTO {
    private Integer idComentario;
    private String contenido;
    private LocalDateTime fecha;
    private String nombreUsuario;
}
