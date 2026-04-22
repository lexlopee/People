package com.example.people.dao;

import com.example.people.entity.CampaingUpdateEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO para gestionar las actualizaciones de campaña.
 * Utiliza JPA y EntityManager para realizar operaciones de persistencia.
 */
@Repository
@Transactional
public class CampaingUpdateDAOImpl implements CampaingUpdateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CampaingUpdateEntity findById(Integer id) {
        return entityManager.find(CampaingUpdateEntity.class, id);
    }

    @Override
    public CampaingUpdateEntity save(CampaingUpdateEntity update) {
        if (update.getId() == null) {
            entityManager.persist(update);
        } else {
            update = entityManager.merge(update);
        }
        return update;
    }

    @Override
    public void delete(Integer id) {
        CampaingUpdateEntity update = findById(id);
        if (update != null) {
            entityManager.remove(update);
        }
    }

    @Override
    public List<CampaingUpdateEntity> findByCampania(Integer idCampania) {
        String jpql = """
                FROM CampaingUpdateEntity u
                WHERE u.campaniaEntity.id = :idCampania
                ORDER BY u.fechaPublicacion ASC
                """;

        return entityManager.createQuery(jpql, CampaingUpdateEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    @Override
    public List<CampaingUpdateEntity> findRecientesByCampania(Integer idCampania) {
        String jpql = """
                FROM CampaingUpdateEntity u
                WHERE u.campaniaEntity.id = :idCampania
                ORDER BY u.fechaPublicacion DESC
                """;

        return entityManager.createQuery(jpql, CampaingUpdateEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }
}

