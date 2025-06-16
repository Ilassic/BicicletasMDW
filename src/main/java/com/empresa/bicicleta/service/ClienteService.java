package com.empresa.bicicleta.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.empresa.bicicleta.model.Cliente;
import com.empresa.bicicleta.repository.ClienteRepository;

@Service
@Transactional
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private AlquilerService AlquilerService;

    // Crear cliente
    public Cliente crearCliente(Cliente cliente) {
        cliente.setFechaRegistro(LocalDateTime.now());
        return clienteRepository.save(cliente);
    }

    // Editar cliente
    public Cliente editarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Listar todos los clientes
    public List<Cliente> listarClientes() {
        return clienteRepository.findAll();
    }

    // Buscar cliente por correo electrónico
    public Optional<Cliente> buscarPorCorreo(String correoElectronico) {
        return clienteRepository.findByCorreoElectronico(correoElectronico);
    }

    // Actualizar información de cliente
    public Cliente actualizarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    // Eliminar cliente
    public void eliminarCliente(String dni) {
        clienteRepository.deleteById(dni);
    }

    // Verificar existencia de cliente por DNI
    public boolean existeClientePorDni(String dni) {
        return clienteRepository.existsByDni(dni);
    }

    // Verificar existencia por correo
    public boolean existeClientePorCorreo(String correo) {
        return clienteRepository.existsByCorreoElectronico(correo);
    }

    // Verificar existencia por teléfono
    public boolean existeClientePorTelefono(String telefono) {
        return clienteRepository.existsByTelefono(telefono);
    }

    // Validar datos de cliente
    public boolean validarDatos(Cliente cliente) {
        if (cliente.getDni() == null || cliente.getDni().trim().isEmpty()) {
            return false;
        }
        if (cliente.getNombre() == null || cliente.getNombre().trim().isEmpty()) {
            return false;
        }
        if (cliente.getApellidos() == null || cliente.getApellidos().trim().isEmpty()) {
            return false;
        }
        if (cliente.getCorreoElectronico() == null || !cliente.getCorreoElectronico().contains("@")) {
            return false;
        }
        if (cliente.getTelefono() == null || cliente.getTelefono().trim().isEmpty()) {
            return false;
        }
        if (cliente.getContrasena() == null || cliente.getContrasena().length() < 6) {
            return false;
        }
        return true;
    }

    // Validar datos para actualización
    public boolean validarDatosParaActualizacion(Cliente cliente) {
        // Verificar si el correo ya existe para otro cliente
        if (clienteRepository.existsCorreoForOtherClient(cliente.getCorreoElectronico(), cliente.getDni())) {
            return false;
        }
        // Verificar si el teléfono ya existe para otro cliente
        if (clienteRepository.existsTelefonoForOtherClient(cliente.getTelefono(), cliente.getDni())) {
            return false;
        }
        return validarDatos(cliente);
    }

    // Recuperar información de cliente por DNI
    public Optional<Cliente> recuperarInformacionCliente(String dni) {
        return clienteRepository.findByDni(dni);
    }

    //  Actualizar contraseña de cliente
    public boolean actualizarContrasena(String dni, String nuevaContrasena) {
        Optional<Cliente> clienteOpt = clienteRepository.findByDni(dni);
        if (clienteOpt.isPresent()) {
            Cliente cliente = clienteOpt.get();
            cliente.setContrasena(nuevaContrasena);
            clienteRepository.save(cliente);
            return true;
        }
        return false;
    }

    // Total Gastado - IMPLEMENTADO
    public BigDecimal calcularTotalGastado(String dniCliente) {
        return AlquilerService.calcularTotalGastadoPorCliente(dniCliente);
    }

    // Búsquedas adicionales
    public Optional<Cliente> buscarPorTelefono(String telefono) {
        return clienteRepository.findByTelefono(telefono);
    }

    public List<Cliente> buscarPorNombre(String nombre) {
        return clienteRepository.findByNombre(nombre);
    }

    public List<Cliente> buscarPorApellidos(String apellidos) {
        return clienteRepository.findByApellidos(apellidos);
    }

    public List<Cliente> buscarPorNombreOApellidos(String termino) {
        return clienteRepository.findByNombreOrApellidosContaining(termino);
    }

    // Búsquedas por fechas
    public List<Cliente> buscarClientesPorRangoFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return clienteRepository.findByFechaRegistroBetween(fechaInicio, fechaFin);
    }

    public List<Cliente> buscarClientesRegistradosDespuesDe(LocalDateTime fecha) {
        return clienteRepository.findByFechaRegistroAfter(fecha);
    }

    public List<Cliente> buscarClientesRegistradosAntesDe(LocalDateTime fecha) {
        return clienteRepository.findByFechaRegistroBefore(fecha);
    }

    // Autenticación básica
    public Optional<Cliente> autenticarCliente(String correo, String contrasena) {
        Optional<Cliente> clienteOpt = clienteRepository.findByCorreoElectronico(correo);
        if (clienteOpt.isPresent() && clienteOpt.get().getContrasena().equals(contrasena)) {
            return clienteOpt;
        }
        return Optional.empty();
    }

    // Contar total de clientes
    public long contarTotalClientes() {
        return clienteRepository.count();
    }

    // Verificar disponibilidad de datos únicos
    public boolean isCorreoDisponible(String correo) {
        return !clienteRepository.existsByCorreoElectronico(correo);
    }

    public boolean isTelefonoDisponible(String telefono) {
        return !clienteRepository.existsByTelefono(telefono);
    }

    public boolean isDniDisponible(String dni) {
        return !clienteRepository.existsByDni(dni);
    }
    // Obtener estadísticas completas del cliente
    public AlquilerService.AlquilerEstadisticas obtenerEstadisticasCompletas(String dni) {
        return AlquilerService.obtenerEstadisticasCliente(dni);
    }

    // Obtener número total de alquileres del cliente
    public Long obtenerTotalAlquileres(String dni) {
        return AlquilerService.contarAlquileresPorCliente(dni);
    }

    // Verificar si cliente tiene historial de alquileres
    public boolean tieneHistorialAlquileres(String dni) {
        return AlquilerService.contarAlquileresPorCliente(dni) > 0;
    }

    public Optional<Cliente> buscarPorDni(String dni) {
        return clienteRepository.findById(dni);
    }
}