package com.example.act1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button sumar,restar;
    TextView texto;
    int num=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumar = (Button) findViewById(R.id.btnSumar);
        restar = (Button) findViewById(R.id.btnRestar);
        texto = (TextView) findViewById(R.id.txt);

        sumar.setOnClickListener(this);
        restar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if ( id == R.id.btnSumar){
            num++;
            texto.setText(num + "");
        } else if (id == R.id.btnRestar) {
            num--;
            texto.setText(num + "");
        }
    }
}