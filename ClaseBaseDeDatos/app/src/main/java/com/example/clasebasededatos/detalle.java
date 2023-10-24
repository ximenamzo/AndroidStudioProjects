package com.example.clasebasededatos;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class detalle extends AppCompatActivity {
    TextView out_id, out_nombre, out_telefono;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setTitle("Detalles");

        out_id = (TextView) findViewById(R.id.txtid);
        out_nombre = (TextView) findViewById(R.id.txtnombre);
        out_telefono = (TextView) findViewById(R.id.txttelefono);
        btn = (Button) findViewById(R.id.volver);

        Bundle objeto = getIntent().getExtras(); // trae el objeto
        Usuarios usu = null;
        if(objeto != null) {
            usu = (Usuarios) objeto.getSerializable("usuario");
            out_id.setText(usu.getId().toString());
            out_nombre.setText(usu.getNombre().toString());
            out_telefono.setText(usu.getTelefono().toString());
        } else {
            out_id.setText("No hay datos para mostrar.");
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}