package com.example.calculadora;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    EditText uno, dos;
    Button suma, resta, multi, divi, pot, signo1, signo2, reiniciar, div, mod;
    TextView resultado;
    String primero, segundo, tmp;
    Integer aux, aux2, aux3;
    double n1, n2, res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uno = (EditText)findViewById(R.id.numero1);
        dos = (EditText)findViewById(R.id.numero2);
        resultado = (TextView) findViewById(R.id.txt_num);

        suma = (Button) findViewById(R.id.btnsumar);
        resta = (Button) findViewById(R.id.btnrestar);
        multi = (Button) findViewById(R.id.btnmultiplicar);
        divi = (Button) findViewById(R.id.btndividir);
        pot = (Button) findViewById(R.id.btnpotencia);
        signo1 = (Button) findViewById(R.id.btnsigno1);
        signo2 = (Button) findViewById(R.id.btnsigno2);
        reiniciar = (Button) findViewById(R.id.btnreiniciar);
        div = (Button) findViewById(R.id.btndiv);
        mod = (Button) findViewById(R.id.btnmod);

        suma.setOnClickListener(this);
        resta.setOnClickListener(this);
        multi.setOnClickListener(this);
        divi.setOnClickListener(this);
        pot.setOnClickListener(this);
        signo1.setOnClickListener(this);
        signo2.setOnClickListener(this);
        reiniciar.setOnClickListener(this);
        div.setOnClickListener(this);
        mod.setOnClickListener(this);
    }

    public static int div(int dividendo, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("División por cero no permitida");
        }
        return dividendo / divisor;
    }

    public static int mod(int dividendo, int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("División por cero no permitida");
        }
        return dividendo % divisor;
    }

    private void notificacion(String mensaje) {
        final Toast toast = Toast.makeText(this, mensaje, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        toast.show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 5000);
    }

    @Override
    public void onClick(View view) {
        primero = uno.getText().toString();
        segundo = dos.getText().toString();
        int id = view.getId();

        if (primero.isEmpty() || segundo.isEmpty()){
            resultado.setText("Alerta: Ingrese valores antes.");
            return;
        }
        n1 = Double.parseDouble(primero);
        n2 = Double.parseDouble(segundo); // Float.parseFloat() Integer.parseInt()

        if (id == R.id.btnsumar){
            resultado.setText(primero + " + " + segundo + " = " + (n1+n2) + "");
        } else if (id == R.id.btnrestar){
            resultado.setText(primero + " - " + segundo + " = " + (n1-n2) + "");
        } else if (id == R.id.btnmultiplicar){
            resultado.setText(primero + " * " + segundo + " = " + (n1*n2) + "");
        } else if (id == R.id.btndividir){
            resultado.setText(primero + " / " + segundo + " = " + (n1/n2) + "");
        }else if (id == R.id.btnpotencia){
            resultado.setText(primero + " ^ " + segundo + " = " + (Math.pow(n1, n2)) + "");
        }else if (id == R.id.btnsigno1){
            n1 = -n1;
            uno.setText(String.valueOf(n1));
        }else if (id == R.id.btnsigno2){
            n2 = -n2;
            dos.setText(String.valueOf(n2));
        }else if (id == R.id.btnreiniciar){
            uno.setText("");
            dos.setText("");
            resultado.setText("0");
        } else if (id == R.id.btndiv){
            if (primero.contains("-") || primero.contains(".") || segundo.contains("-") || segundo.contains(".")) {
                notificacion("Error: Operación válida solo con enteros positivos.");
                resultado.setText(resultado.getText());
            } else {
                n1 = Double.parseDouble(primero);
                n2 = Double.parseDouble(segundo);
                aux = (int) Math.round(n1);
                aux2 = (int) Math.round(n2);
                aux3 = div(aux, aux2);
                res = (double) aux3;
                resultado.setText(primero + " DIV " + segundo + " = " + res + "");
            }
        } else if (id == R.id.btnmod) {
            if (primero.contains("-") || primero.contains(".") || segundo.contains("-") || segundo.contains(".")) {
                notificacion("Error: Operación válida solo con enteros positivos.");
                resultado.setText(resultado.getText());
            } else {
                n1 = Double.parseDouble(primero);
                n2 = Double.parseDouble(segundo);
                aux = (int) Math.round(n1);
                aux2 = (int) Math.round(n2);
                aux3 = mod(aux, aux2);
                res = (double) aux3;
                resultado.setText(primero + " MOD " + segundo + " = " + res + "");
            }
        }
    } // OnClick
} // Main Activity