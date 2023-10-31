package com.ximenamzo.examenlibros.vistas.ventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.ximenamzo.examenlibros.R;
import com.ximenamzo.examenlibros.db.Connect;
import com.ximenamzo.examenlibros.db.Variables;
import com.ximenamzo.examenlibros.modelos.Libros;

import java.util.ArrayList;

public class MainVentas extends AppCompatActivity implements View.OnClickListener {
    TextView out_costotal;
    AutoCompleteTextView campoRfc, campoNombre, campoIsbn, campoTitulo, campoAutor;
    EditText campoPrecio, campoCantidad;
    Button venta, limpiar, search, ver;
    ImageButton btnSumar, btnRestar;
    Connect conectar;
    Intent i;
    Integer cantidad, idCliente, idLibro;
    Double precio, costotal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ventas);
        setTitle("Libros");

        campoRfc = findViewById(R.id.campoRfc);
        campoNombre = findViewById(R.id.campoNombre);
        campoIsbn = findViewById(R.id.campoISBN);
        campoTitulo = findViewById(R.id.cammpoTitulo);
        campoAutor = findViewById(R.id.campoAutor);
        campoPrecio = findViewById(R.id.campoPrecio);
        campoCantidad = findViewById(R.id.campoCantidad);

        btnRestar = findViewById(R.id.btnrestar);
        btnSumar = findViewById(R.id.btnsumar);
        venta = findViewById(R.id.btnventa);
        limpiar = findViewById(R.id.btnlimpiar);
        search = findViewById(R.id.btnbuscar);
        ver = findViewById(R.id.btnver);

        btnRestar.setOnClickListener(this);
        btnSumar.setOnClickListener(this);
        venta.setOnClickListener(this);
        limpiar.setOnClickListener(this);
        search.setOnClickListener(this);
        ver.setOnClickListener(this);

        conectar = new Connect(this, Variables.NOMBRE_BD, null, Connect.APPVERSION);

        adaptadorCliente(campoRfc);
        adaptadorCliente(campoNombre);
        adaptadorLibro(campoIsbn);
        adaptadorLibro(campoTitulo);
        adaptadorLibro(campoAutor);
    }

    @Override
    public void onClick(View v) {
        if (v == limpiar) {
            campoRfc.setText("");
            campoNombre.setText("");
            campoIsbn.setText("");
            campoTitulo.setText("");
            campoAutor.setText("");
            campoPrecio.setText("");
            campoCantidad.setText("");
            out_costotal.setText("0.00");
        }
        if (v == ver) {
            SQLiteDatabase bd = conectar.getReadableDatabase();
            Cursor cursor = bd.rawQuery("SELECT COUNT(*) FROM " + Variables.NOMBRE_TABLA[2], null);
            cursor.moveToFirst();
            int recordCount = cursor.getInt(0);
            cursor.close();
            bd.close();

            if (recordCount > 0) {
                i = new Intent(MainVentas.this, lista_ventas.class);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay ninguna venta registrada.", Toast.LENGTH_LONG).show();
            }
        }

        String rfc = campoRfc.getText().toString();
        String nombre = campoNombre.getText().toString();
        String isbn = campoRfc.getText().toString();

        if (v == venta) {
            if (!isbn.isEmpty() && !rfc.isEmpty()) {
                insertar();
            } else {
                Toast.makeText(this, "Cliente o Libro no encontrados. Modifique sus datos para continuar con la Venta.", Toast.LENGTH_LONG).show();
            }
        }

        if ((!rfc.isEmpty() && nombre.isEmpty()) || (rfc.isEmpty() && !nombre.isEmpty())) {
            if (v == search) buscarPorCliente();
        }

        if (v == btnRestar) {
            cantidad = Integer.parseInt(campoCantidad.getText().toString());
            if (cantidad == 1) {
                btnRestar.setEnabled(false);
            } else if (cantidad > 1) {
                btnRestar.setEnabled(true);
                cantidad--;
                campoCantidad.setText(cantidad);
            }
        }

        if (v == btnSumar) {
            cantidad = Integer.parseInt(campoCantidad.getText().toString());
            cantidad++;
            campoCantidad.setText(cantidad);
        }
    }

    private void adaptadorCliente(AutoCompleteTextView campo) {
        ArrayList<String> lista = new ArrayList<>();
        SQLiteDatabase db = conectar.getReadableDatabase();
        String consulta = "SELECT " + Variables.CAMPO_ID2[1] + ", " + Variables.CAMPO_PERSONA[1] + " FROM " + Variables.NOMBRE_TABLA[1];
        Cursor cursor = db.rawQuery(consulta, null);
        if (cursor.moveToFirst()) {
            do {
                String rfc = cursor.getString(cursor.getInt(0));
                String nombre = cursor.getString(cursor.getInt(1));
                String datoCompleto;
                if (campo == campoRfc) {
                    datoCompleto = rfc + " - " + nombre;
                } else if (campo == campoNombre) {
                    datoCompleto = nombre + " - " + rfc;
                } else { datoCompleto = ""; }
                lista.add(datoCompleto);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lista);
        campo.setAdapter(adapter);
        campo.setOnItemClickListener((parent, view, position, id) -> {
            String item = (String) parent.getItemAtPosition(position);
            String[] partes = item.split(" - ");
            String rfc, nombre;
            if (campo == campoRfc) {
                rfc = partes[0];
                nombre = partes[1];
            } else if (campo == campoNombre) {
                nombre = partes[0];
                rfc = partes[1];
            } else {
                rfc = "";
                nombre = "";
            }
            campoRfc.setText(rfc);
            campoNombre.setText(nombre);
        });
    }

    private void adaptadorLibro(AutoCompleteTextView campo) {
        SQLiteDatabase db = conectar.getReadableDatabase();
        Libros libro;
        ArrayList<Libros> datoslibro = new ArrayList<>();
        ArrayList<String> lista = new ArrayList<>();
        String consulta = "SELECT * FROM " + Variables.NOMBRE_TABLA[0];
        Cursor cursor = db.rawQuery(consulta, null);
        if (cursor.moveToFirst()) {
            do {
                libro = new Libros();
                libro.setId(cursor.getInt(0));
                libro.setIsbn(cursor.getString(1));
                libro.setTitulo(cursor.getString(2));
                libro.setAutor(cursor.getString(3));
                libro.setEditorial(cursor.getString(4));
                libro.setPaginas(Integer.valueOf(cursor.getString(5)));
                libro.setPrecio(Double.valueOf(cursor.getString(6)));
                datoslibro.add(libro);

                /*String isbn = cursor.getString(cursor.getInt(0));
                String titulo = cursor.getString(cursor.getInt(1));
                String autor = cursor.getString(cursor.getInt(1));

                String datoCompleto;
                if (campo==campoIsbn) {
                    datoCompleto = isbn + " - " + titulo;
                } else if (campo==campoTitulo) {
                    datoCompleto = titulo + " - " + autor;
                } else if (campo==campoAutor) {
                    datoCompleto = autor + " - " + titulo;
                } else {
                    datoCompleto = "";
                }
                lista.add(datoCompleto);/**/
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        if (campo==campoIsbn) {
            for(int i = 0; i< datoslibro.size(); i++){
                lista.add(datoslibro.get(i).getIsbn() + " - " + datoslibro.get(i).getTitulo());
            }
        } else if (campo==campoTitulo) {
            for(int i = 0; i< datoslibro.size(); i++){
                lista.add(datoslibro.get(i).getTitulo() + " - " + datoslibro.get(i).getAutor());
            }
        } else if (campo==campoAutor) {
            for(int i = 0; i< datoslibro.size(); i++){
                lista.add(datoslibro.get(i).getAutor() + " - " + datoslibro.get(i).getTitulo());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, lista);
        campo.setAdapter(adapter);
        campo.setOnItemClickListener((parent, view, position, id) -> {
            //String item = (String) parent.getItemAtPosition(position);
            Libros itemLibro = datoslibro.get(position);
            /*String[] partes = item.split(" - ");
            String isbn, titulo, autor, precio;
            if (campo==campoIsbn) {
                rfc = partes[0];
                nombre = partes[1];
            } else {
                nombre = partes[0];
                rfc = partes[1];
            }/**/
            campoIsbn.setText(itemLibro.getIsbn());
            campoTitulo.setText(itemLibro.getTitulo());
            campoAutor.setText(itemLibro.getAutor());
            campoPrecio.setText(String.valueOf(itemLibro.getPrecio()));
            campoCantidad.setText("1");
        });
    }

    private void insertar() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        String rfc = campoRfc.getText().toString();
        idLibro = getObjectId(db, rfc, 0);
        idCliente = getObjectId(db, rfc, 1);
        Log.d("DEBUG_XX", "IDs del Libro es "+idLibro+" y Cliente "+idCliente);

        if (idLibro!=0 && idCliente!=0) {
            cantidad = Integer.parseInt(campoCantidad.getText().toString());
            precio = Double.parseDouble(campoPrecio.getText().toString());
            costotal = precio * cantidad;
            Log.d("DEBUG_XX", "Libros("+cantidad+") * $"+precio+" = $"+costotal);
            //SQLiteDatabase db = conectar.getWritableDatabase();
            ContentValues valores = new ContentValues();
            valores.put(Variables.CAMPO_IDS[1], idLibro); // id del libro
            valores.put(Variables.CAMPO_IDS[2], idCliente); // id del cliente
            valores.put(Variables.CAMPO_CANTIDADES[1], cantidad); // cantidad de libros a comprar
            valores.put(Variables.CAMPO_DINERO[1], costotal); // total a pagar
            long id = db.insert(Variables.NOMBRE_TABLA[2], Variables.CAMPO_IDS[0], valores);
            if (id != -1) {
                Toast.makeText(this, "Registro exitoso con id "+id, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ocurrió un error al registrar los datos.", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } else {
            Toast.makeText(this, "Error para encontrar Cliente o Libro.", Toast.LENGTH_SHORT).show();
        }
    }

    private void buscarPorCliente() {
        SQLiteDatabase bd = conectar.getReadableDatabase();
        String rfc, nombre;
        rfc = campoRfc.getText().toString();
        nombre = campoNombre.getText().toString();

        if (!rfc.isEmpty()) {
            buscarData(bd, Variables.CAMPO_ID2[1], rfc);
        } else if (!nombre.isEmpty()) {
            buscarData(bd, Variables.CAMPO_PERSONA[1],nombre);
        }
        bd.close();
    }

    private int getObjectId(@NonNull SQLiteDatabase db, String dato, @NonNull Integer x) {
        String[] parametros = {dato};
        String[] campos = {Variables.CAMPO_IDS[0]};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[x], campos, Variables.CAMPO_ID2[x] + "=?", parametros, null, null, null);
        boolean existe = cursor.getCount() > 0;
        Log.d("DEBUG_XX", "Id del Cliente: "+cursor.getInt(0));
        cursor.close();
        if (existe) {
            return cursor.getInt(0);
        } else {
            return 0;
        }
    }

    private void buscarData(SQLiteDatabase db, String dbVariable,String dato) {
        String consulta = "SELECT " + Variables.CAMPO_IDS[0] + " FROM " + Variables.NOMBRE_TABLA[1] + " WHERE " + dbVariable + " LIKE ?";
        // ^
        try {
            Cursor cursor = db.rawQuery(consulta, new String[]{"%" + dato + "%"});
            // ^
            if (cursor.getCount() > 1) {
                cursor.close();
                i = new Intent(MainVentas.this, lista_ventas_custom.class);
                i.putExtra("variable", dbVariable);
                i.putExtra("dato", dato);
                // ^^
                startActivity(i);
            } else if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                Log.d("DEBUGXX", "Cursores0: "+cursor.getString(0));
                String idc = cursor.getString(0);
                cursor.close();
                i = new Intent(MainVentas.this, detalle_venta.class);
                i.putExtra("id_cliente", idc);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay datos disponibles para el "+dbVariable+ " '" + dato+"'.", Toast.LENGTH_LONG).show();
                // ^
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al buscar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}