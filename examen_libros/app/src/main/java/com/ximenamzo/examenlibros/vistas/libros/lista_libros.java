package com.ximenamzo.examenlibros.vistas.libros;

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
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Libros;

import java.util.ArrayList;

public class lista_libros extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<String> listalibros;
    ArrayList<Libros> datoslibro; // Array list de la clase Libros.java
    Connect conectar;
    @Override
    protected void onResume() {
        super.onResume();
        mostrar();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Libros");

        lista = findViewById(R.id.lista);
        mostrar();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listalibros);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());
    }

    private void mostrar() {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Libros libro;
        datoslibro = new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA[0], null);

        if (cursor.moveToFirst()) {
            do {
                libro = new Libros();
                libro.setId(cursor.getInt(0));
                libro.setIsbn(cursor.getString(1));
                libro.setTitulo(cursor.getString(2));
                libro.setAutor(cursor.getString(3));
                libro.setEditorial(cursor.getString(4));
                libro.setPaginas(Integer.valueOf(cursor.getString(5)));
                libro.setPrecio(Double.valueOf(cursor.getString(6)));
                datoslibro.add(libro);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Registros limpios.", Toast.LENGTH_SHORT).show();
            listalibros = new ArrayList<>();
            listalibros.add("Sin registros.");
        }
        cursor.close();
        bd.close();

        listalibros = new ArrayList<>();
        for(int i = 0; i< datoslibro.size(); i++){
            listalibros.add(
                    datoslibro.get(i).getId() + " | " +
                            datoslibro.get(i).getTitulo() + " - " +
                            datoslibro.get(i).getAutor()
            );
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listalibros);
        lista.setAdapter(aa);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Libros libro = datoslibro.get(position);
        Intent ii = new Intent(this, detalle_libro.class);
        Bundle b = new Bundle();
        b.putSerializable("libro", libro); // se empaqueta el objeto con la etiqueta libro
        ii.putExtras(b);
        startActivityForResult(ii, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK)
                mostrar();
        }
    }
}