package com.example.greenbyte.repositorio;

import com.example.greenbyte.models.Venta;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    Optional<Venta> findByCodigoCompraAndClienteId(String codigo, Long clienteId);

    @EntityGraph(attributePaths = "cliente")
    List<Venta> findAllByOrderByIdDesc();

    @EntityGraph(attributePaths = {"cliente", "detalles"})
    Optional<Venta> findOneById(Long id);
}
