package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.empresa.bicicleta.model.Bicicleta;

@Repository
public interface BicicletaRepository extends JpaRepository<Bicicleta, String> {
    // Búsqueda por código de bicicleta
    Optional<Bicicleta> findByCodigoBicicleta(String codigoBicicleta);
    
    // Verificar si existe una bicicleta por código
    boolean existsByCodigoBicicleta(String codigoBicicleta);
    
    // Buscar por nombre del modelo
    List<Bicicleta> findByNombreModelo(String nombreModelo);
    
    // Buscar por nombre del modelo (búsqueda parcial)
    @Query("SELECT b FROM Bicicleta b WHERE LOWER(b.nombreModelo) LIKE LOWER(CONCAT('%', :modelo, '%'))")
    List<Bicicleta> findByNombreModeloContaining(@Param("modelo") String modelo);
    
    // Buscar por categoría
    List<Bicicleta> findByCategoria(String categoria);
    
    // Buscar por disponibilidad
    List<Bicicleta> findByDisponibilidad(String disponibilidad);
    
    // Buscar bicicletas disponibles
    @Query("SELECT b FROM Bicicleta b WHERE b.disponibilidad = 'disponible'")
    List<Bicicleta> findBicicletasDisponibles();
    
    // Buscar bicicletas en mantenimiento
    @Query("SELECT b FROM Bicicleta b WHERE b.disponibilidad = 'mantenimiento'")
    List<Bicicleta> findBicicletasEnMantenimiento();
    
    // Buscar bicicletas alquiladas
    @Query("SELECT b FROM Bicicleta b WHERE b.disponibilidad = 'alquilada'")
    List<Bicicleta> findBicicletasAlquiladas();
    
    // Buscar por rango de precios
    List<Bicicleta> findByPrecioBetween(BigDecimal precioMinimo, BigDecimal precioMaximo);
    
    // Buscar por precio menor o igual
    List<Bicicleta> findByPrecioLessThanEqual(BigDecimal precio);
    
    // Buscar por precio mayor o igual
    List<Bicicleta> findByPrecioGreaterThanEqual(BigDecimal precio);
    
    // Buscar por categoría y disponibilidad
    List<Bicicleta> findByCategoriaAndDisponibilidad(String categoria, String disponibilidad);
    
    // Buscar bicicletas disponibles por categoría
    @Query("SELECT b FROM Bicicleta b WHERE b.categoria = :categoria AND b.disponibilidad = 'disponible'")
    List<Bicicleta> findBicicletasDisponiblesByCategoria(@Param("categoria") String categoria);
    
    // Buscar bicicletas registradas en un rango de fechas
    @Query("SELECT b FROM Bicicleta b WHERE b.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Bicicleta> findByFechaRegistroBetween(
        @Param("fechaInicio") Date fechaInicio, 
        @Param("fechaFin") Date fechaFin
    );
    
    // Buscar bicicletas registradas después de una fecha
    List<Bicicleta> findByFechaRegistroAfter(Date fecha);
    
    // Buscar bicicletas registradas antes de una fecha
    List<Bicicleta> findByFechaRegistroBefore(Date fecha);
    
    // Contar bicicletas por categoría
    @Query("SELECT b.categoria, COUNT(b) FROM Bicicleta b GROUP BY b.categoria")
    List<Object[]> countBicicletasPorCategoria();
    
    // Contar bicicletas por disponibilidad
    @Query("SELECT b.disponibilidad, COUNT(b) FROM Bicicleta b GROUP BY b.disponibilidad")
    List<Object[]> countBicicletasPorDisponibilidad();
    
    // Obtener categorías únicas
    @Query("SELECT DISTINCT b.categoria FROM Bicicleta b ORDER BY b.categoria")
    List<String> findDistinctCategorias();
    
    // Precio promedio de bicicletas
    @Query("SELECT AVG(b.precio) FROM Bicicleta b")
    BigDecimal getPromedioPrecioBicicletas();
    
    // Precio promedio por categoría
    @Query("SELECT b.categoria, AVG(b.precio) FROM Bicicleta b GROUP BY b.categoria")
    List<Object[]> getPromedioPrecioPorCategoria();
}
