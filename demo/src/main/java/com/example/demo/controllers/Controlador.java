package com.example.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Controlador {

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

    @GetMapping("/index")
    public String mostrarIndex() {
        return "index";
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

}
