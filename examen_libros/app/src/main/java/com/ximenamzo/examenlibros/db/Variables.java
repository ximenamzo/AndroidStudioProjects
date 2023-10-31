package com.ximenamzo.examenlibros.db;

public class Variables {
    public static final String NOMBRE_BD = "bd_libreria";
    public static final String[] NOMBRE_TABLA = {"libros","clientes","ventas"};
    public static final String[] CAMPO_IDS = {"id","id_libro","id_cliente"};
    public static final String[] CAMPO_ID2 = {"isbn","rfc"};
    public static final String CAMPO_TITULO = "titulo";
    public static final String[] CAMPO_PERSONA = {"autor","nombre"};
    public static final String CAMPO_EDITORIAL = "editorial";
    public static final String[] CAMPO_CANTIDADES = {"paginas","canlib"};
    public static final String[] CAMPO_DINERO = {"precio","costotal"};
    public static final String[][] CAMPOS_TABLAS = {
            {CAMPO_IDS[0],CAMPO_ID2[0],CAMPO_TITULO,CAMPO_PERSONA[0],CAMPO_EDITORIAL,CAMPO_CANTIDADES[0],CAMPO_DINERO[0]},
            {CAMPO_IDS[0],CAMPO_PERSONA[0],CAMPO_ID2[0]},
            {CAMPO_IDS[0],CAMPO_IDS[1],CAMPO_IDS[2],CAMPO_CANTIDADES[1],CAMPO_DINERO[1]}};

    public static final String[] CREAR_TABLA = {"CREATE TABLE " + NOMBRE_TABLA[0] +
            " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_ID2[0] + " TEXT UNIQUE, " + CAMPO_TITULO + " TEXT, " + CAMPO_PERSONA[0] + " TEXT, " +
            CAMPO_EDITORIAL + " TEXT, " + CAMPO_CANTIDADES[0] + " INTEGER, "+ CAMPO_DINERO[0] +" REAL)",

            "CREATE TABLE " + NOMBRE_TABLA[1] + " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_ID2[1] + " TEXT UNIQUE, " + CAMPO_TITULO + " TEXT, " + CAMPO_PERSONA[1] + " TEXT)",

            "CREATE TABLE " + NOMBRE_TABLA[2] + " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_IDS[1] + " INTEGER , " + CAMPO_IDS[2] + " INTEGER, " +
            CAMPO_CANTIDADES[1] + " INTEGER, " + CAMPO_DINERO[1] + " REAL)"};

    public static final String[] ELIMINAR_TABLA = {
            "DROP TABLE IF EXIST" + NOMBRE_TABLA[0],
            "DROP TABLE IF EXIST" + NOMBRE_TABLA[1],
            "DROP TABLE IF EXIST" + NOMBRE_TABLA[2]
    };
}

    /*public static final String CREAR_TABLA_LIBROS = "CREATE TABLE " + NOMBRE_TABLA[0] +
            " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_ID2[0] + " TEXT UNIQUE, " + CAMPO_TITULO + " TEXT, " + CAMPO_PERSONA[0] + " TEXT, " +
            CAMPO_EDITORIAL + " TEXT, " + CAMPO_CANTIDADES[0] + " INTEGER, "+ CAMPO_DINERO[0] +" FLOAT)";
    public static final String CREAR_TABLA_CLIENTES = "CREATE TABLE " + NOMBRE_TABLA[1] +
            " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_ID2[1] + " TEXT UNIQUE, " + CAMPO_TITULO + " TEXT, " + CAMPO_PERSONA[1] + " TEXT)";
    public static final String CREAR_TABLA_VENTAS = "CREATE TABLE " + NOMBRE_TABLA[2] +
            " (" + CAMPO_IDS[0] + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_IDS[1] + " INTEGER , " + CAMPO_IDS[2] + " INTEGER, " +
            CAMPO_CANTIDADES[1] + " INTEGER, " + CAMPO_DINERO[1] + " FLOAT)";*/