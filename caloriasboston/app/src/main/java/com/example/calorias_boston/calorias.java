package com.example.calorias_boston;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class calorias extends AppCompatActivity implements View.OnClickListener{

    TextView txtnombre, txtedad, txtsexo, txtresultado;
    EditText eTpeso, eTaltura;
    Button btncalcular, btnvoler;
    String nombre, edad, sexo, p, a;
    Double peso, altura, anios, tmb;
    Integer sx;
    Toast mensaje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calorias);

        nombre = getIntent().getStringExtra("nombre");
        edad = getIntent().getStringExtra("edad");
        sexo = getIntent().getStringExtra("sexo");

        txtnombre = (TextView) findViewById(R.id.txtnombre);
        txtedad = (TextView) findViewById(R.id.txtedad);
        txtsexo = (TextView) findViewById(R.id.txtsexo);
        txtresultado = (TextView) findViewById(R.id.txtresultado);

        eTpeso = (EditText) findViewById(R.id.editpeso);
        eTaltura = (EditText) findViewById(R.id.editestatura);

        btncalcular = (Button) findViewById(R.id.btncalcular);
        btnvoler = (Button) findViewById(R.id.btnvolver);

        txtnombre.setText("Nombre: " + nombre);
        txtedad.setText("Edad: " + edad);
        txtsexo.setText("Sexo: " + sexo);

        btncalcular.setOnClickListener(this);
        btnvoler.setOnClickListener(view -> finish());
    }
                        /*
                        Como ejemplo:
                        Una mujer de 170cm, 65 kilos y 40 años tiene una TMB de: 1351.5
                        Un hombre de 180 cm, 75 kilos y 40 años tiene una TMB de: 1680
                        */
    public static double tmb(double peso, double altura, double edad, int sexo){
        //TMB Hombre: (10 x peso kg) + (6.25 x altura cm) – (5 x edad) + 5
        //TMB Mujer: (10x peso kg) + (6.25 x altura cm) – (5 x edad) – 161

        double resultado = 0;
        if (sexo == 1) {
            resultado = (10*peso) + (6.25*altura) - (5*edad) + 5;
        } else if (sexo == 2) {
            resultado = (10*peso) + (6.25*altura) - (5*edad) - 161;
        }
        return resultado;
    }

    @Override
    public void onClick(View view) {
        p = eTpeso.getText().toString();
        a = eTaltura.getText().toString();

        if (view.getId() == R.id.btncalcular){
            if (a.isEmpty() || p.isEmpty()){
                mensaje = Toast.makeText(calorias.this, "Ingresa datos faltantes.", Toast.LENGTH_SHORT);
                mensaje.show();
            } else {
                peso = Double.parseDouble(p);
                altura = Double.parseDouble(a);
                anios = Double.parseDouble(edad);
                if (Objects.equals(sexo, "Hombre")) sx = 1;
                if (Objects.equals(sexo, "Mujer")) sx = 2;
                tmb = tmb(peso, altura, anios, sx);
                txtresultado.setText("Tasa Metabólica Basal: " + tmb);
            }
        }
    }
}