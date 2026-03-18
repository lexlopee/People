package com.example.people.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Data
@Table(name = "cobro")
public class CobroEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cobro")
    private Integer id;
    @Column(name = "monto_neto")
    private BigDecimal montoNeto;
    @Column(name = "fecha_solicitud")
    private Date fechaSolicitud;
    @Column(name = "fecha_transferencia")
    private Date fechaTransferencia;
    @Column(name = "cuenta_destino")
    private String cuentaDestino;
    @Column(name = "comprobante_pago")
    private String comprobantePago;

    @OneToOne
    @JoinColumn(name = "id_campaña")
    private CampaniaEntity campaniaEntity;
}
