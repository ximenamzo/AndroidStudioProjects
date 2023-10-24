package com.example.clasebasededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText in_id, in_nombre, in_telefono;
    Button ins1, ins2, bus1, bus2, ed, el, ver, limpiar;
    Conectar conectar;
    Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Agenda");

        in_id = (EditText) findViewById(R.id.etxid);
        in_nombre = (EditText) findViewById(R.id.etxnombre);
        in_telefono = (EditText) findViewById(R.id.etxtelefono);
        ins1 = (Button) findViewById(R.id.btninsertar1);
        ins2 = (Button) findViewById(R.id.btninsertar2);
        bus1 = (Button) findViewById(R.id.btnbuscar1);
        bus2 = (Button) findViewById(R.id.btnbuscar2);
        ed = (Button) findViewById(R.id.btneditar);
        el = (Button) findViewById(R.id.btneliminar);
        ver = (Button) findViewById(R.id.btnver);
        limpiar = (Button) findViewById(R.id.btnlimpiar);

        ins1.setOnClickListener(this);
        ins2.setOnClickListener(this);
        bus1.setOnClickListener(this);
        bus2.setOnClickListener(this);
        ed.setOnClickListener(this);
        el.setOnClickListener(this);
        ver.setOnClickListener(this);
        limpiar.setOnClickListener(this);

        conectar = new Conectar(this, Variables.NOMBRE_BD, null, 1);
    }

    @Override
    public void onClick(View v) {
        if (v == limpiar) {
            in_id.setText("");
            in_nombre.setText("");
            in_telefono.setText("");
        }
        if (v == ver) {
            SQLiteDatabase bd = conectar.getReadableDatabase();
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM " + Variables.NOMBRE_TABLA, null);
            cursor.moveToFirst();
            int recordCount = cursor.getInt(0);
            cursor.close();
            bd.close();

            if (recordCount > 0) {
                i = new Intent(MainActivity.this, lista.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay registros para mostrar.", Toast.LENGTH_LONG).show();
            }
        }

        String nombre = in_nombre.getText().toString();
        String telefono = in_telefono.getText().toString();
        String id = in_id.getText().toString();

        if(!nombre.isEmpty() || !telefono.isEmpty()) {
            if (v == ins1) insertar1();
            if (v == ins2) insertar2();
        }else {
            Toast.makeText(this,"Ingrese datos.", Toast.LENGTH_LONG).show();
        }

        if(!id.isEmpty()){
            if(v == bus1) buscar1();
            if(v == bus2) buscar2();
            if (v == ed) editar();
            if (v == el) eliminar();
        }
    }

    private void insertar1() {
        // Más seguro
        SQLiteDatabase db = conectar.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put(Variables.CAMPO_NOMBRE, in_nombre.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, in_telefono.getText().toString());
        long id = db.insert(Variables.NOMBRE_TABLA, Variables.CAMPO_ID,valores);
        Toast.makeText(this, "id:"+id, Toast.LENGTH_LONG).show();
        in_nombre.setText("");
        in_telefono.setText("");
        db.close();
    }

    private void insertar2() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String nombre = in_nombre.getText().toString();
        String telefono = in_telefono.getText().toString();
        String insertar = "INSERT INTO "+Variables.NOMBRE_TABLA+
                " ("+Variables.CAMPO_NOMBRE+", "
                +Variables.CAMPO_TELEFONO+") VALUES ('"+nombre+"','"+telefono+"')";
        bd.execSQL(insertar);
        Toast.makeText(this,"Se inserto el contacto", Toast.LENGTH_LONG).show();
        in_nombre.setText("");
        in_telefono.setText("");
        bd.close();
    }

    private void buscar1() {
        SQLiteDatabase bd = conectar.getReadableDatabase();
        String[] parametros = {in_id.getText().toString()};
        String[] campos = {Variables.CAMPO_NOMBRE,Variables.CAMPO_TELEFONO};

        try {
            Cursor cursor = bd.query(Variables.NOMBRE_TABLA, campos, Variables.CAMPO_ID + "=?", parametros, null, null, null);
            cursor.moveToFirst();
            in_nombre.setText(cursor.getString(0));
            in_telefono.setText(cursor.getString(1));
            cursor.close();
        } catch (Exception e) {
            Toast.makeText(this,"No hay datos disponibles.", Toast.LENGTH_LONG).show();
            in_nombre.setText("");
            in_telefono.setText("");
            e.printStackTrace();
        }
        bd.close();
    }

    private void buscar2() {
        SQLiteDatabase bd = conectar.getReadableDatabase();
        String[] parametros = {in_id.getText().toString()}; //select * from

        try {
            Cursor cursor = bd.rawQuery("SELECT " + Variables.CAMPO_NOMBRE + ", " + Variables.CAMPO_TELEFONO +
                    " FROM " + Variables.NOMBRE_TABLA + " WHERE " + Variables.CAMPO_ID + " =?", parametros);
            cursor.moveToFirst();
            in_nombre.setText(cursor.getString(0));
            in_telefono.setText(cursor.getString(1));
            cursor.close();
            bd.close();
        } catch (Exception e) {
            Toast.makeText(this,"Error en la consulta.", Toast.LENGTH_LONG).show();
            in_nombre.setText("");
            in_telefono.setText("");
            e.printStackTrace();
        }
        bd.close();
    }

    private void editar() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {in_id.getText().toString()};
        ContentValues valores = new ContentValues();
        // String nombre = in_nombre.getText().toString();
        // String telefono = in_telefono.getText().toString();
        valores.put(Variables.CAMPO_NOMBRE, in_nombre.getText().toString());
        valores.put(Variables.CAMPO_TELEFONO, in_telefono.getText().toString());
        bd.update(Variables.NOMBRE_TABLA, valores,Variables.CAMPO_ID + "=?", parametros);
        Toast.makeText(this,"Registro actualizado.", Toast.LENGTH_LONG).show();
        bd.close();
    }

    private void eliminar() {
        SQLiteDatabase bd = conectar.getWritableDatabase();
        String[] parametros = {in_id.getText().toString()};
        int n = bd.delete(Variables.NOMBRE_TABLA,Variables.CAMPO_ID + "=?", parametros);
        // n regresa registros eliminados
        Toast.makeText(this,"Usuarios eliminados: " + n, Toast.LENGTH_LONG).show();
        in_id.setText("");
        in_nombre.setText("");
        in_telefono.setText("");
        bd.close();
    }
}