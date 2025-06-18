package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.empresa.bicicleta.model.Reserva;

@Repository
public interface ReservaCrudRepository extends JpaRepository<Reserva, Integer> {

    @Procedure("CrearReserva")
    Integer crearReserva(
        @Param("p_dni_cliente") String dniCliente,
        @Param("p_codigo_bicicleta") String codigoBicicleta,
        @Param("p_fecha_reserva") Date fechaReserva,
        @Param("p_hora_reserva") String horaReserva,
        @Param("p_duracion_horas") Integer duracionHoras,
        @Param("p_precio_total") BigDecimal precioTotal
    );

    @Procedure("ReservaPorId")
    List<Object[]> reservaPorId(@Param("p_id_reserva") Integer idReserva);

    @Procedure("TodasReservas")
    List<Object[]> todasReservas();

    @Procedure("ActualizarReserva")
    Integer actualizarReserva(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_dni_cliente") String dniCliente,
        @Param("p_codigo_bicicleta") String codigoBicicleta,
        @Param("p_fecha_reserva") Date fechaReserva,
        @Param("p_hora_reserva") String horaReserva,
        @Param("p_duracion_horas") Integer duracionHoras,
        @Param("p_precio_total") BigDecimal precioTotal,
        @Param("p_estado_reserva") String estadoReserva,
        @Param("p_estado_pago") String estadoPago
    );

    @Procedure("EliminarReserva")
    Integer eliminarReserva(@Param("p_id_reserva") Integer idReserva);

    @Procedure("ReservasPorCliente")
    List<Object[]> reservasPorCliente(@Param("p_dni_cliente") String dniCliente);

    @Procedure("ReservasPorEstado")
    List<Object[]> reservasPorEstado(@Param("p_estado_reserva") String estadoReserva);

    @Procedure("ReservasPorFecha")
    List<Object[]> reservasPorFecha(
        @Param("p_fecha_inicio") Date fechaInicio,
        @Param("p_fecha_fin") Date fechaFin
    );

    @Procedure("ActualizarEstadoReserva")
    Integer actualizarEstadoReserva(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_estado_reserva") String estadoReserva
    );

    @Procedure("ActualizarEstadoPago")
    Integer actualizarEstadoPago(
        @Param("p_id_reserva") Integer idReserva,
        @Param("p_estado_pago") String estadoPago
    );

    @Procedure("ContarReservasCliente")
    Integer contarReservasCliente(@Param("p_dni_cliente") String dniCliente);

    @Procedure("CalcularTotalPagadoCliente")
    BigDecimal calcularTotalPagadoCliente(@Param("p_dni_cliente") String dniCliente);
}