package com.example.people.entity.payment;

import com.example.people.entity.transacction.TransaccionEntity;
import com.example.people.entity.user.UsuarioEntity;
import com.example.people.entity.campaing.CampaniaEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "donacion", schema = "people")
public class PagosEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_donacion")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_donante")
    private UsuarioEntity donante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_campaña")
    private CampaniaEntity campania;

    private BigDecimal monto;

    @Column(name = "fecha_pago")
    private LocalDate fechaPago;

    @Column(name = "estado_pago")
    private String estadoPago;

    @Column(name = "comision_plataforma")
    private BigDecimal comisionPlataforma;

    private String divisa;

    @Column(name = "id_transaccion_pasarela")
    private String idTransaccionPasarela;


    @OneToMany(mappedBy = "donacion", cascade = CascadeType.ALL)
    private List<TransaccionEntity> transacciones;
}
