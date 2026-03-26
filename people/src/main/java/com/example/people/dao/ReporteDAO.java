package com.example.people.dao;

import com.example.people.entity.ReporteEntity;
import java.util.List;

/**
 * DAO para gestionar los reportes dentro de la plataforma de crowdfunding.
 * Los reportes permiten a los usuarios denunciar contenido inapropiado,
 * campañas fraudulentas o comportamientos indebidos.
 */
public interface ReporteDAO {

    /**
     * Busca un reporte por su ID.
     *
     * @param id Identificador del reporte.
     * @return El reporte encontrado o null si no existe.
     */
    ReporteEntity findById(Integer id);

    /**
     * Guarda un nuevo reporte o actualiza uno existente.
     *
     * @param reporte Entidad reporte a persistir.
     * @return El reporte guardado.
     */
    ReporteEntity save(ReporteEntity reporte);

    /**
     * Elimina un reporte por su ID.
     *
     * @param id Identificador del reporte.
     */
    void delete(Integer id);

    /**
     * Obtiene todos los reportes registrados.
     *
     * @return Lista de reportes.
     */
    List<ReporteEntity> findAll();

    /**
     * Obtiene los reportes realizados por un usuario específico.
     *
     * @param idUsuario ID del usuario que realizó el reporte.
     * @return Lista de reportes.
     */
    List<ReporteEntity> findByUsuario(Integer idUsuario);

    /**
     * Obtiene los reportes asociados a una campaña específica.
     *
     * @param idCampania ID de la campaña.
     * @return Lista de reportes.
     */
    List<ReporteEntity> findByCampania(Integer idCampania);

    /**
     * Obtiene los reportes que aún no han sido revisados.
     *
     * @return Lista de reportes pendientes.
     */
    List<ReporteEntity> findPendientes();

    /**
     * Obtiene los reportes que ya han sido revisados.
     *
     * @return Lista de reportes resueltos.
     */
    List<ReporteEntity> findResueltos();
}

