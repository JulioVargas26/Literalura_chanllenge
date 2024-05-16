package alura.challenge.literalura.model;

/*
    Finalmente el Enum de Lengauje nos pertite trabajar con los idiomas de los libros
     de manera mas controlada y evitando
     que el usuario introduzca datos inecesarios o no deseados.
*/
public enum Lenguaje {
    ES("es"),
    EN("en"),
    FR("fr"),
    PT("pt");

    private String idioma;

    Lenguaje(String idioma) {
        this.idioma = idioma;
    }

    public static Lenguaje fromString(String text){
        for (Lenguaje lenguaje : Lenguaje.values()){
            if(lenguaje.idioma.equalsIgnoreCase(text)){
                return lenguaje;
            }
        }
        throw new IllegalArgumentException("Ningun lenguaje encontrado: " + text);
    }

    public String getIdioma(){
        return this.idioma;
    }
}
