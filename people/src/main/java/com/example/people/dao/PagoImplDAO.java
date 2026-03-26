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
        String jpql = "FROM PagoEntity p WHERE p.idUsuario = :idUsuario";
        return entityManager.createQuery(jpql, PagosEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public List<PagosEntity> findByProyecto(Integer idProyecto) {
        String jpql = "FROM PagoEntity p WHERE p.idProyecto = :idProyecto";
        return entityManager.createQuery(jpql, PagosEntity.class)
                .setParameter("idProyecto", idProyecto)
                .getResultList();
    }

    @Override
    public Double obtenerTotalRecaudado(Integer idProyecto) {
        String jpql = "SELECT SUM(p.monto) FROM PagoEntity p WHERE p.idProyecto = :idProyecto";
        Double total = entityManager.createQuery(jpql, Double.class)
                .setParameter("idProyecto", idProyecto)
                .getSingleResult();

        return total != null ? total : 0.0;
    }

    @Override
    public boolean usuarioHaPagado(Integer idUsuario, Integer idProyecto) {
        String jpql = "SELECT COUNT(p) FROM PagoEntity p WHERE p.idUsuario = :idUsuario AND p.idProyecto = :idProyecto";
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

