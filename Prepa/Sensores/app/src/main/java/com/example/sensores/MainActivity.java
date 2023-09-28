package com.example.sensores;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor sensor;
    SensorEventListener sensorEventListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(sensor.TYPE_PROXIMITY);

        if(sensor==null){
            Context context= getApplicationContext();
            CharSequence text = "Tu telefono no tiene el sensor.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

            finish();
        }

        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                if(event.values[0]<sensor.getMaximumRange())
                {
                    getWindow().getDecorView().setBackgroundColor(Color.RED);
                }
                else
                {
                    getWindow().getDecorView().setBackgroundColor(Color.GREEN);
                }
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
        Start();
    }
    public void Start()
    {
        sensorManager.registerListener(sensorEventListener, sensor, 2000*1000);
    }

    public void Stop()
    {
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause() {
        Stop();
        super.onPause();
    }

    @Override
    protected void onResume() {
        Start();
        super.onResume();
    }
}