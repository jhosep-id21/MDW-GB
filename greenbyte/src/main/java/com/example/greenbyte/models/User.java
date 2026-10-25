package com.example.greenbyte.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre no puede estar vacío")
    private String nombre;

    @NotEmpty(message = "El teléfono no puede estar vacío")
    private String telefono;

    // Nivel de acceso del usuario (A: Admin, U: Usuario)
    @NotNull (message = "El nivel de acceso no puede estar vacío")
    @Column
    private char acceso;

    @Email(message = "El correo debe ser válido")
    @NotEmpty(message = "El correo no puede estar vacío")
    @Column(unique = true)
    private String correo;

    @NotEmpty(message = "La clave no puede estar vacía")
    @Column(nullable = false)
    private String clave;
}
