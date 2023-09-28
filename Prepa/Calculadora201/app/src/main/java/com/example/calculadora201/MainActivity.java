package com.example.calculadora201;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText et1, et2;
    Spinner sp1;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et1 = findViewById(R.id.etValor1);
        et2 = findViewById(R.id.etValor2);
        tv1 = findViewById(R.id.tvResultado);
        sp1 = findViewById(R.id.spinner);

        //Llenamos el Spinner de datos con una matriz
        String []opciones={"Sumar","Restar","Multiplicar","Dividir"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        sp1.setAdapter(adapter);
    }

    public void operar (View view)
    {
        String valor1 = et1.getText().toString();
        String valor2 = et2.getText().toString();
        int n1 = Integer.parseInt(valor1);
        int n2 = Integer.parseInt(valor2);

        String selec = sp1.getSelectedItem().toString();
        if (selec.equals("Sumar"))
        {
            int suma = n1 + n2;
            String resu = String.valueOf(suma);
            tv1.setText(resu);
        }
        else
        {
            if (selec.equals("Restar"))
            {
                int resta = n1 - n2;
                String resu = String.valueOf(resta);
                tv1.setText(resu);
            }
            else
            {
                if (selec.equals("Multiplicar"))
                {
                    int mult = n1 * n2;
                    String resu = String.valueOf(mult);
                    tv1.setText(resu);
                }
                else
                {
                    if (selec.equals("Dividir"))
                    {
                        int div = n1 / n2;
                        String resu = String.valueOf(div);
                        tv1.setText(resu);
                    }
                }
            }
        }
    }

}