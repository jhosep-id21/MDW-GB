const modalAcceso = document.getElementById('modal-acceso');

modalAcceso?.addEventListener('show.bs.modal', evento => {
    const aviso = document.getElementById('motivo-acceso');
    const mensaje = evento.relatedTarget?.dataset.accesoMensaje || '';
    aviso.textContent = mensaje;
    aviso.classList.toggle('d-none', !mensaje);
});

function irARegistro(evento) {
    evento?.preventDefault();
    document.getElementById('pestana-registro')?.click();
}

function mostrarMensaje(id, texto, exito = false) {
    const elemento = document.getElementById(id);
    if (!elemento) {
        if (texto) window.alert(texto);
        return;
    }

    elemento.textContent = texto || '';
    elemento.classList.toggle('d-none', !texto);
    elemento.classList.toggle('alert-success', Boolean(texto) && exito);
    elemento.classList.toggle('alert-danger', Boolean(texto) && !exito);
}

async function crearCuenta(evento) {
    evento.preventDefault();
    mostrarMensaje('registro-error', '');
    mostrarMensaje('registro-aviso', '');

    const nombre = document.getElementById('registro-nombre')?.value.trim();
    const correo = document.getElementById('registro-correo')?.value.trim().toLowerCase();
    const telefono = document.getElementById('registro-telefono')?.value.trim();
    const clave = document.getElementById('registro-clave')?.value;
    const repetirClave = document.getElementById('registro-clave-repetida')?.value;

    if (!nombre || !correo || !telefono || !clave || !repetirClave) {
        mostrarMensaje('registro-error', 'Completa todos los campos obligatorios.');
        return false;
    }
    if (clave.length < 8) {
        mostrarMensaje('registro-error', 'La contraseña debe tener al menos 8 caracteres.');
        return false;
    }
    if (clave !== repetirClave) {
        mostrarMensaje('registro-error', 'Las contraseñas no coinciden.');
        return false;
    }

    try {
        const respuesta = await fetch(evento.currentTarget.dataset.url, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ nombre, correo, telefono, acceso: 'U', clave })
        });

        if (!respuesta.ok) {
            const mensaje = await respuesta.text();
            mostrarMensaje('registro-error',
                mensaje.includes('registrado')
                    ? 'Este correo ya está registrado.'
                    : 'No se pudo crear la cuenta. Inténtalo nuevamente.');
            return false;
        }

        mostrarMensaje('acceso-aviso', '¡Cuenta creada! Ya puedes iniciar sesión con tus datos.', true);
        document.getElementById('formulario-registro')?.reset();
        window.setTimeout(() => {
            document.getElementById('pestana-acceso')?.click();
            document.getElementById('correo')?.focus();
        }, 900);
    } catch (error) {
        mostrarMensaje('registro-error', 'No se pudo conectar con el servidor.');
    }

    return false;
}
