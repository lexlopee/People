package com.example.people.entity.rank;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rango", schema = "people")
public class RangoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_rango")
    private Integer id;

    private String nombre;
    private String descripcion;

    private Integer nivel;

    @Column(name = "minimo_aportado")
    private BigDecimal minimoAportado;

    @Column(name = "minimo_donaciones")
    private BigDecimal minimoDonaciones;
}
