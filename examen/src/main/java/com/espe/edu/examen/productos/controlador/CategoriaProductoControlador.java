package com.espe.edu.examen.productos.controlador;

import com.espe.edu.examen.productos.excepcion.*;
import com.espe.edu.examen.productos.modelo.CategoriaProducto;
import com.espe.edu.examen.productos.servicio.CategoriaProductoServicio;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
public class CategoriaProductoControlador {

    private final CategoriaProductoServicio categoriaProductoServicio;

    public CategoriaProductoControlador(CategoriaProductoServicio categoriaProductoServicio) {
        this.categoriaProductoServicio = categoriaProductoServicio;
    }

    @GetMapping
    public ResponseEntity<List<CategoriaProducto>> listarTodos() {
        return ResponseEntity.ok(categoriaProductoServicio.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPorId(@PathVariable Integer id) {
        try {
            CategoriaProducto categoria = categoriaProductoServicio.buscarPorId(id);
            return ResponseEntity.ok(categoria);
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody CategoriaProducto categoria) {
        try {
            categoriaProductoServicio.crear(categoria);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (CrearExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PatchMapping
    public ResponseEntity<?> actualizar(@RequestBody CategoriaProducto categoria) {
        try {
            categoriaProductoServicio.actualizar(categoria);
            return ResponseEntity.noContent().build();
        } catch (ActualizarExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        try {
            categoriaProductoServicio.eliminar(id);
            return ResponseEntity.noContent().build();
        } catch (EliminarExcepcion e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NoEncontradoExcepcion e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
