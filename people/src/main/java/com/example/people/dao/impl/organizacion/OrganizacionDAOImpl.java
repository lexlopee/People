package com.example.people.dao.impl.organizacion;

import com.example.people.dao.organizacion.OrganizacionDAO;
import com.example.people.entity.organizacion.OrganizacionEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO para gestionar organizaciones dentro de la plataforma.
 * Utiliza JPA y EntityManager para realizar operaciones de persistencia.
 */
@Repository
@Transactional
public class OrganizacionDAOImpl implements OrganizacionDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public OrganizacionEntity findById(Integer id) {
        return entityManager.find(OrganizacionEntity.class, id);
    }

    @Override
    public List<OrganizacionEntity> findAll() {
        String jpql = "FROM OrganizacionEntity";
        return entityManager.createQuery(jpql, OrganizacionEntity.class).getResultList();
    }

    @Override
    public OrganizacionEntity save(OrganizacionEntity organizacion) {
        if (organizacion.getId() == null) {
            entityManager.persist(organizacion);
        } else {
            organizacion = entityManager.merge(organizacion);
        }
        return organizacion;
    }

    @Override
    public void delete(Integer id) {
        OrganizacionEntity organizacion = findById(id);
        if (organizacion != null) {
            entityManager.remove(organizacion);
        }
    }

    @Override
    public List<OrganizacionEntity> findByUsuario(Integer idUsuario) {
        String jpql = """
                FROM OrganizacionEntity o
                WHERE o.usuarioEntity.id = :idUsuario
                """;

        return entityManager.createQuery(jpql, OrganizacionEntity.class)
                .setParameter("idUsuario", idUsuario)
                .getResultList();
    }

    @Override
    public OrganizacionEntity findByNombre(String nombre) {
        String jpql = """
                FROM OrganizacionEntity o
                WHERE o.nombre = :nombre
                """;

        List<OrganizacionEntity> resultados = entityManager
                .createQuery(jpql, OrganizacionEntity.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
