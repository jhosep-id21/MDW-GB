const CLAVE_CARRITO = 'greenbyte_cart';

function leerCarrito() {
    try {
        const datos = JSON.parse(localStorage.getItem(CLAVE_CARRITO) || '[]');
        return Array.isArray(datos)
            ? datos.filter(item => item && item.id != null && Number.isFinite(Number(item.precio))
                && Number.isFinite(Number(item.cantidad)) && Number(item.cantidad) > 0)
            : [];
    } catch {
        return [];
    }
}

function guardarCarrito(cesta) {
    localStorage.setItem(CLAVE_CARRITO, JSON.stringify(cesta));
    actualizarContador();
    dibujarCarrito();
}

function actualizarContador() {
    const cantidad = leerCarrito().reduce((total, item) => total + Number(item.cantidad), 0);
    const insignia = document.getElementById('contador-carrito');
    if (!insignia) return;

    insignia.textContent = cantidad;
    insignia.style.display = cantidad > 0 ? 'inline-flex' : 'none';
}

function mostrarAviso(texto) {
    const elemento = document.getElementById('aviso-carrito');
    const textoAviso = document.getElementById('mensaje-aviso');

    if (!elemento || !textoAviso || typeof bootstrap === 'undefined') {
        window.alert(texto);
        return;
    }

    textoAviso.textContent = texto;
    bootstrap.Toast.getOrCreateInstance(elemento, { delay: 2000 }).show();
}

function agregarAlCarrito(producto, cantidad = 1) {
    const precio = Number(producto.precio);
    const id = String(producto.id ?? '');

    if (!id || !Number.isFinite(precio) || precio < 0) {
        console.error('No se pudo agregar el producto: datos inválidos.', producto);
        return;
    }

    const unidades = Math.max(1, Math.floor(Number(cantidad) || 1));
    const cesta = leerCarrito();
    const existente = cesta.find(item => String(item.id) === id);

    if (existente) {
        existente.cantidad = Number(existente.cantidad) + unidades;
    } else {
        cesta.push({
            id,
            nombre: String(producto.nombre || 'Producto'),
            precio,
            imagen: String(producto.imagen || '/img-carousel/imagen-plantas.jpg'),
            cantidad: unidades
        });
    }

    guardarCarrito(cesta);
    mostrarAviso(`${producto.nombre || 'Producto'} agregado al carrito`);
}

function quitarDelCarrito(id) {
    guardarCarrito(leerCarrito().filter(item => String(item.id) !== String(id)));
}

function cambiarCantidad(id, cambio) {
    const cesta = leerCarrito();
    const articulo = cesta.find(item => String(item.id) === String(id));
    if (!articulo) return;

    articulo.cantidad = Number(articulo.cantidad) + Number(cambio);
    guardarCarrito(cesta.filter(item => item.cantidad > 0));
}

function vaciarCarrito() {
    if (!window.confirm('¿Deseas vaciar el carrito?')) return;
    localStorage.removeItem(CLAVE_CARRITO);
    actualizarContador();
    dibujarCarrito();
}

function finalizarCompra() {
    const cesta = leerCarrito();
    if (!cesta.length) return;

    const total = cesta.reduce((suma, item) => suma + Number(item.precio) * item.cantidad, 0);
    const resumen = cesta
        .map(item => `${item.cantidad} × ${item.nombre} (S/ ${Number(item.precio).toFixed(2)})`)
        .join('\n');

    window.alert(`Resumen de demostración (sin pago real)\n\n${resumen}\n\nTotal: S/ ${total.toFixed(2)}`);
    localStorage.removeItem(CLAVE_CARRITO);
    actualizarContador();
    dibujarCarrito();

    const panel = document.getElementById('panel-carrito');
    if (panel && typeof bootstrap !== 'undefined') {
        bootstrap.Offcanvas.getInstance(panel)?.hide();
    }
}

function crearTexto(tag, clase, texto) {
    const elemento = document.createElement(tag);
    if (clase) elemento.className = clase;
    elemento.textContent = texto;
    return elemento;
}

function dibujarCarrito() {
    const contenedor = document.getElementById('lista-carrito');
    if (!contenedor) return;

    const zonaVacia = document.getElementById('carrito-vacio');
    const pie = document.getElementById('pie-carrito');
    const elementoTotal = document.getElementById('total-carrito');
    const cesta = leerCarrito();

    contenedor.querySelectorAll('.articulo-carrito').forEach(elemento => elemento.remove());

    if (!cesta.length) {
        zonaVacia?.classList.remove('d-none');
        pie?.classList.add('d-none');
        return;
    }

    zonaVacia?.classList.add('d-none');
    pie?.classList.remove('d-none');

    let total = 0;
    cesta.forEach(articulo => {
        const precio = Number(articulo.precio);
        const subtotal = precio * Number(articulo.cantidad);
        total += subtotal;

        const fila = document.createElement('div');
        fila.className = 'articulo-carrito d-flex gap-2 align-items-center border-bottom py-2';

        const imagen = document.createElement('img');
        imagen.src = articulo.imagen || '/img-carousel/imagen-plantas.jpg';
        imagen.alt = String(articulo.nombre);
        imagen.loading = 'lazy';
        imagen.style.cssText = 'width:56px;height:56px;object-fit:cover;border-radius:8px;';
        imagen.addEventListener('error', () => {
            imagen.src = '/img-carousel/imagen-plantas.jpg';
        }, { once: true });

        const contenido = document.createElement('div');
        contenido.className = 'flex-grow-1';

        contenido.append(
            crearTexto('div', 'fw-semibold small', String(articulo.nombre)),
            crearTexto('div', 'small text-success', `S/ ${precio.toFixed(2)} c/u`)
        );

        const controles = document.createElement('div');
        controles.className = 'd-flex align-items-center gap-1 mt-1';

        const menos = crearTexto('button', 'btn btn-sm btn-outline-secondary py-0 px-2', '−');
        menos.type = 'button';
        menos.dataset.cartAction = 'menos';
        menos.dataset.id = articulo.id;

        const cantidad = crearTexto('span', 'badge bg-light text-dark', String(articulo.cantidad));

        const mas = crearTexto('button', 'btn btn-sm btn-outline-secondary py-0 px-2', '+');
        mas.type = 'button';
        mas.dataset.cartAction = 'mas';
        mas.dataset.id = articulo.id;

        const subtotalTexto = crearTexto('span', 'ms-2 small', `S/ ${subtotal.toFixed(2)}`);
        controles.append(menos, cantidad, mas, subtotalTexto);
        contenido.append(controles);

        const quitar = crearTexto('button', 'btn btn-sm btn-outline-danger', '×');
        quitar.type = 'button';
        quitar.title = 'Eliminar producto';
        quitar.setAttribute('aria-label', `Eliminar ${articulo.nombre}`);
        quitar.dataset.cartAction = 'eliminar';
        quitar.dataset.id = articulo.id;

        fila.append(imagen, contenido, quitar);
        contenedor.append(fila);
    });

    if (elementoTotal) elementoTotal.textContent = `S/ ${total.toFixed(2)}`;
}

function filtrarProductos() {
    const texto = (document.getElementById('buscador-productos')?.value || '').toLocaleLowerCase('es').trim();

    document.querySelectorAll('.tarjeta-producto').forEach(tarjeta => {
        tarjeta.style.display = tarjeta.innerText.toLocaleLowerCase('es').includes(texto) ? '' : 'none';
    });
}

document.addEventListener('click', evento => {
    const botonCarrito = evento.target.closest('[data-agregar-carrito]');
    if (botonCarrito) {
        evento.preventDefault();
        agregarAlCarrito({
            id: botonCarrito.dataset.id,
            nombre: botonCarrito.dataset.nombre,
            precio: botonCarrito.dataset.precio,
            imagen: botonCarrito.dataset.imagen
        });
        return;
    }

    const accion = evento.target.closest('[data-cart-action]');
    if (!accion) return;

    const id = accion.dataset.id;
    if (accion.dataset.cartAction === 'menos') cambiarCantidad(id, -1);
    if (accion.dataset.cartAction === 'mas') cambiarCantidad(id, 1);
    if (accion.dataset.cartAction === 'eliminar') quitarDelCarrito(id);
});

document.addEventListener('DOMContentLoaded', () => {
    actualizarContador();
    dibujarCarrito();

    const buscador = document.getElementById('buscador-productos');
    buscador?.addEventListener('input', filtrarProductos);
});
