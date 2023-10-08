package com.example.examen2parcial1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class califica extends AppCompatActivity {

    TextView titulo, aciertos;
    Button volver;
    Intent i;
    String nombre, materia, calificacion;
    Integer calif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_califica);

        titulo = (TextView) findViewById(R.id.txttitulo);
        aciertos = (TextView) findViewById(R.id.txtcalif);

        nombre = getIntent().getStringExtra("nombre");
        materia = getIntent().getStringExtra("materia");
        calificacion = getIntent().getStringExtra("calificacion");

        titulo.setText("");
        titulo.setText("Calificación del exámen de " + materia + " de " + nombre);

        aciertos.setText("");
        calif = Integer.parseInt(calificacion);
        if (calif>2){
            aciertos.setText("Aciertos: " +calificacion + "\n Calificación aprobatoria.");
        } else {
            aciertos.setText("Aciertos: " +calificacion + "\n\n Calificación no aprobatoria. Vuélvelo a intentar.");
        }

        volver = (Button) findViewById(R.id.btnvolver);
        volver.setOnClickListener(view -> {
            calificacion = calif.toString();
            i = new Intent(califica.this, MainActivity.class);
            i.putExtra("nombre", nombre);
            i.putExtra("materia", materia);
            i.putExtra("calificacion", calificacion);
            startActivity(i);
            finish();
        });
    }
}