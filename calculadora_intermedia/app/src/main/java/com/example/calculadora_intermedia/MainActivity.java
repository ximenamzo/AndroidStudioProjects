package com.example.calculadora_intermedia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button limpiar, borrar, signo, punto, igual, ans;
    Button div, mod, raiz, potencia, porcentaje, division, multiplicacion, resta, suma;
    Button lognat, log, factorial, pi, inverso, exp, absoluto, seno, coseno, tangente;
    Button t1, t2, t3, t4, t5, t6, t7, t8, t9, t0;
    Double op1, op2, numpi, res;
    Double ultimo=0.0;
    Integer aux, aux2, res2;
    Integer flag=0, operador=0;
    Long res3;
    String primero, restxt;
    EditText num;
    TextView resultado;
    CheckBox cientificos;
    Switch radianes;

    private static final int TIEMPO = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        num = (EditText) findViewById(R.id.numero);
        resultado = (TextView) findViewById(R.id.operacion);
        cientificos = (CheckBox) findViewById(R.id.cientifico);
        radianes = (Switch) findViewById(R.id.swrad);
        final LinearLayout layoutCientifico = (LinearLayout) findViewById(R.id.lycientifico);

        limpiar = (Button) findViewById(R.id.btnlimpiar);
        borrar = (Button) findViewById(R.id.btnborrar);
        signo = (Button) findViewById(R.id.btnsigno);
        punto = (Button) findViewById(R.id.btnpunto);
        igual = (Button) findViewById(R.id.btnigual);
        div = (Button) findViewById(R.id.btndiv);
        mod = (Button) findViewById(R.id.btnmod);
        raiz = (Button) findViewById(R.id.btnraiz);
        potencia = (Button) findViewById(R.id.btnpotencia);
        porcentaje = (Button) findViewById(R.id.btnporcentaje);
        division = (Button) findViewById(R.id.btndividir);
        multiplicacion = (Button) findViewById(R.id.btnmulti);
        resta = (Button) findViewById(R.id.btnrestar);
        suma = (Button) findViewById(R.id.btnsumar);

        lognat = (Button) findViewById(R.id.btnln);
        log = (Button) findViewById(R.id.btnlog);
        factorial = (Button) findViewById(R.id.btnfact);
        pi = (Button) findViewById(R.id.btnpi);
        ans = (Button) findViewById(R.id.btnans);
        inverso = (Button) findViewById(R.id.btninverso);
        exp = (Button) findViewById(R.id.btnexp);
        absoluto = (Button) findViewById(R.id.btnabsoluto);
        seno = (Button) findViewById(R.id.btnseno);
        coseno = (Button) findViewById(R.id.btncoseno);
        tangente = (Button) findViewById(R.id.btntangente);

        t1 = (Button) findViewById(R.id.btn1);
        t2 = (Button) findViewById(R.id.btn2);
        t3 = (Button) findViewById(R.id.btn3);
        t4 = (Button) findViewById(R.id.btn4);
        t5 = (Button) findViewById(R.id.btn5);
        t6 = (Button) findViewById(R.id.btn6);
        t7 = (Button) findViewById(R.id.btn7);
        t8 = (Button) findViewById(R.id.btn8);
        t9 = (Button) findViewById(R.id.btn9);
        t0 = (Button) findViewById(R.id.btn0);


        limpiar.setOnClickListener(this);
        borrar.setOnClickListener(this);
        signo.setOnClickListener(this);
        punto.setOnClickListener(this);
        igual.setOnClickListener(this);
        div.setOnClickListener(this);
        mod.setOnClickListener(this);
        raiz.setOnClickListener(this);
        potencia.setOnClickListener(this);
        porcentaje.setOnClickListener(this);
        division.setOnClickListener(this);
        multiplicacion.setOnClickListener(this);
        resta.setOnClickListener(this);
        suma.setOnClickListener(this);

        lognat.setOnClickListener(this);
        log.setOnClickListener(this);
        factorial.setOnClickListener(this);
        pi.setOnClickListener(this);
        ans.setOnClickListener(this);
        inverso.setOnClickListener(this);
        absoluto.setOnClickListener(this);
        exp.setOnClickListener(this);
        seno.setOnClickListener(this);
        coseno.setOnClickListener(this);
        tangente.setOnClickListener(this);

        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t0.setOnClickListener(this);

        cientificos.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    layoutCientifico.setVisibility(View.VISIBLE);
                } else {
                    layoutCientifico.setVisibility(View.GONE);
                }
            }
        });

        //radianes.setOnCheckedChangeListener();
    }

    public static long factorial(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("El factorial no puede realizarse con números negativos");
        }
        if (n == 0 || n == 1) {
            return 1;
        } else {
            long res = 1;
            for (int i = 2; i <= n; i++) {
                res *= i;
            }
            return res;
        }
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
        }, TIEMPO);
    }



    @Override
    public void onClick(View view) {
        primero = num.getText().toString();
        int id = view.getId();
        ultimo = res;

        if (!primero.isEmpty()){
            if (primero.length() == 1 && "+-".contains(primero)) {
                num.setText(primero);
                if (id == R.id.btnans){
                    num.setText(primero + ultimo + "");
                }
            } else {
                if (id == R.id.btnsumar) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(primero + " + ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 1;
                    flag = 1;
                }
                if (id == R.id.btnrestar) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(primero + " − ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 2;
                    flag = 1;
                }
                if (id == R.id.btnmulti) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(primero + " × ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 3;
                    flag = 1;
                }
                if (id == R.id.btndividir) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(primero + " ÷ ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 4;
                    flag = 1;
                }
                if (id == R.id.btnpotencia) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(primero + " ^ ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 5;
                    flag = 1;
                }
                if (id == R.id.btnexp) {
                    if ("-".contains(primero)) {
                        notificacion("Error: Operación inválida (número negativo).");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText(num.getText() + " E+ ");
                        num.setText(primero);
                        // num.setText("");
                        operador = 6;
                    }
                    flag = 1;
                }
                if (id == R.id.btnporcentaje) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText(num.getText() + "% × ");
                    num.setText(primero);
                    // num.setText("");
                    operador = 7;
                    flag = 1;
                }
                if (id == R.id.btnraiz) {
                    if ("-".contains(primero)) {
                        notificacion("Error: Operación inválida (número negativo).");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText("√(" + num.getText() + ") = ");
                        num.setText(Math.sqrt(op1) + "");
                        flag = 1;
                        // operador = 8;
                    }
                }
                if (id == R.id.btndiv) {
                    if (primero.contains(".")) {
                        notificacion("Alerta: Operación válida únicamente para números enteros.");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText(num.getText() + " DIV ");
                        num.setText("");
                        operador = 9;
                    }
                }
                if (id == R.id.btnmod) {
                    if (primero.contains(".")) {
                        notificacion("Alerta: Operación válida únicamente para números enteros.");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText(num.getText() + " MOD ");
                        num.setText("");
                        operador = 10;
                    }
                }
                if (id == R.id.btnln) {
                    if (primero.contains("-")) {
                        notificacion("Error: Operación inválida (número negativo).");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText("ln(" + num.getText() + ") = ");
                        num.setText(Math.log(op1) + "");
                        flag = 1;
                        // operador = 11;
                    }
                }
                if (id == R.id.btnlog) {
                    if (primero.contains("-")) {
                        notificacion("Error: Operación inválida (número negativo).");
                        num.setText(primero);
                    } else {
                        op1 = Double.parseDouble(primero);
                        resultado.setText("log(" + num.getText() + ") = ");
                        num.setText(Math.log10(op1) + "");
                        flag = 1;
                        // operador = 12;
                    }
                }
                if (id == R.id.btnfact) {
                    if (primero.contains("-")) {
                        notificacion("Error: Operación inválida (número negativo).");
                        num.setText(primero);
                        flag = 1;
                    } else if (primero.contains(".") || (".").contains(primero)) {
                        notificacion("Alerta: Debe ser numero entero.");
                        num.setText(primero);
                        flag = 1;
                    } else {
                        aux = Integer.parseInt(primero);
                        if (aux > 20 || aux < 0) {
                            notificacion("Alerta: Operación Inválida.");
                            num.setText(primero);
                            flag = 1;
                        } else {
                            resultado.setText(primero + "! =");
                            num.setText(factorial(aux) + "");
                            flag = 1;
                            // operador = 13;
                        }
                    }
                }
                if (id == R.id.btninverso) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText("1 / " + primero);
                    num.setText((1.0 / op1) + "");
                    flag = 1;
                    // operador = 14;
                }
                if (id == R.id.btnabsoluto) {
                    if (primero.contains("-")) {
                        resultado.setText("|" + primero + "|");
                        num.setText(primero.substring(1));
                    } else {
                        resultado.setText("|" + primero + "|");
                        num.setText(primero);
                    }
                    flag = 1;
                    // operador = 15;
                }
                if (id == R.id.btnseno) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText("sin(" + primero + ")");
                    num.setText(Math.sin(op1) + "");
                    flag = 1;
                    // operador = 16;
                }
                if (id == R.id.btncoseno) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText("cos(" + primero + ")");
                    num.setText(Math.cos(op1) + "");
                    flag = 1;
                    // operador = 17;
                }
                if (id == R.id.btntangente) {
                    op1 = Double.parseDouble(primero);
                    resultado.setText("tan(" + primero + ")");
                    num.setText(Math.tan(op1) + "");
                    flag = 1;
                    // operador = 18;
                }

                if (id == R.id.btnigual) {
                    if (operador == 0){
                        notificacion("Ingrese valor u operación.");
                    }
                    if (operador == 1){
                        // Suma
                        restxt = resultado.getText().toString();
                        if (restxt.endsWith("= ")){
                            resultado.setText(res + " + " + op2 + " = ");
                        } else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                        }
                        res = op1 + op2;
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                    }
                    if (operador == 2){
                        // Resta
                        restxt = resultado.getText().toString();
                        if (restxt.endsWith("= ")){
                            resultado.setText(res + " - " + op2 + " = ");
                        } else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                        }
                        res = op1 - op2;
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                    }
                    if (operador == 3){
                        // Multiplicacion
                        restxt = resultado.getText().toString();
                        if (restxt.endsWith("= ")){
                            resultado.setText(res + " * " + op2 + " = ");
                        } else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                        }
                        res = op1 * op2;
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                    }
                    if (operador == 4){
                        // Division
                        restxt = resultado.getText().toString();
                        if (restxt.endsWith("= ")){
                            resultado.setText(res + " / " + op2 + " = ");
                        } else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                        }
                        res = op1 / op2;
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                    }
                    if (operador == 5){
                        // Potencia
                        restxt = resultado.getText().toString();
                        if (restxt.endsWith("= ")){
                            resultado.setText(res + " ^ " + op2 + " = ");
                        } else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                        }
                        res = Math.pow(op1, op2);
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                    }
                    if (operador == 6){
                        // Exponente
                        op2 = Double.parseDouble(primero);
                        resultado.setText(resultado.getText() + primero + "= ");
                        res = op1 * (Math.pow(10, op2));
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 7){
                        // Porcentaje
                        op2 = Double.parseDouble(primero);
                        resultado.setText(resultado.getText() + primero + "= ");
                        res = op2 * (op1/100);
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 8){
                        // Raiz
                        op1 = Double.parseDouble(primero);
                        res = Math.sqrt(op1);
                        resultado.setText("√(" + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 9){
                        // Div
                        if (primero.contains("-")) {
                            notificacion("Error: Inválido (división por negativo).");
                            num.setText(primero);
                        }
                        else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                            aux = (int) Math.round(op1);
                            aux2 = (int) Math.round(op2);
                            res2 = div(aux, aux2);
                            res = (double) res2;
                            num.setText(res + "");
                            op1 = res;
                            flag = 1;
                        }
                        operador = 0;
                    }
                    if (operador == 10){
                        // Mod
                        if (primero.contains("-")) {
                            notificacion("Error: Inválido (división por negativo).");
                            num.setText(primero);
                        }
                        else {
                            op2 = Double.parseDouble(primero);
                            resultado.setText(resultado.getText() + primero + " = ");
                            aux = (int) Math.round(op1);
                            aux2 = (int) Math.round(op2);
                            res2 = mod(aux, aux2);
                            res = (double) res2;
                            num.setText(res + "");
                            op1 = res;
                            flag = 1;
                        }
                        operador = 0;
                    }
                    if (operador == 11){
                        // ln
                        op1 = Double.parseDouble(primero);
                        res = Math.log(op1);
                        resultado.setText("ln(" + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;

                    }
                    if (operador == 12){
                        // log
                        op1 = Double.parseDouble(primero);
                        res = Math.log10(op1);
                        resultado.setText("log(" + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 13){
                        // factorial
                        op1 = Double.parseDouble(primero);
                        aux = (int) Math.round(op1);
                        if (aux > 20 || aux < 0){
                            notificacion("Error: Operación inválida.");
                            flag = 1;
                        } else {
                            res3 = factorial(aux);
                            res = (double) res3;
                            resultado.setText(op1 + "! = ");
                            num.setText(res + "");
                            op1 = res;
                            flag = 1;
                        }
                        operador = 0;
                    }
                    if (operador == 14){
                        // inverso
                        op1 = Double.parseDouble(primero);
                        res = 1 / op1;
                        resultado.setText("1 / " + op1 + " = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 15){
                        // absoluto
                        op1 = Double.parseDouble(primero);
                        resultado.setText("|" + op1 + "| = ");
                        if (op1 < 0) op1 = -op1;
                        num.setText(op1 + "");
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 16){
                        // seno
                        op1 = Double.parseDouble(primero);
                        res = Math.sin(op1);
                        resultado.setText("sin( " + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 17){
                        //coseno
                        op1 = Double.parseDouble(primero);
                        res = Math.cos(op1);
                        resultado.setText("cos( " + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                    if (operador == 18){
                        //tangente
                        op1 = Double.parseDouble(primero);
                        res = Math.tan(op1);
                        resultado.setText("tan( " + op1 + ") = ");
                        num.setText(res + "");
                        op1 = res;
                        flag = 1;
                        operador = 0;
                    }
                }
            } // Evitar error de solo haber + o -



            if (id == R.id.btnsigno) {
                if (primero.length() == 1 && "+".contains(primero)) {
                    num.setText("-");
                } else if (primero.length() == 1 && "-".contains(primero)) {
                    num.setText("+");
                } else {
                    op1 = Double.parseDouble(primero);
                    op1 = -op1;
                    primero = String.valueOf(op1);
                    num.setText(primero);
                }
            }
            if (id == R.id.btnborrar) {
                if (primero.length() == 1) {
                    num.setText("");
                } else if (primero.length() > 1) {
                    primero = primero.substring(0, primero.length() - 1);
                    num.setText(primero);
                }
                flag = 0;
            }
        } // !empty o sea cuando si tiene algo

        if (primero.isEmpty()) {
            if (id == R.id.btnrestar) {
                flag = 0;
                num.setText("-");
            }
            if (id == R.id.btnsumar) {
                num.setText("+");
            }
            if (id == R.id.btnsigno) {
                notificacion("Ingresa valores.");
            }
            if (id == R.id.btnigual) {
                notificacion("Error: Ingrese valor.");
                num.setHint("0");
            }
            if (id == R.id.btnraiz) {
                num.setHint("0");
                resultado.setText("√( ");
                operador = 8;
            }
            if (id == R.id.btnln) {
                num.setHint("0");
                resultado.setText("ln( ");
                operador = 11;
            }
            if (id == R.id.btnlog) {
                num.setHint("0");
                resultado.setText("log( ");
                operador = 12;
            }
            if (id == R.id.btnfact) {
                num.setHint("0");
                resultado.setText("( )!");
                operador = 13;
            }
            if (id == R.id.btninverso) {
                num.setHint("0");
                resultado.setText("1 / x ");
                operador = 14;
            }
            if (id == R.id.btnabsoluto) {
                num.setHint("0");
                resultado.setText("|x| ");
                operador = 15;
            }
            if (id == R.id.btnseno) {
                num.setHint("0");
                resultado.setText("sin( ");
                operador = 16;
            }
            if (id == R.id.btncoseno) {
                num.setHint("0");
                resultado.setText("cos( ");
                operador = 17;
            }
            if (id == R.id.btntangente) {
                num.setHint("0");
                resultado.setText("tan( ");
                operador = 18;
            }
        }

        if (id == R.id.btnlimpiar) {
            ultimo = res;
            num.setText("");
            num.setHint("0");
            resultado.setText("0");
            operador = 0;
            op1 = 0.0;
            op2 = 0.0;
            aux = 0;
            aux2 = 0;
        }

        if(id == R.id.btn0){
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"0");
        } else if (id==R.id.btn1){
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"1");
        } else if (id == R.id.btn2) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"2");
        }else if (id == R.id.btn3) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"3");
        }else if (id == R.id.btn4) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"4");
        }else if (id == R.id.btn5) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"5");
        }else if (id == R.id.btn6) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"6");
        }else if (id == R.id.btn7) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"7");
        }else if (id == R.id.btn8) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"8");
        }else if (id == R.id.btn9) {
            if (flag == 1) num.setText("");
            flag = 0;
            num.setText(num.getText()+"9");
        }else if (id == R.id.btnpunto) {
            if (flag == 1) num.setText("");
            flag = 0;
            String act = num.getText().toString();
            if(!act.contains(".")) {
                if(!act.isEmpty()) {
                    num.setText(act + ".");
                } else {
                    num.setText("0.");
                }
            }
        } else if (id == R.id.btnpi) {
            String act = num.getText().toString();
            if(!act.contains("3.1415")) {
                num.setText("");
                numpi = Math.PI;
                num.setText(numpi.toString());
            }
            flag = 1;
        }
        else if (id == R.id.btnans) {
            num.setText(ultimo + "");
        }
    }
}