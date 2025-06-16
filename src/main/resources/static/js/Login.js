document.addEventListener('DOMContentLoaded', () => {
    // Selección de Elementos 
    const loginTab = document.getElementById('login-tab');
    const registerTab = document.getElementById('register-tab');
    const loginFormSection = document.getElementById('login-form');
    const registerFormSection = document.getElementById('register-form');
    const loginForm = document.getElementById('loginForm');
    const registerForm = document.getElementById('registerForm');

    // Inputs de Registro
    const nombreInput = document.getElementById('nombre-register');
    const apellidosInput = document.getElementById('apellidos-register');
    const dniInput = document.getElementById('dni-register');
    const telefonoInput = document.getElementById('telefono-register');
    const emailInput = document.getElementById('email-register');
    const passwordInput = document.getElementById('password-register');
    const confirmPasswordInput = document.getElementById('confirm-password');
    const termsCheckbox = document.getElementById('terms');

    // Inputs de Login
    const emailLoginInput = document.getElementById('email-login');
    const passwordLoginInput = document.getElementById('password-login');

    // Mostrar Contraseña
    const showPasswordLogin = document.getElementById('show-password-login');
    const showPasswordRegister = document.getElementById('show-password-register');

    // Link Olvidaste Contraseña
    const forgotPasswordLink = document.querySelector('.forgot-password');

    // Lógica para Cambiar Pestañas (Login/Registro)
    if (loginTab && registerTab && loginFormSection && registerFormSection) {
        loginTab.addEventListener('click', () => {
            loginTab.classList.add('active'); registerTab.classList.remove('active');
            loginFormSection.classList.add('active'); registerFormSection.classList.remove('active');
        });
        registerTab.addEventListener('click', () => {
            registerTab.classList.add('active'); loginTab.classList.remove('active');
            registerFormSection.classList.add('active'); loginFormSection.classList.remove('active');
        });
    }

    // --- Lógica para Mostrar/Ocultar Contraseña ---
    function setupShowPasswordToggle(checkbox, passwordField) {
        if (checkbox && passwordField) {
            checkbox.addEventListener('change', function () {
                passwordField.type = this.checked ? 'text' : 'password';
            });
        }
    }
    setupShowPasswordToggle(showPasswordLogin, passwordLoginInput);
    setupShowPasswordToggle(showPasswordRegister, passwordInput);

    // --- Lógica del Modal Principal (Errores/Confirmaciones) ---
    const modal = document.getElementById('mensajeModal');
    const modalTitulo = document.getElementById('modal-titulo');
    const modalMensaje = document.getElementById('modal-mensaje');
    const cerrarModalBtn = modal ? modal.querySelector('.close') : null;
    const aceptarBtn = modal ? modal.querySelector('#btnAceptar') : null;

    function mostrarModal(titulo, mensaje) {
        if (modal && modalTitulo && modalMensaje) {
            modalTitulo.textContent = titulo;
            modalMensaje.textContent = mensaje;
            modal.style.display = 'block';
        } else {
            console.error("Error: Elementos del modal 'mensajeModal' no encontrados.");
            alert(titulo + "\n" + mensaje); 
        }
    }
    function ocultarModal() {
        if (modal) modal.style.display = "none";
    }
    if (cerrarModalBtn) cerrarModalBtn.onclick = ocultarModal;
    if (aceptarBtn) aceptarBtn.onclick = ocultarModal;

    // --- Lógica del Modal de Recuperación de Contraseña ---
    function mostrarModalRecuperacion() {
        let recuperacionModal = document.getElementById('recuperacionModal');
        if (!recuperacionModal) {
             const modalHTML = `
            <div id="recuperacionModal" class="modal" style="display: none;">
                <div class="modal-content">
                    <span class="close-recovery">&times;</span>
                    <h2>Recuperar Contraseña</h2><p>Ingresa tu correo electrónico registrado.</p>
                    <form id="recuperarForm"><div class="form-group"><label for="recovery-email">Correo Electrónico:</label><input type="email" id="recovery-email" name="recovery-email" required></div><div class="modal-footer"><button type="submit" class="btn-confirmar">Enviar Instrucciones</button></div></form>
                </div></div>`;
             document.body.insertAdjacentHTML('beforeend', modalHTML);
             recuperacionModal = document.getElementById('recuperacionModal');
             const closeRecoveryBtn = recuperacionModal ? recuperacionModal.querySelector('.close-recovery') : null;
             if(closeRecoveryBtn) closeRecoveryBtn.onclick = () => { if(recuperacionModal) recuperacionModal.style.display = 'none'; };
             const recuperarForm = document.getElementById('recuperarForm');
             if(recuperarForm) {
                 recuperarForm.onsubmit = (e) => {
                    e.preventDefault();
                    const email = document.getElementById('recovery-email').value.trim().toLowerCase();
                    if(recuperacionModal) recuperacionModal.style.display = 'none';
                    mostrarModal('Función no disponible', 'La recuperación de contraseña se implementará próximamente.');
                };
             }
        }
        if (recuperacionModal) recuperacionModal.style.display = 'block';
    }

     // --- Listener Global para Cerrar Modales al Hacer Clic Fuera ---
    window.addEventListener('click', (event) => {
        if (modal && event.target == modal) ocultarModal();
        const recuperacionModal = document.getElementById('recuperacionModal');
        if (recuperacionModal && event.target == recuperacionModal) recuperacionModal.style.display = "none";
    });

    // --- Validaciones y Formateo en Tiempo Real para Registro ---

    // Función auxiliar para limitar letras (excluyendo espacios)
    function limitarLetras(inputElement, maxLength) {
        let value = inputElement.value;
        let lettersOnly = value.replace(/[^a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\s]/g, ''); 
        let lettersCount = lettersOnly.replace(/\s/g, '').length; 

        if (lettersCount > maxLength) {
            let currentLength = 0;
            let truncatedValue = '';
            for (let i = 0; i < lettersOnly.length; i++) {
                truncatedValue += lettersOnly[i];
                if (lettersOnly[i] !== ' ') {
                    currentLength++;
                }
                if (currentLength >= maxLength) {
                    break;
                }
            }
             lettersOnly = truncatedValue;
        }
         // Actualizar valor solo si cambió para evitar bucles infinitos
        if (inputElement.value !== lettersOnly) {
            inputElement.value = lettersOnly;
        }
    }

    // Nombre: Solo letras, max 10 (sin espacios)
    if (nombreInput) {
        nombreInput.addEventListener('input', () => limitarLetras(nombreInput, 10));
    }

    // Apellidos: Solo letras, max 10 (sin espacios)
    if (apellidosInput) {
        apellidosInput.addEventListener('input', () => limitarLetras(apellidosInput, 10));
    }

    // DNI: Solo números, max 8
    if (dniInput) {
        dniInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, ''); 
            e.target.value = value.slice(0, 8); 
        });
    }

    if (telefonoInput) {
        telefonoInput.addEventListener('input', (e) => {
            let value = e.target.value.replace(/\D/g, ''); 

            if (!value.startsWith('9')) {
                value = '';
            }

            let digits = value.slice(0, 9); 

            let formattedValue = '';
            if (digits.length > 6) formattedValue = digits.slice(0, 3) + ' ' + digits.slice(3, 6) + ' ' + digits.slice(6);
            else if (digits.length > 3) formattedValue = digits.slice(0, 3) + ' ' + digits.slice(3);
            else formattedValue = digits;

             if (e.target.value !== formattedValue) {
                e.target.value = formattedValue;
             }
        });
    }

    // --- Listener para el Formulario de Inicio de Sesión ---
    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();
            const correo = emailLoginInput.value.trim();
            const password = passwordLoginInput.value.trim();
            const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/; 

            if (!regexCorreo.test(correo)) return mostrarModal('Error', 'Correo electrónico no válido.');
            if (!password) return mostrarModal('Error', 'Por favor, ingrese su contraseña.');

            try {
                const response = await fetch('/api/auth/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        email: correo,
                        password: password
                    })
                });

                const data = await response.json();

                if (data.success) {
                    mostrarModal('¡Inicio de Sesión Exitoso!', 'Serás redirigido a la pantalla correspondiente para que puedas reservar el alquiler de tu bicicleta.');
                    setTimeout(() => { 
                        window.location.href = data.redirectUrl || '/reservas-bicicletas'; 
                    }, 2000);
                } else {
                    mostrarModal('Error de Inicio de Sesión', data.message || 'El correo electrónico o la contraseña son incorrectos.');
                }
            } catch (error) {
                console.error('Error:', error);
                mostrarModal('Error de Conexión', 'Error de conexión. Por favor intenta nuevamente.');
            }
        });
    }

    // --- Listener para el Formulario de Registro (Validación Final) ---
    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault(); 

            // Obtener valores finales
            const nombre = nombreInput.value.trim();
            const apellidos = apellidosInput.value.trim();
            const dni = dniInput.value.trim(); 
            const telefono = telefonoInput.value.trim(); 
            const correo = emailInput.value.trim().toLowerCase();
            const password = passwordInput.value;
            const confirmPassword = confirmPasswordInput.value;
            const terms = termsCheckbox.checked;

            // Nombre
            const nombreSinEspacios = nombre.replace(/\s+/g, '');
            if (!/^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\s]+$/.test(nombre) || nombreSinEspacios.trim().length === 0) {
                return mostrarModal('Error de Registro', 'Nombre: Solo se permiten letras y no puede estar vacío.');
            }
            if (nombreSinEspacios.length > 10) {
                return mostrarModal('Error de Registro', 'Nombre: No debe exceder las 10 letras (sin contar espacios).');
            }

            // Apellidos
            const apellidosSinEspacios = apellidos.replace(/\s/g, '');
            if (!/^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\s]+$/.test(apellidos) || apellidosSinEspacios.length === 0) {
                 return mostrarModal('Error de Registro', 'Apellidos: Solo se permiten letras y no puede estar vacío.');
            }
            if (apellidosSinEspacios.length > 10) {
                 return mostrarModal('Error de Registro', 'Apellidos: No deben exceder las 10 letras (sin contar espacios).');
            }

            //  DNI
            if (!/^[0-9]{8}$/.test(dni)) {
                return mostrarModal('Error de Registro', 'DNI: Debe contener exactamente 8 números.');
            }
            const primerDigitoDNI = dni.charAt(0);
            if (!['0', '1', '4', '6', '7'].includes(primerDigitoDNI)) {
                return mostrarModal('Error de Registro', 'DNI: El primer dígito no es válido (debe ser 0, 1, 4, 6 o 7).');
            }

            // Teléfono
            const telefonoSinEspacios = telefono.replace(/\s+/g, '');
             if (!/^[9][0-9]{8}$/.test(telefonoSinEspacios)) {
                 return mostrarModal('Error de Registro', 'Teléfono: Debe ser un celular válido de 9 dígitos que empiece con 9.');
             }

            // Correo
            const regexCorreo = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
            if (!regexCorreo.test(correo)) {
                return mostrarModal('Error de Registro', 'Correo Electrónico: El formato no es válido.');
            }

             // Contraseña
             if (password.length !== 12) {
                 return mostrarModal('Error de Registro', 'Contraseña: Debe tener exactamente 12 caracteres.');
             }

             // Confirmar Contraseña
             if (password !== confirmPassword) {
                 return mostrarModal('Error de Registro', 'Confirmar Contraseña: Las contraseñas no coinciden.');
             }

             // Términos y Condiciones
             if (!terms) {
                 return mostrarModal('Error de Registro', 'Debes aceptar los Términos y Condiciones.');
             }

            try {
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                    },
                    body: JSON.stringify({
                        dni: dni,
                        nombre: nombre,
                        apellidos: apellidos,
                        telefono: telefonoSinEspacios,
                        email: correo,
                        password: password,
                        confirmPassword: confirmPassword
                    })
                });

                const data = await response.json();

                if (data.success) {
                    mostrarModal('¡Registro Exitoso!', 'Tu cuenta ha sido creada. Ya puedes iniciar sesión.');
                    registerForm.reset();
                    // Cambiar a tab de login después del registro exitoso
                    setTimeout(() => {
                        loginTab.click();
                    }, 2000);
                } else {
                    mostrarModal('Error de Registro', data.message);
                }
            } catch (error) {
                console.error('Error:', error);
                mostrarModal('Error de Conexión', 'Error de conexión. Por favor intenta nuevamente.');
            }
        });
    }

    if (forgotPasswordLink) {
        forgotPasswordLink.addEventListener('click', (e) => {
            e.preventDefault();
            mostrarModalRecuperacion();
        });
    }

});