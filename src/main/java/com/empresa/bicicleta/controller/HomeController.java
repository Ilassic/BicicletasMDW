package com.empresa.bicicleta.controller;

import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String getMethodName() {
        return "Principal";
    }
    
    @GetMapping("/modelos")
    public String getModelos() {
        return "ModeBici";
    }
    @GetMapping("/reservas-bicicletas")
    public String getReservas() {
        return "Reser-Alqui";
    }
    @GetMapping("/mi-historial")
    public String getHistorial() {
        return "Historial-Alq";
    }

}
