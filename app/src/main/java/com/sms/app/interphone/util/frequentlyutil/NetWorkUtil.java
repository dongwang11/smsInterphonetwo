package com.sms.app.interphone.util.frequentlyutil;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * 网络监测工具类
 */
public class NetWorkUtil {

    /**
     * @return true|false
     */
    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity == null) {
            return false;
        } else {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isNetWorkWifi(Context context) {
        if (isNetworkAvailable(context)) {
            ConnectivityManager conManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conManager.getActiveNetworkInfo().getType() == ConnectivityManager.TYPE_WIFI) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }

    }
}
