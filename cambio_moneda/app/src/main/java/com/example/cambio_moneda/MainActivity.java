package com.example.cambio_moneda;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
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
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    private static final String API_BASE_URL = "https://api.currencyapi.com/v3/";
    private static final String API_KEY = "cur_live_6ZNhaifAhEyKuZPIce9EA43LDjIrKqNKEepmIlYw";
    EditText etx1, etx2;
    Spinner spn1, spn2;
    ImageButton btn;
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
        etx1.addTextChangedListener(this);
        etx2.addTextChangedListener(this);

        btn = findViewById(R.id.btn);
        btn.setOnClickListener(this);

        // Adaptador de la interfaz Java a llamadas HTTP
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        Log.d("DEBUGX", "(59) Retrofit construido.");

        // Creando interfaz con el adaptador
        apiService = retrofit.create(ApiService.class);

        // Invocación de un método Retrofit que envia una petición a un servidor web y envía una respuesta
        // Cada llamada produce su propia respuesta HTTP
        Call<ApiResponse> call = apiService.getCurrencyData();
        Log.d("DEBUGX", "(62) Llamada conseguida.");

        // Se envía asíncronamente la petición y notifica callback de su respuesta
        // o error al crear la petición o procesar la respuesta
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                try {
                    // Si hay éxito en la respuesta
                    if (response.isSuccessful()) {
                        Log.d("DEBUGX", "(77) Respuesta exitosa.");

                        // Respuesta igual a contenido
                        apiResponse = response.body();
                        if (apiResponse != null && apiResponse.getData() != null) {

                            // Muestro en log toda la respuesta JSON
                            Log.d("API_RESPONSE", "(84)." + apiResponse.getData().toString());

                            // Lleno los Arrays con los códigos "code" del JSON
                            Set<String> codigosDivisa = apiResponse.getData().keySet();
                            ArrayList<String> listaCodigosDivisa = new ArrayList<>(codigosDivisa);
                            ArrayAdapter<String> adapter = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_spinner_item, listaCodigosDivisa);

                            // Lleno los spinner con los arrays
                            spn1.setAdapter(adapter);
                            spn2.setAdapter(adapter);
                            spn1.setSelection(adapter.getPosition("MXN"));
                            spn2.setSelection(adapter.getPosition("USD"));
                        } else {
                            // Si la respuesta de la API está vacía
                            Log.e("ERRORX", "(99) ApiResponse Null - vacía.");
                        }
                    } else {
                        // Si no hay respuesta
                        Toast.makeText(MainActivity.this, "Error al obtener datos de moneda.", Toast.LENGTH_SHORT).show();
                        Log.e("ERRORX", "(104) Respuesta sin éxito. Código: " + response.code());
                        try {
                            // Try para "intentar" traer la descripción del código
                            String errorBody = response.errorBody().string();
                            Log.e("DEBUGX", "(108) Cuerpo de respuesta de error: " + errorBody);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    // Si falla el try de toda la respuesta de llamada a la API
                    e.printStackTrace();
                    Log.e("ERRORX", "(116) Excepción durante llamada a API: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Error de red", Toast.LENGTH_SHORT).show();
                Log.e("ERRORX", "(123) Error de red durante la llamada a la API: " + t.getMessage());
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (etx1.isFocused()) {
            // Si estan escribiendo en etx1 se hace la conversión a etx2
            convertirDivisa(etx1, etx2, spn1, spn2);
        } else if (etx2.isFocused()) {
            // Si estan escribiendo en etx2 ahora se escribe la conversión en etx1
            convertirDivisa(etx2, etx1, spn2, spn1);
        }
    }
    @Override
    public void afterTextChanged(Editable s) {}

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.btn) {
            // Obtengo los códigos actuales
            String code1 = spn1.getSelectedItem().toString();
            String code2 = spn2.getSelectedItem().toString();

            spn1.setSelection(((ArrayAdapter<String>) spn1.getAdapter()).getPosition(code2));
            spn2.setSelection(((ArrayAdapter<String>) spn2.getAdapter()).getPosition(code1));

            // Convertimos
            convertirDivisa(etx1, etx2, spn1, spn2);
        }
    }

    @SuppressLint("StaticFieldLeak")
    private void convertirDivisa(final EditText origen, final EditText destino, final Spinner spOrigen, final Spinner spDestino) {
        try {
            final double cantidadIngresada = Double.parseDouble(origen.getText().toString());

            // Tarea asíncrona para evitar la sobrecarga en el hilo principal
            new AsyncTask<Void, Void, Double>() {
                @Override
                protected Double doInBackground(Void... voids) {
                    String codigoOrigen = spOrigen.getSelectedItem().toString(); // De
                    String codigoDestino = spDestino.getSelectedItem().toString(); // a

                    // Buscamos valores equivalentes a 1 dolar de cada divisa
                    CurrencyData monedaOrigen = apiResponse.getData().get(codigoOrigen);
                    CurrencyData monedaDestino = apiResponse.getData().get(codigoDestino);

                    // Si tenemos tooodos nuestros datos necesarios...
                    if (apiResponse != null && monedaOrigen != null && monedaDestino != null) {
                        // Asigno a vars double lo obtenido de "value" del "data" de json
                        double valorMonedaOrigen = monedaOrigen.getValue();
                        double valorMonedaDestino = monedaDestino.getValue();

                        // Ejemplo: 500 Pesos Mexicanos a Yenes Japoneses
                        // 500 / 18.1762829644 = 25.5083747 * 149.3234579538 = 4,107.6465
                        return (cantidadIngresada / valorMonedaOrigen) * valorMonedaDestino;
                    }

                    // Devuelve null en caso de error
                    return null;
                }

                @Override
                protected void onPostExecute(Double resultado) {
                    if (resultado != null) {
                        destino.setText(String.valueOf(resultado));
                    } else {
                        destino.setText("");
                        Log.d("DEBUG", origen + " vacio.");
                    }
                }
            }.execute();
        } catch (NumberFormatException e) {
            // Logs debug para ver errores pero salen si es vacío entonces lo quito
                // Manejo de excepciones si se ingresa una cantidad no válida
                // Log.w("WARNINGX", "(207) Number Format Exception.");
            Toast.makeText(MainActivity.this, "Ingrese una cantidad para realizar una conversión", Toast.LENGTH_SHORT).show();
        }
    }

    public class ApiResponse {
        // Busco solo lo que esté dentro de "data"
        @SerializedName("data")
        private Map<String, CurrencyData> data;
        public ApiResponse(Map<String, CurrencyData> data) {
            this.data = data;
        }
        public Map<String, CurrencyData> getData() {
            // Retorno el resultado total de "data"
            return data;
        }
    }

    public class CurrencyData {
        // Para regresar código "code" y valor "value"
        private String code; // Ej. MXN
        private double value; // Ej. 18.1762829644

        // Datos de la divisa
        public CurrencyData(String code, double value) {
            this.code = code;
            this.value = value;
        }

        // Retorno el código
        public String getCode() {
            return code;
        }

        // Retorno el valor
        public double getValue() {
            return value;
        }
    }

    // Interfaz
        // Colección de métodos abstractos (sin implementacion)
        // Deben ser implementados por cualquier clase que implemente la interfaz
        // Resumiendo, define un conjunto de operaciones que las clases concretas deben proporcionar
    // Define un contrato para realizar solicitudes HTTP
    public interface ApiService {
        // Anotación de Retrofit para indicar el tipo de solicitud HTTP
        @GET("latest?apikey=" + API_KEY)
        Call<ApiResponse> getCurrencyData();
        // Representa la solicitud HTTP enviada al servidor
           // La respuesta se espera que sea un objeto de tipo ApiResponse
    }
}