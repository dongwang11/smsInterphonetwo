package com.sms.app.framework.communication.localayer.cmd;

/**
 * Created by Administrator on 2016/11/21.
 *
 * BLE 命令 实体类
 */

public class Objcode {

    public static final byte YES = 1;

    public static final byte NO = 0;

    public static final byte FILE_INFO= 0;       //头文件
    public static final byte FILE_PART = 1;       //文件进行分包发送，一个包最大是1024,：第几个包，这个包大小
    public static final byte FILE_TEXT = 2;       //文本类型
    public static final byte FILE_RESULT = 3;     //反馈类型，比如：YES 或 NO
    public static final byte FILE_VOICE = 4;      //语音类型
    public static final byte FILE_IMG = 5;        //图片类型
    public static final byte FILE_ORDER = 6;      //AT命令类型
    public static final byte FILE_BASUC = 7;      //基本信息
    public static final byte FILE_ONE = 110;      //升级固件


    /**
     *
     * set命令 类型
     *
     * */
    public static final byte BLE_GATT_AT1846S = 8;
    public static final byte BLE_GATT_CC1101 = 9;
    public static final byte BLE_GATT_ESP8266 = 10;
    public static final byte BLE_GATT_CC2540 = 11;
    public static final byte BLE_GATT_BK8000L = 12;
    public static final byte BLE_GATT_LCD = 13;
    public static final byte BLE_GATT_KEY = 14;
    public static final byte BLE_GATT_APRS = 15;
    public static final byte BLE_IM = 16;

    public static final byte BLE_INTERPHONE = 17;

    public static final byte BLE_FILTER = 18;

    public static final byte BLE_SCNN = 20;
    public static final byte BLE_STATECHANGE = 21;
    public static final byte BLE_CONNECT = 22;

    /**
     * AT1846S  配置item
     *
     * */

    public static final byte BLE_GATT_AT1846S_POWER = 1;
    public static final byte BLE_GATT_AT1846S_FREQ = 2;
    public static final byte BLE_GATT_AT1846S_BW = 3;
    public static final byte BLE_GATT_AT1846S_SQ = 4;
    public static final byte BLE_GATT_AT1846S_VOX = 5;
    public static final byte BLE_GATT_AT1846S_TxCode = 6;
    public static final byte BLE_GATT_AT1846S_RxCode = 7;
    public static final byte BLE_GATT_AT1846S_DTMF = 8;
    public static final byte BLE_GATT_AT1846S_BLUETOOTH = 9;
    public static final byte BLE_GATT_AT1846S_RF = 10;
    public static final byte BLE_GATT_AT1846S_TxPower = 11;
    public static final byte BLE_GATT_AT1846S_TxHite = 12;
    public static final byte BLE_GATT_AT1846S_StopHite = 13;
    public static final byte BLE_GATT_AT1846S_DisconnecHite = 14;
    public static final byte BLE_GATT_AT1846S_wStart = 15;
    public static final byte BLE_GATT_AT1846S_Isplay = 16;
    public static final byte BLE_GATT_AT1846S_FindDevice = 17;
    public static final byte BLE_GATT_AT1846S_netDisconnect = 18;




    /**
     * CC1101  配置item
     * */
    public static final byte BLE_GATT_CC1101_POWER = 1;
    public static final byte BLE_GATT_CC1101_FREQ = 2;
    public static final byte BLE_GATT_CC1101_RATE = 3;
    public static final byte BLE_GATT_CC1101_POWER_TO = 4;
    public static final byte BLE_GATT_CC1101_ZHUWANG = 5;


    /**
     * ESP8266  配置item
     * */
    public static final byte BLE_GATT_ESP8266_POWER = 1;
    public static final byte BLE_GATT_ESP8266_CONT_AP = 2;
    public static final byte BLE_GATT_ESP8266_DISCONT_AP = 3;
    public static final byte BLE_GATT_ESP8266_RESET = 4;


    /**
     * CC2540  配置item
     * */

    public static final byte BLE_GATT_CC2540_NAME = 2;
    public static final byte BLE_GATT_CC2540_BROADCAST = 3;
    public static final byte BLE_GATT_CC2540_ADINV = 4;
    public static final byte BLE_GATT_CC2540_DISCONT = 5;
    public static final byte BLE_GATT_CC2540_NEINV = 6;
    public static final byte BLE_GATT_CC2540_MAC = 7;


    /**
     * BK8000L 配置item
     * */
    public static final byte BLE_GATT_BK8000L_POWER  = 1;



    /**
     * LCD  配置item
     * */
    public static final byte BLE_GATT_LCD_MODE = 1;


    /**
     * KET  配置item
     * */
    public static final byte BLE_GATT_KEY_EVENT_VOICE = 1;
    public static final byte BLE_GATT_KEY_EVENT_PLAY = 2;
    public static final byte BLE_GATT_KEY_EVENT_VOL = 3;
    public static final byte BLE_GATT_KEY_EVENT_SCAN = 4;
    public static final byte BLE_GATT_KEY_EVENT_LOCK = 5;
    public static final byte BLE_GATT_KEY_EVENT_MODE = 6;
    public static final byte BLE_GATT_KEY_EVENT_SQL = 7;
    public static final byte BLE_GATT_KEY_EVENT_WRINING_TONE = 8;


    /**
     * APRS  配置item
     * */
    public static final byte BLE_GATT_APRS_POWER = 1;
    public static final byte BLE_GATT_APRS_POSITION = 2;
    public static final byte BLE_GATT_APRS_MESSAGE = 3;


/*************************************************************************************************************************************/


    /**
     *      GET 命令类型
     *
     * AT1846S  配置item
     * */

    public static final byte BLE_GET_AT1846S = 1;
    public static final byte BLE_GET_CC1101 = 2;
    public static final byte BLE_GET_CONFIG = 3;
    public static final byte BLE_GET_CC2540 = 4;
    public static final byte BLE_GET_BK8000L = 5;
    public static final byte BLE_GET_ESP8266 = 6;


    public static final byte BLE_INTERPHONE_MODE = 1;//8  bit
    public static final byte BLE_INTERPHONE_NAME = 2;//32 byte
    public static final byte BLE_INTERPHONE_HARD_VERSION = 3;//32bit
    public static final byte BLE_INTERPHONE_SOFT_VERSION = 4;//32bit
    public static final byte BLE_INTERPHONE_USERID = 5;//8  bit
    public static final byte BLE_INTERPHONE_FIRMWARE = 9;//8  bit
    public static final byte BLE_INTERPHONE_STATE = 10;//8
    public static final byte BLE_INTERPHONE_BLUETOOTH = 11;//8
    public static final byte BLE_INTERPHONE_FACTIRY_RESET = 12;//8
    public static final byte BLE_INTERPHONE_DEVICE_RESET = 13;//8


    public static final byte COMMON = 1;  //通用
    public static final byte DRIVER = 2;  //对讲机
    public static final byte MESSAGE = 3; //消息
    public static final byte FIRMWARE = 4;//固件
    public static final byte FRIDY = 0;//胡歌版本






}
