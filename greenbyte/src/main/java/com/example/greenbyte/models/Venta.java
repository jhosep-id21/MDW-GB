package com.example.greenbyte.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Venta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User cliente;

    @Column(nullable = false, unique = true, length = 36)
    private String codigoCompra;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, precision = 18, scale = 2)
    private BigDecimal total;

    @OneToMany(mappedBy = "venta", cascade = CascadeType.PERSIST)
    @OrderBy("id")
    private List<DetalleVenta> detalles = new ArrayList<>();
}
