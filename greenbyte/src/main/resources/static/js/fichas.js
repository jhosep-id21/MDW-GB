document.getElementById('filtro-fichas')?.addEventListener('input', e => {
    const textoBuscado = e.target.value.toLowerCase();
    document.querySelectorAll('.tarjeta-tema').forEach(c => {
        c.style.display = c.innerText.toLowerCase().includes(textoBuscado) ? '' : 'none';
    });
});
