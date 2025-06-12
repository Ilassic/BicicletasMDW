package com.empresa.bicicleta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.MetodoPago;

@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer>{
    
    // Buscar método de pago por tipo
    Optional<MetodoPago> findByTipoPago(String tipoPago);
    
    // Buscar métodos de pago por tipo (ignora mayúsculas/minúsculas)
    Optional<MetodoPago> findByTipoPagoIgnoreCase(String tipoPago);
    
    // Buscar todos los métodos ordenados por tipo
    List<MetodoPago> findAllByOrderByTipoPagoAsc();
    
    // Buscar métodos que contengan texto específico en tipo de pago
    List<MetodoPago> findByTipoPagoContainingIgnoreCase(String texto);
    
    // Buscar métodos que contengan texto específico en descripción
    List<MetodoPago> findByDescripcionContainingIgnoreCase(String texto);
    
    // Verificar si existe método de pago por tipo
    boolean existsByTipoPago(String tipoPago);
    
    // Verificar si existe método de pago por tipo (ignora mayúsculas/minúsculas)
    boolean existsByTipoPagoIgnoreCase(String tipoPago);
    
    // Contar cuántos alquileres usan este método de pago
    @Query("SELECT COUNT(a) FROM Alquiler a WHERE a.metodoPago.id = :idMetodoPago")
    Long countAlquileresByMetodoPago(@Param("idMetodoPago") Integer idMetodoPago);
    
    // Buscar métodos de pago activos (que tienen alquileres asociados)
    @Query("SELECT DISTINCT mp FROM MetodoPago mp INNER JOIN Alquiler a ON a.metodoPago.id = mp.id")
    List<MetodoPago> findMetodosPagoActivos();
    
    // Buscar métodos de pago no utilizados
    @Query("SELECT mp FROM MetodoPago mp WHERE mp.id NOT IN (SELECT DISTINCT a.metodoPago.id FROM Alquiler a WHERE a.metodoPago IS NOT NULL)")
    List<MetodoPago> findMetodosPagoNoUtilizados();
    
    // Buscar método de pago más utilizado
    @Query("SELECT a.metodoPago FROM Alquiler a GROUP BY a.metodoPago ORDER BY COUNT(a) DESC LIMIT 1")
    Optional<MetodoPago> findMetodoPagoMasUtilizado();
}