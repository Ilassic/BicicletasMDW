package com.empresa.bicicleta.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDto {
    private Integer id;
    private String dniCliente;
    private String nombreCliente;
    private String apellidosCliente;
    private String codigoBicicleta;
    private String nombreModeloBicicleta;
    private Date fechaReserva;
    private Time horaReserva;
    private Integer duracionHoras;
    private EstadoPago estadoPago;
    private BigDecimal precioTotal;
    private EstadoReserva estadoReserva;
    private Timestamp fechaRegistro;
}