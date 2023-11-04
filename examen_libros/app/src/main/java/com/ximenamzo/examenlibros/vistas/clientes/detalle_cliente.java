package com.ximenamzo.examenlibros.vistas.clientes;

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
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Clientes;

public class detalle_cliente extends AppCompatActivity {
    TextView out_nombre, out_rfc, txtid;
    EditText edit_nombre, edit_rfc;
    Button btneliminar, btneditar, btncancelar, btnguardar;
    private Clientes cliente;
    Connect conectar;
    LinearLayout editlayout, textlayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_cliente);
        setTitle("Detalles del Cliente");

        editlayout = findViewById(R.id.editLayout);
        textlayout = findViewById(R.id.textLayout);

        txtid = findViewById(R.id.txtid);
        out_nombre = findViewById(R.id.txtnombre);
        out_rfc = findViewById(R.id.txtrfc);

        edit_nombre = findViewById(R.id.editNombre);
        edit_rfc = findViewById(R.id.editRfc);

        btneliminar = findViewById(R.id.btnEliminar);
        btneditar = findViewById(R.id.btnEditar);
        btncancelar = findViewById(R.id.btnCancelar);
        btnguardar = findViewById(R.id.btnGuardar);

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);

        Bundle objeto = getIntent().getExtras(); // trae el objeto
        if (objeto != null) {
            cliente = (Clientes) objeto.getSerializable("cliente");

            if (cliente != null) {
                Log.d("DEBUG_XX", cliente.toString());
                mostrarDatos();
            } else {
                String id = getIntent().getExtras().getString("id");
                Log.d("DEBUG_XX", "ID que estare buscando en detalle: "+id);
                buscarPorId(id);
            }

            btneditar.setOnClickListener(v -> editar());
            btnguardar.setOnClickListener(v -> guardarCambios());
            btncancelar.setOnClickListener(v -> cancelarEdicion());

            btneliminar.setOnClickListener(v -> {
                // cuadro de diálogo de confirmación antes de eliminar
                new AlertDialog.Builder(detalle_cliente.this)
                        .setTitle("Confirmar Eliminación")
                        .setMessage("¿Estás seguro de que deseas eliminar este registro?")
                        .setPositiveButton("Sí", (dialog, which) -> eliminarCliente())
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
        if (cliente != null) {
            out_nombre.setText(cliente.getNombre());
            out_rfc.setText(cliente.getRfc());
        } else {
            // Oculta editText y sus botones Guardar y Cancelar
            edit_nombre.setVisibility(View.GONE);
            edit_rfc.setVisibility(View.GONE);
        }
        btnguardar.setVisibility(View.GONE);
        btncancelar.setVisibility(View.GONE);
        out_nombre.setVisibility(View.VISIBLE);
        out_rfc.setVisibility(View.VISIBLE);
        btneliminar.setVisibility(View.VISIBLE);
        btneditar.setVisibility(View.VISIBLE);
    }

    private void editar() {
        // copia los datos a los editText
        txtid.setText("Id del registro: " + cliente.getId());
        edit_nombre.setText(cliente.getNombre());
        edit_rfc.setText(cliente.getRfc());

        // oculto los textViews y botones
        textlayout.setVisibility(View.GONE);
        out_nombre.setVisibility(View.GONE);
        out_rfc.setVisibility(View.GONE);
        btneliminar.setVisibility(View.GONE);
        btneditar.setVisibility(View.GONE);

        // Muestra los campos de edición y los botones Guardar y Cancelar
        editlayout.setVisibility(View.VISIBLE);
        edit_nombre.setVisibility(View.VISIBLE);
        edit_rfc.setVisibility(View.VISIBLE);
        btnguardar.setVisibility(View.VISIBLE);
        btncancelar.setVisibility(View.VISIBLE);
    }

    private void guardarCambios() {
        // Actualiza el cliente con los datos editados
        cliente.setNombre(edit_nombre.getText().toString());
        cliente.setRfc(edit_rfc.getText().toString());

        // BD aquí
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {String.valueOf(cliente.getId())};
        //String[] campos = {Variables.CAMPO_ID, Variables.CAMPO_ISBN, Variables.CAMPO_TITULO, Variables.CAMPO_AUTOR, Variables.CAMPO_EDITORIAL, Variables.CAMPO_PAGINAS};
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_PERSONA[1], edit_nombre.getText().toString());
        valores.put(Variables.CAMPO_ID2[1], edit_rfc.getText().toString());
        bd.update(Variables.NOMBRE_TABLA[1], valores, Variables.CAMPO_IDS[0] + "=?", parametros);
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

    private void eliminarCliente() {
        String id = String.valueOf(cliente.getId());
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {id};
        int n = bd.delete(Variables.NOMBRE_TABLA[1],Variables.CAMPO_IDS[0] + "=?", parametros);
        Toast.makeText(this,"Registros eliminados: " + n, Toast.LENGTH_LONG).show();
        bd.close();

        Intent resultIntent = new Intent();
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void buscarPorId(String id) {
        if (cliente == null) cliente = new Clientes();
        SQLiteDatabase bd = conectar.getReadableDatabase();
        String[] parametros = {id};
        //String[] campos = {Variables.CAMPO_IDS[0],Variables.CAMPO_PERSONA[1],Variables.CAMPO_ID2[1],};
        String[] campos = Variables.CAMPOS_TABLAS[1];

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA[1], campos, Variables.CAMPO_ID2[0] + " =?", parametros, null, null, null);
            if (cursor.getCount() > 0){
                cursor.moveToFirst();
                Log.d("DEBUGXX", "ID encontrado: "+ cursor.getString(0));
                cliente.setId(Integer.valueOf(cursor.getString(0)));
                cliente.setNombre(cursor.getString(1));
                cliente.setRfc(cursor.getString(2));
                cursor.close();
            } else {
                Toast.makeText(this, "No se encontraron datos para ID: " + id, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(this,"Error al obtener datos.", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
        bd.close();
        mostrarDatos();
    }
}