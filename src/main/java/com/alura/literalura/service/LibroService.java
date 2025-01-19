package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.model.Libro;
import com.alura.literalura.repository.AutorRepository;
import com.alura.literalura.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LibroService {
    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarLibros(){
        return libroRepository.findAll();
    }

    public List<Libro> listarLibrosPorIdioma(String idioma){
        return libroRepository.findByIdioma(idioma);
    }

    public Libro guardarLibro(Libro libro){
        Optional<Libro> libroExistente = this.buscarPorTitulo(libro.getTitulo());

        if(libroExistente.isPresent()){
            throw new RuntimeException("Ya existe un libro con el título " + libro.getTitulo());
        }

        return libroRepository.save(libro);
    }

    public Optional<Libro> buscarPorTitulo(String titulo){
        return libroRepository.findByTituloContainingIgnoreCase(titulo);
    }

}
