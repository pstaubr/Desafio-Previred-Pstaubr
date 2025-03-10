# DESAFÍO PREVIRED - EJERCICIO

_El detalle del ejercicio es el siguiente_

## Parte 1: Implementación de un Servicio Web con Servlets y AJAX
```
  Crear una aplicación web en Java 8 con Servlets y manejo de AJAX, con las siguientes características: 

    Endpoint: /api/empleados 
      GET: Retorna una lista de empleados en formato JSON. 
      POST: Permite agregar un nuevo empleado enviando datos en formato JSON. 
      DELETE: Elimina un empleado por su ID. 

  Datos esperados del empleado: 

    ID (autogenerado), Nombre, Apellido, RUT/DNI, Cargo, Salario.

  Interfaz con AJAX: 
    Crear una página web simple en HTML + JavaScript (sin frameworks como React o Angular). 
    Usar AJAX (XMLHttpRequest) para:  
      - Cargar la lista de empleados sin recargar la página. 
      - Agregar nuevos empleados mediante un formulario sin recargar la página. 
      - Eliminar empleados con un botón sin recargar la página. 

  Requerimientos técnicos: 
    - No usar frameworks externos, solo Servlets y JDBC para conexión con una BD en memoria (Puede ser alguna de estas tres: MySql, PostgreSql o H2). 
    - Manejo adecuado de excepciones y logging. 
    - Validación de datos en los endpoints. 
```

## Parte 2: Validaciones de Reglas de Negocio con AJAX

```
  Implementar validaciones en la carga de empleados y nóminas: 

    1. En el backend (Java 8): 
        - Rechazar empleados con RUT/DNI duplicado. 
        - No permitir salarios base menores a $400,000. 
        - Bonos no pueden superar el 50% del salario base. 
        - El total de descuentos no puede ser mayor al salario base. 
        - Si alguna regla se incumple, se debe retornar una respuesta HTTP 400 con un JSON indicando los registros con error. 
    2. En el frontend (JavaScript + AJAX): 
        - Implementar validaciones antes de enviar el formulario:  
        - Verificar que todos los campos estén completos. 
        - Validar formato del RUT/DNI. 
        - Validar que el salario base no sea menor a $400,000. 
        - Mostrar errores de validación de forma dinámica en la página (sin alertas de JavaScript). 
```

## Parte 3: Procesamiento de Nómina con AJAX

```
  Implementar un servicio que procese un archivo CSV (Excel adjunto): 
    
  El servicio debe: 

  1. Leer el archivo CSV y calcular el salario final de cada trabajador usando la siguiente fórmula: 
        
      Salario Final=(Salario Base+Bonos)−Descuentos\text{Salario Final} = (\text{Salario Base} + \text{Bonos}) - \text{Descuentos}  

  2. Almacenar los datos en una base de datos en memoria (Trabaja sobre la misma que ya has elegido). 
  
  3. Exponer un endpoint /api/nominas/calcular que: 
    - Reciba un archivo CSV como multipart/form-data. 
    - Procese el archivo y guarde la información en la BD. 
    - Retorne un JSON con el listado de empleados y sus salarios finales. 

  4. Interfaz con AJAX: 
    - Agregar un formulario en HTML para subir el archivo CSV. 
    - Usar AJAX para enviar el archivo sin recargar la página. 
    - Mostrar los resultados en una tabla generada dinámicamente con JavaScript (Crea una tabla simple sin efectos gráficos). 
    
```


### TIPS
_Para el empaquetado de la aplicación puedes utilizar Spring boot / Aplicación con maven desplegable en Tomcat 9_
Se evaluara las siguientes practicas en su codigo

* Uso de patrones de diseño (Factory, Strategy, DAO, etc.). 
* Manejo de concurrencia (uso de hilos o procesamiento paralelo). 
* Optimización de consultas SQL y manejo eficiente de la BD. 
* Uso de Unit Tests. 
