package com.sms.app.framework.communication.localayer.cmd;

/**
 * Created by Administrator on 2016/11/24.
 */

public class Opcode {

    public static final byte SET = 0;//app对硬件的设置或者写
    public static final byte GET = 1;//app主动去查询硬件信息
    public static final byte NOYIFY = 2;//硬件自己主动来通知app
    public static final byte RESULT = 3;     //反馈类型
    public static final byte START = 5;
    public static final byte ATTR = 6;
    public static final byte MESG = 7;
    public static final byte SCAN = 8;




    public static final String TEXT = "txt";     //文本
    public static final String IMG = "img";     //IMG
    public static final String FILE = "file";     //IMG

    public static final String VERSION = "version";     //






}
