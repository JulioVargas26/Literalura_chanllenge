package alura.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 *      el record de DatosLibros nos permite trabajar con las variables
 *      que necesitamos en cada caso para poder manipular los datos
 *      de los libros de la API.
 * */

@JsonIgnoreProperties(ignoreUnknown = true)
public record DatosLibros(
        @JsonAlias("id") Long id,
        @JsonAlias("title") String titulo,
        @JsonAlias("authors") List<DatosAutor> autor,
        @JsonAlias("languages") List<String> idiomas,
        @JsonAlias("copyright") String copyright,
        @JsonAlias("download_count") Double numeroDeDescargas
) {
}
