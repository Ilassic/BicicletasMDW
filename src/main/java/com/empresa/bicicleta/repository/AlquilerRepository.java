package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.MetodoPago;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer>{
    
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
    
    // Suma de ingresos por cliente (a través de reserva)
    @Query("SELECT SUM(a.costoTotal) FROM Alquiler a WHERE a.reserva.cliente.dni = :dni")
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
    
    // Clientes con más alquileres (a través de reserva)
    @Query("SELECT a.reserva.cliente.dni, a.reserva.cliente.nombre, a.reserva.cliente.apellidos, COUNT(a) as totalAlquileres " +
           "FROM Alquiler a " +
           "GROUP BY a.reserva.cliente.dni, a.reserva.cliente.nombre, a.reserva.cliente.apellidos " +
           "ORDER BY totalAlquileres DESC")
    List<Object[]> getClientesConMasAlquileres();

    // Total de alquileres
    @Query("SELECT COUNT(a) FROM Alquiler a")
    Long countTotalAlquileres();
    
    // Buscar alquileres por DNI del cliente (a través de reserva)
    List<Alquiler> findByReservaClienteDni(String dniCliente);
    
    // Buscar alquileres por código de bicicleta (a través de reserva)
    List<Alquiler> findByReservaBicicletaCodigoBicicleta(String codigoBicicleta);
    
    // Buscar alquileres por ID de reserva
    List<Alquiler> findByReservaId(Integer idReserva);
    
    // Contar alquileres por método de pago
    @Query("SELECT a.metodoPago.tipoPago, COUNT(a) FROM Alquiler a GROUP BY a.metodoPago.tipoPago")
    List<Object[]> countByMetodoPago();
    
    // Ingresos por método de pago
    @Query("SELECT a.metodoPago.tipoPago, SUM(a.costoTotal) FROM Alquiler a GROUP BY a.metodoPago.tipoPago")
    List<Object[]> sumIngresosByMetodoPago();
}