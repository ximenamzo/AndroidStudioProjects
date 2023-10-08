package com.example.cambio_moneda;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

// Para la API
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

// implementación de clase View y evento OnClickListener
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // API
    private static final String API_BASE_URL = "https://api.currencyapi.com/v3/";
    private static final String API_KEY = "cur_live_6ZNhaifAhEyKuZPIce9EA43LDjIrKqNKEepmIlYw";

    EditText etx1, etx2;
    Spinner sp1, sp2;
    Button btn;
    List<String> monedas;

    private class ConversionCompletedListener implements OnConversionCompleted {
        @Override
        public void onConversionResult(double result) {
            // Este método se llamará cuando la conversión se complete
            etx2.setText(String.valueOf(result));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(this);

        etx1 = (EditText) findViewById(R.id.edit1);
        etx2 = (EditText) findViewById(R.id.edit2);

        sp1 = (Spinner) findViewById(R.id.spinner1);
        sp2 = (Spinner) findViewById(R.id.spinner2);

        monedas = new ArrayList<>();
        new FetchCambio().execute();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {
            convertirMoneda();
        }
    }

    private void convertirMoneda() {
        String cantidadString = etx1.getText().toString();

        if (cantidadString.isEmpty()) {
            Log.d("DEBUG", "Cantidad vacía");
            Toast.makeText(this, "Ingresa una cantidad válida", Toast.LENGTH_SHORT).show();
            return;
        }

        double cantidad = Double.parseDouble(cantidadString);
        String monedaOrigen = sp1.getSelectedItem().toString();
        String monedaDestino = sp2.getSelectedItem().toString();

        Log.d("DEBUG", "Moneda de origen: " + monedaOrigen);
        Log.d("DEBUG", "Moneda de destino: " + monedaDestino);

        //double tasaCambio = obtenerTasaDeCambio(monedaOrigen, monedaDestino, cantidad);
        //double resultado = cantidad * tasaCambio;
        //etx2.setText(String.valueOf(resultado));
        obtenerTasaDeCambio(monedaOrigen, monedaDestino, cantidad);
    }

    private void obtenerTasaDeCambio(String monedaOrigen, String monedaDestino, double cantidad) {
        new FetchConversion(cantidad, new ConversionCompletedListener()).execute(monedaOrigen, monedaDestino);
    }

    private class FetchConversion extends AsyncTask<String, Void, Double> {
        private double cantidad;
        private OnConversionCompleted listener;

        // Constructor que recibe la cantidad
        public FetchConversion(double cantidad, OnConversionCompleted listener) {
            this.cantidad = cantidad;
            this.listener = listener;
        }
        @Override
        protected Double doInBackground(String... monedas) {
            String monedaOrigen = monedas[0];
            String monedaDestino = monedas[1];

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            CurrencyService currencyService = retrofit.create(CurrencyService.class);
            Call<ConversionResult> call = currencyService.convertCurrency(API_KEY, monedaOrigen, monedaDestino, cantidad);

            try {
                retrofit2.Response<ConversionResult> response = call.execute();
                if (response.isSuccessful()) {
                    ConversionResult conversionResult = response.body();
                    if (conversionResult != null) {
                        Map<String, Currency> data = conversionResult.getData();
                        if (data != null && data.containsKey(monedaDestino)) {
                            Currency currency = data.get(monedaDestino);
                            double tasaCambio = currency.getValue();
                            return cantidad * tasaCambio;
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0.0;
        }

        @Override
        protected void onPostExecute(Double resultado) {
            if (resultado != 0.0) {
                listener.onConversionResult(resultado);
                etx2.setText(String.valueOf(resultado));
            } else {
                Toast.makeText(MainActivity.this, "Error en la conversión", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class FetchCambio extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected List<String> doInBackground(Void... voids) {
            List<String> monedas = new ArrayList<>();
            try {
                // Solicitud HTTP para obtener lista de monedas
                URL url = new URL(API_BASE_URL + "latest?apikey=" + API_KEY);
                Log.d("API_REQUEST", "Solicitud a la API: " + url.toString());
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");

                int respuesta = connection.getResponseCode();
                Log.d("API_RESPONSE", "Código de respuesta HTTP: " + respuesta);
                if (respuesta == HttpURLConnection.HTTP_OK) {
                    Reader reader = new InputStreamReader(connection.getInputStream());
                    StringBuilder response = new StringBuilder();
                    int charRead;
                    char[] inputBuffer = new char[1024];

                    while ((charRead = reader.read(inputBuffer)) != -1) {
                        response.append(inputBuffer, 0, charRead);
                    }

                    Log.d("API_RESPONSE", "Contenido de la respuesta JSON: " + response.toString());

                    // Procesamiento de respuesta JSON para obtener la lista de las monedas
                    JSONObject jsonResponse = new JSONObject(response.toString());
                    JSONObject dataObject = jsonResponse.getJSONObject("data");
                    //Log.d("API_RESPONSE", "Datos de tasas de cambio: " + dataObject.toString());

                    // Verifica si "ratesObject" contiene los datos esperados
                    if (dataObject == null) {
                        // Muestra un mensaje de error en el hilo principal
                        runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error al analizar los datos JSON", Toast.LENGTH_LONG).show());
                        return null;
                    } else {
                        // Procesa los datos y llena
                        Iterator<String> keys = dataObject.keys();
                        while (keys.hasNext()) {
                            String codigoMoneda = keys.next();
                            JSONObject monedaObject = dataObject.getJSONObject(codigoMoneda);
                            String nombreMoneda = monedaObject.getString("code");
                            monedas.add(nombreMoneda);
                        }
                    }
                } else {
                    Log.e("API_RESPONSE", "Error en la llamada a la API, código: " + respuesta);
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error en la llamada a la API", Toast.LENGTH_LONG).show());
                    return null;
                }

            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            return monedas;
        }

        @Override
        protected void onPostExecute(List<String> monedas) {
            if (monedas != null && !monedas.isEmpty()) {
                // Llenar los Spinners con las monedas disponibles en la lista
                ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                        MainActivity.this, android.R.layout.simple_spinner_item, monedas);
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                sp1.setAdapter(spinnerAdapter);
                sp2.setAdapter(spinnerAdapter);

                // Peso mexicano como moneda de origen y dolar estadounidense como moneda de destino
                sp1.setSelection(spinnerAdapter.getPosition("MXN"));
                sp2.setSelection(spinnerAdapter.getPosition("USD"));

            } else {
                Log.d("DEBUG", "Error datos spinner: ratesMap es nulo o vacío");
                runOnUiThread(() -> Toast.makeText(MainActivity.this, "Error datos spinner", Toast.LENGTH_LONG).show());
            }
        }
    }

    interface CurrencyService {
        @GET("convert")
        Call<ConversionResult> convertCurrency(
                @Query("apiKey") String apiKey,
                @Query("from") String fromCurrency,
                @Query("to") String toCurrency,
                @Query("amount") double amount
        );
    }

    public class ConversionResult {
        @SerializedName("data")
        private Map<String, Currency> data;

        public Map<String, Currency> getData() {
            return data;
        }
    }

    public class Currency {
        @SerializedName("value")
        private double value;

        public double getValue() {
            return value;
        }
    }
}

interface OnConversionCompleted {
    void onConversionResult(double result);
}