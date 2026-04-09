package com.example.people.service;

import com.example.people.dao.UsuarioDAO;
import com.example.people.entity.UsuarioEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar la lógica de negocio relacionada con los usuarios
 * dentro de la plataforma de crowdfunding.
 */
@Service
public class UsuarioService {

    private final UsuarioDAO usuarioDAO;

    @Autowired
    public UsuarioService(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    /**
     * Obtiene un usuario por su ID.
     *
     * @param id Identificador del usuario.
     * @return El usuario encontrado o null si no existe.
     */
    public UsuarioEntity obtenerPorId(Integer id) {
        return usuarioDAO.findById(id);
    }

    /**
     * Obtiene todos los usuarios registrados.
     *
     * @return Lista de usuarios.
     */
    public List<UsuarioEntity> listarTodos() {
        return usuarioDAO.findAll();
    }

    /**
     * Registra un nuevo usuario en el sistema.
     * Valida que el email no esté ya registrado.
     *
     * @param usuario Usuario a registrar.
     * @return El usuario guardado.
     */
    public UsuarioEntity registrarUsuario(UsuarioEntity usuario) {

        if (usuarioDAO.existsByEmail(usuario.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        return usuarioDAO.save(usuario);
    }

    /**
     * Actualiza los datos de un usuario existente.
     *
     * @param usuario Usuario con los datos actualizados.
     * @return El usuario actualizado.
     */
    public UsuarioEntity actualizarUsuario(UsuarioEntity usuario) {
        return usuarioDAO.save(usuario);
    }

    /**
     * Elimina un usuario por su ID.
     *
     * @param id Identificador del usuario.
     */
    public void eliminarUsuario(Integer id) {
        usuarioDAO.delete(id);
    }

    /**
     * Obtiene un usuario por su email.
     *
     * @param email Email del usuario.
     * @return El usuario encontrado o null si no existe.
     */
    public UsuarioEntity obtenerPorEmail(String email) {
        return usuarioDAO.findByEmail(email);
    }

    /**
     * Obtiene todos los usuarios activos.
     *
     * @return Lista de usuarios activos.
     */
    public List<UsuarioEntity> listarActivos() {
        return usuarioDAO.findActivos();
    }

    /**
     * Obtiene usuarios filtrados por rol.
     *
     * @param rol Rol del usuario.
     * @return Lista de usuarios con ese rol.
     */
    public List<UsuarioEntity> listarPorRol(String rol) {
        return usuarioDAO.findByRol(rol);
    }

    /**
     * Obtiene usuarios filtrados por rango.
     *
     * @param idRango Identificador del rango.
     * @return Lista de usuarios pertenecientes a ese rango.
     */
    public List<UsuarioEntity> listarPorRango(Integer idRango) {
        return usuarioDAO.findByRango(idRango);
    }

    /**
     * Actualiza la contraseña de un usuario.
     *
     * @param id ID del usuario.
     * @param nuevaContrasenia Nueva contraseña.
     */
    public void actualizarContrasenia(Integer id, String nuevaContrasenia) {
        usuarioDAO.updateContrasenia(id, nuevaContrasenia);
    }

    /**
     * Marca un usuario como dado de baja.
     *
     * @param id ID del usuario.
     */
    public void darDeBaja(Integer id) {
        usuarioDAO.darDeBaja(id);
    }
}

