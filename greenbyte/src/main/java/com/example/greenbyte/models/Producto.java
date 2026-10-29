package com.example.greenbyte.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "El nombre es obligatorio")
    @Column(nullable = false)
    private String nombre;

    @NotEmpty(message = "La descripción es obligatoria")
    @Column(nullable = false, length = 1000)
    private String descripcion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "El precio debe ser mayor a 0")
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal precio;

    @ManyToOne
    @JoinColumn(name = "idCategoria")
    @JsonIgnoreProperties("productos")
    private Categoria categoria;

    @Column(nullable = true, length = 1000)
    private String imagen;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    @Column(nullable = false)
    private Integer stock;

    public Producto() {
    }

    public String getNombreCategoria() {
        return categoria != null ? categoria.getNombre() : "Sin categoría";
    }

    public String getRutaImagen() {
        if (imagen == null || imagen.isBlank()) {
            return "/img-carousel/imagen-plantas.jpg";
        }
        String img = imagen.trim();
        if (img.startsWith("http://") || img.startsWith("https://") || img.startsWith("/")) {
            return img;
        }
        return "/" + img;
    }
}
