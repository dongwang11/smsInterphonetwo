package com.sms.app.interphone.util.maputil;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.sms.app.interphone.util.msnutil.ExceptionUtil;

/**
 * Created by Administrator on 2017/7/21.
 * YanShi
 */

public class TimeThread implements Runnable {

    private final Object time = new Object();


    private volatile int isCode;

    private int newtime = 1;
    private int start = 2;
    private int end = 3;
    private int stop = 4;
    private int thread = 0;

    private int Time = 0;

    private Handler handler = null;

    private long currentTime = 0;


    public TimeThread(Handler handler){

        this.handler = handler;
        Log.e("Time","TimeThread.start");
    }

    @Override
    public void run() {

        Log.e("Time","TimeThread.run:"+thread);

        while (this.thread != stop){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                ExceptionUtil.handleException(e);
            }
            if(this.thread == start){

                currentTime += 1000;

            }else if(this.thread == newtime){

                currentTime = 0;

            }

            Message msg = new Message();
            msg.what = Time;
            msg.obj = formatTime(currentTime);

            this.handler.sendMessage(msg);
            Log.e("Time","currentTime:"+formatTime(currentTime));
        }
    }


    public void setCode(int isCode) {
        synchronized (time) {
            this.thread = isCode;

            if (this.thread != 0) {
                time.notify();
            }
        }
    }

    public int thread() {
        synchronized (time) {
            return thread;
        }
    }

    /**
     * 毫秒转化时分秒毫秒
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();

        if(hour < 10) {
            sb.append("0"+hour+":");
        }else{
            sb.append(hour+":");
        }
        if(minute < 10) {
            sb.append("0"+minute+":");
        }else{
            sb.append(minute+":");
        }
        if(second < 10){
            sb.append("0"+second);
        }else{
            sb.append(second);
        }

        return sb.toString();
    }
}
