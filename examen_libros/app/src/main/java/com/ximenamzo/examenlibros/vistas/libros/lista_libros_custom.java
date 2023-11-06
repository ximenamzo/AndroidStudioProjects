package com.ximenamzo.examenlibros.vistas.libros;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Libros;

import java.util.ArrayList;

public class lista_libros_custom extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<String> listalibros;
    ArrayList<Libros> datoslibro;
    Connect conectar;
    private String busqueda = "", campo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            campo = extras.getString("campo");
            busqueda = extras.getString("dato");
        }

        setTitle("Libros relacionados con '" + busqueda + "'");

        lista = findViewById(R.id.lista);

        listalibros = new ArrayList<>();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listalibros);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());

        mostrar(busqueda, campo);
    }

    private void mostrar(String busqueda, String campo) {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Libros libro;
        datoslibro = new ArrayList<>();
        String[] campos = Variables.CAMPOS_TABLAS[0];
        String whereLike = campo + " LIKE ?";

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA[0], campos, whereLike, new String[]{"%" + busqueda + "%"}, null, null, null);
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Registros encontrados: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                datoslibro.clear();

                while (cursor.moveToNext()) {
                    libro = new Libros();
                    libro.setId(Integer.valueOf(cursor.getString(0)));
                    libro.setIsbn(cursor.getString(1));
                    libro.setTitulo(cursor.getString(2));
                    libro.setAutor(cursor.getString(3));
                    libro.setEditorial(cursor.getString(4));
                    libro.setPaginas(Integer.valueOf(cursor.getString(5)));
                    libro.setPrecio(Double.valueOf(cursor.getString(6)));
                    datoslibro.add(libro);
                }
            } else {
                Toast.makeText(this, "Sin registros.", Toast.LENGTH_SHORT).show();
                datoslibro.clear();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al acceder a la BD.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();

        ArrayAdapter<String> aa = (ArrayAdapter<String>) lista.getAdapter();
        aa.clear(); // Limpia la lista existente por si a caso
        for (int i = 0; i < datoslibro.size(); i++) {
            aa.add(
                    datoslibro.get(i).getId() + " | " +
                            datoslibro.get(i).getTitulo() + " - " +
                            datoslibro.get(i).getAutor()
            );
        }
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
            if (resultCode == RESULT_OK) mostrar(busqueda, campo);
        }
    }
}