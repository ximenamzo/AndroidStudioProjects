package com.ximenamzo.examenlibros;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;

import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Clientes;
import com.ximenamzo.examenlibros.modelos.Libros;
import com.ximenamzo.examenlibros.modelos.Ventas;
import com.ximenamzo.examenlibros.vistas.libros.MainLibros;
import com.ximenamzo.examenlibros.vistas.clientes.MainClientes;
import com.ximenamzo.examenlibros.vistas.ventas.MainVentas;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    Button libros, clientes, ventas;
    Intent i;
    Connect conectar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Sistema de Librería");

        libros = findViewById(R.id.golibros);
        clientes = findViewById(R.id.goclientes);
        ventas = findViewById(R.id.goventas);
        libros.setOnClickListener(view -> processIntent(MainLibros.class));
        clientes.setOnClickListener(view -> processIntent(MainClientes.class));
        ventas.setOnClickListener(view -> processIntent(MainVentas.class));

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        //insertarDatos();
    }

    private void processIntent(Class<?> activityClass){
            i = new Intent(MainActivity.this, activityClass);
            startActivity(i);
    }

    public void insertarDatos() {
        Libros libro1 = new Libros(null, "ISBN1", "Título 1", "Autor 1", "Editorial 1", 200, 19.99);
        Libros libro2 = new Libros(null, "ISBN2", "Título 2", "Autor 2", "Editorial 2", 250, 29.99);

        Clientes cliente1 = new Clientes(null, "RFC1", "Juan Perez");
        Clientes cliente2 = new Clientes(null, "RFC2", "Maria Torres");

        Ventas venta1 = new Ventas(null, libro1.getId(), cliente1.getId(), 2, 39.98);
        Ventas venta2 = new Ventas(null, libro2.getId(), cliente2.getId(), 1, 29.99);

        SQLiteDatabase db = conectar.getWritableDatabase();

        if (!existeLibro(db, libro1.getIsbn())) {
            insertarLibro(db, libro1);
        }

        if (!existeLibro(db, libro2.getIsbn())) {
            insertarLibro(db, libro2);
        }

        if (!existeCliente(db, cliente1.getRfc())) {
            insertarCliente(db, cliente1);
        }

        if (!existeCliente(db, cliente2.getRfc())) {
            insertarCliente(db, cliente2);
        }

        if (!existeVenta(db, venta1.getId())) {
            insertarVenta(db, venta1);
        }

        if (!existeVenta(db, venta2.getId())) {
            insertarVenta(db, venta2);
        }

        db.close();
    }

    private void insertarLibro(SQLiteDatabase db, Libros libro) {
        ContentValues values = new ContentValues();
        values.put("isbn", libro.getIsbn());
        values.put("titulo", libro.getTitulo());
        values.put("autor", libro.getAutor());
        values.put("editorial", libro.getEditorial());
        values.put("paginas", libro.getPaginas());
        values.put("precio", libro.getPrecio());

        db.insert(Variables.NOMBRE_TABLA[0], null, values);
    }

    private void insertarCliente(SQLiteDatabase db, Clientes cliente) {
        ContentValues values = new ContentValues();
        values.put("rfc", cliente.getRfc());
        values.put("nombre", cliente.getNombre());

        db.insert(Variables.NOMBRE_TABLA[1], null, values);
    }

    private void insertarVenta(SQLiteDatabase db, Ventas venta) {
        ContentValues values = new ContentValues();
        values.put("id_libro", venta.getId_libro());
        values.put("id_cliente", venta.getId_cliente());
        values.put("canlib", venta.getCanlib());
        values.put("costotal", venta.getCostotal());

        db.insert(Variables.NOMBRE_TABLA[2], null, values);
    }

    private boolean existeLibro(SQLiteDatabase db, String isbn) {
        String[] campos = {Variables.CAMPOS_TABLAS[0][0]};
        String[] args = {isbn};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[0], campos, Variables.CAMPOS_TABLAS[0][1] + "=?", args, null, null, null);
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    private boolean existeCliente(SQLiteDatabase db, String rfc) {
        String[] campos = {Variables.CAMPOS_TABLAS[1][0]};
        String[] args = {rfc};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[1], campos, Variables.CAMPOS_TABLAS[1][1] + "=?", args, null, null, null);
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }

    private boolean existeVenta(SQLiteDatabase db, int idVenta) {
        String[] campos = {Variables.CAMPOS_TABLAS[2][0]};
        String[] args = {String.valueOf(idVenta)};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[2], campos, Variables.CAMPOS_TABLAS[2][0] + "=?", args, null, null, null);
        boolean existe = cursor.moveToFirst();
        cursor.close();
        return existe;
    }
}