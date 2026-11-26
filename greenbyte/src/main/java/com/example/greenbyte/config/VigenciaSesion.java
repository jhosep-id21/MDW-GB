package com.example.greenbyte.config;

import com.example.greenbyte.models.User;
import com.example.greenbyte.repositorio.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.IOException;

@Configuration
public class VigenciaSesion implements WebMvcConfigurer, HandlerInterceptor {
    private final UserRepository usuarios;

    public VigenciaSesion(UserRepository usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (!(handler instanceof HandlerMethod) || session == null
                || !(session.getAttribute("usuario") instanceof String correo)) {
            return true;
        }

        response.setHeader("Cache-Control", "no-store");
        User usuario = usuarios.findByCorreo(correo).orElse(null);
        if (usuario != null) {
            char acceso = usuario.getAcceso() == null ? 'U' : usuario.getAcceso();
            if (Character.valueOf(acceso).equals(session.getAttribute("accesoCuenta"))) {
                return true;
            }
        }

        session.invalidate();
        response.sendRedirect(request.getContextPath() + "/");
        return false;
    }
}
