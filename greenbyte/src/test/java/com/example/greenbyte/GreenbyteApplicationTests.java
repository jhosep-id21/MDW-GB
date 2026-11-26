package com.example.greenbyte;

import com.example.greenbyte.models.Categoria;
import com.example.greenbyte.models.Producto;
import com.example.greenbyte.models.User;
import com.example.greenbyte.repositorio.CampaniaRepository;
import com.example.greenbyte.repositorio.CategoriaRepository;
import com.example.greenbyte.repositorio.FichaPlantaRepository;
import com.example.greenbyte.repositorio.UserRepository;
import com.example.greenbyte.repositorio.ProductoRepository;
import com.example.greenbyte.repositorio.VentaRepository;
import jakarta.servlet.RequestDispatcher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GreenbyteApplicationTests {

    @Autowired
    private WebApplicationContext contexto;

    @Autowired
    private UserRepository usuarios;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private CampaniaRepository campanias;

    @Autowired
    private CategoriaRepository categorias;

    @Autowired
    private FichaPlantaRepository fichas;

    @Autowired
    private ProductoRepository productos;

    @Autowired
    private VentaRepository ventas;

    @Test
    void rutaDesconocidaOfreceVolverAlInicio() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        String ruta = "/gestor/pagina-inexistente";
        mvc.perform(get(ruta)).andExpect(status().isNotFound());

        mvc.perform(get("/error").accept(MediaType.TEXT_HTML)
                        .requestAttr(RequestDispatcher.ERROR_STATUS_CODE, 404)
                        .requestAttr(RequestDispatcher.ERROR_REQUEST_URI, ruta))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Volver al inicio")))
                .andExpect(content().string(containsString("href=\"/\"")));
    }

    @Test
    void contextLoads() {
    }

    @Test
    void paginasRenderizanLosControlesCompartidosDelEncabezado() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();

        for (String ruta : new String[] {"/", "/productos", "/contactanos", "/sobre-nosotros", "/registro"}) {
            mvc.perform(get(ruta))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("action=\"/iniciar-sesion\"")))
                    .andExpect(content().string(containsString("id=\"modal-acceso\"")))
                    .andExpect(content().string(containsString("id=\"panel-carrito\"")))
                    .andExpect(content().string(containsString("bootstrap.bundle.min.js")))
                    .andExpect(content().string(containsString("sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI")));
        }
    }

    @Test
    void manualByteRenderizaConSesionYRedirigeAVisitantes() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession sesion = new MockHttpSession();
        sesion.setAttribute("logueado", true);
        sesion.setAttribute("rol", "CLIENTE");

        String enlaceFichasConModal = "(?s).*<a[^>]*href=\"/manual-byte\"[^>]*data-bs-target=\"#modal-acceso\"[^>]*>.*";
        mvc.perform(get("/"))
                .andExpect(content().string(containsString("id=\"manual-byte\"")))
                .andExpect(content().string(containsString("data-sesion=\"false\"")))
                .andExpect(content().string(matchesPattern(enlaceFichasConModal)));
        mvc.perform(get("/").session(sesion))
                .andExpect(content().string(containsString("id=\"manual-byte\"")))
                .andExpect(content().string(containsString("data-sesion=\"true\"")))
                .andExpect(content().string(not(matchesPattern(enlaceFichasConModal))));

        mvc.perform(get("/manual-byte")).andExpect(redirectedUrl("/"));
        mvc.perform(get("/manual-byte").session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Monstera Deliciosa")))
                .andExpect(content().string(containsString("id=\"calculadora-riego\"")))
                .andExpect(content().string(containsString("id=\"tips-luz\"")));
        for (String ruta : new String[] {"/fichas", "/calculadora-riego", "/tips-luz"}) {
            mvc.perform(get(ruta)).andExpect(redirectedUrl("/manual-byte#" + ruta.substring(1)));
        }
    }

    @Test
    void gestorPublicaYOcultaUnaFicha() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession gestor = new MockHttpSession();
        gestor.setAttribute("rol", "GESTOR");
        gestor.setAttribute("tokenCambioRol", "token-de-prueba");
        MockHttpSession cliente = new MockHttpSession();
        cliente.setAttribute("rol", "CLIENTE");
        cliente.setAttribute("logueado", true);

        mvc.perform(get("/gestor/fichas").session(cliente)).andExpect(status().isForbidden());
        mvc.perform(get("/gestor/fichas").session(gestor)).andExpect(status().isOk());
        mvc.perform(formularioFicha(gestor).param("activa", "true"))
                .andExpect(redirectedUrl("/gestor/fichas"));
        mvc.perform(get("/manual-byte").session(cliente))
                .andExpect(content().string(containsString("Begonia de prueba")));

        Long id = fichas.findAllByOrderByNombreAsc().stream()
                .filter(ficha -> ficha.getNombre().equals("Begonia de prueba")).findFirst().orElseThrow().getId();
        mvc.perform(formularioFicha(gestor).param("id", id.toString()).param("_activa", "on"))
                .andExpect(redirectedUrl("/gestor/fichas"));
        mvc.perform(get("/manual-byte").session(cliente))
                .andExpect(content().string(not(containsString("Begonia de prueba"))));
    }

    private MockHttpServletRequestBuilder formularioFicha(MockHttpSession gestor) {
        return post("/gestor/fichas").session(gestor).param("token", "token-de-prueba")
                .param("nombre", "Begonia de prueba").param("tipo", "Interior")
                .param("descripcion", "Planta de interior").param("luz", "Indirecta")
                .param("riego", "Cuando se seque la superficie").param("notas", "Usa una maceta con drenaje.");
    }

    @Test
    void administradorPuedeCambiarDeRolYGestionarSoloConElRolActivo() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession sesion = iniciarSesion(mvc, 'A');
        String token = (String) sesion.getAttribute("tokenCambioRol");

        mvc.perform(get("/elegir-rol").session(sesion)).andExpect(status().isOk());
        mvc.perform(get("/administrador").session(sesion)).andExpect(status().isForbidden());
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "ADMIN").param("token", "incorrecto"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "ADMIN").param("token", token))
                .andExpect(redirectedUrl("/administrador"));
        mvc.perform(get("/administrador").session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Rol activo: Administrador")))
                .andExpect(content().string(containsString("Cambiar rol")))
                .andExpect(content().string(not(containsString("id=\"panel-carrito\""))))
                .andExpect(content().string(not(containsString("id=\"buscador-productos\""))));
        mvc.perform(get("/gestor").session(sesion)).andExpect(status().isForbidden());
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "GESTOR").param("token", token))
                .andExpect(redirectedUrl("/gestor"));
        mvc.perform(get("/gestor").session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Rol activo: Gestor")))
                .andExpect(content().string(containsString("Cambiar rol")))
                .andExpect(content().string(not(containsString("id=\"panel-carrito\""))))
                .andExpect(content().string(not(containsString("id=\"buscador-productos\""))));
        mvc.perform(get("/administrador").session(sesion)).andExpect(status().isForbidden());
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "CLIENTE").param("token", token))
                .andExpect(redirectedUrl("/"));
        mvc.perform(get("/manual-byte").session(sesion)).andExpect(status().isOk());
        mvc.perform(get("/gestor").session(sesion)).andExpect(status().isForbidden());
        mvc.perform(get("/elegir-rol").session(sesion)).andExpect(status().isOk());
    }

    @Test
    void gestorNoPuedeElegirAdministrador() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession sesion = iniciarSesion(mvc, 'G');
        String token = (String) sesion.getAttribute("tokenCambioRol");

        mvc.perform(get("/elegir-rol").session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("value=\"ADMIN\""))));
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "ADMIN").param("token", token))
                .andExpect(status().isForbidden());
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "GESTOR").param("token", token))
                .andExpect(redirectedUrl("/gestor"));
        mvc.perform(get("/manual-byte").session(sesion)).andExpect(redirectedUrl("/elegir-rol"));
    }

    @Test
    void gestorPublicaYDesactivaCampanias() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession sesion = new MockHttpSession();
        sesion.setAttribute("rol", "GESTOR");
        sesion.setAttribute("tokenCambioRol", "token-de-prueba");
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));
        mvc.perform(get("/gestor/categorias")).andExpect(status().isForbidden());
        mvc.perform(get("/gestor/categorias").session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("action=\"/gestor/categorias\"")));
        mvc.perform(post("/gestor/categorias").param("nombre", "Planta navideña").param("token", "token-de-prueba"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/gestor/categorias").session(sesion)
                        .param("nombre", " Planta navideña ").param("token", "token-de-prueba"))
                .andExpect(redirectedUrl("/gestor/categorias"));
        mvc.perform(post("/gestor/categorias").session(sesion)
                        .param("nombre", "PLANTA NAVIDEÑA").param("token", "token-de-prueba"))
                .andExpect(flash().attribute("error", "Ya existe una categoría con ese nombre."));
        Categoria categoria = categorias.findAll().stream()
                .filter(item -> item.getNombre().equals("Planta navideña"))
                .findFirst().orElseThrow();
        mvc.perform(get("/productos"))
                .andExpect(content().string(containsString("value=\"Planta navideña\"")));

        mvc.perform(get("/gestor/categorias").session(sesion).param("editar", categoria.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Guardar cambios")));
        mvc.perform(post("/gestor/categorias").session(sesion)
                        .param("id", categoria.getId().toString()).param("nombre", "Planta de Navidad")
                        .param("token", "token-de-prueba"))
                .andExpect(redirectedUrl("/gestor/categorias"))
                .andExpect(flash().attribute("success", "Categoría actualizada correctamente."));
        mvc.perform(get("/productos"))
                .andExpect(content().string(containsString("value=\"Planta de Navidad\"")));

        mvc.perform(get("/gestor/campanias")).andExpect(status().isForbidden());
        mvc.perform(get("/gestor/campanias").session(sesion)).andExpect(status().isOk());
        mvc.perform(post("/gestor/campanias").session(sesion)
                        .param("token", "token-de-prueba")
                        .param("titulo", "Fechas incorrectas")
                        .param("mensaje", "No se debe publicar")
                        .param("inicio", hoy.toString())
                        .param("fin", hoy.minusDays(1).toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("El fin debe ser igual o posterior al inicio")));

        mvc.perform(post("/gestor/campanias").session(sesion)
                        .param("token", "token-de-prueba")
                        .param("titulo", "Navidad GreenByte")
                        .param("mensaje", "Encuentra macetas para regalar.")
                        .param("inicio", hoy.toString())
                        .param("fin", hoy.toString())
                        .param("categoriaId", categoria.getId().toString())
                        .param("activa", "true"))
                .andExpect(redirectedUrl("/gestor/campanias"));

        Long id = campanias.findAllByOrderByInicioDescIdDesc().getFirst().getId();
        mvc.perform(get("/gestor/campanias/{id}", id).session(sesion))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Navidad GreenByte")));
        mvc.perform(get("/"))
                .andExpect(content().string(containsString("Navidad GreenByte")))
                .andExpect(content().string(containsString("/productos?categoria=Planta")));

        mvc.perform(post("/gestor/categorias/{id}/eliminar", categoria.getId()).session(sesion)
                        .param("token", "token-de-prueba"))
                .andExpect(flash().attribute("error", containsString("La categoría está en uso.")));

        mvc.perform(post("/gestor/campanias/{id}", id).session(sesion)
                        .param("token", "token-de-prueba")
                        .param("titulo", "Navidad GreenByte")
                        .param("mensaje", "Encuentra macetas para regalar.")
                        .param("inicio", hoy.toString())
                        .param("fin", hoy.toString())
                        .param("_activa", "on"))
                .andExpect(redirectedUrl("/gestor/campanias"));
        mvc.perform(get("/")).andExpect(content().string(not(containsString("Navidad GreenByte"))));
        mvc.perform(post("/gestor/categorias/{id}/eliminar", categoria.getId()).session(sesion)
                        .param("token", "token-de-prueba"))
                .andExpect(redirectedUrl("/gestor/categorias"))
                .andExpect(flash().attribute("success", "Categoría eliminada correctamente."));
        mvc.perform(get("/productos"))
                .andExpect(content().string(not(containsString("Planta de Navidad"))));
    }

    @Test
    void administradorEditaUsuarioYElCambioDeRolInvalidaSuSesion() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession admin = new MockHttpSession();
        admin.setAttribute("rol", "ADMIN");
        admin.setAttribute("tokenCambioRol", "token-admin");

        User usuario = new User();
        usuario.setNombre("Usuario de prueba");
        usuario.setCorreo("edicion-usuarios@example.com");
        usuario.setTelefono("999999990");
        usuario.setClave("prueba123");
        usuario.setAcceso('G');
        usuarios.save(usuario);
        // Simula una cuenta anterior a la validación de longitud de contraseña.
        jdbc.update("update \"user\" set clave = ? where id = ?", "1234", usuario.getId());
        MockHttpSession gestor = new MockHttpSession();
        gestor.setAttribute("usuario", usuario.getCorreo());
        gestor.setAttribute("accesoCuenta", 'G');
        gestor.setAttribute("rol", "GESTOR");

        mvc.perform(get("/administrador").session(admin).param("editar", usuario.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Guardar cambios")));
        mvc.perform(post("/administrador/usuarios/{id}", usuario.getId()).session(admin)
                        .param("nombre", "Nombre actualizado").param("telefono", "999999991")
                        .param("acceso", "U").param("token", "token-admin"))
                .andExpect(redirectedUrl("/administrador"));
        mvc.perform(get("/administrador").session(admin))
                .andExpect(content().string(containsString("Nombre actualizado")))
                .andExpect(content().string(containsString("999999991")));
        mvc.perform(get("/gestor").session(gestor)).andExpect(redirectedUrl("/"));
    }

    @Test
    void cerrarSesionInvalidaElAccesoYEvitaGuardarPaginasPrivadasEnCache() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        User usuario = new User();
        usuario.setNombre("Administradora de prueba");
        usuario.setAcceso('A');
        usuario.setCorreo("cerrar-sesion@example.com");
        usuario.setTelefono("999999998");
        usuario.setClave("prueba123");
        usuarios.save(usuario);

        MockHttpSession sesion = (MockHttpSession) mvc.perform(post("/iniciar-sesion")
                        .session(new MockHttpSession()).param("correo", usuario.getCorreo()).param("clave", "prueba123"))
                .andExpect(redirectedUrl("/elegir-rol"))
                .andReturn().getRequest().getSession(false);
        String token = (String) sesion.getAttribute("tokenCambioRol");
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "CLIENTE").param("token", token))
                .andExpect(redirectedUrl("/"));
        mvc.perform(get("/manual-byte").session(sesion))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().string(containsString("/js/historial-sesion.js")));
        mvc.perform(post("/elegir-rol").session(sesion).param("rol", "ADMIN").param("token", token))
                .andExpect(redirectedUrl("/administrador"));
        mvc.perform(get("/productos").session(sesion))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().string(containsString(usuario.getCorreo())));
        mvc.perform(get("/cerrar-sesion").session(sesion))
                .andExpect(redirectedUrl("/"));
        org.junit.jupiter.api.Assertions.assertTrue(sesion.isInvalid());
        mvc.perform(get("/manual-byte")).andExpect(redirectedUrl("/"));
        mvc.perform(get("/administrador/ventas")).andExpect(status().isForbidden());
        mvc.perform(get("/productos"))
                .andExpect(status().isOk())
                .andExpect(header().string("Cache-Control", "no-store"))
                .andExpect(content().string(not(containsString(usuario.getCorreo()))))
                .andExpect(content().string(containsString("Iniciar Sesión")));
    }

    @Test
    void confirmaCompraConStockYPrecioDelServidorYEvitaDuplicados() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        User usuario = new User();
        usuario.setNombre("Cliente de ventas");
        usuario.setAcceso('G');
        usuario.setCorreo("ventas-prueba@example.com");
        usuario.setTelefono("999999994");
        usuario.setClave("prueba123");
        usuarios.save(usuario);

        Categoria categoria = new Categoria();
        categoria.setNombre("Categoría de ventas");
        categorias.save(categoria);
        Producto producto = new Producto();
        producto.setNombre("Planta de prueba");
        producto.setDescripcion("Descripción de la planta");
        producto.setPrecio(new BigDecimal("12.50"));
        producto.setStock(3);
        producto.setCategoria(categoria);
        productos.save(producto);

        MockHttpSession cliente = (MockHttpSession) mvc.perform(post("/iniciar-sesion").session(new MockHttpSession())
                        .param("correo", usuario.getCorreo()).param("clave", "prueba123"))
                .andExpect(redirectedUrl("/elegir-rol"))
                .andReturn().getRequest().getSession(false);
        String token = (String) cliente.getAttribute("tokenCambioRol");
        mvc.perform(post("/elegir-rol").session(cliente).param("rol", "CLIENTE").param("token", token))
                .andExpect(redirectedUrl("/"));
        mvc.perform(get("/productos").session(cliente))
                .andExpect(content().string(containsString("data-compras=\"/compras\"")))
                .andExpect(content().string(containsString("data-cliente=\"true\"")))
                .andExpect(content().string(containsString("data-token=\"" + token + "\"")));

        String compra = """
                {"codigo":"7b8390ab-e38f-4cf3-9f25-cca6967629f0","articulos":[{"id":%d,"cantidad":2}]}
                """.formatted(producto.getId());

        mvc.perform(post("/compras").contentType(MediaType.APPLICATION_JSON).content(compra))
                .andExpect(status().isForbidden());
        for (int intento = 0; intento < 2; intento++) {
            mvc.perform(post("/compras").session(cliente).header("X-Token-Sesion", token)
                            .contentType(MediaType.APPLICATION_JSON).content(compra))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.total").value(25.0));
        }
        assertEquals(1, ventas.count());
        assertEquals(1, productos.findById(producto.getId()).orElseThrow().getStock());

        String otraCompra = compra.replace("7b8390ab-e38f-4cf3-9f25-cca6967629f0", "36378d67-1f42-49a0-8b06-a8a40dc3cc1f");
        mvc.perform(post("/compras").session(cliente).header("X-Token-Sesion", token)
                        .contentType(MediaType.APPLICATION_JSON).content(otraCompra))
                .andExpect(status().isConflict());
        assertEquals(1, ventas.count());

        Long ventaId = ventas.findAllByOrderByIdDesc().getFirst().getId();
        MockHttpSession admin = new MockHttpSession();
        admin.setAttribute("rol", "ADMIN");
        mvc.perform(get("/administrador/ventas").session(cliente)).andExpect(status().isForbidden());
        mvc.perform(get("/administrador/ventas").session(admin)).andExpect(status().isOk());
        mvc.perform(get("/administrador/ventas").session(admin).param("id", ventaId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Cliente de ventas")))
                .andExpect(content().string(containsString("Planta de prueba")));
    }

    private MockHttpSession iniciarSesion(MockMvc mvc, char acceso) throws Exception {
        User usuario = new User();
        usuario.setNombre("Cuenta de prueba");
        usuario.setTelefono("999999999");
        usuario.setCorreo("rol-" + Character.toLowerCase(acceso) + "@example.com");
        usuario.setClave("prueba123");
        usuario.setAcceso(acceso);
        usuarios.save(usuario);

        return (MockHttpSession) mvc.perform(post("/iniciar-sesion").session(new MockHttpSession())
                        .param("correo", usuario.getCorreo()).param("clave", "prueba123"))
                .andExpect(redirectedUrl("/elegir-rol"))
                .andReturn().getRequest().getSession(false);
    }
}
