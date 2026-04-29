package com.example.people.service.payment;

import com.example.people.dao.campaing.CampaniaDAO;
import com.example.people.dao.payment.PagoDAO;
import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.payment.PagosEntity;
import com.example.people.entity.user.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;


/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los pagos
 * dentro de la plataforma de crowdfunding.
 */
@Service
public class PagoService {

    private final PagoDAO pagoDAO;
    private final CampaniaDAO campaniaDAO;

    @Autowired
    public PagoService(PagoDAO pagoDAO, CampaniaDAO campaniaDAO) {
        this.pagoDAO = pagoDAO;
        this.campaniaDAO = campaniaDAO;
    }

    /**
     * Registra un nuevo pago asociado a una campaña.
     * Valida que el usuario no aporte a su propia campaña,
     * actualiza el monto recaudado y guarda el pago.
     *
     * @param pago Entidad PagosEntity con los datos del aporte.
     * @return El pago registrado.
     */
    public PagosEntity registrarPago(PagosEntity pago) {

        CampaniaEntity campania = campaniaDAO.findById(pago.getCampania().getId());
        UsuarioEntity donante = pago.getDonante();

        if (campania == null) {
            throw new IllegalArgumentException("La campaña no existe");
        }

        if (donante == null) {
            throw new IllegalArgumentException("El pago debe tener un donante válido");
        }

        if (campania.getUsuarioEntity().getId().equals(donante.getId())) {
            throw new IllegalArgumentException("Un usuario no puede donar a su propia campaña");
        }

        pago.setFechaPago(LocalDate.now());
        pago.setEstadoPago("COMPLETADO");

        PagosEntity pagoGuardado = pagoDAO.save(pago);

        BigDecimal nuevoMonto = campania.getMontoActual().add(pago.getMonto());
        campania.setMontoActual(nuevoMonto);

        campaniaDAO.save(campania);

        return pagoGuardado;
    }

    /**
     * Obtiene un pago por su ID.
     *
     * @param id Identificador del pago.
     * @return El pago encontrado o null si no existe.
     */
    public PagosEntity obtenerPagoPorId(Integer id) {
        return pagoDAO.findById(id);
    }

    /**
     * Elimina un pago por su ID.
     *
     * @param id Identificador del pago.
     */
    public void eliminarPago(Integer id) {
        pagoDAO.delete(id);
    }

    /**
     * Calcula el total recaudado por una campaña.
     *
     * @param idCampania ID de la campaña.
     * @return Monto total recaudado.
     */
    public BigDecimal obtenerTotalRecaudado(Integer idCampania) {

        return pagoDAO.findByCampania(idCampania).stream()
                .map(PagosEntity::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Verifica si un usuario ya ha realizado un aporte a una campaña.
     *
     * @param idUsuario ID del usuario.
     * @param idCampania ID de la campaña.
     * @return true si ya aportó, false si no.
     */
    public boolean usuarioHaPagado(Integer idUsuario, Integer idCampania) {
        return pagoDAO.usuarioHaPagado(idUsuario, idCampania);
    }
}

