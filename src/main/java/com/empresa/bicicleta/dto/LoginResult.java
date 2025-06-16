package com.empresa.bicicleta.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResult {
    private String status;
    private boolean exitoso;
    
    public LoginResult() {
        this.exitoso = false;
    }
    
    /**
     * Verifica si el login fue exitoso
     * @return
     */
    public boolean isExitoso() {
        return exitoso;
    }
    /**
     * Obtiene un mensaje descriptivo del estado
     * @return 
     */
    public String getMensajeDescriptivo() {
        switch (status) {
            case "SUCCESS":
                return "Inicio de sesión exitoso";
            case "USER_NOT_EXISTS":
                return "El usuario no existe";
            case "INCORRECT_PASSWORD":
                return "Contraseña incorrecta";
            case "ERROR":
                return "Error interno del servidor";
            default:
                return "Estado desconocido";
        }
    }
}
