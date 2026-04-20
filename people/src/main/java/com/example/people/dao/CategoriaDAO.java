package com.example.people.dao;

import com.example.people.entity.CategoriaEntity;

import java.util.List;

/**
 * DAO para gestionar las categorías dentro de la plataforma de crowdfunding.
 * Permite consultar, crear y eliminar categorías utilizadas para clasificar campañas.
 */
public interface CategoriaDAO {

    /**
     * Busca una categoría por su ID.
     *
     * @param id Identificador de la categoría.
     * @return La categoría encontrada o null si no existe.
     */
    CategoriaEntity findById(Integer id);

    /**
     * Obtiene todas las categorías disponibles.
     *
     * @return Lista de categorías.
     */
    List<CategoriaEntity> findAll();

    /**
     * Guarda una nueva categoría o actualiza una existente.
     *
     * @param categoria Entidad categoría a persistir.
     * @return La categoría guardada.
     */
    CategoriaEntity save(CategoriaEntity categoria);

    /**
     * Elimina una categoría por su ID.
     *
     * @param id Identificador de la categoría.
     */
    void delete(Integer id);

    /**
     * Busca una categoría por su nombre.
     *
     * @param nombre Nombre de la categoría.
     * @return La categoría encontrada o null si no existe.
     */
    CategoriaEntity findByNombre(String nombre);
}

