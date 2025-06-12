package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.enums.EstadoDisponibilidad;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, String>{
    
    // Buscar bicicletas disponibles
    List<Bicicleta> findByDisponibilidadOrderByNombreModeloAsc(EstadoDisponibilidad disponibilidad);
    
    // Buscar bicicletas por marca
    List<Bicicleta> findByMarcaIgnoreCaseOrderByNombreModeloAsc(String marca);
    
    // Buscar bicicletas por categoría
    List<Bicicleta> findByCategoriaIgnoreCaseOrderByPrecioAsc(String categoria);
    
    // Buscar bicicletas por nombre de modelo
    List<Bicicleta> findByNombreModeloContainingIgnoreCaseOrderByPrecioAsc(String nombreModelo);
    
    // Buscar bicicletas en rango de precio
    List<Bicicleta> findByPrecioBetweenOrderByPrecioAsc(BigDecimal precioMin, BigDecimal precioMax);
    
    // Buscar bicicletas disponibles por categoría
    List<Bicicleta> findByCategoriaIgnoreCaseAndDisponibilidadOrderByPrecioAsc(String categoria, EstadoDisponibilidad disponibilidad);
    
    // Buscar bicicletas disponibles por marca
    List<Bicicleta> findByMarcaIgnoreCaseAndDisponibilidadOrderByPrecioAsc(String marca, EstadoDisponibilidad disponibilidad);
    
    // Buscar bicicletas disponibles en rango de precio
    List<Bicicleta> findByPrecioBetweenAndDisponibilidadOrderByPrecioAsc(BigDecimal precioMin, BigDecimal precioMax, EstadoDisponibilidad disponibilidad);
    
    // Buscar por descripción
    List<Bicicleta> findByDescriptionContainingIgnoreCase(String texto);
    
    // Buscar todas las marcas distintas
    @Query("SELECT DISTINCT b.marca FROM Bicicleta b ORDER BY b.marca")
    List<String> findDistinctMarcas();
    
    // Buscar todas las categorías distintas
    @Query("SELECT DISTINCT b.categoria FROM Bicicleta b ORDER BY b.categoria")
    List<String> findDistinctCategorias();
    
    // Contar bicicletas por disponibilidad
    Long countByDisponibilidad(EstadoDisponibilidad disponibilidad);
    
    // Contar bicicletas por marca
    Long countByMarcaIgnoreCase(String marca);
    
    // Contar bicicletas por categoría
    Long countByCategoriaIgnoreCase(String categoria);
    
    // Buscar bicicletas más baratas primero
    List<Bicicleta> findByDisponibilidadOrderByPrecioAsc(EstadoDisponibilidad disponibilidad);
    
    // Buscar bicicletas más caras primero
    List<Bicicleta> findByDisponibilidadOrderByPrecioDesc(EstadoDisponibilidad disponibilidad);
    
    // Buscar bicicletas registradas después de una fecha
    List<Bicicleta> findByFechaRegistroAfterOrderByFechaRegistroDesc(Date fecha);
    
    // Actualizar disponibilidad de bicicleta
    @Modifying
    @Query("UPDATE Bicicleta b SET b.disponibilidad = :nuevaDisponibilidad WHERE b.codigoBicicleta = :codigo")
    void actualizarDisponibilidad(@Param("codigo") String codigoBicicleta, @Param("nuevaDisponibilidad") EstadoDisponibilidad nuevaDisponibilidad);
    
    // Verificar si bicicleta está disponible
    @Query("SELECT b.disponibilidad = 'DISPONIBLE' FROM Bicicleta b WHERE b.codigoBicicleta = :codigo")
    boolean isDisponible(@Param("codigo") String codigoBicicleta);
    
    // Buscar bicicletas similares por marca y categoría (excluyendo una específica)
    @Query("SELECT b FROM Bicicleta b WHERE b.marca = :marca AND b.categoria = :categoria AND b.codigoBicicleta != :codigoExcluir AND b.disponibilidad = 'DISPONIBLE' ORDER BY b.precio ASC")
    List<Bicicleta> findSimilares(@Param("marca") String marca, @Param("categoria") String categoria, @Param("codigoExcluir") String codigoExcluir);
    
    // Buscar bicicletas con filtros múltiples
    @Query("SELECT b FROM Bicicleta b WHERE " +
           "(:marca IS NULL OR LOWER(b.marca) LIKE LOWER(CONCAT('%', :marca, '%'))) AND " +
           "(:categoria IS NULL OR LOWER(b.categoria) LIKE LOWER(CONCAT('%', :categoria, '%'))) AND " +
           "(:precioMin IS NULL OR b.precio >= :precioMin) AND " +
           "(:precioMax IS NULL OR b.precio <= :precioMax) AND " +
           "b.disponibilidad = 'DISPONIBLE' " +
           "ORDER BY b.precio ASC")
    List<Bicicleta> findWithFilters(@Param("marca") String marca, 
                                   @Param("categoria") String categoria,
                                   @Param("precioMin") BigDecimal precioMin,
                                   @Param("precioMax") BigDecimal precioMax);
}