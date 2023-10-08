package com.example.examen2parcial1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editnombre;
    RadioGroup rgmaterias;
    RadioButton rbmate, rbesp;
    Button continuar;
    String nombre, materia;
    Toast mensaje;
    Intent i;
    Integer m=0;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editnombre = (EditText) findViewById(R.id.editnombre);

        rgmaterias = (RadioGroup) findViewById(R.id.rgmateria);
        rbmate = (RadioButton) findViewById(R.id.rbmate);
        rbesp = (RadioButton) findViewById(R.id.rbesp);
        rgmaterias.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbmate) {
                materia = "Matemáticas";
                m=1;
            } else if (checkedId == R.id.rbesp) {
                materia = "Español";
                m=2;
            }
        });

        continuar = (Button) findViewById(R.id.btncontinuar);
        continuar.setOnClickListener(view -> {
            nombre = editnombre.getText().toString();

            if (nombre.isEmpty()) {
                Toast.makeText(MainActivity.this, "Ingresa tu nombre.", Toast.LENGTH_LONG).show();
            } else if (materia.isEmpty()){
                Toast.makeText(MainActivity.this, "Selecciona una materia para continuar.", Toast.LENGTH_LONG).show();
            } else {
                if(materia.contains("Matemáticas") || m==1){
                    i = new Intent(MainActivity.this, mate.class);
                    i.putExtra("nombre", nombre);
                    i.putExtra("materia", materia);
                    editnombre.setText("");
                    rgmaterias.clearCheck();
                    startActivity(i);
                } else if(materia.contains("Español") || m==2){
                    i = new Intent(MainActivity.this, espa.class);
                    i.putExtra("nombre", nombre);
                    i.putExtra("materia", materia);
                    editnombre.setText("");
                    rgmaterias.clearCheck();
                    startActivity(i);
                } else {
                    Toast.makeText(MainActivity.this, "Error 404.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


    protected void onResume() {
        super.onResume();
        editnombre.setText("");
        rgmaterias.clearCheck();
    }

    @Override
    public void onClick(View view) {

    }
}
