package com.empresa.bicicleta.controller;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.service.AlquilerService;
import com.empresa.bicicleta.service.BicicletaService;
import com.empresa.bicicleta.service.ReservaService;
import com.empresa.bicicleta.dto.ReservaRequest;

@Controller
public class AlquilerController {

    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private ReservaService reservaService;

    @PostMapping("/reservar-bicicleta")
    public String reservarBicicleta(
            @RequestParam("clienteNombre") String clienteNombre,
            @RequestParam("clienteApellidos") String clienteApellidos,
            @RequestParam("clienteDni") String clienteDni,
            @RequestParam("clienteTelefono") String clienteTelefono,
            @RequestParam("clienteEmail") String clienteEmail,
            @RequestParam("bicicletaId") String bicicletaId,
            @RequestParam("fechaReserva") String fechaReserva,
            @RequestParam("horaReserva") String horaReserva,
            @RequestParam("duracionHoras") Integer duracionHoras,
            @RequestParam("metodoPago") String metodoPago,
            @RequestParam(value = "comprobante", required = false) MultipartFile comprobante,
            RedirectAttributes redirectAttributes) {

        try {
            // Validaciones básicas
            if (clienteDni == null || clienteDni.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El DNI es obligatorio");
                return "redirect:/alquiler";
            }

            if (clienteNombre == null || clienteNombre.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "El nombre es obligatorio");
                return "redirect:/alquiler";
            }

            if (bicicletaId == null || bicicletaId.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Debe seleccionar una bicicleta válida");
                return "redirect:/alquiler";
            }

            if (duracionHoras == null || duracionHoras <= 0) {
                redirectAttributes.addFlashAttribute("error", "La duración debe ser mayor a 0 horas");
                return "redirect:/alquiler";
            }

            // Crear el DTO de reserva
            ReservaRequest request = new ReservaRequest();
            request.setDniCliente(clienteDni.trim());
            request.setNombreCliente(clienteNombre.trim());
            request.setApellidosCliente(clienteApellidos != null ? clienteApellidos.trim() : "");
            request.setTelefonoCliente(clienteTelefono != null ? clienteTelefono.trim() : "");
            request.setEmailCliente(clienteEmail != null ? clienteEmail.trim() : "");
            String codigoBicicleta = bicicletaService.buscarPorId(bicicletaId)
                    .map(b -> b.getCodigoBicicleta())
                    .orElseThrow(() -> new RuntimeException("Bicicleta no encontrada"));

            BigDecimal precioPorHora = bicicletaService.buscarPorId(bicicletaId)
                    .map(b -> b.getPrecio())
                    .orElse(BigDecimal.ZERO);
            BigDecimal precioTotal = precioPorHora.multiply(new BigDecimal(duracionHoras));
            request.setPrecioTotal(precioTotal);

            // Manejo seguro de fechas
            try {
                request.setFechaReserva(java.sql.Date.valueOf(fechaReserva));
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", "Formato de fecha inválido");
                return "redirect:/alquiler";
            }

            // Manejo seguro de horas
            try {
                String horaCompleta = horaReserva.contains(":") ? horaReserva : horaReserva + ":00";
                if (horaCompleta.split(":").length == 2) {
                    horaCompleta += ":00";
                }
                request.setHoraReserva(java.sql.Time.valueOf(horaCompleta));
            } catch (IllegalArgumentException e) {
                redirectAttributes.addFlashAttribute("error", "Formato de hora inválido");
                return "redirect:/alquiler";
            }

            request.setDuracionHoras(duracionHoras);
            request.setMetodoPago(metodoPago != null ? metodoPago : "efectivo");

            // Procesar el comprobante si existe
            if (comprobante != null && !comprobante.isEmpty()) {
                // Validar tipo de archivo
                String contentType = comprobante.getContentType();
                if (contentType != null
                        && (contentType.startsWith("image/") || contentType.equals("application/pdf"))) {
                    System.out.println("Comprobante recibido: " + comprobante.getOriginalFilename());
                    System.out.println("Tamaño: " + comprobante.getSize() + " bytes");
                } else {
                    redirectAttributes.addFlashAttribute("error", "El comprobante debe ser una imagen o PDF");
                    return "redirect:/alquiler";
                }
            }

            // Crear la reserva usando el service
            Reserva reservaCreada = reservaService.crearReservaFromDto(request);

            if (reservaCreada != null) {
                redirectAttributes.addFlashAttribute("success", "Reserva creada exitosamente");
                return "redirect:/mi-historial";
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "No se pudo crear la reserva. Verifique la disponibilidad de la bicicleta.");
                return "redirect:/alquiler";
            }

        } catch (Exception e) {
            System.err.println("Error al crear reserva: " + e.getMessage());
            e.printStackTrace();

            String errorMsg = e.getMessage() != null ? e.getMessage() : "Error interno del sistema";
            redirectAttributes.addFlashAttribute("error", "Error al procesar la reserva: " + errorMsg);
            return "redirect:/alquiler";
        }
    }

    @PostMapping("/entregar-bicicleta")
    public String entregarBicicleta(@RequestParam("reservaId") Integer reservaId,
            RedirectAttributes redirectAttributes) {
        try {
            Reserva reserva = reservaService.buscarPorId(reservaId).orElse(null);
            if (reserva == null) {
                redirectAttributes.addFlashAttribute("error", "Reserva no encontrada");
                return "redirect:/mi-historial";
            }

            // Crear el alquiler cuando se entrega la bicicleta
            Alquiler alquiler = new Alquiler();
            alquiler.setReserva(reserva);
            // Los demás campos se llenan en el servicio

            if (alquilerService.procesarEntregaBicicleta(alquiler.getId())) {
                redirectAttributes.addFlashAttribute("success", "Bicicleta entregada correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo entregar la bicicleta");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/mi-historial";
    }

    @PostMapping("/devolver-bicicleta")
    public String devolverBicicleta(@RequestParam("alquilerId") Integer alquilerId,
            RedirectAttributes redirectAttributes) {
        try {
            Alquiler alquiler = alquilerService.obtenerAlquilerPorId(alquilerId).orElse(null);
            if (alquiler == null) {
                redirectAttributes.addFlashAttribute("error", "Alquiler no encontrado");
                return "redirect:/mi-historial";
            }

            // Obtener precio por hora de la bicicleta
            BigDecimal precioPorHora = alquiler.getReserva().getBicicleta().getPrecio();

            if (alquilerService.procesarDevolucionBicicleta(alquilerId, precioPorHora)) {
                redirectAttributes.addFlashAttribute("success", "Bicicleta devuelta correctamente");
            } else {
                redirectAttributes.addFlashAttribute("error", "No se pudo procesar la devolución");
            }

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error: " + e.getMessage());
        }

        return "redirect:/mi-historial";
    }
}