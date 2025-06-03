package com.empresa.bicicleta.model;

import java.math.BigDecimal;
import java.sql.Date;
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
@Table(name = "alquiler")
@Getter
@Setter
public class Alquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_alquiler")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "dni_cliente")
    private Cliente cliente;

    @Column(name = "fecha_alquiler")
    private Date fechaAlquiler;

    @Column(name = "costo_total")
    private BigDecimal costoTotal;

    @Column(name = "cantidad_bicicletas")
    private Integer cantidadBicicletas;

    @Column(name = "total_horas")
    private Integer totalHoras;

    @ManyToOne
    @JoinColumn(name = "id_metodo_pago")
    private MetodoPago metodoPago;
}

