package com.example.people.dao;

import com.example.people.entity.CampaniaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * Implementación del DAO para gestionar campañas dentro de la plataforma.
 * Utiliza JPA y EntityManager para realizar operaciones de persistencia.
 */
@Repository
@Transactional
public class CampaniaDAOimpl implements CampaniaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CampaniaEntity findById(Integer id) {
        return entityManager.find(CampaniaEntity.class, id);
    }

    @Override
    public CampaniaEntity save(CampaniaEntity campania) {
        if (campania.getId() == null) {
            entityManager.persist(campania);
        } else {
            campania = entityManager.merge(campania);
        }
        return campania;
    }

    @Override
    public void delete(Integer id) {
        CampaniaEntity campania = findById(id);
        if (campania != null) {
            entityManager.remove(campania);
        }
    }

    @Override
    public List<CampaniaEntity> findActivas() {
        String jpql = """
                FROM CampaniaEntity c 
                WHERE c.fechaFin IS NULL OR c.fechaFin > CURRENT_DATE
                """;
        return entityManager.createQuery(jpql, CampaniaEntity.class).getResultList();
    }

    @Override
    public List<CampaniaEntity> findFinalizadas() {
        String jpql = """
                FROM CampaniaEntity c 
                WHERE c.fechaFin IS NOT NULL AND c.fechaFin <= CURRENT_DATE
                """;
        return entityManager.createQuery(jpql, CampaniaEntity.class).getResultList();
    }

    @Override
    public List<CampaniaEntity> findByCategoria(Integer idCategoria) {
        String jpql = """
                SELECT c FROM CampaniaEntity c 
                JOIN c.categoriaEntities cat 
                WHERE cat.id = :idCategoria
                """;
        return entityManager.createQuery(jpql, CampaniaEntity.class)
                .setParameter("idCategoria", idCategoria)
                .getResultList();
    }

    @Override
    public List<CampaniaEntity> findByCreador(Integer idUsuario) {
        String jpql = "FROM CampaniaEntity c WHERE c.usuarioEntity.id = :idUsuario";
        return entityManager.createQuery(jpql, CampaniaEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public List<CampaniaEntity> findRecientes() {
        String jpql = "FROM CampaniaEntity c ORDER BY c.fechaInicio DESC";
        return entityManager.createQuery(jpql, CampaniaEntity.class).getResultList();
    }

    @Override
    public List<CampaniaEntity> findDestacadas() {
        String jpql = "FROM CampaniaEntity c ORDER BY c.montoActual DESC";
        return entityManager.createQuery(jpql, CampaniaEntity.class).getResultList();
    }

    @Override
    public void cerrarCampania(Integer idCampania) {
        CampaniaEntity campania = findById(idCampania);
        if (campania != null) {
            campania.setFechaFin(LocalDate.now());
            entityManager.merge(campania);
        }
    }
}



