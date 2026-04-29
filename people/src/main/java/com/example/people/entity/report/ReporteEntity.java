package com.example.people.entity.report;

import com.example.people.entity.user.UsuarioEntity;
import com.example.people.entity.campaing.CampaniaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "reportes")
public class ReporteEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campaña", nullable = false)
    private CampaniaEntity campania;

    private String motivo;
    private String descripcion;
    private String estado;

    @Column(name = "fecha_reporte")
    private LocalDate fechaReporte = LocalDate.now();
}
