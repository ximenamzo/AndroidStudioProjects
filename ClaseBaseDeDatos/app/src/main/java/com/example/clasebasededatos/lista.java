package com.example.clasebasededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;

public class lista extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<String> listausuarios;
    ArrayList<Usuarios> datosusuario; // Array list de la clase Usuarios.java
    Conectar conectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Personas");

        lista = (ListView) findViewById(R.id.lista);
        mostrar();
        ArrayAdapter<String> aa =new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, listausuarios);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            // Intent intent = new Intent(lista.this, MainActivity.class);
            // startActivity(intent);
            finish();
        });
    }

    private void mostrar() {
        conectar = new Conectar(this, Variables.NOMBRE_BD, null, 1);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Usuarios usuario = null;
        datosusuario = new ArrayList<Usuarios>();
        Cursor cursor = bd.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA, null);

        if (cursor.moveToFirst()) {
            do {
                usuario = new Usuarios();
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setTelefono(cursor.getString(2));
                datosusuario.add(usuario);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Registros limpios.", Toast.LENGTH_SHORT).show();
            listausuarios = new ArrayList<String>();
            listausuarios.add("Sin registros.");
        }
        cursor.close();
        bd.close();
        agregarLista();
    }


    private void agregarLista() {
        listausuarios = new ArrayList<String>();
        for(int i = 0; i< datosusuario.size(); i++){
            listausuarios.add(
                    datosusuario.get(i).getId() + " | " +
                    datosusuario.get(i).getNombre() + " - " +
                    datosusuario.get(i).getTelefono()
            );
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuarios usuario = datosusuario.get(position);
        Intent ii = new Intent(this, detalle.class);
        Bundle b = new Bundle();
        b.putSerializable("usuario", usuario); // se empaqueta el objeto con la etiqueta usuario
        ii.putExtras(b);
        startActivity(ii);
    }
}