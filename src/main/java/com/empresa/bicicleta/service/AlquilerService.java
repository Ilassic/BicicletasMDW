package com.empresa.bicicleta.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.repository.AlquilerRepository;
import com.empresa.bicicleta.repository.BicicletaRepository;
import com.empresa.bicicleta.repository.ReservaRepository;
import com.empresa.bicicleta.enums.EstadoDisponibilidad;
import com.empresa.bicicleta.enums.EstadoReserva;

@Service
@Transactional
public class AlquilerService {

    @Autowired
    private AlquilerRepository alquilerRepository;

    @Autowired
    private BicicletaRepository bicicletaRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    // Crear nuevo alquiler
    public Alquiler crearAlquiler(Alquiler alquiler) {
        return alquilerRepository.save(alquiler);
    }

    // Obtener alquiler por ID
    public Optional<Alquiler> obtenerAlquilerPorId(Integer id) {
        return alquilerRepository.findById(id);
    }

    // Obtener todos los alquileres
    public List<Alquiler> obtenerTodosAlquileres() {
        return alquilerRepository.findAll();
    }

    // Actualizar alquiler
    public Alquiler actualizarAlquiler(Alquiler alquiler) {
        return alquilerRepository.save(alquiler);
    }

    // Eliminar alquiler
    public void eliminarAlquiler(Integer id) {
        alquilerRepository.deleteById(id);
    }

    // Obtener historial de alquileres por DNI cliente
    public List<Alquiler> obtenerHistorialPorCliente(String dniCliente) {
        return alquilerRepository.findByReservaClienteDniOrderByFechaAlquilerDesc(dniCliente);
    }

    // Verificar si cliente tiene alquiler activo
    public Optional<Alquiler> obtenerAlquilerActivoPorCliente(String dniCliente) {
        return alquilerRepository.findAlquilerActivoByCliente(dniCliente);
    }

    // Verificar si cliente tiene alquiler en curso
    public boolean tieneAlquilerActivo(String dniCliente) {
        return alquilerRepository.findAlquilerActivoByCliente(dniCliente).isPresent();
    }

    // Obtener alquiler por ID de reserva
    public Optional<Alquiler> obtenerAlquilerPorReserva(Integer idReserva) {
        return alquilerRepository.findByReservaId(idReserva);
    }

    // Obtener historial de uso de bicicleta
    public List<Alquiler> obtenerHistorialPorBicicleta(String codigoBicicleta) {
        return alquilerRepository.findByReservaBicicletaCodigoBicicletaOrderByFechaAlquilerDesc(codigoBicicleta);
    }

    // Verificar si bicicleta está alquilada
    public boolean bicicletaEstaAlquilada(String codigoBicicleta) {
        return alquilerRepository.existeAlquilerActivoBicicleta(codigoBicicleta);
    }

    // Obtener alquileres por fecha
    public List<Alquiler> obtenerAlquileresPorFecha(Date fecha) {
        return alquilerRepository.findByFechaAlquilerOrderByHoraInicioDesc(fecha);
    }

    // Obtener alquileres en rango de fechas para cliente
    public List<Alquiler> obtenerAlquileresPorClienteYRangoFechas(String dniCliente, Date fechaInicio, Date fechaFin) {
        return alquilerRepository.findAlquileresByClienteAndFechaRange(dniCliente, fechaInicio, fechaFin);
    }

    // Calcular total gastado por cliente
    public BigDecimal calcularTotalGastadoPorCliente(String dniCliente) {
        return alquilerRepository.calcularTotalGastadoByCliente(dniCliente);
    }

    // Contar alquileres de cliente
    public Long contarAlquileresPorCliente(String dniCliente) {
        return alquilerRepository.countByReservaClienteDni(dniCliente);
    }

    // Obtener último alquiler de cliente
    public Optional<Alquiler> obtenerUltimoAlquilerPorCliente(String dniCliente) {
        return alquilerRepository.findUltimoAlquilerByCliente(dniCliente);
    }

    // Obtener alquileres completados por cliente
    public List<Alquiler> obtenerAlquileresCompletadosPorCliente(String dniCliente) {
        return alquilerRepository.findAlquileresCompletadosByCliente(dniCliente);
    }

    // Obtener alquileres por método de pago
    public List<Alquiler> obtenerAlquileresPorClienteYMetodoPago(String dniCliente, String tipoPago) {
        return alquilerRepository.findByClienteAndMetodoPago(dniCliente, tipoPago);
    }

    // Verificar si alquiler pertenece a cliente
    public boolean alquilerPerteneceACliente(Integer idAlquiler, String dniCliente) {
        return alquilerRepository.existeAlquilerForCliente(idAlquiler, dniCliente);
    }

    // Obtener alquileres por rango de costo
    public List<Alquiler> obtenerAlquileresPorClienteYRangoCosto(String dniCliente, BigDecimal costoMin, BigDecimal costoMax) {
        return alquilerRepository.findByClienteAndCostoRange(dniCliente, costoMin, costoMax);
    }

    // Finalizar alquiler - actualizar hora de devolución
    public void finalizarAlquiler(Integer idAlquiler) {
        Time horaActual = Time.valueOf(LocalTime.now());
        alquilerRepository.actualizarHoraDevolucion(idAlquiler, horaActual);
    }

    // Finalizar alquiler con hora específica
    public void finalizarAlquiler(Integer idAlquiler, Time horaDevolucion) {
        alquilerRepository.actualizarHoraDevolucion(idAlquiler, horaDevolucion);
    }

    // Procesar nuevo alquiler completo
    public Alquiler procesarNuevoAlquiler(Alquiler alquiler) {
        // Establecer fecha y hora actual si no están definidas
        if (alquiler.getFechaAlquiler() == null) {
            alquiler.setFechaAlquiler(new Date(System.currentTimeMillis()));
        }
        if (alquiler.getHoraInicio() == null) {
            alquiler.setHoraInicio(Time.valueOf(LocalTime.now()));
        }
        
        return alquilerRepository.save(alquiler);
    }

    // Verificar disponibilidad para nuevo alquiler
    public boolean puedeCrearAlquiler(String dniCliente, String codigoBicicleta) {
        // Verificar que cliente no tenga alquiler activo
        if (tieneAlquilerActivo(dniCliente)) {
            return false;
        }
        
        // Verificar que bicicleta no esté alquilada
        if (bicicletaEstaAlquilada(codigoBicicleta)) {
            return false;
        }
        
        return true;
    }

    // Validar hora de recogida (no puede ser en el pasado)
    public boolean validarHoraRecogida(Date fechaAlquiler, Time horaInicio) {
        Date fechaActual = new Date(System.currentTimeMillis());
        Time horaActual = Time.valueOf(LocalTime.now());
        
        // Si la fecha es posterior a hoy, es válida
        if (fechaAlquiler.after(fechaActual)) {
            return true;
        }
        
        // Si es hoy, la hora debe ser posterior a la actual
        if (fechaAlquiler.equals(fechaActual)) {
            return horaInicio.after(horaActual);
        }
        
        // Si es fecha pasada, no es válida
        return false;
    }

    // Cancelar alquiler (solo si no ha iniciado)
    public boolean cancelarAlquiler(Integer idAlquiler, String dniCliente) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Verificar que pertenece al cliente
        if (!alquiler.getReserva().getCliente().getDni().equals(dniCliente)) {
            return false;
        }
        
        // Solo se puede cancelar si no ha iniciado (fecha futura o misma fecha pero hora futura)
        if (!validarHoraRecogida(alquiler.getFechaAlquiler(), alquiler.getHoraInicio())) {
            return false;
        }
        
        alquilerRepository.deleteById(idAlquiler);
        return true;
    }

    // Calcular costo total basado en horas y precio por hora
    public BigDecimal calcularCostoTotal(Integer totalHoras, BigDecimal precioPorHora) {
        if (totalHoras == null || totalHoras <= 0) {
            return BigDecimal.ZERO;
        }
        
        return precioPorHora.multiply(BigDecimal.valueOf(totalHoras));
    }

    // Calcular duración en horas entre inicio y fin
    public Integer calcularDuracionHoras(Time horaInicio, Time horaFin) {
        if (horaInicio == null || horaFin == null) {
            return 0;
        }
        
        long diferenciaMilis = horaFin.getTime() - horaInicio.getTime();
        long horas = diferenciaMilis / (1000 * 60 * 60);
        
        // Redondear hacia arriba (mínimo 1 hora)
        return Math.max(1, (int) Math.ceil(horas));
    }

    // Finalizar alquiler con cálculo automático de costo
    public Alquiler finalizarAlquilerConCosto(Integer idAlquiler, BigDecimal precioPorHora) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return null;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        Time horaActual = Time.valueOf(LocalTime.now());
        
        // Calcular duración y costo
        Integer duracion = calcularDuracionHoras(alquiler.getHoraInicio(), horaActual);
        BigDecimal costo = calcularCostoTotal(duracion, precioPorHora);
        
        // Actualizar alquiler
        alquiler.setHoraDevolucion(horaActual);
        alquiler.setTotalHoras(duracion);
        alquiler.setCostoTotal(costo);
        
        return alquilerRepository.save(alquiler);
    }

    // Actualizar estado de reserva del alquiler
    public boolean actualizarEstadoReserva(Integer idAlquiler, EstadoReserva nuevoEstado) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        Reserva reserva = alquiler.getReserva();
        reserva.setEstadoReserva(nuevoEstado);
        
        reservaRepository.save(reserva);
        return true;
    }

    // Validar estado de pago antes de procesar
    public boolean validarEstadoPago(Integer idAlquiler) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        return alquilerOpt.get().getMetodoPago() != null;
    }

    // Cambiar disponibilidad de bicicleta
    public void cambiarDisponibilidadBicicleta(String codigoBicicleta, EstadoDisponibilidad nuevaDisponibilidad) {
        bicicletaRepository.actualizarDisponibilidad(codigoBicicleta, nuevaDisponibilidad);
    }

    // Obtener alquileres por estado de reserva
    public List<Alquiler> obtenerAlquileresPorEstadoReserva(EstadoReserva estado) {
        return alquilerRepository.findAll().stream()
                .filter(a -> a.getReserva().getEstadoReserva() == estado)
                .toList();
    }

    // Obtener alquileres por estado de reserva y cliente
    public List<Alquiler> obtenerAlquileresPorEstadoReservaYCliente(String dniCliente, EstadoReserva estado) {
        return alquilerRepository.findByReservaClienteDniOrderByFechaAlquilerDesc(dniCliente)
                .stream()
                .filter(a -> a.getReserva().getEstadoReserva() == estado)
                .toList();
    }

    // Proceso completo de entrega de bicicleta
    @Transactional
    public boolean procesarEntregaBicicleta(Integer idAlquiler) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Validar que el pago esté procesado
        if (!validarEstadoPago(idAlquiler)) {
            return false;
        }
        
        // Validar que la reserva esté en estado PENDIENTE
        if (alquiler.getReserva().getEstadoReserva() != EstadoReserva.PENDIENTE) {
            return false;
        }
        
        // Cambiar estado de reserva a ENTREGADO
        actualizarEstadoReserva(idAlquiler, EstadoReserva.ENTREGADA);
        
        // Cambiar bicicleta a ALQUILADA
        String codigoBicicleta = alquiler.getReserva().getBicicleta().getCodigoBicicleta();
        cambiarDisponibilidadBicicleta(codigoBicicleta, EstadoDisponibilidad.ALQUILADA);
        
        return true;
    }

    // Proceso completo de devolución de bicicleta
    @Transactional
    public boolean procesarDevolucionBicicleta(Integer idAlquiler, BigDecimal precioPorHora) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Validar que la reserva esté en estado ENTREGADO
        if (alquiler.getReserva().getEstadoReserva() != EstadoReserva.ENTREGADA) {
            return false;
        }
        
        // Finalizar alquiler con cálculo de costo
        finalizarAlquilerConCosto(idAlquiler, precioPorHora);
        
        // Cambiar estado de reserva a COMPLETADO
        actualizarEstadoReserva(idAlquiler, EstadoReserva.COMPLETADA);
        
        // Cambiar bicicleta a DISPONIBLE
        String codigoBicicleta = alquiler.getReserva().getBicicleta().getCodigoBicicleta();
        cambiarDisponibilidadBicicleta(codigoBicicleta, EstadoDisponibilidad.DISPONIBLE);
        
        return true;
    }

    // Cancelar alquiler con cambio de estados
    @Transactional
    public boolean cancelarAlquilerCompleto(Integer idAlquiler, String dniCliente) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Verificar que pertenece al cliente
        if (!alquiler.getReserva().getCliente().getDni().equals(dniCliente)) {
            return false;
        }
        
        // Solo se puede cancelar si está PENDIENTE
        if (alquiler.getReserva().getEstadoReserva() != EstadoReserva.PENDIENTE) {
            return false;
        }
        
        // Cambiar estado a CANCELADO
        actualizarEstadoReserva(idAlquiler, EstadoReserva.CANCELADA);
        
        // Asegurar que la bicicleta esté DISPONIBLE
        String codigoBicicleta = alquiler.getReserva().getBicicleta().getCodigoBicicleta();
        cambiarDisponibilidadBicicleta(codigoBicicleta, EstadoDisponibilidad.DISPONIBLE);
        
        return true;
    }

    // Verificar si se puede procesar entrega
    public boolean puedeEntregarBicicleta(Integer idAlquiler) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Debe estar PENDIENTE y con pago procesado
        return alquiler.getReserva().getEstadoReserva() == EstadoReserva.PENDIENTE && 
               validarEstadoPago(idAlquiler);
    }

    // Verificar si se puede procesar devolución
    public boolean puedeRecibirDevolucion(Integer idAlquiler) {
        Optional<Alquiler> alquilerOpt = alquilerRepository.findById(idAlquiler);
        
        if (alquilerOpt.isEmpty()) {
            return false;
        }
        
        Alquiler alquiler = alquilerOpt.get();
        
        // Debe estar ENTREGADO
        return alquiler.getReserva().getEstadoReserva() == EstadoReserva.ENTREGADA;
    }

    // Obtener estadísticas de cliente
    public AlquilerEstadisticas obtenerEstadisticasCliente(String dniCliente) {
        Long totalAlquileres = contarAlquileresPorCliente(dniCliente);
        BigDecimal totalGastado = calcularTotalGastadoPorCliente(dniCliente);
        Optional<Alquiler> ultimoAlquiler = obtenerUltimoAlquilerPorCliente(dniCliente);
        boolean tieneActivo = tieneAlquilerActivo(dniCliente);
        
        return new AlquilerEstadisticas(totalAlquileres, totalGastado, ultimoAlquiler.orElse(null), tieneActivo);
    }

    // Clase interna para estadísticas
    public static class AlquilerEstadisticas {
        private Long totalAlquileres;
        private BigDecimal totalGastado;
        private Alquiler ultimoAlquiler;
        private boolean tieneAlquilerActivo;

        public AlquilerEstadisticas(Long totalAlquileres, BigDecimal totalGastado, Alquiler ultimoAlquiler, boolean tieneAlquilerActivo) {
            this.totalAlquileres = totalAlquileres;
            this.totalGastado = totalGastado;
            this.ultimoAlquiler = ultimoAlquiler;
            this.tieneAlquilerActivo = tieneAlquilerActivo;
        }

        // Getters
        public Long getTotalAlquileres() { return totalAlquileres; }
        public BigDecimal getTotalGastado() { return totalGastado; }
        public Alquiler getUltimoAlquiler() { return ultimoAlquiler; }
        public boolean isTieneAlquilerActivo() { return tieneAlquilerActivo; }
    }
}