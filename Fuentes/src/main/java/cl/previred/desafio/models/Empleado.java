package cl.previred.desafio.models;

public class Empleado {
    private int id;
    private String nombre;
    private String apellido;
    private String rut;
    private String cargo;
    private double salario;
    private Double bonos; // Usamos Double para permitir valores nulos
    private Double descuentos; // Usamos Double para permitir valores nulos
    private Double salarioBase; // Usamos Double para permitir valores nulos
    private Double salarioFinal; // Usamos Double para permitir valores nulos

    public Empleado(int id, String nombre, String apellido, String rut, String cargo, double salario, Double bonos, Double descuentos, Double salarioBase, Double salarioFinal) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
        this.cargo = cargo;
        this.salario = salario;
        this.bonos = bonos;
        this.descuentos = descuentos;
        this.salarioBase = salarioBase;
        this.salarioFinal = salarioFinal;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }

    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public double getSalario() {
        return salario;
    }

    public void setSalario(double salario) {
        this.salario = salario;
    }

    public Double getBonos() {
        return bonos;
    }

    public void setBonos(Double bonos) {
        this.bonos = bonos;
    }

    public Double getDescuentos() {
        return descuentos;
    }

    public void setDescuentos(Double descuentos) {
        this.descuentos = descuentos;
    }

    public Double getSalarioBase() {
        return salarioBase;
    }

    public void setSalarioBase(Double salarioBase) {
        this.salarioBase = salarioBase;
    }

    public Double getSalarioFinal() {
        return salarioFinal;
    }

    public void setSalarioFinal(Double salarioFinal) {
        this.salarioFinal = salarioFinal;
    }
}
