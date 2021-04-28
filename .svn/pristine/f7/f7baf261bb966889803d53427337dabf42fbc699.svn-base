package com.sms.app.interphone.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.sms.app.interphone.ui.MyApplicatoin;


/**
 *
 * 监听网络状态广播接收器
 *
 * */
public class NetworkStateChangedMsnReceiver extends BroadcastReceiver {

    private static int netWorkState = 0;

    private static final String TAG = "NetReceiver";

    private static String hite = "NetReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {


        try {


            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {


                ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo activeNetworkInfo = manager.getActiveNetworkInfo();


                if (activeNetworkInfo == null) {

                    netWorkState = 0;
                    hite = "无网络";

                } else {
                    // 有网，判断是wifi还是mobile
                    NetworkInfo wifiNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (wifiNetworkInfo != null && wifiNetworkInfo.isConnected() == true) {
                        hite = "WIFI";
                        netWorkState = 1;
                    }

                    NetworkInfo mobileNetworkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                    if (mobileNetworkInfo != null && mobileNetworkInfo.isConnected() == true) {
                        hite = "流量";
                        netWorkState = 2;
                    }
                }
                Log.e(TAG, "网络：" + hite);


                MyApplicatoin.showMessage(context,hite,netWorkState);

            }



        }catch(Exception e){

            e.printStackTrace();

        }




    }
}