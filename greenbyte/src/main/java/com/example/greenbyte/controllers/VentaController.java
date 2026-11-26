package com.example.greenbyte.controllers;

import com.example.greenbyte.models.Venta;
import com.example.greenbyte.repositorio.VentaRepository;
import com.example.greenbyte.servicio.VentaService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Controller
public class VentaController {
    public record SolicitudCompra(UUID codigo, List<VentaService.Articulo> articulos) {}
    public record CompraConfirmada(Long id, BigDecimal total) {}

    private final VentaService servicio;
    private final VentaRepository ventas;

    public VentaController(VentaService servicio, VentaRepository ventas) {
        this.servicio = servicio;
        this.ventas = ventas;
    }

    @PostMapping("/compras")
    @ResponseBody
    public ResponseEntity<?> confirmar(@RequestBody SolicitudCompra compra,
                                       @RequestHeader(value = "X-Token-Sesion", required = false) String token,
                                       HttpSession session) {
        if (!"CLIENTE".equals(session.getAttribute("rol"))
                || !(session.getAttribute("usuario") instanceof String correo)
                || token == null || !token.equals(session.getAttribute("tokenCambioRol"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("mensaje", "Inicia sesión con el rol Cliente para confirmar la compra."));
        }
        if (compra.codigo() == null) {
            return ResponseEntity.badRequest().body(Map.of("mensaje", "No se pudo identificar la compra."));
        }

        try {
            Venta venta = servicio.confirmar(correo, compra.codigo().toString(), compra.articulos());
            return ResponseEntity.ok(new CompraConfirmada(venta.getId(), venta.getTotal()));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(Map.of("mensaje", e.getReason()));
        }
    }

    @GetMapping("/administrador/ventas")
    public String mostrarVentas(@RequestParam(required = false) Long id, HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("rol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        model.addAttribute("ventas", ventas.findAllByOrderByIdDesc());
        if (id != null) {
            model.addAttribute("ventaSeleccionada", ventas.findOneById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
        }
        return "ventas";
    }
}
