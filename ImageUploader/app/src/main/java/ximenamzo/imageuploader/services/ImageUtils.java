package ximenamzo.imageuploader.services;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

/* ImageUtils.java */
public class ImageUtils {

    public static File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!Objects.requireNonNull(storageDir).exists()) {
            storageDir.mkdirs();
        }

        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }
}