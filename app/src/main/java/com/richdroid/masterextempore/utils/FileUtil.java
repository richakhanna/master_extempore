package com.richdroid.masterextempore.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

public class FileUtil {

    public static File getOutputMediaFile(Context context, String albumName,
                                          String fileName) {
        Log.d("FileUtil", "media mounted : " + checkExternalStorageState(context));
        if (!checkExternalStorageState(context))
            return null;
        // Constructs a new file instance using the specified directory and
        // name.http://developer.android.com/guide/topics/data/data-storage.html
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("FileUtil", "failed to create directory");
            }
        }
        // Constructs a new file instance using the specified path(in this
        // file,the bitmap image will be written)
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator
                + fileName + ".mp4");
        // Log.v("MediaFile Created", mediaFile.getAbsolutePath());
        return mediaFile;
    }

    public static File getOutputMediaFolder(Context context, String albumName) {

        if (!checkExternalStorageState(context))
            return null;
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                albumName);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.d("SnapShopr", "failed to create directory");
            }
        }

        return mediaStorageDir;
    }

    private static boolean checkExternalStorageState(Context context) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Log.d("FileUtil", "Error! No SDCARD Found! ");
            Toast.makeText(context, "Error! No SDCARD Found!",
                    Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

}
