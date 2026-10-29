package com.example.greenbyte.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenbyte.models.Producto;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByCategoriaNombre(String nombre);
}
