package cl.previred.desafio.servlets;

import cl.previred.desafio.dao.EmpleadoDAO;
import cl.previred.desafio.models.Empleado;
import cl.previred.desafio.service.EmpleadoService;
import cl.previred.desafio.utils.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Servlet que maneja las operaciones CRUD para la entidad Empleado.
 * Expone los endpoints para obtener, crear y eliminar empleados a través de una API REST.
 */
@WebServlet("/api/empleados")
public class EmpleadoServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoServlet.class);
    private final EmpleadoDAO empleadoDAO = new EmpleadoDAO();
    public EmpleadoService empleadoService = new EmpleadoService(empleadoDAO);

    /**
     * Maneja las peticiones GET para obtener la lista de todos los empleados.
     *
     * @param req  HttpServletRequest objeto que contiene la petición del cliente.
     * @param resp HttpServletResponse objeto que contiene la respuesta del servidor.
     * @throws ServletException Si ocurre un error interno del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Empleado> empleados = empleadoDAO.obtenerTodos();
            JsonUtils.enviarRespuesta(resp, empleados);
            logger.debug("Se obtuvieron {} empleados.", empleados.size());
        } catch (Exception e) {
            logger.error("Error al obtener empleados.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor.");
        }
    }

    /**
     * Maneja las peticiones POST para crear un nuevo empleado.
     *
     * @param req  HttpServletRequest objeto que contiene la petición del cliente.
     * @param resp HttpServletResponse objeto que contiene la respuesta del servidor.
     * @throws ServletException Si ocurre un error interno del servlet.
     * @throws IOException      Si ocurre un error de entrada/salida.
     */
    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Empleado empleado = JsonUtils.leerCuerpo(req, Empleado.class);
            List<String> errores = empleadoService.validarEmpleado(empleado);

            if (errores.isEmpty() && empleadoDAO.crear(empleado)) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("mensaje", "Empleado creado exitosamente.");
                respuesta.put("empleado", empleado);
                JsonUtils.enviarRespuesta(resp, respuesta);
                logger.debug("Empleado creado: {}", empleado.getRut());
            } else {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("errores", errores);
                JsonUtils.enviarRespuesta(resp, respuesta);
                logger.warn("Error al crear empleado. Errores: {}", errores);
            }
        } catch (Exception e) {
            logger.error("Error al procesar la petición POST.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor.");
        }
    }

    /**
     * Maneja las peticiones DELETE para eliminar un empleado por su ID.
     *
     * @param req  HttpServletRequest objeto que contiene la petición del cliente.
     * @param resp HttpServletResponse objeto que contiene la respuesta del servidor.
     * @throws IOException Si ocurre un error de entrada/salida.
     */
    @Override
    public void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            if (empleadoDAO.eliminar(id)) {
                resp.setStatus(HttpServletResponse.SC_OK);
                Map<String, Object> respuesta = new HashMap<>();
                respuesta.put("mensaje", "Empleado eliminado exitosamente.");
                JsonUtils.enviarRespuesta(resp, respuesta);
                logger.debug("Empleado eliminado: id={}", id);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Empleado no encontrado");
                logger.warn("No se pudo eliminar el empleado: id={}", id);
            }
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID inválido");
            logger.warn("ID inválido en la petición DELETE.", e);
        } catch (Exception e) {
            logger.error("Error al procesar la petición DELETE.", e);
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor.");
        }
    }
}