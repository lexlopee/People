package com.example.people.entity.comments;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "comentario_likes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComentarioLikeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_like")
    private Integer id;

    @Column(name = "id_comentario")
    private Integer idComentario;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    private LocalDate fecha;
}