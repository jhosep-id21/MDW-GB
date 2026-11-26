package com.example.greenbyte.repositorio;

import com.example.greenbyte.models.FichaPlanta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FichaPlantaRepository extends JpaRepository<FichaPlanta, Long> {
    List<FichaPlanta> findAllByOrderByNombreAsc();

    List<FichaPlanta> findByActivaTrueOrderByNombreAsc();
}
