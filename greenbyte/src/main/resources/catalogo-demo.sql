-- Datos de demostración recuperados de las antiguas vistas del catálogo.
-- Ejecutar después del primer arranque de la aplicación. Stock inicial de muestra: 10.
USE greenbyte;
START TRANSACTION;

INSERT INTO categoria (nombre)
SELECT 'Plantas de Interior'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Plantas de Interior');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Violeta Africana', 'Planta compacta de flores violetas. Prefiere luz indirecta y riego moderado.', 39.90, '/plantas/interior/planta-int-1.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Violeta Africana');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Aloe Vera', 'Suculenta resistente. Mucha luz y poco riego; deja secar la tierra.', 29.90, '/plantas/interior/planta-int-2.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Aloe Vera');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Menta', 'Hierba aromática ideal para infusiones. Buena luz y tierra húmeda.', 19.90, 'https://i0.wp.com/viveroplantas.com/wp-content/uploads/2026/05/menta-hero-usos-casa-1a492c0b-eab1-4904-a79a-298fcb153a89.jpg?fit=1024%2C1024&ssl=1', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Menta');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Peperomia Verde', 'Hojas verdes brillantes, perfecta para interiores con luz indirecta.', 34.90, '/plantas/interior/planta-int-4.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Peperomia Verde');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Monstera Deliciosa', 'Hojas fenestradas, luz indirecta brillante. Muy decorativa.', 89.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcR7k-ZXb1fOobL6PXqygCakMk7xfjUpBBsuKB-l9lGUqb0unmhn0rzCnhQ&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Monstera Deliciosa');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Sansevieria Trifasciata', 'Purificadora y casi indestructible. Tolera poca luz y riego escaso.', 45.90, 'https://static.wixstatic.com/media/b4df8d_602e2b8e6f9c4928a5aa45754971a898~mv2.jpg/v1/fill/w_560,h_746,al_c,q_85,usm_0.66_1.00_0.01,enc_avif,quality_auto/b4df8d_602e2b8e6f9c4928a5aa45754971a898~mv2.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Sansevieria Trifasciata');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Pothos Dorado', 'Enredadera colgante de fácil cuidado. Ideal para estanterías.', 24.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQAuftEjjW1jiJxE1Syf5JgHTRFVzrBroAX6VgsdHMZiUqTJD3m8ACdr319&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Pothos Dorado');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Ficus Lyrata', 'Elegante y escultural. Luz brillante indirecta y riego moderado.', 95.90, 'https://pepinplantcare.com/cdn/shop/articles/Design-sans-titre_9acbdcbd-87f5-43d6-b0d2-3ed0bc2ea79a.webp?v=1725023116&width=800', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Interior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Ficus Lyrata');

INSERT INTO categoria (nombre)
SELECT 'Plantas de Exterior'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Plantas de Exterior');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Geranio', 'Florífera de colores vivos, ideal para balcones soleados.', 49.90, '/plantas/exterior/planta-ext-1.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Geranio');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Hiedra Inglesa', 'Enredadera frondosa que cubre muros y pérgolas.', 59.90, '/plantas/exterior/planta-ext-2.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Hiedra Inglesa');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Hortensia', 'Arbusto de grandes flores. Prefiere semisombra y riego frecuente.', 79.90, '/plantas/exterior/planta-ext-3.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Hortensia');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Sheflera', 'Hojas en forma de paraguas, ideal para patios protegidos.', 99.90, '/plantas/exterior/planta-ext-4.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Sheflera');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Lavanda', 'Aromática de flores moradas, pleno sol y poco riego.', 35.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTEMH96sNRtyWEZlPaNQz8ihyLF5_IQf2_OfWrgDRnsnR0rSt5n6yBR4rLK&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Lavanda');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Buganvilla', 'Trepadora fucsia de pleno sol, floración espectacular.', 69.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSikP_ePOxC-iIdLLE09YIvRKdDKE8ryhT92yMmf59qbQLLb6b-ifg97wFR&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Buganvilla');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Helecho Boston', 'Follaje colgante para terrazas sombreadas.', 42.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSaECU0Q3DMNzc1wzk9gP53ouyTttgbiJbIhJaL51Ov4FZhIQINekuDK28&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Helecho Boston');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Rosal Mini', 'Rosal compacto de floración continua, sol directo.', 55.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTj47RuZlpKPyx001AlUzsfOVaE5XeaJki1bBl1VJeAQ4at73HfBJe-CT0&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Plantas de Exterior')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Rosal Mini');

INSERT INTO categoria (nombre)
SELECT 'Macetas'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Macetas');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Cerámica Blanca Minimalista', 'Cerámica esmaltada 16cm, ideal para interiores. Con drenaje.', 45.90, '/img-carousel/maceta-1.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Cerámica Blanca Minimalista');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Terracota Rústica', 'Barro natural 20cm, respirable para suculentas.', 32.90, '/img-carousel/maceta-2.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Terracota Rústica');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Colgante Yute Trenzado', 'Soporte yute + maceta 14cm, estilo boho.', 55.00, '/img-carousel/maceta-4.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Colgante Yute Trenzado');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Cemento Geométrica', 'Cemento pulido 18cm, diseño hexagonal.', 49.90, '/img-carousel/maceta-3.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Cemento Geométrica');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Set 3 Macetas Pastel', 'Pack 12/14/16cm en tonos pastel.', 39.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRLa9Uh2HZVwNxDXhdRPJZR0bry_CCs_XnTG_7JO5nxPRT--FEZMcI1J-E&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Set 3 Macetas Pastel');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Autorriego 24cm', 'Reserva de agua, ideal para vacaciones.', 69.90, 'https://hortalia.net/cdn/shop/files/05-02.jpg?v=1780323966&width=1080', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Autorriego 24cm');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Macetero Madera Rectangular', 'Madera tratada 40cm para balcón.', 89.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVQdlfS_ecr_c_YdUX9QIF_D9lv9eNecwo-Ofa5Z5KTf2CoOtqkAygDt4&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Macetero Madera Rectangular');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Maceta Vidrio Terrario 15cm', 'Vidrio soplado para terrarios y suculentas.', 28.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSGEs8utPO6n0AUF0oscl1D791TLqlc7lqHdkIYHcVsMi-7u1on75S_TtnZ&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Macetas')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Maceta Vidrio Terrario 15cm');

INSERT INTO categoria (nombre)
SELECT 'Accesorios'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Accesorios');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Tijera de Poda Ergonómica', 'Acero inoxidable con bloqueo de seguridad.', 35.90, '/img-carousel/accesorios-1.png', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Tijera de Poda Ergonómica');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Kit Herramientas Jardín 3 Piezas', 'Pala, rastrillo y trasplantador mango madera.', 65.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRRyimUJrbEwASSn_v2p3hAaAW12kysVbXcXYevcmU83PA-nJ14snbd7Xs&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Kit Herramientas Jardín 3 Piezas');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Sustrato Premium 5L', 'Turba, perlita y humus. Listo para transplantar.', 25.90, 'https://media.falabella.com/falabellaPE/149168091_01/w=1500,h=1500,fit=cover', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Sustrato Premium 5L');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Fertilizante Orgánico 500ml', 'NPK orgánico, uso quincenal.', 22.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcT9UoR9XVF3XIoQgSegnoY08LF-3kZYcMsfd3ZK5Z5vmLKiVykJCffvKT8J&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Fertilizante Orgánico 500ml');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Regadera 2L Verde Mentol', 'Plástico reciclado con rociador fino.', 29.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRVRtrQmxKoTtqGthulQvu03ttdgyMCQbhBYbmXFvosiA&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Regadera 2L Verde Mentol');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Guantes Jardinería', 'Palma engomada y transpirables, talla M.', 18.90, 'https://media.falabella.com/falabellaPE/118373869_01/w=800,h=800,fit=pad', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Guantes Jardinería');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Pulverizador 1L', 'Pulverización fina para riego foliar.', 15.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQzdQHe-KfX-7TCLJDCoTuM0VeAopRLSe3_r4-F1fmnLg&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Pulverizador 1L');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Tutor Bambú Pack x6', 'Soporte natural para trepadoras.', 12.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSKzUZbDvt5Tg5_hp-3S-uc33E0MWlGtWak2YJh9rBmtHjf2qG_lQ1c_ENj&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Accesorios')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Tutor Bambú Pack x6');

INSERT INTO categoria (nombre)
SELECT 'Principiantes'
WHERE NOT EXISTS (SELECT 1 FROM categoria WHERE nombre = 'Principiantes');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Kit Suculentas x3', '3 suculentas + 3 macetitas + sustrato.', 49.90, 'https://m.media-amazon.com/images/I/71auBbxyMYL.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Kit Suculentas x3');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Kit Hierbas Aromáticas', 'Menta, albahaca y cilantro + guía PDF.', 39.90, 'https://fieito.com/wp-content/uploads/2021/06/mini-cultivo-de-hierbas-aromaticas.jpg.webp', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Kit Hierbas Aromáticas');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Combo Monstera', 'Monstera joven + maceta cerámica 16cm.', 69.90, 'https://www.mijardin.pe/cdn/shop/files/setmonsterayluckybambu1.jpg?v=1732597986', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Combo Monstera');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Pack Pothos + Sansevieria', 'Dos plantas indestructibles para poca luz.', 59.90, 'https://www.mijardin.pe/cdn/shop/files/packsansevieria.jpg?v=1700951974', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Pack Pothos + Sansevieria');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Terrario Cerrado', 'Vidrio 20cm con fitonia y musgo, riego mínimo.', 55.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRNbzBO59t4NL_MeVPyqWcBQiqaUvl8VAetEComwAkfYBgjUtZJ3R162Yc&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Terrario Cerrado');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Kit Siembra Completo', 'Semillas, sustrato, macetitas y guía.', 29.90, 'https://i.blogs.es/0bb86b/kit-cultivo-vintage/450_1000.jpg', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Kit Siembra Completo');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Caja Regalo Starter', 'Planta sorpresa + accesorio + fertilizante.', 79.90, 'https://promart.vteximg.com.br/arquivos/ids/9975881-1000-1000/141982_20.jpg?v=639037966597800000', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Caja Regalo Starter');
INSERT INTO producto (nombre, descripcion, precio, imagen, stock, idCategoria)
SELECT 'Suscripción Planta Mes', 'Planta fácil + tips y calendario de riego.', 34.90, 'https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRUSMCOJPDF7dRKX5CxgUBYzAEK62JU87gRxFNrz1ekT1O5LSSUJRkRe0c&s=10', 10, c.id
FROM categoria c WHERE c.id = (SELECT MIN(id) FROM categoria WHERE nombre = 'Principiantes')
AND NOT EXISTS (SELECT 1 FROM producto p WHERE p.nombre = 'Suscripción Planta Mes');

COMMIT;
