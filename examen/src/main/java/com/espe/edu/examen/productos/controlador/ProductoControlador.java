package com.espe.edu.examen.productos.controlador;

import com.espe.edu.examen.productos.excepcion.*;
import com.espe.edu.examen.productos.modelo.Producto;
import com.espe.edu.examen.productos.servicio.ProductoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoControlador {

    private final ProductoServicio productoServicio;

    public ProductoControlador(ProductoServicio productoServicio) {
        this.productoServicio = productoServicio;
    }

    @GetMapping
    public ResponseEntity<List<Producto>> listarTodos() {
        return ResponseEntity.ok(productoServicio.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            Producto producto = productoServicio.obtenerPorId(id);
            return ResponseEntity.ok(producto);
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody Producto producto) {
        try {
            Producto creado = productoServicio.crear(producto);
            return ResponseEntity.status(HttpStatus.CREATED).body(creado);
        } catch (CrearExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer id, @RequestParam String estado) {
        try {
            productoServicio.cambiar(id, estado);
            return ResponseEntity.noContent().build();
        } catch (ActualizarExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/aumentar-stock")
    public ResponseEntity<?> aumentarStock(
            @PathVariable Integer id,
            @RequestParam Integer cantidad,
            @RequestParam BigDecimal nuevoCostoCompra) {
        try {
            productoServicio.aumentarStock(id, cantidad, nuevoCostoCompra);
            return ResponseEntity.noContent().build();
        } catch (ActualizarExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{id}/disminuir-stock")
    public ResponseEntity<?> disminuirStock(@PathVariable Integer id, @RequestParam Integer cantidad) {
        try {
            productoServicio.disminuirStock(id, cantidad);
            return ResponseEntity.noContent().build();
        } catch (ActualizarExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
