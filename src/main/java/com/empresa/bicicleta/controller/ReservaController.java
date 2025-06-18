package com.empresa.bicicleta.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.empresa.bicicleta.dto.ReservaRequest;
import com.empresa.bicicleta.dto.ReservaDto;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.service.ReservaService;
import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "*")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;

    // Crear reserva
    @PostMapping
    public ResponseEntity<ReservaDto> crearReserva(@RequestBody ReservaRequest request) {
        try {
            Reserva reserva = reservaService.crearReservaFromDto(request);
            ReservaDto response = convertirADto(reserva);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Obtener reserva por ID
    @GetMapping("/{id}")
    public ResponseEntity<ReservaDto> obtenerReservaPorId(@PathVariable Integer id) {
        Optional<Reserva> reserva = reservaService.buscarPorId(id);
        if (reserva.isPresent()) {
            return ResponseEntity.ok(convertirADto(reserva.get()));
        }
        return ResponseEntity.notFound().build();
    }

    // Obtener todas las reservas
    @GetMapping
    public ResponseEntity<List<ReservaDto>> obtenerTodasReservas() {
        List<Reserva> reservas = reservaService.listarTodasReservas();
        List<ReservaDto> response = reservas.stream().map(this::convertirADto).toList();
        return ResponseEntity.ok(response);
    }

    // Actualizar reserva
    @PutMapping("/{id}")
    public ResponseEntity<ReservaDto> actualizarReserva(@PathVariable Integer id, @RequestBody ReservaRequest request) {
        try {
            Reserva reservaActualizada = reservaService.actualizarReservaFromDto(id, request);
            if (reservaActualizada != null) {
                return ResponseEntity.ok(convertirADto(reservaActualizada));
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Eliminar reserva
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarReserva(@PathVariable Integer id) {
        try {
            reservaService.eliminarReserva(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Obtener reservas por cliente
    @GetMapping("/cliente/{dni}")
    public ResponseEntity<List<ReservaDto>> obtenerReservasPorCliente(@PathVariable String dni) {
        List<Reserva> reservas = reservaService.buscarReservasPorCliente(dni);
        List<ReservaDto> response = reservas.stream().map(this::convertirADto).toList();
        return ResponseEntity.ok(response);
    }

    // Obtener reservas por estado
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReservaDto>> obtenerReservasPorEstado(@PathVariable EstadoReserva estado) {
        List<Reserva> reservas = reservaService.buscarPorEstadoReserva(estado);
        List<ReservaDto> response = reservas.stream().map(this::convertirADto).toList();
        return ResponseEntity.ok(response);
    }

    // Obtener reservas por rango de fechas
    @GetMapping("/fechas")
    public ResponseEntity<List<ReservaDto>> obtenerReservasPorFecha(
            @RequestParam Date fechaInicio,
            @RequestParam Date fechaFin) {
        List<Reserva> reservas = reservaService.buscarReservasPorRangoFecha(fechaInicio, fechaFin);
        List<ReservaDto> response = reservas.stream().map(this::convertirADto).toList();
        return ResponseEntity.ok(response);
    }

    // Actualizar estado de reserva
    @PatchMapping("/{id}/estado-reserva")
    public ResponseEntity<Void> actualizarEstadoReserva(@PathVariable Integer id, @RequestParam EstadoReserva estado) {
        try {
            reservaService.actualizarEstadoReserva(id, estado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Actualizar estado de pago
    @PatchMapping("/{id}/estado-pago")
    public ResponseEntity<Void> actualizarEstadoPago(@PathVariable Integer id, @RequestParam EstadoPago estado) {
        try {
            reservaService.actualizarEstadoPago(id, estado);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // Contar reservas por cliente
    @GetMapping("/cliente/{dni}/count")
    public ResponseEntity<Long> contarReservasCliente(@PathVariable String dni) {
        Long count = reservaService.contarReservasPorCliente(dni);
        return ResponseEntity.ok(count);
    }

    // Calcular total pagado por cliente
    @GetMapping("/cliente/{dni}/total-pagado")
    public ResponseEntity<BigDecimal> calcularTotalPagadoCliente(@PathVariable String dni) {
        BigDecimal total = reservaService.calcularTotalPagadoPorCliente(dni);
        return ResponseEntity.ok(total);
    }

    // Cancelar reserva
    @PatchMapping("/{id}/cancelar")
    public ResponseEntity<Void> cancelarReserva(@PathVariable Integer id) {
        boolean cancelado = reservaService.cancelarReserva(id);
        if (cancelado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Confirmar reserva
    @PatchMapping("/{id}/confirmar")
    public ResponseEntity<Void> confirmarReserva(@PathVariable Integer id) {
        boolean confirmado = reservaService.confirmarReserva(id);
        if (confirmado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Completar reserva
    @PatchMapping("/{id}/completar")
    public ResponseEntity<Void> completarReserva(@PathVariable Integer id) {
        boolean completado = reservaService.completarReserva(id);
        if (completado) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Método auxiliar para convertir a DTO
    private ReservaDto convertirADto(Reserva reserva) {
        ReservaDto dto = new ReservaDto();
        dto.setId(reserva.getId());
        dto.setDniCliente(reserva.getCliente().getDni());
        dto.setNombreCliente(reserva.getCliente().getNombre() + " " + reserva.getCliente().getApellidos());
        dto.setCodigoBicicleta(reserva.getBicicleta().getCodigoBicicleta());
        dto.setNombreModeloBicicleta(reserva.getBicicleta().getNombreModelo());
        dto.setFechaReserva(reserva.getFechaReserva());
        dto.setHoraReserva(reserva.getHoraReserva());
        dto.setDuracionHoras(reserva.getDuracionHoras());
        dto.setPrecioTotal(reserva.getPrecioTotal());
        dto.setEstadoReserva(reserva.getEstadoReserva());
        dto.setEstadoPago(reserva.getEstadoPago());
        dto.setFechaRegistro(reserva.getFechaRegistro());
        return dto;
    }
}