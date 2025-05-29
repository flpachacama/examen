package com.espe.edu.examen.productos.servicio;

import com.espe.edu.examen.productos.excepcion.ActualizarExcepcion;
import com.espe.edu.examen.productos.excepcion.CrearExcepcion;
import com.espe.edu.examen.productos.excepcion.EliminarExcepcion;
import com.espe.edu.examen.productos.excepcion.NoEncontradoExcepcion;
import com.espe.edu.examen.productos.modelo.CategoriaProducto;
import com.espe.edu.examen.productos.repositorio.CategoriaProductoRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaProductoServicio {
    private final CategoriaProductoRepositorio categoriaProductoRepositorio;

    public CategoriaProductoServicio(CategoriaProductoRepositorio categoriaProductoRepositorio) {
        this.categoriaProductoRepositorio = categoriaProductoRepositorio;
    }

    public List<CategoriaProducto> buscarTodos() {
        return categoriaProductoRepositorio.findAll();
    }

    public CategoriaProducto buscarPorId(Integer id) {
        return categoriaProductoRepositorio.findById(id)
                .orElseThrow(() -> new NoEncontradoExcepcion("CategoriaProducto",
                        "No se encontró la categoría con ID: " + id));
    }

    @Transactional
    public void crear(CategoriaProducto categoria) {
        try {
            if (categoria.getNombre() == null || categoria.getNombre().trim().isEmpty()) {
                throw new CrearExcepcion("Nombre vacío o nulo", "CategoriaProducto");
            }
            Optional<CategoriaProducto> existente = categoriaProductoRepositorio.findByNombre(categoria.getNombre().trim());
            if (existente.isPresent()) {
                throw new CrearExcepcion("Ya existe una categoría con el nombre: " + categoria.getNombre(), "CategoriaProducto");
            }
            categoriaProductoRepositorio.save(categoria);
        } catch (RuntimeException e) {
            throw new CrearExcepcion("Error al crear la categoría. Texto: " + e.getMessage(), "CategoriaProducto");
        }
    }

    @Transactional
    public void actualizar(CategoriaProducto categoria) {
        try {
            Optional<CategoriaProducto> categoriaOptional = categoriaProductoRepositorio.findById(categoria.getId());

            if (categoriaOptional.isPresent()) {
                CategoriaProducto categoriaDB = categoriaOptional.get();

                if (categoria.getNombre() != null) {
                    if (categoria.getNombre().trim().isEmpty()) {
                        throw new ActualizarExcepcion("Nombre vacío", "CategoriaProducto");
                    }
                    Optional<CategoriaProducto> existente = categoriaProductoRepositorio.findByNombre(categoria.getNombre().trim());
                    if (existente.isPresent() && !existente.get().getId().equals(categoria.getId())) {
                        throw new ActualizarExcepcion("Ya existe otra categoría con el nombre: " + categoria.getNombre(), "CategoriaProducto");
                    }
                    categoriaDB.setNombre(categoria.getNombre());
                }

                if (categoria.getDescripcion() != null) {
                    categoriaDB.setDescripcion(categoria.getDescripcion());
                }

                categoriaProductoRepositorio.save(categoriaDB);
            } else {
                throw new ActualizarExcepcion(String.valueOf(categoria.getId()), "CategoriaProducto");
            }
        } catch (RuntimeException e) {
            throw new ActualizarExcepcion("Error al actualizar la categoría: " + e.getMessage(), "CategoriaProducto");
        }
    }

    @Transactional
    public void eliminar(Integer id) {
        try {
            Optional<CategoriaProducto> categoriaOptional = categoriaProductoRepositorio.findById(id);
            if (categoriaOptional.isPresent()) {
                categoriaProductoRepositorio.deleteById(id);
            } else {
                throw new EliminarExcepcion(String.valueOf(id), "CategoriaProducto");
            }
        } catch (RuntimeException e) {
            throw new EliminarExcepcion("Error al eliminar la categoría: " + e.getMessage(), "CategoriaProducto");
        }
    }
}
