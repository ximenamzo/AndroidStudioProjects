package com.example.imagenes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    ImageButton imagen;
    Button boton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imagen = findViewById(R.id.ibClick);
        boton = findViewById(R.id.btnClick);

        //Método on Click dentro del OnCreate
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast notificacion = Toast.makeText(MainActivity.this,
                        "Esto es botón de imágen", Toast.LENGTH_LONG);
                notificacion.show();
            }
        });

        boton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast notificacion = Toast.makeText(MainActivity.this,
                        "Esto es botón normal", Toast.LENGTH_LONG);
                notificacion.show();
            }
        });
    }
}