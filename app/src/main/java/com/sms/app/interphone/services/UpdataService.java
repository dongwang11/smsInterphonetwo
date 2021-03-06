package com.sms.app.interphone.services;

/**
 * Created by Administrator on 2018/5/9.
 */

import android.app.DownloadManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.frequentlyutil.DownloadUtils;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.CRC;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 检测安装更新文件的助手类
 *
 * @author G.Y.Y
 *
 */

public class UpdataService extends Service {

    private static String TAG = "YanShi...Log - UpdataService";

    public static final int imgfile = 0;
    public static final int apkfile = 1;
    public static final int firmwarefile = 2;


    public static final String WELCOME = "welcome";
    public static final String APK = "sms_apk";
    public static final String NITECORE = "nitecore";
    public static final String ERRO = "erro";


    public static String UpDataServiceAcoin = ERRO;





    /** 安卓系统下载类 **/
    DownloadManager manager;

    /** 接收下载完的广播 **/
    DownloadCompleteReceiver receiver;

    /** 初始化下载器
     * @param url**/
    private void initDownManager(String url) {


        MyApplicatoin.isUpdata = true;

        manager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

        receiver = new DownloadCompleteReceiver();

        //注册下载广播
        registerReceiver(receiver, new IntentFilter(
                DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        try {
            //    String url = "http://192.168.1.60:8080/acount_server/app/app_sms_android_3_3178143.apk";


         /*   if (Build.VERSION.SDK_INT > 26||Build.VERSION.SDK_INT==26) {////版本大于等于26 浏览器下载apk
               // startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri.parse(url)));
                MyApplicatoin.isUpdata = false;
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.startActivity(intent);
                return ;
            }*/
            if (Build.VERSION.SDK_INT > 24||Build.VERSION.SDK_INT==24) {////版本大于等于24
                MyApplicatoin.isUpdata = false;
                new DownloadUtils(this,url, "sms.apk");
                return ;
            }

            //设置下载地址
            DownloadManager.Request down = new DownloadManager.Request(
                    Uri.parse(url));


            File file = new File(this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"sms.apk");


            clearApk(file);

            // 设置允许使用的网络类型，这里是移动网络和wifi都可以
            down.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE
                    | DownloadManager.Request.NETWORK_WIFI);

            // 下载时，通知栏显示途中
            down.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);

            // 显示下载界面
            down.setVisibleInDownloadsUi(true);

            // 设置下载后文件存放的位置
            down.setDestinationUri(Uri.fromFile(file));

            //使用DownLoadManager来下载
            // 将下载请求放入队列
            long downloadId = manager.enqueue(down);
            getProgress(downloadId);

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public boolean onUnbind(Intent intent) {
        // 当调用者退出(即使没有调用unbindService)或者主动停止服务时会调用
        LogUtil.i(TAG,"调用者退出了");
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {

        // 注销下载广播
        if (receiver != null)
            unregisterReceiver(receiver);

        LogUtil.i(TAG,"下载服务销毁");

        UpDataServiceAcoin = ERRO;

        super.onDestroy();
    }

    private final IBinder binder = new UpdataBinder();

    @Override
    public IBinder onBind(Intent intent) {

        UpDataServiceAcoin = intent.getAction();
        LogUtil.i(TAG,"onBind:"+intent.getAction());

        return binder;
    }

    public class UpdataBinder extends Binder {
        public UpdataService getService() {
            return UpdataService.this;
        }
    }

    public String excute(final int code, final String url, final String name) {

        final String[] path = {null};

        try {

            if(code != 1){

                //如果是图片下载

                if(url != null){

                    OkHttpClient client = new OkHttpClient();
                    final Request request = new Request.Builder().get()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                            path[0] = "erro";
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            if (response.isSuccessful()) {

                                byte[] bytes = response.body().bytes();

                                if(code == 0){

                                    //图片下载请求结果
                                    path[0] = Tools.writeImgSdcard(UpdataService.this,name,bytes);

                                }else{

                                    //固件下载请求结果
                                    long a = CRC.crc8(bytes,bytes.length);

                                    path[0] = Tools.writeFirmwareSdcard(UpdataService.this,"intercom_app.zip",bytes);

                                }
                            } else {

                                //异常
                                path[0] = "erro";
                            }
                        }
                    });
                } else {
                    //异常
                    path[0] = "erro";
                }

            }else{

                //如果是Apk文件下载
                initDownManager(url);

                path[0] = "正在下载";

            }
        } catch (Exception e) {

            //异常
            path[0] = "erro";

            ExceptionUtil.handleException(e);

        }

        try {
            while(path[0] == null) {
                Thread.sleep(50);
            }

        } catch (InterruptedException e) {

            ExceptionUtil.handleException(e);
        }
        LogUtil.i(GlobalConsts.TAG,"数据："+ path[0]);

        return path[0];

    }

    // 接受下载完成后的intent
    class DownloadCompleteReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            //判断是否下载完成的广播
            if (intent.getAction().equals(
                    DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {

                //获取下载的文件id
                long downId = intent.getLongExtra(
                        DownloadManager.EXTRA_DOWNLOAD_ID, -1);

                MyApplicatoin.isUpdata = false;

                LogUtil.i("UpdataService","下载完成"+downId);
                //停止服务并关闭广播
                UpdataService.this.stopSelf();
                //自动安装apk

                context.sendBroadcast(new Intent(GlobalConsts.ACTION_LOGIN).putExtra(GlobalConsts.ACTION_APK,1));

             //   installVersion(context);
            //    installAPK();
            //    installAPK();



            }
        }


    }




    /**
     * 获取进度信息
     * @param downloadId 要获取下载的id
     * @return 进度信息 max-100
     */
    public int getProgress(long downloadId) {
        //查询进度
        DownloadManager.Query query = new DownloadManager.Query()
                .setFilterById(downloadId);
        Cursor cursor = null;
        int progress = 0;
        try {
            cursor = manager.query(query);//获得游标
            if (cursor != null && cursor.moveToFirst()) {
                //当前的下载量
                int downloadSoFar = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                //文件总大小
                int totalBytes = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                progress = (int) (downloadSoFar * 1.0f / totalBytes * 100);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        LogUtil.i("UpdataService","下载："+progress);
        return progress;
    }



    /**
     * 删除之前的apk
     *
     * @param apkName apk名字
     * @return
     */
    public static void clearApk(File apkName) {

        if (apkName.exists()) {
            apkName.delete();
        }

    }
}
