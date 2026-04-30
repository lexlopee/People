package com.example.people.dto.organizacion;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizacionRequestDTO {
    @NotBlank(message = "La razón social es obligatoria")
    private String razonSocial;

    @NotBlank(message = "El CIF/NIF es obligatorio")
    private String cifNif;

    @NotBlank(message = "La dirección fiscal es obligatoria")
    private String direccionFiscal;
}
