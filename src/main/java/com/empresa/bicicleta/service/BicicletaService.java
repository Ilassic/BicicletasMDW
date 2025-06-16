package com.empresa.bicicleta.service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.enums.EstadoDisponibilidad;
import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.repository.BicicletaRepository;

@Service
@Transactional
public class BicicletaService {

    @Autowired
    private BicicletaRepository bicicletaRepository;

    // 1. Crear bicicleta
    public Bicicleta crearBicicleta(Bicicleta bicicleta) {
        bicicleta.setFechaRegistro(new Date());
        bicicleta.setDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
        return bicicletaRepository.save(bicicleta);
    }

    // 2. Eliminar bicicleta
    public void eliminarBicicleta(String codigoBicicleta) {
        bicicletaRepository.deleteById(codigoBicicleta);
    }

    // 3. Listar todas las bicicletas disponibles
    public List<Bicicleta> listarBicicletasDisponibles() {
        return bicicletaRepository.findByDisponibilidadOrderByNombreModeloAsc(EstadoDisponibilidad.DISPONIBLE);
    }

    // Listar todas las bicicletas
    public List<Bicicleta> listarTodasLasBicicletas() {
        return bicicletaRepository.findAll();
    }

    // 4. Buscar bicicletas por tipo/categoría
    public List<Bicicleta> buscarPorCategoria(String categoria) {
        return bicicletaRepository.findByCategoriaIgnoreCaseOrderByPrecioAsc(categoria);
    }

    // Buscar bicicletas disponibles por categoría
    public List<Bicicleta> buscarDisponiblesPorCategoria(String categoria) {
        return bicicletaRepository.findByCategoriaIgnoreCaseAndDisponibilidadOrderByPrecioAsc(categoria, EstadoDisponibilidad.DISPONIBLE);
    }

    // 5. Actualizar estado de bicicleta
    public void actualizarEstadoBicicleta(String codigoBicicleta, EstadoDisponibilidad nuevoEstado) {
        bicicletaRepository.actualizarDisponibilidad(codigoBicicleta, nuevoEstado);
    }

    // 6. Validación de disponibilidad
    public boolean validarDisponibilidad(String codigoBicicleta) {
        return bicicletaRepository.isDisponible(codigoBicicleta);
    }

    // Buscar bicicleta por código
    public Optional<Bicicleta> buscarPorCodigo(String codigoBicicleta) {
        return bicicletaRepository.findById(codigoBicicleta);
    }

    // Buscar por marca
    public List<Bicicleta> buscarPorMarca(String marca) {
        return bicicletaRepository.findByMarcaIgnoreCaseOrderByNombreModeloAsc(marca);
    }

    // Buscar disponibles por marca
    public List<Bicicleta> buscarDisponiblesPorMarca(String marca) {
        return bicicletaRepository.findByMarcaIgnoreCaseAndDisponibilidadOrderByPrecioAsc(marca, EstadoDisponibilidad.DISPONIBLE);
    }

    // Buscar por modelo
    public List<Bicicleta> buscarPorModelo(String nombreModelo) {
        return bicicletaRepository.findByNombreModeloContainingIgnoreCaseOrderByPrecioAsc(nombreModelo);
    }

    // Buscar por rango de precio
    public List<Bicicleta> buscarPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return bicicletaRepository.findByPrecioBetweenOrderByPrecioAsc(precioMin, precioMax);
    }

    // Buscar disponibles por rango de precio
    public List<Bicicleta> buscarDisponiblesPorRangoPrecio(BigDecimal precioMin, BigDecimal precioMax) {
        return bicicletaRepository.findByPrecioBetweenAndDisponibilidadOrderByPrecioAsc(precioMin, precioMax, EstadoDisponibilidad.DISPONIBLE);
    }

    // Buscar con filtros múltiples
    public List<Bicicleta> buscarConFiltros(String marca, String categoria, BigDecimal precioMin, BigDecimal precioMax) {
        return bicicletaRepository.findWithFilters(marca, categoria, precioMin, precioMax);
    }

    // Obtener bicicletas más baratas
    public List<Bicicleta> obtenerBicicletasMasBaratas() {
        return bicicletaRepository.findByDisponibilidadOrderByPrecioAsc(EstadoDisponibilidad.DISPONIBLE);
    }

    // Obtener bicicletas más caras
    public List<Bicicleta> obtenerBicicletasMasCaras() {
        return bicicletaRepository.findByDisponibilidadOrderByPrecioDesc(EstadoDisponibilidad.DISPONIBLE);
    }

    // Obtener todas las marcas
    public List<String> obtenerTodasMarcas() {
        return bicicletaRepository.findDistinctMarcas();
    }

    // Obtener todas las categorías
    public List<String> obtenerTodasCategorias() {
        return bicicletaRepository.findDistinctCategorias();
    }

    // Contar bicicletas disponibles
    public Long contarBicicletasDisponibles() {
        return bicicletaRepository.countByDisponibilidad(EstadoDisponibilidad.DISPONIBLE);
    }

    // Contar bicicletas por marca
    public Long contarPorMarca(String marca) {
        return bicicletaRepository.countByMarcaIgnoreCase(marca);
    }

    // Contar bicicletas por categoría
    public Long contarPorCategoria(String categoria) {
        return bicicletaRepository.countByCategoriaIgnoreCase(categoria);
    }

    // Buscar bicicletas similares
    public List<Bicicleta> buscarSimilares(String marca, String categoria, String codigoExcluir) {
        return bicicletaRepository.findSimilares(marca, categoria, codigoExcluir);
    }

    // Actualizar bicicleta completa
    public Bicicleta actualizarBicicleta(Bicicleta bicicleta) {
        return bicicletaRepository.save(bicicleta);
    }

    // Marcar como alquilada
    public void marcarComoAlquilada(String codigoBicicleta) {
        actualizarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.ALQUILADA);
    }

    // Marcar como disponible
    public void marcarComoDisponible(String codigoBicicleta) {
        actualizarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.DISPONIBLE);
    }

    // Marcar como fuera de servicio
    public void marcarComoFueraDeServicio(String codigoBicicleta) {
        actualizarEstadoBicicleta(codigoBicicleta, EstadoDisponibilidad.FUERA_DE_SERVICIO);
    }

    // Método para verificar si una bicicleta está alquilada actualmente
    public boolean estaAlquilada(String codigoBicicleta) {
        return bicicletaRepository.findById(codigoBicicleta)
                .map(bici -> bici.getDisponibilidad() == EstadoDisponibilidad.ALQUILADA)
                .orElse(false);
    }

    // Método para obtener el estado de disponibilidad como texto
    public String obtenerEstadoTexto(String codigoBicicleta) {
        return bicicletaRepository.findById(codigoBicicleta)
                .map(bici -> {
                    switch (bici.getDisponibilidad()) {
                        case DISPONIBLE:
                            return "Disponible";
                        case ALQUILADA:
                            return "Alquilada";
                        case FUERA_DE_SERVICIO:
                            return "Fuera de servicio";
                        default:
                            return "Estado desconocido";
                    }
                })
                .orElse("No encontrada");
    }

    // Método para convertir usos recomendados a lista
    public List<String> obtenerUsosRecomendados(String usosRecomendados) {
        if (usosRecomendados == null || usosRecomendados.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(usosRecomendados.split(","));
    }

    // Método para convertir usos no recomendados a lista
    public List<String> obtenerUsosNoRecomendados(String usosNoRecomendados) {
        if (usosNoRecomendados == null || usosNoRecomendados.trim().isEmpty()) {
            return List.of();
        }
        return Arrays.asList(usosNoRecomendados.split(","));
    }
}