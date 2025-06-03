package com.empresa.bicicleta.repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, String> {
    
    // Búsqueda básica por correo electrónico
    Optional<Cliente> findByCorreoElectronico(String correo);
    
    // Verificar si existe un cliente por correo
    boolean existsByCorreoElectronico(String correo);
    
    // Buscar cliente por DNI 
    Optional<Cliente> findByDni(String dni);
    
    // Verificar si existe por DNI
    boolean existsByDni(String dni);
    
    // Buscar por teléfono
    Optional<Cliente> findByTelefono(String telefono);
    
    // Verificar si existe por teléfono
    boolean existsByTelefono(String telefono);
    
    // Buscar por nombre
    List<Cliente> findByNombre(String nombre);
    
    // Buscar por apellidos
    List<Cliente> findByApellidos(String apellidos);
    
    // Buscar por nombre o apellidos (búsqueda parcial)
    @Query("SELECT c FROM Cliente c WHERE " + "LOWER(c.nombre) LIKE LOWER(CONCAT('%', :termino, '%')) OR " +"LOWER(c.apellidos) LIKE LOWER(CONCAT('%', :termino, '%'))")
    List<Cliente> findByNombreOrApellidosContaining(@Param("termino") String termino);
    
    // Buscar clientes registrados en un rango de fechas
    @Query("SELECT c FROM Cliente c WHERE c.fechaRegistro BETWEEN :fechaInicio AND :fechaFin")
    List<Cliente> findByFechaRegistroBetween(
        @Param("fechaInicio") LocalDateTime fechaInicio, 
        @Param("fechaFin") LocalDateTime fechaFin
    );
    
    // Buscar clientes registrados después de una fecha
    List<Cliente> findByFechaRegistroAfter(LocalDateTime fecha);
    
    // Buscar clientes registrados antes de una fecha
    List<Cliente> findByFechaRegistroBefore(LocalDateTime fecha);
    
    // Contar clientes registrados por mes
    @Query("SELECT YEAR(c.fechaRegistro) as año, MONTH(c.fechaRegistro) as mes, COUNT(c) as total " +
           "FROM Cliente c " +
           "GROUP BY YEAR(c.fechaRegistro), MONTH(c.fechaRegistro) " +
           "ORDER BY año DESC, mes DESC")
    List<Object[]> countClientesPorMes();
    
    // Verificar disponibilidad de correo para actualización
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c " +
           "WHERE c.correoElectronico = :correo AND c.dni != :dni")
    boolean existsCorreoForOtherClient(@Param("correo") String correo, @Param("dni") String dni);
    
    // Verificar disponibilidad de teléfono para actualización
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cliente c " +
           "WHERE c.telefono = :telefono AND c.dni != :dni")
    boolean existsTelefonoForOtherClient(@Param("telefono") String telefono, @Param("dni") String dni);
       
    // Estadísticas: Total de clientes registrados
    @Query("SELECT COUNT(c) FROM Cliente c")
    Long countTotalClientes();
}
