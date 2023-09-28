package com.example.mysuma;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Declarar todos los objetos visuales con los que voy a trabajar
    EditText etValor1, etValor2;
    TextView resultado;
    Button btnsuma, btnresta, btnmult, btndiv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Enlazamos los objetos al código
        etValor1 = findViewById(R.id.etValor1);
        etValor2 = findViewById(R.id.etValor2);
        resultado = findViewById(R.id.resultado);
        btnsuma = findViewById(R.id.btnsuma);
        btnresta = findViewById(R.id.btnresta);
        btnmult = findViewById(R.id.btnmult);
        btndiv = findViewById(R.id.btndiv);


    }
    public void sumar(View view){
        //Realizamos la conversión de un String (text) a Integer (int)
        int valor1 = Integer.parseInt(etValor1.getText().toString());
        int valor2 = Integer.parseInt(etValor2.getText().toString());
        //Realizamos la suma
        int suma = valor1 + valor2;
        //Convierto un int en un string para poder usarlo en mi interfaz
        String cadena = String.valueOf(suma);
        //Asigno en valor de cadena al TextView
        resultado.setText(cadena);
    }
    public void restar(View view){
        //Realizamos la conversión de un String (text) a Integer (int)
        int valor1 = Integer.parseInt(etValor1.getText().toString());
        int valor2 = Integer.parseInt(etValor2.getText().toString());
        //Realizamos la resta
        int resta = valor1 - valor2;
        //Convierto un int en un string para poder usarlo en mi interfaz
        String cadena = String.valueOf(resta);
        //Asigno en valor de cadena al TextView
        resultado.setText(cadena);
    }
    public void multiplicar(View view){
        //Realizamos la conversión de un String (text) a Integer (int)
        int valor1 = Integer.parseInt(etValor1.getText().toString());
        int valor2 = Integer.parseInt(etValor2.getText().toString());
        //Realizamos la multiplicacion
        int mult = valor1 * valor2;
        //Convierto un int en un string para poder usarlo en mi interfaz
        String cadena = String.valueOf(mult);
        //Asigno en valor de cadena al TextView
        resultado.setText(cadena);
    }
    public void dividir(View view){
        //Realizamos la conversión de un String (text) a Integer (int)
        int valor1 = Integer.parseInt(etValor1.getText().toString());
        int valor2 = Integer.parseInt(etValor2.getText().toString());
        //Realizamos la division
        int division = valor1 / valor2;
        //Convierto un int en un string para poder usarlo en mi interfaz
        String cadena = String.valueOf(division);
        //Asigno en valor de cadena al TextView
        resultado.setText(cadena);
    }
}