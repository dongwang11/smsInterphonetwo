package com.sms.app.interphone.util.frequentlyutil;

/**
 * Created by Administrator on 2018/3/16 0016.
 */

public class IntValue{

    private long time;
    private int data;

    public IntValue(){}

    public IntValue(int data, long time){
        this.data = data;
        this.time = time;
    }
    public long getTime(){
        return time;
    }

    public int getData(){
        return data;
    }

    public void setTime(long time){
        this.time = time;
    }

    public void setData(int data){
        this.data = data;
    }

}
