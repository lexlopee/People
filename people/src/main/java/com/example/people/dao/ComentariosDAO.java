package com.example.people.dao;

import com.example.people.entity.ComentariosEntity;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz DAO para la gestión de comentarios en la plataforma de crowdfunding.
 * Define las operaciones de acceso a datos para la entidad {@link ComentariosEntity}.
 */
public interface ComentariosDAO {

    /**
     * Busca un comentario por su identificador único.
     *
     * @param id Identificador del comentario.
     * @return El {@link ComentariosEntity} correspondiente, o {@code null} si no existe.
     */
    ComentariosEntity findById(Integer id);

    /**
     * Recupera todos los comentarios almacenados en la base de datos.
     *
     * @return Lista de todos los {@link ComentariosEntity}.
     */
    List<ComentariosEntity> findAll();

    /**
     * Guarda o actualiza un comentario.
     * Si el comentario no tiene ID, se persiste como nuevo.
     * Si ya tiene ID, se actualiza el existente.
     *
     * @param comentario Entidad {@link ComentariosEntity} a guardar o actualizar.
     * @return El {@link ComentariosEntity} guardado o actualizado.
     */
    ComentariosEntity save(ComentariosEntity comentario);

    /**
     * Elimina un comentario por su identificador único.
     * Si no existe ningún comentario con ese ID, no realiza ninguna acción.
     *
     * @param id Identificador del comentario a eliminar.
     */
    void delete(Integer id);

    /**
     * Recupera todos los comentarios realizados por un usuario concreto.
     *
     * @param idUsuario Identificador del usuario.
     * @return Lista de {@link ComentariosEntity} del usuario indicado.
     */
    List<ComentariosEntity> findByUsuarioId(Integer idUsuario);

    /**
     * Recupera todos los comentarios asociados a una campaña concreta.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de {@link ComentariosEntity} de la campaña indicada.
     */
    List<ComentariosEntity> findByCampaniaId(Integer idCampania);

    /**
     * Recupera todos los comentarios publicados en una fecha exacta.
     *
     * @param fecha Fecha de publicación a buscar.
     * @return Lista de {@link ComentariosEntity} publicados en esa fecha.
     */
    List<ComentariosEntity> findByFecha(LocalDate fecha);

    /**
     * Recupera todos los comentarios publicados dentro de un rango de fechas, ambas inclusive.
     *
     * @param inicio Fecha de inicio del rango.
     * @param fin    Fecha de fin del rango.
     * @return Lista de {@link ComentariosEntity} publicados en ese rango.
     */
    List<ComentariosEntity> findByFechaBetween(LocalDate inicio, LocalDate fin);

    /**
     * Recupera todos los comentarios realizados por un usuario en una campaña concreta.
     *
     * @param idUsuario  Identificador del usuario.
     * @param idCampania Identificador de la campaña.
     * @return Lista de {@link ComentariosEntity} que cumplen ambos criterios.
     */
    List<ComentariosEntity> findByUsuarioIdAndCampaniaId(Integer idUsuario, Integer idCampania);

    /**
     * Cuenta el número total de comentarios de una campaña.
     *
     * @param idCampania Identificador de la campaña.
     * @return Número de comentarios asociados a la campaña.
     */
    long countByCampaniaId(Integer idCampania);

    /**
     * Comprueba si existe un comentario con el identificador indicado.
     *
     * @param id Identificador del comentario.
     * @return {@code true} si existe, {@code false} en caso contrario.
     */
    boolean existsById(Integer id);
}