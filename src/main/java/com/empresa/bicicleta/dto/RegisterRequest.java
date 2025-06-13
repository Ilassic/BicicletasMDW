package com.empresa.bicicleta.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterRequest {
    private String dni;
    private String nombre;
    private String apellidos;
    private String telefono;
    private String email;
    private String password;
    private String confirmPassword;
}