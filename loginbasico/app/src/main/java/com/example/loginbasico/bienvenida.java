package com.example.loginbasico;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class bienvenida extends AppCompatActivity {

    TextView txtbnvn, txtsaludo;
    String nombre, pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bienvenida);
        txtbnvn = (TextView) findViewById(R.id.txtbienvenida);
        txtsaludo = (TextView) findViewById(R.id.txthola);
        nombre = getIntent().getStringExtra("usuario");
        pass = getIntent().getStringExtra("password");
        txtsaludo.setText("Hola " + nombre + "!!");
        txtsaludo.setVisibility(View.VISIBLE);
    }
}