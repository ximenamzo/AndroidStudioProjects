package ximenamzo.a7bdusuarios.bd;

public class Variables {
    public static final String[] CAMPO_AUX = {"id","nombre","apellido","edad","sexo","fenac","estatura","telefono"};
    public static final String NOMBRE_BD = "bd_usuarios";
    public static final String NOMBRE_TABLA = "usuarios";
    public static final String CAMPO_ID = "id";
    public static final String CAMPO_NOMBRE = "nombre";
    public static final String CAMPO_APELLIDO = "apellido";
    public static final String CAMPO_EDAD = "edad";
    public static final String CAMPO_SEXO = "sexo";
    public static final String CAMPO_FENAC = "fenac";
    public static final String CAMPO_ESTATURA = "estatura";
    public static final String CAMPO_TELEFONO = "telefono";
    public static final String CREAR_TABLA = "CREATE TABLE " + NOMBRE_TABLA +
            " (" + CAMPO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            CAMPO_NOMBRE + " TEXT, " + CAMPO_APELLIDO + " TEXT, " +
            CAMPO_EDAD + " INTEGER, " + CAMPO_SEXO + " TEXT, " + CAMPO_FENAC + " TEXT, " +
            CAMPO_ESTATURA + " REAL, " + CAMPO_TELEFONO + " TEXT)";
    public static final String ELIMINAR_TABLA = "DROP TABLE IF EXIST" + NOMBRE_TABLA;
}
