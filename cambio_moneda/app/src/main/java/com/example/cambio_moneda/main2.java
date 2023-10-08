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

public class main2 extends AppCompatActivity implements View.OnClickListener{

    private static final String API_BASE_URL = "https://api.currencyapi.com/v3/";
    private static final String API_KEY = "cur_live_6ZNhaifAhEyKuZPIce9EA43LDjIrKqNKEepmIlYw";
    EditText etx1, etx2;
    Spinner spn1, spn2;
    Button btn;
    ApiService apiService;
    ApiResponse apiResponse;

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


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("DEBUG", "Retrofit construido. 52");

        apiService = retrofit.create(ApiService.class);

        Call<ApiResponse> call = apiService.getCurrencyData();
        Log.d("DEBUG", "Llamada conseguida. 57");
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                try{
                    if (response.isSuccessful()) {
                        Log.d("DEBUG", "Respuesta exitosa. 63");
                        apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getData() != null) {
                            Log.d("API_RESPONSE", "70." + apiResponse.getData().toString());
                            // Llenar los spinners con los códigos de moneda
                            Set<String> currencyCodes = apiResponse.getData().keySet();
                            ArrayList<String> currencyList = new ArrayList<>(currencyCodes);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(main2.this,
                                    android.R.layout.simple_spinner_item, currencyList);
                            spn1.setAdapter(adapter);
                            spn2.setAdapter(adapter);
                        } else {
                            Log.d("ERRORX", "Api Response Null. 75");
                        }
                    } else {
                        Toast.makeText(main2.this, "Error al obtener datos de moneda", Toast.LENGTH_SHORT).show();
                        Log.d("DEBUG", "Respuesta sin éxito. 79 Código: " + response.code());
                        try {
                            String errorBody = response.errorBody().string();
                            Log.d("DEBUG", "Cuerpo de respuesta de error (82): " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("DEBUG", "Excepción durante llamada a API (89): " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(main2.this, "Error de red", Toast.LENGTH_SHORT).show();
                Log.e("ERROR", "Error de red durante la llamada a la API (96): " + t.getMessage());
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
                monedaOrigen = apiResponse.getData().get(codigoOrigen);
                monedaDestino = apiResponse.getData().get(codigoDestino);

                if (monedaOrigen != null && monedaDestino != null) {
                    try {
                        double cantidadIngresada = Double.parseDouble(etx1.getText().toString());
                        double valorMonedaOrigen = monedaOrigen.getValue();
                        double valorMonedaDestino = monedaDestino.getValue();

                        double resultado = (cantidadIngresada / valorMonedaOrigen) * valorMonedaDestino;

                        etx2.setText(String.valueOf(resultado));
                        Log.d("SUCCES", "Resultado" + resultado + ". 123");
                    } catch (NumberFormatException e) {
                        Toast.makeText(main2.this, "Ingrese una cantidad válida", Toast.LENGTH_SHORT).show();
                        Log.d("WARNING", "Number Format Exception. 126");
                    }
                }
            } else {
                Log.d("ERRORX", "Api Response Null. 130");
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