package com.sms.app.interphone.util.aesutil;

import android.util.Log;


/**
 * Created by Administrator on 2017/7/27 0027.
 */

public class EncryptUtil {

    private static boolean isEncryptOpen = false;
    private static short[] mKey = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, 0x88, 0x99, 0xaa, 0xbb, 0xcc, 0xdd, 0xee, 0xff}; //密钥

    public static boolean getState(){
        return isEncryptOpen;
    }

    public static void setState(boolean state){
        isEncryptOpen = state;
    }

    public static short[] getKey(){
        return mKey;
    }

    public static void setKey(short[] key){
        if(key.length == 16)    mKey = key;
        Log.i("KEY", "秘钥修改成功");
    }

/*
    public static byte[] encode(byte[] data, short[] key){
        if(isEncryptOpen){
            Log.e("AES", "加密前: " + bytesToHexString(data));
            byte[] vdata  = SMSEncrypt.encode(data, shortToByte(key));
            Log.e("AES", "加密后: " + bytesToHexString(vdata) + " end");
            return vdata;
        }
        return data;
        //System.arraycopy(SMSEncrypt.decode(value, shortToByte(byteKey)), 0, data, 0, value.length);
    }

    public static byte[] decode(byte[] data, short[] key){
        if(isEncryptOpen){
            byte[] vdata  = SMSEncrypt.decode(data, shortToByte(key));
            Log.e("AES", "解密后: " + bytesToHexString(vdata)  + " end");
            return vdata;
        }
        return data;
    }*/

    public static byte[] shortToByte(short[] sdata) {
        byte[] bdata = new byte[sdata.length];
        for(int i=0; i<sdata.length; i++){
            bdata[i] = (byte)sdata[i];
        }
        return bdata;
    }

    public static short[] byteToShort(byte[] sdata) {
        short[] bdata = new short[sdata.length];
        for(int i=0; i<sdata.length; i++){
            bdata[i] = (short)sdata[i];
        }
        return bdata;
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase()+",");
        }
        return  sb.toString();
    }

}
