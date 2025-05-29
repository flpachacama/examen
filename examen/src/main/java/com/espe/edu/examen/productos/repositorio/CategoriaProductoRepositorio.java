package com.espe.edu.examen.productos.repositorio;

import com.espe.edu.examen.productos.modelo.CategoriaProducto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaProductoRepositorio extends JpaRepository<CategoriaProducto, Integer> {
    Optional<CategoriaProducto> findByNombre(String nombre);
}
