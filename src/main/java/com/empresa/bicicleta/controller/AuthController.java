package com.empresa.bicicleta.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.service.ClienteService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class AuthController {

    @Autowired
    ClienteService clienteService;

    @GetMapping("/login")
    public String getMethodName() {
        return "Login";
    }

    @PostMapping("/loguear")
    public String postMethodName(@Valid @ModelAttribute("cliente") Cliente cliente) {

        if (clienteService.login(cliente.getCorreoElectronico(), cliente.getContrasena())) {
            return "redirect:/";
        }
        return "Fallo";
    }
    
    
}
