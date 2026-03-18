package com.example.people.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "campaña")
public class CampaniaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_campaña")
    private Integer id;
    private String titulo;
    @Column(name = "descripcion_larga")
    private String descripcionLarga;
    @Column(name = "monto_objetivo")
    private BigDecimal montoObjetivo;
    @Column(name = "monto_actual")
    private BigDecimal montoActual;
    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;
    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuarioEntity;

    @ManyToMany
    @JoinTable(
            name = "campaña_categoria",
            joinColumns = @JoinColumn(name = "id_campaña"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    private List<CategoriaEntity> categoriaEntities;
}
