package com.example.examen_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String URL_BASE = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "b671cc4ecb7f9a993eef15559b270e24";
    //https://api.openweathermap.org/data/2.5/weather?lat=19.0532&lon=-104.3164&appid=b671cc4ecb7f9a993eef15559b270e24
    TextView txtfecha, txtlugar, txtlat, txtlon, txtcieloynubes, txttempmin, txttempmax,
            txtprepro, txtprelit, txtvievel, txtviegradir;
    Spinner spne, spnm;
    String estado="", municipio="";
    private JSONArray lugaresArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtfecha = (TextView) findViewById(R.id.txtfecha);
        txtlugar = (TextView) findViewById(R.id.txtlugar);
        txtlat = (TextView) findViewById(R.id.txtlat);
        txtlon = (TextView) findViewById(R.id.txtlon);
        txtcieloynubes = (TextView) findViewById(R.id.txtcieloynubes);
        txttempmin = (TextView) findViewById(R.id.txttempmin);
        txttempmax = (TextView) findViewById(R.id.txttempmax);
        txtprepro = (TextView) findViewById(R.id.txtprepor);
        txtprelit = (TextView) findViewById(R.id.txtprelit);
        txtvievel = (TextView) findViewById(R.id.txtvievel);
        txtviegradir = (TextView) findViewById(R.id.txtviegradir);
        spne = (Spinner) findViewById(R.id.spn_estado);
        spnm = findViewById(R.id.spn_municipio);

        try {
            // Cargar el archivo JSON local
            InputStream is = getAssets().open("lugares.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            lugaresArray = new JSONArray(json);
            Log.e("DEBUGXX", "(67) Json Local: " + lugaresArray);

            // Llenar spne con los estados
            Set<String> estadosSet = new HashSet<>();
            for (int i = 0; i < lugaresArray.length(); i++) {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                estadosSet.add(lugar.getString("nes"));
            }
            List<String> estadosList = new ArrayList<>(estadosSet);
            ArrayAdapter<String> adapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadosList);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spne.setAdapter(adapter1);

            // Configurar un listener para spne
            spne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    String selectedEstado = (String) parentView.getItemAtPosition(position);
                    // Llenar spnm con los municipios correspondientes al estado seleccionado
                    fillspnm(selectedEstado);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parentView) {
                    // Manejar el caso cuando no se ha seleccionado ningún estado
                }
            });

            // Establecer los valores predeterminados para spne y spnm
            spne.setSelection(estadosList.indexOf("Colima")); // Predeterminado: Colima
            fillspnm("Colima"); // Llena automáticamente spnm con los municipios de Colima

        } catch (Exception e) {
            e.printStackTrace();
        }

        // Configurar un listener para spnm (para obtener latitud y longitud)
        spnm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMunicipio = (String) parentView.getItemAtPosition(position);
                // Obtener latitud y longitud del municipio seleccionado
                getLatLongFromMunicipio(selectedMunicipio);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Manejar el caso cuando no se ha seleccionado ningún municipio
            }
        });
    }

    private void fillspnm(String selectedEstado) {
        List<String> municipiosList = new ArrayList<>();
        for (int i = 0; i < lugaresArray.length(); i++) {
            try {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                if (lugar.getString("nes").equals(selectedEstado)) {
                    municipiosList.add(lugar.getString("nmun"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, municipiosList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnm.setAdapter(adapter2);
    }

    private void getLatLongFromMunicipio(String selectedMunicipio) {
        try {
            for (int i = 0; i < lugaresArray.length(); i++) {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                if (lugar.getString("nmun").equals(selectedMunicipio)) {
                    String latitud = lugar.getString("lat");
                    String longitud = lugar.getString("lon");
                    // Ahora tienes la latitud y longitud, puedes realizar la solicitud a la API
                    getWeatherData(latitud, longitud);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getWeatherData(String latitud, String longitud) {
        // Configurar la URL de la API de OpenWeatherMap (reemplaza "tu_clave_de_api" por tu clave de API)
        String url = URL_BASE + "weather?lat=" + latitud + "&lon=" + longitud + "&appid=" + API_KEY;

        // Crear una instancia de RequestQueue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        // Realizar una solicitud GET a la API de OpenWeatherMap
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // Procesar la respuesta JSON de la API (datos climáticos)
                        try {
                            JSONObject coord = response.getJSONObject("coord");
                            double latitud = coord.getDouble("lat");
                            double longitud = coord.getDouble("lon");

                            JSONArray weatherArray = response.getJSONArray("weather");
                            JSONObject weather = weatherArray.getJSONObject(0);
                            String descripcionClima = weather.getString("description");

                            JSONObject main = response.getJSONObject("main");
                            double temperatura = main.getDouble("temp");
                            double temperaturaMinima = main.getDouble("temp_min");
                            double temperaturaMaxima = main.getDouble("temp_max");
                            int presion = main.getInt("pressure");
                            int humedad = main.getInt("humidity");

                            int visibilidad = response.getInt("visibility");

                            JSONObject wind = response.getJSONObject("wind");
                            double velocidadViento = wind.getDouble("speed");
                            int direccionViento = wind.getInt("deg");

                            JSONObject clouds = response.getJSONObject("clouds");
                            int porcentajeNubosidad = clouds.getInt("all");

                            // Mostrar los datos en los TextView
                            txtlugar.setText(response.getString("name"));
                            txtlat.setText("Latitud: " + latitud);
                            txtlon.setText("Longitud: " + longitud);
                            txtcieloynubes.setText("Clima: " + descripcionClima);
                            txttempmin.setText("Temp. Mínima: " + temperaturaMinima + "°C");
                            txttempmax.setText("Temp. Máxima: " + temperaturaMaxima + "°C");
                            txtprepro.setText("Nubosidad: " + porcentajeNubosidad + "%");
                            txtprelit.setText("Presión: " + presion + " hPa");
                            txtvievel.setText("Viento: " + velocidadViento + " m/s");
                            txtviegradir.setText("Dirección del viento: " + direccionViento + "°");

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Manejar errores de la solicitud a la API
                        error.printStackTrace();
                    }
                }
        );

        // Agregar la solicitud a la cola de solicitudes
        requestQueue.add(request);
    }
    //https://api.openweathermap.org/data/2.5/lat=18.9371&lon=-103.9650&appid=b671cc4ecb7f9a993eef15559b270e24
    //https://api.openweathermap.org/data/2.5/weather?lat=19.0532&lon=-104.3164&appid=b671cc4ecb7f9a993eef15559b270e24
}