package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.enums.EstadoReserva;
import com.empresa.bicicleta.enums.EstadoPago;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer>{
     
    // Buscar reservas por DNI del cliente
    List<Reserva> findByClienteDni(String dniCliente);
    
    // Buscar reservas por código de bicicleta
    List<Reserva> findByBicicletaCodigoBicicleta(String codigoBicicleta);
    
    // Buscar reservas por fecha de reserva
    List<Reserva> findByFechaReserva(Date fechaReserva);
    
    // Buscar reservas por estado
    List<Reserva> findByEstadoReserva(EstadoReserva estadoReserva);
    
    // Buscar reservas entregadas
    @Query("SELECT r FROM Reserva r WHERE r.estadoReserva = 'ENTREGADO'")
    List<Reserva> findReservasEntregadas();
    
    // Buscar reservas canceladas
    @Query("SELECT r FROM Reserva r WHERE r.estadoReserva = 'CANCELADO'")
    List<Reserva> findReservasCanceladas();
    
    // Buscar reservas completadas
    @Query("SELECT r FROM Reserva r WHERE r.estadoReserva = 'COMPLETADO'")
    List<Reserva> findReservasCompletadas();
    
    // Buscar reservas pendientes
    @Query("SELECT r FROM Reserva r WHERE r.estadoReserva = 'PENDIENTE'")
    List<Reserva> findReservasPendientes();
    
    // Buscar reservas por estado de pago
    List<Reserva> findByEstadoPago(EstadoPago estadoPago);
    
    // Buscar reservas pendientes de pago
    @Query("SELECT r FROM Reserva r WHERE r.estadoPago = 'PENDIENTE'")
    List<Reserva> findReservasPendientesPago();
    
    // Buscar reservas pagadas
    @Query("SELECT r FROM Reserva r WHERE r.estadoPago = 'PAGADO'")
    List<Reserva> findReservasPagadas();
    
    // Buscar reservas en un rango de fechas
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva BETWEEN :fechaInicio AND :fechaFin")
    List<Reserva> findByFechaReservaBetween(
        @Param("fechaInicio") Date fechaInicio, 
        @Param("fechaFin") Date fechaFin
    );
    
    // Buscar reservas después de una fecha
    List<Reserva> findByFechaReservaAfter(Date fecha);
    
    // Buscar reservas antes de una fecha
    List<Reserva> findByFechaReservaBefore(Date fecha);
    
    // Buscar reservas por rango de horas
    @Query("SELECT r FROM Reserva r WHERE r.horaReserva BETWEEN :horaInicio AND :horaFin")
    List<Reserva> findByHoraReservaBetween(
        @Param("horaInicio") Time horaInicio,
        @Param("horaFin") Time horaFin
    );
    
    // Buscar reservas por duración de horas específica
    List<Reserva> findByDuracionHoras(Integer duracionHoras);
    
    // Buscar reservas por duración mayor o igual
    List<Reserva> findByDuracionHorasGreaterThanEqual(Integer duracionHoras);
    
    // Buscar reservas por duración menor o igual
    List<Reserva> findByDuracionHorasLessThanEqual(Integer duracionHoras);
    
    // Buscar reservas por rango de precios
    List<Reserva> findByPrecioTotalBetween(BigDecimal precioMin, BigDecimal precioMax);
    
    // Buscar reservas por precio mayor o igual
    List<Reserva> findByPrecioTotalGreaterThanEqual(BigDecimal precio);
    
    // Buscar reservas por precio menor o igual
    List<Reserva> findByPrecioTotalLessThanEqual(BigDecimal precio);
    
    // Buscar reservas de un cliente por estado
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dniCliente AND r.estadoReserva = :estadoReserva")
    List<Reserva> findByClienteDniAndEstadoReserva(
        @Param("dniCliente") String dniCliente, 
        @Param("estadoReserva") EstadoReserva estadoReserva
    );
    
    // Buscar reservas de una bicicleta por estado
    @Query("SELECT r FROM Reserva r WHERE r.bicicleta.codigoBicicleta = :codigoBicicleta AND r.estadoReserva = :estadoReserva")
    List<Reserva> findByBicicletaCodigoAndEstadoReserva(
        @Param("codigoBicicleta") String codigoBicicleta, 
        @Param("estadoReserva") EstadoReserva estadoReserva
    );
    
    // Verificar disponibilidad de bicicleta en fecha y hora específica
    @Query("SELECT r FROM Reserva r WHERE r.bicicleta.codigoBicicleta = :codigoBicicleta " +
           "AND r.fechaReserva = :fechaReserva " +
           "AND r.horaReserva = :horaReserva " +
           "AND r.estadoReserva IN ('ENTREGADO', 'COMPLETADO')")
    List<Reserva> findConflictingReservas(
        @Param("codigoBicicleta") String codigoBicicleta,
        @Param("fechaReserva") Date fechaReserva,
        @Param("horaReserva") Time horaReserva
    );
      
    // Buscar reservas registradas después de una fecha
    List<Reserva> findByFechaRegistroAfter(Timestamp fecha);
    
    // Buscar reservas registradas antes de una fecha
    List<Reserva> findByFechaRegistroBefore(Timestamp fecha);
    
    // Contar reservas por estado
    @Query("SELECT r.estadoReserva, COUNT(r) FROM Reserva r GROUP BY r.estadoReserva")
    List<Object[]> countByEstadoReserva();
    
    // Contar reservas por estado de pago
    @Query("SELECT r.estadoPago, COUNT(r) FROM Reserva r GROUP BY r.estadoPago")
    List<Object[]> countByEstadoPago();
    
    // Contar reservas por cliente
    @Query("SELECT r.cliente.dni, r.cliente.nombre, r.cliente.apellidos, COUNT(r) " +
           "FROM Reserva r GROUP BY r.cliente.dni, r.cliente.nombre, r.cliente.apellidos " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> countReservasPorCliente();
    
    // Contar reservas por bicicleta
    @Query("SELECT r.bicicleta.codigoBicicleta, r.bicicleta.nombreModelo, COUNT(r) " +
           "FROM Reserva r GROUP BY r.bicicleta.codigoBicicleta, r.bicicleta.nombreModelo " +
           "ORDER BY COUNT(r) DESC")
    List<Object[]> countReservasPorBicicleta();
    
    // Contar reservas por mes
    @Query("SELECT YEAR(r.fechaReserva) as año, MONTH(r.fechaReserva) as mes, COUNT(r) as total " +
           "FROM Reserva r " +
           "GROUP BY YEAR(r.fechaReserva), MONTH(r.fechaReserva) " +
           "ORDER BY año DESC, mes DESC")
    List<Object[]> countReservasPorMes();

    // Buscar reservas activas (entregadas) para hoy
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva = CURRENT_DATE AND r.estadoReserva = 'ENTREGADO'")
    List<Reserva> findReservasActivasHoy();    
}