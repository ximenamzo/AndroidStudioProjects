package com.example.cambio_moneda;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;

public class main2 extends AppCompatActivity implements View.OnClickListener {

    private static final String API_BASE_URL = "https://api.currencyapi.com/v3/";
    private static final String API_KEY = "cur_live_6ZNhaifAhEyKuZPIce9EA43LDjIrKqNKEepmIlYw";
    EditText etx1, etx2;
    Spinner spn1, spn2;
    Button btn;
    ApiService apiService; // interfaz
    ApiResponse apiResponse; // clase

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etx1 = findViewById(R.id.edit1);
        etx2 = findViewById(R.id.edit2);
        spn1 = findViewById(R.id.spinner1);
        spn2 = findViewById(R.id.spinner2);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);

        // Adaptador de la interfaz Java a llamadas HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("DEBUGX", "(52) Retrofit construido.");

        // Creando interfaz con el adaptador
        apiService = retrofit.create(ApiService.class);

        // Invocación de un método Retrofit que envia una petición a un servidor web y envía una respuesta
        // Cada llamada produce su propia respuesta HTTP 
        Call<ApiResponse> call = apiService.getCurrencyData();
        Log.d("DEBUGX", "(60) Llamada conseguida.");

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                try {
                    // Si hay éxito en la respuesta
                    if (response.isSuccessful()) {
                        Log.d("DEBUGX", "(68) Respuesta exitosa.");

                        // Respuesta igual a contenido
                        apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getData() != null) {

                            // Muestro en log toda la respuesta
                            Log.d("API_RESPONSE", "(75)." + apiResponse.getData().toString());

                            // Lleno los Arrays con los códigos "code" del JSON
                            Set<String> codigosDivisa = apiResponse.getData().keySet();
                            ArrayList<String> listaCodigosDivisa = new ArrayList<>(codigosDivisa);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(main2.this,
                                    android.R.layout.simple_spinner_item, listaCodigosDivisa);

                            // Lleno los spinner con los arrays
                            spn1.setAdapter(adapter);
                            spn2.setAdapter(adapter);
                        } else {
                            // Si la respuesta de la API está vacía
                            Log.e("ERRORX", "(88) ApiResponse Null - vacía.");
                        }
                    } else {
                        // Si no hay respuesta
                        Toast.makeText(main2.this, "Error al obtener datos de moneda.", Toast.LENGTH_SHORT).show();
                        Log.e("ERRORX", "(93) Respuesta sin éxito. Código: " + response.code());
                        try {
                            // Try para "intentar" traer la descripción del código
                            String errorBody = response.errorBody().string();
                            Log.e("DEBUGX", "(97) Cuerpo de respuesta de error: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    // Si falla el try de toda la respuesta de llamada a la API
                    e.printStackTrace();
                    Log.e("ERRORX", "(105) Excepción durante llamada a API: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(main2.this, "Error de red", Toast.LENGTH_SHORT).show();
                Log.e("ERRORX", "(112) Error de red durante la llamada a la API: " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            String codigoOrigen = spn1.getSelectedItem().toString();
            String codigoDestino = spn2.getSelectedItem().toString();

            CurrencyData monedaOrigen = apiResponse.getData().get(codigoOrigen);
            CurrencyData monedaDestino = apiResponse.getData().get(codigoDestino);

            if (apiResponse != null) {
                if (monedaOrigen != null && monedaDestino != null) {
                    try {
                        double cantidadIngresada = Double.parseDouble(etx1.getText().toString());
                        double valorMonedaOrigen = monedaOrigen.getValue();
                        double valorMonedaDestino = monedaDestino.getValue();

                        double resultado = (cantidadIngresada / valorMonedaOrigen) * valorMonedaDestino;

                        etx2.setText(String.valueOf(resultado));
                        Log.d("SUCCES", "(136) Resultado: " + resultado);

                    } catch (NumberFormatException e) {
                        // Si hay errores de tipos de datos
                        Toast.makeText(main2.this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show();
                        Log.w("WARNINGX", "(141) Number Format Exception.");
                    }
                }
            } else {
                Log.d("ERRORX", "(145) Api Response Null.");
            }
        }
    }

    public class ApiResponse {
        @SerializedName("data")
        private Map<String, CurrencyData> data;
        public ApiResponse(Map<String, CurrencyData> data) {
            this.data = data;
        }
        public Map<String, CurrencyData> getData() {
            return data;
        }
    }

    public class CurrencyData {
        private String code;
        private double value;
        public CurrencyData(String code, double value) {
            this.code = code;
            this.value = value;
        }
        public String getCode() {
            return code;
        }
        public double getValue() {
            return value;
        }
    }

    public interface ApiService {
        @GET("latest?apikey=" + API_KEY)
        Call<ApiResponse> getCurrencyData();
    }
}