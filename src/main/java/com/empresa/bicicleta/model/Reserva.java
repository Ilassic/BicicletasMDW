package com.empresa.bicicleta.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    @Column(name = "fecha_reserva")
    private Date fechaReserva;

    @Column(name = "hora_reserva")
    private Time horaReserva;

    @Column(name = "duracion_horas")
    private Integer duracionHoras;

    @Column(name = "metodo_pago")
    private String metodoPago;

    @Column(name = "precio_total")
    private BigDecimal precioTotal;

    private String estado;

    @Column(name = "fecha_registro")
    private Timestamp fechaRegistro;
}
