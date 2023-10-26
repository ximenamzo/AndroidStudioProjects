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

public class lista_custom extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lista;
    TextInputLayout menuOrden;
    AutoCompleteTextView itemOrden;
    ArrayList<String> listausuarios;
    ArrayList<Usuarios> datosusuario;
    Connect conexion;
    public String busqueda = "", campo = "", orden;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("nombre")) {
                busqueda = extras.getString("nombre");
                campo = Variables.CAMPO_NOMBRE;
            } else if (extras.containsKey("apellido")) {
                busqueda = extras.getString("apellido");
                campo = Variables.CAMPO_APELLIDO;
            } else if (extras.containsKey("edad")) {
                busqueda = extras.getString("edad");
                campo = Variables.CAMPO_EDAD;
            } else if (extras.containsKey("sexo")) {
                busqueda = extras.getString("sexo");
                campo = Variables.CAMPO_SEXO;
            } else if (extras.containsKey("fenac")) {
                busqueda = extras.getString("fenac");
                campo = Variables.CAMPO_FENAC;
            } else if (extras.containsKey("estatura")) {
                busqueda = extras.getString("estatura");
                campo = Variables.CAMPO_ESTATURA;
            } else if (extras.containsKey("telefono")) {
                busqueda = extras.getString("telefono");
                campo = Variables.CAMPO_TELEFONO;
            }
        }

        setTitle("Resultados de la búsqueda: '" + busqueda + "'");

        lista = (ListView) findViewById(R.id.lista);

        menuOrden = findViewById(R.id.menuorden);
        itemOrden = menuOrden.findViewById(R.id.itemorden);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_orden, R.layout.custom_item);
        adapter.setDropDownViewResource(R.layout.custom_dropdown_item);
        itemOrden.setAdapter(adapter);
        itemOrden.setOnItemClickListener((parent, view, position, id) -> {
            orden = (String) parent.getItemAtPosition(position);

            if ("Nombre".equals(orden)) {
                mostrar(busqueda, campo, orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Apellido".equals(orden)) {
                mostrar(busqueda, campo, orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Edad".equals(orden)) {
                mostrar(busqueda, campo, orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else if ("Estatura".equals(orden)) {
                mostrar(busqueda, campo, orden.toLowerCase());
                ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
                lista.setAdapter(aa);
            } else {
                mostrar(busqueda, campo, "id");
            }
        });

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> finish());

        conexion = new Connect(this, Variables.NOMBRE_BD, null, 1);

        mostrar(busqueda, campo, "id");
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
        lista.setAdapter(aa);
        lista.setOnItemClickListener(this);
    }

    private void mostrar(String busqueda, String campo, String orden) {
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Usuarios usuario = null;
        datosusuario = new ArrayList<Usuarios>();
        String[] campos = {Variables.CAMPO_ID, Variables.CAMPO_NOMBRE, Variables.CAMPO_APELLIDO, Variables.CAMPO_EDAD, Variables.CAMPO_SEXO, Variables.CAMPO_FENAC, Variables.CAMPO_ESTATURA, Variables.CAMPO_TELEFONO};
        String whereLike = campo + " LIKE ?";

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA, Variables.CAMPO_AUX, whereLike, new String[]{"%" + busqueda + "%"}, null, null, orden);
            if (cursor.getCount() > 0) {
                Toast.makeText(this, "Registros encontrados: " + cursor.getCount(), Toast.LENGTH_SHORT).show();
                datosusuario.clear();

                while (cursor.moveToNext()) {
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
                }
            } else {
                Toast.makeText(this, "Sin registros.", Toast.LENGTH_SHORT).show();
                datosusuario.clear();
            }
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this, "Error al acceder a la BD.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();

        Log.d("DEBUG_XX", "Orden: " + orden);
        if ("apellido".equals(orden)) {
            agregarLista(1);
        } else {
            agregarLista(2);
        }
        ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.item_lista, listausuarios);
        lista.setAdapter(aa);
    }

    private void agregarLista(Integer op) {
        listausuarios = new ArrayList<>();
        if (op!=1) {
            for(int i = 0; i< datosusuario.size(); i++){
                listausuarios.add(datosusuario.get(i).getNombre() + " " + datosusuario.get(i).getApellido() + "\n" +
                        datosusuario.get(i).getSexo() + ", " + datosusuario.get(i).getEdad() + " años, " +
                        datosusuario.get(i).getEstatura() + " cm.\n" + datosusuario.get(i).getTelefono());
            }
        } else {
            for(int i = 0; i< datosusuario.size(); i++){
                listausuarios.add(datosusuario.get(i).getApellido() + " " + datosusuario.get(i).getNombre() + "\n" +
                        datosusuario.get(i).getSexo() + ", " + datosusuario.get(i).getEdad() + " años, " +
                        datosusuario.get(i).getEstatura() + " cm.\n" + datosusuario.get(i).getTelefono());
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
            if (resultCode == RESULT_OK) mostrar(busqueda, campo, "id");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mostrar(busqueda, campo, "id");
    }
}