package ximenamzo.a7bdusuarios;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import ximenamzo.a7bdusuarios.bd.Connect;
import ximenamzo.a7bdusuarios.bd.Variables;
import ximenamzo.a7bdusuarios.models.Usuarios;

public class lista extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    TextInputLayout menuOrden;
    AutoCompleteTextView itemOrden;
    ArrayList<String> listausuarios;
    ArrayList<Usuarios> datosusuario; // Array list de la clase Usuarios.java
    Connect conexion;
    String orden;
    @Override
    protected void onResume() {
        super.onResume();
        mostrar("id");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);
        setTitle("Usuarios");

        lista = (ListView) findViewById(R.id.lista);
        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());

        menuOrden = findViewById(R.id.menuorden);
        itemOrden = menuOrden.findViewById(R.id.itemorden);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_orden, R.layout.custom_item);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_item);
        itemOrden.setAdapter(adapter);
        itemOrden.setOnItemClickListener((parent, view, position, id) -> {
            orden = (String) parent.getItemAtPosition(position);

            if ("Nombre".equals(orden)) {
                mostrar(orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Apellido".equals(orden)) {
                mostrar(orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Edad".equals(orden)) {
                mostrar(orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Estatura".equals(orden)) {
                mostrar(orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else {
                mostrar("id");
            }
        });

        conexion = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);

        mostrar("id");
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);
    }

    private void mostrar(String orden) {
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Usuarios usuario = null;
        datosusuario = new ArrayList<Usuarios>();
        Cursor cursor = bd.rawQuery("SELECT * FROM " + Variables.NOMBRE_TABLA + " ORDER BY " + orden + " ASC", null);

        if (cursor.moveToFirst()) {
            do {
                usuario = new Usuarios();
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setApellido(cursor.getString(2));
                usuario.setEdad(Integer.valueOf(cursor.getString(3)));
                usuario.setSexo(cursor.getString(4));
                usuario.setFenac(cursor.getString(5));
                usuario.setEstatura(Integer.valueOf(cursor.getString(6)));
                usuario.setTelefono(cursor.getString(7));
                datosusuario.add(usuario);
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "Registros limpios: no hay registros.", Toast.LENGTH_SHORT).show();
            listausuarios = new ArrayList<String>();
            listausuarios.add("Sin registros.");
        }
        cursor.close();
        bd.close();

        Log.d("DEBUG_XX", "Orden: " + orden);
        if ("apellido".equals(orden)) {
            agregarLista(1);
        } else {
            agregarLista(2);
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
        lista.setAdapter(aa);
        Log.d("DEBUG_XX", "Lista: " + lista.toString());
    }


    private void agregarLista(Integer op) {
        listausuarios = new ArrayList<>();
        if (op!=1) {
            for(int i = 0; i< datosusuario.size(); i++){
                listausuarios.add(datosusuario.get(i).getNombre() + " " + datosusuario.get(i).getApellido() + "\n" +
                        datosusuario.get(i).getSexo() + ", " + datosusuario.get(i).getEdad() + " años, " +
                        datosusuario.get(i).getEstatura() + " cm.\n" + datosusuario.get(i).getTelefono());
                Log.d("DEBUG_XX", "ListaUsuario: " + listausuarios.toString());
            }
        } else {
            for(int i = 0; i< datosusuario.size(); i++){
                listausuarios.add(datosusuario.get(i).getApellido() + " " + datosusuario.get(i).getNombre() + "\n" +
                        datosusuario.get(i).getSexo() + ", " + datosusuario.get(i).getEdad() + " años, " +
                        datosusuario.get(i).getEstatura() + " cm.\n" + datosusuario.get(i).getTelefono());
                Log.d("DEBUG_XX", "ListaUsuario: " + listausuarios.toString());
            }
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Usuarios usuario = datosusuario.get(position);
        Intent ii = new Intent(this, detalle.class);
        Bundle b = new Bundle();
        b.putSerializable("usuario", usuario); // se empaqueta el objeto con la etiqueta usuario
        ii.putExtras(b);
        startActivityForResult(ii, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) mostrar("id");
        }
    }
}