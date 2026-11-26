const pestanasManual = Array.from(document.querySelectorAll('.manual-tabs [role="tab"]'));

function mostrarPestanaDelEnlace() {
    const destino = window.location.hash || '#fichas';
    const pestana = pestanasManual.find(boton => boton.dataset.bsTarget === destino);
    if (pestana) bootstrap.Tab.getOrCreateInstance(pestana).show();
}

for (const pestana of pestanasManual) {
    pestana.addEventListener('shown.bs.tab', () => {
        if (window.location.hash !== pestana.dataset.bsTarget) {
            window.history.replaceState(null, '', pestana.dataset.bsTarget);
        }
    });
}

window.addEventListener('hashchange', mostrarPestanaDelEnlace);
mostrarPestanaDelEnlace();
