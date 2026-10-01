const CLAVE_CARRITO = 'greenbyte_cart';

function leerCarrito() {
    try { return JSON.parse(localStorage.getItem(CLAVE_CARRITO)) || []; } catch { return []; }
}
function guardarCarrito(cesta) {
    localStorage.setItem(CLAVE_CARRITO, JSON.stringify(cesta));
    actualizarContador();
    dibujarCarrito();
}
function actualizarContador() {
    const cesta = leerCarrito();
    const conteo = cesta.reduce((s, i) => s + i.cantidad, 0);
    const insignia = document.getElementById('contador-carrito');
    if (insignia) {
        insignia.textContent = conteo;
        insignia.style.display = conteo > 0 ? 'inline' : 'none';
    }
}
function mostrarAviso(texto) {
    const elemento = document.getElementById('aviso-carrito');
    const textoAviso = document.getElementById('mensaje-aviso');
    if (!elemento || !textoAviso) { 
        alert(texto); return; 
    }
    textoAviso.textContent = texto;
    const aviso = new bootstrap.Toast(elemento, { delay: 2000 });
    aviso.show();
}
function agregarAlCarrito(producto, cantidad) {
    cantidad = Math.max(1, parseInt(cantidad) || 1);
    const cesta = leerCarrito();
    const existente = cesta.find(p => p.id === producto.id);
    if (existente) {
        existente.cantidad += cantidad;
    } else {
        cesta.push({ ...producto, cantidad });
    }
    guardarCarrito(cesta);
    mostrarAviso(producto.nombre + " agregado al carrito");
}
function quitarDelCarrito(id) {
    let cesta = leerCarrito();
    cesta = cesta.filter(p => p.id !== id);
    guardarCarrito(cesta);
}
function cambiarCantidad(id, cambio) {
    const cesta = leerCarrito();
    const articulo = cesta.find(p => p.id === id);
    if (!articulo) return;
    articulo.cantidad += cambio;
    if (articulo.cantidad <= 0) {
        guardarCarrito(cesta.filter(p => p.id !== id));
    } else {
        guardarCarrito(cesta);
    }
}
function vaciarCarrito() {
    if (!confirm('¿Vaciar carrito?')) return;
    localStorage.removeItem(CLAVE_CARRITO);
    actualizarContador();
    dibujarCarrito();
}
function finalizarCompra() {
    const cesta = leerCarrito();
    if (cesta.length === 0) return;
    const total = cesta.reduce((s, p) => s + p.precio * p.cantidad, 0).toFixed(2);
    const resumen = cesta.map(p => `${p.cantidad}x ${p.nombre} (S/.${p.precio})`).join('\n');
    alert(`¡Gracias por tu compra!\n\n${resumen}\n\nTotal: S/. ${total}\n`);
    localStorage.removeItem(CLAVE_CARRITO);
    actualizarContador();
    dibujarCarrito();
    const panel = bootstrap.Offcanvas.getInstance(document.getElementById('panel-carrito'));
    if (panel) panel.hide();
}

function dibujarCarrito() {
    const contenedor = document.getElementById('lista-carrito');
    const zonaVacia = document.getElementById('carrito-vacio');
    const pie = document.getElementById('pie-carrito');
    const elementoTotal = document.getElementById('total-carrito');
    if (!contenedor) return;
    const cesta = leerCarrito();

    contenedor.querySelectorAll('.articulo-carrito').forEach(e => e.remove());

    if (cesta.length === 0) {
        if (zonaVacia) zonaVacia.classList.remove('d-none');
        if (pie) pie.classList.add('d-none');
        return;
    }
    if (zonaVacia) zonaVacia.classList.add('d-none');
    if (pie) pie.classList.remove('d-none');

    let total = 0;
    cesta.forEach(articulo => {
        total += articulo.precio * articulo.cantidad;
        const div = document.createElement('div');
        div.className = 'articulo-carrito d-flex gap-2 align-items-center border-bottom border-secondary py-2';
        div.innerHTML = `
            <img src="${articulo.imagen}" alt="${articulo.nombre}" style="width:56px;height:56px;object-fit:cover;border-radius:8px;" onerror="this.src='/img-carousel/maceta-1.jpg'">
            <div class="flex-grow-1">
                <div class="fw-semibold small" style="line-height:1.2">${articulo.nombre}</div>
                <div class="small text-success">S/. ${articulo.precio.toFixed(2)} c/u</div>
                <div class="d-flex align-items-center gap-1 mt-1">
                    <button class="btn btn-sm btn-outline-light py-0 px-2" onclick="cambiarCantidad('${articulo.id}', -1)">−</button>
                    <span class="badge bg-light text-dark">${articulo.cantidad}</span>
                    <button class="btn btn-sm btn-outline-light py-0 px-2" onclick="cambiarCantidad('${articulo.id}', 1)">+</button>
                    <span class="ms-2 small">S/. ${(articulo.precio * articulo.cantidad).toFixed(2)}</span>
                </div>
            </div>
            <button class="btn btn-sm btn-outline-danger" onclick="quitarDelCarrito('${articulo.id}')" title="Eliminar"><i class="bi bi-trash"></i></button>
        `;
        contenedor.appendChild(div);
    });
    if (elementoTotal) elementoTotal.textContent = 'S/. ' + total.toFixed(2);
}

function filtrarProductos() {
    const textoBuscado = (document.getElementById('buscador-productos')?.value || '').toLowerCase().trim();
    if (!textoBuscado) {
        document.querySelectorAll('.tarjeta-producto').forEach(c => c.style.display = '');
        return;
    }
    document.querySelectorAll('.tarjeta-producto').forEach(card => {
        const text = card.innerText.toLowerCase();
        card.style.display = text.includes(textoBuscado) ? '' : 'none';
    });
}

document.addEventListener('click', (e) => {
    const boton = e.target.closest('[data-agregar-carrito]');
    if (!boton) return;
    e.preventDefault();
    const producto = {
        id: boton.dataset.id,
        nombre: boton.dataset.nombre,
        precio: parseFloat(boton.dataset.precio),
        imagen: boton.dataset.imagen
    };
    if (!producto.id || isNaN(producto.precio)) {
        console.error('Datos incompletos', boton.dataset);
        return;
    }
    agregarAlCarrito(producto);
});

document.addEventListener('DOMContentLoaded', () => {
    actualizarContador();
    dibujarCarrito();
    const buscador = document.getElementById('buscador-productos');
    if (buscador) buscador.addEventListener('input', filtrarProductos);
});
