package com.empresa.bicicleta.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.model.MetodoPago;
import com.empresa.bicicleta.repository.MetodoPagoRepository;

@Service
@Transactional
public class MetodoPagoService {

    @Autowired
    private MetodoPagoRepository metodoPagoRepository;

    // Crear método de pago
    public MetodoPago crearMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    // Actualizar método de pago
    public MetodoPago actualizarMetodoPago(MetodoPago metodoPago) {
        return metodoPagoRepository.save(metodoPago);
    }

    // Eliminar método de pago
    public void eliminarMetodoPago(Integer id) {
        metodoPagoRepository.deleteById(id);
    }

    // Listar todos los métodos de pago
    public List<MetodoPago> listarTodosMetodosPago() {
        return metodoPagoRepository.findAllByOrderByTipoPagoAsc();
    }

    // Buscar método de pago por ID
    public Optional<MetodoPago> buscarPorId(Integer id) {
        return metodoPagoRepository.findById(id);
    }

    // Buscar método de pago por tipo
    public Optional<MetodoPago> buscarPorTipo(String tipoPago) {
        return metodoPagoRepository.findByTipoPagoIgnoreCase(tipoPago);
    }

    // Buscar métodos que contengan texto en tipo
    public List<MetodoPago> buscarPorTextoEnTipo(String texto) {
        return metodoPagoRepository.findByTipoPagoContainingIgnoreCase(texto);
    }

    // Buscar métodos que contengan texto en descripción
    public List<MetodoPago> buscarPorTextoEnDescripcion(String texto) {
        return metodoPagoRepository.findByDescripcionContainingIgnoreCase(texto);
    }

    // Verificar si existe método de pago por tipo
    public boolean existeMetodoPagoPorTipo(String tipoPago) {
        return metodoPagoRepository.existsByTipoPagoIgnoreCase(tipoPago);
    }

    // Validar datos del método de pago
    public boolean validarDatos(MetodoPago metodoPago) {
        if (metodoPago.getTipoPago() == null || metodoPago.getTipoPago().trim().isEmpty()) {
            return false;
        }
        if (metodoPago.getDescripcion() == null || metodoPago.getDescripcion().trim().isEmpty()) {
            return false;
        }
        return true;
    }

    // Obtener métodos de pago activos (que tienen alquileres)
    public List<MetodoPago> obtenerMetodosPagoActivos() {
        return metodoPagoRepository.findMetodosPagoActivos();
    }

    // Obtener métodos de pago no utilizados
    public List<MetodoPago> obtenerMetodosPagoNoUtilizados() {
        return metodoPagoRepository.findMetodosPagoNoUtilizados();
    }

    // Obtener método de pago más utilizado
    public Optional<MetodoPago> obtenerMetodoPagoMasUtilizado() {
        return metodoPagoRepository.findMetodoPagoMasUtilizado();
    }

    // Contar alquileres por método de pago
    public Long contarAlquileresPorMetodo(Integer idMetodoPago) {
        return metodoPagoRepository.countAlquileresByMetodoPago(idMetodoPago);
    }

    // Contar total de métodos de pago
    public long contarTotalMetodosPago() {
        return metodoPagoRepository.count();
    }

    // Verificar si se puede eliminar (no tiene alquileres asociados)
    public boolean puedeEliminar(Integer id) {
        Long cantidadAlquileres = metodoPagoRepository.countAlquileresByMetodoPago(id);
        return cantidadAlquileres == 0;
    }
}