package com.empresa.bicicleta.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.empresa.bicicleta.model.Bicicleta;
import com.empresa.bicicleta.service.BicicletaService;
import com.empresa.bicicleta.service.AlquilerService;

import jakarta.servlet.http.HttpSession;

@Controller
public class BicicletaController {

    @Autowired
    private BicicletaService bicicletaService;

    @Autowired
    private AlquilerService alquilerService;

    @GetMapping("/modelos")
    public String mostrarModelos(HttpSession session, Model model) {
        // Obtener todas las bicicletas disponibles
        List<Bicicleta> todasLasBicicletas = bicicletaService.listarTodasLasBicicletas();
        
        // Separar por categorías
        List<Bicicleta> bicicletasMontana = bicicletaService.buscarPorCategoria("Montaña");
        List<Bicicleta> bicicletasRuta = bicicletaService.buscarPorCategoria("Ruta");
        List<Bicicleta> bicicletasUrbanas = bicicletaService.buscarPorCategoria("Urbana");
        List<Bicicleta> bicicletasInfantiles = bicicletaService.buscarPorCategoria("Infantil");
        List<Bicicleta> bicicletasBMX = bicicletaService.buscarPorCategoria("BMX");

        if (session.getAttribute("loggedIn") != null && (Boolean) session.getAttribute("loggedIn")) {
            model.addAttribute("nombre", session.getAttribute("nombre"));
            model.addAttribute("apellidos", session.getAttribute("apellidos"));
            model.addAttribute("loggedIn", true);
        } else {
            model.addAttribute("loggedIn", false);
        }
        // Agregar al modelo
        model.addAttribute("bicicletasMontana", bicicletasMontana);
        model.addAttribute("bicicletasRuta", bicicletasRuta);
        model.addAttribute("bicicletasUrbanas", bicicletasUrbanas);
        model.addAttribute("bicicletasInfantiles", bicicletasInfantiles);
        model.addAttribute("bicicletasBMX", bicicletasBMX);

        return "ModeBici";
    }
}