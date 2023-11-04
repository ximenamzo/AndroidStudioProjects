package com.ximenamzo.examenlibros.modelos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Clientes implements Serializable {
    Integer id;
    String nombre, rfc;

    public Clientes(Integer id, String nombre, String rfc) {
        this.id = id;
        this.nombre = nombre;
        this.rfc = rfc;
    }

    public Clientes() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Libro [id=" + id + ", Nombre=" + nombre + ", RFC=" + rfc + "]";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
}
