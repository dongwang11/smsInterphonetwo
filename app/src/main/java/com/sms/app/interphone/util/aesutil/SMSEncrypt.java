package com.sms.app.interphone.util.aesutil;



public class SMSEncrypt {

    static {
        try {
            System.loadLibrary("aes");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //public static native byte[] encode(byte[] data, byte[] key);
    //public static native byte[] decode(byte[] data, byte[] key);


    public static native byte[] aes_init(byte[] id);//机械ID

    public static native void aes_encode(byte[] in, byte[] out);//AES加密

    public static native void aes_decode(byte[] in, byte[] out);//AES解密

    public static native String sayHello();
}
