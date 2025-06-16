package com.empresa.bicicleta.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.empresa.bicicleta.dto.LoginRequest;
import com.empresa.bicicleta.dto.RegisterRequest;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.service.AuthService;

import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar campos requeridos
            if (loginRequest.getEmail() == null || loginRequest.getEmail().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "El correo electrónico es requerido");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
                response.put("success", false);
                response.put("message", "La contraseña es requerida");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Intentar autenticar
            Cliente cliente = authService.autenticar(loginRequest.getEmail().trim(), loginRequest.getPassword());
            
            if (cliente != null) {
                // Crear sesión del usuario
                session.setAttribute("usuario", cliente);
                session.setAttribute("dni", cliente.getDni());
                session.setAttribute("nombre", cliente.getNombre());
                session.setAttribute("apellidos", cliente.getApellidos());
                session.setAttribute("email", cliente.getCorreoElectronico());
                session.setAttribute("telefono", cliente.getTelefono());
                session.setAttribute("fechaLogin", LocalDateTime.now());
                session.setAttribute("loggedIn", true);
                
                // Establecer tiempo de vida de la sesión (30 minutos)
                session.setMaxInactiveInterval(30 * 60);
                
                response.put("success", true);
                response.put("message", "Inicio de sesión exitoso");
                response.put("redirectUrl", "/reservas-bicicletas");
                
                // Datos del usuario para el frontend
                Map<String, Object> userData = new HashMap<>();
                userData.put("dni", cliente.getDni());
                userData.put("nombre", cliente.getNombre());
                userData.put("apellidos", cliente.getApellidos());
                userData.put("email", cliente.getCorreoElectronico());
                userData.put("telefono", cliente.getTelefono());
                userData.put("fechaRegistro", cliente.getFechaRegistro().toString());
                userData.put("sessionId", session.getId());
                
                response.put("usuario", userData);
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Correo electrónico o contraseña incorrectos");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest registerRequest, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Validar campos requeridos
            String errorValidacion = validarDatosRegistro(registerRequest);
            if (errorValidacion != null) {
                response.put("success", false);
                response.put("mensaje", errorValidacion);
                return ResponseEntity.badRequest().body(response);
            }
            
            // Verificar si ya existe el usuario
            if (authService.existeClientePorCorreo(registerRequest.getEmail().trim())) {
                response.put("success", false);
                response.put("mensaje", "Este correo electrónico ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (authService.existeClientePorDni(registerRequest.getDni().trim())) {
                response.put("success", false);
                response.put("mensaje", "Este DNI ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            if (authService.existeClientePorTelefono(registerRequest.getTelefono().replaceAll("\\s+", ""))) {
                response.put("success", false);
                response.put("mensaje", "Este teléfono ya está registrado");
                return ResponseEntity.badRequest().body(response);
            }
            
            // Crear nuevo cliente
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setDni(registerRequest.getDni().trim());
            nuevoCliente.setNombre(registerRequest.getNombre().trim());
            nuevoCliente.setApellidos(registerRequest.getApellidos().trim());
            nuevoCliente.setTelefono(registerRequest.getTelefono().replaceAll("\\s+", ""));
            nuevoCliente.setCorreoElectronico(registerRequest.getEmail().trim().toLowerCase());
            nuevoCliente.setContrasena(registerRequest.getPassword());
            nuevoCliente.setFechaRegistro(LocalDateTime.now());
            
            // Registrar cliente
            Cliente clienteRegistrado = authService.registrarCliente(nuevoCliente);
            
            if (clienteRegistrado != null) {
                response.put("success", true);
                response.put("mensaje", "Registro exitoso");
                
                // Datos del usuario registrado
                Map<String, Object> userData = new HashMap<>();
                userData.put("dni", clienteRegistrado.getDni());
                userData.put("nombre", clienteRegistrado.getNombre());
                userData.put("apellidos", clienteRegistrado.getApellidos());
                userData.put("email", clienteRegistrado.getCorreoElectronico());
                userData.put("telefono", clienteRegistrado.getTelefono());
                userData.put("fechaRegistro", clienteRegistrado.getFechaRegistro().toString());
                
                response.put("usuario", userData);
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            } else {
                response.put("success", false);
                response.put("mensaje", "Error al registrar el usuario");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("mensaje", "Error interno del servidor: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Invalidar la sesión
            session.invalidate();
            
            response.put("success", true);
            response.put("message", "Sesión cerrada exitosamente");
            response.put("redirectUrl", "/logeo");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al cerrar sesión");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    private String validarDatosRegistro(RegisterRequest request) {
        // Validar campos no nulos
        if (request.getNombre() == null || request.getNombre().trim().isEmpty()) {
            return "El nombre es requerido";
        }
        if (request.getApellidos() == null || request.getApellidos().trim().isEmpty()) {
            return "Los apellidos son requeridos";
        }
        if (request.getDni() == null || request.getDni().trim().isEmpty()) {
            return "El DNI es requerido";
        }
        if (request.getTelefono() == null || request.getTelefono().trim().isEmpty()) {
            return "El teléfono es requerido";
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            return "El correo electrónico es requerido";
        }
        if (request.getPassword() == null || request.getPassword().isEmpty()) {
            return "La contraseña es requerida";
        }
        
        // Validar formato nombre (solo letras y espacios, máximo 10 letras sin contar espacios)
        String nombre = request.getNombre().trim();
        if (!nombre.matches("^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$")) {
            return "El nombre solo puede contener letras y espacios";
        }
        if (nombre.replaceAll("\\s+", "").length() > 10) {
            return "El nombre no puede exceder 10 letras (sin contar espacios)";
        }
        
        // Validar formato apellidos
        String apellidos = request.getApellidos().trim();
        if (!apellidos.matches("^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$")) {
            return "Los apellidos solo pueden contener letras y espacios";
        }
        if (apellidos.replaceAll("\\s+", "").length() > 10) {
            return "Los apellidos no pueden exceder 10 letras (sin contar espacios)";
        }
        
        // Validar DNI (8 dígitos, debe empezar con 0, 1, 4, 6 o 7)
        String dni = request.getDni().trim();
        if (!dni.matches("^[0-9]{8}$")) {
            return "El DNI debe contener exactamente 8 números";
        }
        char primerDigito = dni.charAt(0);
        if (primerDigito != '0' && primerDigito != '1' && primerDigito != '4' && 
            primerDigito != '6' && primerDigito != '7') {
            return "El DNI debe empezar con 0, 1, 4, 6 o 7";
        }
        
        // Validar teléfono (9 dígitos, debe empezar con 9)
        String telefono = request.getTelefono().replaceAll("\\s+", "");
        if (!telefono.matches("^9[0-9]{8}$")) {
            return "El teléfono debe ser un celular de 9 dígitos que empiece con 9";
        }
        
        // Validar email
        String email = request.getEmail().trim();
        if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            return "El formato del correo electrónico no es válido";
        }
        
        // Validar contraseña (exactamente 12 caracteres)
        if (request.getPassword().length() != 12) {
            return "La contraseña debe tener exactamente 12 caracteres";
        }
        
        return null; // Todo válido
    }
}