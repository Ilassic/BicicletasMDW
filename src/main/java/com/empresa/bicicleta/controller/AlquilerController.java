package com.empresa.bicicleta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Reserva;
import com.empresa.bicicleta.service.AlquilerService;
import com.empresa.bicicleta.service.ReservaService;

@Controller
public class AlquilerController {

    @Autowired
    private AlquilerService alquilerService;
    
    @Autowired
    private ReservaService reservaService;

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
            java.math.BigDecimal precioPorHora = alquiler.getReserva().getBicicleta().getPrecio();
            
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