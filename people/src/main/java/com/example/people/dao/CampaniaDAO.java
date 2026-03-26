package com.example.people.dao;

import com.example.people.entity.CampaniaEntity;
import java.util.List;

/**
 * DAO para gestionar las campañas dentro de la plataforma de crowdfunding.
 * Permite realizar operaciones de consulta, creación, actualización y filtrado
 * sobre las campañas creadas por los usuarios.
 */
public interface CampaniaDAO {

    /**
     * Busca una campaña por su ID.
     *
     * @param id Identificador de la campaña.
     * @return La campaña encontrada o null si no existe.
     */
    CampaniaEntity findById(Integer id);

    /**
     * Guarda una nueva campaña o actualiza una existente.
     *
     * @param campania Entidad campaña a persistir.
     * @return La campaña guardada.
     */
    CampaniaEntity save(CampaniaEntity campania);

    /**
     * Elimina una campaña por su ID.
     *
     * @param id Identificador de la campaña.
     */
    void delete(Integer id);

    /**
     * Obtiene todas las campañas activas (no finalizadas).
     *
     * @return Lista de campañas activas.
     */
    List<CampaniaEntity> findActivas();

    /**
     * Obtiene todas las campañas finalizadas.
     *
     * @return Lista de campañas finalizadas.
     */
    List<CampaniaEntity> findFinalizadas();

    /**
     * Obtiene campañas filtradas por categoría.
     *
     * @param idCategoria ID de la categoría.
     * @return Lista de campañas pertenecientes a esa categoría.
     */
    List<CampaniaEntity> findByCategoria(Integer idCategoria);

    /**
     * Obtiene campañas creadas por un usuario específico.
     *
     * @param idUsuario ID del creador.
     * @return Lista de campañas creadas por ese usuario.
     */
    List<CampaniaEntity> findByCreador(Integer idUsuario);

    /**
     * Obtiene campañas ordenadas por fecha de inicio (más recientes primero).
     *
     * @return Lista de campañas recientes.
     */
    List<CampaniaEntity> findRecientes();

    /**
     * Obtiene campañas destacadas según su monto actual.
     *
     * @return Lista de campañas destacadas.
     */
    List<CampaniaEntity> findDestacadas();

    /**
     * Marca una campaña como finalizada.
     *
     * @param idCampania ID de la campaña.
     */
    void cerrarCampania(Integer idCampania);
}

