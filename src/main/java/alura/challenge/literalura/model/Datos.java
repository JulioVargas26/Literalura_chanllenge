package alura.challenge.literalura.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 *      Datos es un record que nos facilita trabajar con las variables
 *      que vamos a utilizar para manipular los datos
 *      que recibimos de la API al traer todos los libros que se encuentran en ella:
 * */

@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(
        @JsonAlias("count") Integer total,
        @JsonAlias("results") List<DatosLibros> resultados
) {
}
