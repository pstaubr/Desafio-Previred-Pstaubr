package cl.previred.desafio.service;

import cl.previred.desafio.dao.EmpleadoDAO;
import cl.previred.desafio.models.Empleado;
import cl.previred.desafio.utils.RutValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para validar y manejar la lógica de negocio relacionada con la entidad Empleado.
 */
public class EmpleadoService {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoService.class);
    private final EmpleadoDAO empleadoDAO;

    /**
     * Constructor que recibe una instancia de EmpleadoDAO.
     *
     * @param empleadoDAO Objeto DAO para acceder a la base de datos de empleados.
     */
    public EmpleadoService(EmpleadoDAO empleadoDAO) {
        this.empleadoDAO = empleadoDAO;
    }

    /**
     * Valida los datos de un empleado antes de su creación o actualización.
     *
     * @param empleado Objeto Empleado a validar.
     * @return Lista de mensajes de error encontrados durante la validación.
     */
    public List<String> validarEmpleado(Empleado empleado) {
        List<String> errores = new ArrayList<>();

        if (empleado == null) {
            errores.add("Empleado no puede ser nulo.");
            logger.warn("Validación fallida: Empleado nulo.");
            return errores;
        }

        if (empleado.getNombre() == null || empleado.getNombre().isEmpty()) {
            errores.add("Nombre no puede estar vacío.");
            logger.warn("Validación fallida: Nombre vacío o nulo para RUT: {}", empleado.getRut());
        }

        if (empleado.getApellido() == null || empleado.getApellido().isEmpty()) {
            errores.add("Apellido no puede estar vacío.");
            logger.warn("Validación fallida: Apellido vacío o nulo para RUT: {}", empleado.getRut());
        }

        if (empleado.getRut() == null || !RutValidator.validarRut(empleado.getRut())) {
            errores.add("Rut es inválido.");
            logger.warn("Validación fallida: RUT inválido para RUT: {}", empleado.getRut());
        }

        if (empleado.getCargo() == null || empleado.getCargo().isEmpty()) {
            errores.add("El cargo no puede estar vacío.");
            logger.warn("Validación fallida: Cargo vacío o nulo para RUT: {}", empleado.getRut());
        }

        if (empleado.getSalario() < 400000) {
            errores.add("Salario debe ser mayor o igual a $400,000.");
            logger.warn("Validación fallida: Salario menor a 400000 o nulo para RUT: {}", empleado.getRut());
        }

        // Validación de duplicados
        try {
            if (empleadoDAO.existeRut(empleado.getRut())) {
                errores.add("Rut existente. Por favor, verifique los datos");
                logger.warn("Validación fallida: RUT duplicado para RUT: {}", empleado.getRut());
            }
        } catch (Exception e) {
            logger.error("Error al validar existencia de RUT: {}", empleado.getRut(), e);
            errores.add("Error interno al validar RUT."); // Mensaje genérico para el usuario
        }

        //Validaciones de bono y descuentos.
        if (empleado.getBonos() == null || empleado.getBonos() > empleado.getSalario() * 0.5) {
            errores.add("El bono no puede ser mayor al 50% del salario base.");
            logger.warn("Validación fallida: Bono mayor al 50% del salario para RUT: {}", empleado.getRut());
        }

        if (empleado.getDescuentos() == null || empleado.getDescuentos() > empleado.getSalario()) {
            errores.add("El total de descuentos no puede ser mayor al salario base.");
            logger.warn("Validación fallida: Descuentos mayores al salario para RUT: {}", empleado.getRut());
        }

        return errores;
    }
}