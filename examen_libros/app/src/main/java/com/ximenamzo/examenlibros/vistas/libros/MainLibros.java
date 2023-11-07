package com.ximenamzo.examenlibros.vistas.libros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.DB;

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

        in_isbn = findViewById(R.id.etxid);
        in_titulo = findViewById(R.id.etxtitulo);
        in_autor = findViewById(R.id.etxautor);
        in_editorial = findViewById(R.id.etxeditorial);
        in_paginas = findViewById(R.id.etxpaginas);
        in_precio = findViewById(R.id.etxprecio);

        insert = findViewById(R.id.btninsertar1);
        searchTitulo = findViewById(R.id.btnbuscar1);
        searchAutor = findViewById(R.id.btnbuscar2);
        ver = findViewById(R.id.btnver);
        limpiar = findViewById(R.id.btnlimpiar);

        insert.setOnClickListener(this);
        searchTitulo.setOnClickListener(this);
        searchAutor.setOnClickListener(this);
        ver.setOnClickListener(this);
        limpiar.setOnClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

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
            if (!isbn.isEmpty() && !titulo.isEmpty() && !autor.isEmpty() &&
                    !editorial.isEmpty() && !paginas.isEmpty() && !precio.isEmpty()) {
                insertar();
            } else {
                Toast.makeText(this, "Ingrese los datos restantes.", Toast.LENGTH_LONG).show();
            }
        }

        if(v == searchTitulo){
            if(!titulo.isEmpty()) {
                DB.buscar(this, in_titulo.getText().toString(),Variables.NOMBRE_TABLA[0],Variables.CAMPO_TITULO);
            } else {
                Toast.makeText(this, "Ingrese un titulo para buscar.", Toast.LENGTH_SHORT).show();
            }
        }

        if(v == searchAutor){
            if(!autor.isEmpty()) {
                DB.buscar(this, in_autor.getText().toString(),Variables.NOMBRE_TABLA[0],Variables.CAMPO_PERSONA[0]);
            } else {
                Toast.makeText(this, "Ingrese un autor para buscar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertar() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        String isbn = in_isbn.getText().toString();

        if (isbnExiste(db, isbn)) {
            Toast.makeText(this, "ISBN ya registrado.", Toast.LENGTH_SHORT).show();
        } else {
            ContentValues valores = new ContentValues();
            valores.put(Variables.CAMPO_ID2[0], in_isbn.getText().toString()); // isbn
            valores.put(Variables.CAMPO_TITULO, in_titulo.getText().toString()); //titulo
            valores.put(Variables.CAMPO_PERSONA[0], in_autor.getText().toString()); // autor
            valores.put(Variables.CAMPO_EDITORIAL, in_editorial.getText().toString()); // editorial
            valores.put(Variables.CAMPO_CANTIDADES[0], in_paginas.getText().toString()); // paginas
            valores.put(Variables.CAMPO_DINERO[0], in_precio.getText().toString()); // precio
            long id = db.insert(Variables.NOMBRE_TABLA[0], Variables.CAMPO_IDS[0], valores);
            if (id != -1) {
                Toast.makeText(this, "Registro exitoso con id "+id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ocurrió un error al registrar los datos.", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
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