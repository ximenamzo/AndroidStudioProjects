package com.ximenamzo.examenlibros;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText in_isbn, in_titulo, in_autor, in_editorial, in_paginas;
    Button insert, searchTitulo, searchAutor, ver, limpiar;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sistema de Librería");

        in_isbn = (EditText) findViewById(R.id.etxid);
        in_titulo = (EditText) findViewById(R.id.etxtitulo);
        in_autor = (EditText) findViewById(R.id.etxautor);
        in_editorial = (EditText) findViewById(R.id.etxeditorial);
        in_paginas = (EditText) findViewById(R.id.etxpaginas);

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

    }

    @Override
    public void onClick(View v) {

    }
}