package com.example.conversordata;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;

public class MainActivity extends AppCompatActivity {

    EditText etx1, etx2;
    Spinner spn1, spn2;
    Button btn;
    TextView txt1, txt2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etx1 = (EditText) findViewById(R.id.edit1);
        etx2 = (EditText) findViewById(R.id.edit2);
        txt1 = (TextView) findViewById(R.id.txt1);
        txt2 = (TextView) findViewById(R.id.txt2);
        spn1 = (Spinner) findViewById(R.id.spinner1);
        spn2 = (Spinner) findViewById(R.id.spinner2);

        // Relleno de Spinners
        String[] unidades = {"bit", "Byte", "Kilobyte", "Kibibyte", "Megabyte", "Mebibyte", "Gigabyte", "Gibibyte", "Terabyte", "Tebibyte", "Petabyte", "Pebibyte"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, unidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spn1.setAdapter(adapter);
        spn2.setAdapter(adapter);
        // Para poner titulos
        spn1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String unidadSeleccionada = spn1.getSelectedItem().toString();
                txt1.setText("Unidad 1: " + unidadSeleccionada); }
            @Override public void onNothingSelected(AdapterView<?> parentView) {}
        });
        spn2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String unidadSeleccionada = spn2.getSelectedItem().toString();
                txt2.setText("Unidad 2: " + unidadSeleccionada); }
            @Override public void onNothingSelected(AdapterView<?> parentView) {}
        });

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(view -> convertirUnidades());
    }

    private void convertirUnidades() {
        // Obtener el valor a convertir y las unidades seleccionadas
        BigDecimal valor = new BigDecimal(etx1.getText().toString());
        String unidadOrigen = spn1.getSelectedItem().toString();
        String unidadDestino = spn2.getSelectedItem().toString();

        // Valor respecto a Mebibyte
        BigDecimal vlrOrigen = new BigDecimal(String.valueOf(relacionMiB(unidadOrigen)));
        BigDecimal vlrDestino = new BigDecimal(String.valueOf(relacionMiB(unidadDestino)));

        // Realizar la conversión
        // resultado = (valor / unidadOrigen) * unidadDestino
        BigDecimal resultado = valor.divide(vlrOrigen, 10, BigDecimal.ROUND_HALF_UP).multiply(vlrDestino);

        etx2.setText(resultado + "");
    }

    public static BigDecimal relacionMiB (String unidad) {
        // Valor respecto 1 Mebibyte
        String[] valorMiB = {"8.389e+6", "1.049e+6", "1048.58", "1024", "1.04858", "1", "0.00104858", "0.000976563", "1.0486e-6", "9.5367e-7", "1.0486e-9", "9.3132e-10"};
        int i = encontrarPosicion(unidad);
        BigDecimal equivalencia = new BigDecimal(valorMiB[i]);
        return equivalencia;
    }

    public static int encontrarPosicion (String unidad) {
        // Como tienen la misma posición, nos sirve mucho encontrar por el nombre
        String[] s = {"bit", "Byte", "Kilobyte", "Kibibyte", "Megabyte", "Mebibyte", "Gigabyte", "Gibibyte", "Terabyte", "Tebibyte", "Petabyte", "Pebibyte"};
        int posicion = -1;
        for (int i = 0; i < s.length; i++) { if (s[i].equals(unidad)) { posicion = i; break; } }
        return posicion;
    }
}