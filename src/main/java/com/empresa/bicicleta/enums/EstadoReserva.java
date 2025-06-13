package com.empresa.bicicleta.enums;

public enum EstadoReserva {
    PENDIENTE("Reserva Pendiente"),
    ENTREGADA("Bicicleta Entregada"),
    COMPLETADA("Reserva Completada"),
    CANCELADA("Reserva Cancelada");

    private final String descripcion;

    EstadoReserva(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean puedeTransitarA(EstadoReserva nuevoEstado) {
        switch (this) {
            case PENDIENTE:
                return nuevoEstado == ENTREGADA || nuevoEstado == CANCELADA;
            case ENTREGADA:
                return nuevoEstado == COMPLETADA || nuevoEstado == CANCELADA;
            case COMPLETADA:
                return false; 
            case CANCELADA:
                return false; 
            default:
                return false;
        }
    }
}
