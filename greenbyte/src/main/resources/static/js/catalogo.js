const filtro = document.getElementById('filtro-categoria');
const orden = document.getElementById('orden-productos');
const rejilla = document.getElementById('rejilla-productos');
const buscador = document.getElementById('buscador-productos');
const cantidad = document.getElementById('cantidad-productos');
const sinCoincidencias = document.getElementById('sin-coincidencias');

function normalizar(texto) {
    return texto.toLocaleLowerCase('es').normalize('NFD').replace(/\p{Diacritic}/gu, '').trim();
}

function claveCategoria(nombre) {
    return normalizar(nombre).replace(/^(?:plantas? (?:de|para)|para)\s+/, '');
}

const tarjetas = Array.from(document.querySelectorAll('[data-categoria]'), tarjeta => ({
    elemento: tarjeta,
    categoria: claveCategoria(tarjeta.dataset.categoria),
    texto: normalizar(tarjeta.textContent),
    precio: Number(tarjeta.dataset.precio),
    stock: Number(tarjeta.dataset.stock)
}));

const categorias = new Set();
for (const opcion of Array.from(filtro.options)) {
    const clave = claveCategoria(opcion.value);
    if (categorias.has(clave)) {
        opcion.remove();
    } else {
        opcion.value = clave;
        categorias.add(clave);
    }
}
for (const { elemento, categoria } of tarjetas) {
    if (!categorias.has(categoria)) {
        filtro.add(new Option(elemento.dataset.categoria, categoria));
        categorias.add(categoria);
    }
}

const criteriosOrden = new Map([
    ['precio-asc', (a, b) => a.precio - b.precio],
    ['precio-desc', (a, b) => b.precio - a.precio],
    ['stock-desc', (a, b) => b.stock - a.stock]
]);

function ordenarProductos() {
    if (!rejilla) return;

    const comparar = criteriosOrden.get(orden.value);
    const ordenadas = comparar ? [...tarjetas].sort(comparar) : tarjetas;
    rejilla.append(...ordenadas.map(tarjeta => tarjeta.elemento));
}

function aplicarFiltros() {
    const texto = normalizar(buscador.value);
    let visibles = 0;
    for (const tarjeta of tarjetas) {
        const coincide = (!filtro.value || tarjeta.categoria === filtro.value) && tarjeta.texto.includes(texto);
        tarjeta.elemento.hidden = !coincide;
        if (coincide) visibles++;
    }
    cantidad.textContent = visibles === 1 ? '1 producto' : visibles + ' productos';
    sinCoincidencias.hidden = visibles > 0 || tarjetas.length === 0;
}

function cargarFiltros() {
    const parametros = new URLSearchParams(window.location.search);
    const categoria = claveCategoria(parametros.get('categoria') || '');
    filtro.value = categorias.has(categoria) ? categoria : '';
    buscador.value = parametros.get('q') || '';
    const criterio = parametros.get('orden');
    orden.value = criteriosOrden.has(criterio) ? criterio : '';
    ordenarProductos();
    aplicarFiltros();
}

function guardarFiltros() {
    const url = new URL(window.location.href);
    for (const [clave, valor] of [['categoria', filtro.value], ['q', buscador.value.trim()], ['orden', orden.value]]) {
        if (valor) url.searchParams.set(clave, valor);
        else url.searchParams.delete(clave);
    }
    window.history.replaceState(null, '', url);
    aplicarFiltros();
}

document.getElementById('filtro-catalogo').hidden = false;
document.getElementById('orden-catalogo').hidden = false;
filtro.addEventListener('change', guardarFiltros);
orden.addEventListener('change', () => {
    ordenarProductos();
    guardarFiltros();
});
buscador.addEventListener('input', guardarFiltros);
buscador.form.addEventListener('submit', evento => {
    evento.preventDefault();
    guardarFiltros();
});
window.addEventListener('popstate', cargarFiltros);
cargarFiltros();
