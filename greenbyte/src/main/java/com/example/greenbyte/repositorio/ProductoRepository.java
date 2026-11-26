package com.example.greenbyte.repositorio;

import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenbyte.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findFirst4ByStockGreaterThanOrderByIdAsc(int stock);

    List<Producto> findByCategoriaNombre(String nombre);
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Producto p where p.id = :id")
    Optional<Producto> buscarParaVenta(@Param("id") Long id);
}
