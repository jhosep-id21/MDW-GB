package com.example.demo.service;

import com.example.demo.model.Producto;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductoService {

    private final List<Producto> productos = new ArrayList<>();

    public ProductoService() {
        // INTERIOR - 8 productos
        productos.add(new Producto("int-1", "Violeta Africana", "Planta compacta de flores violetas que alegra cualquier espacio. Prefiere luz indirecta y riego moderado.", 39.90, "/plantas/interior/planta-int-1.webp", "interior", "Violeta Africana"));
        productos.add(new Producto("int-2", "Aloe Vera", "Suculenta resistente de hojas carnosas. Necesita mucha luz y poco riego; deja secar la tierra entre riegos.", 29.90, "/plantas/interior/planta-int-2.webp", "interior", "Aloe Vera"));
        productos.add(new Producto("int-3", "Menta", "Hierba aromática de hojas frescas, ideal para infusiones y cocina. Crece mejor con buena luz y tierra húmeda.", 19.90, "/plantas/interior/planta-int-3.webp", "interior", "Menta"));
        productos.add(new Producto("int-4", "Peperomia Verde", "Planta ornamental de hojas verdes y brillantes, perfecta para interiores. Prefiere luz indirecta.", 34.90, "/plantas/interior/planta-int-4.webp", "interior", "Peperomia Verde"));
        productos.add(new Producto("int-5", "Monstera Deliciosa", "La reina de interiores. Hojas grandes fenestradas, ideal para luz indirecta brillante. Muy decorativa.", 89.90, "/plantas/interior/planta-int-1.webp", "interior", "Monstera Deliciosa"));
        productos.add(new Producto("int-6", "Sansevieria Trifasciata", "Lengua de suegra, purificadora de aire y casi indestructible. Tolera poca luz y riego escaso.", 45.90, "/plantas/interior/planta-int-2.webp", "interior", "Sansevieria"));
        productos.add(new Producto("int-7", "Pothos Dorado", "Enredadera colgante de fácil cuidado. Perfecta para estanterías, tolera semisombra.", 24.90, "/plantas/interior/planta-int-3.webp", "interior", "Pothos"));
        productos.add(new Producto("int-8", "Ficus Lyrata", "Ficus de hoja de violín, elegante y escultural. Luz brillante indirecta y riego moderado.", 95.90, "/plantas/interior/planta-int-4.webp", "interior", "Ficus Lyrata"));

        // EXTERIOR - 8 productos
        productos.add(new Producto("ext-1", "Geranio", "Planta florífera de colores vivos, ideal para balcones y jardines soleados.", 49.90, "/plantas/exterior/planta-ext-1.webp", "exterior", "Geranio"));
        productos.add(new Producto("ext-2", "Hiedra Inglesa", "Enredadera de follaje frondoso que cubre muros y pérgolas. Semisombra.", 59.90, "/plantas/exterior/planta-ext-2.webp", "exterior", "Hiedra Inglesa"));
        productos.add(new Producto("ext-3", "Hortensia", "Arbusto ornamental de grandes flores. Prefiere lugares frescos con semisombra.", 79.90, "/plantas/exterior/planta-ext-3.webp", "exterior", "Hortensia"));
        productos.add(new Producto("ext-4", "Sheflera", "Arbusto de hojas en forma de paraguas, adecuado para patios protegidos.", 99.90, "/plantas/exterior/planta-ext-4.webp", "exterior", "Sheflera"));
        productos.add(new Producto("ext-5", "Lavanda", "Aromática de flores moradas, ideal para exteriores soleados. Atrae polinizadores.", 35.90, "/plantas/exterior/planta-ext-1.webp", "exterior", "Lavanda"));
        productos.add(new Producto("ext-6", "Buganvilla", "Trepadora espectacular de flores fucsias. Pleno sol y riego moderado.", 69.90, "/plantas/exterior/planta-ext-2.webp", "exterior", "Buganvilla"));
        productos.add(new Producto("ext-7", "Helecho Boston", "Follaje colgante frondoso para terrazas sombreadas. Mantener humedad.", 42.90, "/plantas/exterior/planta-ext-3.webp", "exterior", "Helecho"));
        productos.add(new Producto("ext-8", "Rosal Mini", "Rosal compacto de floración continua. Sol directo y poda regular.", 55.90, "/plantas/exterior/planta-ext-4.webp", "exterior", "Rosal Mini"));

        // MACETAS - 8 productos
        productos.add(new Producto("mac-1", "Maceta Cerámica Blanca Minimalista", "Cerámica esmaltada 16cm, ideal para interiores modernos. Con drenaje.", 45.90, "/img-carousel/maceta-1.jpg", "macetas", "Maceta Cerámica Blanca"));
        productos.add(new Producto("mac-2", "Maceta Terracota Rústica", "Barro natural 20cm, respirable y perfecta para suculentas.", 32.90, "/img-carousel/maceta-2.jpg", "macetas", "Maceta Terracota"));
        productos.add(new Producto("mac-3", "Maceta Colgante Yute Trenzado", "Soporte colgante de yute + maceta plástica 14cm, estilo boho.", 55.00, "/img-carousel/maceta-3.jpg", "macetas", "Maceta Colgante Yute"));
        productos.add(new Producto("mac-4", "Maceta Cemento Geométrica", "Cemento pulido 18cm, diseño hexagonal. Muy resistente.", 49.90, "/img-carousel/maceta-4.jpg", "macetas", "Maceta Cemento"));
        productos.add(new Producto("mac-5", "Set 3 Macetas Pastel", "Pack de 3 macetas plásticas 12/14/16cm en tonos pastel.", 39.90, "/img-carousel/maceta-1.jpg", "macetas", "Set Macetas Pastel"));
        productos.add(new Producto("mac-6", "Maceta Autorriego 24cm", "Sistema de reserva de agua, ideal para vacaciones. Blanca.", 69.90, "/img-carousel/maceta-2.jpg", "macetas", "Maceta Autorriego"));
        productos.add(new Producto("mac-7", "Macetero Madera Rectangular", "Madera tratada 40cm para balcón, con plástico interior.", 89.90, "/img-carousel/maceta-3.jpg", "macetas", "Macetero Madera"));
        productos.add(new Producto("mac-8", "Maceta Vidrio Terrario 15cm", "Vidrio soplado para terrarios y suculentas. Sin drenaje.", 28.90, "/img-carousel/maceta-4.jpg", "macetas", "Maceta Vidrio"));

        // ACCESORIOS - 8 productos
        productos.add(new Producto("acc-1", "Tijera de Poda Ergonómica", "Acero inoxidable con bloqueo de seguridad. Corte limpio 20mm.", 35.90, "/img-carousel/accesorios-1.png", "accesorios", "Tijera Poda"));
        productos.add(new Producto("acc-2", "Kit Herramientas Jardín 3 Piezas", "Pala, rastrillo y trasplantador con mango de madera.", 65.90, "/img-carousel/accesorios-2.png", "accesorios", "Kit Herramientas"));
        productos.add(new Producto("acc-3", "Sustrato Premium 5L", "Turba, perlita y humus. Listo para transplantar.", 25.90, "/img-carousel/accesorios-3.png", "accesorios", "Sustrato 5L"));
        productos.add(new Producto("acc-4", "Fertilizante Orgánico Líquido 500ml", "NPK orgánico para todo tipo de plantas. Uso quincenal.", 22.90, "/img-carousel/accesorios-1.png", "accesorios", "Fertilizante"));
        productos.add(new Producto("acc-5", "Regadera 2L Verde Mentol", "Plástico reciclado con rociador fino, diseño nórdico.", 29.90, "/img-carousel/accesorios-2.png", "accesorios", "Regadera 2L"));
        productos.add(new Producto("acc-6", "Guantes Jardinería Antideslizantes", "Talla M, palma engomada y transpirables.", 18.90, "/img-carousel/accesorios-3.png", "accesorios", "Guantes"));
        productos.add(new Producto("acc-7", "Pulverizador 1L Presurizado", "Pulverización fina para riego foliar y limpieza.", 15.90, "/img-carousel/accesorios-1.png", "accesorios", "Pulverizador"));
        productos.add(new Producto("acc-8", "Tutor Bambú Pack x6 (60cm)", "Soporte natural para plantas trepadoras y hortalizas.", 12.90, "/img-carousel/accesorios-2.png", "accesorios", "Tutores Bambú"));

        // PRINCIPIANTES - 8 productos / kits
        productos.add(new Producto("prin-1", "Kit Iniciación Suculentas x3", "3 suculentas variadas + 3 macetitas blancas + sustrato. ¡Para empezar sin fallar!", 49.90, "/plantas/interior/planta-int-2.webp", "principiantes", "Kit Suculentas"));
        productos.add(new Producto("prin-2", "Kit Hierbas Aromáticas", "Menta, albahaca y cilantro en macetas biodegradables + guía PDF.", 39.90, "/plantas/interior/planta-int-3.webp", "principiantes", "Kit Aromáticas"));
        productos.add(new Producto("prin-3", "Monstera + Maceta Blanca (Combo)", "Monstera joven + maceta cerámica 16cm. Regalo ideal.", 69.90, "/plantas/interior/planta-int-1.webp", "principiantes", "Combo Monstera"));
        productos.add(new Producto("prin-4", "Pack Principiante: Pothos + Sansevieria", "Dos plantas indestructibles para interiores con poca luz.", 59.90, "/plantas/interior/planta-int-4.webp", "principiantes", "Pack Principiante"));
        productos.add(new Producto("prin-5", "Terrario Cerrado Fácil", "Vidrio 20cm con fitonia y musgo. Ecosistema autosuficiente, riego mínimo.", 55.90, "/img-carousel/maceta-4.jpg", "principiantes", "Terrario Fácil"));
        productos.add(new Producto("prin-6", "Kit Siembra Completo", "Semillas, sustrato, macetitas y guía paso a paso ilustrada.", 29.90, "/img-carousel/accesorios-3.png", "principiantes", "Kit Siembra"));
        productos.add(new Producto("prin-7", "Caja Regalo GreenByte Starter", "Planta sorpresa + accesorio + tarjeta de cuidados + fertilizante.", 79.90, "/img-carousel/accesorios-2.png", "principiantes", "Caja Regalo"));
        productos.add(new Producto("prin-8", "Suscripción Guía Digital + Planta Mes", "Recibe cada mes una planta fácil + tips y calendario de riego.", 34.90, "/plantas/exterior/planta-ext-1.webp", "principiantes", "Suscripción"));
    }

    public List<Producto> findAll() {
        return new ArrayList<>(productos);
    }

    public List<Producto> findByCategoria(String categoria) {
        return productos.stream()
                .filter(p -> p.getCategoria().equalsIgnoreCase(categoria))
                .collect(Collectors.toList());
    }

    public Optional<Producto> findById(String id) {
        return productos.stream().filter(p -> p.getId().equals(id)).findFirst();
    }

    public void addProducto(Producto p) {
        // genera id si no tiene
        if (p.getId() == null || p.getId().isBlank()) {
            p.setId(p.getCategoria().substring(0,3) + "-" + (productos.size()+1));
        }
        productos.add(p);
    }

    public boolean removeById(String id) {
        return productos.removeIf(p -> p.getId().equals(id));
    }

    public long count() { return productos.size(); }
    public long countByCategoria(String cat) { return findByCategoria(cat).size(); }
}
