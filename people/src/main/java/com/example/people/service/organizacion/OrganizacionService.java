package com.example.people.service.organizacion;

import com.example.people.dao.organizacion.OrganizacionDAO;
import com.example.people.entity.organizacion.OrganizacionEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las organizaciones
 * dentro de la plataforma de crowdfunding.
 *
 * Actúa como capa intermedia entre los controladores y el DAO, proporcionando
 * lógica de negocio adicional si fuera necesaria.
 */
@Service
public class OrganizacionService {

    private final OrganizacionDAO organizacionDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param organizacionDAO DAO encargado del acceso a datos de organizaciones.
     */
    public OrganizacionService(OrganizacionDAO organizacionDAO) {
        this.organizacionDAO = organizacionDAO;
    }

    /**
     * Obtiene una organización por su identificador único.
     *
     * @param id Identificador de la organización.
     * @return La organización encontrada o null si no existe.
     */
    public OrganizacionEntity obtenerPorId(Integer id) {
        return organizacionDAO.findById(id);
    }

    /**
     * Obtiene todas las organizaciones registradas en el sistema.
     *
     * @return Lista de organizaciones.
     */
    public List<OrganizacionEntity> obtenerTodas() {
        return organizacionDAO.findAll();
    }

    /**
     * Guarda o actualiza una organización.
     *
     * @param organizacion Entidad organización a persistir.
     * @return La organización guardada o actualizada.
     */
    public OrganizacionEntity guardar(OrganizacionEntity organizacion) {
        return organizacionDAO.save(organizacion);
    }

    /**
     * Elimina una organización por su identificador.
     *
     * @param id Identificador de la organización.
     */
    public void eliminar(Integer id) {
        organizacionDAO.delete(id);
    }

    /**
     * Obtiene todas las organizaciones creadas por un usuario concreto.
     *
     * @param idUsuario Identificador del usuario creador.
     * @return Lista de organizaciones asociadas al usuario.
     */
    public List<OrganizacionEntity> obtenerPorUsuario(Integer idUsuario) {
        return organizacionDAO.findByUsuario(idUsuario);
    }

    /**
     * Busca una organización por su nombre.
     *
     * @param nombre Nombre de la organización.
     * @return La organización encontrada o null si no existe.
     */
    public OrganizacionEntity buscarPorNombre(String nombre) {
        return organizacionDAO.findByNombre(nombre);
    }
}
