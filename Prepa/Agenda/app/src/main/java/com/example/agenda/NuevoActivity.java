package com.example.agenda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.agenda.db.DbContactos;

public class NuevoActivity extends AppCompatActivity {

    EditText etNombre, etTelefono, etEmail;
    Button btnGuardar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nuevo);

        etNombre = findViewById(R.id.etNombre);
        etTelefono = findViewById(R.id.etTelefono);
        etEmail = findViewById(R.id.etEmail);
        btnGuardar = findViewById(R.id.btnGuardar);

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DbContactos dbContactos = new DbContactos(NuevoActivity.this);
                long id = dbContactos.insertarContactos(etNombre.getText().toString(),
                        etTelefono.getText().toString(), etEmail.getText().toString());
                if (id>0)
                {
                    Toast.makeText(NuevoActivity.this, "Registro Guardado", Toast.LENGTH_LONG).show();
                    limpiar();
                }
                else
                {
                    Toast.makeText(NuevoActivity.this, "Registro Guardado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void limpiar()
    {
        etNombre.setText("");
        etTelefono.setText("");
        etEmail.setText("");
    }

}