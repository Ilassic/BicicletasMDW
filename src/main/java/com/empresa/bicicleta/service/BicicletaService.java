package com.empresa.bicicleta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.repository.BicicletaRepository;
import com.empresa.bicicleta.enums.EstadoDisponibilidad;

@Service
public class BicicletaService {
    
    @Autowired
    private BicicletaRepository bicicletaRepository;

    public List<Bicicleta> obtenerTodasLasBicicletas() {
        return bicicletaRepository.findAll();
    }
    
    public Optional<Bicicleta> obtenerBicicletaPorCodigo(String codigoBicicleta) {
        return bicicletaRepository.findByCodigoBicicleta(codigoBicicleta);
    }

    public List<Bicicleta> obtenerBicicletasDisponibles() {
        return bicicletaRepository.findByDisponibilidad(EstadoDisponibilidad.DISPONIBLE.name());
    }

    public boolean esBicicletaDisponible(String codigoBicicleta) {
        Optional<Bicicleta> bicicleta = bicicletaRepository.findByCodigoBicicleta(codigoBicicleta);
        return bicicleta.isPresent() && bicicleta.get().getDisponibilidad() == EstadoDisponibilidad.DISPONIBLE;
    }

    public void cambiarEstadoBicicleta(String codigoBicicleta, EstadoDisponibilidad nuevoEstado) {
        Optional<Bicicleta> bicicletaOpt = bicicletaRepository.findByCodigoBicicleta(codigoBicicleta);
        if (bicicletaOpt.isPresent()) {
            Bicicleta bicicleta = bicicletaOpt.get();
            bicicleta.setDisponibilidad(nuevoEstado);
            bicicletaRepository.save(bicicleta);
        } else {
            throw new RuntimeException("Bicicleta no encontrada con código: " + codigoBicicleta);
        }
    }

    public void marcarComoAlquilada(String codigoBicicleta) {
        cambiarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.ALQUILADA);
    }
    
    public void marcarComoDisponible(String codigoBicicleta) {
        cambiarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.DISPONIBLE);
    }
    
    public void marcarComoMantenimiento(String codigoBicicleta) {
        cambiarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.FUERA_DE_SERVICIO);
    }
}
