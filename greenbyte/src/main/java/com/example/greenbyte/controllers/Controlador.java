package com.example.greenbyte.controllers;

import com.example.greenbyte.models.Categoria;
import com.example.greenbyte.models.Producto;
import com.example.greenbyte.models.User;
import com.example.greenbyte.repositorio.CategoriaRepository;
import com.example.greenbyte.repositorio.ProductoRepository;
import com.example.greenbyte.repositorio.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
public class Controlador {

    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;

    @Autowired
    public Controlador(JdbcTemplate jdbcTemplate,
                       UserRepository userRepository,
                       ProductoRepository productoRepository,
                       CategoriaRepository categoriaRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
    }

    // ================= Vistas Principales =================

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/inicio")
    public String inicio() {
        return "inicio";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        List<Producto> lista = productoRepository.findAll();
        model.addAttribute("productos", lista);
        return "productos";
    }

    @GetMapping("/accesorios")
    public String mostrarAccesorios() {
        return "accesorios";
    }

    @GetMapping("/administrador")
    public String mostrarAdministrador(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("productoAEditar", new Producto());
        model.addAttribute("editando", false);
        return "administrador";
    }

    @GetMapping("/contactanos")
    public String mostrarContactanos() {
        return "contactanos";
    }

    @GetMapping("/exterior")
    public String mostrarExterior() {
        return "exterior";
    }

    @GetMapping("/interior")
    public String mostrarInterior() {
        return "interior";
    }

    @GetMapping("/macetas")
    public String mostrarMacetas() {
        return "macetas";
    }

    @GetMapping("/principiantes")
    public String mostrarPrincipiantes() {
        return "principiantes";
    }

    @GetMapping("/fichas")
    public String fichas() {
        return "fichas";
    }

    @GetMapping("/calculadora-riego")
    public String calculadoraRiego() {
        return "calculadora-riego";
    }

    @GetMapping("/tips-luz")
    public String tipsLuz() {
        return "tips-luz";
    }

    // ================= Registro con Spring Validator =================

    @GetMapping("/registro")
    public String mostrarRegistro(Model model) {
        if (!model.containsAttribute("usuario")) {
            model.addAttribute("usuario", new User());
        }
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarUsuario(@Valid @ModelAttribute("usuario") User usuario,
                                   BindingResult result,
                                   RedirectAttributes redireccion) {
        // 1. Si hay errores de validación (@NotEmpty, @Email, etc.), volvemos al formulario
        if (result.hasErrors()) {
            return "registro";
        }

        // 2. Validación de negocio: correo único
        if (userRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            result.rejectValue("correo", "usuario.duplicado", "El correo ya está registrado");
            return "registro";
        }

        // 3. Guardar en base de datos
        if (usuario.getAcceso() == null) {
            usuario.setAcceso('U');
        }
        userRepository.save(usuario);
        redireccion.addFlashAttribute("success", "¡Cuenta creada correctamente! Ya puedes iniciar sesión.");
        return "redirect:/";
    }

    // ================= Login (sesión) =================

    @RequestMapping(value = "/iniciar-sesion", method = {RequestMethod.GET, RequestMethod.POST})
    public String iniciarSesion(@RequestParam(required = false) String correo,
                                @RequestParam(required = false) String clave,
                                HttpSession session,
                                RedirectAttributes redireccion) {
        if (correo == null || correo.isBlank() || clave == null || clave.isBlank()) {
            redireccion.addFlashAttribute("error", "Ingresa correo y contraseña");
            return "redirect:/";
        }

        String correoLimpio = correo.trim().toLowerCase();
        Optional<User> usuarioOpt = userRepository.findByCorreo(correoLimpio);

        if (usuarioOpt.isPresent()) {
            User u = usuarioOpt.get();
            if (u.getClave().equals(clave)) {
                session.setAttribute("usuario", u.getCorreo());
                session.setAttribute("nombre", u.getNombre());
                session.setAttribute("rol", u.getAcceso() != null && u.getAcceso() == 'A' ? "ADMIN" : "USER");
                session.setAttribute("logueado", true);
                redireccion.addFlashAttribute("success", "¡Bienvenido " + u.getNombre() + "!");
                if (u.getAcceso() != null && u.getAcceso() == 'A') {
                    return "redirect:/administrador";
                }
                return "redirect:/";
            } else {
                redireccion.addFlashAttribute("error", "Contraseña incorrecta");
                return "redirect:/";
            }
        } else {
            redireccion.addFlashAttribute("error", "El correo no está registrado");
            return "redirect:/";
        }
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================= Gestión Web Admin (CRUD de Productos) =================

    @PostMapping("/administrador/productos/guardar")
    public String guardarProductoAdmin(@Valid @ModelAttribute("nuevoProducto") Producto producto,
                                       BindingResult result,
                                       @RequestParam(value = "categoriaId", required = false) Long categoriaId,
                                       RedirectAttributes redireccion,
                                       Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("productoAEditar", new Producto());
            model.addAttribute("editando", false);
            return "administrador";
        }

        if (categoriaId != null) {
            categoriaRepository.findById(categoriaId).ifPresent(producto::setCategoria);
        }

        productoRepository.save(producto);
        redireccion.addFlashAttribute("success", "Producto agregado correctamente");
        return "redirect:/administrador";
    }

    @GetMapping("/administrador/productos/eliminar/{id}")
    public String eliminarProductoAdmin(@PathVariable Long id, RedirectAttributes redireccion) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            redireccion.addFlashAttribute("success", "Producto eliminado correctamente");
        } else {
            redireccion.addFlashAttribute("error", "El producto no existe");
        }
        return "redirect:/administrador";
    }

    @GetMapping("/administrador/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model, RedirectAttributes redireccion) {
        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isEmpty()) {
            redireccion.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/administrador";
        }
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("productoAEditar", opt.get());
        model.addAttribute("editando", true);
        model.addAttribute("productoId", id);
        return "administrador";
    }

    @PostMapping("/administrador/productos/editar/{id}")
    public String actualizarProducto(@PathVariable Long id,
                                     @Valid @ModelAttribute("productoAEditar") Producto producto,
                                     BindingResult result,
                                     @RequestParam(value = "categoriaId", required = false) Long categoriaId,
                                     RedirectAttributes redireccion,
                                     Model model) {
        if (result.hasErrors()) {
            model.addAttribute("productos", productoRepository.findAll());
            model.addAttribute("categorias", categoriaRepository.findAll());
            model.addAttribute("nuevoProducto", new Producto());
            model.addAttribute("editando", true);
            model.addAttribute("productoId", id);
            return "administrador";
        }

        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isEmpty()) {
            redireccion.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/administrador";
        }

        Producto existente = opt.get();
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setImagen(producto.getImagen());

        if (categoriaId != null) {
            categoriaRepository.findById(categoriaId).ifPresent(existente::setCategoria);
        }

        productoRepository.save(existente);
        redireccion.addFlashAttribute("success", "Producto actualizado correctamente");
        return "redirect:/administrador";
    }

    // ================= Endpoints de Prueba (Estilo Semana 11) =================

    @GetMapping("/db-test")
    @ResponseBody
    public String test() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return "Conexión a la base de datos exitosa";
    }

    @GetMapping("/usuario")
    @ResponseBody
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/crear")
    @ResponseBody
    public String saveUser(@RequestBody User user) {
        if (user.getAcceso() == null) {
            user.setAcceso('U');
        }
        if (user.getCorreo() != null && userRepository.findByCorreo(user.getCorreo().trim().toLowerCase()).isPresent()) {
            return "El correo ya está registrado";
        }
        if (user.getCorreo() != null) {
            user.setCorreo(user.getCorreo().trim().toLowerCase());
        }
        userRepository.save(user);
        return "Guardado correctamente";
    }

    // --- Endpoints Categorías ---

    @GetMapping("/test/categorias")
    @ResponseBody
    public List<Categoria> listarCategoriasTest() {
        return categoriaRepository.findAll();
    }

    @PostMapping("/test/categorias")
    @ResponseBody
    public String guardarCategoriaTest(@RequestBody Categoria c) {
        categoriaRepository.save(c);
        return "Categoría guardada correctamente con ID: " + c.getId();
    }

    // --- Endpoints CRUD Productos ---

    @GetMapping("/test/productos")
    @ResponseBody
    public List<Producto> listarProductosTest() {
        return productoRepository.findAll();
    }

    @GetMapping("/test/productos/{id}")
    @ResponseBody
    public Optional<Producto> obtenerProductoTest(@PathVariable Long id) {
        return productoRepository.findById(id);
    }

    @PostMapping("/test/productos")
    @ResponseBody
    public String guardarProductoTest(@RequestBody Producto p) {
        if (p.getCategoria() != null && p.getCategoria().getId() != null) {
            categoriaRepository.findById(p.getCategoria().getId()).ifPresent(p::setCategoria);
        }
        productoRepository.save(p);
        return "Producto guardado correctamente con ID: " + p.getId();
    }

    @PutMapping("/test/productos/{id}")
    @ResponseBody
    public String actualizarProductoTest(@PathVariable Long id, @RequestBody Producto nuevoP) {
        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isPresent()) {
            Producto actual = opt.get();
            actual.setNombre(nuevoP.getNombre());
            actual.setDescripcion(nuevoP.getDescripcion());
            actual.setPrecio(nuevoP.getPrecio());
            actual.setStock(nuevoP.getStock());
            if (nuevoP.getCategoria() != null && nuevoP.getCategoria().getId() != null) {
                categoriaRepository.findById(nuevoP.getCategoria().getId()).ifPresent(actual::setCategoria);
            }
            if (nuevoP.getImagen() != null) {
                actual.setImagen(nuevoP.getImagen());
            }
            productoRepository.save(actual);
            return "Producto actualizado correctamente";
        }
        return "Error: Producto no encontrado con ID " + id;
    }

    @DeleteMapping("/test/productos/{id}")
    @ResponseBody
    public String eliminarProductoTest(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return "Producto eliminado correctamente";
        }
        return "Error: Producto no encontrado con ID " + id;
    }

    @GetMapping("/test/productos/eliminar/{id}")
    @ResponseBody
    public String eliminarProductoGetTest(@PathVariable Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            return "Producto eliminado correctamente mediante GET";
        }
        return "Error: Producto no encontrado con ID " + id;
    }
}
