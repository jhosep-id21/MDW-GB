/* Vista detalle de producto: lee los datos del DOM de la card */
let productoDetalle = null;
let cantidadDetalle = 1;

function cambiarCantidadDetalle(d) {
    cantidadDetalle = Math.max(1, Math.min(99, cantidadDetalle + d));
    document.getElementById('cantidad-detalle').textContent = cantidadDetalle;
}

function agregarDetalle() {
    if (!productoDetalle) return;
    agregarAlCarrito(productoDetalle, cantidadDetalle);
    const ventana = bootstrap.Modal.getInstance(document.getElementById('modal-detalle'));
    if (ventana) ventana.hide();
}

document.addEventListener('click', (e) => {
    const boton = e.target.closest('.btn-detalle');
    if (!boton) return;
    const alcance = boton.closest('.tarjeta-producto') || document;
    const tarjeta = boton.closest('.tarjeta-tienda, .card') || alcance;
    const botonAgregar = alcance.querySelector('[data-agregar-carrito]');
    const foto = tarjeta.querySelector('img');
    const titulo = tarjeta.querySelector('.card-title, h5');
    const descripcion = tarjeta.querySelector('.card-text, .descripcion-tarjeta');
    const nombre = botonAgregar?.dataset.nombre || titulo?.innerText.trim() || 'Producto';
    const precio = parseFloat(botonAgregar?.dataset.precio || '0');
    productoDetalle = {
        id: botonAgregar?.dataset.id || nombre,
        nombre,
        precio: isNaN(precio) ? 0 : precio,
        imagen: botonAgregar?.dataset.imagen || foto?.getAttribute('src') || ''
    };
    cantidadDetalle = 1;
    document.getElementById('cantidad-detalle').textContent = '1';
    document.getElementById('nombre-detalle').textContent = nombre;
    document.getElementById('descripcion-detalle').textContent = descripcion?.innerText.trim() || '';
    document.getElementById('precio-detalle').textContent = 'S/. ' + productoDetalle.precio.toFixed(2);
    const dtImg = document.getElementById('foto-detalle');
    dtImg.src = productoDetalle.imagen;
    dtImg.alt = nombre;
    new bootstrap.Modal(document.getElementById('modal-detalle')).show();
});
