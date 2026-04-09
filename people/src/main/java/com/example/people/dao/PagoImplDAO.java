package com.example.people.dao;

import com.example.people.entity.PagosEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO de pagos utilizando JPA y EntityManager.
 * Gestiona las operaciones de persistencia relacionadas con aportes económicos.
 */
@Repository
@Transactional
public class PagoImplDAO implements PagoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public PagosEntity findById(Integer id) {
        return entityManager.find(PagosEntity.class, id);
    }

    @Override
    public PagosEntity save(PagosEntity pago) {
        if (pago.getId() == null) {
            entityManager.persist(pago);
        } else {
            pago = entityManager.merge(pago);
        }
        return pago;
    }

    @Override
    public List<PagosEntity> findByUsuario(Integer idUsuario) {
        String jpql = """
            SELECT p FROM PagosEntity p
            WHERE p.donante.id = :idUsuario
        """;

        return entityManager.createQuery(jpql, PagosEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public List<PagosEntity> findByProyecto(Integer idProyecto) {
        // ⚠ Este método se mantiene por compatibilidad,
        // pero tu entidad NO tiene "proyecto", así que lo adaptamos a "campania".
        String jpql = """
            SELECT p FROM PagosEntity p
            WHERE p.campania.id = :idProyecto
        """;

        return entityManager.createQuery(jpql, PagosEntity.class)
                .setParameter("idProyecto", idProyecto)
                .getResultList();
    }

    /**
     * Nuevo método necesario para CampaniaService.
     * Obtiene todos los pagos asociados a una campaña.
     */
    @Override
    public List<PagosEntity> findByCampania(Integer idCampania) {
        String jpql = """
            SELECT p FROM PagosEntity p
            WHERE p.campania.id = :idCampania
        """;

        return entityManager.createQuery(jpql, PagosEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    @Override
    public Double obtenerTotalRecaudado(Integer idProyecto) {
        String jpql = """
            SELECT SUM(p.monto) FROM PagosEntity p
            WHERE p.campania.id = :idProyecto
        """;

        Double total = entityManager.createQuery(jpql, Double.class)
                .setParameter("idProyecto", idProyecto)
                .getSingleResult();

        return total != null ? total : 0.0;
    }

    @Override
    public boolean usuarioHaPagado(Integer idUsuario, Integer idProyecto) {
        String jpql = """
            SELECT COUNT(p) FROM PagosEntity p
            WHERE p.donante.id = :idUsuario
            AND p.campania.id = :idProyecto
        """;

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("idUsuario", idUsuario)
                .setParameter("idProyecto", idProyecto)
                .getSingleResult();

        return count > 0;
    }

    @Override
    public void delete(Integer id) {
        PagosEntity pago = findById(id);
        if (pago != null) {
            entityManager.remove(pago);
        }
    }
}


