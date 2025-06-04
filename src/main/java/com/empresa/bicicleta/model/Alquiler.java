package com.empresa.bicicleta.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "alquiler")
@Getter
@Setter
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;
    
    @OneToOne
    private Reserva reserva;

    private Date fechaAlquiler;

    private BigDecimal costoTotal;

    private Integer totalHoras;

    private Time horaInicio;

    private Time horaDevolucion;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago")
    private MetodoPago metodoPago;
}

