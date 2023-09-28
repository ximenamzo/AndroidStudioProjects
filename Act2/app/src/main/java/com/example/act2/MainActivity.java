package com.example.act2;

import static com.example.act2.R.id.numb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.act2.R.id;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button cero, signo, sumar, restar, plus, minus;
    TextView numero;
    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cero = (Button) findViewById(R.id.btncero);
        signo = (Button) findViewById(R.id.btnsign);
        sumar = (Button) findViewById(R.id.btnsuma);
        restar = (Button) findViewById(R.id.btnresta);
        plus = (Button) findViewById(R.id.btnplus);
        minus = (Button) findViewById(R.id.btnminus);
        numero = (TextView) findViewById(R.id.numb);

        cero.setOnClickListener(this);
        signo.setOnClickListener(this);
        sumar.setOnClickListener(this);
        restar.setOnClickListener(this);
        plus.setOnClickListener(this);
        minus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        /*int id = view.getId();
        if ( id == R.id.btncero){
            num=0;
            numero.setText(num + "");
        } else if (id == R.id.btnsign) {
            if (num > 0){
                num = num * -1;
            } else if (num < 0) {
                num = num * -1;
            }
            numero.setText(num + "");
        } else if ( id == R.id.btnsuma){
            num++;
            numero.setText(num + "");
        } else if (id == R.id.btnresta) {
            num--;
            numero.setText(num + "");
        } else if ( id == R.id.btnplus){
            num=num+10;
            numero.setText(num + "");
        } else if (id == R.id.btnminus) {
            num=num-10;
            numero.setText(num + "");
        }

        int id = view.getId();
        if (id == R.id.btncero) {
            num = 0;
        } else if (id == R.id.btnsign) {
            num = -num;
        } else if (id == R.id.btnsuma) {
            num++;
        } else if (id == R.id.btnresta) {
            num--;
        } else if (id == R.id.btnplus) {
            num += 10;
        } else if (id == R.id.btnminus) {
            num -= 10;
        }
        numero.setText(String.valueOf(num));*/


        switch (view.getId()) {
            case R.id.btncero:
                num = 0;
                break;
            case R.id.btnsign:
                num = -num;
                break;
            case R.id.btnsuma:
                num++;
                break;
            case R.id.btnresta:
                num--;
                break;
            case R.id.btnplus:
                num += 10;
                break;
            case R.id.btnminus:
                num -= 10;
                break;
        }
        numero.setText(String.valueOf(num));
    }
}