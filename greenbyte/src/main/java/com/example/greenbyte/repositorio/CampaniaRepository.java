package com.example.greenbyte.repositorio;

import com.example.greenbyte.models.Campania;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CampaniaRepository extends JpaRepository<Campania, Long> {

    List<Campania> findAllByOrderByInicioDescIdDesc();
}
