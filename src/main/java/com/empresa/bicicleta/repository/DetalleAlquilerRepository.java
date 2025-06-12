package com.empresa.bicicleta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.DetalleAlquiler;

@Repository
public interface DetalleAlquilerRepository extends JpaRepository<DetalleAlquiler, Integer>{
    
    // Buscar detalles por ID de alquiler
    List<DetalleAlquiler> findByAlquilerId(Integer idAlquiler);
    
    // Buscar detalles por código de bicicleta
    List<DetalleAlquiler> findByBicicletaCodigoBicicletaOrderByIdDesc(String codigoBicicleta);
    
    // Buscar detalle específico por alquiler y bicicleta
    Optional<DetalleAlquiler> findByAlquilerIdAndBicicletaCodigoBicicleta(Integer idAlquiler, String codigoBicicleta);
    
    // Buscar detalles por cliente a través del alquiler
    @Query("SELECT da FROM DetalleAlquiler da WHERE da.alquiler.reserva.cliente.dni = :dni ORDER BY da.id DESC")
    List<DetalleAlquiler> findDetallesByCliente(@Param("dni") String dniCliente);
    
    // Buscar detalles por horas reservadas específicas
    List<DetalleAlquiler> findByHorasReservadas(Integer horasReservadas);
    
    // Buscar detalles en rango de horas
    List<DetalleAlquiler> findByHorasReservadasBetween(Integer horasMin, Integer horasMax);
    
    // Contar total de horas reservadas por bicicleta
    @Query("SELECT COALESCE(SUM(da.horasReservadas), 0) FROM DetalleAlquiler da WHERE da.bicicleta.codigoBicicleta = :codigoBicicleta")
    Long sumHorasReservadasByBicicleta(@Param("codigoBicicleta") String codigoBicicleta);
    
    // Contar total de horas reservadas por cliente
    @Query("SELECT COALESCE(SUM(da.horasReservadas), 0) FROM DetalleAlquiler da WHERE da.alquiler.reserva.cliente.dni = :dni")
    Long sumHorasReservadasByCliente(@Param("dni") String dniCliente);
    
    // Verificar si existe detalle para un alquiler específico
    boolean existsByAlquilerId(Integer idAlquiler);
    
    // Contar detalles por bicicleta
    Long countByBicicletaCodigoBicicleta(String codigoBicicleta);
    
    // Buscar último detalle de una bicicleta
    @Query("SELECT da FROM DetalleAlquiler da WHERE da.bicicleta.codigoBicicleta = :codigoBicicleta ORDER BY da.id DESC LIMIT 1")
    Optional<DetalleAlquiler> findUltimoDetalleByBicicleta(@Param("codigoBicicleta") String codigoBicicleta);
    
    // Buscar detalles de alquileres activos por bicicleta
    @Query("SELECT da FROM DetalleAlquiler da WHERE da.bicicleta.codigoBicicleta = :codigoBicicleta AND da.alquiler.horaDevolucion IS NULL")
    List<DetalleAlquiler> findDetallesActivosByBicicleta(@Param("codigoBicicleta") String codigoBicicleta);
    
    // Eliminar detalles por ID de alquiler
    void deleteByAlquilerId(Integer idAlquiler);
    
    // Verificar si existe detalle para cliente específico
    @Query("SELECT COUNT(da) > 0 FROM DetalleAlquiler da WHERE da.id = :idDetalle AND da.alquiler.reserva.cliente.dni = :dni")
    boolean existeDetalleForCliente(@Param("idDetalle") Integer idDetalle, @Param("dni") String dniCliente);
}