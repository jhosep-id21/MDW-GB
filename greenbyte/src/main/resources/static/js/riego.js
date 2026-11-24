function calcularRiego() {
    const tipoPlanta = document.getElementById('riego-planta').value;
    const tamanoMaceta = document.getElementById('riego-maceta').value;
    const estacion = document.getElementById('riego-estacion').value;
    const luz = document.getElementById('riego-luz').value;
    let diasBase = { tropical: 7, suculenta: 14, helecho: 4, aromatica: 5, flor: 4 }[tipoPlanta] || 7;
    if (estacion === 'invierno') diasBase += 4;
    if (luz === 'alta') diasBase -= 2;
    if (luz === 'baja') diasBase += 2;
    if (tamanoMaceta === 's') diasBase -= 1;
    if (tamanoMaceta === 'l') diasBase += 2;
    if (diasBase < 2) diasBase = 2;
    if (diasBase > 21) diasBase = 21;
    const mililitros = { s: 120, m: 250, l: 400 }[tamanoMaceta];
    const nombre = { tropical: 'Tropical', suculenta: 'Suculenta', helecho: 'Helecho', aromatica: 'Aromática', flor: 'Flor' }[tipoPlanta];
    const resultado = document.getElementById('resultado-riego');
    resultado.classList.remove('d-none');
    resultado.innerHTML = `<b>${nombre}</b> — Riega cada <span class="text-success fs-5">${diasBase} días</span> aprox. con <b>${mililitros} ml</b> por riego (hasta que drene).<br><span class="small text-muted">Próximo riego: ${new Date(Date.now() + diasBase * 86400000).toLocaleDateString('es-PE', { weekday: 'long', day: 'numeric', month: 'short' })}</span>`;
}
