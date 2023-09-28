package com.example.act1_xmc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button cero, signo, sumar, restar, sum10, res10;
    TextView numero;
    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cero = (Button)findViewById(R.id.cero);
        signo = (Button)findViewById(R.id.sign);
        sumar = (Button)findViewById(R.id.suma);
        restar = (Button)findViewById(R.id.resta);
        sum10 = (Button)findViewById(R.id.suma10);
        res10 = (Button)findViewById(R.id.resta10);

        numero = (TextView)findViewById(R.id.numb);

        cero.setOnClickListener(this);
        signo.setOnClickListener(this);
        sumar.setOnClickListener(this);
        restar.setOnClickListener(this);
        sum10.setOnClickListener(this);
        res10.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        // int id = view.getId();
        int id = view.getId();
        if (id == R.id.cero) {
            num = 0;
        } else if (id == R.id.sign) {
            num = -num;
        } else if (id == R.id.suma) {
            num++;
        } else if (id == R.id.resta) {
            num--;
        } else if (id == R.id.suma10) {
            num += 10;
        } else if (id == R.id.resta10) {
            num -= 10;
        }
        numero.setText(String.valueOf(num));
    }
}