package com.empresa.bicicleta.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ClienteRepository;

@Service
public class Authservice {

    @Autowired 
    private ClienteRepository usuarios;
    
    public boolean inicioSesion(String correo, String contraseña){
        try {
           Cliente cli = usuarios.findByCorreoElectronico(correo).orElseThrow(()->new IllegalArgumentException("Usuario no encontrado"));
           if (cli.getCorreoElectronico().equals(correo)) {
            if (cli.getContrasena().equals(contraseña)) {
             return true;   
            }
           }
           return false;
        } catch (Exception e) {
            return false;
        }
    }
}
