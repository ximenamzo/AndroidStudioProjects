package com.ximenamzo.examenlibros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;

public class MainLibros extends AppCompatActivity implements View.OnClickListener {

    EditText in_isbn, in_titulo, in_autor, in_editorial, in_paginas, in_precio;
    Button insert, searchTitulo, searchAutor, ver, limpiar;
    Connect conectar;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_libros);
        setTitle("Libros");

        in_isbn = (EditText) findViewById(R.id.etxid);
        in_titulo = (EditText) findViewById(R.id.etxtitulo);
        in_autor = (EditText) findViewById(R.id.etxautor);
        in_editorial = (EditText) findViewById(R.id.etxeditorial);
        in_paginas = (EditText) findViewById(R.id.etxpaginas);
        in_precio = (EditText) findViewById(R.id.etxprecio);

        insert = (Button) findViewById(R.id.btninsertar1);
        searchTitulo = (Button) findViewById(R.id.btnbuscar1);
        searchAutor = (Button) findViewById(R.id.btnbuscar2);
        ver = (Button) findViewById(R.id.btnver);
        limpiar = (Button) findViewById(R.id.btnlimpiar);

        insert.setOnClickListener(this);
        searchTitulo.setOnClickListener(this);
        searchAutor.setOnClickListener(this);
        ver.setOnClickListener(this);
        limpiar.setOnClickListener(this);

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
    }

    @Override
    public void onClick(View v) {
        if (v == limpiar) {
            in_isbn.setText("");
            in_titulo.setText("");
            in_autor.setText("");
            in_editorial.setText("");
            in_paginas.setText("");
            in_precio.setText("");
        }
        if (v == ver) {
            SQLiteDatabase bd = conectar.getReadableDatabase();
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM " + Variables.NOMBRE_TABLA[0], null);
            cursor.moveToFirst();
            int recordCount = cursor.getInt(0);
            cursor.close();
            bd.close();

            if (recordCount > 0) {
                i = new Intent(MainLibros.this, lista_libros.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay registros para mostrar.", Toast.LENGTH_LONG).show();
            }
        }

        String isbn = in_isbn.getText().toString();
        String titulo = in_titulo.getText().toString();
        String autor = in_autor.getText().toString();
        String editorial = in_editorial.getText().toString();
        String paginas = in_paginas.getText().toString();
        String precio = in_precio.getText().toString();

        if (v == insert) {
            if (!isbn.isEmpty() && !titulo.isEmpty() && !autor.isEmpty() && !editorial.isEmpty() && !paginas.isEmpty() && !precio.isEmpty()) {
                insertar();
            } else {
                Toast.makeText(this, "Ingrese todos los datos.", Toast.LENGTH_LONG).show();
            }
        }
        if(!titulo.isEmpty()){
            if(v == searchTitulo) buscarTitulo();
        }

        if(!autor.isEmpty()){
            if(v == searchAutor) buscarAutor();
        }
    }

    private void insertar() {
        // Método más seguro
        SQLiteDatabase db = conectar.getWritableDatabase();
        String isbn = in_isbn.getText().toString();

        if (isbnExiste(db, isbn)) {
            Toast.makeText(this, "ISBN ya registrado.", Toast.LENGTH_LONG).show();
        } else {
            ContentValues valores = new ContentValues();
            valores.put(Variables.CAMPO_ID2[0], in_isbn.getText().toString()); // isbn
            valores.put(Variables.CAMPO_TITULO, in_titulo.getText().toString()); //titulo
            valores.put(Variables.CAMPO_PERSONA[0], in_autor.getText().toString()); // autor
            valores.put(Variables.CAMPO_EDITORIAL, in_editorial.getText().toString()); // editorial
            valores.put(Variables.CAMPO_CANTIDADES[0], in_paginas.getText().toString());
            long id = db.insert(Variables.NOMBRE_TABLA[0], Variables.CAMPO_IDS[0],valores);
            if (id != -1) {
                Toast.makeText(this, "Registro exitoso con id "+id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ocurrió un error al registrar los datos.", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    private void buscarTitulo() {
        SQLiteDatabase bd = null;
        bd = conectar.getReadableDatabase();
        String titulo = null;
        titulo = in_titulo.getText().toString();
        String consulta = null;
        consulta = "SELECT " + Variables.CAMPO_ID2[0] + " FROM " + Variables.NOMBRE_TABLA[0] + " WHERE " + Variables.CAMPO_TITULO + " LIKE ?";

        try {
            Cursor cursor = null;
            cursor = bd.rawQuery(consulta, new String[]{"%" + titulo + "%"});
            if (cursor.getCount() > 1) {
                cursor.close();
                i = new Intent(MainLibros.this, lista_libros_custom.class);
                i.putExtra("titulo", titulo);
                startActivity(i);
            } else if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                Log.d("DEBUGXX", "Cursores tit 0: "+cursor.getString(0));
                String isbn = cursor.getString(0);
                cursor.close();
                i = new Intent(MainLibros.this, detalle_libro.class);
                i.putExtra("isbn", isbn);
                startActivity(i);
            } else {
                Toast.makeText(this, "No titulos que coincidan con " + titulo, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al buscar el titulo: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
    }

    private void buscarAutor() {
        SQLiteDatabase bd = null;
        bd = conectar.getReadableDatabase();
        String autor = null;
        autor = in_autor.getText().toString();
        String consulta = null;
        consulta = "SELECT " + Variables.CAMPO_ID2[0] + " FROM " + Variables.NOMBRE_TABLA[0] + " WHERE " + Variables.CAMPO_PERSONA[0] + " LIKE ?";

        try {
            Cursor cursor = null;
            cursor = bd.rawQuery(consulta, new String[]{"%" + autor + "%"});
            if (cursor.getCount() > 1) {
                cursor.close();
                i = new Intent(MainLibros.this, lista_libros_custom.class);
                i.putExtra("autor", autor);
                startActivity(i);
            } else if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                Log.d("DEBUGXX", "Cursores0: "+cursor.getString(0));
                String isbn = cursor.getString(0);
                cursor.close();
                i = new Intent(MainLibros.this, detalle_libro.class);
                i.putExtra("isbn", isbn);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay datos disponibles para el autor " + autor, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al buscar al autor: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
    }

    private boolean isbnExiste(SQLiteDatabase db, String isbn) {
        String[] parametros = {isbn};
        String[] campos = {Variables.CAMPO_ID2[0]};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[0], campos, Variables.CAMPO_ID2[0] + "=?", parametros, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }
}