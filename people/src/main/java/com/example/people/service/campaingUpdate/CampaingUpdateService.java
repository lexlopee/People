package com.example.people.service.campaingUpdate;

import com.example.people.dao.campaingUpdate.CampaingUpdateDAO;
import com.example.people.entity.campaingUdapte.CampaingUpdateEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar las actualizaciones de campaña dentro de la
 * plataforma de crowdfunding.
 *
 * Permite registrar nuevas actualizaciones, consultar las existentes y obtener
 * las más recientes asociadas a una campaña.
 */
@Service
public class CampaingUpdateService {

    private final CampaingUpdateDAO campaingUpdateDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param campaingUpdateDAO DAO encargado del acceso a datos de actualizaciones.
     */
    public CampaingUpdateService(CampaingUpdateDAO campaingUpdateDAO) {
        this.campaingUpdateDAO = campaingUpdateDAO;
    }

    /**
     * Obtiene una actualización por su identificador único.
     *
     * @param id Identificador de la actualización.
     * @return La actualización encontrada o null si no existe.
     */
    public CampaingUpdateEntity obtenerPorId(Integer id) {
        return campaingUpdateDAO.findById(id);
    }

    /**
     * Guarda o actualiza una actualización de campaña.
     *
     * @param update Entidad de actualización a persistir.
     * @return La actualización guardada o actualizada.
     */
    public CampaingUpdateEntity guardar(CampaingUpdateEntity update) {
        return campaingUpdateDAO.save(update);
    }

    /**
     * Elimina una actualización por su identificador.
     *
     * @param id Identificador de la actualización.
     */
    public void eliminar(Integer id) {
        campaingUpdateDAO.delete(id);
    }

    /**
     * Obtiene todas las actualizaciones asociadas a una campaña concreta.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de actualizaciones.
     */
    public List<CampaingUpdateEntity> obtenerPorCampania(Integer idCampania) {
        return campaingUpdateDAO.findByCampania(idCampania);
    }

    /**
     * Obtiene las actualizaciones más recientes de una campaña,
     * ordenadas por fecha descendente.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de actualizaciones recientes.
     */
    public List<CampaingUpdateEntity> obtenerRecientes(Integer idCampania) {
        return campaingUpdateDAO.findRecientesByCampania(idCampania);
    }
}
