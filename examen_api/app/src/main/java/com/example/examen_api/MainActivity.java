package com.example.examen_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String URL_BASE = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "b671cc4ecb7f9a993eef15559b270e24";
    //https://api.openweathermap.org/data/2.5/weather?lat=19.0532&lon=-104.3164&lang=es&appid=b671cc4ecb7f9a993eef15559b270e24
    //https://api.openweathermap.org/data/2.5/forecast?lat=19.0532&lon=-104.3164&lang=es&appid=b671cc4ecb7f9a993eef15559b270e24
    TextView txtfecha, txtlugar, txtlat, txtlon, txtcieloynubes, txttempmin, txttempmax,
            txtprepro, txtprelit, txtvievel, txtviegradir;
    Spinner spne, spnm;
    String estado="", municipio="", latitud="", longitud="";
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
            // Cargo el JSON local con estados y municipios de México para que me den latitud y longitud
            // JSON descargado de https://smn.conagua.gob.mx/es/web-service-api 
                // (ya no sirve como API, solo se descarga un comprimido .gz con el JSON)
            InputStream is = getAssets().open("lugares.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String json = new String(buffer, "UTF-8");
            lugaresArray = new JSONArray(json);
            Log.d("DEBUGXX", "(69) Json Local: " + lugaresArray);

            // Uso el Set para no repetir elementos
            Set<String> estadosSet = new HashSet<>();
            for (int i = 0; i < lugaresArray.length(); i++) {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                estadosSet.add(lugar.getString("nes"));
            }
            // Lleno Spinner de Estados con los nombres de estados
            List<String> estadosList = new ArrayList<>(estadosSet);
            Collections.sort(estadosList);
            Log.d("DEBUGXX", "(80) Lista de los estados: " + estadosList);
            ArrayAdapter<String> adapterEstados = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, estadosList);
            adapterEstados.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spne.setAdapter(adapterEstados);

            // Listener que dependiendo del estado seleccionado, colocar sus respectivos municipios
            spne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int posicion, long id) {
                    estado = (String) parentView.getItemAtPosition(posicion);
                    // Llenar spnm con los municipios correspondientes al estado seleccionado
                    llenarMunicipios(estado);
                }
                @Override public void onNothingSelected(AdapterView<?> parentView){}
            });

            // Valores predeterminados
            spne.setSelection(estadosList.indexOf("Colima"));
            llenarMunicipios("Colima");
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Ya con el municipio seleccionado puedo obtener la latitud y la longitud de mi JSON local
        spnm.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                municipio = (String) parentView.getItemAtPosition(position);
                // Traigo la latitud y la longitud del municipio seleccionado (con JSON local)
                obtenerLatitudLongitud(municipio);
            }
            @Override public void onNothingSelected(AdapterView<?> parentView) {}
        });
    }

    private void llenarMunicipios(String estado) {
        List<String> municipiosList = new ArrayList<>();
        for (int i = 0; i < lugaresArray.length(); i++) {
            try {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                if (lugar.getString("nes").equals(estado)) {
                    municipiosList.add(lugar.getString("nmun"));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Collections.sort(municipiosList);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, municipiosList);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnm.setAdapter(adapter2);
    }

    private void obtenerLatitudLongitud(String municipio) {
        try {
            for (int i = 0; i < lugaresArray.length(); i++) {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                // Si en el JSON 'lugar' encuentra el estado y el municipio entoncesss...
                if (lugar.getString("nes").equals(estado) && lugar.getString("nmun").equals(municipio)) {
                    latitud = lugar.getString("lat");
                    longitud = lugar.getString("lon");
                    // Con estos datos puedo acceder a la API
                    datosOpenWeatherApi(latitud, longitud);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void datosOpenWeatherApi(String latitud, String longitud) {
        String url = URL_BASE + "forecast?lat=" + latitud + "&lon=" + longitud + "&lang=es&appid=" + API_KEY + "&units=metric";
        Log.d("DEBUGXX", "(152) Url final: " + url);
        
        // Nueva instancia de RequestQueue (cola de solicitud)
        RequestQueue solicitudCola = Volley.newRequestQueue(this);
        
        // Realizo una solicitud GET a la API de OpenWeatherMap (es plan gratuito jeje)
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null, response -> {
                    // Traigo datos de la API a distintas variables
                    try {
                        JSONArray weatherArray = response.getJSONArray("weather");
                        JSONObject clima = weatherArray.getJSONObject(0);
                        String descripcion = clima.getString("description");

                        JSONObject main = response.getJSONObject("main");
                        double temperaturaMinima = main.getDouble("temp_min");
                        double temperaturaMaxima = main.getDouble("temp_max");
                        int presion = main.getInt("pressure");

                        JSONObject viento = response.getJSONObject("wind");
                        double velocidad = viento.getDouble("speed");
                        int direccion = viento.getInt("deg");

                        JSONObject clouds = response.getJSONObject("clouds");
                        int nubosidad = clouds.getInt("all");

                        double pop = response.getInt("pop");

                        // Rellenar TextView
                        txtlugar.setText(response.getString(municipio +", "+ estado +"."));
                        txtlat.setText("Latitud: " + latitud);
                        txtlon.setText("Longitud: " + longitud);
                        txtcieloynubes.setText("Cielo "+descripcion+" - cobertura de nubes del "+nubosidad+"%");
                        txttempmin.setText("Mínima: " + temperaturaMinima + " °C");
                        txttempmax.setText("Máxima: " + temperaturaMaxima + " °C");
                        txtprepro.setText("Probabilidad del lluvia del " + pop + "%");
                        txtprelit.setText("Presión: " + presion + " hPa");
                        txtvievel.setText("Viento: " + velocidad + " m/s");
                        txtviegradir.setText("Dirección del viento: " + direccion + "°");

                    } catch (Exception e) {
                        e.printStackTrace();
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
        solicitudCola.add(request);
    }
}