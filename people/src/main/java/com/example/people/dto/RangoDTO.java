package com.example.people.dto;

import lombok.Data;

@Data
public class RangoDTO {
    private Integer idRango;
    private Integer nivel;
    private String nombre;
    private String descripcion;
    private Integer minimoAportado;
    private Integer minimoDonaciones;
}