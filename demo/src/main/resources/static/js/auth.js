/* Auth simple sin backend - solo localStorage, compatible con Controller solo GetMapping */
function getRol() { return localStorage.getItem('gb_rol'); }
function getUsuario() { return localStorage.getItem('gb_usuario'); }

function handleLogin(e) {
    e.preventDefault();
    const email = document.getElementById('email')?.value?.trim();
    const pass = document.getElementById('password')?.value;
    if (!email || !pass) return false;
    const rol = email.toLowerCase() === 'admin@greenbyte.com' ? 'ADMIN' : 'USER';
    localStorage.setItem('gb_usuario', email);
    localStorage.setItem('gb_rol', rol);
    updateAuthUI();
    const modal = bootstrap.Modal.getInstance(document.getElementById('loginModal'));
    if (modal) modal.hide();
    if (rol === 'ADMIN') {
        window.location.href = '/administrador';
    } else {
        // toast simple
        alert('¡Bienvenido ' + email + '!');
    }
    return false;
}
function logout() {
    localStorage.removeItem('gb_usuario');
    localStorage.removeItem('gb_rol');
    updateAuthUI();
    window.location.href = '/';
}
function updateAuthUI() {
    const usuario = getUsuario();
    const rol = getRol();
    const navLogin = document.getElementById('nav-login');
    const navUser = document.getElementById('nav-user');
    const userEmail = document.getElementById('user-email');
    const userRol = document.getElementById('user-rol');
    const adminLink = document.getElementById('nav-admin-link');
    const adminDiv = document.getElementById('nav-admin-divider');
    if (usuario && rol) {
        if (navLogin) navLogin.classList.add('d-none');
        if (navUser) navUser.classList.remove('d-none');
        if (userEmail) userEmail.textContent = usuario;
        if (userRol) {
            userRol.textContent = rol;
            userRol.className = 'badge ms-2 ' + (rol === 'ADMIN' ? 'bg-danger' : 'bg-success');
        }
        if (adminLink) adminLink.classList.toggle('d-none', rol !== 'ADMIN');
        if (adminDiv) adminDiv.classList.toggle('d-none', rol !== 'ADMIN');
    } else {
        if (navLogin) navLogin.classList.remove('d-none');
        if (navUser) navUser.classList.add('d-none');
    }
    // proteger /administrador si no es ADMIN (frontend)
    const isAdminPage = window.location.pathname === '/administrador';
    if (isAdminPage) {
        const noAuth = document.getElementById('admin-noauth');
        const adminPanel = document.getElementById('admin-panel');
        if (rol === 'ADMIN') {
            if (noAuth) noAuth.classList.add('d-none');
            if (adminPanel) adminPanel.classList.remove('d-none');
        } else {
            if (noAuth) noAuth.classList.remove('d-none');
            if (adminPanel) adminPanel.classList.add('d-none');
        }
    }
}
document.addEventListener('DOMContentLoaded', updateAuthUI);
