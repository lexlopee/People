package com.example.people.dao.cashing;

import com.example.people.entity.cashing.CobroEntity;

import java.util.Date;
import java.util.List;

/**
 * DAO para gestionar los cobros (retiros de fondos) asociados a las campañas.
 * Permite registrar solicitudes de retiro, consultar cobros pendientes,
 * completados y obtener información relacionada con las transferencias.
 */
public interface CobroDAO {

    /**
     * Busca un cobro por su ID.
     *
     * @param id Identificador del cobro.
     * @return El cobro encontrado o null si no existe.
     */
    CobroEntity findById(Integer id);

    /**
     * Guarda un nuevo cobro o actualiza uno existente.
     *
     * @param cobro Entidad de cobro a persistir.
     * @return El cobro guardado.
     */
    CobroEntity save(CobroEntity cobro);

    /**
     * Elimina un cobro por su ID.
     *
     * @param id Identificador del cobro.
     */
    void delete(Integer id);

    /**
     * Obtiene todos los cobros asociados a una campaña.
     *
     * @param idCampania ID de la campaña.
     * @return Lista de cobros.
     */
    List<CobroEntity> findByCampania(Integer idCampania);

    /**
     * Obtiene todos los cobros pendientes (sin fecha de transferencia).
     *
     * @return Lista de cobros pendientes.
     */
    List<CobroEntity> findPendientes();

    /**
     * Obtiene todos los cobros completados (con fecha de transferencia).
     *
     * @return Lista de cobros completados.
     */
    List<CobroEntity> findCompletados();

    /**
     * Obtiene los cobros solicitados en una fecha específica.
     *
     * @param fecha Fecha de solicitud.
     * @return Lista de cobros.
     */
    List<CobroEntity> findByFechaSolicitud(Date fecha);

    /**
     * Obtiene los cobros enviados a una cuenta bancaria específica.
     *
     * @param cuentaDestino Número de cuenta destino.
     * @return Lista de cobros.
     */
    List<CobroEntity> findByCuentaDestino(String cuentaDestino);

    /**
     * Verifica si una campaña tiene un cobro pendiente.
     *
     * @param idCampania ID de la campaña.
     * @return true si existe un cobro pendiente, false en caso contrario.
     */
    boolean existeCobroPendiente(Integer idCampania);
}
