package com.example.calorias_boston;

import static android.widget.Toast.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText eTnombre, eTedad;
    Button calorias, boston;
    RadioGroup radgsexo;
    String nombre, edad, sexo="";
    Toast mensaje;
    Intent i; // Intent to change the activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Usuario");

        eTnombre = (EditText) findViewById(R.id.editnombre);
        eTedad = (EditText) findViewById(R.id.editedad);
        calorias = (Button) findViewById(R.id.btncalorias);
        boston = (Button) findViewById(R.id.btnboston);
        radgsexo = (RadioGroup) findViewById(R.id.rgsexo);

        nombre = eTnombre.getText().toString();
        edad = eTedad.getText().toString();

        radgsexo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbhombre) {
                sexo = "Hombre";
            } else if (checkedId == R.id.rbmujer) {
                sexo = "Mujer";
            }
        });

        calorias.setOnClickListener(view -> {
            nombre = eTnombre.getText().toString();
            edad = eTedad.getText().toString();

            if (nombre.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
                mensaje = Toast.makeText(MainActivity.this, "Ingresa la información faltante.", Toast.LENGTH_LONG);
                mensaje.show();
            } else {
                i = new Intent(MainActivity.this, calorias.class);
                i.putExtra("nombre", nombre);
                i.putExtra("edad", edad);
                i.putExtra("sexo", sexo);
                startActivity(i);
            }
        });

        boston.setOnClickListener(view -> {
            nombre = eTnombre.getText().toString();
            edad = eTedad.getText().toString();

            if (nombre.isEmpty() || edad.isEmpty() || sexo.isEmpty()) {
                mensaje = Toast.makeText(MainActivity.this, "Ingresa la información faltante.", Toast.LENGTH_LONG);
                mensaje.show();
            } else {
                i = new Intent(MainActivity.this, boston.class);
                i.putExtra("nombre", nombre);
                i.putExtra("edad", edad);
                i.putExtra("sexo", sexo);
                startActivity(i);
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}