package com.sms.app.interphone.util.msnutil;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;

import com.sms.app.interphone.ui.MyApplicatoin;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2018/8/30.
 */

public class PhoneManager {

    /**
     * 获取当前手机系统语言。
     *
     * @return 返回当前系统语言。例如：当前设置的是“中文-中国”，则返回“zh-CN”
     */
    public static String getSystemLanguage() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * 获取当前系统上的语言列表(Locale列表)
     *
     * @return  语言列表
     */
    public static Locale[] getSystemLanguageList() {
        return Locale.getAvailableLocales();
    }

    /**
     * 获取当前手机系统版本号
     *
     * @return  系统版本号
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }

    /**
     * 获取当前时间
     *
     * @return  时间
     */
    public static String getSystemTime() {

        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        return sdf.format(date);
    }


    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }


    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getPhoneManager() {
        String phoneManager = null;

        try {
            phoneManager = "手机厂商: "+getDeviceBrand()+"\n"
                          +"手机型号: "+getSystemModel()+"\n"
                          +"手机系统语言: "+getSystemLanguage()+"\n"
                          +"Android系统版本号: "+getSystemVersion()+"\n"
                          +"时间: "+getSystemTime()+"\n"
                          +"APK版本: "+MyApplicatoin.versionCode;

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }


        return phoneManager;
    }

    /**
     * 获取手机IMEI(需要“android.permission.READ_PHONE_STATE”权限)
     *
     * @return  手机IMEI
     */
    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Activity.TELEPHONY_SERVICE);
        if (tm != null) {
            return tm.getDeviceId();
        }
        return null;
    }

}
