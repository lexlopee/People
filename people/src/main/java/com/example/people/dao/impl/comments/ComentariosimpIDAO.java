package com.example.people.dao.impl.comments;

import com.example.people.dao.comments.ComentariosDAO;
import com.example.people.entity.comments.ComentariosEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación de {@link ComentariosDAO} mediante JPA/Hibernate.
 * Gestiona las operaciones de acceso a datos para {@link ComentariosEntity}.
 */
@Repository
@Transactional
public class ComentariosimpIDAO implements ComentariosDAO {

    /**
     * EntityManager inyectado por el contenedor JPA para interactuar con la base de datos.
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public ComentariosEntity findById(Integer id) {
        return entityManager.find(ComentariosEntity.class, id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findAll() {
        return entityManager.createQuery("FROM ComentariosEntity", ComentariosEntity.class)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ComentariosEntity save(ComentariosEntity comentario) {
        if (comentario.getId() == null) {
            entityManager.persist(comentario);
        } else {
            comentario = entityManager.merge(comentario);
        }
        return comentario;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delete(Integer id) {
        ComentariosEntity comentario = findById(id);
        if (comentario != null) {
            entityManager.remove(comentario);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findByUsuarioId(Integer idUsuario) {
        String jpql = "FROM ComentariosEntity c WHERE c.usuario.id = :idUsuario";
        return entityManager.createQuery(jpql, ComentariosEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findByCampaniaId(Integer idCampania) {
        String jpql = "FROM ComentariosEntity c WHERE c.campaniaEntity.id = :idCampania";
        return entityManager.createQuery(jpql, ComentariosEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findByFecha(LocalDate fecha) {
        String jpql = "FROM ComentariosEntity c WHERE c.fecha = :fecha";
        return entityManager.createQuery(jpql, ComentariosEntity.class)
                .setParameter("fecha", fecha)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findByFechaBetween(LocalDate inicio, LocalDate fin) {
        String jpql = "FROM ComentariosEntity c WHERE c.fecha BETWEEN :inicio AND :fin";
        return entityManager.createQuery(jpql, ComentariosEntity.class)
                .setParameter("inicio", inicio)
                .setParameter("fin", fin)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ComentariosEntity> findByUsuarioIdAndCampaniaId(Integer idUsuario, Integer idCampania) {
        String jpql = "FROM ComentariosEntity c WHERE c.usuario.id = :idUsuario AND c.campaniaEntity.id = :idCampania";
        return entityManager.createQuery(jpql, ComentariosEntity.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countByCampaniaId(Integer idCampania) {
        String jpql = "SELECT COUNT(c) FROM ComentariosEntity c WHERE c.campaniaEntity.id = :idCampania";
        return entityManager.createQuery(jpql, Long.class)
                .setParameter("idCampania", idCampania)
                .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean existsById(Integer id) {
        String jpql = "SELECT COUNT(c) FROM ComentariosEntity c WHERE c.id = :id";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}