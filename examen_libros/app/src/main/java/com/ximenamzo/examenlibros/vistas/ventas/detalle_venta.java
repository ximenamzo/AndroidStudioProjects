package com.ximenamzo.examenlibros.vistas.ventas;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Libros;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class detalle_venta extends AppCompatActivity {
    TextView out_id, out_nombre, out_rfc, out_titulo, out_autor,
            out_isbn, out_precio, out_cantidad, out_costotal;
    private Libros libro;
    Connect conectar;
    Integer id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acivity_detalle_venta);
        Log.d("DEBUG_33", "Entrando a detalle_venta.java...");
        setTitle("Detalles del Libro");

        out_id = findViewById(R.id.txtid);
        out_nombre = findViewById(R.id.txtnombre);
        out_rfc = findViewById(R.id.txtrfc);
        out_titulo = findViewById(R.id.txttitulo);
        out_autor = findViewById(R.id.txtautor);
        out_isbn = findViewById(R.id.txtisbn);
        out_precio = findViewById(R.id.txtprecio);
        out_cantidad = findViewById(R.id.txtcantidad);
        out_costotal = findViewById(R.id.txtcostotal);

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);

        Bundle objeto = getIntent().getExtras();
        if (objeto != null) {
            if (objeto.containsKey("idVenta")) {
                id = objeto.getInt("idVenta");
                mostrarDetalles(id);
            } else if (objeto.containsKey("idCliente")) {
                int idCliente = objeto.getInt("idCliente");
                id = buscarVenta(idCliente);
                if (id != -1) {
                    mostrarDetalles(id);
                } else { out_id.setText("No se encontraron ventas para el cliente especificado."); finish(); }
            } else { out_id.setText("Error trayendo datos."); finish(); }
        } else { out_id.setText("Error trayendo datos."); finish(); }

        ExtendedFloatingActionButton extendedFab = findViewById(R.id.extended_fab);
        extendedFab.setOnClickListener(view -> {
            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }

    private int buscarVenta(Integer idCliente) {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();

        String[] parametros = {String.valueOf(idCliente)};
        String[] campos = {Variables.CAMPO_IDS[0]};
        Cursor cursor = bd.query(Variables.NOMBRE_TABLA[2], campos, Variables.CAMPO_IDS[2] + " =?", parametros, null, null, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            int idVenta = cursor.getInt(0);
            cursor.close();
            bd.close();
            return idVenta;
        } else {
            Toast.makeText(this, "No se encontraron registros de ventas para el ID especificado.", Toast.LENGTH_SHORT).show();
            return 0;
        }
    }

    private void mostrarDetalles(Integer idVenta) {
        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);
        SQLiteDatabase bd = conectar.getReadableDatabase();

        String[] campos = { Variables.NOMBRE_TABLA[2]+"."+Variables.CAMPO_IDS[0]+" AS VentaId",

                Variables.NOMBRE_TABLA[1]+"."+Variables.CAMPO_IDS[0]+" AS ClienteID",
                Variables.NOMBRE_TABLA[1]+"."+Variables.CAMPO_PERSONA[1]+" AS ClienteNombre",
                Variables.NOMBRE_TABLA[1]+"."+Variables.CAMPO_ID2[1]+" AS ClienteRFC",

                Variables.NOMBRE_TABLA[0]+"."+Variables.CAMPO_IDS[0]+" AS LibroID",
                Variables.NOMBRE_TABLA[0]+"."+Variables.CAMPO_TITULO+" AS LibroTitulo",
                Variables.NOMBRE_TABLA[0]+"."+Variables.CAMPO_PERSONA[0]+" AS LibroAutor",
                Variables.NOMBRE_TABLA[0]+"."+Variables.CAMPO_ID2[0]+" AS LibroISBN",
                Variables.NOMBRE_TABLA[0]+"."+Variables.CAMPO_DINERO[0]+" AS LibroPrecio",

                Variables.NOMBRE_TABLA[2]+"."+Variables.CAMPO_CANTIDADES[1]+" AS VentaCantidad",
                Variables.NOMBRE_TABLA[2]+"."+Variables.CAMPO_DINERO[1]+" AS VentaCostoTotal" };

        String consulta = "SELECT " + TextUtils.join(", ", campos) +
                " FROM " + Variables.NOMBRE_TABLA[2] +
                " INNER JOIN " + Variables.NOMBRE_TABLA[1] +
                    " ON " + Variables.NOMBRE_TABLA[2] + "." + Variables.CAMPO_IDS[2] +
                    " = " + Variables.NOMBRE_TABLA[1] + "." + Variables.CAMPO_IDS[0] +
                " INNER JOIN " + Variables.NOMBRE_TABLA[0] + " ON " + Variables.NOMBRE_TABLA[2] + "." + Variables.CAMPO_IDS[1] + " = " + Variables.NOMBRE_TABLA[0] + "." + Variables.CAMPO_IDS[0] +
                " WHERE " + Variables.NOMBRE_TABLA[2] + "." + Variables.CAMPO_IDS[0] + " = ?";
        String[] parametros = {String.valueOf(idVenta)};
        Cursor cursor = bd.rawQuery(consulta, parametros);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            idVenta = cursor.getInt(0);
            int idCliente = cursor.getInt(1);
            String nombreCliente = cursor.getString(2);
            String rfcCliente = cursor.getString(3);
            int idLibro = cursor.getInt(4);
            String tituloLibro = cursor.getString(5);
            String autorLibro = cursor.getString(6);
            String isbnLibro = cursor.getString(7);
            double precioLibro = cursor.getDouble(8);
            int cantidadVenta = cursor.getInt(9);
            double costoTotalVenta = cursor.getDouble(10);

            out_id.setText("Venta #" + idVenta);
            out_nombre.setText("Cliente #"+idCliente+": " + nombreCliente);
            out_rfc.setText("RFC: " + rfcCliente);
            out_titulo.setText("Libro #"+idLibro+": " + tituloLibro);
            out_autor.setText("Autor: " + autorLibro);
            out_isbn.setText("ISBN: " + isbnLibro);
            out_precio.setText("Precio del Libro: $" + precioLibro);
            out_cantidad.setText("Cantidad comprada: " + cantidadVenta);
            out_costotal.setText(" " + costoTotalVenta);

        } else {
            Toast.makeText(this, "No se encontraron registros de ventas para el ID especificado.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        bd.close();
    }
}
