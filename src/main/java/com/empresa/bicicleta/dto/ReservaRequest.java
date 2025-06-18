package com.empresa.bicicleta.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequest {
    
    @NotNull(message = "El DNI del cliente es obligatorio")
    private String dniCliente;
    
    @NotNull(message = "El código de bicicleta es obligatorio")
    private String codigoBicicleta;
    
    @NotNull(message = "La fecha de reserva es obligatoria")
    private Date fechaReserva;
    
    @NotNull(message = "La hora de reserva es obligatoria")
    private Time horaReserva;
    
    @NotNull(message = "La duración en horas es obligatoria")
    @Positive(message = "La duración debe ser mayor a 0")
    private Integer duracionHoras;
    
    @NotNull(message = "El precio total es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precioTotal;
    
    private EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;
    
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;
}