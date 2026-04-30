package com.example.people.service.campaing;
import com.example.people.dao.campaing.CampaniaDAO;
import com.example.people.dao.payment.PagoDAO;
import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.payment.PagosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con las campañas.
 * Utiliza los DAOs para realizar operaciones de persistencia.
 */
@Service
public class CampaniaService {

    private final CampaniaDAO campaniaDAO;
    private final PagoDAO pagoDAO;

    @Autowired
    public CampaniaService(CampaniaDAO campaniaDAO, PagoDAO pagoDAO) {
        this.campaniaDAO = campaniaDAO;
        this.pagoDAO = pagoDAO;
    }

    /**
     * Crea una nueva campaña en el sistema.
     * Valida fechas y asigna valores iniciales.
     *
     * @param campania CampaniaEntity con los datos a registrar.
     * @return La campaña creada.
     */
    public CampaniaEntity crearCampania(CampaniaEntity campania) {

        if (campania.getFechaFin().isBefore(campania.getFechaInicio())) {
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        }

        if (campania.getMontoActual() == null) {
            campania.setMontoActual(BigDecimal.ZERO);
        }

        campania.setEstado("ACTIVA");

        return campaniaDAO.save(campania);
    }

    /**
     * Actualiza una campaña existente.
     *
     * @param campania CampaniaEntity con los datos actualizados.
     * @return La campaña actualizada.
     */
    public CampaniaEntity actualizarCampania(CampaniaEntity campania) {
        return campaniaDAO.save(campania);
    }

    /**
     * Obtiene una campaña por su ID.
     *
     * @param id Identificador de la campaña.
     * @return La campaña encontrada o null si no existe.
     */
    public CampaniaEntity obtenerPorId(Integer id) {
        return campaniaDAO.findById(id);
    }

    /**
     * Obtiene todas las campañas activas según la lógica definida en el DAO.
     *
     * @return Lista de campañas activas.
     */
    public List<CampaniaEntity> listarActivas() {
        return campaniaDAO.findActivas();
    }

    /**
     * Cierra una campaña estableciendo su fecha de fin a la fecha actual.
     *
     * @param idCampania ID de la campaña a cerrar.
     */
    public void cerrarCampania(Integer idCampania) {
        campaniaDAO.cerrarCampania(idCampania);
    }

    /**
     * Calcula el total recaudado por una campaña sumando todos los pagos asociados.
     *
     * @param idCampania ID de la campaña.
     * @return Monto total recaudado como BigDecimal.
     */
    public BigDecimal calcularTotalRecaudado(Integer idCampania) {

        List<PagosEntity> pagos = pagoDAO.findByCampania(idCampania);

        return pagos.stream()
                .map(PagosEntity::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}



