package com.example.greenbyte;

import com.example.greenbyte.models.Categoria;
import com.example.greenbyte.models.User;
import com.example.greenbyte.repositorio.CampaniaRepository;
import com.example.greenbyte.repositorio.CategoriaRepository;
import com.example.greenbyte.repositorio.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.time.ZoneId;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.matchesPattern;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GreenbyteApplicationTests {

    @Autowired
    private WebApplicationContext contexto;

    @Autowired
    private UserRepository usuarios;

    @Autowired
    private CampaniaRepository campanias;

    @Autowired
    private CategoriaRepository categorias;

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

        String enlaceFichasConModal = "(?s).*<a href=\"/fichas\"[^>]*data-bs-target=\"#modal-acceso\"[^>]*>.*";
        mvc.perform(get("/"))
                .andExpect(content().string(containsString("id=\"manual-byte\"")))
                .andExpect(content().string(containsString("data-sesion=\"false\"")))
                .andExpect(content().string(matchesPattern(enlaceFichasConModal)));
        mvc.perform(get("/").session(sesion))
                .andExpect(content().string(containsString("id=\"manual-byte\"")))
                .andExpect(content().string(containsString("data-sesion=\"true\"")))
                .andExpect(content().string(not(matchesPattern(enlaceFichasConModal))));

        for (String ruta : new String[] {"/fichas", "/calculadora-riego", "/tips-luz"}) {
            mvc.perform(get(ruta)).andExpect(redirectedUrl("/"));
            mvc.perform(get(ruta).session(sesion))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("id=\"panel-carrito\"")))
                    .andExpect(content().string(containsString("sha384-FKyoEForCGlyvwx9Hj09JcYn3nv7wiPVlz7YYwJrWVcXK/BmnVDxM+D2scQbITxI")));
        }
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
        mvc.perform(get("/fichas").session(sesion)).andExpect(status().isOk());
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
        mvc.perform(get("/fichas").session(sesion)).andExpect(redirectedUrl("/elegir-rol"));
    }

    @Test
    void gestorPublicaYDesactivaCampanias() throws Exception {
        MockMvc mvc = MockMvcBuilders.webAppContextSetup(contexto).build();
        MockHttpSession sesion = new MockHttpSession();
        sesion.setAttribute("rol", "GESTOR");
        sesion.setAttribute("tokenCambioRol", "token-de-prueba");
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));
        mvc.perform(post("/gestor/categorias").param("nombre", "Planta navideña").param("token", "token-de-prueba"))
                .andExpect(status().isForbidden());
        mvc.perform(post("/gestor/categorias").session(sesion)
                        .param("nombre", " Planta navideña ").param("token", "token-de-prueba"))
                .andExpect(redirectedUrl("/gestor"));
        mvc.perform(post("/gestor/categorias").session(sesion)
                        .param("nombre", "PLANTA NAVIDEÑA").param("token", "token-de-prueba"))
                .andExpect(flash().attribute("error", "Ya existe una categoría con ese nombre."));
        Categoria categoria = categorias.findAll().stream()
                .filter(item -> item.getNombre().equals("Planta navideña"))
                .findFirst().orElseThrow();
        mvc.perform(get("/productos"))
                .andExpect(content().string(containsString("value=\"Planta navideña\"")));

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

        mvc.perform(post("/gestor/campanias/{id}", id).session(sesion)
                        .param("token", "token-de-prueba")
                        .param("titulo", "Navidad GreenByte")
                        .param("mensaje", "Encuentra macetas para regalar.")
                        .param("inicio", hoy.toString())
                        .param("fin", hoy.toString())
                        .param("_activa", "on"))
                .andExpect(redirectedUrl("/gestor/campanias"));
        mvc.perform(get("/")).andExpect(content().string(not(containsString("Navidad GreenByte"))));
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
