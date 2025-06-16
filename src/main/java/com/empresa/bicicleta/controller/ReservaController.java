package com.empresa.bicicleta.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.service.BicicletaService;
import com.empresa.bicicleta.service.ClienteService;
import com.empresa.bicicleta.service.ReservaService;
import com.empresa.bicicleta.enums.EstadoPago;
import com.empresa.bicicleta.enums.EstadoReserva;

import jakarta.servlet.http.HttpSession;

@Controller
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private BicicletaService bicicletaService;

    @PostMapping("/reservar-bicicleta")
    public String procesarReserva(
            @RequestParam("clienteNombre") String nombre,
            @RequestParam("clienteApellidos") String apellidos,
            @RequestParam("clienteDni") String dni,
            @RequestParam("clienteTelefono") String telefono,
            @RequestParam("clienteEmail") String email,
            @RequestParam("bicicletaId") String bicicletaId,
            @RequestParam("fechaReserva") String fechaReserva,
            @RequestParam("horaReserva") String horaReserva,
            @RequestParam("duracionHoras") Integer duracionHoras,
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam(value = "comprobante", required = false) MultipartFile comprobante,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {
        
        try {
            // 1. Verificar o crear cliente
            Cliente cliente = clienteService.buscarPorDni(dni).orElse(null);
            if (cliente == null) {
                // Crear nuevo cliente si no existe
                cliente = new Cliente();
                cliente.setDni(dni);
                cliente.setNombre(nombre);
                cliente.setApellidos(apellidos);
                cliente.setTelefono(telefono);
                cliente.setCorreoElectronico(email);
                
                // Establecer contraseña temporal si es necesario
                cliente.setContrasena("temporal123"); // Se debería cambiar
                cliente = clienteService.crearCliente(cliente);
            }

            // 2. Verificar disponibilidad de la bicicleta
            Bicicleta bicicleta = bicicletaService.buscarPorCodigo(bicicletaId).orElse(null);
            if (bicicleta == null) {
                redirectAttributes.addFlashAttribute("error", "La bicicleta seleccionada no existe");
                return "redirect:/reservas-bicicletas";
            }

            if (!bicicletaService.validarDisponibilidad(bicicletaId)) {
                redirectAttributes.addFlashAttribute("error", "La bicicleta no está disponible");
                return "redirect:/reservas-bicicletas";
            }

            // 3. Verificar conflictos de horario
            Date fecha = Date.valueOf(fechaReserva);
            Time hora = Time.valueOf(horaReserva + ":00");
            
            if (!reservaService.verificarDisponibilidadBicicleta(bicicletaId, fecha, hora, duracionHoras)) {
                redirectAttributes.addFlashAttribute("error", "La bicicleta ya está reservada en ese horario");
                return "redirect:/reservas-bicicletas";
            }

            // 4. Calcular precio total
            BigDecimal precioTotal = bicicleta.getPrecio().multiply(new BigDecimal(duracionHoras));

            // 5. Crear la reserva
            Reserva reserva = new Reserva();
            reserva.setCliente(cliente);
            reserva.setBicicleta(bicicleta);
            reserva.setFechaReserva(fecha);
            reserva.setHoraReserva(hora);
            reserva.setDuracionHoras(duracionHoras);
            reserva.setPrecioTotal(precioTotal);
            
            // 6. Establecer estado según método de pago
            if ("efectivo".equals(metodoPago)) {
                reserva.setEstadoPago(EstadoPago.PENDIENTE);
                reserva.setEstadoReserva(EstadoReserva.PENDIENTE);
            } else if ("transferencia".equals(metodoPago)) {
                // Si tiene comprobante, marcar como pagado (se debería validar)
                if (comprobante != null && !comprobante.isEmpty()) {
                    reserva.setEstadoPago(EstadoPago.PAGADO);
                    reserva.setEstadoReserva(EstadoReserva.ENTREGADA);
                    // Aquí podrías guardar el comprobante
                } else {
                    reserva.setEstadoPago(EstadoPago.PENDIENTE);
                    reserva.setEstadoReserva(EstadoReserva.PENDIENTE);
                }
            }

            // 7. Guardar la reserva
            Reserva reservaCreada = reservaService.crearReserva(reserva);

            // 8. Marcar bicicleta como reservada (opcional, dependiendo de tu lógica)
            // bicicletaService.marcarComoReservada(bicicletaId);

            redirectAttributes.addFlashAttribute("success", "Reserva creada exitosamente");
            redirectAttributes.addFlashAttribute("reservaId", reservaCreada.getId());
            redirectAttributes.addFlashAttribute("codigoReserva", reservaService.generarCodigoReserva(reservaCreada.getId()));
            
            return "redirect:/mi-historial";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al procesar la reserva: " + e.getMessage());
            return "redirect:/reservas-bicicletas";
        }
    }
}