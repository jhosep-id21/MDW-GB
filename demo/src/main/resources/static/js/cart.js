/* GreenByte Carrito - localStorage */
const CART_KEY = 'greenbyte_cart';

function getCart() {
    try { return JSON.parse(localStorage.getItem(CART_KEY)) || []; } catch { return []; }
}
function saveCart(cart) {
    localStorage.setItem(CART_KEY, JSON.stringify(cart));
    updateBadge();
    renderCart();
}
function updateBadge() {
    const cart = getCart();
    const count = cart.reduce((s, i) => s + i.qty, 0);
    const badge = document.getElementById('cart-count');
    if (badge) {
        badge.textContent = count;
        badge.style.display = count > 0 ? 'inline' : 'none';
    }
}
function showToast(msg) {
    const el = document.getElementById('cartToast');
    const msgEl = document.getElementById('cartToastMsg');
    if (!el || !msgEl) { alert(msg); return; }
    msgEl.textContent = msg;
    const toast = new bootstrap.Toast(el, { delay: 2000 });
    toast.show();
}
function addToCart(product) {
    const cart = getCart();
    const existing = cart.find(p => p.id === product.id);
    if (existing) {
        existing.qty += 1;
    } else {
        cart.push({ ...product, qty: 1 });
    }
    saveCart(cart);
    showToast(product.nombre + " agregado al carrito");
}
function removeFromCart(id) {
    let cart = getCart();
    cart = cart.filter(p => p.id !== id);
    saveCart(cart);
}
function changeQty(id, delta) {
    const cart = getCart();
    const item = cart.find(p => p.id === id);
    if (!item) return;
    item.qty += delta;
    if (item.qty <= 0) {
        saveCart(cart.filter(p => p.id !== id));
    } else {
        saveCart(cart);
    }
}
function clearCart() {
    if (!confirm('¿Vaciar carrito?')) return;
    localStorage.removeItem(CART_KEY);
    updateBadge();
    renderCart();
}
function checkout() {
    const cart = getCart();
    if (cart.length === 0) return;
    const total = cart.reduce((s, p) => s + p.precio * p.qty, 0).toFixed(2);
    const resumen = cart.map(p => `${p.qty}x ${p.nombre} (S/.${p.precio})`).join('\n');
    alert(`¡Gracias por tu compra!\n\n${resumen}\n\nTotal: S/. ${total}\n\nDemo: pedido simulado, carrito se vaciará.`);
    localStorage.removeItem(CART_KEY);
    updateBadge();
    renderCart();
    const offcanvas = bootstrap.Offcanvas.getInstance(document.getElementById('offcanvasCart'));
    if (offcanvas) offcanvas.hide();
}

function renderCart() {
    const container = document.getElementById('cart-items');
    const empty = document.getElementById('cart-empty');
    const footer = document.getElementById('cart-footer');
    const totalEl = document.getElementById('cart-total');
    if (!container) return;
    const cart = getCart();

    // limpiar items previos (excepto empty placeholder)
    container.querySelectorAll('.cart-item').forEach(e => e.remove());

    if (cart.length === 0) {
        if (empty) empty.classList.remove('d-none');
        if (footer) footer.classList.add('d-none');
        return;
    }
    if (empty) empty.classList.add('d-none');
    if (footer) footer.classList.remove('d-none');

    let total = 0;
    cart.forEach(item => {
        total += item.precio * item.qty;
        const div = document.createElement('div');
        div.className = 'cart-item d-flex gap-2 align-items-center border-bottom border-secondary py-2';
        div.innerHTML = `
            <img src="${item.imagen}" alt="${item.nombre}" style="width:56px;height:56px;object-fit:cover;border-radius:8px;" onerror="this.src='/img-carousel/maceta-1.jpg'">
            <div class="flex-grow-1">
                <div class="fw-semibold small" style="line-height:1.2">${item.nombre}</div>
                <div class="small text-success">S/. ${item.precio.toFixed(2)} c/u</div>
                <div class="d-flex align-items-center gap-1 mt-1">
                    <button class="btn btn-sm btn-outline-light py-0 px-2" onclick="changeQty('${item.id}', -1)">−</button>
                    <span class="badge bg-light text-dark">${item.qty}</span>
                    <button class="btn btn-sm btn-outline-light py-0 px-2" onclick="changeQty('${item.id}', 1)">+</button>
                    <span class="ms-2 small">S/. ${(item.precio * item.qty).toFixed(2)}</span>
                </div>
            </div>
            <button class="btn btn-sm btn-outline-danger" onclick="removeFromCart('${item.id}')" title="Eliminar"><i class="bi bi-trash"></i></button>
        `;
        container.appendChild(div);
    });
    if (totalEl) totalEl.textContent = 'S/. ' + total.toFixed(2);
}

// Búsqueda en cards
function filtrarProductos() {
    const q = (document.getElementById('searchInput')?.value || '').toLowerCase().trim();
    if (!q) {
        document.querySelectorAll('.product-card').forEach(c => c.style.display = '');
        return;
    }
    document.querySelectorAll('.product-card').forEach(card => {
        const text = card.innerText.toLowerCase();
        card.style.display = text.includes(q) ? '' : 'none';
    });
}

// Delegación para botones "Agregar"
document.addEventListener('click', (e) => {
    const btn = e.target.closest('[data-add-to-cart]');
    if (!btn) return;
    e.preventDefault();
    const product = {
        id: btn.dataset.id,
        nombre: btn.dataset.nombre,
        precio: parseFloat(btn.dataset.precio),
        imagen: btn.dataset.imagen
    };
    if (!product.id || isNaN(product.precio)) {
        console.error('Datos incompletos', btn.dataset);
        return;
    }
    addToCart(product);
});

document.addEventListener('DOMContentLoaded', () => {
    updateBadge();
    renderCart();
    const search = document.getElementById('searchInput');
    if (search) search.addEventListener('input', filtrarProductos);
});
