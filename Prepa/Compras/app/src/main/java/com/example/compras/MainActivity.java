package com.example.compras;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView total;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10;
    RadioButton rb1, rb2, rb3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        total = findViewById(R.id.tvTotal);
        cb1 = findViewById(R.id.cbLeoSen);
        cb2 = findViewById(R.id.cbLeoDet);
        cb3 = findViewById(R.id.cbManLar);
        cb4 = findViewById(R.id.cbFalda);
        cb5 = findViewById(R.id.cbMallas);
        cb6 = findViewById(R.id.cbCalCor);
        cb7 = findViewById(R.id.cbCalLar);
        cb8 = findViewById(R.id.cbZap);
        cb9 = findViewById(R.id.cbPunBlo);
        cb10 = findViewById(R.id.cbPunGay);
        rb1 = findViewById(R.id.rbCon);
        rb2 = findViewById(R.id.rbTarCre);
        rb3 = findViewById(R.id.rbTarDeb);
    }

    public void compras(View view){
        int tot = 0;
        if(cb1.isChecked()==true)
        {
            tot = tot + 339;
        }
        if(cb2.isChecked()==true)
        {
            tot = tot + 379;
        }
        if(cb3.isChecked()==true)
        {
            tot = tot + 446;
        }
        if(cb4.isChecked()==true)
        {
            tot = tot + 153;
        }
        if(cb5.isChecked()==true)
        {
            tot = tot + 199;
        }
        if(cb6.isChecked()==true)
        {
            tot = tot + 199;
        }
        if(cb7.isChecked()==true)
        {
            tot = tot + 259;
        }
        if(cb8.isChecked()==true)
        {
            tot = tot + 309;
        }
        if(cb9.isChecked()==true)
        {
            tot = tot + 2199;
        }
        if(cb10.isChecked()==true)
        {
            tot = tot + 3495;
        }
        String tot2 = String.valueOf(tot);

        String op = "";
        if(rb1.isChecked()==true)
        {
            op = op + "de contado.";
        }
        else if(rb2.isChecked()==true)
        {
            op = op + "con tarjeta de crédito.";
        }
        else if(rb3.isChecked()==true)
        {
            op = op + "con tarjeta de débito.";
        }

        total.setText("Tu total será $" + tot + " pesos. Tu método de pago será " + op);
    }
}