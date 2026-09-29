const USERS_KEY = 'gb_users';

function getUsers() {
    try {
        return JSON.parse(localStorage.getItem(USERS_KEY)) || {};
    } catch {
        return {};
    }
}
function saveUsers(u) { 
    localStorage.setItem(USERS_KEY, JSON.stringify(u)); 
}

function irARegistro(e) {
    if (e) e.preventDefault();
    document.getElementById('tab-reg-btn')?.click();
}

function showMsg(id, msg, ok) {
    const el = document.getElementById(id);
    if (!el) { 
        if (msg) alert(msg); return; 
    }
    if (!msg) { 
        el.classList.add('d-none'); return; 
    }
    el.textContent = msg;
    el.classList.remove('d-none', 'alert-danger', 'alert-success');
    el.classList.add(ok ? 'alert-success' : 'alert-danger');
}

function handleRegister(e) {
    e.preventDefault();
    showMsg('reg-error', ''); showMsg('reg-ok', '');
    const nombre = document.getElementById('reg-nombre')?.value?.trim();
    const email = document.getElementById('reg-email')?.value?.trim().toLowerCase();
    const p1 = document.getElementById('reg-pass')?.value;
    const p2 = document.getElementById('reg-pass2')?.value;
    if (!nombre || !email || !p1) { 
        showMsg('reg-error', 'Completa todos los campos.'); 
        return false; 
    }
    if (p1 !== p2) { 
        showMsg('reg-error', 'Las contraseñas no coinciden.'); 
        return false; 
    }
    if (email === 'admin@greenbyte.com') { 
        showMsg('reg-error', 'Ese correo está reservado para el administrador.'); 
        return false; 
    }
    const users = getUsers();
    if (users[email]) { 
        showMsg('reg-error', 'Ese correo ya está registrado. Inicia sesión.'); 
        return false; 
    }
    users[email] = { 
        nombre, pass: p1 
    };
    saveUsers(users);
    showMsg('reg-ok', '¡Cuenta creada! Redirigiendo para iniciar sesión...', true);
    setTimeout(() => {
        window.location.href = '/login?email=' + encodeURIComponent(email) + '&password=' + encodeURIComponent(p1);
    }, 800);
    return false;
}
