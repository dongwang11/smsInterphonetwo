package com.sms.app.interphone.util.voiceutil.SpeexRecorder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.voiceutil.voice.SpeexEncoder;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/8.
 */

public class VoiceBleRunnable extends Thread {

    private String TAG = VoiceBleRunnable.class.getName();

    private List<byte[]> list = new ArrayList<>();
    private BluetoothLeService leService = null;
    private boolean isRecording = true;



    public VoiceBleRunnable(BluetoothLeService le_services) {
        leService = le_services;
        LogUtil.i(TAG,"VoiceBleRunnable:"+this.isRecording);
    }

    @Override
    public void run() {
        // 启动编码线程
        LogUtil.i(TAG,"run:"+this.isRecording);

        int time = 1;

        while (true){

            while (list.size() > 0 && isRecording){

                try {
                    time = 0;
                    byte[] voiceData = list.remove(0);

                    byte[] id = {voiceData[10],voiceData[11],voiceData[12],voiceData[13]};

                    boolean isk = false;
                    for(long l : MyApplicatoin.ls){

                        if(l == toLong(id)){
                            isk = true;
                        }

                    }


                    if(!isk){

                        if(leService != null){

                            leService.sendWriteByte(Objcode.MESSAGE,voiceData);

                        }

                        try {
                            Thread.sleep(25);//47
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }else{

                        time = 110;

                        list.clear();

                        break;

                    }
                } catch (Exception e) {
                    e.printStackTrace();

                    list.clear();
                    time = 110;

                    break;
                }

            }

            if(time < 100)
            {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                time++;

            }else{

                setRecording(false);

                break;

            }
        }


    }

    public void setRecording(boolean isRecording) {
        synchronized (this) {
            this.isRecording = isRecording;

            if (!this.isRecording) {
                this.list.clear();
            }
        }
    }

    public void putByte(byte[] bytes) {
        list.add(bytes);
    }

    public boolean isRecording() {
        synchronized (this) {
            return isRecording;
        }
    }
    private long toLong(byte[] date){

        long iOutcome = 0;
        iOutcome = (long) ((date[0] & 0xFF)
                | ((date[1] & 0xFF)<<8)
                | ((date[2] & 0xFF)<<16)
                | ((date[3] & 0xFF)<<24));
        return iOutcome;

    }
}
