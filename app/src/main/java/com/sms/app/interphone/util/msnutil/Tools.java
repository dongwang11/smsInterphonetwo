package com.sms.app.interphone.util.msnutil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;


import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.ui.activity.StartActivity;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceConstant;
import com.sms.app.interphone.util.frequentlyutil.SharePreferenceUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class Tools {

    public static Context mContext = null;

    public void onCreate() {
        //super.onCreate();
        mContext = mContext.getApplicationContext();
    }


    public static String getCurrentVersion(Context context) {
        String version = "";
        try {
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();
            PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            version = packageInfo.versionName;
        } catch (Exception e) {
            // TODO: handle exception
            ExceptionUtil.handleException(e);
        }
        return version;
    }

    public static void showInfo(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT)
                .show();
    }

    private static ProgressDialog progressDialog;

    public static void showProgressDialog(final Activity activity, String msg) {

        if (progressDialog == null) {
            progressDialog = new ProgressDialog(activity);
            progressDialog.setMessage(msg);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        ExceptionUtil.handleException(e);
                    }
                    if (progressDialog != null) {
                        progressDialog.cancel();
                        progressDialog = null;

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Tools.showInfo(activity,"????????????????????????~");
                            }
                        });

                    }

                }
            }).start();

        }else{

            closeProgressDialog();

            showProgressDialog(activity,msg);

        }
    }

    public static void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.cancel();
            progressDialog = null;
        }
    }

    public static byte[] readFileFromSdcard(File file) {
        FileInputStream fileInputStream = null;
        byte[] fileData = null;
        try {
            fileInputStream = new FileInputStream(file);
            int fileSize = fileInputStream.available();
            fileData = new byte[fileSize];
            fileInputStream.read(fileData);
        } catch (Exception e) {
            // TODO: handle exception
            ExceptionUtil.handleException(e);
        } finally {
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    ExceptionUtil.handleException(e);
                }
            }
        }
        return fileData;
    }

    public static String writeToSdcard(Context context, String groupName, String fileName, byte[] data) throws IOException {

        String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+groupName+"/sms_icon/";

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

            File filePath = new File(path);
            if (!filePath.exists()) {

                filePath.mkdirs();
            }

            File file = new File(path, fileName+".png");

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            LogUtil.i(GlobalConsts.TAG,"bitmap:"+bitmap);

            if(bitmap != null){

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);

            }


            fileOutputStream.flush();

            fileOutputStream.close();

            return file.getAbsolutePath();

        }

        return null;

    }


    public static String writeImgSdcard(Context context, String fileName, byte[] data) throws IOException {

        String path =context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/sms_img/";

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

            File filePath = new File(path);
            if (!filePath.exists()) {

                filePath.mkdirs();
            }

            File file = new File(path, fileName+".png");

            FileOutputStream fileOutputStream = new FileOutputStream(file);

            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

            LogUtil.i(GlobalConsts.TAG,"bitmap:"+bitmap);

            if(bitmap != null){

                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,fileOutputStream);

            }


            fileOutputStream.flush();

            fileOutputStream.close();

            return file.getAbsolutePath();

        }

        return null;

    }

    public static String writeFirmwareSdcard(Context context, String fileName, byte[] data) throws IOException {

        String path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/sms_firmware/";

        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

            File filePath = new File(path);
            if (!filePath.exists()) {

                filePath.mkdirs();
            }

            File file = new File(path, fileName);

            if (file.exists()) {
                file.delete();
            }

            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(data, 0, data.length);
            fileOutputStream.flush();
            fileOutputStream.close();

            return file.getAbsolutePath();

        }

        return null;

    }

    public static final String SPECIAL_CHAR = "[`~!@#$%^&*+=|{}',:;\"[url=file://\\[\\].<]\\[\\].<>/[/url]??????????????????????????????????????????????????????????????????????????????????????????????????????]";

    public static String StringFilter(String srcString) throws PatternSyntaxException {

        return Pattern.compile(SPECIAL_CHAR).matcher(srcString).replaceAll("").replaceAll("[url=]\\\\[/url]", "").trim();
    }

    public static boolean existSpecialChar(String srcString, char[] specialChar){
        for (Character c : specialChar) {
            if(srcString.contains(c.toString())){
                return true;
            }
        }
        return false;
    }

    /**
     * ????????????????????????
     *
     * */

    public static boolean note_Intent(Context context) {
        ConnectivityManager con = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = con.getActiveNetworkInfo();
        if (networkinfo == null || !networkinfo.isAvailable()) {
            // ?????????????????????
            Toast.makeText(context.getApplicationContext(), "????????????Internet???",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        boolean wifi = con.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .isConnectedOrConnecting();
        if (!wifi) { // ????????????wifi
            Toast.makeText(context.getApplicationContext(), "???????????????WIFI??????????????????",
                    Toast.LENGTH_SHORT).show();
        }
        return true;
    }





    public static String getGroupName(String name) {

        //
        try {

            String aName = name.substring(0,14);

            String bName = name.substring(14,name.length());


            byte[] datas= aName.getBytes("ISO-8859-1");

            byte[] one = {datas[0],datas[1],datas[2],datas[3],datas[4],datas[5]};

            byte[] to = {datas[6],datas[7],datas[8],datas[9],datas[10],datas[11],datas[12],datas[13]};

            long names = bytesToLong(to);

            try {

                byte[] hs = bName.getBytes();

                int k = 0;

                for(int i = 0;i < hs.length;i++){

                    if(hs[i] == 0){

                        k = i;

                        break;

                    }

                }

                if(k > 0){

                    byte[] yhs = new byte[k];

                    System.arraycopy(hs, 0,yhs, 0,k);

                    bName = new String(yhs);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            try {

                if(MyApplicatoin.interphone.getImCode() != null){
                    MyApplicatoin.interphone.getImCode().setHite(bName);//   bName
                    //Log.d("resultString","  bName3 "+bName+"  "+MyApplicatoin.interphone.getImCode().getName());

                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            /*LogUtil.i(GlobalConsts.TAG,"?????????"+names);
            LogUtil.i(GlobalConsts.TAG,"?????????"+Arrays.toString(bName.getBytes()));*/

            return String.valueOf(names);

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

        return null;
    }


    public static String getGroupHiteName(String name) {
        //
        try {

            String aName = name.substring(0,14);

            String bName = name.substring(14,name.length());

            byte[] datas= aName.getBytes("ISO-8859-1");

            byte[] one = {datas[0],datas[1],datas[2],datas[3],datas[4],datas[5]};

            byte[] to = {datas[6],datas[7],datas[8],datas[9],datas[10],datas[11],datas[12],datas[13]};

            long names = bytesToLong(to);

            MyApplicatoin.interphone.getImCode().setHite(bName);

            /*LogUtil.i(GlobalConsts.TAG,"?????????"+names);
            LogUtil.i(GlobalConsts.TAG,"?????????"+Arrays.toString(bName.getBytes()));*/

            return String.valueOf(bName);

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

        return null;
    }
    public static byte[] getDataCode(String name) {

        try {
            byte[] datas= name.getBytes("ISO-8859-1");

            byte[] one = {datas[0],datas[1],datas[2],datas[3],datas[4],datas[5]};

            byte[] to = {datas[6],datas[7],datas[8],datas[9],datas[10],datas[11],datas[12],datas[13]};

            //    LogUtil.i(GlobalConsts.TAG,"data:"+ Arrays.toString(datas)+",name:"+name+",code"+code);

            return one;

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

        return null;
    }


    //byte ????????? long ???????????????

    public  static byte[] longToBytes(long number) {
        long temp = number;
        byte[] b = new byte[8];
        for (int i = 0; i < b.length; i++) {
            // ?????????????????????????????? temp = temp
            b[i] = new Long(temp & 0xff).byteValue();

            temp = temp >> 8;

        }
        return b;
    }

    public static long bytesToLong(byte[] b) {

        long s = 0;
        long s0 = b[0] & 0xff;// ?????????
        long s1 = b[1] & 0xff;
        long s2 = b[2] & 0xff;
        long s3 = b[3] & 0xff;
        long s4 = b[4] & 0xff;// ?????????
        long s5 = b[5] & 0xff;
        long s6 = b[6] & 0xff;
        long s7 = b[7] & 0xff;

        // s0??????
        s1 <<= 8;
        s2 <<= 16;
        s3 <<= 24;
        s4 <<= 8 * 4;
        s5 <<= 8 * 5;
        s6 <<= 8 * 6;
        s7 <<= 8 * 7;
        s = s0 | s1 | s2 | s3 | s4 | s5 | s6 | s7;
        return s;

    }


}
