package com.ximenamzo.actividad8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText e1, e2;
    Button b1;
    TextView txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        e1 = findViewById(R.id.etxNombre);
        e2 = findViewById(R.id.etxApellido);
        b1 = findViewById(R.id.btn);
        registerForContextMenu(e1);
        registerForContextMenu(e2);
        registerForContextMenu(b1);
        txt = findViewById(R.id.txt);
        b1.setOnClickListener(this);
    }

    // agregamos menu principal
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.m1) {
            Toast.makeText(this, "Opción configuración", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.m2) {
            Toast.makeText(this, "Opción mic", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.m3) {
            Toast.makeText(this, "Opción salir", Toast.LENGTH_SHORT).show();
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        int id = v.getId();
        if (id == R.id.nombre) {
            getMenuInflater().inflate(R.menu.menu_nombre, menu);
        } else if (id == R.id.apellido) {
            getMenuInflater().inflate(R.menu.menu_apellido, menu);
        } else if (id == R.id.btn) {
            getMenuInflater().inflate(R.menu.menu_boton, menu);
        } else {
            throw new IllegalStateException("Unexpected value: " + v.getId());
        }
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.i1) {
            Toast.makeText(this, "Click en Nombre opción 1", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.i2) {
            Toast.makeText(this, "Click en Nombre opción 2", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.s1) {
            Toast.makeText(this, "Click en Apellido opción 1", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.s2) {
            Toast.makeText(this, "Click en Apellido opción 2", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.btnmenu1) {
            Toast.makeText(this, "Click en Botón opción Btn Menú 1", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.btnmenu2) {
            Toast.makeText(this, "Click en Botón opción Btn Menú 2", Toast.LENGTH_SHORT).show();
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn) {
            b1.setText("Click en el botón");
            txt.setText("Hola Munda!");
            Toast.makeText(this, "Hola", Toast.LENGTH_SHORT).show();
        }
    }
}