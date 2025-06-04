package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.model.MetodoPago;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer>{
     // Buscar por cliente
    List<Alquiler> findByCliente(Cliente cliente);
    // Buscar por DNI del cliente
    List<Alquiler> findByCliente_Dni(String dni);
    
    // Buscar por fecha de alquiler
    List<Alquiler> findByFechaAlquiler(Date fechaAlquiler);

    // Buscar por método de pago
    List<Alquiler> findByMetodoPago(MetodoPago metodoPago);
    
    // Buscar por tipo de pago
    List<Alquiler> findByMetodoPago_TipoPago(String tipoPago);
    
    // Buscar por ID de método de pago
    List<Alquiler> findByMetodoPago_Id(Integer idMetodoPago);
    
    // Buscar por costo total
    List<Alquiler> findByCostoTotal(BigDecimal costoTotal);
    
    // Buscar por rango de costo
    List<Alquiler> findByCostoTotalBetween(BigDecimal costoMinimo, BigDecimal costoMaximo);

     // Contar alquileres por fecha
    Long countByFechaAlquiler(Date fecha);

    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a")
    BigDecimal sumTotalIngresos();
    
    // Suma de ingresos por cliente
    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a WHERE a.cliente.dni = :dni")
    BigDecimal sumIngresosByCliente(@Param("dni") String dni);
    
    // Suma de ingresos por fecha
    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a WHERE a.fechaAlquiler = :fecha")
    BigDecimal sumIngresosByFecha(@Param("fecha") Date fecha);
    
    // Suma de ingresos en rango de fechas
    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a WHERE a.fechaAlquiler BETWEEN :fechaInicio AND :fechaFin")
    BigDecimal sumIngresosByFechaRange(
        @Param("fechaInicio") Date fechaInicio, 
        @Param("fechaFin") Date fechaFin
    );
    // Promedio de costo por alquiler
    @Query("SELECT AVG(a.costoTotal) FROM Alquiler a")
    BigDecimal getPromedioCostoAlquiler();
    
    // Promedio de horas por alquiler
    @Query("SELECT AVG(a.totalHoras) FROM Alquiler a")
    Double getPromedioHorasAlquiler();
    
     // Clientes con más alquileres
    @Query("SELECT a.cliente.dni, a.cliente.nombre, a.cliente.apellidos, COUNT(a) as totalAlquileres " +
           "FROM Alquiler a " +
           "GROUP BY a.cliente.dni, a.cliente.nombre, a.cliente.apellidos " +
           "ORDER BY totalAlquileres DESC")
    List<Object[]> getClientesConMasAlquileres();

     // Total de alquileres
    @Query("SELECT COUNT(a) FROM Alquiler a")
    Long countTotalAlquileres();
}
