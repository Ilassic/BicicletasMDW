package com.empresa.bicicleta.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.DetalleAlquiler;
import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Bicicleta;

@Repository
public interface DetalleAlquilerRepository extends JpaRepository<DetalleAlquiler, Integer>{

    // Buscar por ID de alquiler
    List<DetalleAlquiler> findByAlquiler_Id(Integer idAlquiler);
    
    // Buscar por alquiler
    List<DetalleAlquiler> findByAlquiler(Alquiler alquiler);
    
    // Buscar por código de bicicleta
    List<DetalleAlquiler> findByBicicleta_CodigoBicicleta(String codigoBicicleta);
    
    // Buscar por bicicleta
    List<DetalleAlquiler> findByBicicleta(Bicicleta bicicleta);
    
    // Buscar por horas reservadas
    List<DetalleAlquiler> findByHorasReservadas(Integer horasReservadas);
    
    // Buscar por rango de horas reservadas
    List<DetalleAlquiler> findByHorasReservadasBetween(Integer horasMinimas, Integer horasMaximas);
    
    // Buscar por horas reservadas mayor o igual
    List<DetalleAlquiler> findByHorasReservadasGreaterThanEqual(Integer horas);
    
    // Buscar por horas reservadas menor o igual
    List<DetalleAlquiler> findByHorasReservadasLessThanEqual(Integer horas);
    
    // Buscar detalle específico por alquiler y bicicleta
    Optional<DetalleAlquiler> findByAlquilerAndBicicleta(Alquiler alquiler, Bicicleta bicicleta);
    
    // Buscar por ID de alquiler y código de bicicleta
    Optional<DetalleAlquiler> findByAlquiler_IdAndBicicleta_CodigoBicicleta(
        Integer idAlquiler, String codigoBicicleta);
    
    // Verificar si existe detalle para un alquiler específico
    boolean existsByAlquiler_Id(Integer idAlquiler);
    
    // Verificar si existe detalle para una bicicleta específica
    boolean existsByBicicleta_CodigoBicicleta(String codigoBicicleta);
    
    // Contar detalles por alquiler
    Long countByAlquiler_Id(Integer idAlquiler);
    
    // Contar detalles por bicicleta
    Long countByBicicleta_CodigoBicicleta(String codigoBicicleta);
    
    // Suma total de horas reservadas por alquiler
    @Query("SELECT SUM(d.horasReservadas) FROM DetalleAlquiler d WHERE d.alquiler.id = :idAlquiler")
    Integer sumHorasReservadasByAlquiler(@Param("idAlquiler") Integer idAlquiler);
    
    // Suma total de horas reservadas por bicicleta
    @Query("SELECT SUM(d.horasReservadas) FROM DetalleAlquiler d WHERE d.bicicleta.codigoBicicleta = :codigoBicicleta")
    Integer sumHorasReservadasByBicicleta(@Param("codigoBicicleta") String codigoBicicleta);
    
    // Obtener bicicletas más alquiladas (con más horas)
    @Query("SELECT d.bicicleta.codigoBicicleta, d.bicicleta.nombreModelo, SUM(d.horasReservadas) as totalHoras " +
           "FROM DetalleAlquiler d " +
           "GROUP BY d.bicicleta.codigoBicicleta, d.bicicleta.nombreModelo " +
           "ORDER BY totalHoras DESC")
    List<Object[]> findBicicletasMasAlquiladas();
    
    // Promedio de horas reservadas por alquiler
    @Query("SELECT AVG(d.horasReservadas) FROM DetalleAlquiler d")
    Double getPromedioHorasReservadas();
      
    // Total de detalles de alquiler
    @Query("SELECT COUNT(d) FROM DetalleAlquiler d")
    Long countTotalDetalles();
    
    // Eliminar detalles por ID de alquiler
    void deleteByAlquiler_Id(Integer idAlquiler);
    
    // Eliminar detalles por código de bicicleta
    void deleteByBicicleta_CodigoBicicleta(String codigoBicicleta);
    
}
