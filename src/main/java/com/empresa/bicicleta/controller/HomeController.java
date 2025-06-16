package com.empresa.bicicleta.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;

import jakarta.servlet.http.HttpSession;

import com.empresa.bicicleta.service.BicicletaService;
import com.empresa.bicicleta.model.Bicicleta;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private BicicletaService bicicletaService;
    
    @GetMapping("/")
    public String getMethodName(HttpSession session, Model model) {
        // Agregar información de sesión al modelo si existe
        if (session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn")) {
            model.addAttribute("usuario", session.getAttribute("nombre"));
            model.addAttribute("nombre", session.getAttribute("nombre"));
            model.addAttribute("apellidos", session.getAttribute("apellidos"));
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        return "Principal";
    }

    @GetMapping("/reservas-bicicletas")
    public String getReservas(HttpSession session, Model model) {
        // Verificar si el usuario está autenticado
        if (session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            return "redirect:/logeo";
        }
        
        // Pasar todos los datos del usuario autenticado al modelo
        model.addAttribute("nombre", session.getAttribute("nombre"));
        model.addAttribute("apellidos", session.getAttribute("apellidos"));
        model.addAttribute("dni", session.getAttribute("dni"));
        model.addAttribute("email", session.getAttribute("email"));
        model.addAttribute("telefono", session.getAttribute("telefono"));
        model.addAttribute("loggedIn", true);
        
        // Cargar lista de bicicletas disponibles
        List<Bicicleta> bicicletasDisponibles = bicicletaService.listarBicicletasDisponibles();
        model.addAttribute("bicicletas", bicicletasDisponibles);
        
        // Obtener categorías únicas para agrupar en el select
        Set<String> categorias = bicicletasDisponibles.stream()
            .map(Bicicleta::getCategoria)
            .collect(Collectors.toSet());
        model.addAttribute("categorias", categorias);
        
        return "Reser-Alqui";
    }
    
    @GetMapping("/logeo")
    public String getLogin(HttpSession session) {
        // Si ya está logueado, redirigir a reservas
        if (session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn")) {
            return "redirect:/reservas-bicicletas";
        }
        return "Login";
    }

    @GetMapping("/mi-historial")
    public String getHistorial(HttpSession session, Model model) {
        // Verificar si el usuario está autenticado
        if (session.getAttribute("loggedIn") == null || !(Boolean) session.getAttribute("loggedIn")) {
            return "redirect:/logeo";
        }
        
        model.addAttribute("usuario", session.getAttribute("nombre"));
        model.addAttribute("nombre", session.getAttribute("nombre"));
        model.addAttribute("apellidos", session.getAttribute("apellidos"));
        model.addAttribute("dni", session.getAttribute("dni"));
        model.addAttribute("email", session.getAttribute("email"));
        model.addAttribute("telefono", session.getAttribute("telefono"));
        model.addAttribute("loggedIn", true);
        
        return "Historial-Alq";
    }
}