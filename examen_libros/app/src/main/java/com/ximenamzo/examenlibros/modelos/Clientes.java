package com.ximenamzo.examenlibros.modelos;

import java.io.Serializable;

public class Clientes implements Serializable {
    Integer id;
    String nombre, rfc;

    public Clientes(Integer id, String rfc, String nombre) {
        this.id = id;
        this.rfc = rfc;
        this.nombre = nombre;
    }

    public Clientes() {

    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getRfc() { return rfc; }
    public void setRfc(String rfc) { this.rfc = rfc; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
}
