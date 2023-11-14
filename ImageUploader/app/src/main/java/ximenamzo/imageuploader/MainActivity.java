package ximenamzo.imageuploader;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import ximenamzo.imageuploader.services.ImageUploadTask;
import ximenamzo.imageuploader.services.UtilImage;

/* MainActivity.java */
public class MainActivity extends AppCompatActivity {

    /*private static final int REQUEST_IMAGE_GALLERY = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int REQUEST_PERMISSION_CAMERA = 101;
    private static final int REQUEST_PERMISSION_READ_EXTERNAL_STORAGE = 102;
    private static final int REQUEST_PERMISSION_READ_MEDIA_IMAGES = 103; /**/
    public static final int PERMISSIONS_CALLBACK=11;
    public static final int CAMERA_CALLBACK=12;
    public static final int GALLERY_CALLBACK=13;
    private File file;
    private ImageView imageView;
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.baseline_cloud_upload_24);

        Button gallery = findViewById(R.id.btnGaleria);
        Button camera = findViewById(R.id.btnCamara);
        Button upload = findViewById(R.id.btnSubirImg);
        Button clear = findViewById(R.id.limpiar);

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE
        }, PERMISSIONS_CALLBACK);

        gallery.setOnClickListener(view -> {
            if (checkReadExternalStoragePermission()) {
                Intent j = new Intent(Intent.ACTION_GET_CONTENT);
                j.setType("image/*");
                // Para video sería j.setType("video/*");
                startActivityForResult(j, GALLERY_CALLBACK);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PERMISSIONS_CALLBACK);
            }
        });

        camera.setOnClickListener(view -> {
            if (checkCameraPermission() && checkReadExternalStoragePermission()) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //Null para poder que quede en el directorio raiz de la app
                file = new File(getExternalFilesDir(null) + "/photo.png");
                Log.d("DEBUG_MAIN81", "" + file);
                //Uri uri = FileProvider.getUriForFile(this, getPackageName(), file);
                Uri uri = Uri.fromFile(file);
                i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(i, CAMERA_CALLBACK);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                }, PERMISSIONS_CALLBACK);
            }
        });

        upload.setOnClickListener(view -> {
            if (currentImagePath != null) {
                Log.d("DEBUG_MAIN95", "Llamando a subir img");
                new ImageUploadTask(MainActivity.this).execute(currentImagePath);
            } else {
                Toast.makeText(MainActivity.this, "No hay imagen para subir", Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(view -> {
            Log.d("DEBUG_MAIN103", "Imagen Limpia.");
            imageView.setImageResource(R.drawable.baseline_cloud_upload_24);
        });
    }

    private boolean checkCameraPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkReadExternalStoragePermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_CALLBACK) {
            boolean allGranted = true;
            for (int grantResult : grantResults) {
                if (grantResult == PackageManager.PERMISSION_DENIED) {
                    Log.d("DEBUG_MAIN124", "Permiso denegado");
                    allGranted = false;
                    break;
                }
            }
            if (allGranted) {
                Toast.makeText(this, "Todos los permisos concedidos", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Alerta!, no todos los permisos fueron concedidos", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("DEBUG_MAIN141", "onActivityResult - requestCode: " + requestCode + ", resultCode: " + resultCode);

        if (requestCode == CAMERA_CALLBACK && resultCode == RESULT_OK) {
            try {
                Bitmap image = BitmapFactory.decodeFile(file.getPath());
                Bitmap thumbnail = Bitmap.createScaledBitmap(image, image.getWidth()/4, image.getHeight()/4, true);
                imageView.setImageBitmap(thumbnail);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DEBUG_MAIN150", "Error: "+e);
            }
        } else if (requestCode == GALLERY_CALLBACK && resultCode == RESULT_OK) {
            try {
                Uri uri = data.getData();
                Log.d("DEBUG_MAIN155", "Uri: " + uri);
                String path = UtilImage.getPath(this, uri);
                Log.d("DEBUG_MAIN157", "Path: " + path);

                if (path != null) {
                    currentImagePath = path;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        Bitmap image = BitmapFactory.decodeStream(inputStream);
                        imageView.setImageBitmap(image);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("DEBUG_MAIN169", "Error al obtener la ruta del archivo");
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("DEBUG_MAIN173", "Error: "+e);
            }
        }
    }
}