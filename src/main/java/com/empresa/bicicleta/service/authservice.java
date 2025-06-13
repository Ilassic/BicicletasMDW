package com.empresa.bicicleta.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ClienteRepository;

@Service
@Transactional
public class AuthService {

    @Autowired
    private ClienteRepository clienteRepository;
    
    /**
     * Autentica un usuario con correo y contraseña
     * @param correo Correo electrónico del usuario
     * @param contrasena Contraseña del usuario
     * @return Cliente autenticado o null si no es válido
     */
    public Cliente autenticar(String correo, String contrasena) {
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
            System.err.println("Error en autenticación: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Registra un nuevo cliente
     * @param cliente Cliente a registrar
     * @return Cliente registrado o null si hay error
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
     * @param correo Correo a verificar
     * @return true si existe, false si no
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
     * @param dni DNI a verificar
     * @return true si existe, false si no
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
     * @param telefono Teléfono a verificar
     * @return true si existe, false si no
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
     * @param correo Correo a buscar
     * @return Cliente encontrado o null
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
     * @param dni DNI a buscar
     * @return Cliente encontrado o null
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
     * @param cliente Cliente con datos actualizados
     * @return Cliente actualizado o null si hay error
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
     * @param correo Correo del cliente
     * @param nuevaContrasena Nueva contraseña
     * @return true si se cambió exitosamente, false si no
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
     * Método legacy para mantener compatibilidad
     * @deprecated Usar autenticar() en su lugar
     */
    @Deprecated
    public boolean inicioSesion(String correo, String contraseña) {
        return autenticar(correo, contraseña) != null;
    }
}