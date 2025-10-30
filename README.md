# 📚 LiterAlura Challenge

![Java | Spring Boot | PostgreSQL](https://img.shields.io/badge/Java-17+-blue?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3+-green?logo=spring)
![PostgreSQL](https://img.shields.io/badge/Database-PostgreSQL-blue?logo=postgresql)
![API](https://img.shields.io/badge/API-Gutendex-lightgrey)

Un proyecto desarrollado como parte del **Challenge de Backend de Alura Latam** que permite interactuar con la **API de Gutendex** para buscar y registrar información de libros y autores, y almacenar los datos localmente usando Spring Data JPA.

---

## ✨ Funcionalidades

El proyecto ofrece un **menú interactivo por consola** para realizar las siguientes operaciones:

1. **Buscar libro por título:** Consulta la API Gutendex y registra el libro si se encuentra.
2. **Listar libros registrados:** Muestra todos los libros guardados en la base de datos local.
3. **Listar autores registrados:** Muestra todos los autores guardados en la base de datos local.
4. **Listar autores vivos en un determinado año:** Permite filtrar autores por un rango de años.
5. **Listar libros por idioma:** Permite buscar libros registrados según el código de idioma (ej: `es`, `en`, `fr`).
6. **Generar estadísticas:** Calcula y muestra estadísticas de los libros.
7. **Top 10 libros:** Muestra los 10 libros con más descargas.
8. **Buscar autor por nombre:** Permite buscar un autor por su nombre.
9. **Listar autores con otras consultas** (Consultas personalizadas).
10. **Salir**

### 🖼️ Ejemplo de Uso
Aquí se muestra la ejecución de la opción `1` (Buscar libro por título) y la consulta por "batman".

<img width="464" height="507" alt="image" src="https://github.com/user-attachments/assets/b06191bd-f511-4005-a437-198acb571070" />

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología | Versión/Descripción |
| :--- | :--- | :--- |
| **Lenguaje** | Java | 17+ |
| **Framework** | Spring Boot | 3+ |
| **Persistencia** | Spring Data JPA (Hibernate) | Para la gestión de la base de datos. |
| **Base de Datos** | PostgreSQL / MySQL | Configurable en `application.properties`. |
| **Manejo de Dependencias**| Maven | Para la construcción del proyecto. |
| **API Externa** | Gutendex | Servicio de acceso a la base de datos de libros de dominio público. |

---

## ⚙️ Instalación y Ejecución

Para levantar y ejecutar el proyecto localmente, sigue estos pasos:

### 1. Clonar el Repositorio

```bash
git clone [https://github.com/tu-usuario/LiterAlura.git](https://github.com/tu-usuario/LiterAlura.git)
cd LiterAlura
```
### 2. Configurar la Base de Datos

El proyecto requiere una base de datos relacional para almacenar los libros y autores.

Crea la base de datos (ejemplo: literalura_db) en tu servidor de PostgreSQL o MySQL.

Edita el archivo de configuración src/main/resources/application.properties con tus credenciales.

Ejemplo de configuración (PostgreSQL):

```Properties
spring.datasource.url=jdbc:postgresql://localhost:5432/literalura_db?serverTimezone=UTC
spring.datasource.username=tu usuario
spring.datasource.password=tu contraseña
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```
### 3. Compilar y Ejecutar
Usa Maven para compilar y ejecutar la aplicación:

```Bash

# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

Una vez que la aplicación se inicie, se mostrará el menú principal en la consola, listo para interactuar.

<img width="493" height="284" alt="image" src="https://github.com/user-attachments/assets/41fae530-55f8-4ef0-ab91-d3f2f6e15024" />





