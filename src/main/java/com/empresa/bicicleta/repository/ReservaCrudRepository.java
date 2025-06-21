package com.empresa.bicicleta.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Reserva;

import java.sql.Date;
import java.sql.Time;
import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ReservaCrudRepository extends JpaRepository<Reserva, Long> {
    
    // Procedimiento almacenado para crear reserva
    @Procedure(procedureName = "CrearReserva")
    Long crearReserva(
        @Param("p_dni_cliente") String dniCliente,
        @Param("p_codigo_bicicleta") String codigoBicicleta,
        @Param("p_fecha_reserva") Date fechaReserva,
        @Param("p_hora_reserva") Time horaReserva,
        @Param("p_duracion_horas") Integer duracionHoras,
        @Param("p_precio_total") BigDecimal precioTotal
    );
    
    // Procedimiento almacenado para obtener reserva por ID
    @Procedure(procedureName = "ReservaPorId")
    Reserva obtenerReservaPorId(@Param("p_id_reserva") Integer idReserva);
    
    // Procedimiento almacenado para obtener todas las reservas
    @Procedure(procedureName = "TodasReservas") 
    List<Reserva> obtenerTodasReservas();
    
    // Procedimiento almacenado para buscar reservas por cliente
    @Procedure(procedureName = "ReservasPorCliente")
    List<Reserva> buscarReservasPorCliente(@Param("p_dni_cliente") String dniCliente);
    
    // Procedimiento almacenado para obtener reservas por estado
    @Procedure(procedureName = "ReservasPorEstado")
    List<Reserva> obtenerReservasPorEstado(@Param("p_estado_reserva") String estadoReserva);
    
    // Procedimiento almacenado para obtener reservas por fecha
    @Procedure(procedureName = "ReservasPorFecha")
    List<Reserva> obtenerReservasPorFecha(
        @Param("p_fecha_inicio") Date fechaInicio,
        @Param("p_fecha_fin") Date fechaFin
    );
    
    // Procedimiento almacenado para actualizar reserva completa
    @Procedure(procedureName = "ActualizarReserva")
    Integer actualizarReservaCompleta(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_dni_cliente") String dniCliente,
        @Param("p_codigo_bicicleta") String codigoBicicleta,
        @Param("p_fecha_reserva") Date fechaReserva,
        @Param("p_hora_reserva") Time horaReserva,
        @Param("p_duracion_horas") Integer duracionHoras,
        @Param("p_precio_total") BigDecimal precioTotal,
        @Param("p_estado_reserva") String estadoReserva,
        @Param("p_estado_pago") String estadoPago
    );
    
    // Procedimiento almacenado para actualizar estado de reserva
    @Procedure(procedureName = "ActualizarEstadoReserva")
    Integer actualizarEstadoReserva(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_estado_reserva") String estadoReserva
    );
    
    // Procedimiento almacenado para actualizar estado de pago
    @Procedure(procedureName = "ActualizarEstadoPago")
    Integer actualizarEstadoPago(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_estado_pago") String estadoPago
    );
    
    // Procedimiento almacenado para eliminar reserva
    @Procedure(procedureName = "EliminarReserva")
    Integer eliminarReserva(@Param("p_id_reserva") Integer idReserva);
    
    // Procedimiento almacenado para contar reservas por cliente
    @Procedure(procedureName = "ContarReservasCliente")
    Integer contarReservasCliente(@Param("p_dni_cliente") String dniCliente);
    
    // Procedimiento almacenado para calcular total pagado por cliente
    @Procedure(procedureName = "CalcularTotalPagadoCliente")
    BigDecimal calcularTotalPagadoCliente(@Param("p_dni_cliente") String dniCliente);
}