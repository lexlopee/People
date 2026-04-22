package com.example.people.dto;

import lombok.Data;

@Data
public class DocumentoValidacionDTO {
    private Integer idDocumento;
    private Integer idOrganizacion;
    private String tipoDocumento; // Ej: "DNI_REPRESENTANTE", "CIF_EMPRESA"
    private String rutaArchivo;   // La URL de donde se guardó en el servidor/nube
    private String estadoValidacion; // Ej: "PENDIENTE", "APROBADO", "RECHAZADO"
}