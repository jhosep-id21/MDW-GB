package com.example.greenbyte.config;

import com.example.greenbyte.models.FichaPlanta;
import com.example.greenbyte.repositorio.FichaPlantaRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class FichasIniciales {
    @Bean
    ApplicationRunner cargarFichasIniciales(FichaPlantaRepository fichas) {
        return args -> {
            if (fichas.count() == 0) {
                fichas.saveAll(List.of(
                        ficha("Monstera Deliciosa", "Interior", "Fenestrada, tropical. Muy popular.", "Brillante indirecta", "Cada 7-10 días, sustrato seco 2cm", "Temperatura: 18-27°C. Toxicidad: Sí (mascotas)."),
                        ficha("Sansevieria", "Interior", "Lengua de suegra, indestructible.", "Baja a brillante", "Cada 14-21 días", "Temperatura: 15-30°C. Toxicidad: Leve."),
                        ficha("Aloe Vera", "Suculenta", "Medicinal y resistente.", "Sol directo / brillante", "Cada 12-18 días", "Temperatura: 18-28°C. Uso: Gel hidratante."),
                        ficha("Geranio", "Exterior", "Balcones a pleno sol.", "Sol directo 6h+", "Cada 3-5 días en verano", "Temperatura: 15-25°C. Floración: Primavera-verano."),
                        ficha("Hortensia", "Exterior", "Flores grandes, suelo ácido.", "Semisombra", "Diario en verano, abundante", "Temperatura: 14-22°C. Color: pH dependiente."),
                        ficha("Menta", "Aromática", "Culinaria, invasiva.", "Brillante / sol suave", "Cada 2-4 días, siempre húmedo", "Poda: Frecuente para ramificar. Uso: Infusiones."),
                        ficha("Pothos Dorado", "Colgante", "Ideal para principiantes, purifica.", "Baja a media", "Cada 7-12 días", "Temperatura: 17-30°C. Propagación: Esqueje en agua."),
                        ficha("Lavanda", "Exterior", "Aromática, atrae polinizadores.", "Sol pleno 8h", "Cada 10-15 días, muy drenado", "Temperatura: Tolera heladas leves. Plagas: Muy resistente.")
                ));
            }
        };
    }

    private FichaPlanta ficha(String nombre, String tipo, String descripcion, String luz,
                             String riego, String notas) {
        FichaPlanta ficha = new FichaPlanta();
        ficha.setNombre(nombre);
        ficha.setTipo(tipo);
        ficha.setDescripcion(descripcion);
        ficha.setLuz(luz);
        ficha.setRiego(riego);
        ficha.setNotas(notas);
        return ficha;
    }
}
