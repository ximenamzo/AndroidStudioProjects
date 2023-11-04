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

        String nombre = in_nombre.getText().toString();
        String rfc = in_rfc.getText().toString();

        if (v == insert) {
            if (!nombre.isEmpty() && !rfc.isEmpty()) {
                insertar();
            } else {
                Toast.makeText(this, "Ingrese los datos restantes.", Toast.LENGTH_LONG).show();
            }
        }

        if(v == searchNombre){
            if(!nombre.isEmpty()) {
                DB.buscar(this, in_nombre.getText().toString(),Variables.NOMBRE_TABLA[1],Variables.CAMPO_PERSONA[1]);
            } else {
                Toast.makeText(this, "Ingrese un nombre para buscar.", Toast.LENGTH_SHORT).show();
            }
        }

        if(v == searchRfc){
            if(!rfc.isEmpty()) {
                DB.buscar(this, in_rfc.getText().toString(),Variables.NOMBRE_TABLA[1],Variables.CAMPO_ID2[1]);
            } else {
                Toast.makeText(this, "Ingrese un RFC para buscar.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertar() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        String rfc = in_nombre.getText().toString();

        if (rfcExiste(db, rfc)) {
            Toast.makeText(this, "RFC ya registrado.", Toast.LENGTH_LONG).show();
        } else {
            ContentValues valores = new ContentValues();
            valores.put(Variables.CAMPO_PERSONA[1], in_nombre.getText().toString()); // nombre
            valores.put(Variables.CAMPO_ID2[1], in_rfc.getText().toString()); //rfc
            long id = db.insert(Variables.NOMBRE_TABLA[1], Variables.CAMPO_IDS[0],valores);
            if (id != -1) {
                Toast.makeText(this, "Registro exitoso con id "+id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ocurrió un error al registrar los datos.", Toast.LENGTH_SHORT).show();
            }
        }
        db.close();
    }

    private boolean rfcExiste(SQLiteDatabase db, String rfc) {
        String[] parametros = {rfc};
        String[] campos = {Variables.CAMPO_ID2[0]};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[1], campos, Variables.CAMPO_ID2[1] + "=?", parametros, null, null, null);
        boolean existe = cursor.getCount() > 0;
        cursor.close();
        return existe;
    }
}