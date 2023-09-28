package com.example.firebase;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button btnAgregar;
    EditText etNombre, etCuenta, etEdad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAgregar = findViewById(R.id.btnAgregar);
        etNombre = findViewById(R.id.etNombre);
        etCuenta = findViewById(R.id.etCuenta);
        etEdad = findViewById(R.id.etEdad);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myref = database.getReference();
                myref.child("Nombre").setValue("Christian");
            }
        });

    }
}