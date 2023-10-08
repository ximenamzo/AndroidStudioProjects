package com.example.examenparcial1;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;
import java.time.Period;

public class etapavida extends AppCompatActivity implements View.OnClickListener {

    TextView txtname, txtdate, txtage, txtstage, txtsex;
    Button btnvolver;
    String nombre, dia, mes, anio, sexo, etapa;

    String[] stage = {"Maternal (0-2 años)","Kinder (3-5 años)", "Primaria (6-12 años)", "Secundaria (13-15 años)", "Prepa (16-18 años)", "Universidad (19-24 años)", "Trabaja (25-65 años)","Jubilado/a (65 años o más)"};
    String[] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
    Integer day, month, year, age, e;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_etapavida);

        txtname = (TextView) findViewById(R.id.txtname);
        txtdate = (TextView) findViewById(R.id.txtdate);
        txtage = (TextView) findViewById(R.id.txtage);
        txtsex = (TextView) findViewById(R.id.txtsex);
        txtstage = (TextView) findViewById(R.id.txtstage);

        nombre = getIntent().getStringExtra("nombre");
        dia = getIntent().getStringExtra("dia");
        mes = getIntent().getStringExtra("mes");
        anio = getIntent().getStringExtra("anio");
        sexo = getIntent().getStringExtra("sexo");

        day = Integer.parseInt(dia);
        month = findArrayIndex(meses, mes);
        year = Integer.parseInt(anio);

        age = edadactual(year, month, day);
        etapa = getEtapa(age);

        txtname.setText(nombre + "");
        txtdate.setText(dia + " de " + mes + " de " + anio);
        txtage.setText(age + "");
        txtsex.setText(sexo + "");
        txtstage.setText(etapa + "");

        btnvolver = (Button) findViewById(R.id.btnvolver);
        btnvolver.setOnClickListener(view -> finish());
    }

    public String getEtapa(int age){
        if (age >= 0 && age <=2) { etapa = stage[0]; }
          else if (age >= 3 && age <=5) { etapa = stage[1]; }
          else if (age >= 6 && age <=12) { etapa = stage[2]; }
          else if (age >= 13 && age <=15) { etapa = stage[3]; }
          else if (age >= 16 && age <=18) { etapa = stage[4]; }
          else if (age >= 19 && age <=24) { etapa = stage[5]; }
          else if (age >= 25 && age <=65) { etapa = stage[6]; }
          else if (age > 65) { etapa = stage[7]; }
          else { etapa = "null"; }
        return etapa;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public int edadactual(int year, int month, int day) {
        return Period.between(
                LocalDate.of(year, month, day),
                LocalDate.now()
        ).getYears();
    }

    @Override
    public void onClick(View view) {

    }

    public static int findArrayIndex(String[] arr, String str) {
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].equals(str)) { index = i; break; }
        } return index+1;
    }
}