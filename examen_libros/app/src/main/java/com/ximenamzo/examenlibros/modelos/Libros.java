package com.ximenamzo.examenlibros.modelos;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Libros implements Serializable {
    Integer id, paginas;
    String isbn, titulo, autor, editorial;
    Double precio;
    
    public Libros(Integer id, String isbn, String titulo, String autor, String editorial, Integer paginas, Double precio) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.editorial = editorial;
        this.paginas = paginas;
        this.precio = precio;
    }

    public Libros() {

    }

    @NonNull
    @Override
    public String toString() {
        return "Libro [id=" + id + ", ISBN=" + isbn + ", Título=" + titulo + ", Autor=" + autor + ", Editorial=" + editorial +
                ", Páginas=" + paginas + ", Precio=" + precio + "]";
    }


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getEditorial() { return editorial; }
    public void setEditorial(String editorial) { this.editorial = editorial; }
    public Integer getPaginas() { return paginas; }
    public void setPaginas(Integer paginas) { this.paginas = paginas; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
}
