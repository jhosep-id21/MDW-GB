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

    @GetMapping("/login")
    public String login(@RequestParam(required = false) String email, @RequestParam(required = false) String password, HttpSession session, RedirectAttributes redirect) {
        if (email == null || email.isBlank() || password == null || password.isBlank()) {
            redirect.addFlashAttribute("error", "Ingresa correo y contraseña");
            return "redirect:/";
        }
        email = email.trim();
        if (email.equalsIgnoreCase("admin@greenbyte.com")) {
            if (!password.equals("admin123")) {
                redirect.addFlashAttribute("error", "Contraseña de administrador incorrecta (usa admin123)");
                return "redirect:/";
            }
            session.setAttribute("usuario", email);
            session.setAttribute("nombre", "Admin");
            session.setAttribute("rol", "ADMIN");
            session.setAttribute("logueado", true);
            return "redirect:/administrador";
        }
        String base = email.contains("@") ? email.substring(0, email.indexOf('@')) : email;
        String nombre = base.length() > 12 ? base.substring(0, 12) + "…" : base;
        session.setAttribute("usuario", email);
        session.setAttribute("nombre", nombre);
        session.setAttribute("rol", "USER");
        session.setAttribute("logueado", true);
        redirect.addFlashAttribute("success", "¡Bienvenido " + nombre + "!");
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
