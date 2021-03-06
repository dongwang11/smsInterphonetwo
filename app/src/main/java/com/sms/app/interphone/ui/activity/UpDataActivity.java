package com.sms.app.interphone.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothProfile;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.communication.localayer.cmd.Result;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.services.DfuService;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.view.ArcProgressBar;

import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.nordicsemi.android.dfu.DfuProgressListener;
import no.nordicsemi.android.dfu.DfuProgressListenerAdapter;
import no.nordicsemi.android.dfu.DfuServiceInitiator;
import no.nordicsemi.android.dfu.DfuServiceListenerHelper;

public class UpDataActivity extends Activity {

    private final String TAG = "YanShi...Log - UpDataActivity";
    private ArcProgressBar mArcProgressBar;
//    private ImageView img_return;

    private String mFilePath ;

    private Uri mFileStreamUri;

    private boolean isUpData;


   // private long MaxProgress = 0; //总进度
   // private long Progress = 0;   //当前进度



    private final DfuProgressListener mDfuProgressListener = new DfuProgressListenerAdapter() {

        /**
         *
         * 连接，方法在DFU服务开始连接到DFU目标时调用。
         * */

        @Override
        public void onDeviceConnecting(String deviceAddress) {
            LogUtil.i(TAG,"onDeviceConnecting:"+deviceAddress);


        }

        /**
         *
         * 方法调用时，服务已成功连接，在DFU目标上发现服务和DFU服务。
         */

        @Override
        public void onDeviceConnected(String deviceAddress) {
            LogUtil.i(TAG,"onDeviceConnected:"+deviceAddress);

            isUpData = true;

        }

        /**
         *
         * DFU进程启动时调用的方法。这包括读取DFU版本特征，发送DFU_START命令以及初始化包(如果设置)。
         */
        @Override
        public void onDfuProcessStarting(String deviceAddress) {

            LogUtil.i(TAG,"onDfuProcessStarting:"+deviceAddress);
            isUpData = true;
            mArcProgressBar.setProgress(1);



        }

        /**
         *
         * 方法调用时，启动DFU进程，即将发送字节。
         */
        @Override
        public void onDfuProcessStarted(String deviceAddress) {
            LogUtil.i(TAG,"onDfuProcessStarted:"+deviceAddress);

            isUpData = true;

            BluetoothLeService.isConnTime = 2000;

        }

        /**
         *
         * 当服务发现DFU目标处于应用程序模式且必须切换到DFU模式时调用的方法。
         * 将发送switch命令，DFU进程应该重新启动。在这个调用之后将不会有{@link #onDeviceDisconnected(String)}事件。
         */
        @Override
        public void onEnablingDfuMode(String deviceAddress) {
            LogUtil.i(TAG,"onEnablingDfuMode:"+deviceAddress);

        }

        /***
         *
         * 方法在上传固件时调用。当百分比值相同时，它不会被调用两次，但是，对于小型固件文件，可能会省略一些值。
         */

        @Override
        public void onProgressChanged(String deviceAddress, int percent, float speed, float avgSpeed, int currentPart, int partsTotal) {
            LogUtil.i(TAG,"onProgressChanged:"+deviceAddress+","+percent+","+speed+","+avgSpeed+","+currentPart+","+partsTotal);

            isUpData = true;
            mArcProgressBar.setProgress(percent);

        }

        /**
         *
         * 方法在目标设备上验证新固件时调用。
         */
        @Override
        public void onFirmwareValidating(String deviceAddress) {
            LogUtil.i(TAG,"onFirmwareValidating:"+deviceAddress);

        }

        /**
         *
         * 当服务开始与目标设备断开连接时调用。
         */
        @Override
        public void onDeviceDisconnecting(String deviceAddress) {
            LogUtil.i(TAG,"onDeviceDisconnecting:"+deviceAddress);

        }

        /**
         *
         * 方法从设备断开服务时调用。设备已复位。
         * */
        @Override
        public void onDeviceDisconnected(String deviceAddress) {
            LogUtil.i(TAG,"onDeviceDisconnected:"+deviceAddress);

        }


        /**
         *
         * 升级成功
         */
        @Override
        public void onDfuCompleted(String deviceAddress) {
            LogUtil.i(TAG,"onDfuCompleted:"+deviceAddress);

            Tools.showInfo(UpDataActivity.this,"更新成功");
            Tools.showInfo(UpDataActivity.this,"请重新开启对讲机");
            isUpData = false;

            finish();

        }


        /**
         *
         * 升级失败
         */
        @Override
        public void onDfuAborted(String deviceAddress) {
            LogUtil.i(TAG,"onDfuAborted:"+deviceAddress);

            Tools.showInfo(UpDataActivity.this,"更新失败");
            isUpData = false;

            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }

        /**
         *
         * 升级异常
         */

        @Override
        public void onError(String deviceAddress, int error, int errorType, String message) {
            LogUtil.i(TAG,"onError:"+deviceAddress+","+error+","+errorType+","+message);

            Tools.showInfo(UpDataActivity.this,"更新失败");
            isUpData = false;

            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up_data);
        BluetoothLeService.isConnTime = 5000;

        mFilePath = UpDataActivity.this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/sms_firmware/intercom_app.zip";
        File file = new File(mFilePath);
        if (!file.exists()) {
            mFilePath = null;

        }
        DfuServiceListenerHelper.registerProgressListener(this, mDfuProgressListener);


        if(MyApplicatoin.IS_BLE){
            if(mFilePath != null){
                final DfuServiceInitiator starter = new DfuServiceInitiator(MyApplicatoin.interphone.getAddress())
                        .setDeviceName(MyApplicatoin.interphone.getName())
                        .setKeepBond(false)
                   //     .setForceDfu(false)
                    //    .setPacketsReceiptNotificationsEnabled(false)
                    //    .setPacketsReceiptNotificationsValue(12)
                    //    .setUnsafeExperimentalButtonlessServiceInSecureDfuEnabled(true);


            //    starter.setZip(mFileStreamUri,mFilePath);
                .setZip(mFilePath);

                if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){//安卓8以上需要设置
                    starter.setForeground(false);
                    starter.setDisableNotification(true);
                }
                starter.start(UpDataActivity.this, DfuService.class);
            }else{
                Tools.showInfo(UpDataActivity.this,"文件为空！");
            }



        }else{
            Tools.showInfo(UpDataActivity.this,"设备为空！");
        }


        /*Interphone interphone = new Interphone();
        interphone.setFirmware(firePath);

        start_Ble(interphone);*/



    //    img_return = (ImageView) findViewById(R.id.update_image_return);
        mArcProgressBar = (ArcProgressBar) findViewById(R.id.arcProgressBar);

        mArcProgressBar.setMaxProgress(100);

    //    setListener();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setListener() {

        /*img_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*if(Progress == 0 || Progress < MaxProgress){
                    Toast.makeText(UpDataActivity.this, "请等待固件更新完毕", Toast.LENGTH_SHORT).show();
                    return;
                }else{
                    finish();
                }*//*

                if(isUpData){
                    Tools.showInfo(UpDataActivity.this, "请等待固件更新完毕");
                    return;
                }else{
                    finish();
                }

            }
        });*/

    }


    private void start_Ble(Interphone interphone) {


        sms_fw_api.do_request(UpDataActivity.this, sms_request.SET, interphone, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {


            }
        });

        sms_fw_api.add_attr_listenner(UpDataActivity.this, new attr_listenner() {
            @Override
            public void onAttributeChang(int code, Object object) {

                if(code == Opcode.ATTR){

                    Interphone interphone = (Interphone) object;
                    if(interphone.getState() == BluetoothProfile.STATE_CONNECTED) {

                        LogUtil.i(TAG,"UpDataActivity_连接成功");

                        GlobalConsts.connect(true,interphone);


                    }else{
                        LogUtil.i(TAG,"UpDataActivity_连接断开");

                        GlobalConsts.connect(false,interphone);

                    }
                }else if(code == Opcode.MESG){

                    LogUtil.i(TAG,"UpDataActivity_收到BLE消息"+System.currentTimeMillis());


                }else if(code == Opcode.RESULT){

                    Result res = (Result) object;

                    byte[] data = res.getObjcode();

                    byte resu = res.getResult();

                    int isobjcode = Integer.parseInt(String.format("%02X", data[4]), 16);
                    int isopcode = Integer.parseInt(String.format("%02X", data[3]), 16);


                    if(resu == GlobalConsts.YES){

                        LogUtil.i(TAG,"UpDataActivity。发送成功！！！");



                        if (isobjcode == Objcode.FILE_INFO){

                            //头文件

                            byte [] bytes = new byte[data.length - 6];

                            int a = 0;

                            for(int i = 0; i < data.length; i++) {

                                if(i >= 5 && i < data.length - 1) {

                                    bytes[a++] = data[i];
                                }
                            }


                            int one = 0;
                            int to = 0;

                            for(int i = 0; i < bytes.length; i++) {

                                if(bytes[i] == 44) {

                                    if (one == 0) {

                                        one = i;
                                    } else {

                                        to = i;
                                    }
                                }
                            }


                            // 截取文件名字
                            if(one != 0) {

                                byte[] names = new byte[one];

                                for (int i = 0; i < names.length; i++) {

                                    names[i] = bytes[i];
                                }
                            }

                            //截取文件版本号

                            if(one != 0 && to != 0) {

                                byte[] names = new byte[one];

                                for (int i = 0; i < names.length; i++) {

                                    names[i] = bytes[i];
                                }


                                byte[] versions  = new byte[to - (one + 1)];

                                int m = 0;

                                for(int i = 0;i < bytes.length;i++){

                                    if(i > one && i < to){

                                        versions[m++] = bytes[i];
                                    }
                                }

                                byte[] lengths  = new byte[bytes.length - (to + 1)];

                                int e = 0;

                                for(int i = 0;i < bytes.length;i++){

                                    if(i > to){

                                        lengths[e++] = bytes[i];

                                    }
                                }

                                /*MaxProgress = toInt(lengths);

                                LogUtil.i(TAG,"总长度："+MaxProgress);*/

                                LogUtil.i(TAG, Arrays.toString(names));
                                LogUtil.i(TAG,Arrays.toString(versions));
                                LogUtil.i(TAG,Arrays.toString(lengths));

                                mArcProgressBar.setMaxProgress(100);


                            }



                        }else if(isobjcode == Objcode.FILE_PART){

                            //分包文件

                            byte [] leng = new byte[2];

                            leng[0] = data[9];
                            leng[1] = data[10];

                        //    Progress += toInt(leng);


                            // 创建一个数值格式化对象
                            NumberFormat numberFormat = NumberFormat.getInstance();
                            // 设置精确到小数点后2位
                            numberFormat.setMaximumFractionDigits(0);

                            /*int result = Integer.parseInt(numberFormat.format((float)Progress/(float)MaxProgress*100));
                            mArcProgressBar.setProgress(result);

                            LogUtil.i(TAG,"已发送的长度："+result);


                            if(Progress != 0 && Progress == MaxProgress){

                                LogUtil.i(TAG,"UpDataActivity。固件更新完成");

                                *//*Toast.makeText(UpDataActivity.this, "固件更新完成", Toast.LENGTH_SHORT).show();

                                finish();*//*
                            }*/
                        }

                    }else{
                        LogUtil.i(TAG,"UpDataActivity。发送失败！！！");

                        if(isobjcode == Objcode.FILE_INFO || isobjcode == Objcode.FILE_PART){

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    Toast.makeText(UpDataActivity.this, "固件更新失败", Toast.LENGTH_SHORT).show();

                                    finish();
                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                }
                            });

                        }
                    }

                }

            }

        });
    }

    private long toInt(byte[] date){

        long iOutcome = 0;
        if(date.length > 2){
            iOutcome = (long) ((date[0] & 0xFF)
                    | ((date[1] & 0xFF)<<8)
                    | ((date[2] & 0xFF)<<16)
                    | ((date[3] & 0xFF)<<24));
            return iOutcome;
        }else{
            iOutcome = (long) ((date[0] & 0xFF)
                    | ((date[1] & 0xFF)<<8));
            return iOutcome;
        }

    }


    @Override
    public void onBackPressed() {

        /*if(Progress == 0 || Progress < MaxProgress){
            Toast.makeText(this, "请等待固件更新完毕", Toast.LENGTH_SHORT).show();
            return;
        }else{
            super.onBackPressed();
        }*/
        if(isUpData){
            Tools.showInfo(this, "请等待固件更新完毕");
            return;
        }else{
            super.onBackPressed();
        }


    }





}