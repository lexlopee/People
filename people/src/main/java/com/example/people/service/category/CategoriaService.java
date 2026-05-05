package com.example.people.service.category;

import com.example.people.dao.category.CategoriaDAO;
import com.example.people.entity.category.CategoriaEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con las categorías
 * dentro de la plataforma de crowdfunding.
 *
 * Este servicio actúa como capa intermedia entre los controladores y el DAO,
 * proporcionando lógica de negocio adicional si fuera necesaria.
 */
@Service
public class CategoriaService {

    private final CategoriaDAO categoriaDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param categoriaDAO DAO encargado del acceso a datos de categorías.
     */
    public CategoriaService(CategoriaDAO categoriaDAO) {
        this.categoriaDAO = categoriaDAO;
    }

    /**
     * Obtiene una categoría por su identificador único.
     *
     * @param id Identificador de la categoría.
     * @return La categoría encontrada o null si no existe.
     */
    public CategoriaEntity obtenerPorId(Integer id) {
        return categoriaDAO.findById(id);
    }

    /**
     * Obtiene todas las categorías disponibles en el sistema.
     *
     * @return Lista de categorías.
     */
    public List<CategoriaEntity> obtenerTodas() {
        return categoriaDAO.findAll();
    }

    /**
     * Crea o actualiza una categoría en la base de datos.
     *
     * @param categoria Entidad categoría a guardar.
     * @return La categoría persistida.
     */
    public CategoriaEntity guardar(CategoriaEntity categoria) {
        return categoriaDAO.save(categoria);
    }

    /**
     * Elimina una categoría por su identificador.
     *
     * @param id Identificador de la categoría.
     */
    public void eliminar(Integer id) {
        categoriaDAO.delete(id);
    }

    /**
     * Busca una categoría por su nombre.
     *
     * @param nombre Nombre de la categoría.
     * @return La categoría encontrada o null si no existe.
     */
    public CategoriaEntity buscarPorNombre(String nombre) {
        return categoriaDAO.findByNombre(nombre);
    }
}


