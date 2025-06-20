package com.empresa.bicicleta.model;

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
@Table(name = "detalle_alquiler")
@Getter
@Setter
public class DetalleAlquiler {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @ManyToOne
    private Alquiler alquiler;

    @ManyToOne
    @JoinColumn(name = "codigo_bicicleta")
    private Bicicleta bicicleta;

    private Integer horasReservadas;
}
