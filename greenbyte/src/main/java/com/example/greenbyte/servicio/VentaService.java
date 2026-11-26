package com.example.greenbyte.servicio;

import com.example.greenbyte.models.DetalleVenta;
import com.example.greenbyte.models.Producto;
import com.example.greenbyte.models.User;
import com.example.greenbyte.models.Venta;
import com.example.greenbyte.repositorio.ProductoRepository;
import com.example.greenbyte.repositorio.UserRepository;
import com.example.greenbyte.repositorio.VentaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class VentaService {
    public record Articulo(Long id, Integer cantidad) {}

    private final VentaRepository ventas;
    private final ProductoRepository productos;
    private final UserRepository usuarios;

    public VentaService(VentaRepository ventas, ProductoRepository productos, UserRepository usuarios) {
        this.ventas = ventas;
        this.productos = productos;
        this.usuarios = usuarios;
    }

    @Transactional
    public Venta confirmar(String correo, String codigo, List<Articulo> articulos) {
        if (articulos == null || articulos.isEmpty() || articulos.size() > 100) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Revisa los productos del carrito.");
        }
        Map<Long, Integer> cantidades = new TreeMap<>();
        for (Articulo articulo : articulos) {
            if (articulo == null || articulo.id() == null || articulo.id() < 1
                    || articulo.cantidad() == null || articulo.cantidad() < 1 || articulo.cantidad() > 1000) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Revisa las cantidades del carrito.");
            }
            cantidades.merge(articulo.id(), articulo.cantidad(), Integer::sum);
        }

        User cliente = usuarios.buscarClienteParaVenta(correo)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN, "Inicia sesión para continuar."));
        Venta existente = ventas.findByCodigoCompraAndClienteId(codigo, cliente.getId()).orElse(null);
        if (existente != null) {
            return existente;
        }

        Venta venta = new Venta();
        venta.setCliente(cliente);
        venta.setCodigoCompra(codigo);
        venta.setFecha(LocalDateTime.now(ZoneId.of("America/Lima")));
        BigDecimal total = BigDecimal.ZERO;

        for (var entrada : cantidades.entrySet()) {
            Producto producto = productos.buscarParaVenta(entrada.getKey())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Un producto ya no está disponible."));
            int cantidad = entrada.getValue();
            if (producto.getStock() < cantidad) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Stock insuficiente para " + producto.getNombre() + ".");
            }

            DetalleVenta detalle = new DetalleVenta();
            detalle.setVenta(venta);
            detalle.setProductoId(producto.getId());
            detalle.setNombreProducto(producto.getNombre());
            detalle.setCantidad(cantidad);
            detalle.setPrecioUnitario(producto.getPrecio());
            venta.getDetalles().add(detalle);
            total = total.add(producto.getPrecio().multiply(BigDecimal.valueOf(cantidad)));
            producto.setStock(producto.getStock() - cantidad);
        }

        venta.setTotal(total);
        return ventas.save(venta);
    }
}
