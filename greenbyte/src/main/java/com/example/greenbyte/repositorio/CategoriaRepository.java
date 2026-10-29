package com.example.greenbyte.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.greenbyte.models.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
}
