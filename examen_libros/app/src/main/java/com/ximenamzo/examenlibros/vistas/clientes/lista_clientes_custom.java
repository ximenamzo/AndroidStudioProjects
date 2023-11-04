package com.ximenamzo.examenlibros.vistas.clientes;

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
import com.ximenamzo.examenlibros.modelos.Clientes;

import java.util.ArrayList;

public class lista_clientes_custom extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    ArrayList<String> listaclientes;
    ArrayList<Clientes> datoscliente;
    Connect conectar;
    private String busqueda = "", campo = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("titulo")) {
                busqueda = extras.getString("titulo");
                campo = Variables.CAMPO_TITULO;
            } else if (extras.containsKey("autor")) {
                busqueda = extras.getString("autor");
                campo = Variables.CAMPO_PERSONA[0];
            }
        }

        setTitle("Clientes relacionados con '" + busqueda + "'");

        lista = findViewById(R.id.lista);

        listaclientes = new ArrayList<>();
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, listaclientes);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());

        mostrar(busqueda, campo);
    }

    private void mostrar(String busqueda, String campo) {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();
        Clientes cliente;
        datoscliente = new ArrayList<>();
        String[] campos = {Variables.CAMPO_IDS[0], Variables.CAMPO_PERSONA[1], Variables.CAMPO_ID2[1]};
        String whereLike = campo + " LIKE ?";

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA[1], campos, whereLike, new String[]{"%" + busqueda + "%"}, null, null, null);
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Registros encontrados: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                datoscliente.clear();

                while (cursor.moveToNext()) {
                    cliente = new Clientes();
                    cliente.setId(Integer.valueOf(cursor.getString(0)));
                    cliente.setNombre(cursor.getString(1));
                    cliente.setRfc(cursor.getString(2));
                    datoscliente.add(cliente);
                }
            } else {
                Toast.makeText(this, "Sin registros.", Toast.LENGTH_SHORT).show();
                datoscliente.clear();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al acceder a la BD.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();

        agregarLista();
    }

    private void agregarLista() {
        ArrayAdapter<String> aa = (ArrayAdapter<String>) lista.getAdapter();
        aa.clear(); // Limpia la lista existente por si a caso
        for (int i = 0; i < datoscliente.size(); i++) {
            aa.add(
                    datoscliente.get(i).getId() + " | " +
                            datoscliente.get(i).getNombre() + " - " +
                            datoscliente.get(i).getRfc()
            );
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Clientes cliente = datoscliente.get(position);
        Intent ii = new Intent(this, detalle_cliente.class);
        Bundle b = new Bundle();
        b.putSerializable("cliente", cliente); // se empaqueta el objeto con la etiqueta cliente
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