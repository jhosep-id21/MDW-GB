function irARegistro(e) {
    if (e) e.preventDefault();
    document.getElementById('pestana-registro')?.click();
}

function mostrarMensaje(id, texto, exito) {
    const elemento = document.getElementById(id);
    if (!elemento) { 
        if (texto) alert(texto); return; 
    }
    if (!texto) { 
        elemento.classList.add('d-none'); return; 
    }
    elemento.textContent = texto;
    elemento.classList.remove('d-none', 'alert-danger', 'alert-success');
    elemento.classList.add(exito ? 'alert-success' : 'alert-danger');
}

async function crearCuenta(e) {
    e.preventDefault();
    mostrarMensaje('registro-error', '');
    mostrarMensaje('registro-aviso', '');

    const nombre = document.getElementById('registro-nombre')?.value?.trim();
    const correo = document.getElementById('registro-correo')?.value?.trim().toLowerCase();
    const telefono = document.getElementById('registro-telefono')?.value?.trim() || '999999999';
    const clave1 = document.getElementById('registro-clave')?.value;
    const clave2 = document.getElementById('registro-clave-repetida')?.value;

    if (!nombre || !correo || !clave1) { 
        mostrarMensaje('registro-error', 'Completa todos los campos obligatorios.'); 
        return false; 
    }
    if (clave1 !== clave2) { 
        mostrarMensaje('registro-error', 'Las contraseñas no coinciden.'); 
        return false; 
    }

    try {
        const respuesta = await fetch('/crear', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                nombre: nombre,
                telefono: telefono,
                acceso: 'U',
                correo: correo,
                clave: clave1
            })
        });

        if (respuesta.ok) {
            mostrarMensaje('registro-aviso', '¡Cuenta creada con éxito! Iniciando sesión...', true);
            setTimeout(() => {
                window.location.href = '/iniciar-sesion?correo=' + encodeURIComponent(correo) + '&clave=' + encodeURIComponent(clave1);
            }, 600);
        } else {
            mostrarMensaje('registro-error', 'No se pudo crear la cuenta. Verifica que el correo no esté ya registrado.');
        }
    } catch (err) {
        mostrarMensaje('registro-error', 'Error al conectar con el servidor.');
    }

    return false;
}
