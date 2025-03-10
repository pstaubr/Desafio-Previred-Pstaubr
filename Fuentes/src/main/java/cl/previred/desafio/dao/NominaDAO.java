package cl.previred.desafio.dao;

import cl.previred.desafio.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Data Access Object (DAO) para la entidad Nomina.
 * Proporciona métodos para calcular y guardar nóminas en la base de datos.
 */
public class NominaDAO {

    /**
     * Calcula los salarios finales para una lista de empleados y guarda las nóminas en la base de datos.
     *
     * @param datos Lista de mapas, donde cada mapa representa los datos de un empleado.
     *              Cada mapa debe contener las claves "salarioBase", "bonos", "descuentos" y "id".
     * @return Lista de mapas con los salarios finales calculados y guardados.
     */
    public List<Map<String, String>> calcularNominas(List<Map<String, String>> datos) {
        return datos.stream().map(empleado -> {
            double salarioBase = Double.parseDouble(empleado.get("salarioBase"));
            double bonos = Double.parseDouble(empleado.get("bonos"));
            double descuentos = Double.parseDouble(empleado.get("descuentos"));
            double salarioFinal = (salarioBase + bonos) - descuentos;
            empleado.put("salarioFinal", String.valueOf(salarioFinal));
            guardarNomina(empleado);
            return empleado;
        }).collect(Collectors.toList());
    }

    /**
     * Guarda la información de una nómina en la base de datos.
     *
     * @param empleado Mapa que contiene los datos de la nómina a guardar.
     *                 Debe contener las claves "id", "salarioBase", "bonos", "descuentos" y "salarioFinal".
     */
    private void guardarNomina(Map<String, String> empleado) {
        String sql = "INSERT INTO nominas (empleado_id, salario_base, bonos, descuentos, salario_final) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, Integer.parseInt(empleado.get("id")));
            stmt.setDouble(2, Double.parseDouble(empleado.get("salarioBase")));
            stmt.setDouble(3, Double.parseDouble(empleado.get("bonos")));
            stmt.setDouble(4, Double.parseDouble(empleado.get("descuentos")));
            stmt.setDouble(5, Double.parseDouble(empleado.get("salarioFinal")));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(); // Consider using logger instead of printStackTrace
        }
    }
}