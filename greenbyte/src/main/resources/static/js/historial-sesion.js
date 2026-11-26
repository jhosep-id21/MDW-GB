window.addEventListener('pagehide', evento => {
    if (evento.persisted) {
        document.documentElement.style.visibility = 'hidden';
    }
});

window.addEventListener('pageshow', evento => {
    const entrada = performance.getEntriesByType('navigation')[0];
    if (evento.persisted || entrada?.type === 'back_forward') {
        window.location.reload();
    }
});
