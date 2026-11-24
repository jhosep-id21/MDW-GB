document.addEventListener("DOMContentLoaded", function () {
  // Mostrar modal de crear producto solo si data-abrir-modal-crear="true"
  var elCrear = document.getElementById('modal-crear-producto');
  if (elCrear && elCrear.dataset.abrirModalCrear === 'true') {
    var modalCrear = new bootstrap.Modal(elCrear);
    modalCrear.show();
  }

  // Mostrar modal de editar producto solo si data-editando="true"
  var elEditar = document.getElementById('modal-editar-producto');
  if (elEditar && elEditar.dataset.editando === 'true') {
    var modalEditar = new bootstrap.Modal(elEditar);
    modalEditar.show();

    elEditar.addEventListener('hidden.bs.modal', function () {
      window.location.href = '/administrador';
    });
  }
});
