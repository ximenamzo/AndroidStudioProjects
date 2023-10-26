package com.ximenamzo.examenlibros;

import androidx.appcompat.app.AlertDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Libros;

public class detalle_libro extends AppCompatActivity {
    TextView out_isbn, out_titulo, out_autor, out_editorial, out_paginas, txtid;
    EditText edit_isbn, edit_titulo, edit_autor, edit_editorial, edit_paginas;
    Button btneliminar, btneditar, btncancelar, btnguardar;
    private Libros libro;
    Connect conectar;
    String isbnSolo;
    LinearLayout editlayout, textlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_libro);
        setTitle("Detalles del Libro");

        editlayout = (LinearLayout) findViewById(R.id.editLayout);
        textlayout = (LinearLayout) findViewById(R.id.textLayout);

        txtid = (TextView) findViewById(R.id.txtid);
        out_isbn = (TextView) findViewById(R.id.txtisbn);
        out_titulo = (TextView) findViewById(R.id.txttitulo);
        out_autor = (TextView) findViewById(R.id.txtautor);
        out_editorial = (TextView) findViewById(R.id.txteditorial);
        out_paginas = (TextView) findViewById(R.id.txtpaginas);

        edit_isbn = (EditText) findViewById(R.id.editIsbn);
        edit_titulo = (EditText) findViewById(R.id.editTitulo);
        edit_autor = (EditText) findViewById(R.id.editAutor);
        edit_editorial = (EditText) findViewById(R.id.editEditorial);
        edit_paginas = (EditText) findViewById(R.id.editPaginas);

        btneliminar = (Button) findViewById(R.id.btnEliminar);
        btneditar = (Button) findViewById(R.id.btnEditar);
        btncancelar = (Button) findViewById(R.id.btnCancelar);
        btnguardar = (Button) findViewById(R.id.btnGuardar);

        conectar = new Connect(this, Variables.NOMBRE_BD, null, 1);

        Bundle objeto = getIntent().getExtras(); // trae el objeto
        if (objeto != null) {
            libro = (Libros) objeto.getSerializable("libro");
            isbnSolo = objeto.getString("isbn");

            if (libro != null) {
                Log.d("DEBUG_XX", libro.toString());
                mostrarDatos();
            } else if (isbnSolo != null) {
                buscarPorIsbn(isbnSolo);
            }

            btneditar.setOnClickListener(v -> editar());
            btnguardar.setOnClickListener(v -> guardarCambios());
            btncancelar.setOnClickListener(v -> cancelarEdicion());

            btneliminar.setOnClickListener(v -> {
                // cuadro de diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(detalle_libro.this)
                        .setTitle("Confirmar Eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar este libro?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarLibro())
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss(); // esto no hacer nada, cierra el cuadro de diálogo
                        }).show();
            });
        } else {
            out_isbn.setText("No hay datos para mostrar.");
        }

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void mostrarDatos() {
        editlayout.setVisibility(View.GONE);
        textlayout.setVisibility(View.VISIBLE);
        if (libro != null) {
            out_isbn.setText(libro.getIsbn());
            out_titulo.setText(libro.getTitulo());
            out_autor.setText(libro.getAutor());
            out_editorial.setText(libro.getEditorial());
            out_paginas.setText(String.valueOf(libro.getPaginas()));
        } else {
            // Oculta editText y sus botones Guardar y Cancelar
            edit_titulo.setVisibility(View.GONE);
            edit_autor.setVisibility(View.GONE);
            edit_editorial.setVisibility(View.GONE);
            edit_paginas.setVisibility(View.GONE);
        }
        btnguardar.setVisibility(View.GONE);
        btncancelar.setVisibility(View.GONE);
        out_titulo.setVisibility(View.VISIBLE);
        out_isbn.setVisibility(View.VISIBLE);
        out_autor.setVisibility(View.VISIBLE);
        out_editorial.setVisibility(View.VISIBLE);
        out_paginas.setVisibility(View.VISIBLE);
        btneliminar.setVisibility(View.VISIBLE);
        btneditar.setVisibility(View.VISIBLE);
    }

    private void editar() {
        // copia los datos a los editText
        txtid.setText("Id del registro: " + libro.getId());
        edit_titulo.setText(libro.getTitulo());
        edit_isbn.setText(libro.getIsbn());
        edit_autor.setText(libro.getAutor());
        edit_editorial.setText(libro.getEditorial());
        edit_paginas.setText(String.valueOf(libro.getPaginas()));

        // oculto los textViews y botones
        textlayout.setVisibility(View.GONE);
        out_titulo.setVisibility(View.GONE);
        out_isbn.setVisibility(View.GONE);
        out_autor.setVisibility(View.GONE);
        out_editorial.setVisibility(View.GONE);
        out_paginas.setVisibility(View.GONE);
        btneliminar.setVisibility(View.GONE);
        btneditar.setVisibility(View.GONE);

        // Muestra los campos de edición y los botones Guardar y Cancelar
        editlayout.setVisibility(View.VISIBLE);
        edit_titulo.setVisibility(View.VISIBLE);
        edit_isbn.setVisibility(View.VISIBLE);
        edit_autor.setVisibility(View.VISIBLE);
        edit_editorial.setVisibility(View.VISIBLE);
        edit_paginas.setVisibility(View.VISIBLE);
        btnguardar.setVisibility(View.VISIBLE);
        btncancelar.setVisibility(View.VISIBLE);
    }

    private void guardarCambios() {
        // Actualiza el libro con los datos editados
        libro.setIsbn(edit_isbn.getText().toString());
        libro.setTitulo(edit_titulo.getText().toString());
        libro.setAutor(edit_autor.getText().toString());
        libro.setEditorial(edit_editorial.getText().toString());
        libro.setPaginas(Integer.parseInt(edit_paginas.getText().toString()));

        // BD aquí
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {String.valueOf(libro.getId())};
        //String[] campos = {Variables.CAMPO_ID, Variables.CAMPO_ISBN, Variables.CAMPO_TITULO, Variables.CAMPO_AUTOR, Variables.CAMPO_EDITORIAL, Variables.CAMPO_PAGINAS};
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_ID2[0], edit_isbn.getText().toString());
        valores.put(Variables.CAMPO_TITULO, edit_titulo.getText().toString());
        valores.put(Variables.CAMPO_PERSONA[0], edit_autor.getText().toString());
        valores.put(Variables.CAMPO_EDITORIAL, edit_editorial.getText().toString());
        valores.put(Variables.CAMPO_CANTIDADES[0], edit_paginas.getText().toString());
        bd.update(Variables.NOMBRE_TABLA[0], valores, Variables.CAMPO_IDS[0] + "=?", parametros);
        Toast.makeText(this, "Registro actualizado.", Toast.LENGTH_LONG).show();
        bd.close();

        // Muestra los datos actualizados y regresa a la "vista" de detalles
        editlayout.setVisibility(View.GONE);
        btnguardar.setVisibility(View.GONE);
        btncancelar.setVisibility(View.GONE);
        textlayout.setVisibility(View.VISIBLE);
        mostrarDatos();
    }

    private void cancelarEdicion() {
        editlayout.setVisibility(View.GONE);
        mostrarDatos(); // ignora cambios y muestra los datos originales
    }

    private void eliminarLibro() {
        String isbn = out_isbn.getText().toString();
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {isbn};
        int n = bd.delete(Variables.NOMBRE_TABLA[0],Variables.CAMPO_ID2[0] + "=?", parametros);
        Toast.makeText(this,"Usuarios eliminados: " + n, Toast.LENGTH_LONG).show();
        bd.close();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void buscarPorIsbn(String isbn) {
        if (libro == null) libro = new Libros();
        SQLiteDatabase bd = conectar.getReadableDatabase();
        String[] parametros = {isbn};
        String[] campos = {Variables.CAMPO_IDS[0],Variables.CAMPO_ID2[0],Variables.CAMPO_TITULO,
                Variables.CAMPO_PERSONA[0],Variables.CAMPO_EDITORIAL,Variables.CAMPO_CANTIDADES[0]};

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA[0], campos, Variables.CAMPO_ID2[0] + " =?", parametros, null, null, null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                Log.d("DEBUGXX", "ISBN encontrado: "+ cursor.getString(0) + ". Con el ID: "+ cursor.getString(1)+".");
                libro.setId(Integer.valueOf(cursor.getString(0)));
                libro.setIsbn(cursor.getString(1));
                libro.setTitulo(cursor.getString(2));
                libro.setAutor(cursor.getString(3));
                libro.setEditorial(cursor.getString(4));
                libro.setPaginas(Integer.valueOf(cursor.getString(5)));
                cursor.close();
            } else {
                Toast.makeText(this, "No se encontraron datos para ISBN: " + isbn, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Error al obtener datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
        mostrarDatos();
    }
}