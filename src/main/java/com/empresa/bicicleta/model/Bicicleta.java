package com.empresa.bicicleta.model;

import java.util.Date;

import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bicicleta")
@Getter
@Setter
public class Bicicleta {
    @Id
    @Column(name = "codigo_bicicleta")
    private String codigoBicicleta;

    @Column(name = "nombre_modelo")
    private String nombreModelo;

    private String categoria;
    private BigDecimal precio;

    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    private String disponibilidad;
}
