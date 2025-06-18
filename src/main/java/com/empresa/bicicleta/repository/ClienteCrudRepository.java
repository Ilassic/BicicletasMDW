package com.empresa.bicicleta.repository;

import com.empresa.bicicleta.dto.CrudResult;
import com.empresa.bicicleta.model.Cliente;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository("clienteCrudRepository")
public class ClienteCrudRepository {

    @Autowired
    private DataSource dataSource;

    /**
     * Crear cliente usando procedimiento almacenado
     */
    public CrudResult crearCliente(Cliente cliente) {
        Connection connection = null;
        CallableStatement statement = null;
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL CrearCliente(?, ?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareCall(sql);
            
            // Parámetros de entrada
            statement.setString(1, cliente.getDni());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getApellidos());
            statement.setString(4, cliente.getTelefono());
            statement.setString(5, cliente.getCorreoElectronico());
            statement.setString(6, cliente.getContrasena());
            
            // Parámetros de salida
            statement.registerOutParameter(7, Types.VARCHAR); // p_status
            statement.registerOutParameter(8, Types.VARCHAR); // p_mensaje
            
            statement.execute();
            
            String status = statement.getString(7);
            String mensaje = statement.getString(8);
            
            CrudResult result = new CrudResult();
            result.setStatus(status);
            result.setMensaje(mensaje);
            result.setExitoso("SUCCESS".equals(status));
            
            return result;
            
        } catch (SQLException e) {
            System.err.println("Error al crear cliente: " + e.getMessage());
            CrudResult result = new CrudResult();
            result.setStatus("ERROR");
            result.setMensaje("Error de base de datos: " + e.getMessage());
            result.setExitoso(false);
            return result;
        } finally {
            cerrarRecursos(statement, connection);
        }
    }

    /**
     * Leer cliente por DNI
     */
    public Cliente leerClientePorDni(String dni) {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL LeerClientePorDni(?, ?)";
            statement = connection.prepareCall(sql);
            
            statement.setString(1, dni);
            statement.registerOutParameter(2, Types.VARCHAR); // p_status
            
            boolean hasResults = statement.execute();
            String status = statement.getString(2);
            
            if ("SUCCESS".equals(status) && hasResults) {
                resultSet = statement.getResultSet();
                if (resultSet.next()) {
                    return mapearCliente(resultSet);
                }
            }
            
            return null;
            
        } catch (SQLException e) {
            System.err.println("Error al leer cliente: " + e.getMessage());
            return null;
        } finally {
            cerrarRecursos(resultSet, statement, connection);
        }
    }

    /**
     * Leer todos los clientes
     */
    public List<Cliente> leerTodosClientes() {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        List<Cliente> clientes = new ArrayList<>();
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL LeerTodosClientes()";
            statement = connection.prepareCall(sql);
            
            boolean hasResults = statement.execute();
            
            if (hasResults) {
                resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    clientes.add(mapearCliente(resultSet));
                }
            }
            
            return clientes;
            
        } catch (SQLException e) {
            System.err.println("Error al leer clientes: " + e.getMessage());
            return clientes;
        } finally {
            cerrarRecursos(resultSet, statement, connection);
        }
    }

    /**
     * Actualizar cliente
     */
    public CrudResult actualizarCliente(Cliente cliente) {
        Connection connection = null;
        CallableStatement statement = null;
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL ActualizarCliente(?, ?, ?, ?, ?, ?, ?)";
            statement = connection.prepareCall(sql);
            
            // Parámetros de entrada
            statement.setString(1, cliente.getDni());
            statement.setString(2, cliente.getNombre());
            statement.setString(3, cliente.getApellidos());
            statement.setString(4, cliente.getTelefono());
            statement.setString(5, cliente.getCorreoElectronico());
            
            // Parámetros de salida
            statement.registerOutParameter(6, Types.VARCHAR); // p_status
            statement.registerOutParameter(7, Types.VARCHAR); // p_mensaje
            
            statement.execute();
            
            String status = statement.getString(6);
            String mensaje = statement.getString(7);
            
            CrudResult result = new CrudResult();
            result.setStatus(status);
            result.setMensaje(mensaje);
            result.setExitoso("SUCCESS".equals(status));
            
            return result;
            
        } catch (SQLException e) {
            System.err.println("Error al actualizar cliente: " + e.getMessage());
            CrudResult result = new CrudResult();
            result.setStatus("ERROR");
            result.setMensaje("Error de base de datos: " + e.getMessage());
            result.setExitoso(false);
            return result;
        } finally {
            cerrarRecursos(statement, connection);
        }
    }

    /**
     * Eliminar cliente
     */
    public CrudResult eliminarCliente(String dni) {
        Connection connection = null;
        CallableStatement statement = null;
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL EliminarCliente(?, ?, ?)";
            statement = connection.prepareCall(sql);
            
            statement.setString(1, dni);
            statement.registerOutParameter(2, Types.VARCHAR); // p_status
            statement.registerOutParameter(3, Types.VARCHAR); // p_mensaje
            
            statement.execute();
            
            String status = statement.getString(2);
            String mensaje = statement.getString(3);
            
            CrudResult result = new CrudResult();
            result.setStatus(status);
            result.setMensaje(mensaje);
            result.setExitoso("SUCCESS".equals(status));
            
            return result;
            
        } catch (SQLException e) {
            System.err.println("Error al eliminar cliente: " + e.getMessage());
            CrudResult result = new CrudResult();
            result.setStatus("ERROR");
            result.setMensaje("Error de base de datos: " + e.getMessage());
            result.setExitoso(false);
            return result;
        } finally {
            cerrarRecursos(statement, connection);
        }
    }

    /**
     * Buscar clientes por nombre
     */
    public List<Cliente> buscarClientesPorNombre(String nombre) {
        Connection connection = null;
        CallableStatement statement = null;
        ResultSet resultSet = null;
        List<Cliente> clientes = new ArrayList<>();
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL BuscarClientesPorNombre(?)";
            statement = connection.prepareCall(sql);
            
            statement.setString(1, nombre);
            
            boolean hasResults = statement.execute();
            
            if (hasResults) {
                resultSet = statement.getResultSet();
                while (resultSet.next()) {
                    clientes.add(mapearCliente(resultSet));
                }
            }
            
            return clientes;
            
        } catch (SQLException e) {
            System.err.println("Error al buscar clientes: " + e.getMessage());
            return clientes;
        } finally {
            cerrarRecursos(resultSet, statement, connection);
        }
    }

    /**
     * Contar total de clientes
     */
    public int contarClientes() {
        Connection connection = null;
        CallableStatement statement = null;
        
        try {
            connection = dataSource.getConnection();
            String sql = "CALL ContarClientes(?)";
            statement = connection.prepareCall(sql);
            
            statement.registerOutParameter(1, Types.INTEGER); // p_total
            
            statement.execute();
            
            return statement.getInt(1);
            
        } catch (SQLException e) {
            System.err.println("Error al contar clientes: " + e.getMessage());
            return 0;
        } finally {
            cerrarRecursos(statement, connection);
        }
    }

    /**
     * Mapear ResultSet a Cliente
     */
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setDni(rs.getString("dni"));
        cliente.setNombre(rs.getString("nombre"));
        cliente.setApellidos(rs.getString("apellidos"));
        cliente.setTelefono(rs.getString("telefono"));
        cliente.setCorreoElectronico(rs.getString("correo_electronico"));
        Timestamp timestamp = rs.getTimestamp("fecha_registro");
        if (timestamp != null) {
            cliente.setFechaRegistro(timestamp.toLocalDateTime());
        }
        return cliente;
    }

    /**
     * Cerrar recursos
     */
    private void cerrarRecursos(AutoCloseable... recursos) {
        for (AutoCloseable recurso : recursos) {
            if (recurso != null) {
                try {
                    recurso.close();
                } catch (Exception e) {
                    System.err.println("Error al cerrar recurso: " + e.getMessage());
                }
            }
        }
    }
}