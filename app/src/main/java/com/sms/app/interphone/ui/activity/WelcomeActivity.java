package com.sms.app.interphone.ui.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.widget.ImageView;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Precursor;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.UpdataService;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WelcomeActivity extends BaseActivity {

    private static String TAG = "YanShi...Log - WelcomeActivity";

    private Handler handler=new Handler();

    private ImageView imageView;

    private UpdataService service;


    private String url;

    private String android_version;
    private String android_url;
    private String android_description;



    private String a_adv_version;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        setUi();
        setNextPager();

    }


    private void setUi() {

        imageView = (ImageView) findViewById(R.id.welcome_image);

        Precursor precursor = new Precursor();

        precursor.setSeries("GLOBALWALKIE");

        SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
        final String PrecursorString[] = new String[]{sharedPreferences.getString(GlobalConsts.PRECURSOR_ADV_VERSION, ""),
                sharedPreferences.getString(GlobalConsts.PRECURSOR_ADV_URL, "")};

        if(PrecursorString[1].equals("")){

            imageView.setImageResource(R.mipmap.sms_advtwo);

        }else{

            //版本一致，加载本地图片文件

            Bitmap bitmap = getLoacalBitmap(PrecursorString[1]); //从本地取图片(在cdcard中获取)

            imageView.setImageBitmap(bitmap);

        }
        sms_request.dorequest(WelcomeActivity.this, sms_request.GET, precursor, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                LogUtil.i(TAG,"RequestCB:"+opcode);

                if(opcode == sms_request.GET){
                    try {
                        Precursor precursor = (Precursor) obj;


                        if (precursor != null) {

                            a_adv_version = precursor.getAdv_version();

                            android_version = precursor.getAndroid_version();
                            android_url = precursor.getAndroid_url();
                            android_description = precursor.getAndroid_description();

                            if(PrecursorString[0].equals("")){

                                url = precursor.getAdv_url();

                                if(url != null){

                                    Intent intent = new Intent(WelcomeActivity.this, UpdataService.class);
                                    intent.setAction(UpdataService.WELCOME);
                                    bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                }else{

                                    LogUtil.i(TAG,"异常。。url = null");
                                }
                            //  int b_adv_version = Integer.parseInt(PrecursorString[0]);

                            }else{


                                if(!precursor.getAdv_version().equals(PrecursorString[0])){

                                    //版本不一致重新下载图片文件

                                    url = precursor.getAdv_url();

                                    if(url != null){

                                        Intent intent = new Intent(WelcomeActivity.this, UpdataService.class);
                                        intent.setAction(UpdataService.WELCOME);
                                        bindService(intent, connection, Context.BIND_AUTO_CREATE);

                                    }else{

                                        LogUtil.i(TAG,"异常。。url = null");
                                    }

                                }else{

                                    //版本一致，加载本地图片文件

                                    Bitmap bitmap = getLoacalBitmap(PrecursorString[1]); //从本地取图片(在cdcard中获取)

                                    imageView.setImageBitmap(bitmap);
                                    // imageView.setImageResource(R.mipmap.sms_advtwo);
                                }
                            }

                        }else{

                            LogUtil.i(TAG,"异常。。。precursor = null");


                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }


                }else{

                    //网络访问异常，加载本地图片文件

                    Bitmap bitmap = getLoacalBitmap(PrecursorString[1]); //从本地取图片(在cdcard中获取)

                    //imageView.setImageBitmap(bitmap);
                    imageView.setImageResource(R.mipmap.sms_advtwo);

                }

            }
        });

    }

    /**负责从网络读取广告图片*/
    class HandlerTask implements Runnable{
        @Override
        public void run() {

            SharedPreferences sp= getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);//"sms"
            boolean isUsed=sp.getBoolean("isUsed", false);
            if(isUsed){//已经使用过
               // LogUtil.i("aaaa","bbbbbbbbbandroid_version:"+android_version+"      ,android_url:"+android_url+"   a_adv_version:"+a_adv_version);
                Intent intent=new Intent(WelcomeActivity.this,BlescanActivity.class);//BlescanActivity   MainActivity
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_URL,android_url);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION,android_version);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION,android_description);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }else{//第一次使用
                SharedPreferences.Editor et=sp.edit();
                et.putBoolean("isUsed", true);
                et.commit();
                Intent intent=new Intent(WelcomeActivity.this,BlescanActivity.class);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_URL,android_url);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_VERSION,android_version);
                intent.putExtra(GlobalConsts.PRECURSOR_ANDROID_DESCRIPTION,android_description);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);

            }

            finish();
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out);


        }
    }


    /**进入下一个页面*/
    private void setNextPager() {
        Handler h=new Handler();
        h.postDelayed(
                new HandlerTask(),
                3500);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {
            service = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder services) {

            service = ((UpdataService.UpdataBinder) services).getService();
            // 执行Service内部自己的方法

            if(service != null){

                if(service.UpDataServiceAcoin.equals(UpdataService.WELCOME)){

                    new AsyncTask<Object, Object, String>() {
                        @Override
                        protected String doInBackground(Object... params) {
                            return service.excute(UpdataService.imgfile,url,"adv");
                        }

                        @Override
                        protected void onPostExecute(String path) {

                            try {
                                if(path != null){

                                    SharedPreferences sharedPreferences = WelcomeActivity.this.getSharedPreferences(GlobalConsts.PRECURSOR, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(GlobalConsts.PRECURSOR_ADV_URL,path);
                                    editor.putString(GlobalConsts.PRECURSOR_ADV_VERSION,a_adv_version);

                                    editor.commit();

                            Bitmap bitmap = getLoacalBitmap(path); //从本地取图片(在cdcard中获取)

                                  // imageView.setImageBitmap(bitmap);
                                    imageView.setImageResource(R.mipmap.sms_advtwo);

                                    LogUtil.i(TAG,"下载完成。。。");
                                }else{

                                    LogUtil.i(TAG,"下载异常。。。");

                                }


                            } catch (Exception e) {

                                ExceptionUtil.handleException(e);

                            }

                            try {

                                if(connection != null){
                                    unbindService(connection);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }.execute();
                }else{
                    try {

                        if(connection != null){
                            unbindService(connection);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }else{
                try {

                    if(connection != null){
                        unbindService(connection);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }


        }
    };


    /**
     * 加载本地图片
     * http://bbs.3gstdy.com
     * @param url
     * @return
     */
    public Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            ExceptionUtil.handleException(e);
            return null;
        }
    }

    /**重写此方法的目的，屏蔽回退操作*/
    @Override
    public void onBackPressed() {}

}