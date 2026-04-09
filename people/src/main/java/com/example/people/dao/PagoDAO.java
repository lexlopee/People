package com.example.people.dao;

import com.example.people.entity.PagosEntity;
import java.util.List;

/**
 * Interfaz DAO para gestionar los pagos dentro de la plataforma de crowdfunding.
 * Define las operaciones de acceso a datos relacionadas con aportes económicos.
 */
public interface PagoDAO {

    /**
     * Busca un pago por su ID.
     *
     * @param id Identificador del pago.
     * @return El pago encontrado o null si no existe.
     */
    PagosEntity findById(Integer id);

    /**
     * Guarda un nuevo pago o actualiza uno existente.
     *
     * @param pago Entidad Pago a persistir.
     * @return El pago guardado.
     */
    PagosEntity save(PagosEntity pago);

    /**
     * Obtiene todos los pagos realizados por un usuario.
     *
     * @param idUsuario ID del usuario.
     * @return Lista de pagos.
     */
    List<PagosEntity> findByUsuario(Integer idUsuario);

    /**
     * Obtiene todos los pagos realizados a un proyecto.
     *
     * @param idProyecto ID del proyecto.
     * @return Lista de pagos.
     */
    List<PagosEntity> findByProyecto(Integer idProyecto);

    /**
     * Obtiene todos los pagos asociados a una campaña.
     *
     * @param idCampania ID de la campaña.
     * @return Lista de pagos asociados a esa campaña.
     */
    List<PagosEntity> findByCampania(Integer idCampania);

    /**
     * Calcula el total recaudado por un proyecto.
     *
     * @param idProyecto ID del proyecto.
     * @return Monto total recaudado.
     */
    Double obtenerTotalRecaudado(Integer idProyecto);

    /**
     * Verifica si un usuario ya ha aportado a un proyecto.
     *
     * @param idUsuario ID del usuario.
     * @param idProyecto ID del proyecto.
     * @return true si ya aportó, false si no.
     */
    boolean usuarioHaPagado(Integer idUsuario, Integer idProyecto);

    /**
     * Elimina un pago por su ID.
     *
     * @param id ID del pago.
     */
    void delete(Integer id);
}





