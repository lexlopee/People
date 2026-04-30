package com.example.people.entity.documents;

import com.example.people.entity.organizacion.OrganizacionEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "documento_validacion")
public class DocumentoValidacionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_documento")
    private Integer id;
    @Column(name = "tipo_documento")
    private String tipoDocumento;
    @Column(name = "url_archivo")
    private String urlArchivo;
    @Column(name = "estado_validacion")
    private String estadoValidacion;
    @Column(name = "fecha_subida")
    private Date fechaSubida;

    @ManyToOne
    @JoinColumn(name = "id_organizacion")
    private OrganizacionEntity organizacionEntity;
}
