package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText usuario, pass;
    CheckBox recuerdame;
    Button btnLogin;
    SharedPreferences sharedPreferences;
    boolean guardado = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usuario = findViewById(R.id.etUsuario);
        pass = findViewById(R.id.etPass);
        recuerdame = findViewById(R.id.cbGuardar);
        btnLogin = findViewById(R.id.btnLogin);

        sharedPreferences = getSharedPreferences("SHARED_PREF", MODE_PRIVATE);

        guardado = sharedPreferences.getBoolean("CHECKBOX", false);

        if(guardado)
        {
            Intent intent = new Intent(MainActivity.this,Principal.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = usuario.getText().toString();
                String contra = pass.getText().toString();
                boolean checked = recuerdame.isChecked();

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("NAME", name);
                editor.putString("CONTRA", contra);
                editor.putBoolean("CHECKBOX", checked);
                editor.apply();
                Toast.makeText(MainActivity.this, "Información guardada", Toast.LENGTH_LONG).show();
                // Creamos un nuevo Activity
                Intent intent = new Intent(MainActivity.this,Principal.class);
                startActivity(intent);
                finish();
            }
        });
    }
}