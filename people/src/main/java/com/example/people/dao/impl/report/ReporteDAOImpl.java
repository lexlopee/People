package com.example.people.dao.impl.report;

import com.example.people.dao.report.ReporteDAO;
import com.example.people.entity.report.ReporteEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO para gestionar reportes dentro de la plataforma.
 * Utiliza JPA y EntityManager para realizar operaciones de persistencia.
 */
@Repository
@Transactional
public class ReporteDAOImpl implements ReporteDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public ReporteEntity findById(Integer id) {
        return entityManager.find(ReporteEntity.class, id);
    }

    @Override
    public ReporteEntity save(ReporteEntity reporte) {
        if (reporte.getId() == null) {
            entityManager.persist(reporte);
        } else {
            reporte = entityManager.merge(reporte);
        }
        return reporte;
    }

    @Override
    public void delete(Integer id) {
        ReporteEntity reporte = findById(id);
        if (reporte != null) {
            entityManager.remove(reporte);
        }
    }

    @Override
    public List<ReporteEntity> findAll() {
        String jpql = "FROM ReporteEntity";
        return entityManager.createQuery(jpql, ReporteEntity.class).getResultList();
    }

    @Override
    public List<ReporteEntity> findByUsuario(Integer idUsuario) {
        String jpql = """
                FROM ReporteEntity r
                WHERE r.usuarioEntity.id = :idUsuario
                ORDER BY r.fechaReporte DESC
                """;

        return entityManager.createQuery(jpql, ReporteEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public List<ReporteEntity> findByCampania(Integer idCampania) {
        String jpql = """
                FROM ReporteEntity r
                WHERE r.campaniaEntity.id = :idCampania
                ORDER BY r.fechaReporte DESC
                """;

        return entityManager.createQuery(jpql, ReporteEntity.class)
                .setParameter("idCampania", idCampania)
                .getResultList();
    }

    @Override
    public List<ReporteEntity> findPendientes() {
        String jpql = """
                FROM ReporteEntity r
                WHERE r.estado = 'PENDIENTE'
                ORDER BY r.fechaReporte ASC
                """;

        return entityManager.createQuery(jpql, ReporteEntity.class)
                .getResultList();
    }

    @Override
    public List<ReporteEntity> findResueltos() {
        String jpql = """
                FROM ReporteEntity r
                WHERE r.estado = 'RESUELTO'
                ORDER BY r.fechaReporte DESC
                """;

        return entityManager.createQuery(jpql, ReporteEntity.class)
                .getResultList();
    }
}
