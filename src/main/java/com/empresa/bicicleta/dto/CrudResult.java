package com.empresa.bicicleta.dto;

public class CrudResult {
    private String status;
    private String mensaje;
    private boolean exitoso;
    
    // Constructores
    public CrudResult() {}
    
    public CrudResult(String status, String mensaje, boolean exitoso) {
        this.status = status;
        this.mensaje = mensaje;
        this.exitoso = exitoso;
    }
    
    // Getters y Setters
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMensaje() {
        return mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    
    public boolean isExitoso() {
        return exitoso;
    }
    
    public void setExitoso(boolean exitoso) {
        this.exitoso = exitoso;
    }
    
    @Override
    public String toString() {
        return "CrudResult{" +
                "status='" + status + '\'' +
                ", mensaje='" + mensaje + '\'' +
                ", exitoso=" + exitoso +
                '}';
    }
}

