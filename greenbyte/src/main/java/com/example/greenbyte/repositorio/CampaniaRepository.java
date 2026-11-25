package com.example.greenbyte.repositorio;

import com.example.greenbyte.models.Campania;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CampaniaRepository extends JpaRepository<Campania, Long> {

    List<Campania> findAllByOrderByInicioDescIdDesc();

    Optional<Campania> findFirstByActivaTrueAndInicioLessThanEqualAndFinGreaterThanEqualOrderByInicioDescIdDesc(
            LocalDate inicio, LocalDate fin);
}
