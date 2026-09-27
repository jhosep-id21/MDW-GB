package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controlador {

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

    @GetMapping("/login")
    public String login() {
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout() {
        return "redirect:/";
    }
}
