package com.example.greenbyte.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class FichaPlanta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max = 80)
    @Column(nullable = false, length = 80)
    private String nombre;

    @NotBlank
    @Size(max = 50)
    @Column(nullable = false, length = 50)
    private String tipo;

    @NotBlank
    @Size(max = 240)
    @Column(nullable = false, length = 240)
    private String descripcion;

    @NotBlank
    @Size(max = 120)
    @Column(nullable = false, length = 120)
    private String luz;

    @NotBlank
    @Size(max = 160)
    @Column(nullable = false, length = 160)
    private String riego;

    @NotBlank
    @Size(max = 240)
    @Column(nullable = false, length = 240)
    private String notas;

    @Column(nullable = false)
    private boolean activa = true;
}
