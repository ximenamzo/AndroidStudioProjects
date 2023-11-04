package com.ximenamzo.examenlibros.vistas.ventas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    private final ArrayList<String> listaClientes = new ArrayList<>();
    private final ArrayList<String> listaLibros = new ArrayList<>();
    private final Set<String> setClientes = new HashSet<>();
    private final Set<String> setLibros = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_ventas);
        setTitle("Ventas");

        campoRfc = findViewById(R.id.campoRfc);
        campoNombre = findViewById(R.id.campoNombre);
        campoIsbn = findViewById(R.id.campoISBN);
        campoTitulo = findViewById(R.id.campoTitulo);
        campoAutor = findViewById(R.id.campoAutor);
        campoPrecio = findViewById(R.id.campoPrecio);
        campoCantidad = findViewById(R.id.campoCantidad);

        btnRestar = findViewById(R.id.btnrestar);
        btnSumar = findViewById(R.id.btnsumar);
        venta = findViewById(R.id.btnventa);
        limpiar = findViewById(R.id.btnlimpiar);
        search = findViewById(R.id.btnbuscar);
        ver = findViewById(R.id.btnver);

        out_costotal = findViewById(R.id.txtcostotal);

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

        campoRfc.setOnClickListener(view -> campoRfc.showDropDown());
        campoNombre.setOnClickListener(view -> campoNombre.showDropDown());
        campoIsbn.setOnClickListener(view -> campoIsbn.showDropDown());
        campoTitulo.setOnClickListener(view -> campoTitulo.showDropDown());
        campoAutor.setOnClickListener(view -> campoAutor.showDropDown());

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

        cantidad = Integer.parseInt(campoCantidad.getText().toString());

        if (v == btnRestar) {
            if (cantidad == 1) {
                btnRestar.setEnabled(false);
            } else if (cantidad > 1) {
                btnRestar.setEnabled(true);
                cantidad--;
                campoCantidad.setText(String.valueOf(cantidad));
                double total = Double.parseDouble(campoPrecio.getText().toString()) * cantidad;
                out_costotal.setText(String.valueOf(total));
            }
        }

        if (v == btnSumar) {
            cantidad++;
            campoCantidad.setText(String.valueOf(cantidad));
            double total = Double.parseDouble(campoPrecio.getText().toString()) * cantidad;
            out_costotal.setText(String.valueOf(total));
        }
    }

    private void adaptadorCliente(AutoCompleteTextView campo) {
        SQLiteDatabase db = conectar.getReadableDatabase();
        String consulta = "SELECT * FROM " + Variables.NOMBRE_TABLA[1];
        Cursor cursor = db.rawQuery(consulta, null);
        listaClientes.clear();
        setClientes.clear();
        List<String> listaRFCNombre = new ArrayList<>();
        List<String> listaNombreRFC = new ArrayList<>();
        if (cursor.moveToFirst()) {
            String[] columnNames = cursor.getColumnNames();
            for (String columnName : columnNames) {
                Log.d("Depuracion", "Nombre de columna: " + columnName);
            }
            do {
                @SuppressLint("Range") String id = cursor.getString(cursor.getColumnIndex(Variables.CAMPO_IDS[0]));
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex(Variables.CAMPO_PERSONA[1]));
                @SuppressLint("Range") String rfc = cursor.getString(cursor.getColumnIndex(Variables.CAMPO_ID2[1]));

                Log.d("Depuracion", "ID: " + id);
                Log.d("Depuracion", "Nombre: " + nombre);
                Log.d("Depuracion", "RFC: " + rfc);

                String datoCompletoRFCNombre = rfc + " - " + nombre;
                String datoCompletoNombreRFC = nombre + " - " + rfc;

                listaRFCNombre.add(datoCompletoRFCNombre);
                listaNombreRFC.add(datoCompletoNombreRFC);

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        List<String> listaSeleccionada = (campo == campoRfc) ? listaRFCNombre : listaNombreRFC;

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listaSeleccionada);

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


    private void adaptadorLibro(AutoCompleteTextView campo) { // TODO
        SQLiteDatabase db = conectar.getReadableDatabase();
        Libros libro;
        ArrayList<Libros> datoslibro = new ArrayList<>();
        String consulta = "SELECT * FROM " + Variables.NOMBRE_TABLA[0];
        Cursor cursor = db.rawQuery(consulta, null);

        listaLibros.clear();
        setLibros.clear();

        List<String> listaISBNtitulo = new ArrayList<>();
        List<String> listaTituloAutor = new ArrayList<>();
        List<String> listaAutorTitulo = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                libro = new Libros();
                libro.setId(cursor.getInt(0));
                libro.setIsbn(cursor.getString(1));
                libro.setTitulo(cursor.getString(2));
                libro.setAutor(cursor.getString(3));
                libro.setEditorial(cursor.getString(4));
                libro.setPaginas(cursor.getInt(5));
                libro.setPrecio(cursor.getDouble(6));
                datoslibro.add(libro);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        for (Libros libroItem : datoslibro) {
            if (campo == campoIsbn) {
                listaLibros.add(libroItem.getIsbn() + " - " + libroItem.getTitulo());
                setLibros.add(libroItem.getIsbn() + " - " + libroItem.getTitulo());
            } else if (campo == campoTitulo) {
                listaLibros.add(libroItem.getTitulo() + " - " + libroItem.getAutor());
                setLibros.add(libroItem.getTitulo() + " - " + libroItem.getAutor());
            } else if (campo == campoAutor) {
                listaLibros.add(libroItem.getAutor() + " - " + libroItem.getTitulo());
                setLibros.add(libroItem.getAutor() + " - " + libroItem.getTitulo());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, listaLibros);
        campo.setAdapter(adapter);

        campo.setOnItemClickListener((parent, view, position, id) -> {
            Libros itemLibro = datoslibro.get(position);
            campoIsbn.setText(itemLibro.getIsbn());
            campoTitulo.setText(itemLibro.getTitulo());
            campoAutor.setText(itemLibro.getAutor());
            campoPrecio.setText(String.valueOf(itemLibro.getPrecio()));
            campoCantidad.setText("1");
            out_costotal.setText(String.valueOf(itemLibro.getPrecio()));
        });
    }


    private void insertar() {
        SQLiteDatabase db = conectar.getWritableDatabase();
        String isbn = campoIsbn.getText().toString();
        String rfc = campoRfc.getText().toString();
        idLibro = getObjectId(db, isbn, 0);
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
            long id = db.insert(Variables.NOMBRE_TABLA[2], null, valores);
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

    private int getObjectId(@NonNull SQLiteDatabase db, String dato, Integer x) {
        String[] parametros = {dato};
        String[] campos = {Variables.CAMPO_IDS[0]};
        Cursor cursor = db.query(Variables.NOMBRE_TABLA[x], campos, Variables.CAMPO_ID2[x] + "=?", parametros, null, null, null);
        boolean existe = cursor.getCount() > 0;
        int id = 0;
        if (existe) {
            cursor.moveToFirst();
            id = cursor.getInt(0);
        }
        Log.d("DEBUG_XX", "Id del objeto "+x+": "+cursor.getInt(0));
        cursor.close();
        return id;
    }

    private void buscarData(SQLiteDatabase db, String dbVariable,String dato) {
        String consulta = "SELECT " + Variables.CAMPO_IDS[0] + " FROM " + Variables.NOMBRE_TABLA[1] + " WHERE " + dbVariable + " LIKE ?";
        try {
            Cursor cursor = db.rawQuery(consulta, new String[]{"%" + dato + "%"});
            int count = cursor.getCount();
            if (count > 1) {
                cursor.close();
                i = new Intent(MainVentas.this, lista_ventas_custom.class);
                i.putExtra("variable", dbVariable);
                i.putExtra("dato", dato);
                startActivity(i);
            } else if (count == 1) {
                cursor.moveToFirst();
                String idc = cursor.getString(0);
                cursor.close();
                i = new Intent(MainVentas.this, detalle_venta.class);
                i.putExtra("id_cliente", idc);
                startActivity(i);
            } else {
                Toast.makeText(this, "No hay datos disponibles para el " + dbVariable + " '" + dato + "'.", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al buscar: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }
}