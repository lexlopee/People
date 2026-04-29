package com.example.people.dao.campaingUpdate;

import com.example.people.entity.campaingUdapte.CampaingUpdateEntity;

import java.util.List;

/**
 * DAO para gestionar las actualizaciones de campaña dentro de la plataforma
 * de crowdfunding. Las actualizaciones permiten a los creadores informar
 * a los donantes sobre el progreso o novedades del proyecto.
 */
public interface CampaingUpdateDAO {

    /**
     * Busca una actualización por su ID.
     *
     * @param id Identificador de la actualización.
     * @return La actualización encontrada o null si no existe.
     */
    CampaingUpdateEntity findById(Integer id);

    /**
     * Guarda una nueva actualización o actualiza una existente.
     *
     * @param update Entidad de actualización a persistir.
     * @return La actualización guardada.
     */
    CampaingUpdateEntity save(CampaingUpdateEntity update);

    /**
     * Elimina una actualización por su ID.
     *
     * @param id Identificador de la actualización.
     */
    void delete(Integer id);

    /**
     * Obtiene todas las actualizaciones asociadas a una campaña.
     *
     * @param idCampania ID de la campaña.
     * @return Lista de actualizaciones.
     */
    List<CampaingUpdateEntity> findByCampania(Integer idCampania);

    /**
     * Obtiene las actualizaciones más recientes de una campaña,
     * ordenadas por fecha descendente.
     *
     * @param idCampania ID de la campaña.
     * @return Lista de actualizaciones recientes.
     */
    List<CampaingUpdateEntity> findRecientesByCampania(Integer idCampania);
}

