package cl.previred.desafio.dao;

import cl.previred.desafio.models.Empleado;
import cl.previred.desafio.utils.DatabaseConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para la entidad Empleado.
 * Proporciona métodos para realizar operaciones CRUD en la base de datos.
 */
public class EmpleadoDAO {

    private static final Logger logger = LoggerFactory.getLogger(EmpleadoDAO.class);

    /**
     * Obtiene todos los empleados de la base de datos.
     *
     * @return Lista de objetos Empleado.
     */
    public List<Empleado> obtenerTodos() {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM empleados");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                empleados.add(new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("rut"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"),
                        rs.getDouble("bonos"),
                        rs.getDouble("descuentos"),
                        rs.getDouble("salario_base"),
                        rs.getDouble("salario_final")
                ));
            }
            logger.debug("Obtenidos {} empleados", empleados.size());
        } catch (SQLException e) {
            logger.error("Error al obtener todos los empleados", e);
        }
        return empleados;
    }

    /**
     * Crea un nuevo empleado en la base de datos.
     *
     * @param empleado Objeto Empleado a crear.
     * @return true si la creación fue exitosa, false en caso contrario.
     */
    public boolean crear(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, apellido, rut, cargo, salario, bonos, descuentos, salario_base, salario_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, empleado.getNombre());
            stmt.setString(2, empleado.getApellido());
            stmt.setString(3, empleado.getRut());
            stmt.setString(4, empleado.getCargo());
            stmt.setDouble(5, empleado.getSalario());
            stmt.setDouble(6, empleado.getBonos());
            stmt.setDouble(7, empleado.getDescuentos());
            if (empleado.getSalarioBase() == null) {
                stmt.setNull(8, java.sql.Types.DOUBLE);
            } else {
                stmt.setDouble(8, empleado.getSalarioBase());
            }

            if (empleado.getSalarioFinal() == null) {
                stmt.setNull(9, java.sql.Types.DOUBLE);
            } else {
                stmt.setDouble(9, empleado.getSalarioFinal());
            }

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                logger.debug("Empleado creado: {}", empleado.getRut());
                return true;
            } else {
                logger.warn("No se pudo crear el empleado: {}", empleado.getRut());
                return false;
            }

        } catch (SQLException e) {
            logger.error("Error al crear el empleado: {}", empleado.getRut(), e);
        }
        return false;
    }

    /**
     * Elimina un empleado de la base de datos por su ID.
     *
     * @param id ID del empleado a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminar(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                logger.debug("Empleado eliminado: id={}", id);
                return true;
            } else {
                logger.warn("No se pudo eliminar el empleado: id={}", id);
                return false;
            }
        } catch (SQLException e) {
            logger.error("Error al eliminar el empleado: id={}", id, e);
        }
        return false;
    }

    /**
     * Verifica si un RUT ya existe en la base de datos.
     *
     * @param rut RUT a verificar.
     * @return true si el RUT existe, false en caso contrario.
     */
    public boolean existeRut(String rut) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM public.empleados WHERE rut = ?")) {
            stmt.setString(1, rut);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                boolean existe = rs.getInt(1) > 0;
                logger.debug("Verificación de existencia de RUT {}: {}", rut, existe);
                return existe;
            }
        } catch (SQLException e) {
            logger.error("Error al verificar la existencia del RUT: {}", rut, e);
        }
        return false;
    }

    /**
     * Inserta una lista de empleados en la base de datos.
     *
     * @param empleados Lista de objetos Empleado a insertar.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public void insertarEmpleados(List<Empleado> empleados) throws SQLException {
        String sql = "INSERT INTO empleados (nombre, apellido, rut, cargo, salario, bonos, descuentos, salario_base, salario_final) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (Empleado empleado : empleados) {

                pstmt.setString(1, empleado.getNombre());
                pstmt.setString(2, empleado.getApellido());
                pstmt.setString(3, empleado.getRut());
                pstmt.setString(4, empleado.getCargo());
                pstmt.setDouble(5, empleado.getSalario());
                pstmt.setDouble(6, empleado.getBonos());
                pstmt.setDouble(7, empleado.getDescuentos());
                pstmt.setDouble(8, empleado.getSalarioBase());
                pstmt.setDouble(9, empleado.getSalarioFinal());
                pstmt.executeUpdate();
            }
            logger.debug("Insertados {} empleados desde CSV", empleados.size());
        }
    }

    /**
     * Obtiene todos los empleados de la base de datos para la nómina.
     *
     * @return Lista de objetos Empleado.
     * @throws SQLException Si ocurre un error al acceder a la base de datos.
     */
    public List<Empleado> obtenerEmpleados() throws SQLException {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT id, nombre, apellido, rut, cargo, salario, bonos, descuentos, salario_base, salario_final FROM empleados";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("rut"),
                        rs.getString("cargo"),
                        rs.getDouble("salario"),
                        rs.getDouble("bonos"),
                        rs.getDouble("descuentos"),
                        rs.getDouble("salario_base"),
                        rs.getDouble("salario_final")
                );
                empleados.add(empleado);
            }
            logger.debug("Obtenidos {} empleados para la nómina", empleados.size());
        }
        return empleados;
    }
}