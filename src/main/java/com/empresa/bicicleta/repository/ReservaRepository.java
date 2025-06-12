package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {

    // Buscar reservas por DNI del cliente ordenadas por fecha más reciente
    List<Reserva> findByClienteDniOrderByFechaReservaDescHoraReservaDesc(String dniCliente);

    // Buscar reservas activas de un cliente
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dni AND r.estadoReserva = 'ACTIVA'")
    List<Reserva> findReservasActivasByCliente(@Param("dni") String dniCliente);

    // Buscar reserva por código de bicicleta y fecha
    List<Reserva> findByBicicletaCodigoBicicletaAndFechaReserva(String codigoBicicleta, Date fechaReserva);

    // Verificar disponibilidad de bicicleta en fecha y hora específica
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.bicicleta.codigoBicicleta = :codigoBicicleta " +
            "AND r.fechaReserva = :fecha AND r.estadoReserva IN ('ACTIVA', 'CONFIRMADA') " +
            "AND ((r.horaReserva <= :horaInicio AND ADDTIME(r.horaReserva, SEC_TO_TIME(r.duracionHoras * 3600)) > :horaInicio) "
            +
            "OR (r.horaReserva < ADDTIME(:horaInicio, SEC_TO_TIME(:duracionHoras * 3600)) AND r.horaReserva >= :horaInicio))")
    boolean existeConflictoReserva(@Param("codigoBicicleta") String codigoBicicleta, @Param("fecha") Date fecha,
            @Param("horaInicio") Time horaInicio,
            @Param("duracionHoras") Integer duracionHoras);

    // Buscar reservas por estado de pago
    List<Reserva> findByEstadoPagoOrderByFechaRegistroDesc(EstadoPago estadoPago);

    // Buscar reservas por estado de reserva
    List<Reserva> findByEstadoReservaOrderByFechaReservaDesc(EstadoReserva estadoReserva);

    // Buscar reservas por cliente y estado de reserva
    List<Reserva> findByClienteDniAndEstadoReservaOrderByFechaReservaDesc(String dniCliente,
            EstadoReserva estadoReserva);

    // Buscar reservas por cliente y estado de pago
    List<Reserva> findByClienteDniAndEstadoPagoOrderByFechaReservaDesc(String dniCliente, EstadoPago estadoPago);

    // Buscar reservas pendientes de pago de un cliente
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dni AND r.estadoPago = 'PENDIENTE' ORDER BY r.fechaRegistro ASC")
    List<Reserva> findReservasPendientesPagoByCliente(@Param("dni") String dniCliente);

    // Buscar reservas por rango de fechas
    @Query("SELECT r FROM Reserva r WHERE r.fechaReserva BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fechaReserva DESC")
    List<Reserva> findReservasByFechaRange(@Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Buscar reservas por cliente en rango de fechas
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dni AND r.fechaReserva BETWEEN :fechaInicio AND :fechaFin ORDER BY r.fechaReserva DESC")
    List<Reserva> findReservasByClienteAndFechaRange(@Param("dni") String dniCliente,
            @Param("fechaInicio") Date fechaInicio, @Param("fechaFin") Date fechaFin);

    // Buscar reservas por duración específica
    List<Reserva> findByDuracionHorasOrderByFechaRegistroDesc(Integer duracionHoras);

    // Buscar reservas por precio en rango
    @Query("SELECT r FROM Reserva r WHERE r.precioTotal BETWEEN :precioMin AND :precioMax ORDER BY r.fechaReserva DESC")
    List<Reserva> findReservasByPrecioRange(@Param("precioMin") BigDecimal precioMinimo,
            @Param("precioMax") BigDecimal precioMaximo);

    // Contar reservas por cliente
    Long countByClienteDni(String dniCliente);

    // Contar reservas por estado
    Long countByEstadoReserva(EstadoReserva estadoReserva);

    // Calcular total gastado por cliente en reservas pagadas
    @Query("SELECT COALESCE(SUM(r.precioTotal), 0) FROM Reserva r WHERE r.cliente.dni = :dni AND r.estadoPago = 'PAGADO'")
    BigDecimal calcularTotalPagadoByCliente(@Param("dni") String dniCliente);

    // Buscar última reserva de un cliente
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dni ORDER BY r.fechaRegistro DESC LIMIT 1")
    Optional<Reserva> findUltimaReservaByCliente(@Param("dni") String dniCliente);

    // Buscar reserva más próxima de un cliente
    @Query("SELECT r FROM Reserva r WHERE r.cliente.dni = :dni AND r.estadoReserva = 'ACTIVA' " +
            "AND (r.fechaReserva > CURRENT_DATE OR (r.fechaReserva = CURRENT_DATE AND r.horaReserva > CURRENT_TIME)) " +
            "ORDER BY r.fechaReserva ASC, r.horaReserva ASC LIMIT 1")
    Optional<Reserva> findProximaReservaByCliente(@Param("dni") String dniCliente);

    // Actualizar estado de reserva
    @Modifying
    @Query("UPDATE Reserva r SET r.estadoReserva = :nuevoEstado WHERE r.id = :idReserva")
    void actualizarEstadoReserva(@Param("idReserva") Integer idReserva,
            @Param("nuevoEstado") EstadoReserva nuevoEstado);

    // Actualizar estado de pago
    @Modifying
    @Query("UPDATE Reserva r SET r.estadoPago = :nuevoEstado WHERE r.id = :idReserva")
    void actualizarEstadoPago(@Param("idReserva") Integer idReserva, @Param("nuevoEstado") EstadoPago nuevoEstado);

    // Verificar si existe reserva para un cliente específico
    @Query("SELECT COUNT(r) > 0 FROM Reserva r WHERE r.id = :idReserva AND r.cliente.dni = :dni")
    boolean existeReservaForCliente(@Param("idReserva") Integer idReserva, @Param("dni") String dniCliente);

    // Buscar reservas que pueden ser convertidas a alquiler
    @Query("SELECT r FROM Reserva r WHERE r.estadoReserva = 'CONFIRMADA' AND r.estadoPago = 'PAGADO' " +
            "AND r.fechaReserva = CURRENT_DATE AND r.horaReserva <= CURRENT_TIME")
    List<Reserva> findReservasListasParaAlquiler();
}