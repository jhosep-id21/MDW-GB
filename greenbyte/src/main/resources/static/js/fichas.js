document.getElementById('filtro-fichas')?.addEventListener('input', e => {
    const textoBuscado = e.target.value.toLowerCase();
    document.querySelectorAll('#rejilla-fichas > div').forEach(c => {
        c.style.display = c.textContent.toLowerCase().includes(textoBuscado) ? '' : 'none';
    });
});
