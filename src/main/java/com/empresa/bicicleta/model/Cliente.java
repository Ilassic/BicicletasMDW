package com.empresa.bicicleta.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "clientes")
@Getter
@Setter
public class Cliente {

    @Id
    private String dni;

    private String nombre;

    private String apellidos;

    private String telefono;

    private String correoElectronico;

    private String contrasena;

    private LocalDateTime fechaRegistro;
    
}
