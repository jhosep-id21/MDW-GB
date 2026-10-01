const CLAVE_USUARIOS = 'gb_users';

function leerUsuarios() {
    try {
        return JSON.parse(localStorage.getItem(CLAVE_USUARIOS)) || {};
    } catch {
        return {};
    }
}
function guardarUsuarios(datos) { 
    localStorage.setItem(CLAVE_USUARIOS, JSON.stringify(datos)); 
}

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

function crearCuenta(e) {
    e.preventDefault();
    mostrarMensaje('registro-error', ''); mostrarMensaje('registro-aviso', '');
    const nombre = document.getElementById('registro-nombre')?.value?.trim();
    const correo = document.getElementById('registro-correo')?.value?.trim().toLowerCase();
    const clave1 = document.getElementById('registro-clave')?.value;
    const clave2 = document.getElementById('registro-clave-repetida')?.value;
    if (!nombre || !correo || !clave1) { 
        mostrarMensaje('registro-error', 'Completa todos los campos.'); 
        return false; 
    }
    if (clave1 !== clave2) { 
        mostrarMensaje('registro-error', 'Las contraseñas no coinciden.'); 
        return false; 
    }
    if (correo === 'admin@greenbyte.com') { 
        mostrarMensaje('registro-error', 'Ese correo está reservado para elemento administrador.'); 
        return false; 
    }
    const usuarios = leerUsuarios();
    if (usuarios[correo]) { 
        mostrarMensaje('registro-error', 'Ese correo ya está registrado. Inicia sesión.'); 
        return false; 
    }
    usuarios[correo] = { 
        nombre, clave: clave1 
    };
    guardarUsuarios(usuarios);
    mostrarMensaje('registro-aviso', '¡Cuenta creada! Redirigiendo para iniciar sesión...', true);
    setTimeout(() => {
        window.location.href = '/login?correo=' + encodeURIComponent(correo) + '&clave=' + encodeURIComponent(clave1);
    }, 800);
    return false;
}
