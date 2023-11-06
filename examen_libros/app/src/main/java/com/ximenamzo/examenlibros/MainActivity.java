package com.ximenamzo.examenlibros;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Clientes;
import com.ximenamzo.examenlibros.modelos.Libros;
import com.ximenamzo.examenlibros.vistas.libros.MainLibros;
import com.ximenamzo.examenlibros.vistas.clientes.MainClientes;
import com.ximenamzo.examenlibros.vistas.ventas.MainVentas;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Button libros, clientes, ventas;
    Intent i;
    Connect conectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sistema de Librería");

        libros = findViewById(R.id.golibros);
        clientes = findViewById(R.id.goclientes);
        ventas = findViewById(R.id.goventas);
        libros.setOnClickListener(view -> processIntent(MainLibros.class));
        clientes.setOnClickListener(view -> processIntent(MainClientes.class));
        ventas.setOnClickListener(view -> processIntent(MainVentas.class));

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        insertarDatos();
        imprimirLogTabla(0);
        imprimirLogTabla(1);
    }

    private void processIntent(Class<?> activityClass){
            i = new Intent(MainActivity.this, activityClass);
            startActivity(i);
    }

    public void insertarDatos() {
        List<Libros> libros = new ArrayList<>();
        List<Clientes> clientes = new ArrayList<>();
        String[][] datos = {
                {"[0][0]", "isbn [0][1]", "titulo [0][2]", "autor [0][3]", "editorial [0][4]", "paginas [0][5]", "precio [0][6]", "nombre [0][7]", "rfc [0][8]"},
                {"[1][0]", "1234567890", "El Misterio", "Juan Pérez", "Planeta", "500", "19.99", "María Torres", "ABCD123456EN1"},
                {"[2][0]", "9781234567", "Secretos Oscuros", "Juan Torres", "Alfaguara", "350", "15.99", "Sofía García", "EFGH789012FB2"},
                {"[3][0]", "3456789012", "La Aventura", "Julian Pereira", "Debolsillo", "400", "12.99", "Pedro Rodríguez", "IJKL345678MR3"},
                {"[4][0]", "7898765432", "Cazadores de Tesoros", "Maria González", "Espasa Calpe", "800", "21.99", "Ana García", "MNOP901234AB4"},
                {"[5][0]", "5678901234", "Misterios del Pasado", "Luis Sánchez", "Salamandra", "320", "14.99", "María Rodríguez", "QRST567890MY5"},
                {"[6][0]", "6789012345", "El Viajero", "Ana Ruiz", "Alfaguara", "150", "9.99", "Luis González", "UVWX123456JL7"},
                {"[7][0]", "7890123456", "El Descubrimiento", "Juan Torres", "Editorial Molino", "300", "16.99", "Elena Martínez", "YZAB789012AG8"},
                {"[8][0]", "8901234567", "Secretos Revelados", "Julia Fernández", "Anaya", "400", "13.99", "Pedro Ramírez", "CDEF345678SP9"},
                {"[9][0]", "9012345678", "El Enigma", "Santiago Pérez", "Debolsillo", "250", "12.99", "Isabel Fernández", "GHIJ901234OC0"},
               {"[10][0]", "0123456789", "El Legado Perdido", "Julian Pereira", "Alianza Editorial", "400", "17.99", "Antonio López", "KLMN567890NV6"}
        };
        //Log.d("DEBUG_XX", Arrays.deepToString(datos));
        for (int i = 1; i <= 10; i++) {
            Libros libro = new Libros(null, datos[i][1], datos[i][2], datos[i][3], datos[i][4], Integer.parseInt(datos[i][5]), Double.parseDouble(datos[i][6]));
            libros.add(libro);
            //Log.d("DEBUG_INDATOS", libro.toString());

            Clientes cliente = new Clientes(null, datos[i][7], datos[i][8]);
            clientes.add(cliente);
            //Log.d("DEBUG_INDATOS", cliente.toString());
        }

        SQLiteDatabase db = conectar.getWritableDatabase();

        for (Libros libro : libros) {
            if (!existe(db, libro.getIsbn(), Variables.CAMPOS_TABLAS[0][1], 0)) {
                //Log.d("DEBUG_INDATOS", libro.toString());
                insertarLibro(db, libro);
            }
        }

        for (Clientes cliente : clientes) {
            if (!existe(db, cliente.getRfc(), Variables.CAMPOS_TABLAS[1][2], 1)) {
                //Log.d("DEBUG_INDATOS", cliente.toString());
                insertarCliente(db, cliente);
            }
        }
        db.close();
    }


    private void insertarLibro(SQLiteDatabase db, Libros libro) {
        ContentValues valores = new ContentValues();
        valores.put("isbn", libro.getIsbn());
        valores.put("titulo", libro.getTitulo());
        valores.put("autor", libro.getAutor());
        valores.put("editorial", libro.getEditorial());
        valores.put("paginas", libro.getPaginas());
        valores.put("precio", libro.getPrecio());

        db.insert(Variables.NOMBRE_TABLA[0], Variables.CAMPO_IDS[0], valores);
        //Log.d("DEBUX_INSERT", "Tabla "+Variables.NOMBRE_TABLA[0]+": valores - "+valores);
    }

    private void insertarCliente(SQLiteDatabase db, Clientes cliente) {
        ContentValues valores = new ContentValues();
        valores.put("nombre", cliente.getNombre());
        valores.put("rfc", cliente.getRfc());

        db.insert(Variables.NOMBRE_TABLA[1], Variables.CAMPO_IDS[0], valores);
        //Log.d("DEBUX_INSERT", "Tabla "+Variables.NOMBRE_TABLA[1]+": valores - "+valores);
    }

    private boolean existeLibro(SQLiteDatabase db, String isbn) {
        //Log.d("DEBUG_EXL", "Existe Libro...");
        String[] campos = {Variables.CAMPOS_TABLAS[0][1]};
        //Log.d("DEBUG_EXL", "Campos:" + Arrays.toString(campos));
        String[] args = {isbn};
        //Log.d("DEBUG_EXL", "Args:" + Arrays.toString(args));
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[0], campos, Variables.CAMPOS_TABLAS[0][1] + "=?", args, null, null, null);
        //Log.d("DEBUG_EXL", cursor.toString());
        boolean existe = cursor.moveToFirst();
        cursor.close();
        //Log.d("DEBUG_EXL", "Existe?? " + existe + "!!  !!  !!  !!  !!");
        return existe;
    }

    private boolean existe(SQLiteDatabase db, String dato, String campo, Integer i) {
        //Log.d("DEBUG_EXS", "Existe Dato...");
        String[] campos = {campo};
        //Log.d("DEBUG_EXS", "Campos:" + Arrays.toString(campos));
        String[] args = {dato};
        //Log.d("DEBUG_EXS", "Args:" + Arrays.toString(args));
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[i], campos, Arrays.toString(campos) + "=?", args, null, null, null);
        //Log.d("DEBUG_EXS", cursor.toString());
        boolean existe = cursor.moveToFirst();
        cursor.close();
        //Log.d("DEBUG_EXL", "Existe?? " + existe + " !!  !!  !!  !!  !!");
        return existe;
    }

    private boolean existeCliente(SQLiteDatabase db, String rfc) {
        //Log.d("DEBUG_EXC", "Existe Cliente...");
        String[] campos = {Variables.CAMPOS_TABLAS[1][2]};
        //Log.d("DEBUG_EXC", "Campos:" + Arrays.toString(campos));
        String[] args = {rfc};
        //Log.d("DEBUG_EXC", "Args:" + Arrays.toString(args));
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[1], campos, Variables.CAMPOS_TABLAS[1][1] + "=?", args, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int columnIndex = cursor.getColumnIndex(Variables.CAMPOS_TABLAS[1][2]); // Cambia el índice según la columna deseada
                String rfcValue = cursor.getString(columnIndex);
                //Log.d("DEBUG_EXC", "RFC encontrado en la base de datos: " + rfcValue);
            } while (cursor.moveToNext());
        }

        cursor.close();

        boolean existe = cursor.getCount() > 0; // Verificar si se encontraron resultados
        //Log.d("DEBUG_EXC", "Existe? " + existe);

        return existe;
    }

    private void imprimirLogTabla(Integer i) {
        SQLiteDatabase db = conectar.getReadableDatabase();
        String[] columnas = Variables.CAMPOS_TABLAS[i];

        Cursor cursor = db.query(Variables.NOMBRE_TABLA[i], columnas, null, null, null, null, null);
        
        if (cursor.moveToFirst()) {
            do {
                StringBuilder rowData = new StringBuilder();
                rowData.append("Row Data: ");
                for (String columna : columnas) {
                    int columnIndex = cursor.getColumnIndex(columna);
                    String value = cursor.getString(columnIndex);
                    rowData.append(columna).append(": ").append(value).append(", ");
                }
                if (rowData.length() > 2) {
                    rowData.delete(rowData.length() - 2, rowData.length());
                }

                Log.d("TABLA "+Variables.NOMBRE_TABLA[i], rowData.toString());
            } while (cursor.moveToNext());
        }
        cursor.close();
    }
}