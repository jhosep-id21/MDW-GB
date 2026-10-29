package com.example.greenbyte.repositorio;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.greenbyte.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByCorreo(String correo);
}
