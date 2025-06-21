package com.empresa.bicicleta.dto;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaRequest {
    
    @NotNull(message = "El DNI del cliente es obligatorio")
    @NotBlank(message = "El DNI no puede estar vacío")
    private String dniCliente;
    
    @NotBlank(message = "El nombre del cliente es obligatorio")
    private String nombreCliente;
    
    private String apellidosCliente;
    
    private String telefonoCliente;
    
    @Email(message = "El formato del email no es válido")
    private String emailCliente;
    
    @NotNull(message = "El código de bicicleta es obligatorio")
    @NotBlank(message = "El código de bicicleta no puede estar vacío")
    private String codigoBicicleta;
    
    @NotNull(message = "La fecha de reserva es obligatoria")
    private Date fechaReserva;
    
    @NotNull(message = "La hora de reserva es obligatoria")
    private Time horaReserva;
    
    @NotNull(message = "La duración en horas es obligatoria")
    @Positive(message = "La duración debe ser mayor a 0")
    private Integer duracionHoras;
    
    private String metodoPago;
    
    @NotNull(message = "El precio total es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    private BigDecimal precioTotal;
    
    private EstadoReserva estadoReserva = EstadoReserva.PENDIENTE;
    
    private EstadoPago estadoPago = EstadoPago.PENDIENTE;
    
    // Constructores
    public ReservaRequest() {}

    public ReservaRequest(String dniCliente, String nombreCliente, String apellidosCliente, 
                         String telefonoCliente, String emailCliente, String codigoBicicleta, 
                         Date fechaReserva, Time horaReserva, Integer duracionHoras, 
                         String metodoPago) {
        this.dniCliente = dniCliente;
        this.nombreCliente = nombreCliente;
        this.apellidosCliente = apellidosCliente;
        this.telefonoCliente = telefonoCliente;
        this.emailCliente = emailCliente;
        this.codigoBicicleta = codigoBicicleta;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.duracionHoras = duracionHoras;
        this.metodoPago = metodoPago;
    }
    
    // Constructor simplificado para lo que necesita el procedimiento almacenado CrearReserva
    public ReservaRequest(String dniCliente, String codigoBicicleta, Date fechaReserva, 
                         Time horaReserva, Integer duracionHoras, BigDecimal precioTotal) {
        this.dniCliente = dniCliente;
        this.codigoBicicleta = codigoBicicleta;
        this.fechaReserva = fechaReserva;
        this.horaReserva = horaReserva;
        this.duracionHoras = duracionHoras;
        this.precioTotal = precioTotal;
    }

    @Override
    public String toString() {
        return "ReservaRequest{" +
                "dniCliente='" + dniCliente + '\'' +
                ", nombreCliente='" + nombreCliente + '\'' +
                ", apellidosCliente='" + apellidosCliente + '\'' +
                ", telefonoCliente='" + telefonoCliente + '\'' +
                ", emailCliente='" + emailCliente + '\'' +
                ", codigoBicicleta='" + codigoBicicleta + '\'' +
                ", fechaReserva=" + fechaReserva +
                ", horaReserva=" + horaReserva +
                ", duracionHoras=" + duracionHoras +
                ", metodoPago='" + metodoPago + '\'' +
                ", precioTotal=" + precioTotal +
                ", estadoReserva=" + estadoReserva +
                ", estadoPago=" + estadoPago +
                '}';
    }
}