package com.empresa.bicicleta.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.empresa.bicicleta.model.Alquiler;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.service.AlquilerService;
import com.empresa.bicicleta.service.ClienteService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/historial")
public class HistorialController {

    @Autowired
    private AlquilerService alquilerService;

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/obtener")
    @ResponseBody
    public ResponseEntity<?> obtenerHistorialUsuario(HttpSession session) {
        // Verificar autenticación
        if (session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        String dniCliente = (String) session.getAttribute("dni");
        
        try {
            // Obtener historial de alquileres del cliente
            List<Alquiler> historial = alquilerService.obtenerHistorialPorCliente(dniCliente);
            
            // Obtener estadísticas del cliente
            AlquilerService.AlquilerEstadisticas estadisticas = alquilerService.obtenerEstadisticasCliente(dniCliente);
            
            // Obtener información del cliente
            Optional<Cliente> clienteOpt = clienteService.recuperarInformacionCliente(dniCliente);
            Cliente cliente = clienteOpt.orElse(null);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("historial", historial);
            respuesta.put("estadisticas", estadisticas);
            respuesta.put("cliente", cliente);
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al obtener el historial: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    @ResponseBody
    public ResponseEntity<?> buscarHistorialPorDni(@RequestParam String dni, HttpSession session) {
        // Verificar autenticación
        if (session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            return ResponseEntity.status(401).body("Usuario no autenticado");
        }

        try {
            // Verificar que el cliente existe
            Optional<Cliente> clienteOpt = clienteService.recuperarInformacionCliente(dni);
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.status(404).body("Cliente no encontrado");
            }
            
            // Obtener historial de alquileres
            List<Alquiler> historial = alquilerService.obtenerHistorialPorCliente(dni);
            
            // Obtener estadísticas
            AlquilerService.AlquilerEstadisticas estadisticas = alquilerService.obtenerEstadisticasCliente(dni);
            
            Map<String, Object> respuesta = new HashMap<>();
            respuesta.put("historial", historial);
            respuesta.put("estadisticas", estadisticas);
            respuesta.put("cliente", clienteOpt.get());
            
            return ResponseEntity.ok(respuesta);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error al buscar el historial: " + e.getMessage());
        }
    }
}