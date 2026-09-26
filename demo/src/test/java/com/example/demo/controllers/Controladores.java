package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;

public class Controladores {
    @GetMapping("/index")
    public String index() {
        return "index";
    } 
}
