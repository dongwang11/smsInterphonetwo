package com.sms.app.interphone.util.voiceutil.SpeexRecorder;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.interphone.util.voiceutil.voice.SpeexEncoder;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

/**
 * Created by Administrator on 2016/12/8.
 */

public class Voice_Read_Thread implements Runnable {

    private Context mContext;

    private final Object mutex = new Object();

    private static int packagesize = 160;

    private boolean isRecording=true;

    private String mNeme;

    private int mQuality;


    private int max=6535;
    private int min=0;

    private Random random = new Random();

    private int s = random.nextInt(max)%(max-min+1) + min;

    //随机数生成语音包ID
    private long packId = s;

    private long time = System.currentTimeMillis();

    private Handler mHandler = null;

    // 采样率 每秒8000个采样点 api23支持从4000到192000。api15支持是4000到48000,。考虑到兼容性，采样率还是尽量在4000-48000.
    private int frequence = 8000; //录制频率，单位hz.这里的值注意了，写的不好，可能实例化AudioRecord对象的时候，会出错。我开始写成11025就不行。这取决于硬件设备

    //声道数2个
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;

    //采样精度 一个采样点16比特，相当于2个字节
    private int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;


    public Voice_Read_Thread(Context context,String name, Handler mhandler, int quality) {
        this.mContext = context;
        this.mQuality = quality;
        this.mHandler = mhandler;
        //在这里我们创建一个文件，用于保存录制内容

        String fileName = context.getExternalFilesDir(Environment.DIRECTORY_RINGTONES).getPath()+"/data/"+name+"/sms_voice/"+UUID.randomUUID().toString()+".pcm";

        try {
            File file = new File(fileName);
            if(!file.exists()){
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        }

        this.mNeme=fileName;

        if(MyApplicatoin.audioRecord == null){

            int bufferSize = AudioRecord.getMinBufferSize(frequence, channelConfig, audioEncoding);

            MyApplicatoin.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);

        }else{

            if(MyApplicatoin.audioRecord.getState() != AudioRecord.STATE_INITIALIZED){

                MyApplicatoin.audioRecord.release();
                MyApplicatoin.audioRecord = null;

                int bufferSize = AudioRecord.getMinBufferSize(frequence, channelConfig, audioEncoding);

                MyApplicatoin.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);

            }

        }

        LogUtil.i(GlobalConsts.TAG,"FileNeme="+mNeme);
    }


    @Override
    public void run() {
        // 启动编码线程
        SpeexEncoder encoder = new SpeexEncoder(this.mContext,this.mHandler,packId,this.mNeme, this.mQuality);
        Thread encodeThread = new Thread(encoder);
        encoder.setRecording(true);
        encodeThread.start();

        synchronized (mutex) {
            while (!this.isRecording) {
                try {
                    mutex.wait();
                } catch (InterruptedException e) {
                    ExceptionUtil.handleException(e);
                    throw new IllegalStateException("Wait() interrupted!", e);
                }
            }
        }

        isRecording = true;

        try {

            //根据定义好的几个配置，来获取合适的缓冲大小

            /*if(MyApplicatoin.audioRecord == null){
                //实例化AudioRecord
                MyApplicatoin.audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequence, channelConfig, audioEncoding, bufferSize);

            }*/

            try {
                /*AutomaticGainControl agc = AutomaticGainControl.create(audioRecord.getAudioSessionId());

                AcousticEchoCanceler aec = AcousticEchoCanceler.create(audioRecord.getAudioSessionId());

                NoiseSuppressor ns = NoiseSuppressor.create(audioRecord.getAudioSessionId());*/


                /*if (AutomaticGainControl.isAvailable()) {
                    agc.setEnabled(true);
                } else {
                    LogUtil.i(GlobalConsts.TAG,"您的手机不支持自动增强");
                }

                if (AcousticEchoCanceler.isAvailable()) {
                    aec.setEnabled(true);
                } else {
                    LogUtil.i(GlobalConsts.TAG,"您的手机不支持回声消除");
                }
                if (NoiseSuppressor.isAvailable()) {
                    ns.setEnabled(true);
                } else {
                    LogUtil.i(GlobalConsts.TAG,"您的手机不支持降噪");
                }*/
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }

            //定义缓冲
            short[] buffer = new short[packagesize];
            //开始录制

            if(MyApplicatoin.audioRecord.getState() == AudioRecord.STATE_INITIALIZED){

                MyApplicatoin.audioRecord.startRecording();

                int r = 0; //存储录制进度

                while (isRecording) {

                    int bufferReadResult = MyApplicatoin.audioRecord.read(buffer, 0, packagesize);
                    if (bufferReadResult == AudioRecord.ERROR_INVALID_OPERATION) {
                        throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
                    } else if (bufferReadResult == AudioRecord.ERROR_BAD_VALUE) {
                        throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
                    } else if (bufferReadResult == AudioRecord.ERROR_INVALID_OPERATION) {
                        throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
                    }

                    //    System.out.printf("-----------------------------------");

                    /*short[] voiceBuffer = new short[packagesize];

                    for(int i = 0;i < buffer.length;i++){

                           voiceBuffer[i] = (short) (buffer[i] * 0.1);
                        //    System.out.printf("0x%04x", buffer[i]);
                        //    LogUtil.i(GlobalConsts.TAG,"录音数据："+buffer[i]);
                    }*/

                    LogUtil.i(GlobalConsts.TAG,"录音数据："+Arrays.toString(buffer));

                    encoder.putData(buffer, bufferReadResult,time);

                    r++; //自增进度值

                    long v = 0;
                    // 将 buffer 内容取出，进行平方和运算
                    for (int i = 0; i < buffer.length; i++) {
                        v += buffer[i] * buffer[i];
                    }
                    // 平方和除以数据总长度，得到音量大小。
                    double mean = v / (double) bufferReadResult;
                    double volume = 10 * Math.log10(mean);
                    int l = 150/10;
                    int voice = (int) (volume/l);

                    //    LogUtil.i(GlobalConsts.TAG, "分贝值:" + voice);

                    Message mes = new Message();
                    mes.what = GlobalConsts.VOICE_SIZE;
                    mes.obj = voice;

                    mHandler.sendMessage(mes);
                }
                //录制结束
                encoder.setRecording(false);

                MyApplicatoin.audioRecord.stop();

            }

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public void setRecording(boolean isRecording) {
        synchronized (mutex) {
            this.isRecording = isRecording;

            if (this.isRecording) {
                mutex.notify();
            }
        }
    }

    public boolean isRecording() {
        synchronized (mutex) {
            return isRecording;
        }
    }
}
