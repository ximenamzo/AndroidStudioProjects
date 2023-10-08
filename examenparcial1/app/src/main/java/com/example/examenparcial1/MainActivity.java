package com.example.examenparcial1;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDate;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener {

    EditText editnombre, editdia, editanio;
    RadioGroup rgsexo;
    RadioButton rbhombre, rbmujer;
    Button continuar;
    String nombre, dia, mes, anio, sexo;
    Toast mensaje;
    Intent i;
    Spinner spnmes;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editnombre = (EditText) findViewById(R.id.editnombre);
        editdia = (EditText) findViewById(R.id.editdia);
        editanio = (EditText) findViewById(R.id.editanio);

        spnmes = (Spinner) findViewById(R.id.spnmes);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this, R.array.meses, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnmes.setAdapter(adapter);
        spnmes.setOnItemSelectedListener(this);

        rgsexo = (RadioGroup) findViewById(R.id.rgsexo);
        rbhombre = (RadioButton) findViewById(R.id.rbhombre);
        rbmujer = (RadioButton) findViewById(R.id.rbmujer);
        rgsexo.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbhombre) sexo = "Hombre";
            if (checkedId == R.id.rbmujer) sexo = "Mujer";
        });

        continuar = (Button) findViewById(R.id.btncontinuar);
        continuar.setOnClickListener(view -> {
            nombre = editnombre.getText().toString();
            dia = editdia.getText().toString();
            anio = editanio.getText().toString();

            if (nombre.isEmpty() || dia.isEmpty() || mes.isEmpty() || anio.isEmpty() || sexo.isEmpty()) {
                mensaje = Toast.makeText(MainActivity.this, "Ingresa la información faltante.", Toast.LENGTH_LONG);
                mensaje.show();
            } else {
                int auxd = Integer.parseInt(dia);
                int auxa = Integer.parseInt(anio);
                int auxm = findArrayIndex(mes);
                int auxb = auxa%4;
                LocalDate fechoy = LocalDate.now();
                int a = fechoy.getYear();
                int m = fechoy.getMonthValue();
                int d = fechoy.getDayOfMonth();

                if (auxd<0 || auxd>31) {
                    Toast.makeText(MainActivity.this, "Error: Día inválido.", Toast.LENGTH_LONG).show();
                } else if (auxa<1907) {
                    Toast.makeText(MainActivity.this, "Error: Año inválido.", Toast.LENGTH_LONG).show();
                } else if (auxa==1907) {
                    if (auxm>=3) {
                        if (auxm==3 && auxd<4) {
                            Toast.makeText(MainActivity.this, "Error: Ni la persona más longeva del mundo nació antes de la fecha ingresada.", Toast.LENGTH_LONG).show();
                        } else verificar(auxd, auxm, auxb);
                    } Toast.makeText(MainActivity.this, "Error: Ni la persona más longeva del mundo nació antes de la fecha ingresada.", Toast.LENGTH_LONG).show();
                } else if (auxa>a) {
                    Toast.makeText(MainActivity.this, "Error: Aún no llegamos a ese año.", Toast.LENGTH_LONG).show();
                } else if (auxa==a) {
                    if (auxm>m) {
                        Toast.makeText(MainActivity.this, "Error: Aún no llegamos a ese mes.", Toast.LENGTH_LONG).show();
                    } else if (auxm==m) {
                        if (auxd>d) {
                            Toast.makeText(MainActivity.this, "Error: Aún no llegamos a ese día.", Toast.LENGTH_LONG).show();
                        } else {
                            if (auxd==d) Toast.makeText(MainActivity.this, "Mensaje: Te damos la bienvenida a la vida.", Toast.LENGTH_LONG).show();
                            iniciar();
                        }
                    } else verificar(auxd, auxm, auxb);
                } else verificar(auxd, auxm, auxb);
            }
        });
    }

    public void verificar(int auxd, int auxm, int auxb){
        if (auxm==2){
            if (auxd<=29) {
                if(auxd==29 && auxb==0){
                    iniciar();
                } else Toast.makeText(MainActivity.this, "Error: "+anio+" no es año bisiesto.", Toast.LENGTH_LONG).show();
                if (auxd<29 && auxb!=0) iniciar();
            } else Toast.makeText(MainActivity.this, "Error: Excediste el día ingresado.", Toast.LENGTH_LONG).show();
        } else {
            if (auxm==4 || auxm==6 || auxm==9 || auxm==11) {
                if (auxd>30) {
                    Toast.makeText(MainActivity.this, "Error: Excediste los días del mes.", Toast.LENGTH_LONG).show();
                } else iniciar();
            } else iniciar();
        }
    }

    protected void iniciar(){
        i = new Intent(MainActivity.this, etapavida.class);
        i.putExtra("nombre", nombre);
        i.putExtra("dia", dia);
        i.putExtra("mes", mes);
        i.putExtra("anio", anio);
        i.putExtra("sexo", sexo);
        startActivity(i);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
        mes = (String) parent.getItemAtPosition(pos);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        mes = "";
    }

    protected void onResume() {
        super.onResume();
        editnombre.setText("");
        editdia.setText("");
        editanio.setText("");
        spnmes.setSelection(0);
        rgsexo.clearCheck();
    }

    public static int findArrayIndex(String str) {
        String[] meses = {"enero","febrero","marzo","abril","mayo","junio","julio","agosto","septiembre","octubre","noviembre","diciembre"};
        int index = -1;
        for (int i = 0; i < meses.length; i++) {
            if (meses[i].equals(str)) { index = i; break; }
        } return index+1;
    }

    @Override
    public void onClick(View view) {

    }
}
