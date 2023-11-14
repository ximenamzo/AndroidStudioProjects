package ximenamzo.imageuploader.services;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;

import ximenamzo.imageuploader.MainActivity;

/* ImageUploadTask.java */
/*
Ejemplo para usar en código
    upload.setOnClickListener(view -> {
        if (currentImagePath != null) {
            new ImageUploadTask(MainActivity.this).execute(currentImagePath);
        } else {
            Toast.makeText(this, "No hay imagen para subir", Toast.LENGTH_SHORT).show();
        }
    });
*/
public class ImageUploadTask extends AsyncTask<String, Void, String> {

    private WeakReference<MainActivity> activityReference;

    public ImageUploadTask(MainActivity activity) {
        this.activityReference = new WeakReference<>(activity);
    }

    @Override
    protected String doInBackground(String... params) {
        Log.d("DEBUG_IUT27", "doInBackground: Inicio");
        try {
            Log.d("DEBUG_IUT29", "Entro al try");
            URL url = new URL("https://www.toptal.com/developers/postbin/1699833675873-4651303514838");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");

            try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
                Log.d("DEBUG_IUT37", "Entro al segundo try");
                String imagePath = params[0];
                try (FileInputStream fileInputStream = new FileInputStream(imagePath)) {
                    Log.d("DEBUG_IUT40", "Entro al tercer try");
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                return "Imagen subida exitosamente";
            } else {
                return "Error al subir la imagen. Código de respuesta: " + responseCode;
            }
        } catch (IOException e) {
            Log.e("ImageUploadTask", "Error al subir la imagen", e);
            return "Error al subir la imagen. Detalles: " + e.getMessage();
        }
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("ImageUploadTask", result);
        MainActivity activity = activityReference.get();
        if (activity != null) {
            Toast.makeText(activity, result, Toast.LENGTH_LONG).show();
            Log.d("DEGUB_IUT67", result);
        }
    }
}
