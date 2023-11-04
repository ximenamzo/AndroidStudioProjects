package com.ximenamzo.examenlibros.modelos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Ventas implements Serializable {
    Integer id, id_libro, id_cliente, canlib;
    Double costotal;

    public Ventas(Integer id, Integer id_libro, Integer id_cliente, Integer canlib, Double costotal) {
        this.id = id;
        this.id_libro = id_libro;
        this.id_cliente = id_cliente;
        this.canlib = canlib;
        this.costotal = costotal;
    }

    public Ventas() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Libro [id=" + id + ", IdLibro=" + id_libro + ", IdCliente=" + id_cliente + ", Cantidad=" + canlib + ", Total=" + costotal + "]";
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getId_libro() { return id_libro; }
    public void setId_libro(Integer id_libro) { this.id_libro = id_libro; }
    public Integer getId_cliente() { return id_cliente; }
    public void setId_cliente(Integer id_cliente) { this.id_cliente = id_cliente; }
    public Integer getCanlib() { return canlib; }
    public void setCanlib(Integer canlib) { this.canlib = canlib; }
    public Double getCostotal() { return costotal; }
    public void setCostotal(Double costotal) { this.costotal = costotal; }
}
