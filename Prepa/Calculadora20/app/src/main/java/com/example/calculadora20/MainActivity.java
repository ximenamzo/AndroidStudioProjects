package com.example.calculadora20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    //Colocamos los objetos
    EditText et1, et2;
    TextView tv1;
    RadioButton rb1, rb2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Enlazamos los objetos
        et1 = findViewById(R.id.etvalor1);
        et2 = findViewById(R.id.etvalor2);
        tv1 = findViewById(R.id.tvresultado);
        rb1 = findViewById(R.id.rbsumar);
        rb2 = findViewById(R.id.rbrestar);
    }
    //Cuerpo de nuestra app

    public void operacion(View view)
    {
        //Convierto las cadenas en numeros
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        //Convierto la cadena en numero
        int n1 = Integer.parseInt(valor1);
        int n2 = Integer.parseInt(valor2);

        if(rb1.isChecked()==true)
        {
            int suma = n1 + n2;
            String res = String.valueOf(suma);
            tv1.setText(res);
        }
        else if(rb2.isChecked()==true)
        {
            int resta = n1 - n2;
            String res = String.valueOf(resta);
            tv1.setText(res);
        }

    }




}