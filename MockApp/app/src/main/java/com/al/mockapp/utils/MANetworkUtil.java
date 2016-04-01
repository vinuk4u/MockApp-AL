package com.al.mockapp.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by vineeth on 31/03/16
 */
public class MANetworkUtil {

    public static boolean hasInternetAccess(Context context) {
        try {
            boolean hasInternet = false;

            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = cm.getActiveNetworkInfo();
            NetworkInfo wifi = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            NetworkInfo mobile = cm
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if ((wifi.isAvailable() || mobile.isAvailable()) && networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                hasInternet = true;
            }

            return hasInternet;
        } catch (Exception e) {
            return false;
        }
    }

}
