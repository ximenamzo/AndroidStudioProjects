package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Principal extends AppCompatActivity {

    TextView usuario, contra;
    Button salir;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);

        usuario = findViewById(R.id.tvUsuario);
        contra = findViewById(R.id.tvContra);
        salir = findViewById(R.id.btnSalir);

        preferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        String nombre = preferences.getString("NAME", "");
        usuario.setText(nombre);
        String pass = preferences.getString("CONTRA", "");
        usuario.setText(pass);

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.apply();

                Intent intent = new Intent(Principal.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}