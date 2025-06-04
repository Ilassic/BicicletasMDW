package com.empresa.bicicleta.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "reservas")
@Getter
@Setter
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dni_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "codigo_bicicleta")
    private Bicicleta bicicleta;
    
    private Date fechaReserva;
    
    private Time horaReserva;

    private Integer duracionHoras;

    @Enumerated(EnumType.STRING)
    private EstadoPago estadoPago;

    private BigDecimal precioTotal;

    @Enumerated(EnumType.STRING)
    private EstadoReserva estadoReserva;

    private Timestamp fechaRegistro;
}
