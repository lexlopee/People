package com.example.people.service;

import com.example.people.dao.CobroDAO;
import com.example.people.entity.CobroEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Servicio encargado de gestionar los cobros (retiros de fondos) asociados
 * a las campañas dentro de la plataforma de crowdfunding.
 *
 * Permite registrar solicitudes de cobro, consultar cobros pendientes,
 * completados y obtener información relacionada con transferencias.
 */
@Service
public class CobroService {

    private final CobroDAO cobroDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param cobroDAO DAO encargado del acceso a datos de cobros.
     */
    public CobroService(CobroDAO cobroDAO) {
        this.cobroDAO = cobroDAO;
    }

    /**
     * Obtiene un cobro por su identificador único.
     *
     * @param id Identificador del cobro.
     * @return El cobro encontrado o null si no existe.
     */
    public CobroEntity obtenerPorId(Integer id) {
        return cobroDAO.findById(id);
    }

    /**
     * Guarda o actualiza un cobro.
     *
     * @param cobro Entidad de cobro a persistir.
     * @return El cobro guardado o actualizado.
     */
    public CobroEntity guardar(CobroEntity cobro) {
        return cobroDAO.save(cobro);
    }

    /**
     * Elimina un cobro por su identificador.
     *
     * @param id Identificador del cobro.
     */
    public void eliminar(Integer id) {
        cobroDAO.delete(id);
    }

    /**
     * Obtiene todos los cobros asociados a una campaña concreta.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de cobros.
     */
    public List<CobroEntity> obtenerPorCampania(Integer idCampania) {
        return cobroDAO.findByCampania(idCampania);
    }

    /**
     * Obtiene todos los cobros pendientes (sin fecha de transferencia).
     *
     * @return Lista de cobros pendientes.
     */
    public List<CobroEntity> obtenerPendientes() {
        return cobroDAO.findPendientes();
    }

    /**
     * Obtiene todos los cobros completados (con fecha de transferencia).
     *
     * @return Lista de cobros completados.
     */
    public List<CobroEntity> obtenerCompletados() {
        return cobroDAO.findCompletados();
    }

    /**
     * Obtiene los cobros solicitados en una fecha específica.
     *
     * @param fecha Fecha de solicitud.
     * @return Lista de cobros solicitados ese día.
     */
    public List<CobroEntity> obtenerPorFechaSolicitud(Date fecha) {
        return cobroDAO.findByFechaSolicitud(fecha);
    }

    /**
     * Obtiene los cobros enviados a una cuenta bancaria específica.
     *
     * @param cuentaDestino Número de cuenta destino.
     * @return Lista de cobros asociados a esa cuenta.
     */
    public List<CobroEntity> obtenerPorCuentaDestino(String cuentaDestino) {
        return cobroDAO.findByCuentaDestino(cuentaDestino);
    }

    /**
     * Verifica si una campaña tiene un cobro pendiente.
     *
     * @param idCampania Identificador de la campaña.
     * @return true si existe un cobro pendiente, false en caso contrario.
     */
    public boolean existeCobroPendiente(Integer idCampania) {
        return cobroDAO.existeCobroPendiente(idCampania);
    }
}

