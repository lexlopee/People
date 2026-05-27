package com.example.people.entity.comments;

import com.example.people.entity.user.UsuarioEntity;
import com.example.people.entity.campaing.CampaniaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "comentarios")
public class ComentariosEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comentario")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campaña")
    private CampaniaEntity campaniaEntity;

    private String contenido;
    private LocalDate fecha;

    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer likes = 0;
}