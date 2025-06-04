package com.empresa.bicicleta.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.empresa.bicicleta.model.MetodoPago;


@Repository
public interface MetodoPagoRepository extends JpaRepository<MetodoPago, Integer>{
    
    // Buscar por tipo de pago
    List<MetodoPago> findByTipoPago(String tipoPago);

    // Buscar por tipo de pago específico (único)
    Optional<MetodoPago> findByTipoPagoIgnoreCase(String tipoPago);
    
    // Verificar si existe un tipo de pago
    boolean existsByTipoPago(String tipoPago);

     // Buscar métodos de pago con descripción no nula
    List<MetodoPago> findByDescripcionIsNotNull();
    
    // Buscar métodos de pago sin descripción
    List<MetodoPago> findByDescripcionIsNull();

    // Buscar por descripción
    List<MetodoPago> findByDescripcion(String descripcion);

    // Buscar por tipo de pago que contenga texto
    @Query("SELECT m FROM MetodoPago m WHERE LOWER(m.tipoPago) LIKE LOWER(CONCAT('%', :texto, '%'))")
    List<MetodoPago> findByTipoPagoContaining(@Param("texto") String texto);

    // Obtener todos los tipos de pago únicos
    @Query("SELECT DISTINCT m.tipoPago FROM MetodoPago m ORDER BY m.tipoPago")
    List<String> findDistinctTiposPago();

    // Contar métodos de pago por tipo
    @Query("SELECT m.tipoPago, COUNT(m) FROM MetodoPago m GROUP BY m.tipoPago ORDER BY COUNT(m) DESC")
    List<Object[]> countMetodosPagoPorTipo();
    
    // Buscar métodos de pago ordenados por tipo
    List<MetodoPago> findAllByOrderByTipoPagoAsc();
    
    // Buscar métodos de pago ordenados por ID
    List<MetodoPago> findAllByOrderByIdAsc();

    //Verificar disponibilidad de tipo de pago para actualización
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END FROM MetodoPago m " +
           "WHERE m.tipoPago = :tipoPago AND m.id != :id")
    boolean existsTipoPagoForOtherMetodo(@Param("tipoPago") String tipoPago, @Param("id") Integer id);
}
