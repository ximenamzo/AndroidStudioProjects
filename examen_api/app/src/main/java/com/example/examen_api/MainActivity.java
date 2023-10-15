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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String URL_BASE = "https://api.openweathermap.org/data/2.5/";
    private static final String API_KEY = "b671cc4ecb7f9a993eef15559b270e24";
    //http://api.openweathermap.org/data/2.5/forecast?lat=19.0532&lon=-104.3164&cnt=3&lang=es&appid=b671cc4ecb7f9a993eef15559b270e24
    TextView txtfecha, txtlugar, txtlat, txtlon, txtcielo, txttemp, txtsensacion, txttempmin, txttempmax,
            txtprepor, txtprelit, txtnub, txthum, txtvievel, txtviegradir;
    Spinner spne, spnm;
    String estado="", municipio="", latitud="", longitud="";
    private JSONArray lugaresArray;

    List<String> municipiosList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtfecha = (TextView) findViewById(R.id.txtfecha);
        txtlugar = (TextView) findViewById(R.id.txtlugar);
        txtlat = (TextView) findViewById(R.id.txtlat);
        txtlon = (TextView) findViewById(R.id.txtlon);
        txtcielo = (TextView) findViewById(R.id.txtcielo);
        txttemp = (TextView) findViewById(R.id.txttemp);
        txtsensacion = (TextView) findViewById(R.id.txtsensacion);
        txttempmin = (TextView) findViewById(R.id.txttempmin);
        txttempmax = (TextView) findViewById(R.id.txttempmax);
        txtprepor = (TextView) findViewById(R.id.txtprepor);
        txtprelit = (TextView) findViewById(R.id.txtprelit);
        txtvievel = (TextView) findViewById(R.id.txtvievel);
        txtviegradir = (TextView) findViewById(R.id.txtviegradir);
        txtnub = (TextView) findViewById(R.id.txtnub);
        txthum = (TextView) findViewById(R.id.txthum);
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
            Log.d("DEBUGXX", "(78) Json Local: " + lugaresArray);

            // Uso el Set para no repetir elementos
            Set<String> estadosSet = new HashSet<>();
            for (int i = 0; i < lugaresArray.length(); i++) {
                JSONObject lugar = lugaresArray.getJSONObject(i);
                estadosSet.add(lugar.getString("nes"));
            }
            // Lleno Spinner de Estados con los nombres de estados
            List<String> estadosList = new ArrayList<>(estadosSet);
            Collections.sort(estadosList);
            Log.d("DEBUGXX", "(89) Lista de los estados: " + estadosList);
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
            Log.d("DEBUGXX", "(108) Municipios disponibles de inicio: " + municipiosList);
            Log.d("DEBUGXX", "(109) Index de Manzanillo: " + municipiosList.indexOf("Manzanillo"));
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
        if (!municipiosList.isEmpty()) municipiosList.clear();
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
        if (estado.contains("Colima")) spnm.setSelection(municipiosList.indexOf("Manzanillo"));
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
        String url = URL_BASE + "forecast?lat=" + latitud + "&lon=" + longitud + "&cnt=8&lang=es&appid=" + API_KEY + "&units=metric";
        //http://api.openweathermap.org/data/2.5/forecast?lat=19.0532&lon=-104.3164&cnt=3&lang=es&appid=b671cc4ecb7f9a993eef15559b270e24&units=metric
        Log.d("DEBUGXX", "(153) Url final: " + url);
        
        // Nueva instancia de RequestQueue (cola de solicitud)
        RequestQueue solicitudCola = Volley.newRequestQueue(this);
        
        // Realizo una solicitud GET a la API de OpenWeatherMap (es plan gratuito jeje)
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url, null, response -> {
                    // Traigo datos de la API a distintas variables
                    try {
                        JSONObject list = response.getJSONArray("list").getJSONObject(0);

                        JSONArray weatherArray = list.getJSONArray("weather");
                        JSONObject clima = weatherArray.getJSONObject(0);
                        String descripcion = clima.getString("description");

                        JSONObject main = list.getJSONObject("main");

                        double temperatura = main.getDouble("temp");
                        double sensacion = main.getDouble("feels_like");

                        // Temperatura mínima de "ahorita"
                        double temperaturaMinima = main.getDouble("temp_min");
                        double tempMin = 0.0;
                        // Buscando la temperatura mínima de las siguientes 24 horas
                        for (int i=0; i<8; i++){
                            list = response.getJSONArray("list").getJSONObject(i);
                            main = list.getJSONObject("main");
                            tempMin = main.getDouble("temp_min");
                            if(tempMin<temperaturaMinima) temperaturaMinima = tempMin;
                            Log.d("DEBUGXX", "(188) Iteración " + (i+1)*3 +". Temp Mínima: " + temperaturaMinima + " °C");
                        }

                        // Temperatura máxima de "ahorita"
                        double temperaturaMaxima = main.getDouble("temp_max");
                        double tempMax = 0.0;
                        // Buscando la temperatura máxima de las siguientes 24 horas
                        for (int i=0; i<8; i++){
                            list = response.getJSONArray("list").getJSONObject(i);
                            main = list.getJSONObject("main");
                            tempMax = main.getDouble("temp_max");
                            if(tempMax>temperaturaMaxima) temperaturaMaxima = tempMax;
                            Log.d("DEBUGXX", "(200) Iteración " + (i+1)*3 +". Temperatura Máxima: " + temperaturaMaxima + " °C");
                        }

                        // Regreso al 0 (hoy)
                        list = response.getJSONArray("list").getJSONObject(0);

                        JSONObject viento = list.getJSONObject("wind");
                        double velocidad = viento.getDouble("speed");
                        int direccion = viento.getInt("deg");

                        JSONObject clouds = list.getJSONObject("clouds");
                        int nubosidad = clouds.getInt("all");

                        double pop = list.getDouble("pop");

                        double preci = 0.0;
                        if (list.has("rain")){
                            JSONObject rain = list.getJSONObject("rain");
                            preci = rain.getDouble("3h");
                        }

                        String date = list.getString("dt_txt");
                        Locale locale = new Locale("es", "ES");
                        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale);
                        SimpleDateFormat desiredFormat = new SimpleDateFormat("EEEE dd 'de' MMMM 'de' yyyy", locale);
                        String fechaHoy = null;
                        try {
                            Date fecha = originalFormat.parse(date);
                            fechaHoy = desiredFormat.format(fecha);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Rellenar TextView
                        txtfecha.setText(fechaHoy + "");
                        txtlugar.setText(municipio +", "+ estado +".");
                        txtlat.setText("Latitud: " + latitud);
                        txtlon.setText("Longitud: " + longitud);
                        txtcielo.setText("Ahora mismo hay "+descripcion+".");

                        txttemp.setText("Temperatura actual: " + temperatura + " °C");
                        txtsensacion.setText("Sensación térmica de " + sensacion + " °C");
                        txttempmin.setText("Mínima: " + temperaturaMinima + " °C");
                        txttempmax.setText("Máxima: " + temperaturaMaxima + " °C");

                        txtprepor.setText("Probabilidad del lluvia del " + pop + "%");
                        txtprelit.setText("Volumen de lluvia: " + preci + "mm (últ. 3hrs)");

                        txtnub.setText("Nubosidad: " + nubosidad + " %");
                        txthum.setText("Humedad: " + velocidad + " %");

                        txtvievel.setText("Velocidad del viento\nde " + velocidad + " m/s");
                        txtviegradir.setText("Dirección del viento\nhacia los " + direccion + "°");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }, error -> error.printStackTrace()
        );

        // Agregar la solicitud a la cola de solicitudes
        solicitudCola.add(request);
    }
}