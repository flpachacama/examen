package com.espe.edu.examen.productos.servicio;

import com.espe.edu.examen.productos.excepcion.NoEncontradoExcepcion;
import com.espe.edu.examen.productos.modelo.Producto;
import com.espe.edu.examen.productos.repositorio.ProductoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductoServicio {
    private final ProductoRepositorio productoRepositorio;

    public ProductoServicio(ProductoRepositorio productoRepositorio) {
        this.productoRepositorio = productoRepositorio;
    }

    public Producto obtenerPorId(Integer id) {
        return productoRepositorio.findById(id)
                .orElseThrow(() -> new NoEncontradoExcepcion("Producto", "No se encontró el producto con ID: " + id));
    }

    public List<Producto> obtenerTodos() {
        return productoRepositorio.findAll();
    }

    @Transactional
    public Producto crear(Producto producto) {
        if (producto.getCostoCompra() == null || producto.getCostoCompra().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El costo de compra debe ser un valor positivo.");
        }
        if (producto.getPrecioVenta() == null || producto.getPrecioVenta().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El precio de venta debe ser un valor positivo.");
        }
        if (producto.getStockActual() == null || producto.getStockActual() < 0) {
            throw new IllegalArgumentException("El stock actual no puede ser negativo.");
        }
        return productoRepositorio.save(producto);
    }

    @Transactional
    public void cambiar(Integer id, String nuevoEstado) {
        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            throw new IllegalArgumentException("El estado nuevo no puede estar vacío.");
        }

        Producto producto = obtenerPorId(id);
        producto.setEstado(nuevoEstado);
        productoRepositorio.save(producto);
    }

    @Transactional
    public void aumentarStock(Integer id, Integer cantidadIncremento, BigDecimal nuevoCostoCompra) {
        if (cantidadIncremento == null || cantidadIncremento <= 0) {
            throw new IllegalArgumentException("La cantidad a aumentar debe ser mayor que cero.");
        }
        if (nuevoCostoCompra == null || nuevoCostoCompra.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El nuevo costo de compra debe ser mayor que cero.");
        }

        Producto producto = obtenerPorId(id);

        producto.setStockActual(producto.getStockActual() + cantidadIncremento);
        producto.setCostoCompra(nuevoCostoCompra);

        BigDecimal nuevoPrecioVenta = nuevoCostoCompra.multiply(BigDecimal.valueOf(1.25));
        producto.setPrecioVenta(nuevoPrecioVenta);

        producto.setEstado("ACTIVO");

        productoRepositorio.save(producto);
    }

    @Transactional
    public void disminuirStock(Integer id, Integer cantidadDisminuir) {
        if (cantidadDisminuir == null || cantidadDisminuir <= 0) {
            throw new IllegalArgumentException("La cantidad a disminuir debe ser mayor que cero.");
        }

        Producto producto = obtenerPorId(id);

        int stockActual = producto.getStockActual();
        if (cantidadDisminuir > stockActual) {
            throw new IllegalArgumentException("La cantidad a disminuir excede el stock actual.");
        }

        producto.setStockActual(stockActual - cantidadDisminuir);
        productoRepositorio.save(producto);
    }
}
