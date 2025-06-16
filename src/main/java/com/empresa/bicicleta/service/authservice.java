package com.empresa.bicicleta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.dto.LoginResult;
import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ClienteRepository;
import com.empresa.bicicleta.repository.AuthRepository;;

@Service
@Transactional
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AuthRepository authRepository;
    
    /**
     * @param correo 
     * @param contrasena 
     * @return 
     */
    public Cliente autenticar(String correo, String contrasena) {
        try {
            if (correo == null || correo.trim().isEmpty() || 
                contrasena == null || contrasena.trim().isEmpty()) {
                return null;
            }
            
            // Usar el procedimiento almacenado para validar credenciales
            LoginResult resultado = authRepository.ejecutarLoginProcedimiento(
                correo.trim().toLowerCase(), 
                contrasena
            );
            
            if (resultado != null && resultado.isExitoso()) {
                Optional<Cliente> clienteOpt = clienteRepository.findByCorreoElectronico(correo.trim().toLowerCase());
                
                if (clienteOpt.isPresent()) {
                    return clienteOpt.get();
                } else {
                    System.err.println("Error: procedimiento dice SUCCESS pero no se encontró el usuario en BD");
                    return null;
                }
            } else if (resultado != null) {
                // Log detallado del tipo de error para debugging
                System.out.println("Error de autenticación: " + resultado.getStatus());
                if ("USER_NOT_EXISTS".equals(resultado.getStatus())) {
                    System.out.println("Usuario no existe: " + correo);
                } else if ("INCORRECT_PASSWORD".equals(resultado.getStatus())) {
                    System.out.println("Contraseña incorrecta para usuario: " + correo);
                }
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error en autenticación con procedimiento: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Método alternativo que mantiene la lógica original para casos de emergencia
     * @param correo 
     * @param contrasena 
     * @return 
     */
    public Cliente autenticarConJPA(String correo, String contrasena) {
        try {
            if (correo == null || correo.trim().isEmpty() || 
                contrasena == null || contrasena.trim().isEmpty()) {
                return null;
            }
            
            Optional<Cliente> clienteOpt = clienteRepository.findByCorreoElectronico(correo.trim().toLowerCase());
            
            if (clienteOpt.isPresent()) {
                Cliente cliente = clienteOpt.get();
                // Comparar contraseñas
                if (cliente.getContrasena() != null && cliente.getContrasena().equals(contrasena)) {
                    return cliente;
                }
            }
            
            return null;
            
        } catch (Exception e) {
            System.err.println("Error en autenticación JPA: " + e.getMessage());
            return null;
        }
    }
    /**
     * Registra un nuevo cliente
     * @param cliente 
     * @return 
     */
    public Cliente registrarCliente(Cliente cliente) {
        try {
            if (cliente == null) {
                return null;
            }
            
            // Normalizar datos antes de guardar
            if (cliente.getCorreoElectronico() != null) {
                cliente.setCorreoElectronico(cliente.getCorreoElectronico().toLowerCase());
            }
            
            // Guardar en base de datos
            return clienteRepository.save(cliente);
            
        } catch (Exception e) {
            System.err.println("Error al registrar cliente: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica si existe un cliente por correo electrónico
     * @param correo 
     * @return 
     */
    public boolean existeClientePorCorreo(String correo) {
        try {
            return correo != null && clienteRepository.existsByCorreoElectronico(correo.toLowerCase());
        } catch (Exception e) {
            System.err.println("Error al verificar correo: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si existe un cliente por DNI
     * @param dni 
     * @return 
     */
    public boolean existeClientePorDni(String dni) {
        try {
            return dni != null && clienteRepository.existsByDni(dni);
        } catch (Exception e) {
            System.err.println("Error al verificar DNI: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Verifica si existe un cliente por teléfono
     * @param telefono 
     * @return 
     */
    public boolean existeClientePorTelefono(String telefono) {
        try {
            return telefono != null && clienteRepository.existsByTelefono(telefono);
        } catch (Exception e) {
            System.err.println("Error al verificar teléfono: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Busca un cliente por correo electrónico
     * @param correo 
     * @return 
     */
    public Cliente buscarClientePorCorreo(String correo) {
        try {
            if (correo == null || correo.trim().isEmpty()) {
                return null;
            }
            
            Optional<Cliente> clienteOpt = clienteRepository.findByCorreoElectronico(correo.toLowerCase());
            return clienteOpt.orElse(null);
            
        } catch (Exception e) {
            System.err.println("Error al buscar cliente por correo: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Busca un cliente por DNI
     * @param dni
     * @return 
     */
    public Cliente buscarClientePorDni(String dni) {
        try {
            if (dni == null || dni.trim().isEmpty()) {
                return null;
            }
            
            Optional<Cliente> clienteOpt = clienteRepository.findByDni(dni);
            return clienteOpt.orElse(null);
            
        } catch (Exception e) {
            System.err.println("Error al buscar cliente por DNI: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Actualiza la información de un cliente
     * @param cliente 
     * @return 
     */
    public Cliente actualizarCliente(Cliente cliente) {
        try {
            if (cliente == null || cliente.getDni() == null) {
                return null;
            }
            
            // Verificar que el cliente existe
            if (!clienteRepository.existsByDni(cliente.getDni())) {
                return null;
            }
            
            // Normalizar email
            if (cliente.getCorreoElectronico() != null) {
                cliente.setCorreoElectronico(cliente.getCorreoElectronico().toLowerCase());
            }
            
            return clienteRepository.save(cliente);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Cambia la contraseña de un cliente
     * @param correo 
     * @param nuevaContrasena 
     * @return 
     */
    public boolean cambiarContrasena(String correo, String nuevaContrasena) {
        try {
            if (correo == null || nuevaContrasena == null || nuevaContrasena.trim().isEmpty()) {
                return false;
            }
            
            Cliente cliente = buscarClientePorCorreo(correo);
            if (cliente != null) {
                cliente.setContrasena(nuevaContrasena);
                Cliente clienteActualizado = clienteRepository.save(cliente);
                return clienteActualizado != null;
            }
            
            return false;
            
        } catch (Exception e) {
            System.err.println("Error al cambiar contraseña: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * @deprecated Usar autenticar() en su lugar
     */
    @Deprecated
    public boolean inicioSesion(String correo, String contraseña) {
        return autenticar(correo, contraseña) != null;
    }
}