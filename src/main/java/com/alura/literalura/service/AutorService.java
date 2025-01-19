package com.alura.literalura.service;

import com.alura.literalura.model.Autor;
import com.alura.literalura.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService {
    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarAutores(){
        return autorRepository.findAll();
    }

    public Optional<Autor> buscarPorNombre(String nombre){
        return autorRepository.findByNombre(nombre);
    }

    public List<Autor> listarAutoresVivosPorFecha(Integer anio){
        return autorRepository.stillAlive(anio);
    }

    public void guardarAutor(Autor autor) {
        autorRepository.save(autor);
    }
}
