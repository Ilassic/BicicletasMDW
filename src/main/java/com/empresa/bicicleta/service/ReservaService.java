package com.empresa.bicicleta.service;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.dto.ReservaRequest;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ReservaRepository;
import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

@Service
@Transactional
public class ReservaService {


    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private ClienteService clienteService;

    // Crear reserva
    public Reserva crearReserva(Reserva reserva) {
        reserva.setFechaRegistro(new Timestamp(System.currentTimeMillis()));
        reserva.setEstadoReserva(EstadoReserva.PENDIENTE);
        reserva.setEstadoPago(EstadoPago.PENDIENTE);
        return reservaRepository.save(reserva);
    }

    // Cancelar reserva
    public boolean cancelarReserva(Integer idReserva) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setEstadoReserva(EstadoReserva.CANCELADA);
            reservaRepository.save(reserva);
            return true;
        }
        return false;
    }

    // Listar reservas del cliente
    public List<Reserva> listarReservasCliente(String dniCliente) {
        return reservaRepository.findByClienteDniOrderByFechaReservaDescHoraReservaDesc(dniCliente);
    }

    // Verificar estado de reserva
    public EstadoReserva verificarEstadoReserva(Integer idReserva) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        return reservaOpt.map(Reserva::getEstadoReserva).orElse(null);
    }

    // Asignar bicicleta a reserva
    public boolean asignarBicicletaAReserva(Integer idReserva, Bicicleta bicicleta) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            reserva.setBicicleta(bicicleta);
            reservaRepository.save(reserva);
            return true;
        }
        return false;
    }

    // Generar código de reserva (usando ID de reserva)
    public String generarCodigoReserva(Integer idReserva) {
        return "RES-" + String.format("%06d", idReserva);
    }

    // Buscar reservas por cliente
    public List<Reserva> buscarReservasPorCliente(String dniCliente) {
        return reservaRepository.findByClienteDniOrderByFechaReservaDescHoraReservaDesc(dniCliente);
    }

    // Buscar reservas por fecha
    public List<Reserva> buscarReservasPorRangoFecha(Date fechaInicio, Date fechaFin) {
        return reservaRepository.findReservasByFechaRange(fechaInicio, fechaFin);
    }

    // Buscar reservas por cliente y rango de fechas
    public List<Reserva> buscarReservasPorClienteYFecha(String dniCliente, Date fechaInicio, Date fechaFin) {
        return reservaRepository.findReservasByClienteAndFechaRange(dniCliente, fechaInicio, fechaFin);
    }

    // Actualizar estado de reserva
    public void actualizarEstadoReserva(Integer idReserva, EstadoReserva nuevoEstado) {
        reservaRepository.actualizarEstadoReserva(idReserva, nuevoEstado);
    }

    // Actualizar estado de pago
    public void actualizarEstadoPago(Integer idReserva, EstadoPago nuevoEstado) {
        reservaRepository.actualizarEstadoPago(idReserva, nuevoEstado);
    }

    // Historial de reservas
    public List<Reserva> obtenerHistorialReservas(String dniCliente) {
        return reservaRepository.findByClienteDniOrderByFechaReservaDescHoraReservaDesc(dniCliente);
    }

    // Filtrar reservas por estado
    public List<Reserva> filtrarReservasPorEstado(String dniCliente, EstadoReserva estado) {
        return reservaRepository.findByClienteDniAndEstadoReservaOrderByFechaReservaDesc(dniCliente, estado);
    }

    // Filtrar reservas por estado de pago
    public List<Reserva> filtrarReservasPorEstadoPago(String dniCliente, EstadoPago estadoPago) {
        return reservaRepository.findByClienteDniAndEstadoPagoOrderByFechaReservaDesc(dniCliente, estadoPago);
    }

    // Filtrar reservas por duración
    public List<Reserva> filtrarReservasPorDuracion(Integer duracionHoras) {
        return reservaRepository.findByDuracionHorasOrderByFechaRegistroDesc(duracionHoras);
    }

    // Eliminar reserva
    public void eliminarReserva(Integer idReserva) {
        reservaRepository.deleteById(idReserva);
    }

    // Calcular costo total de reserva
    public BigDecimal calcularCostoTotalReserva(BigDecimal precioPorHora, Integer duracionHoras) {
        return precioPorHora.multiply(new BigDecimal(duracionHoras));
    }

    // Calcular total pagado por cliente
    public BigDecimal calcularTotalPagadoPorCliente(String dniCliente) {
        return reservaRepository.calcularTotalPagadoByCliente(dniCliente);
    }

    // Funcionalidades adicionales

    // Verificar disponibilidad de bicicleta
    public boolean verificarDisponibilidadBicicleta(String codigoBicicleta, Date fecha,
            Time horaInicio, Integer duracionHoras) {
        return !reservaRepository.existeConflictoReserva(codigoBicicleta, fecha, horaInicio, duracionHoras);
    }

    // Buscar reservas activas de un cliente
    public List<Reserva> buscarReservasActivas(String dniCliente) {
        return reservaRepository.findReservasActivasByCliente(dniCliente);
    }

    // Buscar reservas pendientes de pago
    public List<Reserva> buscarReservasPendientesPago(String dniCliente) {
        return reservaRepository.findReservasPendientesPagoByCliente(dniCliente);
    }

    // Obtener última reserva del cliente
    public Optional<Reserva> obtenerUltimaReserva(String dniCliente) {
        return reservaRepository.findUltimaReservaByCliente(dniCliente);
    }

    // Obtener próxima reserva del cliente
    public Optional<Reserva> obtenerProximaReserva(String dniCliente) {
        return reservaRepository.findProximaReservaByCliente(dniCliente);
    }

    // Buscar por ID
    public Optional<Reserva> buscarPorId(Integer idReserva) {
        return reservaRepository.findById(idReserva);
    }

    // Listar todas las reservas
    public List<Reserva> listarTodasReservas() {
        return reservaRepository.findAll();
    }

    // Buscar reservas por estado general
    public List<Reserva> buscarPorEstadoReserva(EstadoReserva estado) {
        return reservaRepository.findByEstadoReservaOrderByFechaReservaDesc(estado);
    }

    // Buscar reservas por estado de pago general
    public List<Reserva> buscarPorEstadoPago(EstadoPago estadoPago) {
        return reservaRepository.findByEstadoPagoOrderByFechaRegistroDesc(estadoPago);
    }

    // Contar reservas por cliente
    public Long contarReservasPorCliente(String dniCliente) {
        return reservaRepository.countByClienteDni(dniCliente);
    }

    // Contar reservas por estado
    public Long contarReservasPorEstado(EstadoReserva estado) {
        return reservaRepository.countByEstadoReserva(estado);
    }

    // Verificar si la reserva pertenece al cliente
    public boolean reservaPerteneceACliente(Integer idReserva, String dniCliente) {
        return reservaRepository.existeReservaForCliente(idReserva, dniCliente);
    }

    // Buscar reservas listas para convertir a alquiler
    public List<Reserva> buscarReservasListasParaAlquiler() {
        return reservaRepository.findReservasListasParaAlquiler();
    }

    // Confirmar reserva
    public boolean confirmarReserva(Integer idReserva) {
        return cambiarEstadoReserva(idReserva, EstadoReserva.PENDIENTE);
    }

    // Completar reserva
    public boolean completarReserva(Integer idReserva) {
        return cambiarEstadoReserva(idReserva, EstadoReserva.COMPLETADA);
    }

    // Método auxiliar para cambiar estado

    // Validar datos de reserva
    public boolean validarDatosReserva(Reserva reserva) {
        if (reserva.getCliente() == null || reserva.getBicicleta() == null) {
            return false;
        }
        if (reserva.getFechaReserva() == null || reserva.getHoraReserva() == null) {
            return false;
        }
        if (reserva.getDuracionHoras() == null || reserva.getDuracionHoras() <= 0) {
            return false;
        }
        if (reserva.getPrecioTotal() == null || reserva.getPrecioTotal().compareTo(BigDecimal.ZERO) <= 0) {
            return false;
        }
        return true;
    }

    public boolean cambiarEstadoReserva(Integer idReserva, EstadoReserva nuevoEstado) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        if (reservaOpt.isPresent()) {
            Reserva reserva = reservaOpt.get();
            EstadoReserva estadoActual = reserva.getEstadoReserva();

            if (!estadoActual.puedeTransitarA(nuevoEstado)) {
                // Aquí podrías lanzar un error o manejar la transición incorrecta.
                return false;
            }
            reserva.setEstadoReserva(nuevoEstado);
            reservaRepository.save(reserva);
            return true;
        }
        return false;
    }

    // Crear reserva desde DTO
    public Reserva crearReservaFromDto(ReservaRequest request) {
        Reserva reserva = new Reserva();
        
        // Buscar cliente
        Cliente cliente = clienteService.buscarPorDni(request.getDniCliente())
            .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
        
        // Buscar bicicleta
        Bicicleta bicicleta = bicicletaService.buscarPorId(request.getCodigoBicicleta())
            .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));
        
        // Verificar disponibilidad
        if (!verificarDisponibilidadBicicleta(request.getCodigoBicicleta(), 
                                             request.getFechaReserva(), 
                                             request.getHoraReserva(), 
                                             request.getDuracionHoras())) {
            throw new RuntimeException("Bicicleta no disponible en la fecha y hora solicitada");
        }
        
        reserva.setCliente(cliente);
        reserva.setBicicleta(bicicleta);
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setHoraReserva(request.getHoraReserva());
        reserva.setDuracionHoras(request.getDuracionHoras());
        reserva.setPrecioTotal(request.getPrecioTotal());
        
        return crearReserva(reserva);
    }

    // Actualizar reserva desde DTO
    public Reserva actualizarReservaFromDto(Integer idReserva, ReservaRequest request) {
        Optional<Reserva> reservaOpt = reservaRepository.findById(idReserva);
        if (!reservaOpt.isPresent()) {
            return null;
        }
        
        Reserva reserva = reservaOpt.get();
        
        // Buscar cliente si cambió
        if (!reserva.getCliente().getDni().equals(request.getDniCliente())) {
            Cliente cliente = clienteService.buscarPorDni(request.getDniCliente())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
            reserva.setCliente(cliente);
        }
        
        // Buscar bicicleta si cambió
        if (!reserva.getBicicleta().getCodigoBicicleta().equals(request.getCodigoBicicleta())) {
            Bicicleta bicicleta = bicicletaService.buscarPorId(request.getCodigoBicicleta())
                .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));
            reserva.setBicicleta(bicicleta);
        }
        
        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setHoraReserva(request.getHoraReserva());
        reserva.setDuracionHoras(request.getDuracionHoras());
        reserva.setPrecioTotal(request.getPrecioTotal());
        reserva.setEstadoReserva(request.getEstadoReserva());
        reserva.setEstadoPago(request.getEstadoPago());
        
        return reservaRepository.save(reserva);
    }

}
