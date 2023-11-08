package com.ximenamzo.examenlibros.vistas.ventas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;

import java.util.ArrayList;

public class lista_ventas_custom extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayAdapter<String> adapter;
    ArrayList<String> listaventas;
    ArrayList<Integer> idsventas;
    ArrayList<String> idsVentas;
    Connect conectar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Log.d("DEBUG_35", "Entrando a listas_ventas_custom.java...");

        Intent intent = getIntent();
        String campo = intent.getStringExtra("campo");
        String dato = intent.getStringExtra("dato");
        idsVentas = intent.getStringArrayListExtra("idsVentas");


        Log.d("DEBUG_43", "Campo: "+ campo +", Dato: "+ dato);
        setTitle("Ventas relacionados con '" + dato + "'");
        TextView tituloLista = findViewById(R.id.tituloLista);
        tituloLista.setVisibility(View.VISIBLE);
        tituloLista.setText(String.format("Ventas relacionadas con '%s'", dato));

        lista = findViewById(R.id.lista);

        listaventas = new ArrayList<>();
        idsventas = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaventas);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());

        if (idsVentas != null) {
            mostrar(idsVentas);
        }
    }

    private void mostrar(ArrayList<String> idsVentas) {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        listaventas.clear();
        idsventas.clear();

        String listaIds = TextUtils.join(",", idsVentas);

        String consulta = "SELECT ventas.id AS VentaId, " +
                "clientes.nombre AS ClienteNombre, " +
                "libros.titulo AS LibroTitulo " +
                "FROM ventas " +
                "INNER JOIN clientes ON ventas.id_cliente = clientes.id " +
                "INNER JOIN libros ON ventas.id_libro = libros.id " +
                "WHERE ventas.id IN (" + listaIds + ")";
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
            listaventas.add("Sin registros.");
        }
        cursor.close();
        bd.close();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Integer idVenta = idsventas.get(position);
        Intent ii = new Intent(this, detalle_venta.class);
        ii.putExtra("idVenta", idVenta);
        //noinspection deprecation
        startActivityForResult(ii, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) mostrar(idsVentas);
        }
    }
}
