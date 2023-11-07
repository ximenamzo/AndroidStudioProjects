package com.ximenamzo.examenlibros.vistas.clientes;

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
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.DB;

public class MainClientes extends AppCompatActivity implements View.OnClickListener {

    EditText in_nombre, in_rfc;
    Button insert, searchNombre, searchRfc, ver, limpiar;
    Connect conectar;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_clientes);
        setTitle("Clientes");

        in_nombre = findViewById(R.id.etxnombre);
        in_rfc = findViewById(R.id.etxrfc);

        insert = findViewById(R.id.btninsertar1);
        searchNombre = findViewById(R.id.btnbuscar1);
        searchRfc = findViewById(R.id.btnbuscar2);
        ver = findViewById(R.id.btnver);
        limpiar = findViewById(R.id.btnlimpiar);

        insert.setOnClickListener(this);
        searchNombre.setOnClickListener(this);
        searchRfc.setOnClickListener(this);
        ver.setOnClickListener(this);
        limpiar.setOnClickListener(this);

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
    }

    @Override
    public void onClick(View v) {
        if (v == limpiar) {
            in_nombre.setText("");
            in_rfc.setText("");
        }
        if (v == ver) {
            SQLiteDatabase bd = conectar.getReadableDatabase();
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM " + Variables.NOMBRE_TABLA[1], null);
            cursor.moveToFirst();
            int recordCount = cursor.getInt(0);
            cursor.close();
            bd.close();

            if (recordCount > 0) {
                i = new Intent(MainClientes.this, lista_clientes.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay registros para mostrar.", Toast.LENGTH_LONG).show();
            }
        }

        String nombre;
        String rfc;

        if (v == insert) {
            nombre = in_nombre.getText().toString();
            rfc = in_rfc.getText().toString();
            if (!nombre.isEmpty() && !rfc.isEmpty()) {
                if (rfcExiste(rfc)) {
                    Toast.makeText(this, "RFC ya registrado.", Toast.LENGTH_LONG).show();
                } else {
                    insertar(nombre, rfc);
                }
            } else {
                Toast.makeText(this, "Ingrese los datos restantes.", Toast.LENGTH_LONG).show();
            }
        }

        if(v == searchNombre){
            nombre = in_nombre.getText().toString();
            if(!nombre.isEmpty()) {
                DB.buscar(this, in_nombre.getText().toString(),Variables.NOMBRE_TABLA[1],Variables.CAMPO_PERSONA[1]);
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar.", Toast.LENGTH_SHORT).show();
            }
        }

        if(v == searchRfc){
            rfc = in_rfc.getText().toString();
            if(!rfc.isEmpty()) {
                DB.buscar(this, in_rfc.getText().toString(),Variables.NOMBRE_TABLA[1],Variables.CAMPO_ID2[1]);
            } else {
                Toast.makeText(this, "Ingrese un RFC para buscar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertar(String nombre, String rfc) {
        Log.d("DEBUG_111", "Insertando...");
        SQLiteDatabase db = conectar.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_PERSONA[1], nombre);
        valores.put(Variables.CAMPO_ID2[1], rfc);
        long id = db.insert(Variables.NOMBRE_TABLA[1], Variables.CAMPO_IDS[0], valores);
        if (id != -1) {
            Toast.makeText(this, "Registro exitoso con id "+id, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ocurrió un error al registrar los datos.", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private boolean rfcExiste(String rfc) {
        SQLiteDatabase db = conectar.getWritableDatabase();
        String[] campos = {Variables.CAMPO_IDS[0]};
        String[] parametros = {rfc};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[1], campos, Variables.CAMPO_ID2[1] + "=?", parametros, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        Log.d("DEBUG_128", "Existe el rfc? "+existe);
        db.close();
        return existe;
    }
}