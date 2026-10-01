package com.example.demo.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class Controladores {

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @GetMapping("/accesorios")
    public String mostrarAccesorios() {
        return "accesorios";
    }

    @GetMapping("/administrador")
    public String mostrarAdministrador() {
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

    @GetMapping("/iniciar-sesion")
    public String iniciarSesion(@RequestParam(required = false) String correo,
            @RequestParam(required = false) String clave, HttpSession session, RedirectAttributes redireccion) {
        if (correo == null || correo.isBlank() || clave == null || clave.isBlank()) {
            redireccion.addFlashAttribute("error", "Ingresa correo y contraseña");
            return "redirect:/";
        }
        correo = correo.trim();
        if (correo.equalsIgnoreCase("admin@greenbyte.com")) {
            if (!clave.equals("admin123")) {
                redireccion.addFlashAttribute("error", "Contraseña de administrador incorrecta");
                return "redirect:/";
            }
            session.setAttribute("usuario", correo);
            session.setAttribute("nombre", "Admin");
            session.setAttribute("rol", "ADMIN");
            session.setAttribute("logueado", true);
            return "redirect:/administrador";
        }
        String prefijo = correo.contains("@") ? correo.substring(0, correo.indexOf('@')) : correo;
        String nombre = prefijo.length() > 12 ? prefijo.substring(0, 12) + "…" : prefijo;
        session.setAttribute("usuario", correo);
        session.setAttribute("nombre", nombre);
        session.setAttribute("rol", "USER");
        session.setAttribute("logueado", true);
        redireccion.addFlashAttribute("success", "¡Bienvenido " + nombre + "!");
        return "redirect:/";
    }

    @GetMapping("/cerrar-sesion")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
