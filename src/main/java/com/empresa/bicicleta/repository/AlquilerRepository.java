package com.empresa.bicicleta.repository;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.empresa.bicicleta.model.Alquiler;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Integer>{
    
    // Buscar alquileres por DNI del cliente - Para historial del usuario
    List<Alquiler> findByReservaClienteDniOrderByFechaAlquilerDesc(String dniCliente);
    
    // Buscar alquiler activo por DNI del cliente - Para verificar si tiene alquiler en curso
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni AND a.horaDevolucion IS NULL")
    Optional<Alquiler> findAlquilerActivoByCliente(@Param("dni") String dniCliente);
    
    // Buscar alquiler por ID de reserva - Para procesar el alquiler de una reserva
    Optional<Alquiler> findByReservaId(Integer idReserva);
    
    // Buscar alquileres por código de bicicleta - Para historial de uso de bicicleta
    List<Alquiler> findByReservaBicicletaCodigoBicicletaOrderByFechaAlquilerDesc(String codigoBicicleta);
    
    // Verificar si una bicicleta está actualmente alquilada - Para disponibilidad
    @Query("SELECT COUNT(a) > 0 FROM Alquiler a WHERE a.reserva.bicicleta.codigoBicicleta = :codigoBicicleta AND a.horaDevolucion IS NULL")
    boolean existeAlquilerActivoBicicleta(@Param("codigoBicicleta") String codigoBicicleta);
    
    // Buscar alquileres por fecha específica - Para consultas del usuario
    List<Alquiler> findByFechaAlquilerOrderByHoraInicioDesc(Date fechaAlquiler);
    
    // Buscar alquileres en rango de fechas para un cliente - Para historial filtrado
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni AND a.fechaAlquiler BETWEEN :fechaInicio AND :fechaFin ORDER BY a.fechaAlquiler DESC")
    List<Alquiler> findAlquileresByClienteAndFechaRange(
        @Param("dni") String dniCliente,
        @Param("fechaInicio") Date fechaInicio, 
        @Param("fechaFin") Date fechaFin
    );
    
    // Calcular costo total de alquileres de un cliente - Para mostrar gastos totales
    @Query("SELECT COALESCE(SUM(a.costoTotal), 0) FROM Alquiler a WHERE a.reserva.cliente.dni = :dni")
    BigDecimal calcularTotalGastadoByCliente(@Param("dni") String dniCliente);
    
    // Contar total de alquileres de un cliente - Para estadísticas personales
    Long countByReservaClienteDni(String dniCliente);
    
    // Buscar último alquiler de un cliente - Para mostrar última actividad
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni ORDER BY a.fechaAlquiler DESC, a.horaInicio DESC LIMIT 1")
    Optional<Alquiler> findUltimoAlquilerByCliente(@Param("dni") String dniCliente);
    
    // Buscar alquileres completados (con hora de devolución) por cliente
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni AND a.horaDevolucion IS NOT NULL ORDER BY a.fechaAlquiler DESC")
    List<Alquiler> findAlquileresCompletadosByCliente(@Param("dni") String dniCliente);
    
    // Buscar alquileres por método de pago y cliente - Para filtros de historial
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni AND a.metodoPago.tipoPago = :tipoPago ORDER BY a.fechaAlquiler DESC")
    List<Alquiler> findByClienteAndMetodoPago(@Param("dni") String dniCliente, @Param("tipoPago") String tipoPago);
    
    // Verificar si existe alquiler con ID específico para un cliente - Para validaciones de seguridad
    @Query("SELECT COUNT(a) > 0 FROM Alquiler a WHERE a.id = :idAlquiler AND a.reserva.cliente.dni = :dni")
    boolean existeAlquilerForCliente(@Param("idAlquiler") Integer idAlquiler, @Param("dni") String dniCliente);
    
    // Buscar alquileres por costo en rango para un cliente - Para filtros de búsqueda
    @Query("SELECT a FROM Alquiler a WHERE a.reserva.cliente.dni = :dni AND a.costoTotal BETWEEN :costoMin AND :costoMax ORDER BY a.fechaAlquiler DESC")
    List<Alquiler> findByClienteAndCostoRange(
        @Param("dni") String dniCliente,
        @Param("costoMin") BigDecimal costoMinimo, 
        @Param("costoMax") BigDecimal costoMaximo
    );
    
    // Actualizar hora de devolución - Para finalizar alquiler
    @Query("UPDATE Alquiler a SET a.horaDevolucion = :horaDevolucion WHERE a.id = :idAlquiler")
    void actualizarHoraDevolucion(@Param("idAlquiler") Integer idAlquiler, @Param("horaDevolucion") Time horaDevolucion);
}