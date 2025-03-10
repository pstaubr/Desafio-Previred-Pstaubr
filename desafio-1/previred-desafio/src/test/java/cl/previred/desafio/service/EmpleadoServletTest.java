package cl.previred.desafio.service;


import cl.previred.desafio.dao.EmpleadoDAO;
import cl.previred.desafio.models.Empleado;
import cl.previred.desafio.servlets.EmpleadoServlet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmpleadoServletTest {

    @Mock
    private EmpleadoDAO empleadoDAO;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @InjectMocks
    private EmpleadoServlet empleadoServlet;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        empleadoServlet = new EmpleadoServlet();
        empleadoServlet.empleadoService = new EmpleadoService(empleadoDAO);
    }

    @Test
    public void doPost_createEmpleado() throws Exception {
        String empleadoJson = "{\"rut\":\"12345678-9\",\"nombre\":\"Juan\",\"apellido\":\"Pérez\",\"cargo\":\"Desarrollador\",\"salarioBase\":1000000.0,\"bonos\":100000.0,\"descuentos\":50000.0}";
        BufferedReader reader = new BufferedReader(new StringReader(empleadoJson));
        when(request.getReader()).thenReturn(reader);
        when(empleadoDAO.crear(any(Empleado.class))).thenReturn(true);
        when(empleadoDAO.existeRut("12345678-9")).thenReturn(false); // Simular que el RUT no existe

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);

        empleadoServlet.doPost(request, response);

        verify(empleadoDAO).crear(any(Empleado.class));
        assertEquals(201, response.getStatus());
    }

    @Test
    public void doGet_shouldReturnListOfEmpleados() throws Exception {
        List<Empleado> empleados = new ArrayList<>();
        String empleadoJson = "{\"rut\":\"12345678-9\",\"nombre\":\"Juan\",\"apellido\":\"Pérez\",\"cargo\":\"Desarrollador\",\"salarioBase\":1000000.0,\"bonos\":100000.0,\"descuentos\":50000.0}";
        when(empleadoDAO.obtenerTodos()).thenReturn(empleados);

        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        when(response.getWriter()).thenReturn(writer);
        empleadoServlet.doGet(request, response);

        //(Verificación)
        verify(empleadoDAO).obtenerTodos();
        assertEquals("{\"rut\":\"12345678-9\",\"nombre\":\"Juan\",\"apellido\":\"Pérez\",\"cargo\":\"Desarrollador\",\"salarioBase\":1000000.0,\"bonos\":100000.0,\"descuentos\":50000.0}", stringWriter.toString().trim());
    }

    @Test
    public void doGet_shouldReturnInternalServerErrorOnException() throws Exception {
        // Arrange
        when(empleadoDAO.obtenerTodos()).thenThrow(new RuntimeException("Simulated exception"));
        empleadoServlet.doGet(request, response);
        verify(response).sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error interno del servidor.");
    }

}