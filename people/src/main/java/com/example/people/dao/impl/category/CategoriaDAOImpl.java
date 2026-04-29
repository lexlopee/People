package com.example.people.dao.impl.category;

import com.example.people.dao.category.CategoriaDAO;
import com.example.people.entity.category.CategoriaEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementación del DAO para gestionar categorías dentro de la plataforma.
 * Utiliza JPA y EntityManager para realizar operaciones de persistencia.
 */
@Repository
@Transactional
public class CategoriaDAOImpl implements CategoriaDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public CategoriaEntity findById(Integer id) {
        return entityManager.find(CategoriaEntity.class, id);
    }

    @Override
    public List<CategoriaEntity> findAll() {
        String jpql = "FROM CategoriaEntity";
        return entityManager.createQuery(jpql, CategoriaEntity.class).getResultList();
    }

    @Override
    public CategoriaEntity save(CategoriaEntity categoria) {
        if (categoria.getId() == null) {
            entityManager.persist(categoria);
        } else {
            categoria = entityManager.merge(categoria);
        }
        return categoria;
    }

    @Override
    public void delete(Integer id) {
        CategoriaEntity categoria = findById(id);
        if (categoria != null) {
            entityManager.remove(categoria);
        }
    }

    @Override
    public CategoriaEntity findByNombre(String nombre) {
        String jpql = "FROM CategoriaEntity c WHERE c.nombre = :nombre";

        List<CategoriaEntity> resultados = entityManager
                .createQuery(jpql, CategoriaEntity.class)
                .setParameter("nombre", nombre)
                .getResultList();

        return resultados.isEmpty() ? null : resultados.get(0);
    }
}
