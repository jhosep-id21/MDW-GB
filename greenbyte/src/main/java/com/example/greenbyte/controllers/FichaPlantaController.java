package com.example.greenbyte.controllers;

import com.example.greenbyte.models.FichaPlanta;
import com.example.greenbyte.repositorio.FichaPlantaRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/gestor/fichas")
public class FichaPlantaController {
    private final FichaPlantaRepository fichas;

    public FichaPlantaController(FichaPlantaRepository fichas) {
        this.fichas = fichas;
    }

    @ModelAttribute
    public void exigirGestor(HttpSession session) {
        if (!"GESTOR".equals(session.getAttribute("rol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
    }

    @InitBinder("ficha")
    public void camposDelFormulario(WebDataBinder binder) {
        binder.setAllowedFields("nombre", "tipo", "descripcion", "luz", "riego", "notas", "activa");
    }

    @GetMapping
    public String mostrar(@RequestParam(required = false) Long id, Model model) {
        model.addAttribute("ficha", id == null ? new FichaPlanta() : buscarFicha(id));
        return mostrarFormulario(model, id);
    }

    @PostMapping
    public String guardar(@Valid @ModelAttribute("ficha") FichaPlanta formulario,
                          BindingResult errores, @RequestParam(required = false) Long id,
                          @RequestParam String token, HttpSession session, Model model,
                          RedirectAttributes redireccion) {
        if (!token.equals(session.getAttribute("tokenCambioRol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }
        if (errores.hasErrors()) {
            return mostrarFormulario(model, id);
        }
        FichaPlanta ficha = id == null ? new FichaPlanta() : buscarFicha(id);
        ficha.setNombre(formulario.getNombre().strip());
        ficha.setTipo(formulario.getTipo().strip());
        ficha.setDescripcion(formulario.getDescripcion().strip());
        ficha.setLuz(formulario.getLuz().strip());
        ficha.setRiego(formulario.getRiego().strip());
        ficha.setNotas(formulario.getNotas().strip());
        ficha.setActiva(formulario.isActiva());
        fichas.save(ficha);
        redireccion.addFlashAttribute("success", "Ficha guardada");
        return "redirect:/gestor/fichas";
    }

    private FichaPlanta buscarFicha(Long id) {
        return fichas.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ficha no encontrada"));
    }

    private String mostrarFormulario(Model model, Long id) {
        model.addAttribute("fichaId", id);
        model.addAttribute("fichas", fichas.findAllByOrderByNombreAsc());
        return "gestor-fichas";
    }
}
