package com.example.people.entity.category;

import com.example.people.entity.campaing.CampaniaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "categoria")
public class CategoriaEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer id;

    private String nombre;
    private String descripcion;

    @ManyToMany(mappedBy = "categorias")
    private List<CampaniaEntity> campaniaEntities;
}
