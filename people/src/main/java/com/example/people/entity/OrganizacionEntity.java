package com.example.people.entity;

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
@Table(name = "organizacion_promotora")
public class OrganizacionEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_organizacion")
    private Integer id;

    @Column(name = "tipo_entidad")
    private String tipoEntidad;
    @Column(name = "razon_social")
    private String razonSocial;
    @Column(name = "cif_nif")
    private String cifNif;
    @Column(name = "representante_legal")
    private String representanteLegal;
    @Column(name = "dni_representante")
    private String dniRepresentante;
    private String iban;
    @Column(name = "swift_bic")
    private String swiftBic;
    @Column(name = "email_stripe")
    private String emailStripe;
    @Column(name = "direccion_fiscal")
    private String direccionFiscal;
    private String ciudad;
    @Column(name = "codigo_postal")
    private String codigoPostal;
    private String pais;
    @Column(name = "estado_verificacion")
    private String estadoVerificacion;

    @OneToOne
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    @OneToMany(mappedBy = "organizacionEntity", cascade = CascadeType.ALL)
    private List<DocumentoValidacionEntity> documentos;


}
