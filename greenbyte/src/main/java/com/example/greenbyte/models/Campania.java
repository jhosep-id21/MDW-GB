package com.example.greenbyte.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Getter
@Setter
public class Campania {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Escribe un título para la campaña")
    @Size(max = 80, message = "El título puede tener hasta 80 caracteres")
    @Column(nullable = false, length = 80)
    private String titulo;

    @NotBlank(message = "Escribe un mensaje para la campaña")
    @Size(max = 240, message = "El mensaje puede tener hasta 240 caracteres")
    @Column(nullable = false, length = 240)
    private String mensaje;

    @NotNull(message = "Selecciona la fecha de inicio")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate inicio;

    @NotNull(message = "Selecciona la fecha de fin")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(nullable = false)
    private LocalDate fin;

    @ManyToOne
    private Categoria categoria;

    @Column(nullable = false)
    private boolean activa = true;
}
