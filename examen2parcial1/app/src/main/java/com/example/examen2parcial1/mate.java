package com.example.examen2parcial1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


public class mate extends AppCompatActivity implements View.OnClickListener {

    Button btncalifica;
    String nombre, materia, calificacion;
    RadioGroup rgep1, rgep2, rgep3, rgep4;
    RadioButton rb1ep1, rb2ep1, rb3ep1, rb1ep2, rb2ep2, rb3ep2, rb1ep3, rb2ep3, rb3ep3, rb1ep4, rb2ep4, rb3ep4;
    Integer cont=0, calif=0;
    Intent i;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mate);

        nombre = getIntent().getStringExtra("nombre");
        materia = getIntent().getStringExtra("materia");

        rgep1 = (RadioGroup) findViewById(R.id.rgep1);
        rb1ep1 = (RadioButton) findViewById(R.id.rb1ep1);
        rb2ep1 = (RadioButton) findViewById(R.id.rb2ep1);
        rb3ep1 = (RadioButton) findViewById(R.id.rb3ep1);
        rgep1.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb2ep1) {
                calif++;
                cont++;
            } else if (checkedId == R.id.rb1ep1 || checkedId == R.id.rb3ep1) {
                cont++;
            }
        });

        rgep2 = (RadioGroup) findViewById(R.id.rgep2);
        rb1ep2 = (RadioButton) findViewById(R.id.rb1ep2);
        rb2ep2 = (RadioButton) findViewById(R.id.rb2ep2);
        rb3ep2 = (RadioButton) findViewById(R.id.rb3ep2);
        rgep2.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1ep2) {
                calif++;
                cont++;
            } else if (checkedId == R.id.rb2ep2 || checkedId == R.id.rb3ep2) {
                cont++;
            }
        });

        rgep3 = (RadioGroup) findViewById(R.id.rgep3);
        rb1ep3 = (RadioButton) findViewById(R.id.rb1ep3);
        rb2ep3 = (RadioButton) findViewById(R.id.rb2ep3);
        rb3ep3 = (RadioButton) findViewById(R.id.rb3ep3);
        rgep3.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb3ep3) {
                calif++;
                cont++;
            } else if (checkedId == R.id.rb1ep3 || checkedId == R.id.rb2ep3) {
                cont++;
            }
        });

        rgep4 = (RadioGroup) findViewById(R.id.rgep4);
        rb1ep4 = (RadioButton) findViewById(R.id.rb1ep4);
        rb2ep4 = (RadioButton) findViewById(R.id.rb2ep4);
        rb3ep4 = (RadioButton) findViewById(R.id.rb3ep4);
        rgep4.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb1ep4) {
                calif++;
                cont++;
            } else if (checkedId == R.id.rb2ep4 || checkedId == R.id.rb3ep4) {
                cont++;
            }
        });


        btncalifica = (Button) findViewById(R.id.btncalifica);
        btncalifica.setOnClickListener(view -> {
            if (cont<4) {
                Toast.makeText(mate.this, "No has terminado de responder.", Toast.LENGTH_LONG).show();
            } else {
                calificacion = calif.toString();
                i = new Intent(mate.this, califica.class);
                i.putExtra("nombre", nombre);
                i.putExtra("materia", materia);
                i.putExtra("calificacion", calificacion);
                startActivity(i);
                finish();
            }
        });
    }

    @Override
    public void onClick(View view) {

    }
}