package ximenamzo.a7bdusuarios;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import ximenamzo.a7bdusuarios.bd.Connect;
import ximenamzo.a7bdusuarios.bd.Variables;
import ximenamzo.a7bdusuarios.models.Usuarios;

public class detalle extends AppCompatActivity {
    TextView out_nombre, out_edad, out_sexo, out_fenac, out_estatura, out_telefono, txtid;
    EditText edit_nombre, edit_apellido, edit_edad, edit_sexo, edit_fenac, edit_estatura, edit_telefono;
    Button btneliminar, btneditar, btncancelar, btnguardar;
    private Usuarios usuario;
    Connect conexion;
    Integer id;
    LinearLayout editlayout, textlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setTitle("Detalles del Usuario");

        editlayout = (LinearLayout) findViewById(R.id.editLayout);
        textlayout = (LinearLayout) findViewById(R.id.textLayout);

        txtid = (TextView) findViewById(R.id.txtid);
        out_nombre = (TextView) findViewById(R.id.txtnombre);
        out_sexo = (TextView) findViewById(R.id.txtsexo);
        out_edad = (TextView) findViewById(R.id.txtedad);
        out_fenac = (TextView) findViewById(R.id.txtfenac);
        out_estatura = (TextView) findViewById(R.id.txtestatura);
        out_telefono = (TextView) findViewById(R.id.txttelefono);

        edit_nombre = (EditText) findViewById(R.id.editNombre);
        edit_apellido = (EditText) findViewById(R.id.editApellido);
        edit_sexo = (EditText) findViewById(R.id.editSexo);
        edit_edad = (EditText) findViewById(R.id.editEdad);
        edit_fenac = (EditText) findViewById(R.id.editFenac);
        edit_estatura = (EditText) findViewById(R.id.editEstatura);
        edit_telefono = (EditText) findViewById(R.id.editTelefono);

        btneliminar = (Button) findViewById(R.id.btnEliminar);
        btneditar = (Button) findViewById(R.id.btnEditar);
        btncancelar = (Button) findViewById(R.id.btnCancelar);
        btnguardar = (Button) findViewById(R.id.btnGuardar);

        conexion = new Connect(this, Variables.NOMBRE_BD, null, 1);

        // RECIBIENDO DATOS
        Bundle objeto = getIntent().getExtras(); // trae el objeto
        Log.d("DEBUG_XX", "Objeto de detalle: " + objeto.toString());
        if (objeto != null) {
            usuario = (Usuarios) objeto.getSerializable("usuario");

            if (usuario != null) {
                Log.d("DEBUG_XX", "Detalles de todo el objeto: " + usuario.toString());
                mostrarDatos();
            } else {
                //int id = objeto.getInt("id");
                int id = getIntent().getIntExtra("id", 0);
                Log.d("DEBUG_XX", "ID que estare buscando en detalle: "+id);
                buscarId(id);
            };

            btneditar.setOnClickListener(v -> editar());
            btnguardar.setOnClickListener(v -> guardarCambios());
            btncancelar.setOnClickListener(v -> cancelarEdicion());

            btneliminar.setOnClickListener(v -> {
                // cuadro de diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(detalle.this)
                        .setTitle("Confirmar Eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar este usuario?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarUsuario())
                        .setNegativeButton("No", (dialog, which) -> {
                            dialog.dismiss(); // esto no hacer nada, cierra el cuadro de diálogo
                        }).show();
            });
        } else {
            out_nombre.setText("No hay datos para mostrar.");
        }

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private void mostrarDatos() {
        editlayout.setVisibility(View.GONE);
        textlayout.setVisibility(View.VISIBLE);
        if (usuario != null) {
            out_nombre.setText(usuario.getNombre() + " " + usuario.getApellido());
            out_sexo.setText(usuario.getSexo());
            out_edad.setText("Edad: " + String.valueOf(usuario.getEdad()) + " años.");
            out_fenac.setText(usuario.getFenac());
            out_estatura.setText("Altura: " + String.valueOf(usuario.getEstatura()) + " centímetros.");
            out_telefono.setText("Teléfono: " + usuario.getTelefono());
        } else {
            // Oculta editText y sus botones Guardar y Cancelar
            edit_nombre.setVisibility(View.GONE);
            edit_apellido.setVisibility(View.GONE);
            edit_sexo.setVisibility(View.GONE);
            edit_edad.setVisibility(View.GONE);
            edit_fenac.setVisibility(View.GONE);
            edit_estatura.setVisibility(View.GONE);
            edit_telefono.setVisibility(View.GONE);
        }
        btnguardar.setVisibility(View.GONE);
        btncancelar.setVisibility(View.GONE);
        out_nombre.setVisibility(View.VISIBLE);
        out_sexo.setVisibility(View.VISIBLE);
        out_edad.setVisibility(View.VISIBLE);
        out_fenac.setVisibility(View.VISIBLE);
        out_estatura.setVisibility(View.VISIBLE);
        out_telefono.setVisibility(View.VISIBLE);
        btneliminar.setVisibility(View.VISIBLE);
        btneditar.setVisibility(View.VISIBLE);
    }

    private void editar() {
        // copia los datos a los editText
        txtid.setText("Id del usuario: " + String.valueOf(usuario.getId()));
        edit_nombre.setText(usuario.getNombre());
        edit_apellido.setText(usuario.getApellido());
        edit_sexo.setText(usuario.getSexo());
        edit_edad.setText(String.valueOf(usuario.getEdad()));
        edit_fenac.setText(usuario.getFenac());
        edit_estatura.setText(String.valueOf(usuario.getEstatura()));
        edit_telefono.setText(usuario.getTelefono());

        // oculto los textViews y botones
        textlayout.setVisibility(View.GONE);
        out_nombre.setVisibility(View.GONE);
        out_sexo.setVisibility(View.GONE);
        out_edad.setVisibility(View.GONE);
        out_fenac.setVisibility(View.GONE);
        out_estatura.setVisibility(View.GONE);
        out_telefono.setVisibility(View.GONE);
        btneliminar.setVisibility(View.GONE);
        btneditar.setVisibility(View.GONE);

        // Muestra los campos de edición y los botones Guardar y Cancelar
        editlayout.setVisibility(View.VISIBLE);
        edit_nombre.setVisibility(View.VISIBLE);
        edit_apellido.setVisibility(View.VISIBLE);
        edit_sexo.setVisibility(View.VISIBLE);
        edit_edad.setVisibility(View.VISIBLE);
        edit_fenac.setVisibility(View.VISIBLE);
        edit_estatura.setVisibility(View.VISIBLE);
        edit_telefono.setVisibility(View.VISIBLE);
        btnguardar.setVisibility(View.VISIBLE);
        btncancelar.setVisibility(View.VISIBLE);
    }

    private void guardarCambios() {
        // Actualiza el usuario con los datos editados
        usuario.setNombre(edit_nombre.getText().toString());
        usuario.setApellido(edit_apellido.getText().toString());
        usuario.setEdad(Integer.parseInt(edit_edad.getText().toString()));
        usuario.setFenac(edit_fenac.getText().toString());
        usuario.setEstatura(Integer.parseInt(edit_estatura.getText().toString()));
        usuario.setTelefono(edit_telefono.getText().toString());

        // BD aquí
        SQLiteDatabase bd = conexion.getWritableDatabase();
        String[] parametros = {String.valueOf(usuario.getId())};
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, edit_nombre.getText().toString());
        valores.put(Variables.CAMPO_APELLIDO, edit_apellido.getText().toString());
        valores.put(Variables.CAMPO_EDAD, edit_edad.getText().toString());
        valores.put(Variables.CAMPO_SEXO, edit_sexo.getText().toString());
        valores.put(Variables.CAMPO_FENAC, edit_fenac.getText().toString());
        valores.put(Variables.CAMPO_ESTATURA, edit_estatura.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, edit_telefono.getText().toString());
        bd.update(Variables.NOMBRE_TABLA, valores, Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this, "Registro actualizado.", Toast.LENGTH_LONG).show();
        bd.close();

        // Muestra los datos actualizados y regresa a la "vista" de detalles
        editlayout.setVisibility(View.GONE);
        btnguardar.setVisibility(View.GONE);
        btncancelar.setVisibility(View.GONE);
        textlayout.setVisibility(View.VISIBLE);
        mostrarDatos();
    }

    private void cancelarEdicion() {
        editlayout.setVisibility(View.GONE);
        mostrarDatos(); // ignora cambios y muestra los datos originales
    }

    private void eliminarUsuario() {
        SQLiteDatabase bd = conexion.getWritableDatabase();
        String[] parametros = {String.valueOf(usuario.getId())};
        int n = bd.delete(Variables.NOMBRE_TABLA,Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this,"Usuarios eliminados: " + n, Toast.LENGTH_LONG).show();
        bd.close();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void buscarId(Integer id) {
        Log.d("DEBUG_XX", "ID que busco en detalle: "+id);
        if (usuario == null) usuario = new Usuarios();
        SQLiteDatabase bd = conexion.getReadableDatabase();
        String[] parametros = {String.valueOf(id)};

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA, Variables.CAMPO_AUX, Variables.CAMPO_ID + " =?", parametros, null, null, null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                Log.d("DEBUGXX", "ISBN encontrado: "+ cursor.getString(0) + ". Con el ID: "+ cursor.getString(1)+".");
                usuario.setId(cursor.getInt(0));
                usuario.setNombre(cursor.getString(1));
                usuario.setApellido(cursor.getString(2));
                usuario.setEdad(Integer.valueOf(cursor.getString(3)));
                usuario.setSexo(cursor.getString(4));
                usuario.setFenac(cursor.getString(5));
                usuario.setEstatura(Integer.valueOf(cursor.getString(6)));
                usuario.setTelefono(cursor.getString(7));
                cursor.close();
            } else {
                Toast.makeText(this, "No se encontraron datos para el ID: " + id, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Error al obtener datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
        mostrarDatos();
    }
}