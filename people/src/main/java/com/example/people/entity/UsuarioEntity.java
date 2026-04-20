package com.example.people.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "usuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_rango")
    private RangoEntity rango;


    private String nombre;
    private String email;

    @Column(name = "contraseña")
    private String contrasenia;

    private String rol;

    @Column(name = "fecha_alt")
    private LocalDate fechaAlt;

    @Column(name = "fecha_baja")
    private LocalDate fechaBaja;
}
