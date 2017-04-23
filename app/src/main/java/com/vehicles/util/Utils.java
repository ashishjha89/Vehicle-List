package com.vehicles.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        if (context == null) {
            return false;
        }
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
