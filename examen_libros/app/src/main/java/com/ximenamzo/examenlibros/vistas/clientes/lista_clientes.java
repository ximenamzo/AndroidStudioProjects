package com.ximenamzo.examenlibros.vistas.clientes;

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

import java.util.ArrayList;

public class lista_clientes extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<String> listaclientes;
    ArrayList<Clientes> datoscliente;
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
        setTitle("Clientes");

        TextView tituloLista = findViewById(R.id.tituloLista);
        tituloLista.setVisibility(View.VISIBLE);
        tituloLista.setText("Registros de Clientes");

        lista = findViewById(R.id.lista);
        mostrar();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaclientes);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());
    }

    private void mostrar() {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Clientes cliente;
        datoscliente = new ArrayList<>();
        Cursor cursor = bd.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA[1], null);

        if (cursor.moveToFirst()) {
            do {
                cliente = new Clientes();
                cliente.setId(cursor.getInt(0));
                cliente.setNombre(cursor.getString(1));
                cliente.setRfc(cursor.getString(2));
                datoscliente.add(cliente);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Registros limpios.", Toast.LENGTH_SHORT).show();
            listaclientes = new ArrayList<>();
            listaclientes.add("Sin registros.");
        }
        cursor.close();
        bd.close();

        listaclientes = new ArrayList<>();
        for(int i = 0; i< datoscliente.size(); i++){
            listaclientes.add(
                    datoscliente.get(i).getId() + " | " +
                            datoscliente.get(i).getNombre() + " - " +
                            datoscliente.get(i).getRfc());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Clientes cliente = datoscliente.get(position);
        Intent ii = new Intent(this, detalle_cliente.class);
        Bundle b = new Bundle();
        b.putSerializable("cliente", cliente);
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