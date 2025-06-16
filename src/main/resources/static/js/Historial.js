document.addEventListener('DOMContentLoaded', function() {
    cargarHistorialUsuario();
    
    // Event listeners para los botones
    document.getElementById('btn-buscar-historial').addEventListener('click', buscarHistorial);
    document.getElementById('btn-mostrar-todo').addEventListener('click', cargarHistorialUsuario);
});

function cargarHistorialUsuario() {
    fetch('/historial/obtener')
        .then(response => {
            if (!response.ok) {
                if (response.status === 401) {
                    window.location.href = '/logeo';
                    return;
                }
                throw new Error('Error al cargar el historial');
            }
            return response.json();s
        })
        .then(data => {
            mostrarInformacionCliente(data.cliente);
            mostrarHistorial(data.historial);
            mostrarEstadisticas(data.estadisticas);
        })
        .catch(error => {
            console.error('Error:', error);
            mostrarMensaje('Error al cargar el historial', 'error');
        });
}

function buscarHistorial() {
    const dni = document.getElementById('filtro-dni').value.trim();
    
    if (!dni) {
        mostrarMensaje('Ingrese un DNI para buscar', 'warning');
        return;
    }
    
    fetch(`/historial/buscar?dni=${encodeURIComponent(dni)}`)
        .then(response => {
            if (!response.ok) {
                if (response.status === 404) {
                    throw new Error('Cliente no encontrado');
                }
                throw new Error('Error en la búsqueda');
            }
            return response.json();
        })
        .then(data => {
            mostrarInformacionCliente(data.cliente);
            mostrarHistorial(data.historial);
            mostrarEstadisticas(data.estadisticas);
            
            if (data.historial.length === 0) {
                mostrarMensaje('El cliente no tiene alquileres registrados', 'info');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            mostrarMensaje(error.message || 'Error en la búsqueda', 'error');
        });
}

function mostrarInformacionCliente(cliente) {
    if (!cliente) return;
    
    document.getElementById('usuario-nombre').textContent = 
        `${cliente.nombre} ${cliente.apellidos || ''}`.trim();
    document.getElementById('usuario-dni').textContent = cliente.dni;
    
    if (cliente.fechaRegistro) {
        document.getElementById('usuario-fecha-registro').textContent = 
            formatearFecha(cliente.fechaRegistro);
    }
}

function mostrarHistorial(historial) {
    const tbody = document.getElementById('historial-tbody');
    const mensajeNoHistorial = document.getElementById('mensaje-no-historial');
    const tabla = document.getElementById('tabla-historial');
    
    // Limpiar tabla
    tbody.innerHTML = '';
    
    if (!historial || historial.length === 0) {
        mensajeNoHistorial.style.display = 'block';
        tabla.style.display = 'none';
        return;
    }
    
    mensajeNoHistorial.style.display = 'none';
    tabla.style.display = 'table';
    
    historial.forEach(alquiler => {
        const fila = document.createElement('tr');
        
        // Obtener información de la reserva y bicicleta
        const reserva = alquiler.reserva;
        const bicicleta = reserva ? reserva.bicicleta : null;
        
        fila.innerHTML = `
            <td>${formatearFecha(alquiler.fechaAlquiler)}</td>
            <td>${formatearHora(alquiler.horaInicio)}</td>
            <td>${bicicleta ? `${bicicleta.marca || ''} ${bicicleta.nombreModelo || ''}`.trim() : 'N/A'}</td>
            <td>${alquiler.totalHoras || (alquiler.horaDevolucion ? 'Completado' : 'En curso')}</td>
            <td>S/ ${alquiler.costoTotal ? alquiler.costoTotal.toFixed(2) : '0.00'}</td>
            <td><span class="estado-${reserva ? reserva.estadoReserva.toLowerCase() : 'desconocido'}">${formatearEstado(reserva ? reserva.estadoReserva : 'DESCONOCIDO')}</span></td>
        `;
        tbody.appendChild(fila);
    });
}

function mostrarEstadisticas(estadisticas) {
    if (!estadisticas) return;
    
    const infoUsuario = document.getElementById('info-usuario');
    
    // Remover estadísticas anteriores si existen
    let estadisticasDiv = infoUsuario.querySelector('.estadisticas-usuario');
    if (estadisticasDiv) {
        estadisticasDiv.remove();
    }
    
    // Crear nuevo div de estadísticas
    estadisticasDiv = document.createElement('div');
    estadisticasDiv.className = 'estadisticas-usuario';
    estadisticasDiv.innerHTML = `
        <h4>Resumen de Actividad</h4>
        <p><strong>Total de Alquileres:</strong> ${estadisticas.totalAlquileres || 0}</p>
        <p><strong>Total Gastado:</strong> S/ ${estadisticas.totalGastado ? estadisticas.totalGastado.toFixed(2) : '0.00'}</p>
        <p><strong>Estado Actual:</strong> ${estadisticas.tieneAlquilerActivo ? 'Tiene Alquiler Activo' : 'Sin Alquiler Activo'}</p>
        ${estadisticas.ultimoAlquiler ? `<p><strong>Último Alquiler:</strong> ${formatearFecha(estadisticas.ultimoAlquiler.fechaAlquiler)}</p>` : ''}
    `;
    
    infoUsuario.appendChild(estadisticasDiv);
    
    // Mostrar mensaje de descuento si aplica
    const mensajeDescuento = document.getElementById('mensaje-descuento');
    if (estadisticas.totalAlquileres >= 5) {
        mensajeDescuento.innerHTML = '🎉 ¡Cliente frecuente! Ha realizado más de 5 alquileres.';
        mensajeDescuento.style.display = 'block';
    } else {
        mensajeDescuento.style.display = 'none';
    }
}

function formatearFecha(fecha) {
    if (!fecha) return 'N/A';
    
    // Si viene como timestamp de Java (LocalDateTime)
    if (typeof fecha === 'string' && fecha.includes('T')) {
        const date = new Date(fecha);
        return date.toLocaleDateString('es-PE');
    }
    
    // Si viene como Date de SQL
    const date = new Date(fecha);
    return date.toLocaleDateString('es-PE');
}

function formatearHora(hora) {
    if (!hora) return 'N/A';
    
    // Si viene como string "HH:mm:ss", extraer solo "HH:mm"
    if (typeof hora === 'string') {
        return hora.substring(0, 5);
    }
    
    // Si viene como timestamp
    const time = new Date(`1970-01-01T${hora}`);
    return time.toLocaleTimeString('es-PE', { 
        hour: '2-digit', 
        minute: '2-digit',
        hour12: false 
    });
}

function formatearEstado(estado) {
    const estados = {
        'PENDIENTE': 'Pendiente',
        'ENTREGADA': 'En Curso',
        'COMPLETADA': 'Completado',
        'CANCELADA': 'Cancelado'
    };
    return estados[estado] || estado;
}

function mostrarMensaje(mensaje, tipo) {
    // Crear o actualizar mensaje temporal
    let mensajeDiv = document.getElementById('mensaje-temporal');
    if (!mensajeDiv) {
        mensajeDiv = document.createElement('div');
        mensajeDiv.id = 'mensaje-temporal';
        document.querySelector('.historial-section').insertBefore(
            mensajeDiv, 
            document.querySelector('.filtros-historial')
        );
    }
    
    mensajeDiv.className = `mensaje-temporal ${tipo}`;
    mensajeDiv.textContent = mensaje;
    mensajeDiv.style.display = 'block';
    
    // Ocultar después de 5 segundos
    setTimeout(() => {
        mensajeDiv.style.display = 'none';
    }, 5000);
}

// Función para cerrar sesión
function cerrarSesion() {
    fetch('/logout', { method: 'POST' })
        .then(() => {
            window.location.href = '/';
        })
        .catch(() => {
            window.location.href = '/';
        });
}