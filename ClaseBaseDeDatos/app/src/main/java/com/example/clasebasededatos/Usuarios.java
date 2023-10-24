package com.example.clasebasededatos;

import java.io.Serializable;

// getters and setters
public class Usuarios implements Serializable {
    Integer id;
    String nombre;
    String telefono;

    public Usuarios(Integer id, String nombre, String telefono) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
    }

    public Usuarios() {

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
