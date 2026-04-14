package com.example.people.service;

import com.example.people.dao.ComentariosDAO;
import com.example.people.entity.ComentariosEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Servicio encargado de gestionar las operaciones relacionadas con los comentarios
 * dentro de la plataforma de crowdfunding.
 *
 * Proporciona métodos para crear, consultar, filtrar y eliminar comentarios,
 * actuando como capa intermedia entre los controladores y el DAO.
 */
@Service
public class ComentariosService {

    private final ComentariosDAO comentariosDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param comentariosDAO DAO encargado del acceso a datos de comentarios.
     */
    public ComentariosService(ComentariosDAO comentariosDAO) {
        this.comentariosDAO = comentariosDAO;
    }

    /**
     * Obtiene un comentario por su identificador único.
     *
     * @param id Identificador del comentario.
     * @return El comentario encontrado o null si no existe.
     */
    public ComentariosEntity obtenerPorId(Integer id) {
        return comentariosDAO.findById(id);
    }

    /**
     * Obtiene todos los comentarios almacenados en el sistema.
     *
     * @return Lista de comentarios.
     */
    public List<ComentariosEntity> obtenerTodos() {
        return comentariosDAO.findAll();
    }

    /**
     * Guarda o actualiza un comentario.
     *
     * @param comentario Entidad comentario a persistir.
     * @return El comentario guardado o actualizado.
     */
    public ComentariosEntity guardar(ComentariosEntity comentario) {
        return comentariosDAO.save(comentario);
    }

    /**
     * Elimina un comentario por su identificador.
     *
     * @param id Identificador del comentario.
     */
    public void eliminar(Integer id) {
        comentariosDAO.delete(id);
    }

    /**
     * Obtiene todos los comentarios realizados por un usuario concreto.
     *
     * @param idUsuario Identificador del usuario.
     * @return Lista de comentarios del usuario.
     */
    public List<ComentariosEntity> obtenerPorUsuario(Integer idUsuario) {
        return comentariosDAO.findByUsuarioId(idUsuario);
    }

    /**
     * Obtiene todos los comentarios asociados a una campaña concreta.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de comentarios de la campaña.
     */
    public List<ComentariosEntity> obtenerPorCampania(Integer idCampania) {
        return comentariosDAO.findByCampaniaId(idCampania);
    }

    /**
     * Obtiene todos los comentarios publicados en una fecha exacta.
     *
     * @param fecha Fecha de publicación.
     * @return Lista de comentarios publicados ese día.
     */
    public List<ComentariosEntity> obtenerPorFecha(LocalDate fecha) {
        return comentariosDAO.findByFecha(fecha);
    }

    /**
     * Obtiene todos los comentarios publicados dentro de un rango de fechas.
     *
     * @param inicio Fecha de inicio.
     * @param fin    Fecha de fin.
     * @return Lista de comentarios dentro del rango.
     */
    public List<ComentariosEntity> obtenerPorRangoFechas(LocalDate inicio, LocalDate fin) {
        return comentariosDAO.findByFechaBetween(inicio, fin);
    }

    /**
     * Obtiene los comentarios realizados por un usuario en una campaña concreta.
     *
     * @param idUsuario  Identificador del usuario.
     * @param idCampania Identificador de la campaña.
     * @return Lista de comentarios que cumplen ambos criterios.
     */
    public List<ComentariosEntity> obtenerPorUsuarioYCampania(Integer idUsuario, Integer idCampania) {
        return comentariosDAO.findByUsuarioIdAndCampaniaId(idUsuario, idCampania);
    }

    /**
     * Cuenta el número total de comentarios asociados a una campaña.
     *
     * @param idCampania Identificador de la campaña.
     * @return Número total de comentarios.
     */
    public long contarPorCampania(Integer idCampania) {
        return comentariosDAO.countByCampaniaId(idCampania);
    }

    /**
     * Comprueba si existe un comentario con el ID indicado.
     *
     * @param id Identificador del comentario.
     * @return true si existe, false en caso contrario.
     */
    public boolean existe(Integer id) {
        return comentariosDAO.existsById(id);
    }
}
