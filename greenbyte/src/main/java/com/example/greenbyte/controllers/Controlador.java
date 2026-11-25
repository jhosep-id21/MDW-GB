package com.example.greenbyte.controllers;

import com.example.greenbyte.models.Categoria;
import com.example.greenbyte.models.Producto;
import com.example.greenbyte.models.User;
import com.example.greenbyte.repositorio.CampaniaRepository;
import com.example.greenbyte.repositorio.CategoriaRepository;
import com.example.greenbyte.repositorio.ProductoRepository;
import com.example.greenbyte.repositorio.UserRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.server.ResponseStatusException;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class Controlador {

    private final UserRepository userRepository;
    private final ProductoRepository productoRepository;
    private final CategoriaRepository categoriaRepository;
    private final PasswordEncoder passwordEncoder;
    private final CampaniaRepository campaniaRepository;

    public Controlador(UserRepository userRepository,
            ProductoRepository productoRepository,
            CategoriaRepository categoriaRepository,
            PasswordEncoder passwordEncoder,
            CampaniaRepository campaniaRepository) {
        this.userRepository = userRepository;
        this.productoRepository = productoRepository;
        this.categoriaRepository = categoriaRepository;
        this.passwordEncoder = passwordEncoder;
        this.campaniaRepository = campaniaRepository;
    }

    @ModelAttribute
    public void protegerRutasPorRol(HttpServletRequest request, HttpSession session) {
        String ruta = String.valueOf(request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE));
        Object rol = session.getAttribute("rol");

        if (ruta.equals("/administrador") && !"ADMIN".equals(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Se requiere el rol de administrador");
        }
        if ((ruta.equals("/gestor") || ruta.startsWith("/gestor/"))
                && !"GESTOR".equals(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Se requiere el rol de gestor");
        }
    }

    // ================= Vistas Principales =================

    private List<Categoria> obtenerOCrearCategorias() {
        List<Categoria> categorias = categoriaRepository.findAll();
        if (categorias.isEmpty()) {
            Categoria c1 = new Categoria();
            c1.setNombre("Plantas de Interior");
            categoriaRepository.save(c1);

            Categoria c2 = new Categoria();
            c2.setNombre("Plantas de Exterior");
            categoriaRepository.save(c2);

            Categoria c3 = new Categoria();
            c3.setNombre("Macetas");
            categoriaRepository.save(c3);

            Categoria c4 = new Categoria();
            c4.setNombre("Accesorios");
            categoriaRepository.save(c4);

            categorias = categoriaRepository.findAll();
        }
        return categorias;
    }

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("destacados", productoRepository.findFirst4ByStockGreaterThanOrderByIdAsc(0));
        LocalDate hoy = LocalDate.now(ZoneId.of("America/Lima"));
        campaniaRepository.findFirstByActivaTrueAndInicioLessThanEqualAndFinGreaterThanEqualOrderByInicioDescIdDesc(hoy, hoy)
                .ifPresent(campania -> model.addAttribute("campaniaVigente", campania));
        return "index";
    }

    @GetMapping({"/inicio", "/explorar"})
    public String redirigirAInicio() {
        return "redirect:/";
    }

    @GetMapping("/sobre-nosotros")
    public String sobreNosotros() {
        return "sobre-nosotros";
    }

    @GetMapping("/productos")
    public String productos(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("categorias", categoriaRepository.findAll());
        return "productos";
    }

    @GetMapping("/{categoria:interior|exterior|macetas|accesorios|principiantes}")
    public String redirigirCategoria(@PathVariable String categoria) {
        return "redirect:/productos?categoria=" + categoria;
    }

    @GetMapping("/administrador")
    public String mostrarAdministrador(Model model) {
        model.addAttribute("usuarios", userRepository.findAll());
        return "administrador";
    }

    @GetMapping("/gestor")
    public String mostrarGestor(Model model) {
        cargarCatalogoGestor(model);
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("productoAEditar", new Producto());
        model.addAttribute("editando", false);
        return "gestor";
    }

    @GetMapping("/contactanos")
    public String mostrarContactanos() {
        return "contactanos";
    }

    @GetMapping("/{herramienta:fichas|calculadora-riego|tips-luz}")
    public String manualByte(@PathVariable String herramienta, HttpSession session,
                             RedirectAttributes redireccion) {
        if (!Boolean.TRUE.equals(session.getAttribute("logueado"))) {
            redireccion.addFlashAttribute("error", "Inicia sesión para acceder a Manual Byte.");
            return "redirect:/";
        }
        if (!"CLIENTE".equals(session.getAttribute("rol"))) {
            return "redirect:/elegir-rol";
        }
        return herramienta;
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
        // 1. Si hay errores de validación (@NotEmpty, @Email, etc.), volvemos al
        // formulario
        if (result.hasErrors()) {
            return "registro";
        }

        // 2. Normalizamos el correo antes de comprobar su unicidad.
        usuario.setCorreo(usuario.getCorreo().trim().toLowerCase());
        if (userRepository.findByCorreo(usuario.getCorreo()).isPresent()) {
            result.rejectValue("correo", "usuario.duplicado", "El correo ya está registrado");
            return "registro";
        }

        // 3. Guardar en base de datos
        // El rol nunca debe venir del formulario público.
        usuario.setId(null);
        usuario.setAcceso('U');
        usuario.setClave(passwordEncoder.encode(usuario.getClave()));
        userRepository.save(usuario);
        redireccion.addFlashAttribute("success", "¡Cuenta creada correctamente! Ya puedes iniciar sesión.");
        return "redirect:/";
    }

    @PostMapping("/crear")
    @ResponseBody
    public String registrarUsuarioDesdeModal(@Valid @RequestBody User user) {
        // Este endpoint es de registro público: nunca permite crear administradores.
        user.setId(null);
        user.setAcceso('U');
        if (user.getCorreo() != null
                && userRepository.findByCorreo(user.getCorreo().trim().toLowerCase()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El correo ya está registrado");
        }
        if (user.getCorreo() != null) {
            user.setCorreo(user.getCorreo().trim().toLowerCase());
        }
        if (user.getClave() == null || user.getClave().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La contraseña es obligatoria");
        }
        user.setClave(passwordEncoder.encode(user.getClave()));
        userRepository.save(user);
        return "Guardado correctamente";
    }

    // ================= Login (sesión) =================

    @PostMapping("/iniciar-sesion")
    public String iniciarSesion(HttpServletRequest request,
            @RequestParam(required = false) String correo,
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
            boolean claveValida = u.getClave() != null
                    && (u.getClave().startsWith("$2a$")
                        ? passwordEncoder.matches(clave, u.getClave())
                        : u.getClave().equals(clave));

            if (claveValida) {
                request.changeSessionId();
                // Migra contraseñas antiguas en texto plano al primer inicio de sesión.
                if (!u.getClave().startsWith("$2a$")) {
                    u.setClave(passwordEncoder.encode(clave));
                    userRepository.save(u);
                }
                session.setAttribute("usuario", u.getCorreo());
                session.setAttribute("nombre", u.getNombre());
                List<String> roles = switch (u.getAcceso() == null ? 'U' : u.getAcceso()) {
                    case 'A' -> List.of("ADMIN", "GESTOR", "CLIENTE");
                    case 'G' -> List.of("GESTOR", "CLIENTE");
                    default -> List.of("CLIENTE");
                };
                session.setAttribute("rolesPermitidos", roles);
                session.setAttribute("tokenCambioRol", UUID.randomUUID().toString());
                session.setAttribute("logueado", true);
                redireccion.addFlashAttribute("success", "¡Bienvenido " + u.getNombre() + "!");
                if (roles.size() > 1) {
                    session.removeAttribute("rol");
                    return "redirect:/elegir-rol";
                }
                session.setAttribute("rol", "CLIENTE");
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

    @GetMapping("/elegir-rol")
    public String elegirRol(HttpSession session) {
        if (!(session.getAttribute("rolesPermitidos") instanceof List<?> roles) || roles.size() < 2) {
            return "redirect:/";
        }
        return "elegir-rol";
    }

    @PostMapping("/elegir-rol")
    public String cambiarRol(@RequestParam String rol, @RequestParam String token, HttpSession session) {
        if (!token.equals(session.getAttribute("tokenCambioRol"))
                || !(session.getAttribute("rolesPermitidos") instanceof List<?> roles)
                || roles.size() < 2 || !roles.contains(rol)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "El rol solicitado no está disponible");
        }
        session.setAttribute("rol", rol);
        return switch (rol) {
            case "ADMIN" -> "redirect:/administrador";
            case "GESTOR" -> "redirect:/gestor";
            default -> "redirect:/";
        };
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ================= Gestión de catálogo =================

    private void cargarCatalogoGestor(Model model) {
        model.addAttribute("productos", productoRepository.findAll());
        model.addAttribute("categorias", obtenerOCrearCategorias());
    }

    @PostMapping("/gestor/categorias")
    public String crearCategoria(@RequestParam String nombre, @RequestParam String token,
            HttpSession session, RedirectAttributes redireccion) {
        if (!token.equals(session.getAttribute("tokenCambioRol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Formulario inválido");
        }

        String nombreLimpio = nombre.strip();
        if (nombreLimpio.isEmpty() || nombreLimpio.length() > 50) {
            redireccion.addFlashAttribute("error", "El nombre debe tener entre 1 y 50 caracteres.");
        } else if (categoriaRepository.existsByNombreIgnoreCase(nombreLimpio)) {
            redireccion.addFlashAttribute("error", "Ya existe una categoría con ese nombre.");
        } else {
            Categoria categoria = new Categoria();
            categoria.setNombre(nombreLimpio);
            categoriaRepository.save(categoria);
            redireccion.addFlashAttribute("success", "Categoría creada correctamente.");
        }
        return "redirect:/gestor";
    }

    @PostMapping("/gestor/productos/guardar")
    public String guardarProducto(@Valid @ModelAttribute("nuevoProducto") Producto producto,
            BindingResult result,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            RedirectAttributes redireccion,
            Model model) {
        validarFormularioProducto(producto, categoriaId, result);

        if (result.hasErrors()) {
            model.addAttribute("error", "No se pudo agregar el producto. Revisa los campos señalados.");
            cargarCatalogoGestor(model);
            model.addAttribute("productoAEditar", new Producto());
            model.addAttribute("editando", false);
            model.addAttribute("abrirModalCrear", true);

            return "gestor";
        }

        try {
            productoRepository.save(producto);
            redireccion.addFlashAttribute("success", "Producto agregado correctamente");
            return "redirect:/gestor";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al guardar en la base de datos: " + e.getMessage());
            cargarCatalogoGestor(model);
            model.addAttribute("productoAEditar", new Producto());
            model.addAttribute("editando", false);
            model.addAttribute("abrirModalCrear", true);
            return "gestor";
        }
    }

    @PostMapping("/gestor/productos/eliminar/{id}")
    public String eliminarProducto(@PathVariable Long id, RedirectAttributes redireccion) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
            redireccion.addFlashAttribute("success", "Producto eliminado correctamente");
        } else {
            redireccion.addFlashAttribute("error", "El producto no existe");
        }
        return "redirect:/gestor";
    }

    @GetMapping("/gestor/productos/editar/{id}")
    public String editarProducto(@PathVariable Long id, Model model, RedirectAttributes redireccion) {
        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isEmpty()) {
            redireccion.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/gestor";
        }
        cargarCatalogoGestor(model);
        model.addAttribute("nuevoProducto", new Producto());
        model.addAttribute("productoAEditar", opt.get());
        model.addAttribute("editando", true);
        model.addAttribute("productoId", id);
        return "gestor";
    }

    @PostMapping("/gestor/productos/editar/{id}")
    public String actualizarProducto(@PathVariable Long id,
            @Valid @ModelAttribute("productoAEditar") Producto producto,
            BindingResult result,
            @RequestParam(value = "categoriaId", required = false) Long categoriaId,
            RedirectAttributes redireccion,
            Model model) {

        validarFormularioProducto(producto, categoriaId, result);

        if (result.hasErrors()) {
            model.addAttribute("error", "No se pudo actualizar el producto. Revisa los campos señalados.");
            cargarCatalogoGestor(model);
            model.addAttribute("nuevoProducto", new Producto());
            model.addAttribute("editando", true);
            model.addAttribute("productoId", id);
            return "gestor";
        }

        Optional<Producto> opt = productoRepository.findById(id);
        if (opt.isEmpty()) {
            redireccion.addFlashAttribute("error", "Producto no encontrado");
            return "redirect:/gestor";
        }

        Producto existente = opt.get();
        existente.setNombre(producto.getNombre());
        existente.setDescripcion(producto.getDescripcion());
        existente.setPrecio(producto.getPrecio());
        existente.setStock(producto.getStock());
        existente.setImagen(producto.getImagen());
        existente.setCategoria(producto.getCategoria());

        try {
            productoRepository.save(existente);
            redireccion.addFlashAttribute("success", "Producto actualizado correctamente");
            return "redirect:/gestor";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error al actualizar en la base de datos: " + e.getMessage());
            cargarCatalogoGestor(model);
            model.addAttribute("nuevoProducto", new Producto());
            model.addAttribute("editando", true);
            model.addAttribute("productoId", id);
            return "gestor";
        }
    }
    private void validarFormularioProducto(Producto producto, Long categoriaId, BindingResult result) {
        if (categoriaId == null) {
            result.rejectValue("categoria", "error.categoria", "Debe seleccionar una categoría");
        } else {
            categoriaRepository.findById(categoriaId).ifPresentOrElse(
                    producto::setCategoria,
                    () -> result.rejectValue("categoria", "error.categoria", "La categoría seleccionada no es válida"));
        }

        if (producto.getImagen() != null && producto.getImagen().isBlank()) {
            producto.setImagen(null);
        }
    }

}
