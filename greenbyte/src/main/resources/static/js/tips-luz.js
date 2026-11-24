function filtrarLuz(tipo, boton) {
    document.querySelectorAll('#lista-luces .tarjeta-tema').forEach(c =>
        c.style.display = (tipo === 'todos' || c.dataset.luz === tipo) ? '' : 'none'
    );
    document.querySelectorAll('.pill-filtro').forEach(b => b.classList.remove('active'));
    if (boton) boton.classList.add('active');
}
