package com.example.people.dao.user;

import com.example.people.entity.user.UsuarioEntity;

import java.util.List;

public interface UsuarioDAO {
    UsuarioEntity findById(Integer id);
    List<UsuarioEntity> findAll();
    UsuarioEntity save(UsuarioEntity usuario);
    void delete(Integer id);
    UsuarioEntity findByEmail(String email);
    boolean existsByEmail(String email);
    List<UsuarioEntity> findActivos();
    List<UsuarioEntity> findByRol(String rol);
    List<UsuarioEntity> findByRango(Integer idRango);
    void updateContrasenia(Integer id, String nuevaContrasenia);
    void darDeBaja(Integer id);

}
