# Proyecto de Gestión de Empleados / Nómina - Desafío Previred - Pstaubr

Este proyecto es una API RESTful desarrollada en Java para la gestión de empleados y el cálculo de nóminas.

## Repositorio Git

Puedes encontrar el código fuente completo en el siguiente repositorio de GitHub:

[https://github.com/pstaubr/Desafio-Previred-Pstaubr](https://github.com/pstaubr/Desafio-Previred-Pstaubr)

## Requisitos

* **Java 8:** El proyecto está desarrollado y probado con Java 8. Asegúrate de tener Java 8 instalado en tu sistema.
* **PostgreSQL:** Se utiliza PostgreSQL como base de datos. Asegúrate de tener PostgreSQL instalado y configurado.
* **Maven:** Se utiliza Maven para la gestión de dependencias y la construcción del proyecto.
* **Postman (opcional):** Se proporciona una colección Postman para facilitar la prueba de los endpoints de la API.

## Configuración de la Base de Datos

1.  Crea una base de datos PostgreSQL llamada `postgres`.
2.  Ejecuta los scripts SQL proporcionados en el directorio `src/main/resources/db/empleados.sql` para crear las tablas necesarias.
3.  Configura la conexión a la base de datos en el archivo `src/main/resources/database.properties`. Asegúrate de actualizar los valores de `database.url`, `database.user`, y `database.password` con tu configuración local.

## Configuración de la API

1.  Clona el repositorio del proyecto:
    ```bash
    git clone [https://github.com/pstaubr/Desafio-Previred-Pstaubr.git](https://github.com/pstaubr/Desafio-Previred-Pstaubr.git)
    ```
2.  Navega al directorio del proyecto:
    ```bash
    cd previred-desafio-pstaubr
    ```
3.  Ejecuta `mvn clean install` para construir el proyecto y descargar las dependencias.
4.  Ejecuta la aplicación desde tu IDE o utilizando el comando `mvn spring-boot:run`.

## Uso de Postman

1.  Importa la colección Postman proporcionada en el archivo `Desafío Previred - Pstaubr.postman_collection.json`.
2.  Configura las variables de entorno en Postman para la URL base de la API y las credenciales de autenticación (si es necesario).
3.  Utiliza los endpoints de la colección Postman para interactuar con la API.

## Endpoints de la API

* `GET /previred-desafio-pstaubr/api/empleados`: Obtiene la lista de todos los empleados.
* `POST /previred-desafio-pstaubr/api/empleados`: Crea un nuevo empleado.
* `DELETE /previred-desafio-pstaubr/api/empleados/{id}`: Elimina un empleado por su ID.
* `POST /previred-desafio-pstaubr/api/nominas/calcular`: Calcula la nómina a partir de un archivo CSV.


## Contacto

Si tienes alguna pregunta o sugerencia, no dudes en contactarme.