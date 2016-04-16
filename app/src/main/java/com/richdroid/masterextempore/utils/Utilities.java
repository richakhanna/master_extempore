package com.richdroid.masterextempore.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.io.File;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilities {

  private static String PAGE0 = "Category";
  private static String PAGE1 = "Try";
  private static String PAGE2 = "Attempted";

  public static String getPageTitle(Context context, int position) {
    switch (position) {
      case 0:
        return PAGE0;
      case 1:
        return PAGE1;
      case 2:
        return PAGE2;
    }
    return null;
  }

  public static boolean isNetworkAvailable(Context context) {
    ConnectivityManager cm =
        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo info = cm.getActiveNetworkInfo();
    if (info == null) return false;
    NetworkInfo.State network = info.getState();
    return (network == NetworkInfo.State.CONNECTED || network == NetworkInfo.State.CONNECTING);
  }

  public static String getApplicationDirectory(Context context) {
    boolean isSdPresent = android.os.Environment.getExternalStorageState()
        .equals(android.os.Environment.MEDIA_MOUNTED);
    if (isSdPresent) {

      return context.getExternalFilesDir(null).toString();
    } else {
      return null;
    }
  }

  public static boolean isExternalStorageAvailable() {
    return android.os.Environment.getExternalStorageState()
        .equals(android.os.Environment.MEDIA_MOUNTED);
  }

  public static boolean doesFileExist(String filePath) {
    File file = new File(filePath);
    return file.exists();
  }
}
