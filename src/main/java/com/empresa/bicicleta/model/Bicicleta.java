package com.empresa.bicicleta.model;

import java.util.Date;

import com.empresa.bicicleta.enums.EstadoDisponibilidad;

import java.math.BigDecimal;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "bicicleta")
@Getter
@Setter
public class Bicicleta {
    @Id
    private String codigoBicicleta;

    private String nombreModelo;

    private String marca;

    private String categoria;
    
    private BigDecimal precio;

    private Date fechaRegistro;

    private String description;

    @Enumerated(EnumType.STRING)
    private EstadoDisponibilidad disponibilidad;

    // Nuevos campos para soportar la estructura del HTML
    @Column(length = 500)
    private String usosRecomendados; // JSON o texto separado por comas

    @Column(length = 500)
    private String usosNoRecomendados; // JSON o texto separado por comas

    private String imagenUrl; // Ruta de la imagen

    @Column(length = 1000)
    private String descripcionDetallada; // Descripción más completa
}