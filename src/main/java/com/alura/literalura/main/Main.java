package com.alura.literalura.main;

import com.alura.literalura.model.*;
import com.alura.literalura.service.AutorService;
import com.alura.literalura.service.ConsumoAPI;
import com.alura.literalura.service.ConvierteDatos;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.AbstractResource;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private Scanner sc = new Scanner(System.in);
    private ConsumoAPI consumoApi = new ConsumoAPI();
    private final String URL_BASE = "https://gutendex.com";
    private ConvierteDatos conversor = new ConvierteDatos();

    private LibroService libroService;

    private AutorService autorService;

    public Main(LibroService libroService, AutorService autorService) {
        this.libroService = libroService;
        this.autorService = autorService;
    }

    public void show() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    Elija la opción a través de su número:
                    1- Buscar libro por titulo
                    2- Listar Libros registrados
                    3- Listar autores registrados
                    4- Listar autores vivos en un determinado año
                    5- Listar Libros por idioma
                    0 - salir
                    """;
            System.out.println(menu);
            opcion = sc.nextInt();
            sc.nextLine();

            switch (opcion) {
                case 1:
                    buscarLibroPorTitulo();
                    break;
                case 2:
                    listarLibrosRegistrados();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosPorFecha();
                    break;
                case 5:
                    listarLibrosPorIdioma();
                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        }

    }

    private void listarLibrosPorIdioma() {
        int choice = 0;

        String lang = "en"; // default
        do {
                System.out.println("""
                Elige el idioma a buscar:
                1. EN
                2. ES
                3. IT
                4. FR
                """);
                choice = sc.nextInt();

                switch (choice) {
                    case 2 -> lang = "es";
                    case 3 -> lang = "it";
                    case 4 -> lang = "fr";
                }
    } while ((choice < 0 || choice > 4));

        List<Libro> libros = libroService.listarLibrosPorIdioma(lang);

        if (libros.isEmpty()){
            System.out.println("No hay libros registrados con ese idioma");
        } else{
            libros.forEach(System.out::println);
        }
    }

    private void listarAutoresVivosPorFecha() {
        System.out.println("Ingresa el año a buscar: ");
        Integer anio = sc.nextInt();

        List<Autor> listaAutores = autorService.listarAutoresVivosPorFecha(anio);

        listaAutores.forEach(System.out::println);
    }

    private void listarAutoresRegistrados() {
        List<Autor> listaAutores = autorService.listarAutores();

        listaAutores.forEach(System.out::println);
    }

    private void listarLibrosRegistrados() {
        List<Libro> listaLibros = libroService.listarLibros();

        listaLibros.forEach(System.out::println);
    }


    // TODO: Buscar libros por titulo en el repositorio, si existe ya no hago una busqueda en el API
    private void buscarLibroPorTitulo(){
        System.out.println("Ingresa el titulo del libro a buscar: ");
        String titulo = sc.nextLine();

        var libro = libroService.buscarPorTitulo(titulo);

        if (libro.isPresent()){
            System.out.println("El libro fue encontrado en la base de datos.");
            System.out.println(libro.get());
        } else{
            var json = consumoApi.obtenerDatos(URL_BASE + "/books/?search=" + titulo.replace(" ", "+"));

            Datos datos = conversor.obtenerDatos(json,  Datos.class);

            System.out.println(datos);

            Optional<DTOLibro> DTOLibroBuscado = datos.resultados().stream()
                    .filter(l -> l.titulo().toUpperCase().contains(titulo.toUpperCase()))
                    .findFirst();

            if (DTOLibroBuscado.isPresent()) {
                DTOAutor datosAutor = DTOLibroBuscado.get().autor().get(0);

                Libro nuevoLibro = new Libro(DTOLibroBuscado.get());
                Autor autor = new Autor(datosAutor.nombre(),datosAutor.fechaNacimiento(), datosAutor.fechaFallecimiento());

                // Validar si el autor ya existe
                var autorPersistido = autorService.buscarPorNombre(autor.getNombre());

                if(autorPersistido.isPresent()){
                    nuevoLibro.setAutor(autorPersistido.get());
                } else{
                    nuevoLibro.setAutor(autor);
                    autorService.guardarAutor(autor);
                }

                libroService.guardarLibro(nuevoLibro);


            } else {
                System.out.println("Libro no encontrado");
            }
        }

    }
}
