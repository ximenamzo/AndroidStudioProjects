package com.ximenamzo.examenlibros;

public class Variables {
    public static final String NOMBRE_BD = "bd_libros";
    public static final String NOMBRE_TABLA = "libros";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_ISBN = "isbn";
    public static final String CAMPO_TITULO = "titulo";
    public static final String CAMPO_AUTOR = "autor";
    public static final String CAMPO_EDITORIAL = "editorial";
    public static final String CAMPO_PAGINAS = "paginas";

    public static final String CREAR_TABLA = "CREATE TABLE " + NOMBRE_TABLA +
            " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_ISBN + " TEXT UNIQUE, " + CAMPO_TITULO + " TEXT, " + CAMPO_AUTOR + " TEXT, " +
            CAMPO_EDITORIAL + " TEXT, " + CAMPO_PAGINAS + " INTEGER)";
    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXIST" + NOMBRE_TABLA;

}