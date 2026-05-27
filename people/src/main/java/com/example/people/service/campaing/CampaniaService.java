package com.example.people.service.campaing;

import com.example.people.dao.campaing.CampaniaDAO;
import com.example.people.dao.payment.PagoDAO;
import com.example.people.entity.campaing.CampaniaEntity;
import com.example.people.entity.payment.PagosEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CampaniaService {

    private final CampaniaDAO campaniaDAO;
    private final PagoDAO pagoDAO;

    @Autowired
    public CampaniaService(CampaniaDAO campaniaDAO, PagoDAO pagoDAO) {
        this.campaniaDAO = campaniaDAO;
        this.pagoDAO = pagoDAO;
    }

    public CampaniaEntity crearCampania(CampaniaEntity campania) {
        if (campania.getFechaFin().isBefore(campania.getFechaInicio()))
            throw new IllegalArgumentException("La fecha fin no puede ser anterior a la fecha inicio");
        if (campania.getMontoActual() == null)
            campania.setMontoActual(BigDecimal.ZERO);
        campania.setEstado("ACTIVA");
        return campaniaDAO.save(campania);
    }

    public CampaniaEntity actualizarCampania(CampaniaEntity campania) {
        return campaniaDAO.save(campania);
    }

    public CampaniaEntity obtenerPorId(Integer id) {
        return campaniaDAO.findById(id);
    }

    public List<CampaniaEntity> listarActivas() {
        return campaniaDAO.findActivas();
    }

    // ← NUEVO: filtra por categoría en BD usando la relación ManyToMany
    public List<CampaniaEntity> listarPorCategoria(Integer idCategoria) {
        return campaniaDAO.findByCategoria(idCategoria);
    }

    public void cerrarCampania(Integer idCampania) {
        campaniaDAO.cerrarCampania(idCampania);
    }

    public BigDecimal calcularTotalRecaudado(Integer idCampania) {
        List<PagosEntity> pagos = pagoDAO.findByCampania(idCampania);
        return pagos.stream()
                .map(PagosEntity::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}