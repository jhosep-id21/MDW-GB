package com.example.demo.controllers;

import com.example.demo.model.Producto;
import com.example.demo.service.ProductoService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Controlador {

    private final ProductoService productoService;

    public Controlador(ProductoService productoService) {
        this.productoService = productoService;
    }

    // --- Vistas principales (solo GetMapping para la presentación) ---
    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/accesorios")
    public String mostrarAccesorios(Model model) {
        model.addAttribute("productos", productoService.findByCategoria("accesorios"));
        return "accesorios";
    }

    @GetMapping("/contactanos")
    public String mostrarContactanos() {
        return "contactanos";
    }

    @GetMapping("/exterior")
    public String mostrarExterior(Model model) {
        model.addAttribute("productos", productoService.findByCategoria("exterior"));
        return "exterior";
    }

    @GetMapping("/interior")
    public String mostrarInterior(Model model) {
        model.addAttribute("productos", productoService.findByCategoria("interior"));
        return "interior";
    }

    @GetMapping("/macetas")
    public String mostrarMacetas(Model model) {
        model.addAttribute("productos", productoService.findByCategoria("macetas"));
        return "macetas";
    }

    @GetMapping("/principiantes")
    public String mostrarPrincipiantes(Model model) {
        model.addAttribute("productos", productoService.findByCategoria("principiantes"));
        return "principiantes";
    }

    // --- Login / Logout / Rol (solo GetMapping) ---
    // Uso: /login?email=admin@greenbyte.com&password=xxx  -> ADMIN, resto USER
    @GetMapping("/login")
    public String login(@RequestParam(required = false) String email,
                        @RequestParam(required = false) String password,
                        HttpSession session,
                        RedirectAttributes redirect) {
        if (email == null || email.isBlank()) {
            redirect.addFlashAttribute("error", "Ingresa un correo");
            return "redirect:/";
        }
        // password opcional para la demo, solo valida que no esté vacío si se envía
        String rol = email.trim().equalsIgnoreCase("admin@greenbyte.com") ? "ADMIN" : "USER";
        session.setAttribute("usuario", email.trim());
        session.setAttribute("rol", rol);
        session.setAttribute("logueado", true);

        if ("ADMIN".equals(rol)) {
            return "redirect:/administrador";
        }
        redirect.addFlashAttribute("success", "¡Bienvenido " + email + "!");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- Administrador (solo GetMapping) ---
    @GetMapping("/administrador")
    public String administrador(HttpSession session, Model model) {
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            model.addAttribute("noAutorizado", true);
        }
        model.addAttribute("productos", productoService.findAll());
        model.addAttribute("totalProductos", productoService.count());
        model.addAttribute("totalInterior", productoService.countByCategoria("interior"));
        model.addAttribute("totalExterior", productoService.countByCategoria("exterior"));
        model.addAttribute("totalMacetas", productoService.countByCategoria("macetas"));
        model.addAttribute("totalAccesorios", productoService.countByCategoria("accesorios"));
        model.addAttribute("totalPrincipiantes", productoService.countByCategoria("principiantes"));
        return "administrador";
    }

    // Demo de gestión sin BD - todo vía GetMapping para cumplir requisito del profesor
    @GetMapping("/admin/productos/agregar")
    public String agregarProducto(@RequestParam String nombre,
                                  @RequestParam String descripcion,
                                  @RequestParam double precio,
                                  @RequestParam String categoria,
                                  @RequestParam String imagen,
                                  HttpSession session,
                                  RedirectAttributes redirect) {
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            redirect.addFlashAttribute("error", "No autorizado - solo ADMIN");
            return "redirect:/administrador";
        }
        Producto p = new Producto(null, nombre, descripcion, precio, imagen, categoria.toLowerCase(), nombre);
        productoService.addProducto(p);
        redirect.addFlashAttribute("success", "Producto agregado: " + nombre);
        return "redirect:/administrador";
    }

    @GetMapping("/admin/productos/eliminar")
    public String eliminarProducto(@RequestParam String id, HttpSession session, RedirectAttributes redirect) {
        String rol = (String) session.getAttribute("rol");
        if (!"ADMIN".equals(rol)) {
            redirect.addFlashAttribute("error", "No autorizado");
            return "redirect:/administrador";
        }
        boolean ok = productoService.removeById(id);
        redirect.addFlashAttribute(ok ? "success" : "error", ok ? "Producto eliminado" : "No se encontró el producto");
        return "redirect:/administrador";
    }
}
