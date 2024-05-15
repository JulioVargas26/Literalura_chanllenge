package alura.challenge.literalura.principal;

import alura.challenge.literalura.model.*;
import alura.challenge.literalura.repository.IAutorRepository;
import alura.challenge.literalura.service.ConsumoAPI;
import alura.challenge.literalura.service.ConvierteDatos;

import java.util.*;
import java.util.stream.Collectors;

public class Principal {
    private static final String URL_BASE = "https://gutendex.com/books/";
    private final ConsumoAPI consumoAPI = new ConsumoAPI();
    private final ConvierteDatos conversor = new ConvierteDatos();
    private final Scanner teclado = new Scanner(System.in);
    private IAutorRepository repo;

    public Principal(IAutorRepository repository){
        this.repo = repository;
    }

    public void mostrarMenu() {
        var opcion = -1;
        var menu = """
                -------------
                Elija la opcion a través de su numero:
                1 - Buscar libro por titulo
                2 - Listar libros registrados
                3 - Listar autores registrados
                4 - Listar autores vivos en un determinado año
                5 - Listar libros por idioma
                6 - Generar estadisticas
                7 - Top 10 libros
                8 - Buscar autor por nombre
                9 - Listar autores con otras consultas
                0 - Salir
                """;
        while (opcion != 0) {
            System.out.println(menu);
            try {
                opcion = Integer.valueOf(teclado.nextLine());
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
                        listarAutoresVivos();
                        break;
                    case 5:
                        listarLibrosPorIdioma();
                        break;
                    case 6:
                        generarEstadisticas();
                        break;
                    case 7:
                        top10Libros();
                        break;
                    case 8:
                        buscarAutorPorNombre();
                        break;
                    case 9:
                        listarAutoresConOtrasConsultas();
                        break;
                    case 0:
                        System.out.println("Gracias por usar LiterAlura");
                        System.out.println("Cerrando la aplicacion...");
                        break;
                    default:
                        System.out.println("Opcion no valida!");
                        break;
                }
            } catch (NumberFormatException e) {
                System.out.println("Opcion no valida: " + e.getMessage());

            }
        }
    }

    public void buscarLibroPorTitulo(){
        System.out.println("Introduce el nombre del libro que deseas buscar:");
        var nombre = teclado.nextLine();
        var json = consumoAPI.obtenerDatos(URL_BASE + "?search=" + nombre.replace(" ","+"));
        var datos = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datos.resultados().stream()
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println(
                    "\n----- LIBRO -----" +
                            "\nTitulo: " + libroBuscado.get().titulo() +
                            "\nAutor: " + libroBuscado.get().autor().stream()
                            .map(a -> a.nombre()).limit(1).collect(Collectors.joining())+
                            "\nIdioma: " + libroBuscado.get().idiomas().stream().collect(Collectors.joining()) +
                            "\nNumero de descargas: " + libroBuscado.get().numeroDeDescargas() +
                            "\n-----------------\n"
            );

            try{
                List<Libro> libroEncontrado = libroBuscado.stream().map(a -> new Libro(a)).collect(Collectors.toList());
                Autor autorAPI = libroBuscado.stream().
                        flatMap(l -> l.autor().stream()
                                .map(a -> new Autor(a)))
                        .collect(Collectors.toList()).stream().findFirst().get();
                Optional<Autor> autorBD = repo.buscarAutorPorNombre(libroBuscado.get().autor().stream()
                        .map(a -> a.nombre())
                        .collect(Collectors.joining()));
                Optional<Libro> libroOptional = repo.buscarLibroPorNombre(nombre);
                if (libroOptional.isPresent()) {
                    System.out.println("El libro ya está guardado en la base de datos.");
                } else {
                    Autor autor;
                    if (autorBD.isPresent()) {
                        autor = autorBD.get();
                        System.out.println("EL autor ya esta guardado en la BD!");
                    } else {
                        autor = autorAPI;
                        repo.save(autor);
                    }
                    autor.setLibros(libroEncontrado);
                    repo.save(autor);
                }
            } catch(Exception e) {
                System.out.println("Advertencia! " + e.getMessage());
            }
        } else {
            System.out.println("Libro no encontrado!");
        }
    }

    public void listarLibrosRegistrados(){
        List<Libro> libros = repo.buscarTodosLosLibros();
        libros.forEach(l -> System.out.println(
                "----- LIBRO -----" +
                        "\nTitulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getLenguaje().getIdioma() +
                        "\nNumero de descargas: " + l.getDescarga() +
                        "\n-----------------\n"
        ));
    }

    public void listarAutoresRegistrados(){
        List<Autor> autores = repo.findAll();
        System.out.println();
        autores.forEach(l-> System.out.println(
                "Autor: " + l.getNombre() +
                        "\nFecha de nacimiento: " + l.getNacimiento() +
                        "\nFecha de fallecimiento: " + l.getFallecimiento() +
                        "\nLibros: " + l.getLibros().stream()
                        .map(t -> t.getTitulo()).collect(Collectors.toList()) + "\n"
        ));
    }

    public void listarAutoresVivos(){
        System.out.println("Introduce el año vivo del autor(es) que deseas buscar:");
        try{
            var fecha = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repo.buscarAutoresVivos(fecha);
            if(!autores.isEmpty()){
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNombre() +
                                "\nFecha de nacimiento: " + a.getNacimiento() +
                                "\nFecha de fallecimiento: " + a.getFallecimiento() +
                                "\nLibros: " + a.getLibros().stream()
                                .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            } else{
                System.out.println("No hay autores vivos en ese año registradoe en la BD!");
            }
        } catch(NumberFormatException e){
            System.out.println("introduce un año valido " + e.getMessage());
        }
    }

    public void listarLibrosPorIdioma(){
        var menu = """
                Ingrese el idioma para buscar los libros:
                es - español
                en - inglés
                fr - francés
                pt - portugués
                """;
        System.out.println(menu);
        var idioma = teclado.nextLine();
        if(idioma.equalsIgnoreCase("es") || idioma.equalsIgnoreCase("en") ||
                idioma.equalsIgnoreCase("fr") || idioma.equalsIgnoreCase("pt")){
            Lenguaje lenguaje = Lenguaje.fromString(idioma);
            List<Libro> libros = repo.buscarLibrosPorIdioma(lenguaje);
            if(libros.isEmpty()){
                System.out.println("No hay libros registrados en ese idioma!");
            } else{
                System.out.println();
                libros.forEach(l -> System.out.println(
                        "----- LIBRO -----" +
                                "\nTitulo: " + l.getTitulo() +
                                "\nAutor: " + l.getAutor().getNombre() +
                                "\nIdioma: " + l.getLenguaje().getIdioma() +
                                "\nNumero de descargas: " + l.getDescarga() +
                                "\n-----------------\n"
                ));
            }
        } else{
            System.out.println("Introduce un idioma en el formato valido");
        }
    }

    public void generarEstadisticas(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        var datos = conversor.obtenerDatos(json, Datos.class);
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(l -> l.numeroDeDescargas() > 0.0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        Integer media = (int) est.getAverage();
        System.out.println("\n----- ESTADISTICAS -----");
        System.out.println("Cantidad media de descargas: " + media);
        System.out.println("Cantidad maxima de descargas: " + est.getMax());
        System.out.println("Cantidad minima de descargas: " + est.getMin());
        System.out.println("Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount());
        System.out.println("-----------------\n");
    }

    public void top10Libros(){
        List<Libro> libros = repo.top10Libros();
        System.out.println();
        libros.forEach(l -> System.out.println(
                "----- LIBRO -----" +
                        "\nTitulo: " + l.getTitulo() +
                        "\nAutor: " + l.getAutor().getNombre() +
                        "\nIdioma: " + l.getLenguaje().getIdioma() +
                        "\nNumero de descargas: " + l.getDescarga() +
                        "\n-----------------\n"
        ));
    }

    public void buscarAutorPorNombre(){
        System.out.println("Ingrese el nombre del autor que deseas buscar:");
        var nombre = teclado.nextLine();
        Optional<Autor> autor = repo.buscarAutorPorNombre(nombre);
        if(autor.isPresent()){
            System.out.println(
                    "\nAutor: " + autor.get().getNombre() +
                            "\nFecha de nacimiento: " + autor.get().getNacimiento() +
                            "\nFecha de fallecimiento: " + autor.get().getFallecimiento() +
                            "\nLibros: " + autor.get().getLibros().stream()
                            .map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
            );
        } else {
            System.out.println("El autor no existe en la BD!");
        }
    }

    public void listarAutoresConOtrasConsultas(){
        var menu = """
                Ingrese la opcion por la cual desea listar los autores
                1 - Listar autor por Año de nacimiento
                2 - Listar autor por año de fallecimiento
                """;
        System.out.println(menu);
        try{
            var opcion = Integer.valueOf(teclado.nextLine());
            switch (opcion){
                case 1:
                    ListarAutoresPorNacimiento();
                    break;
                case 2:
                    ListarAutoresPorFallecimiento();
                    break;
                default:
                    System.out.println("Opcion invalida!");
                    break;
            }
        } catch (NumberFormatException e) {
            System.out.println("Opcion no valida: " + e.getMessage());
        }
    }

    public void ListarAutoresPorNacimiento(){
        System.out.println("Introduce el año de nacimiento que deseas buscar:");
        try{
            var nacimiento = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repo.ListarAutoresPorNacimiento(nacimiento);
            if(autores.isEmpty()){
                System.out.println("No existen autores con año de nacimeinto igual a " + nacimiento);
            } else {
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNombre() +
                                "\nFecha de nacimiento: " + a.getNacimiento() +
                                "\nFecha de fallecimeinto: " + a.getFallecimiento() +
                                "\nLibros: " + a.getLibros().stream().map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            }
        } catch (NumberFormatException e){
            System.out.println("Año no valido: " + e.getMessage());
        }
    }

    public void ListarAutoresPorFallecimiento(){
        System.out.println("Introduce el año de fallecimiento que deseas buscar:");
        try{
            var fallecimiento = Integer.valueOf(teclado.nextLine());
            List<Autor> autores = repo.ListarAutoresPorFallecimiento(fallecimiento);
            if(autores.isEmpty()){
                System.out.println("No existen autores con año de fallecimiento igual a " + fallecimiento);
            } else {
                System.out.println();
                autores.forEach(a -> System.out.println(
                        "Autor: " + a.getNombre() +
                                "\nFecha de fallecimiento: " + a.getNacimiento() +
                                "\nFecha de fallecimeinto: " + a.getFallecimiento() +
                                "\nLibros: " + a.getLibros().stream().map(l -> l.getTitulo()).collect(Collectors.toList()) + "\n"
                ));
            }
        } catch (NumberFormatException e) {
            System.out.println("Opcion no valida: " + e.getMessage());
        }
    }



/*public void muestraElMenu(){
        var json = consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = conversor.obtenerDatos(json, Datos.class);
        System.out.println(datos);

        //Top 10 libros más descargados
        System.out.println("Top 10 libros más descargados");
        datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l -> l.titulo().toUpperCase())
                .forEach(System.out::println);

        //Busqueda de libros por nombre
        System.out.println("Ingrese el nombre del libro que desea buscar");
        var tituloLibro = teclado.nextLine();
        json = consumoAPI.obtenerDatos(URL_BASE+"?search=" + tituloLibro.replace(" ","+"));
        var datosBusqueda = conversor.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if(libroBuscado.isPresent()){
            System.out.println("Libro Encontrado ");
            System.out.println(libroBuscado.get());
        }else {
            System.out.println("Libro no encontrado");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.resultados().stream()
                .filter(d -> d.numeroDeDescargas() >0 )
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Cantidad media de descargas: " + est.getAverage());
        System.out.println("Cantidad máxima de descargas: "+ est.getMax());
        System.out.println("Cantidad mínima de descargas: " + est.getMin());
        System.out.println(" Cantidad de registros evaluados para calcular las estadisticas: " + est.getCount());

    }*/
}
