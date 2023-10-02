package com.example.calorias_boston;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class boston extends AppCompatActivity implements View.OnClickListener{

    TextView txtnombre, txtedad, txtsexo, txttiempo;
    Button btntiempo, btnvoler;
    String nombre, edad, sexo, tiempo;
    Integer age;
    Toast mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boston);

        nombre = getIntent().getStringExtra("nombre");
        edad = getIntent().getStringExtra("edad");
        sexo = getIntent().getStringExtra("sexo");

        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txtedad = (TextView) findViewById(R.id.txtedad);
        txtsexo = (TextView) findViewById(R.id.txtsexo);
        txttiempo = (TextView) findViewById(R.id.txttiempo);

        btntiempo = (Button) findViewById(R.id.btntiempo);
        btnvoler = (Button) findViewById(R.id.btnvolver);

        txtnombre.setText("Nombre: " + nombre);
        txtedad.setText("Edad: " + edad);
        txtsexo.setText("Sexo: " + sexo);

        btntiempo.setOnClickListener(this);
        btnvoler.setOnClickListener(view -> finish());
    }

    @Override
    public void onClick(View view) {
        age = Integer.parseInt(edad);

        if (view.getId() == R.id.btntiempo){
            if (sexo.contains("Hombre")){
                if (age>=18 && age<=34){ tiempo = "3:00"; }
                else if (age>=35 && age<=39) { tiempo = "3:05"; }
                else if (age>=40 && age<=44) { tiempo = "3:10"; }
                else if (age>=45 && age<=49) { tiempo = "3:20"; }
                else if (age>=50 && age<=54) { tiempo = "3:25"; }
                else if (age>=55 && age<=59) { tiempo = "3:35"; }
                else if (age>=60 && age<=64) { tiempo = "3:50"; }
                else if (age>=65 && age<=69) { tiempo = "4:05"; }
                else if (age>=70 && age<=74) { tiempo = "4:20"; }
                else if (age>=75 && age<=79) { tiempo = "4:35"; }
                else if (age>=80) { tiempo = "4:50"; }
                else { tiempo = "0:00"; }
            } else if (sexo.contains("Mujer")){
                if (age>=18 && age<=34){ tiempo = "3:30"; }
                else if (age>=35 && age<=39) { tiempo = "3:35"; }
                else if (age>=40 && age<=44) { tiempo = "3:40"; }
                else if (age>=45 && age<=49) { tiempo = "3:50"; }
                else if (age>=50 && age<=54) { tiempo = "3:55"; }
                else if (age>=55 && age<=59) { tiempo = "4:05"; }
                else if (age>=60 && age<=64) { tiempo = "4:20"; }
                else if (age>=65 && age<=69) { tiempo = "4:35"; }
                else if (age>=70 && age<=74) { tiempo = "4:50"; }
                else if (age>=75 && age<=79) { tiempo = "5:05"; }
                else if (age>=80) { tiempo = "5:20"; }
                else { tiempo = "0:00"; }
            } else {
                mensaje = Toast.makeText(boston.this, "Error inesperado.", Toast.LENGTH_SHORT);
                mensaje.show();
            }

            txttiempo.setText("Tiempo requerido de " + tiempo + " horas.");
            if (Objects.equals(tiempo, "0:00")) {
                mensaje = Toast.makeText(boston.this,
                        "Edad no permitida para clasificar al maratón.",
                        Toast.LENGTH_SHORT);
                mensaje.show();
            }
        }
    }
}