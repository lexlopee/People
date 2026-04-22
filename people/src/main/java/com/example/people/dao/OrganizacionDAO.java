package com.example.people.dao;

import com.example.people.entity.OrganizacionEntity;

import java.util.List;

/**
 * DAO para gestionar organizaciones dentro de la plataforma de crowdfunding.
 * Permite consultar, crear, actualizar y eliminar organizaciones, así como
 * realizar búsquedas por nombre o por usuario creador.
 */
public interface OrganizacionDAO {

    /**
     * Busca una organización por su ID.
     *
     * @param id Identificador de la organización.
     * @return La organización encontrada o null si no existe.
     */
    OrganizacionEntity findById(Integer id);

    /**
     * Obtiene todas las organizaciones registradas.
     *
     * @return Lista de organizaciones.
     */
    List<OrganizacionEntity> findAll();

    /**
     * Guarda una nueva organización o actualiza una existente.
     *
     * @param organizacion Entidad organización a persistir.
     * @return La organización guardada.
     */
    OrganizacionEntity save(OrganizacionEntity organizacion);

    /**
     * Elimina una organización por su ID.
     *
     * @param id Identificador de la organización.
     */
    void delete(Integer id);

    /**
     * Busca organizaciones creadas por un usuario específico.
     *
     * @param idUsuario ID del usuario creador.
     * @return Lista de organizaciones creadas por ese usuario.
     */
    List<OrganizacionEntity> findByUsuario(Integer idUsuario);

    /**
     * Busca una organización por su nombre.
     *
     * @param nombre Nombre de la organización.
     * @return La organización encontrada o null si no existe.
     */
    OrganizacionEntity findByNombre(String nombre);
}
