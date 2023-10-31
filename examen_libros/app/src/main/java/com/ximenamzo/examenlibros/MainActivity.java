package com.ximenamzo.examenlibros;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.ximenamzo.examenlibros.vistas.libros.MainLibros;
import com.ximenamzo.examenlibros.vistas.clientes.MainClientes;
import com.ximenamzo.examenlibros.vistas.ventas.MainVentas;

public class MainActivity extends AppCompatActivity {
    Button libros, clientes, ventas;
    Intent i;
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
    }

    private void processIntent(Class<?> activityClass){
            i = new Intent(MainActivity.this, activityClass);
            startActivity(i);
    }
}