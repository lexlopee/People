package com.example.people.dao;

import com.example.people.entity.CobroEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Implementación del DAO para gestionar los cobros (retiros de fondos)
 * asociados a las campañas de la plataforma.
 */
@Repository
@Transactional
public class CobroDAOImpl implements CobroDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CobroEntity findById(Integer id) {
        return entityManager.find(CobroEntity.class, id);
    }

    @Override
    public CobroEntity save(CobroEntity cobro) {
        if (cobro.getId() == null) {
            entityManager.persist(cobro);
        } else {
            cobro = entityManager.merge(cobro);
        }
        return cobro;
    }

    @Override
    public void delete(Integer id) {
        CobroEntity cobro = findById(id);
        if (cobro != null) {
            entityManager.remove(cobro);
        }
    }

    @Override
    public List<CobroEntity> findByCampania(Integer idCampania) {
        String jpql = """
                FROM CobroEntity c
                WHERE c.campaniaEntity.id = :idCampania
                ORDER BY c.fechaSolicitud DESC
                """;

        return entityManager.createQuery(jpql, CobroEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    @Override
    public List<CobroEntity> findPendientes() {
        String jpql = """
                FROM CobroEntity c
                WHERE c.fechaTransferencia IS NULL
                ORDER BY c.fechaSolicitud ASC
                """;

        return entityManager.createQuery(jpql, CobroEntity.class)
                .getResultList();
    }

    @Override
    public List<CobroEntity> findCompletados() {
        String jpql = """
                FROM CobroEntity c
                WHERE c.fechaTransferencia IS NOT NULL
                ORDER BY c.fechaTransferencia DESC
                """;

        return entityManager.createQuery(jpql, CobroEntity.class)
                .getResultList();
    }

    @Override
    public List<CobroEntity> findByFechaSolicitud(Date fecha) {
        String jpql = """
                FROM CobroEntity c
                WHERE c.fechaSolicitud = :fecha
                """;

        return entityManager.createQuery(jpql, CobroEntity.class)
                .setParameter("fecha", fecha)
                .getResultList();
    }

    @Override
    public List<CobroEntity> findByCuentaDestino(String cuentaDestino) {
        String jpql = """
                FROM CobroEntity c
                WHERE c.cuentaDestino = :cuentaDestino
                """;

        return entityManager.createQuery(jpql, CobroEntity.class)
                .setParameter("cuentaDestino", cuentaDestino)
                .getResultList();
    }

    @Override
    public boolean existeCobroPendiente(Integer idCampania) {
        String jpql = """
                SELECT COUNT(c)
                FROM CobroEntity c
                WHERE c.campaniaEntity.id = :idCampania
                AND c.fechaTransferencia IS NULL
                """;

        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("idCampania", idCampania)
                .getSingleResult();

        return count > 0;
    }
}
