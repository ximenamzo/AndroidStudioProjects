package ximenamzo.a7bdusuarios.models;

import java.io.Serializable;

// getters and setters
public class Usuarios implements Serializable {
    Integer id, edad, estatura;
    String nombre, apellido, sexo, fenac, telefono;


    public Usuarios(Integer id, String nombre, String apellido, Integer edad, String sexo, String fenac, Integer estatura, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.edad = edad;
        this.sexo = sexo;
        this.fenac = fenac;
        this.estatura = estatura;
        this.telefono = telefono;
    }

    public Usuarios() {

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }
    public Integer getEstatura() { return estatura; }
    public void setEstatura(Integer estatura) { this.estatura = estatura; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getFenac() { return fenac; }
    public void setFenac(String fenac) { this.fenac = fenac; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
