package cl.previred.desafio.servlets;

import cl.previred.desafio.dao.EmpleadoDAO;
import cl.previred.desafio.models.Empleado;
import com.google.gson.Gson;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Servlet que maneja la carga de nóminas de empleados desde un archivo CSV y calcula sus salarios.
 * Expone un endpoint para procesar archivos CSV subidos mediante peticiones multipart/form-data.
 */
@WebServlet("/api/nominas/calcular")
public class NominaServlet extends HttpServlet {
    private static final Logger logger = LoggerFactory.getLogger(NominaServlet.class);
    private EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    private Gson gson = new Gson();

    /**
     * Maneja las peticiones POST para procesar un archivo CSV de nómina de empleados.
     * El archivo CSV debe ser enviado como parte de una petición multipart/form-data.
     *
     * @param request  HttpServletRequest objeto que contiene la petición del cliente.
     * @param response HttpServletResponse objeto que contiene la respuesta del servidor.
     * @throws ServletException Si ocurre un error interno del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            if (!ServletFileUpload.isMultipartContent(request)) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "No es una petición multipart/form-data");
                return;
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> items = upload.parseRequest(request);

            FileItem file = items.stream()
                    .filter(item -> !item.isFormField())
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("No se encontró el archivo"));

            List<Empleado> empleados = procesarCSV(file.getInputStream());
            empleadoDAO.insertarEmpleados(empleados);

            response.setContentType("application/json");
            response.getWriter().write(obtenerEmpleadosJSON());

        } catch (Exception e) {
            logger.error("Error al procesar la nómina", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor");
        }
    }

    /**
     * Procesa un InputStream de un archivo CSV y retorna una lista de objetos Empleado.
     *
     * @param inputStream InputStream del archivo CSV.
     * @return Lista de objetos Empleado.
     * @throws IOException            Si ocurre un error de entrada/salida.
     * @throws CsvValidationException Si el formato del CSV no es válido.
     */
    private List<Empleado> procesarCSV(InputStream inputStream) throws IOException, CsvValidationException {
        List<Empleado> empleados = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            String[] line;
            boolean primeraFila = true; // Para ignorar el encabezado
            while ((line = reader.readNext()) != null) {
                if (primeraFila) {
                    primeraFila = false;
                    continue; // Ignorar la fila de encabezado
                }
                if (line.length != 7) { // Corregido: 7 columnas
                    throw new IllegalArgumentException("Formato CSV incorrecto");
                }

                // Agregar logs aquí
                logger.debug("Procesando fila: {}", Arrays.toString(line));
                logger.debug("Nombre: {}", line[1]);
                logger.debug("Apellido: {}", line[2]);
                logger.debug("RUT/DNI: {}", line[0]);
                logger.debug("Cargo: {}", line[3]);
                logger.debug("Salario Base: {}", line[4]);
                logger.debug("Bonos: {}", line[5]);
                logger.debug("Descuentos: {}", line[6]);
                logger.debug("Salario Final: {}", Double.parseDouble(line[4]) + Double.parseDouble(line[5]) - Double.parseDouble(line[6]));

                Empleado empleado = new Empleado(
                        0, // El ID no está en el CSV, así que lo ponemos como 0 o algún valor por defecto
                        line[1],
                        line[2],
                        line[0],
                        line[3],
                        Double.parseDouble(line[4]),
                        Double.parseDouble(line[5]),
                        Double.parseDouble(line[6]),
                        Double.parseDouble(line[4]),
                        Double.parseDouble(line[4]) + Double.parseDouble(line[5]) - Double.parseDouble(line[6])
                );
                empleados.add(empleado);
            }
        }
        return empleados;
    }

    /**
     * Obtiene una lista de empleados desde la base de datos y la convierte a formato JSON.
     *
     * @return String JSON que representa la lista de empleados.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     * @throws IOException  Si ocurre un error de entrada/salida.
     */
    private String obtenerEmpleadosJSON() throws SQLException, IOException {
        List<Empleado> empleados = empleadoDAO.obtenerEmpleados();
        return gson.toJson(empleados);
    }
}