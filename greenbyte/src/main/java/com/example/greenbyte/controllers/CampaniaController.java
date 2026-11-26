package com.example.greenbyte.controllers;

import com.example.greenbyte.models.Campania;
import com.example.greenbyte.models.Categoria;
import com.example.greenbyte.repositorio.CampaniaRepository;
import com.example.greenbyte.repositorio.CategoriaRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/gestor/campanias")
public class CampaniaController {

    private final CampaniaRepository campanias;
    private final CategoriaRepository categorias;

    public CampaniaController(CampaniaRepository campanias, CategoriaRepository categorias) {
        this.campanias = campanias;
        this.categorias = categorias;
    }

    @ModelAttribute
    public void exigirGestor(HttpSession session) {
        if (!"GESTOR".equals(session.getAttribute("rol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Se requiere el rol de gestor");
        }
    }

    @InitBinder("campania")
    public void camposDelFormulario(WebDataBinder binder) {
        binder.setAllowedFields("titulo", "mensaje", "inicio", "fin", "activa");
    }

    @GetMapping
    public String nueva(Model model) {
        model.addAttribute("campania", new Campania());
        return mostrarFormulario(model, null, null);
    }

    @GetMapping("/{id}")
    public String editar(@PathVariable Long id, Model model) {
        Campania campania = buscarCampania(id);
        model.addAttribute("campania", campania);
        Long categoriaId = campania.getCategoria() != null ? campania.getCategoria().getId() : null;
        return mostrarFormulario(model, id, categoriaId);
    }

    @PostMapping
    public String crear(@Valid @ModelAttribute("campania") Campania formulario,
                        BindingResult errores, @RequestParam(required = false) Long categoriaId,
                        @RequestParam String token, HttpSession session, Model model,
                        RedirectAttributes redireccion) {
        return guardar(null, formulario, errores, categoriaId, token, session, model, redireccion);
    }

    @PostMapping("/{id}")
    public String actualizar(@PathVariable Long id,
                             @Valid @ModelAttribute("campania") Campania formulario,
                             BindingResult errores, @RequestParam(required = false) Long categoriaId,
                             @RequestParam String token, HttpSession session, Model model,
                             RedirectAttributes redireccion) {
        return guardar(id, formulario, errores, categoriaId, token, session, model, redireccion);
    }

    private String guardar(Long id, Campania formulario, BindingResult errores, Long categoriaId,
                           String token, HttpSession session, Model model, RedirectAttributes redireccion) {
        if (!token.equals(session.getAttribute("tokenCambioRol"))) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Formulario inválido");
        }

        if (formulario.getInicio() != null && formulario.getFin() != null
                && formulario.getFin().isBefore(formulario.getInicio())) {
            errores.rejectValue("fin", "campania.fechas", "El fin debe ser igual o posterior al inicio");
        }
        Categoria destino = null;
        if (categoriaId != null) {
            destino = categorias.findById(categoriaId).orElse(null);
            if (destino == null) {
                errores.reject("campania.categoria", "Selecciona una categoría válida");
            }
        }
        Campania campania = id == null ? new Campania() : buscarCampania(id);
        if (errores.hasErrors()) {
            return mostrarFormulario(model, id, categoriaId);
        }

        campania.setTitulo(formulario.getTitulo().trim());
        campania.setMensaje(formulario.getMensaje().trim());
        campania.setInicio(formulario.getInicio());
        campania.setFin(formulario.getFin());
        campania.setCategoria(destino);
        campania.setActiva(formulario.isActiva());
        campanias.save(campania);
        redireccion.addFlashAttribute("success", "Campaña guardada");
        return "redirect:/gestor/campanias";
    }

    private Campania buscarCampania(Long id) {
        return campanias.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Campaña no encontrada"));
    }

    private String mostrarFormulario(Model model, Long id, Long categoriaId) {
        model.addAttribute("campaniaId", id);
        model.addAttribute("categoriaId", categoriaId);
        model.addAttribute("categorias", categorias.findAll());
        model.addAttribute("campanias", campanias.findAllByOrderByInicioDescIdDesc());
        return "campanias";
    }
}
