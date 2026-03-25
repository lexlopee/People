package com.example.people.dao;

import com.example.people.entity.UsuarioEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public class UsuarioImplDAO implements UsuarioDAO{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public UsuarioEntity findById(Integer id) {
        return entityManager.find(UsuarioEntity.class, id);
    }

    @Override
    public List<UsuarioEntity> findAll() {
        return entityManager.createQuery("FROM UsuarioEntity", UsuarioEntity.class)
                .getResultList();
    }

    @Override
    public UsuarioEntity save(UsuarioEntity usuario) {
        if(usuario.getId() == null){
            entityManager.persist(usuario);
        }else{
            usuario = entityManager.merge(usuario);
        }
        return usuario;
    }

    @Override
    public void delete(Integer id) {
        UsuarioEntity usuarioEntity = findById(id);
        if(usuarioEntity != null){
            entityManager.remove(usuarioEntity);
        }
    }

    @Override
    public UsuarioEntity findByEmail(String email) {
        String jpql = "FROM UsuarioEntity u WHERE u.email = :email";
        TypedQuery<UsuarioEntity> query = entityManager.createQuery(jpql, UsuarioEntity.class);
        query.setParameter("email", email);

        List<UsuarioEntity> resultados = query.getResultList();
        return resultados.isEmpty() ? null : resultados.getFirst();
    }

    @Override
    public boolean existsByEmail(String email) {
        String jpql = "SELECT COUNT(u) FROM UsuarioEntity u WHERE u.email = :email";
        Long count = entityManager.createQuery(jpql, Long.class)
                .setParameter("email", email)
                .getSingleResult();
        return count > 0;
    }

    @Override
    public List<UsuarioEntity> findActivos() {
        String jpql = "FROM UsuarioEntity u WHERE u.fechaBaja IS NULL";
        return entityManager.createQuery(jpql, UsuarioEntity.class).getResultList();
    }

    @Override
    public List<UsuarioEntity> findByRol(String rol) {
        String jpql = "FROM UsuarioEntity u WHERE u.rol = :rol";
        return entityManager.createQuery(jpql, UsuarioEntity.class)
                .setParameter("rol", rol)
                .getResultList();
    }

    @Override
    public List<UsuarioEntity> findByRango(Integer idRango) {
        String jpql = "FROM UsuarioEntity u WHERE u.rango.id = :idRango";
        return entityManager.createQuery(jpql, UsuarioEntity.class)
                .setParameter("idRango", idRango)
                .getResultList();
    }

    @Override
    public void updateContrasenia(Integer id, String nuevaContrasenia) {
        UsuarioEntity usuario = entityManager.find(UsuarioEntity.class, id);
        if (usuario != null) {
            usuario.setContrasenia(nuevaContrasenia);
            entityManager.merge(usuario);
        }
    }

    @Override
    public void darDeBaja(Integer id) {
        UsuarioEntity usuario = entityManager.find(UsuarioEntity.class, id);
        if (usuario != null && usuario.getFechaBaja() == null) {
            usuario.setFechaBaja(LocalDate.now());
            entityManager.merge(usuario);
        }
    }


}
