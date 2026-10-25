package com.example.greenbyte.controllers;

import com.example.greenbyte.models.User;
import com.example.greenbyte.repo.UserRepository;
import java.util.List;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class ControladorBD {
    private final JdbcTemplate jdbcTemplate;
    private final UserRepository userRepository;

    public ControladorBD(JdbcTemplate jdbcTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.userRepository = userRepository;
    }

    @GetMapping("/db-test")
    public String test() {
        jdbcTemplate.queryForObject("SELECT 1", Integer.class);
        return "Conexión a la base de datos exitosa";
    }

    @GetMapping("/usuario")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/crear")
    public String saveUser(@RequestBody User user) {
        userRepository.save(user);
        return "Guardado correctamente";
    }
}
