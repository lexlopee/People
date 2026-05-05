package com.example.people.entity.campaingUdapte;

import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.user.UsuarioEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "campaign_updates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CampaingUpdateEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_update")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_campaña")
    private CampaniaEntity campaniaEntity;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_usuario")
    private UsuarioEntity usuario;

    private String titulo;
    private String descripcion;

    @Column(name = "fecha_publicacion")
    private LocalDate fechaPublicacion;

    @Column(name = "fecha_solicitud")
    private LocalDate fechaSolicitud;

    private String version;
}
