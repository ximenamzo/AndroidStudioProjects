package com.example.guardardatos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText etDato;
    Button btnGuardar, btnMostrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Context context = this;
        SharedPreferences sharedPreferences = getSharedPreferences("archivoSP", context.MODE_PRIVATE);

        etDato = findViewById(R.id.etTexto);
        btnMostrar = findViewById(R.id.btnMostrar);
        btnGuardar = findViewById(R.id.btnGrabar);

        btnMostrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = getPreferences(context .MODE_PRIVATE);
                String valor = sharedPreferences1.getString("midato", "no dato");
                Toast.makeText(getApplicationContext(), "Dato guardado: "+valor, Toast.LENGTH_LONG).show();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences1 = getPreferences(context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences1.edit();
                editor.putString("midato", etDato.getText().toString());
                editor.commit();
                Toast.makeText(getApplicationContext(),"El dato se ha guardado", Toast.LENGTH_LONG).show();
            }
        });
    }
}