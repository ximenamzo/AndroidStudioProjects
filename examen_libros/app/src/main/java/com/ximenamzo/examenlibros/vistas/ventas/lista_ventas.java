package com.ximenamzo.examenlibros.vistas.ventas;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Clientes;
import com.ximenamzo.examenlibros.modelos.Libros;
import com.ximenamzo.examenlibros.modelos.Ventas;

import java.util.ArrayList;

public class lista_ventas extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ArrayList<String> listaventas;
    ArrayList<Integer> idsventas;
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

        TextView tituloLista = findViewById(R.id.tituloLista);
        tituloLista.setVisibility(View.VISIBLE);
        tituloLista.setText("Registros de Ventas");

        ListView lista = findViewById(R.id.lista);
        idsventas = new ArrayList<>();
        mostrar();

        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaventas);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());
    }

    private void mostrar() {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        listaventas = new ArrayList<>();

        String consulta = "SELECT ventas.id AS VentaId, " +
                           "clientes.nombre AS ClienteNombre, " +
                             "libros.titulo AS LibroTitulo " +
                          "FROM ventas " +
                              "INNER JOIN clientes ON ventas.id_cliente = clientes.id " +
                                "INNER JOIN libros ON ventas.id_libro = libros.id";
        Cursor cursor = bd.rawQuery(consulta, null);

        if (cursor.moveToFirst()) {
            do {
                int ventaId = cursor.getInt(0);
                String clienteNombre = cursor.getString(1);
                String libroTitulo = cursor.getString(2);
                listaventas.add(ventaId + " |  " + clienteNombre + " compró: " + libroTitulo);
                idsventas.add(ventaId);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No hay registros de ventas.", Toast.LENGTH_SHORT).show();
            listaventas = new ArrayList<>();
            listaventas.add("Sin registros.");
        }
        cursor.close();
        bd.close();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer idVenta = idsventas.get(position);
        Intent ii = new Intent(this, detalle_venta.class);
        ii.putExtra("idVenta", idVenta);
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