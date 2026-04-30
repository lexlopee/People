package com.example.people.service.report;

import com.example.people.dao.report.ReporteDAO;
import com.example.people.entity.report.ReporteEntity;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar los reportes dentro de la plataforma
 * de crowdfunding.
 *
 * Permite registrar nuevos reportes, consultar reportes existentes,
 * filtrar por usuario, campaña o estado, y eliminar reportes.
 */
@Service
public class ReporteService {

    private final ReporteDAO reporteDAO;

    /**
     * Constructor para la inyección de dependencias.
     *
     * @param reporteDAO DAO encargado del acceso a datos de reportes.
     */
    public ReporteService(ReporteDAO reporteDAO) {
        this.reporteDAO = reporteDAO;
    }

    /**
     * Obtiene un reporte por su identificador único.
     *
     * @param id Identificador del reporte.
     * @return El reporte encontrado o null si no existe.
     */
    public ReporteEntity obtenerPorId(Integer id) {
        return reporteDAO.findById(id);
    }

    /**
     * Guarda o actualiza un reporte.
     *
     * @param reporte Entidad reporte a persistir.
     * @return El reporte guardado o actualizado.
     */
    public ReporteEntity guardar(ReporteEntity reporte) {
        return reporteDAO.save(reporte);
    }

    /**
     * Elimina un reporte por su identificador.
     *
     * @param id Identificador del reporte.
     */
    public void eliminar(Integer id) {
        reporteDAO.delete(id);
    }

    /**
     * Obtiene todos los reportes registrados en el sistema.
     *
     * @return Lista de reportes.
     */
    public List<ReporteEntity> obtenerTodos() {
        return reporteDAO.findAll();
    }

    /**
     * Obtiene los reportes realizados por un usuario concreto.
     *
     * @param idUsuario Identificador del usuario.
     * @return Lista de reportes del usuario.
     */
    public List<ReporteEntity> obtenerPorUsuario(Integer idUsuario) {
        return reporteDAO.findByUsuario(idUsuario);
    }

    /**
     * Obtiene los reportes asociados a una campaña concreta.
     *
     * @param idCampania Identificador de la campaña.
     * @return Lista de reportes de la campaña.
     */
    public List<ReporteEntity> obtenerPorCampania(Integer idCampania) {
        return reporteDAO.findByCampania(idCampania);
    }

    /**
     * Obtiene los reportes que aún no han sido revisados.
     *
     * @return Lista de reportes pendientes.
     */
    public List<ReporteEntity> obtenerPendientes() {
        return reporteDAO.findPendientes();
    }

    /**
     * Obtiene los reportes que ya han sido revisados.
     *
     * @return Lista de reportes resueltos.
     */
    public List<ReporteEntity> obtenerResueltos() {
        return reporteDAO.findResueltos();
    }
}

