package com.alura.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record DTOLibro(
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DTOAutor> autor,
        @JsonAlias("languages") List<String> languages,
        @JsonAlias("download_count") Double descargas
) {
    public DTOLibro(String titulo, List<DTOAutor> autor, List<String> languages, Double descargas) {
        this.titulo = titulo;
        this.autor = autor != null && !autor.isEmpty() ? List.of(autor.get(0)) : List.of();  // Solo el primer autor
        this.languages = languages;
        this.descargas = descargas;
    }
}
