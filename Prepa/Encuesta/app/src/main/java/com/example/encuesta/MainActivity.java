package com.example.encuesta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView resultado;
    CheckBox cb1, cb2, cb3, cb4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resultado = findViewById(R.id.tvresultado);
        cb1 = findViewById(R.id.cbjava);
        cb2 = findViewById(R.id.cbcpp);
        cb3 = findViewById(R.id.cbpython);
        cb4 = findViewById(R.id.cbexcel);
    }

    public void encuesta (View view)
    {
        String res ="";
        if(cb1.isChecked()==true)
        {
            res = res + " Java";
        }
        if(cb2.isChecked()==true)
        {
            res = res + " C++";
        }
        if(cb3.isChecked()==true)
        {
            res = res + " Python";
        }
        if(cb4.isChecked()==true)
        {
            res = res + " Excel";
        }
        resultado.setText("La selección de tus lenguajes fue: " + res);
    }
}