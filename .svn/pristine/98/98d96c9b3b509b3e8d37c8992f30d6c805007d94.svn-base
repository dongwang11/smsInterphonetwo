package com.sms.app.interphone.ui.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;

import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.interphone.R;
import com.sms.app.interphone.util.frequentlyutil.MyContextWrapper;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Administrator on 2017/1/13.
 *
 * 初始化Activity，进行相应的初始化
 *
 * 权限申请业务
 *
 */


public class BaseActivity extends Activity {

    public static final String TAG="YanShi...Log - BaseActivity:";


    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

    }


    /**
     *
     * 为子类提供一个权限检查方法
     *
     * */
    public boolean hasPermission(String... permissions){

        for(String permission : permissions){

            if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){

                return false;
            }

            break;
        }
        return true;
    }



    public void requstPermissions(int code, String... permissions){

        ActivityCompat.requestPermissions(this, permissions, code);

    }

    /**
     *
     * 用户选择打开回调
     *
     * */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    //    LogUtil.i(TAG,"grantResults:"+grantResults[0]);


        switch (requestCode){

            case GlobalConsts.CAMERA:

                LogUtil.i(TAG,"相机:"+grantResults[0]);

                if(grantResults[0] == -1){

                    doLocation(grantResults,getResources().getString(R.string.sms_main_hite_icon_photo_camera));

                }else{

                //    LogUtil.i(TAG,"获取相机权限成功");

                }

                break;
            case GlobalConsts.RECORD_AUDIO:

                LogUtil.i(TAG,"麦克风:"+grantResults[0]);


                doLocation(grantResults,getResources().getString(R.string.sms_main_hite_voice));
                /*if(grantResults[0] == -1){

                }*/

                break;
            case GlobalConsts.ACCESS_FINE_LOCATION:

                LogUtil.i(TAG,"定位:"+grantResults[0]);

                if(grantResults[0] == -1){
                    doLocation(grantResults,getResources().getString(R.string.sms_main_hite_location));
                }

                break;
            case GlobalConsts.WRITE_EXTERNAL_STORAGE:

                LogUtil.i(TAG,"存储:"+grantResults[0]);

                if(grantResults[0] == -1){
                    doLocation(grantResults,getResources().getString(R.string.sms_main_hite_storage));
                }

                break;

        }
    }


    /**
     *
     * 定位权限
     *
     * @param grantResults*/
    public void doLocation(int[] grantResults, final String hite) {


        LogUtil.i(TAG,"doLocation"+grantResults);

        if (grantResults.length > 0) {

            if(grantResults[0] == -1){

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Dialog dialog = new AlertDialog.Builder(BaseActivity.this)
                                .setTitle(getResources().getString(R.string.sms_dialog_hite))
                                .setMessage(hite+getResources().getString(R.string.sms_dialog_hite_permission))
                                .setPositiveButton(getResources().getString(R.string.sms_dialog_hite_permission_up),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();

                                            }
                                        })
                                .setNegativeButton(getResources().getString(R.string.sms_dialog_hite_permission_ok),
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                                Intent intent = new Intent();
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                if (Build.VERSION.SDK_INT >= 9) {
                                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                                } else if (Build.VERSION.SDK_INT <= 8) {
                                                    intent.setAction(Intent.ACTION_VIEW);
                                                    intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
                                                    intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
                                                }
                                                startActivity(intent);
                                            }
                                        }).create();
                        dialog.show();
                    }
                });


            }
        }

    }


    /**
     * 安装apk文件
     */
    public void installAPK() {


        try {

            File file = new File(BaseActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"sms.apk");

            setPermission(file.getPath());

            Intent intent = new Intent(Intent.ACTION_VIEW);

            //版本在7.0以上是不能直接通过uri访问的\

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (Build.VERSION.SDK_INT >= 24) {

                // 由于没有在Activity环境下启动Activity,设置下面的标签

                //参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
                Uri apkUri = FileProvider.getUriForFile(BaseActivity.this, "com.example.ys.androidapplication.fileProvider", file);
                //添加这一句表示对目标应用临时授权该Uri所代表的文件

                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                //    intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setDataAndType(apkUri, "application/vnd.android.package-archive");

            } else {
                intent.setDataAndType(Uri.fromFile(file),"application/vnd.android.package-archive");
            }

            startActivity(intent);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 提升读写权限
     * @param filePath 文件路径
     * @return
     * @throws IOException
     */
    public static void setPermission(String filePath)  {

        String[] command = {"chmod", "777", filePath};
        ProcessBuilder builder = new ProcessBuilder(command);
        try {
            builder.start();
        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        }
    }


    public void initEvents() {
        // TODO Auto-generated method stub

    }


    @Override
    protected void attachBaseContext(Context newBase) {//语言切换  zh_cn  zh_tw   en

        Context context = MyContextWrapper.wrap(newBase, new Locale("zh_cn"));
        super.attachBaseContext(context);
    }
}
