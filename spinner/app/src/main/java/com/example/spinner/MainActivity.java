package com.example.spinner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.spinner.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    ActivityMainBinding binding;
    // Spinner animales1, animales2;
    String[] opciones = {"Oso","Tigre","Camello","Burro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ArrayAdapter<String> aa = new ArrayAdapter<>(
                MainActivity.this,
                android.R.layout.simple_dropdown_item_1line,
                opciones);
        binding.spanim1.setAdapter(aa);
        binding.spanim1.setOnItemSelectedListener(this);
        binding.spanim2.setAdapter(aa);
        binding.spanim2.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        int n=0;
        if (adapterView.getId()==R.id.spanim1) n=1;
        if (adapterView.getId()==R.id.spanim1) n=2;
        
        Toast.makeText(
                MainActivity.this,
                "Spinner "+n +". "+ i + " " + opciones[i],
                Toast.LENGTH_LONG
        ).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}