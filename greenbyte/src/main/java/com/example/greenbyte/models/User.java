package com.example.greenbyte.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String telefono;

    // Nivel de acceso del usuario (A: Admin, U: Usuario)
    @Column(nullable = false)
    private char acceso;

    @Column(unique = true, nullable = false)
    private String correo;

    @Column(nullable = false)
    private String clave;
}
