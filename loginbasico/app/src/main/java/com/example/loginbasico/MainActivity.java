package com.example.loginbasico;

import static android.widget.Toast.*;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    EditText usuario, password;
    Button ingresar;
    String us, ps;
    Toast mensaje;
    Intent i; // Intent to change the activity

    String[][] nombres = {{"ximena", "123"},{"manzo", "456"},{"cast", "789"}};
    Boolean existe = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Ingresa tus credenciales");

        usuario = (EditText) findViewById(R.id.editusuario);
        password = (EditText) findViewById(R.id.editpassword);
        ingresar = (Button) findViewById(R.id.btningresar);

        ingresar.setOnClickListener(view -> {
            us = usuario.getText().toString();
            ps = password.getText().toString();

            // Popup
            // Sirve para debuggear y ver nombres de variables
            // mensaje = Toast.makeText(MainActivity.this, us + " " + ps, LENGTH_LONG);

            for (int c=0; c<nombres.length; c++) {
                if (us.equals(nombres[c][0]) && ps.equals(nombres[c][1])){
                    existe = true;
                }
            }

            if (existe) {
                //Toast.makeText(MainActivity.this, "Bienvenid@", Toast.LENGTH_SHORT).show();
                i = new Intent(MainActivity.this, bienvenida.class);
                i.putExtra("usuario", us);
                i.putExtra("password", ps);
                startActivity(i);
            } else {
                Toast.makeText(MainActivity.this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
            }

            /*if (us.isEmpty() || ps.isEmpty()) {
                mensaje = Toast.makeText(MainActivity.this, "Debes llenar los campos primero", Toast.LENGTH_LONG);
                mensaje.show();
            } else if (us.equals("xime") && ps.equals("123")) {
                mensaje = Toast.makeText(MainActivity.this, "Bienvenid@", Toast.LENGTH_SHORT);
                mensaje.show();
                i = new Intent(MainActivity.this, bienvenida.class);
                i.putExtra("usuario", us);
                i.putExtra("password", ps);
                startActivity(i);
            }*/
        });
    }

    @Override
    public void onClick(View view) {

    }
}