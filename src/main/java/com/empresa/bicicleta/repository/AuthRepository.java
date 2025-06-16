package com.empresa.bicicleta.repository;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.empresa.bicicleta.dto.LoginResult;

@Repository
public class AuthRepository {

    @Autowired
    private DataSource dataSource;
    
    /**
     * Ejecuta el procedimiento almacenado para login
     * @param email Correo electrónico del usuario
     * @param password Contraseña del usuario
     * @return LoginResult con el estado de la operación
     */
    public LoginResult ejecutarLoginProcedimiento(String email, String password) {
        Connection connection = null;
        CallableStatement statement = null;
        
        try {
            connection = dataSource.getConnection();
            
            // Preparar la llamada al procedimiento almacenado
            String sql = "CALL Login(?, ?, ?)";
            statement = connection.prepareCall(sql);
            
            // Parámetros de entrada
            statement.setString(1, email);
            statement.setString(2, password);
            
            // Parámetro de salida
            statement.registerOutParameter(3, Types.VARCHAR); // p_status
            
            // Ejecutar el procedimiento
            statement.execute();
            
            // Obtener el resultado
            String status = statement.getString(3);
            
            // Crear resultado basado en el status
            LoginResult result = new LoginResult();
            result.setStatus(status != null ? status : "ERROR");
            result.setExitoso("SUCCESS".equals(status));
            
            return result;
            
        } catch (SQLException e) {
            System.err.println("Error al ejecutar procedimiento de login: " + e.getMessage());
            e.printStackTrace();
            
            LoginResult result = new LoginResult();
            result.setStatus("ERROR");
            result.setExitoso(false);
            return result;
            
        } finally {
            // Cerrar recursos
            try {
                if (statement != null) statement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}