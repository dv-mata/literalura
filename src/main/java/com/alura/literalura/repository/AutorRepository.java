package com.alura.literalura.repository;

import com.alura.literalura.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AutorRepository extends JpaRepository<Autor, Long> {
    boolean existsByNombre(String nombre);

    @Query("""
            SELECT a from Autor a
            WHERE :anio BETWEEN a.fechaNacimiento AND a.fechaFallecimiento
            """)
    List<Autor> stillAlive(Integer anio);

    Optional<Autor> findByNombre(String nombre);
}

//1990