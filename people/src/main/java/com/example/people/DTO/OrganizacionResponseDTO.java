package com.example.people.DTO;

import lombok.Data;

@Data
public class OrganizacionResponseDTO {
    private Integer idOrganizacion;
    private String razonSocial;
    private String cifNif;
    private String estadoVerificacion;
}
