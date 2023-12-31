package ximenamzo.a7bdusuarios;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import ximenamzo.a7bdusuarios.bd.Connect;
import ximenamzo.a7bdusuarios.bd.Variables;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText in_nombre, in_apellido, in_edad, in_telefono, in_estatura, in_cumple;
    Button ins1, ver, limpiar, buscar;
    TextInputLayout menuSexo;
    AutoCompleteTextView itemSexo;
    String nombre, apellido, edad, telefono, estatura, fenac;
    public String sexo;
    Connect conexion;
    Intent i;
    Integer favorito = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Agenda");

        in_nombre = findViewById(R.id.etxnombre);
        in_apellido = findViewById(R.id.etxapellido);
        in_edad = findViewById(R.id.etxedad);
        in_telefono = findViewById(R.id.etxtelefono);
        in_estatura = findViewById(R.id.etxestatura);
        in_cumple = findViewById(R.id.etxfenac);
        ins1 = findViewById(R.id.btninsertar1);
        ver = findViewById(R.id.btnver);
        limpiar = findViewById(R.id.btnlimpiar);
        buscar = findViewById(R.id.btnbuscar);
        MaterialButton button = findViewById(R.id.btncalendario);

        ins1.setOnClickListener(this);
        ver.setOnClickListener(this);
        limpiar.setOnClickListener(this);
        buscar.setOnClickListener(this);

        button.setOnClickListener(v -> {
            MaterialDatePicker<Long> materialDatePicker = MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Fecha de Nacimiento")
                    .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                    .build();
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                String fecha = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date(selection));
                in_cumple.setText(fecha);
            });
            materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER_TAG");
        });

        menuSexo = findViewById(R.id.menusexo);
        itemSexo = menuSexo.findViewById(R.id.itemsexo);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.items_sexo, R.layout.custom_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemSexo.setAdapter(adapter);
        itemSexo.setOnItemClickListener((parent, view, position, id) -> sexo = parent.getItemAtPosition(position).toString());
        Log.d("DEBUGXX", "Sexo seleccionado: "+sexo);

        conexion = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemConf) {
            Toast.makeText(this, "Opción configuración", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.itemInfo) {
            Toast.makeText(this, "Creado por Ximena Manzo", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.itemReporte) {
            Toast.makeText(this, "No puedes reportar la app lero lero", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.itemFav) {
            favorito++;
            Toast.makeText(this, "Haz dado "+favorito+" likes", Toast.LENGTH_SHORT).show();
        } else {
            throw new IllegalStateException("Unexpected value: " + item.getItemId());
        }
        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == limpiar) {
            in_nombre.setText("");
            in_apellido.setText("");
            in_edad.setText("");
            in_telefono.setText("");
            in_estatura.setText("");
            in_cumple.setText("");
            itemSexo.setText("");
        }

        if (v == ver) {
            SQLiteDatabase bd = conexion.getReadableDatabase();
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM " + Variables.NOMBRE_TABLA, null);
            cursor.moveToFirst();
            int recordCount = cursor.getInt(0);
            cursor.close();
            bd.close();

            if (recordCount > 0) {
                i = new Intent(MainActivity.this, lista.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay registros para mostrar.", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == ins1) {
            nombre = in_nombre.getText().toString();
            apellido = in_apellido.getText().toString();
            edad = in_edad.getText().toString();
            telefono = in_telefono.getText().toString();
            estatura = in_estatura.getText().toString();
            fenac = in_cumple.getText().toString();
            if (!nombre.isEmpty() || !apellido.isEmpty() || !edad.isEmpty() || !telefono.isEmpty() || !estatura.isEmpty() || !fenac.isEmpty()) {
                insertar();
            } else {
                Toast.makeText(this,"Ingresa todos los datos.", Toast.LENGTH_SHORT).show();
            }
        }

        if (v == buscar) {
            Log.d("DEBUG_MAIN159", "Click en buscar");
            nombre = in_nombre.getText().toString();
            apellido = in_apellido.getText().toString();
            edad = in_edad.getText().toString();
            telefono = in_telefono.getText().toString();
            estatura = in_estatura.getText().toString();
            fenac = in_cumple.getText().toString();

            if (nombre != null && !nombre.isEmpty()) {
                Log.d("DEBUG_MAIN168", "Buscando por nombre");
                busqueda(nombre, 1);
            } else if (apellido != null && !apellido.isEmpty()) {
                Log.d("DEBUG_MAIN171", "Buscando por apellido");
                busqueda(apellido, 2);
            } else if (sexo != null && !sexo.isEmpty()) {
                Log.d("DEBUG_MAIN174", "Buscando por sexo");
                busqueda(sexo, 4);
            } else if (edad != null && !edad.isEmpty()) {
                Log.d("DEBUG_MAIN177", "Buscando por edad");
                busqueda(edad, 3);
            } else if (fenac != null && !fenac.isEmpty()) {
                Log.d("DEBUG_MAIN180", "Buscando por fecha de nacimiento");
                String[] partes = fenac.split("-");
                String mes = partes[1];
                busqueda(mes, 5);
            } else if (telefono != null && !telefono.isEmpty()) {
                Log.d("DEBUG_MAIN185", "Buscando por telefono");
                busqueda(telefono, 7);
            } else if (estatura != null && !estatura.isEmpty()) {
                Log.d("DEBUG_MAIN188", "Buscando por estatura");
                busqueda(estatura, 6);
            } else {
                Toast.makeText(this, "Ingresa un campo para buscar!!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertar() {
        SQLiteDatabase db = conexion.getWritableDatabase();
        ContentValues valores = new ContentValues();
        //valores.put(Variables.CAMPO_NOMBRE, in_nombre.getText().toString());
        valores.put(Variables.CAMPO_NOMBRE, nombre);
        valores.put(Variables.CAMPO_APELLIDO, apellido);
        valores.put(Variables.CAMPO_EDAD, edad);
        valores.put(Variables.CAMPO_SEXO, sexo);
        valores.put(Variables.CAMPO_FENAC, fenac);
        valores.put(Variables.CAMPO_ESTATURA, estatura);
        valores.put(Variables.CAMPO_TELEFONO, telefono);
        long id = db.insert(Variables.NOMBRE_TABLA, Variables.CAMPO_ID, valores);
        Toast.makeText(this, "id:"+id, Toast.LENGTH_SHORT).show();
        db.close();
    }

    private void busqueda(String datoQueBusco, Integer campo) {
        Log.d("DEBUG_MAIN214", "Llegando a busqueda() con el datoQueBusco("+datoQueBusco+") y el campo"+campo+"("+Variables.CAMPO_AUX[campo]+")");

        String consulta = "SELECT " + Variables.CAMPO_ID + " FROM " + Variables.NOMBRE_TABLA + " WHERE " + Variables.CAMPO_AUX[campo] + " LIKE ?";
        SQLiteDatabase bd = conexion.getReadableDatabase();
        Log.d("DEBUG_MAIN218", "DATABASE "+bd.isOpen());

        try {
            Cursor cursor = bd.rawQuery(consulta, new String[]{"%" + datoQueBusco + "%"});

            Log.d("DEBUG_XX", "Consulta SQL: " + consulta +" "+ datoQueBusco);
            Log.d("DEBUG_XX", "Número de resultados: " + cursor.getCount());

            if (cursor.getCount() > 0) {
                if (cursor.getCount() == 1) {
                    cursor.moveToFirst();
                    Integer id = Integer.valueOf(cursor.getString(0));
                    cursor.close();
                    Log.d("DEBUG_XX", "ID encontrado: " + id);
                    Intent i = new Intent(this, detalle.class);
                    i.putExtra("id", id);
                    startActivity(i);
                } else {
                    cursor.close();
                    String columna = Variables.CAMPO_AUX[campo];
                    Log.d("DEBUG_XX", "Múltiples resultados, redirigiendo a lista_custom.");
                    Intent i = new Intent(this, lista_custom.class);
                    i.putExtra("busqueda", datoQueBusco);
                    i.putExtra("campo", columna);
                    Log.d("DEBUG_XX", "Buscaré "+datoQueBusco+" en "+columna+" ("+campo+"). En MainActivity.java 200");
                    startActivity(i);
                }
            } else {
                Toast.makeText(this, "No hay datos que coincidan con " + datoQueBusco, Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("ERROR", "Error al buscar el dato: " + e.getMessage());
            Toast.makeText(this, "Error al buscar el dato: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
    }
}