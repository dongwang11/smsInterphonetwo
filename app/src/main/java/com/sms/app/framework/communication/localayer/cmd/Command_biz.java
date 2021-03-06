package com.sms.app.framework.communication.localayer.cmd;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.content.Context;
import android.location.Location;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.sms.app.framework.communication.localayer.ImCode;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.aesutil.EncryptUtil;
import com.sms.app.interphone.util.aesutil.SMSEncrypt;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.bledriver.Ble_request;
import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;
import com.sms.app.framework.communication.localayer.bledriver.Le_intercface;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.respon;
import com.sms.app.framework.trans.bean.Interphone;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Administrator on 2017/3/21.
 */

public class Command_biz {



    private boolean isStart = false;

    private BluetoothLeService ble_service = null;

    private List<Byte> voice_data = new ArrayList<>();           // 当前接收到的语音数据集合

    private List<Byte> grout_data = new ArrayList<>();           // 当前接收到的群名数据集合

    public static Interphone_notify_listenner  notify_listenner = null;

    private static int interval = 500;               //语音接收间隔

    private static int voiceLenth = 750;               //设定语音接收最小长度


    int kkl = 0;

    private byte[] img_data = null;                                 // 当前接收到的图片数据集合

    private int fire_length = 0;

    private Context context;

    private Ble_request reqcb;

    private int UUID_A = 0;

    private static String TAG = "YanShi...Log - Command_biz";

    private static byte[] returnData = null;

    // byte[] prackID = null;

    private static byte dataCode = 0;

    private Interphone interphone = new Interphone();

    private long userID,prackID;



    private TimerTask startTime;
    private Timer startTask = new Timer();
    private int startRecLen = 3;


    public static void add_notify_listenner(Interphone_notify_listenner listenner)
    {
        Command_biz.notify_listenner = listenner;
    }
    public Command_biz(Context context, Command command, Ble_request ble_reqcb) {
        this.context = context;
        this.reqcb = ble_reqcb;

    //    LogUtil.i(TAG,"CODE:"+dataCode);

        ble_service = BluetoothLeService.get_le_service(context, new Le_intercface() {

            @Override
            public void on_scan(BluetoothDevice device, int rssi, byte[] scanRecord, BluetoothLeService service) {

                ble_service = service;

                interphone.setName(device.getName());
                interphone.setAddress(device.getAddress());

                reqcb.respone(Opcode.SCAN,device);
            }

            @Override
            public void on_statechange(BluetoothGatt gatt, int status, int newState) {

                interphone.setName(gatt.getDevice().getName());
                interphone.setAddress(gatt.getDevice().getAddress());
                interphone.setState((byte) newState);
                reqcb.respone(Opcode.ATTR,interphone);
            }

            @Override
            public void on_result(byte[] is_Date, Byte aByte) {

            //    LogUtil.i(TAG,"接收数据类型："+aByte+",数据："+Arrays.toString(is_Date));

                LogUtil.i(TAG,"接收次数");
                try {
                    if(aByte == Objcode.FRIDY){

                        byte[] data = new byte[is_Date.length-1];
                        byte crc = is_Date[is_Date.length-1];

                        System.arraycopy(is_Date,0,data,0,is_Date.length-1);

                        set_dal_data(data,crc);

                    }else{

                      setDataVersions(aByte,is_Date);

                    }
                } catch (Exception e) {

                    ExceptionUtil.handleException(e);

                }


            }

            @Override
            public void on_data(byte[] is_Date) {

                returnData = is_Date;

                if(getStart(is_Date)){

                    if(!isStart && startTime != null){

                        Interphone interphone = new Interphone();

                        interphone.setmState((byte)2);
                    //  interphone.setState(MyApplicatoin.interphone.getState());

                        reqcb.respone(Opcode.START,interphone);
                        isStart = true;
                    }

                    if(startTime == null){
                        try {

                            startRecLen = 3;

                            startTime = new TimerTask() {
                                @Override
                                public void run() {

                                    startRecLen--;

                                    if(startRecLen <= 0){

                                        if(startTime != null){

                                            startTime.cancel();

                                            startTime=null;
                                        }


                                        isStart = false;

                                        Interphone interphone = new Interphone();

                                        interphone.setmState((byte)3);
                                    //    interphone.setState(MyApplicatoin.interphone.getState());

                                        reqcb.respone(Opcode.START,interphone);
                                    }
                                }
                            };
                            startTask.schedule(startTime, 200, 200);

                        } catch (Exception e) {

                            ExceptionUtil.handleException(e);

                        }
                    }else{
                        startRecLen=3;
                    }
                }


            }

            @Override
            public void on_notification(byte code) {

                dataCode = code;

                MyApplicatoin.dataCode = code;

            }

            /**
             *
             * 发送失败回调
             *
             * */
            @Override
            public void on_send_result(byte[] objcode,byte resilt,byte code) {

                //筛选出只发送一个字节的设备信息命令，不回调。
                if(objcode.length >= 2){

                    Result res = new Result();

                    res.setObjcode(objcode);
                    res.setResult(resilt);
                    res.setCode(code);

                    reqcb.respone(Opcode.RESULT,res);

                }

            }

        });

        this.send_Command(command);
    }

    private Boolean getStart(byte[] is_Date) {

        if(is_Date.length > 10){

            byte a = is_Date[0];
            byte b = is_Date[1];
            byte c = is_Date[2];

            byte d = (byte) (is_Date[3] & 0x7F);

            if(a == 'A' && b == 'T' && c == '+' && d == 1){

                return true;

            }else{

                return false;

            }

        }else{
            return false;
        }
    }


    /**
     *
     * 协议版本2.0 数据解析层。
     *
     * */
    private void setDataVersions(Byte code, byte[] is_date) {

        // 等于0就是读操作。反之是写操作
        byte objCode = (byte) (is_date[0] & 0x80);

        byte opCode = (byte) (is_date[0] & 0x7F);

        if(code == Objcode.COMMON){

            startDevice(opCode);

            if(opCode == 1){

                if(is_date[1] != 0){

                    String a = null;

                    int b = 0;

                    for(int i = 0;i<is_date.length;i++){

                        if(is_date[i] == 0){

                            b = i;

                            break;
                        }

                    }

                    if(b >= 1){

                        byte[] data = new byte[b - 1];

                        System.arraycopy(is_date, 1,data, 0,  b-1);

                        LogUtil.i(TAG,"协议版本:"+Arrays.toString(data));

                        a = new String(data);

                    }else{

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy(is_date, 1,data, 0,  data.length);

                        LogUtil.i(TAG,"协议版本:"+Arrays.toString(data));

                        a = new String(data);


                    }

                    LogUtil.i(TAG,"协议版本："+a);

                }


            } else if (opCode == 2) {

                byte[] data = new byte[is_date.length - 1];

                System.arraycopy(is_date, 1,data, 0,  data.length);

                long a = toInt(data);

                LogUtil.i(TAG,"硬件版本："+a);

                Interphone interphone = new Interphone();

                interphone.setHard_version(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 3) {

                byte[] data = new byte[is_date.length - 1];

                System.arraycopy(is_date, 1,data, 0,  data.length);

                long a = toInt(data);

                LogUtil.i(TAG,"软件版本："+a);

                Interphone interphone = new Interphone();

                interphone.setSoft_version(a);

                reqcb.respone(Opcode.NOYIFY,interphone);

            } else if (opCode == 4) {

                if(is_date[1] != 0){

                    String a = null;

                    int b = 0;

                    for(int i = 0;i<is_date.length;i++){

                        if(is_date[i] == 0){

                            b = i;

                            break;
                        }

                    }

                    if(b >= 1){

                        byte[] data = new byte[b - 1];

                        System.arraycopy(is_date, 1,data, 0,  b-1);

                        LogUtil.i(TAG,"产品型号:"+Arrays.toString(data));

                        a = new String(data);

                       Log.d("aaaaaa","产品型号:"+Arrays.toString(data));

                    }else{

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy(is_date, 1,data, 0,  data.length);

                        LogUtil.i(TAG,"产品型号:"+Arrays.toString(data));

                        a = new String(data);


                    }

                    LogUtil.i(TAG,"产品型号："+a);
                   // Log.d("resultString","11111111111a:"+a);
                    if(a != null){//自己设置产品型号Product_model  和get set
                       // Log.d("resultString","22222222222222a:"+a);
                        Interphone interphone = new Interphone();

                        interphone.setProduct_model(a);

                        reqcb.respone(Opcode.NOYIFY,interphone);
                       // Log.d("resultString","3333333333333a:"+a);
                    }
                   // Log.d("resultString","444444444444444a:"+a);

                }



            } else if (opCode == 5) {

                String a = null;

                if(objCode == 0){

                    int b = 0;

                    for(int i = 0;i<is_date.length;i++){

                        if(is_date[i] == 0){

                            b = i;

                            break;
                        }

                    }


                    if(b >= 1){

                        byte[] data = new byte[b - 1];

                        System.arraycopy(is_date, 1,data, 0,  b-1);

                        LogUtil.i(TAG,"设备名字:"+Arrays.toString(data));

                        a = new String(data);



                    }else{

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy(is_date, 1,data, 0,  data.length);

                        LogUtil.i(TAG,"设备名字:"+Arrays.toString(data));

                        a = new String(data);


                    }

                }else{

                    byte[] data = new byte[returnData.length - 1];

                    System.arraycopy(returnData, 1,data, 0, data.length);

                    LogUtil.i(TAG,"设备名字:"+Arrays.toString(data));

                    a = new String(data);

                }

                if(a != null){

                    LogUtil.i(TAG,"设备名字："+a);

                    Interphone interphone = new Interphone();

                    interphone.setName(a);

                    reqcb.respone(Opcode.NOYIFY,interphone);
                }


            } else if (opCode == 6) {

                byte[] data = new byte[is_date.length - 1];

                System.arraycopy(is_date, 1,data, 0,  data.length);
                LogUtil.i("aaaaaaaa","data1："+toInt(data)+"   "+EncryptUtil.bytesToHexString(data));
                byte[] key = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,};
                key = SMSEncrypt.aes_init(data);
                LogUtil.i("aaaaaaaa","key："+toInt(data)+"   "+EncryptUtil.bytesToHexString(key));
                long a = toInt(data);

                LogUtil.i(TAG,"产品机械ID："+a);



            } else if (opCode == 7) {

                byte a = is_date[1];

                LogUtil.i(TAG,"OTA开关："+a);

            } else if (opCode == 8) {

                if(is_date[1] != 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0,  data.length);

                    String a = new String(data);

                    LogUtil.i(TAG,"产品激活时间："+a);

                }

            } else if (opCode == 9) {

                byte[] data = new byte[is_date.length - 1];

                System.arraycopy(is_date, 1,data, 0,  data.length);

                long a = toInt(data);

                LogUtil.i(TAG,"广播间隔："+a);
                Log.d("aaaaaaaa","广播间隔:   "+a);
            } else if (opCode == 10) {

                byte[] data = new byte[is_date.length - 1];

                System.arraycopy( is_date, 1,data, 0, data.length);

                long a = toInt(data);

                LogUtil.i(TAG,"连接间隔："+a);
                Log.d("aaaaaaaa","连接间隔:   "+a);

            }else if (opCode == 12) {

                byte a = is_date[1];

                if( a >= 60){
                    BluetoothLeService.ble_length = 60;
                }
                LogUtil.i(TAG,"MTU："+a);

            }/*else if(opCode==13){
                Log.d("aaaaaaaa","重置13 ");
                if(is_date[1] != 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0,  data.length);

                    String a = new String(data);

                    LogUtil.i(TAG,"重置："+a);
                    Log.d("aaaaaaaa","重置:   "+a);
                }

            }*/



        }else if(code == Objcode.DRIVER){

            //Log.d("aaaaaaaa","接收到的数据："+Arrays.toString(is_date));
            //没加密之前调用
            //startDevice(opCode);


            //接收解密数据，每次20个数据解密
            byte[] buffer = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
            SMSEncrypt.aes_decode(is_date,buffer);//解密
            objCode = (byte) (buffer[0] & 0x80);
            opCode = (byte) (buffer[0] & 0x7F);
            // Log.d("aaaaaaaa","接收解密      "+  EncryptUtil.bytesToHexString(buffer)+"  "+Arrays.toString(buffer));
            Log.d("aaaaaaaa","objCode:     "+  objCode+"       opCode:"+opCode+"    buffer[0]:"+buffer[0]);
            startDevice(opCode);
            is_date=buffer;
            if(grout_data.size() > 0){

                for(byte data : is_date){

                    grout_data.add(data);
                }

                byte[] name = new byte[grout_data.size()];

                for(int i = 0;i < grout_data.size();i++){

                    name[i] = grout_data.get(i);

                }
               // Log.d("bbbbbbbbbbbbbbbbb","name:     "+ EncryptUtil.bytesToHexString(name)+ "  "+Arrays.toString(name)+" "+grout_data.size());
                try {

                    if(grout_data.size() > 14){

                        byte[] data = new byte[14];

                        System.arraycopy(name, 1,data, 0,  14);
                        Log.d("aaaaaaaa","群名昵称："+ Arrays.toString(data)+EncryptUtil.bytesToHexString(data));

                        String akName = new String(data,"GBK");//new String(data,"ISO-8859-1")
                        /*byte[] aData = new byte[grout_data.size() - 15];

                        System.arraycopy(name, 15,aData, 0,  grout_data.size() - 15);*/

                        int k = grout_data.size()-15;

                        for(int i = 15;i <grout_data.size();i++){
                            if(grout_data.get(i) == (byte)0){
                                k = i-15;
                                break;
                            }
                        }
                       LogUtil.i("aaaaaaaa","接收到的数据2:"+k);
                        int l = 0;
                        byte[] aData = new byte[k];

                        for(int i = 15;i <15+k;i++){
                            aData[l++] = grout_data.get(i);
                        }
                    //    LogUtil.i(TAG,"接收到的数据1:"+Arrays.toString(aData));
                        String aName = new String(aData,"GBK");

                        /*int k = aData.length;

                        String aName = null;

                        for(int i = 0; i < aData.length;i++){

                            if(aData[i] == 0 &&){

                                k = i;
                                break;
                            }
                        }

                        if(k == aData.length){



                        }else{

                            byte[] ak = new byte[k];
                            System.arraycopy(aData, 0,ak, 0,  k);

                            aName = new String(ak,"ISO-8859-1");
                        }*/

                        if(aName != null){

                            Log.d("resultString","resultString656565656:     "+ akName+"       "+aName);
                            Log.d("aaaaaaaa","群名昵称："+aName);
                            //Log.d("bbbbbbbbbbbbbbbbb","a22222222222："+akName+"  "+aName);
                            Interphone interphone = new Interphone();

                            ImCode imCode = new ImCode();
                            imCode.setName(akName+aName);

                            interphone.setImCode(imCode);

                            reqcb.respone(Opcode.NOYIFY,interphone);


                        }



                    }
                } catch (Exception e) {//UnsupportedEncodingException
                    ExceptionUtil.handleException(e);
                }

                grout_data.clear();


            }

            if(opCode == 1){
                byte a = 0;

                if(objCode == 0){
                    //读操作
                     a = is_date[1];
                    Log.d("aaaaaaaa","带宽模式："+a);
                }else{

                     a = returnData[1];
                    Log.d("aaaaaaaa","带宽模式2222222："+a);

                }

                LogUtil.i(TAG,"带宽模式："+a);

                Interphone interphone = new Interphone();

                interphone.setBw(a);

                reqcb.respone(Opcode.NOYIFY,interphone);



            } else if (opCode == 2) {

                long a = 0;

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0,  data.length);

                    a = toInt(data);

                }else{

                    byte[] data = new byte[returnData.length - 1];

                    System.arraycopy(returnData, 1,data, 0,  data.length);

                    a = toInt(data);

                }

                LogUtil.i("aaaaaaaa","频率大小："+a);

                Interphone interphone = new Interphone();

                interphone.setFrequne((int) a);
                reqcb.respone(Opcode.NOYIFY,interphone);

            } else if (opCode == 3) {

                byte a = 0;
                if(objCode == 0){

                    a = is_date[1];

                    LogUtil.i(TAG,"音量："+a);


                }else{

                }


            } else if (opCode == 4) {

                byte a = 0;
                if(objCode == 0){

                    a = is_date[1];

                }else{
                    a = returnData[1];

                }

                LogUtil.i(TAG,"SQ等级："+a);

                Interphone interphone = new Interphone();

                interphone.setSq(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 5) {

                byte a = 0;
                if(objCode == 0){

                    a = is_date[1];

                }else{
                    a = returnData[1];

                }
                LogUtil.i(TAG,"vox等级："+a);

                Interphone interphone = new Interphone();

                interphone.setVox(a);

                reqcb.respone(Opcode.NOYIFY,interphone);



            } else if (opCode == 6) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"接收亚音频："+a);

                Interphone interphone = new Interphone();

                interphone.setRxcode((int) a);

                reqcb.respone(Opcode.NOYIFY,interphone);





            } else if (opCode == 7) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"发送亚音频："+a);

                Interphone interphone = new Interphone();

                interphone.setTxcode((int) a);

                reqcb.respone(Opcode.NOYIFY,interphone);




            } else if (opCode == 8) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"数模模式："+a);

                Interphone interphone = new Interphone();

                interphone.setMode(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 9) {

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0, data.length);

                    Ble_message ble_message = new Ble_message();

                    byte[] userID = new byte[4];

                    userID[0] = data[0];
                    userID[1] = data[1];
                    userID[2] = data[2];
                    userID[3] = data[3];

                    byte loceions[] = new byte[7];

                    loceions[0] = data[4];
                    loceions[1] = data[5];
                    loceions[2] = data[6];
                    loceions[3] = data[7];
                    loceions[4] = data[8];
                    loceions[5] = data[9];
                    loceions[6] = data[10];

                    LogUtil.i(TAG,"位置数据："+Arrays.toString(loceions));

                    Location location = Locations.aprs_dec_dmss(loceions);


                    /*if(location != null){

                        ble_message.setTime(System.currentTimeMillis());
                        ble_message.setType(Ble_message.aprs);
                        ble_message.setUserId(toInt(userID));
                        ble_message.setLatitude(location.getLatitude());
                        ble_message.setLongitude(location.getLongitude());

                        reqcb.respone(Opcode.MESG,ble_message);

                    }*/

                    LogUtil.i(TAG,"位置共享："+location.toString());


                }else{

                }



            } else if (opCode == 10) {

                if(objCode == 0){

                    byte a = is_date[1];

                    Interphone interphone = new Interphone();

                    interphone.setState(a);

                    reqcb.respone(Opcode.START,interphone);


                }



            } else if (opCode == 11) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                Interphone interphone = new Interphone();

                interphone.setPlays(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


                LogUtil.i(TAG,"电源状态："+a);




            } else if (opCode == 12) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"蓝牙音乐开关："+a);

                Interphone interphone = new Interphone();

                interphone.setBluetooth(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 13) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"发射功率："+a);

                Interphone interphone = new Interphone();

                interphone.setTx_power(a);

                reqcb.respone(Opcode.NOYIFY,interphone);



            } else if (opCode == 14) {

                if(objCode == 0){


                    byte a = is_date[1];

                    LogUtil.i(TAG,"LCD背光开关："+a);

                }else{

                }



            } else if (opCode == 15) {

                if(objCode == 0){


                    byte a = is_date[1];

                    LogUtil.i(TAG,"按键音消除开关："+a);

                }else{

                }



            } else if (opCode == 16) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"尾音消除开关："+a);

                Interphone interphone = new Interphone();

                interphone.setStop_hite(a);

                reqcb.respone(Opcode.NOYIFY,interphone);



            } else if (opCode == 17) {

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy( is_date, 1,data, 0, data.length);

                    long a = toInt(data);

                    LogUtil.i(TAG,"DTMF码："+a);


                }else{

                }



            } else if (opCode == 18) {

                if(objCode == 0){


                    byte a = is_date[1];

                    LogUtil.i(TAG,"wifi 开关："+a);

                }else{

                }



            } else if (opCode == 19) {

                if(objCode == 0){


                    if(is_date[1] != 0){

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy( is_date, 1,data, 0, data.length);

                        String a = new String(data);

                        LogUtil.i(TAG,"wifi ssid："+a);

                    }

                }else{

                }



            } else if (opCode == 20) {

                if(objCode == 0){

                    if(is_date[1] != 0){

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy(is_date, 1,data, 0, data.length);

                        String a = new String(data);

                        LogUtil.i(TAG,"wifi 密码："+a);

                    }


                }else{

                }



            } else if (opCode == 21) {

                if(objCode == 0){

                    byte a = is_date[1];

                    LogUtil.i(TAG,"恢复出厂设置开关："+a);


                }else{

                }



            } else if (opCode == 22) {


                String a = null;

                try {

                    if(objCode == 0){

                        for(byte data : is_date){

                            grout_data.add(data);
                        }


                    }else{

                        byte[] adata = new byte[14];

                        System.arraycopy(returnData, 1,adata, 0,  14);

                        byte[] bdata = new byte[returnData.length-14];

                        System.arraycopy(returnData, 14,bdata, 0,  returnData.length-14);

                        a = URLDecoder.decode( new String(adata)+new String(adata), "GBK");//new String(adata,"ISO-8859-1")+new String(adata,"ISO-8859-1")
                        //Log.d("aaaaaaaa","a111111111111111："+a);
                        //Log.d("bbbbbbbbbbbbbbbbb","adata55555555555:     "+ EncryptUtil.bytesToHexString(adata)+ "  "+Arrays.toString(adata)+" "+adata.length);
                        //Log.d("bbbbbbbbbbbbbbbbb","adata66666666666:     "+ EncryptUtil.bytesToHexString(bdata)+ "  "+Arrays.toString(bdata)+" "+bdata.length);
                    }

                } catch (Exception e) {//UnsupportedEncodingException

                    e.printStackTrace();

                }

                LogUtil.i(TAG,"群名："+a);

                if(a != null){

                    Interphone interphone = new Interphone();

                    ImCode imCode = new ImCode();
                    imCode.setName(a);
                    interphone.setImCode(imCode);
                    Log.d("aaaaaaaa","a222222222222："+a);
                //    reqcb.respone(Opcode.NOYIFY,interphone);

                }

            } else if (opCode == 23) {

                long a = 0;

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0, data.length);

                    a = toInt(data);

                }else{

                    byte[] data = new byte[returnData.length - 1];

                    System.arraycopy(returnData, 1,data, 0, data.length);

                    a = toInt(data);

                }

                LogUtil.i(TAG,"群biss id："+a);

                Interphone interphone = new Interphone();

                ImCode imCode = new ImCode();
                imCode.setId(a);

                interphone.setImCode(imCode);

                reqcb.respone(Opcode.NOYIFY,interphone);



            } else if (opCode == 24) {


                String a = null;

                if(objCode == 0){

                    if(is_date[1] != 0){

                        byte[] data = new byte[is_date.length - 1];

                        System.arraycopy(is_date, 1,data, 0, data.length);

                        a = new String(data);
                    }

                }else{

                    byte[] data = new byte[returnData.length - 1];

                    System.arraycopy(returnData, 1,data, 0, data.length);

                    a = new String(data);

                }

                if(a != null){

                    LogUtil.i(TAG,"群密码："+a);

                    Interphone interphone = new Interphone();

                    ImCode imCode = new ImCode();
                    imCode.setUuid(a);

                    interphone.setImCode(imCode);

                    reqcb.respone(Opcode.NOYIFY,interphone);

                }




            } else if (opCode == 25) {

                long a = 0;

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0, data.length);

                    a = toInt(data);

                }else{

                    byte[] data = new byte[returnData.length - 1];

                    System.arraycopy(returnData, 1,data, 0, data.length);

                    a = toInt(data);

                }

                LogUtil.i(TAG,"设备用户ID："+a);

                Interphone interphone = new Interphone();

                interphone.setUserId(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 26) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"RF开关："+a);

                Interphone interphone = new Interphone();

                interphone.setRf(a);

                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 27) {

                short a = 0;

                if(objCode == 0){

                    byte[] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 1,data, 0, data.length);

                    short s0 = (short) (data[0] & 0xff);// 最低位
                    short s1 = (short) (data[1] & 0xff);

                    s1 <<= 8;

                    a = (short) (s0 | s1);

                }
                Log.d("resultString","name:     "+ EncryptUtil.bytesToHexString(is_date)+ "  "+Arrays.toString(is_date)+is_date[2]);
                Interphone interphone = new Interphone();
                interphone.setVolt((short)(Integer.parseInt(EncryptUtil.bytesToHexString(is_date).substring(3,5))*100));//a
                interphone.setPower(is_date[2]+"");
                reqcb.respone(Opcode.NOYIFY,interphone);


            } else if (opCode == 28) {

                byte a = 0;

                if(objCode == 0){

                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"发送提示音开关："+a);

                Interphone interphone = new Interphone();

                interphone.setTx_hite(a);

                reqcb.respone(Opcode.NOYIFY,interphone);

            }else if (opCode == 29) {

                byte a = 0;

                if(objCode == 0){
                    //读操作
                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"灯闪时间："+a);

                Interphone interphone = new Interphone();

                interphone.setDisconnec_hite(a);

                reqcb.respone(Opcode.NOYIFY,interphone);

            }else if (opCode == 30) {

                byte a = 0;

                if(objCode == 0){
                    //读操作
                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"灯闪时间："+a);

                Interphone interphone = new Interphone();

                interphone.setDisconnec_hite(a);

                reqcb.respone(Opcode.NOYIFY,interphone);

            }else if (opCode == 31) {

                byte a = 0;

                if(objCode == 0){
                    //读操作
                    a = is_date[1];

                }else{

                    a = returnData[1];

                }

                LogUtil.i(TAG,"实时播放："+a);

                Interphone interphone = new Interphone();

                interphone.setIsplay(a);

                reqcb.respone(Opcode.NOYIFY,interphone);

            }

        }else if(code == Objcode.MESSAGE){


            byte a = (byte) (is_date[3] & 0x80);

            byte b = (byte) (is_date[3] & 0x7F);
            LogUtil.i(TAG,"MESSAGE："+b+"-"+a);
            if(a == 0){
                //读操作

                if(b == 1){

                    LogUtil.i(TAG,kkl+++"时间："+System.currentTimeMillis()+"接收到语音包："+Arrays.toString(is_date));

                    byte[] uderId = new byte[4];

                    uderId[0] = is_date[6];
                    uderId[1] = is_date[7];
                    uderId[2] = is_date[8];
                    uderId[3] = is_date[9];

                    byte[] prackId = new byte[4];

                    prackId[0] = is_date[10];
                    prackId[1] = is_date[11];
                    prackId[2] = is_date[12];
                    prackId[3] = is_date[13];






                    /*if(toInt(uderId)  == userID && toInt(prackId) == prackID){

                    }else{

                        new_yes_voice_data(userID,prackID);

                    }*/

                    byte[] bts=new byte[is_date.length - 15];

                    int k=0;
                    //循环数据剔除 AT 传输协议
                    for(int i=14;i<is_date.length-1;i++){
                        bts[k++]=is_date[i];
                    }




                    /*for(byte bxs : bts){
                        voice_data.add(bxs);
                    }
                    LogUtil.i(TAG,"语音数据:"+bts.length);
                    LogUtil.i(TAG,"语音数据:"+voice_data.size());
*/
                    userID = toInt(uderId);
                    prackID = toInt(prackId);

                //    startVoiceIntent(uderId,prackId);

                    Ble_message ble_message = new Ble_message();

                    ble_message.setMessage(Base64.encodeToString(bts, Base64.DEFAULT));
                    ble_message.setTime(System.currentTimeMillis());
                    ble_message.setType(MsgResponse.Voice);
                    ble_message.setUserId(userID);
                    ble_message.setId(prackID);

                    reqcb.respone(Opcode.MESG,ble_message);


                } else if (b == 2) {

                    byte crc = is_date[is_date.length - 1];

                    byte [] data = new byte[is_date.length - 1];

                    System.arraycopy(is_date, 0,data, 0,  data.length);

                    if (crc == (byte) (CRC.crc8(data, data.length) & 0xff)) {
                        LogUtil.i(TAG, "CRC校验通过，反馈信息，获取结果"+System.currentTimeMillis());
                        //CRC校验成功，反馈信息，获取结果
                        startService();

                        byte[] c ={1};

                        ble_service.sendWriteByte(Objcode.MESSAGE,ATcommand.at_cmd_versions_crc((byte) 2, c));

                    } else {
                        //校验失败，返回 no
                        LogUtil.i(TAG,"校验失败返回no");
                        byte[] c = {0};
                        ble_service.sendWriteByte(Objcode.MESSAGE,ATcommand.at_cmd_versions_crc((byte) 2, c));

                    }

                    byte[] userId = new byte[4];

                    userId[0] = is_date[6];
                    userId[1] = is_date[7];
                    userId[2] = is_date[8];
                    userId[3] = is_date[9];

                    byte[] prickId = new byte[4];

                    prickId[0] = is_date[10];
                    prickId[1] = is_date[11];
                    prickId[2] = is_date[12];
                    prickId[3] = is_date[13];

                    byte[] bxs = new byte[is_date.length - 14];

                    int k = 0;
                    for(int i=14 ;i < is_date.length - 1;i++){

                        bxs[k++] = is_date[i];
                    }

                    reqcb.respone(Opcode.MESG,setFile_text(toInt(userId),toInt(prickId),bxs));

                } else if (b == 3) {

                    LogUtil.i(TAG,"收到userID,prckerID");

                    /*if(ble_service != null){

                        MyApplicatoin.IS_BLE_VOICE = false;

                        ble_service.write = true;

                    }*/

                    byte[] psid = new byte[4];

                    psid[0] = is_date[10];
                    psid[1] = is_date[11];
                    psid[2] = is_date[12];
                    psid[3] = is_date[13];

                    boolean isk = false;



                    for(long l : MyApplicatoin.ls){

                        if(l == toInt(psid)){
                            isk = true;
                        }

                    }
                    if(!isk){
                        MyApplicatoin.ls.add(toInt(psid));
                    }

                    LogUtil.i(TAG,"中断数据:");

                    /*try {
                        ble_service.write = true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/


                            /*byte[] ble = new byte[6];
                            byte[] phone = new byte[6];

                            System.arraycopy(bytes,7,ble,0,6);

                            if(ble_service.writebyte != null && ble_service.writebyte.length > 13){
                                System.arraycopy(ble_service.writebyte,7,phone,0,6);

                                if(Arrays.equals(ble,phone)){

                                    LogUtil.i(GlobalConsts.TAG,"结束");
                                }else{
                                    LogUtil.i(GlobalConsts.TAG,"ID不相等，不结束");
                                }
                            }*/

                } else if (b == 4) {

                    LogUtil.i(TAG,"语音结束符");

                    LogUtil.i(TAG,"收到userID,prckerID");

                    byte[] userid = new byte[4];

                    userid[0] = is_date[6];
                    userid[1] = is_date[7];
                    userid[2] = is_date[8];
                    userid[3] = is_date[9];

                    byte[] psid = new byte[4];

                    psid[0] = is_date[10];
                    psid[1] = is_date[11];
                    psid[2] = is_date[12];
                    psid[3] = is_date[13];


                    Ble_message ble_message = new Ble_message();
                    ble_message.setMessage("over");
                    ble_message.setTime(System.currentTimeMillis());
                    ble_message.setId(toInt(psid));
                    ble_message.setUserId(toInt(userid));
                    ble_message.setType(Ble_message.over);

                    reqcb.respone(Opcode.MESG,ble_message);

                }

            }else{

                set_Result(is_date[is_date.length-2]);

            }

        }else if(code == Objcode.FIRMWARE){

            LogUtil.i(TAG, "FIRMWARE：");

            byte a = (byte) (is_date[3] & 0x80);

            byte b = (byte) (is_date[3] & 0x7F);


            if(a == 0){
                //读操作

                if(b == 1){

                    byte crc = is_date[is_date.length-1];

                    byte [] data = new byte[is_date.length - 1];

                    System.arraycopy( is_date, 0,data, 0, data.length);

                    if (crc == (byte) (CRC.crc8(data, data.length) & 0xff)) {
                        LogUtil.i(TAG, "CRC校验通过，反馈信息，获取结果"+System.currentTimeMillis());
                        //CRC校验成功，反馈信息，获取结果
                        startService();

                        byte[] c ={1};

                        ble_service.sendWriteByte(Objcode.FIRMWARE,ATcommand.at_cmd_versions_crc((byte) 1, c));

                        setUpdate(dataCode,is_date);

                    } else {
                        //校验失败，返回 no
                        LogUtil.i(TAG,"校验失败返回no");
                        byte[] c = {0};
                        ble_service.sendWriteByte(Objcode.FIRMWARE,ATcommand.at_cmd_versions_crc((byte) 1, c));

                    }

                }

            }else{

                set_Result(is_date[is_date.length-2]);

            }

        }

    }

    /**
     *
     * 生成AT+模式的数据并发送
     *
     * */
    public void send_Command(Command cmd){

        try {
            int objcode = cmd.getObjcode();


            //    LogUtil.i(TAG,"dataCode:"+dataCode);


            if(objcode == Objcode.FILE_ONE){

             //   ble_service.resend_file(set_file_command(cmd));

            }else if(objcode == Objcode.FILE_PART){

             //  ble_service.resend_file(Objcode.FRIDY,file_info(dataCode,cmd));

            }else if(objcode == Objcode.FILE_TEXT){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,file_chat(dataCode,cmd));
                }else{
                    ble_service.resend_file(Objcode.MESSAGE,file_chat(dataCode,cmd));
                }



            }else if(objcode == Objcode.FILE_VOICE){


                MsgResponse msg = (MsgResponse) cmd.getValue();


                if(dataCode == ble_service.versions_one){

                    byte [] voiceData = ATcommand.at_set_file_user_chat(Opcode.SET, Objcode.FILE_VOICE,msg.getUserId(),msg.getPacketID(),msg.getVoice());

                    BluetoothLeService.le_service.sendWriteByte(Objcode.FRIDY,voiceData);

                }else{

                    byte prica = (byte) (1 | 0x80);

                    byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,msg.getUserId(),msg.getPacketID(),msg.getVoice());

                    //   BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);
                }



            }else if(objcode == Objcode.FILE_RESULT){

                ble_service.sendWriteByte(Objcode.FRIDY,ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),(byte) cmd.getValue()));

            }else if(objcode == Objcode.FILE_IMG){

            //    ble_service.resend_file(Objcode.FRIDY,file_chat(cmd));

            }else if(objcode == 6){

            }else if(objcode == Objcode.FILE_BASUC){


            }else if(objcode == Objcode.BLE_GATT_AT1846S){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_file_command(dataCode,cmd));
                }else{
                    ble_service.resend_file(Objcode.DRIVER,set_file_command(dataCode,cmd));
                }


            }else if(objcode == Objcode.BLE_GATT_CC1101){

                //    ble_service.resend_file(set_file_command(cmd));

            }else if(objcode == Objcode.BLE_GATT_ESP8266){

                //   ble_service.resend_file(set_file_command(cmd));

            }else if(objcode == Objcode.BLE_GATT_CC2540){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_file_command(dataCode,cmd));

                }else{

                    ble_service.resend_file(Objcode.COMMON,set_file_command(dataCode,cmd));
                }




            }else if(objcode == Objcode.BLE_GATT_BK8000L){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_file_command(dataCode,cmd));

                }else{

                    ble_service.resend_file(Objcode.DRIVER,set_file_command(dataCode,cmd));

                }


            }else if(objcode == Objcode.BLE_GATT_LCD){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_file_command(dataCode,cmd));

                }else{

                    ble_service.resend_file(Objcode.DRIVER,set_file_command(dataCode,cmd));
                }


            }else if(objcode == Objcode.BLE_GATT_KEY){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_file_command(dataCode,cmd));

                }else{

                    ble_service.resend_file(Objcode.DRIVER,set_file_command(dataCode,cmd));
                }

            }else if(objcode == Objcode.BLE_GATT_APRS){

                List<byte[]> bxs = set_file_command(dataCode,cmd);

                if(dataCode == ble_service.versions_one){

                    for(byte[] bs : bxs){
                        ble_service.sendWriteByte(Objcode.FRIDY,bs);
                    }

                }else{

                    for(byte[] bs : bxs){
                        ble_service.sendWriteByte(Objcode.DRIVER,bs);
                    }

                }



            }else if(objcode == Objcode.BLE_IM){

                if(dataCode == ble_service.versions_one){

                    ble_service.resend_file(Objcode.FRIDY,set_ble_im(dataCode,cmd));

                }else{

                    ble_service.resend_file(Objcode.DRIVER,set_ble_im(dataCode,cmd));

                }


            }else if(objcode == Objcode.BLE_INTERPHONE){


                if(cmd.getPrica() == Objcode.BLE_INTERPHONE_FIRMWARE){

                    /*byte[] param = new byte[1];

                    param[0] = cmd.getPrica();

                    ble_service.sendWriteByte(ATcommand.at_cmd(cmd.getOpcode(),cmd.getObjcode(),param));*/

                }else if(cmd.getPrica() == Objcode.BLE_INTERPHONE_MODE || cmd.getPrica() == Objcode.BLE_INTERPHONE_BLUETOOTH){

                    List<byte[]> sen_date = new ArrayList<byte[]>();

                    byte[] value = new byte[1];

                    value[0] = (byte)cmd.getValue();

                    if(dataCode == BluetoothLeService.versions_one){

                        if(cmd.getOpcode() == Opcode.GET){

                            sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                            ble_service.resend_file(Objcode.FRIDY,sen_date);

                        }else{

                            value[0] = (byte)cmd.getValue();

                            sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                            ble_service.resend_file(Objcode.FRIDY,sen_date);
                        }

                    }else{

                        if(cmd.getPrica() == Objcode.BLE_INTERPHONE_MODE){

                            byte obj = (byte) (8 | 0x80);

                            sen_date.add(ATcommand.at_cmd_versions(obj,value));

                            ble_service.resend_file(Objcode.DRIVER,sen_date);

                        }else if(cmd.getPrica() == Objcode.BLE_INTERPHONE_BLUETOOTH){

                            byte obj = (byte) (12 | 0x80);

                            sen_date.add(ATcommand.at_cmd_versions(obj,value));

                            ble_service.resend_file(Objcode.DRIVER,sen_date);

                        }


                    }

                }else if(cmd.getPrica() == Objcode.BLE_INTERPHONE_USERID){

                    if(dataCode == ble_service.versions_one){

                        ble_service.resend_file(Objcode.FRIDY,set_ble_interphone(dataCode,cmd));

                    }else{

                        ble_service.resend_file(Objcode.DRIVER,set_ble_interphone(dataCode,cmd));
                    }

                }else if(cmd.getPrica() == Objcode.BLE_INTERPHONE_FACTIRY_RESET){

                    List<byte[]> sen_date = new ArrayList<byte[]>();

                    byte[] value = new byte[1];

                    value[0] = (byte)cmd.getValue();

                    if(dataCode == BluetoothLeService.versions_one){

                        if(cmd.getOpcode() == Opcode.GET){

                            sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                            ble_service.resend_file(Objcode.FRIDY,sen_date);

                        }else{

                            value[0] = (byte)cmd.getValue();

                            sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                            ble_service.resend_file(Objcode.FRIDY,sen_date);
                        }

                    }else{

                        byte obj = (byte) (11 | 0x80);
                        sen_date.add(ATcommand.at_cmd_versions(obj,value));
                        ble_service.resend_file(Objcode.COMMON,sen_date);
                    }

                }/*else if (cmd.getPrica() == Objcode.BLE_INTERPHONE_DEVICE_RESET) {//重置
                    List<byte[]> sen_date = new ArrayList<byte[]>();
                    byte obj = (byte) (13 | 0x80);
                    sen_date.add(ATcommand.at_cmd_versions(obj,cmd.getValue().toString().getBytes()));
                    ble_service.resend_file(Objcode.COMMON, sen_date);
                }*/
            }else if(objcode == Objcode.BLE_SCNN){
                if(ble_service != null){
                    LogUtil.i(TAG,"ble_service != null");

                    if(cmd.getPrica() == 1){
                        ble_service.startLeScan();
                    }else if(cmd.getPrica() == 2){
                        ble_service.closeBluetooth();
                    }else if(cmd.getPrica() == 3){
                        ble_service.scanLeScan();
                    }else{
                        ble_service.stopLeScan();
                    }

                }else{
                    LogUtil.i(TAG,"ble_service == null");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        ExceptionUtil.handleException(e);
                    }
                    if(ble_service != null){
                        LogUtil.i(TAG,"线程等待启动service");
                        ble_service.startLeScan();
                    }
                }
            }else if(objcode == Objcode.BLE_STATECHANGE){
                if(ble_service != null){

                }
            }else if(objcode == Objcode.BLE_CONNECT){
                if(ble_service != null){

                    if(cmd.getOpcode() == Opcode.SET){

                        ble_service.connect(Opcode.SET,(String) cmd.getValue());

                    }else{

                        ble_service.connect(Opcode.GET,(String) cmd.getValue());

                    }


                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * 文件类型
     *
     * */
    private static List<byte[]> file_info(byte code,Command cmd){

        File file = new File((String) cmd.getValue());
        BufferedInputStream input = null;
        short k = 0;
        byte dataOne[] = new byte[1024];
        try {
            if(file.exists()){
                FileInputStream fos = new FileInputStream(file);
                input = new BufferedInputStream(fos);// 输入缓冲流
                int lenth=0;
                if (file.exists() && file.isFile()){
                    LogUtil.i(TAG,"FILE.lenth="+file.getAbsolutePath());
                    lenth=(int)file.length();
                }
                int bytesRead = 0;

                List<byte[]> sen_date = new ArrayList<byte[]>();

                if(code == BluetoothLeService.versions_one){

                    sen_date.add(ATcommand.at_set_file_info(file.getName(),"1.0",lenth));

                    while ((bytesRead = input.read(dataOne)) != -1) {
                        LogUtil.i(TAG,"数量="+k);
                        sen_date.add(ATcommand.at_send_order(Objcode.FILE_PART,k++, (short) bytesRead, dataOne));
                    }
                    return sen_date;

                }else{

                    byte prica = (byte) (2 | 0x80);

                    sen_date.add(ATcommand.at_set_file_info_versions(prica,file.getName(),"1.0",lenth));

                    byte obj = (byte) (3 | 0x80);

                    while ((bytesRead = input.read(dataOne)) != -1) {
                        LogUtil.i(TAG,"数量="+k);
                        sen_date.add(ATcommand.at_send_order_versions(obj,k++, (short) bytesRead, dataOne));
                    }
                    return sen_date;

                }

            }

        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        } finally {
            //关闭 BufferedInputStream
            try {
                if (input != null)
                    input.close();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
        return null;
    }

    /**
     *
     * 通讯类型 带包ID 用户ID
     *
     * */
    private static List<byte[]> file_chat(byte code,Command cmd){
        List<byte[]> sen_date = new ArrayList<byte[]>();
        int objcode = cmd.getObjcode();

        MsgResponse msg = (MsgResponse) cmd.getValue();

        if(objcode == Objcode.FILE_TEXT){

            if(code == BluetoothLeService.versions_one){

                sen_date.add(ATcommand.at_set_file_user_chat(cmd.getOpcode(),cmd.getObjcode(),msg.getUserId(),msg.getPacketID(),msg.getContent().getBytes()));

            }else{

                byte prica = (byte) (2 | 0x80);

                sen_date.add(ATcommand.at_set_file_user_chat_versions(prica,msg.getUserId(),msg.getPacketID(),msg.getContent().getBytes()));

            }

            return sen_date;

        }

        return null;
    }


    /**
     * 命令类型
     * */
    private static List<byte[]> set_file_command(byte code,Command cmd){

        List<byte[]> sen_date = new ArrayList<byte[]>();
        int opcode = cmd.getOpcode();
        int objcode = cmd.getObjcode();

        LogUtil.i(TAG,"Commadn_biz:set_file_command:"+opcode+","+objcode);

        if(objcode == Objcode.FILE_ONE){
            //发送文件的名字，版本，大小
        //    Files files = (Files) cmd.getValue();
        //    sen_date.add(ATcommand.at_set_file_info(files.getName(),files.getVersion(),files.getSize()));

            return sen_date;

        }else if(objcode == Objcode.BLE_GATT_AT1846S){                 // AT1846S

            int prica = cmd.getPrica();

            if(prica == Objcode.BLE_GATT_AT1846S_BW || prica ==Objcode.BLE_GATT_AT1846S_RF
                    || prica ==Objcode.BLE_GATT_AT1846S_TxPower || prica ==Objcode.BLE_GATT_AT1846S_SQ
                    || prica == Objcode.BLE_GATT_AT1846S_VOX|| prica == Objcode.BLE_GATT_AT1846S_TxHite
                    || prica == Objcode.BLE_GATT_AT1846S_StopHite || prica == Objcode.BLE_GATT_AT1846S_DisconnecHite
                    || prica == Objcode.BLE_GATT_AT1846S_wStart || prica == Objcode.BLE_GATT_AT1846S_Isplay
                    || prica == Objcode.BLE_GATT_AT1846S_FindDevice|| prica == Objcode.BLE_GATT_AT1846S_netDisconnect
            ){


                if(code == BluetoothLeService.versions_one){

                    if(opcode == Opcode.SET){

                        byte[] value = new byte[1];

                        value[0] = (byte)cmd.getValue();

                        sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                        return sen_date;

                    }else if(opcode == Opcode.GET){

                        sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                        return sen_date;

                    }

                }else{

                    byte[] value = new byte[1];

                    value[0] = (byte)cmd.getValue();

                    if(prica == Objcode.BLE_GATT_AT1846S_BW){

                        byte obj = (byte) (1 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_SQ){

                        byte obj = (byte) (4 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_VOX){

                        byte obj = (byte) (5 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_RF){

                        byte obj = (byte) (26 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_TxPower){

                        byte obj = (byte) (13 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_TxHite){

                        byte obj = (byte) (28 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_StopHite){

                        byte obj = (byte) (16 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_DisconnecHite){

                        byte obj = (byte) (29 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_wStart){

                        byte obj = (byte) (30 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_Isplay){

                        byte obj = (byte) (31 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_FindDevice){

                        byte obj = (byte) (32 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }else if(prica ==Objcode.BLE_GATT_AT1846S_netDisconnect){

                        byte obj = (byte) (33 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }


                }



            }else if(prica == Objcode.BLE_GATT_AT1846S_FREQ){               //      FREQ 对讲机频率


                byte[] value = new byte[4];

                int v1 = (int)cmd.getValue();

                LogUtil.i(TAG,v1);

                value[0] = (byte) (v1  & 0xff);
                value[1] = (byte) (v1 >> 8);
                value[2] = (byte) (v1 >> 16);
                value[3] = (byte) (v1 >> 24);

                if(code == BluetoothLeService.versions_one){

                    if(opcode == Opcode.SET){

                        sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                        return sen_date;

                    }else if(opcode == Opcode.GET){

                        sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                        return sen_date;
                    }

                }else{

                    byte obj = (byte) (2 | 0x80);

                    sen_date.add(ATcommand.at_cmd_versions(obj,value));

                    return sen_date;


                }


            }else if(prica == Objcode.BLE_GATT_AT1846S_RxCode || prica == Objcode.BLE_GATT_AT1846S_TxCode){

                byte[] value = new byte[2];

                int v1 = (int)cmd.getValue();

                value[0] = (byte) (v1  & 0xff);
                value[1] = (byte) (v1 >> 8);

                if(code == BluetoothLeService.versions_one){

                    sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                    return sen_date;

                }else{

                    byte[] vv ={(byte) (v1  & 0xff)};

                    if(prica == Objcode.BLE_GATT_AT1846S_RxCode){

                        byte obj = (byte) (6 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,vv));

                        return sen_date;

                    }else if(prica == Objcode.BLE_GATT_AT1846S_TxCode){

                        byte obj = (byte) (7 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,vv));

                        return sen_date;

                    }

                }

            }

        }else if(objcode == Objcode.BLE_GATT_CC2540){

            int prica = cmd.getPrica();

            if(prica == Objcode.BLE_GATT_CC2540_BROADCAST || prica == Objcode.BLE_GATT_CC2540_ADINV || prica == Objcode.BLE_GATT_CC2540_NEINV){               //      POWER 电源模块开关

                if(opcode == Opcode.SET){

                    byte[] value = new byte[1];

                    value[0] = (byte)cmd.getValue();

                    sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                    return sen_date;

                }else if(opcode == Opcode.GET){

                    sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                    return sen_date;

                }

            }else if(prica == Objcode.BLE_GATT_CC2540_NAME){

                if(code == BluetoothLeService.versions_one){

                    if(opcode == Opcode.SET){

                        sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),cmd.getValue().toString().getBytes()));

                        return sen_date;

                    }else if(opcode == Opcode.GET){

                        sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                        return sen_date;
                    }

                }else{


                    byte obj = (byte) (5 | 0x80);

                    sen_date.add(ATcommand.at_cmd_versions(obj,cmd.getValue().toString().getBytes()));

                    return sen_date;

                }

            }

        }else if(objcode == Objcode.BLE_GATT_BK8000L || objcode == Objcode.BLE_GATT_LCD || objcode == Objcode.BLE_GATT_KEY){

            if(opcode == Opcode.SET){

                byte[] value = new byte[1];

                value[0] = (byte)cmd.getValue();

                sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                return sen_date;

            }else if(opcode == Opcode.GET){

                sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                return sen_date;

            }

        }else if(objcode == Objcode.BLE_GATT_APRS){

            int prica = cmd.getPrica();

            if(prica == Objcode.BLE_GATT_APRS_POWER){

                if(opcode == Opcode.SET){

                    byte[] value = new byte[1];

                    value[0] = (byte)cmd.getValue();

                    sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                    return sen_date;

                }else if(opcode == Opcode.GET){

                    sen_date.add(ATcommand.at_get_file_result(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica()));

                    return sen_date;

                }

            }else if(prica == Objcode.BLE_GATT_APRS_POSITION){

                if(opcode == Opcode.SET){

                    Location location = (Location) cmd.getValue();

                    byte[] value = Locations.new_byte_Locations((int)location.getTime(),location);

                    if(code == BluetoothLeService.versions_one){

                        sen_date.add(ATcommand.at_set_file_part(cmd.getOpcode(),cmd.getObjcode(),cmd.getPrica(),value));

                        return sen_date;


                    }else{

                        byte obj = (byte) (9 | 0x80);

                        sen_date.add(ATcommand.at_cmd_versions(obj,value));

                        return sen_date;

                    }


                }

            }

        }

        return null;
    }

    public static List<byte[]> set_ble_im(byte code, Command ble_im) {

        List<byte[]> sen_date = new ArrayList<byte[]>();

        ImCode imCode = (ImCode) ble_im.getValue();

        String imName = imCode.getName();

        long mxId = imCode.getId();

        String UUID = imCode.getUuid();

        String name = ","+imName+","+UUID;

        LogUtil.i(TAG,mxId+name);

        byte[] data = name.getBytes();


        byte[] cmd = new byte[4 + data.length];

        int i = 0;

        cmd[i++] = (byte) (mxId & 0xff);
        cmd[i++] = (byte) (mxId >> 8);
        cmd[i++] = (byte) (mxId >> 16);
        cmd[i++] = (byte) (mxId >> 24);

        for (int j = 0; j < data.length; j++, i++) {
            cmd[i] = data[j];
        }


        i = 0;

        if(code == BluetoothLeService.versions_one){

            sen_date.add(ATcommand.at_cmd(ble_im.getOpcode(),ble_im.getObjcode(),cmd));

            return sen_date;


        }else{

            byte objName = (byte) (22 | 0x80);

            byte[]  nameData = new byte[32];

            byte[] databytes = new byte[14];

            try {

                databytes = imName.getBytes("GBK");//imName.getBytes("ISO-8859-1")
               // Log.d("bbbbbbbbbbbbbbbbb","adata9999999999999:     "+ EncryptUtil.bytesToHexString(databytes)+ "  "+Arrays.toString(databytes)+" "+databytes.length);
            } catch (Exception e) {
                e.printStackTrace();
            }


            for(int k = 0; k < 32; k++){

                if( k >= databytes.length){

                    nameData[k] = 0;

                }else{

                    nameData[k] = databytes[k];

                }

            }

            sen_date.add(ATcommand.at_cmd_versions(objName,nameData));

            byte objId = (byte) (23 | 0x80);

            byte[] id = new byte[4];

            id[i++] = (byte) (mxId & 0xff);
            id[i++] = (byte) (mxId >> 8);
            id[i++] = (byte) (mxId >> 16);
            id[i++] = (byte) (mxId >> 24);

            sen_date.add(ATcommand.at_cmd_versions(objId,id));

            byte objPassWold = (byte) (24 | 0x80);

            sen_date.add(ATcommand.at_cmd_versions(objPassWold,UUID.getBytes()));

            //Log.d("bbbbbbbbbbbbbbbbb","adata232343435445454:     "+ EncryptUtil.bytesToHexString(nameData)+ "  "+Arrays.toString(nameData)+" "+nameData.length);
            return sen_date;

        }
    }

    public static List<byte[]> set_ble_interphone(byte code, Command command) {


        List<byte[]> sen_date = new ArrayList<byte[]>();

        long id = (long) command.getValue();

        if(code == BluetoothLeService.versions_one){

            int i = 0;
            byte[] param = new byte[5];

            param[i++] = command.getPrica();

            param[i++] = (byte) (id & 0xff);
            param[i++] = (byte) (id >> 8);
            param[i++] = (byte) (id >> 16);
            param[i++] = (byte) (id >> 24);

            sen_date.add(ATcommand.at_cmd(command.getOpcode(),command.getObjcode(),param));

            return sen_date;

        }else{

            int i = 0;

            byte[] param = new byte[4];

            param[i++] = (byte) (id & 0xff);
            param[i++] = (byte) (id >> 8);
            param[i++] = (byte) (id >> 16);
            param[i++] = (byte) (id >> 24);

            byte obj = (byte) (25 | 0x80);

            sen_date.add(ATcommand.at_cmd_versions(obj,param));

            return sen_date;
        }

    }




    /**------------------------------------------------------------------------------------------------
     *
     * BLE 解析层
     *
     * */


    public void set_dal_data(byte[] bytes, Byte aByte) {




        int isobjcode = Integer.parseInt(String.format("%02X", bytes[4]), 16);

        int isopcode = Integer.parseInt(String.format("%02X", bytes[3]), 16);

    //    LogUtil.i(GlobalConsts.TAG, "isopcode="+isopcode);

        if (isopcode == Opcode.RESULT && isobjcode == Objcode.FILE_RESULT) {

            setFile_result(bytes);

        }else{

            if(isobjcode != Objcode.FILE_VOICE){

                if(isobjcode != Objcode.BLE_GATT_APRS){

                    if(is_crc(ble_service,bytes,aByte,isobjcode)){

                        if (isobjcode == Objcode.FILE_ONE) {

                            setUpdate(dataCode,bytes);

                            //   reqcb.respone(Objcode.FILE_INFO,Objcode.FILE_INFO,setFile_info(bytes));

                        } else if (isobjcode == Objcode.FILE_PART) {

                            setFile_part(bytes);

                        } else if (isobjcode == Objcode.FILE_TEXT) {


                            byte[] userId = new byte[4];

                            userId[0] = bytes[7];
                            userId[1] = bytes[8];
                            userId[2] = bytes[9];
                            userId[3] = bytes[10];

                            byte[] prickId = new byte[2];

                            prickId[0] = bytes[11];
                            prickId[1] = bytes[12];



                            byte[] bxs = new byte[bytes.length - 13];

                            int k = 0;
                            for(int i=13 ;i < bytes.length;i++){
                                bxs[k++] = bytes[i];
                            }

                            reqcb.respone(Opcode.MESG,setFile_text(toInt(userId),toInt(prickId),bxs));

                        } else if (isobjcode == Objcode.FILE_RESULT) {

                            setFile_result(bytes);

                        } else if (isobjcode == Objcode.FILE_IMG) {

                            setFile_img(bytes);

                        } else if (isobjcode == 6) {

                            setFile_order(bytes);

                        } else if (isobjcode == Objcode.FILE_BASUC) {

                            setFile_basuc(bytes);

                        } else if (isobjcode == Objcode.BLE_GATT_AT1846S) {


                            MsgResponse msg=new MsgResponse();
                            msg.setContent(new String(bytes));
                            interphone.setMsgResponse(msg);


                            LogUtil.i(TAG,"interphone:"+interphone.toString());

                        } else if (isobjcode == Objcode.BLE_GATT_CC1101) {



                        } else if (isobjcode == Objcode.BLE_GATT_ESP8266) {



                        } else if (isobjcode == Objcode.BLE_GATT_CC2540) {

                            //    interphone.setRxcode(bytes);
                            //    reqcb.respone(interphone);

                        } else if (isobjcode == Objcode.BLE_GATT_BK8000L) {

                            //    interphone.setRxcode(bytes);
                            //    reqcb.respone(interphone);

                        } else if (isobjcode == Objcode.BLE_GATT_LCD) {



                        }else if (isobjcode == Objcode.BLE_INTERPHONE) {


                            if(bytes[bytes.length - 2] == Objcode.BLE_INTERPHONE_STATE){

                                Interphone interphone = new Interphone();

                                interphone.setState(bytes[bytes.length - 1]);

                                LogUtil.i(TAG,"接收到对讲机状态");

                                reqcb.respone(Opcode.START,interphone);

                            }

                        } else if (isobjcode == Objcode.BLE_FILTER) {

                            LogUtil.i(TAG,"收到userID,prckerID");

                            if(ble_service != null){

                                MyApplicatoin.IS_BLE_VOICE = false;

                                ble_service.write = true;

                            }

                            LogUtil.i(TAG,"中断数据:");

                            /*byte[] ble = new byte[6];
                            byte[] phone = new byte[6];

                            System.arraycopy(bytes,7,ble,0,6);

                            if(ble_service.writebyte != null && ble_service.writebyte.length > 13){
                                System.arraycopy(ble_service.writebyte,7,phone,0,6);

                                if(Arrays.equals(ble,phone)){
                                    ble_service.write = true;
                                    LogUtil.i(GlobalConsts.TAG,"结束");
                                }else{
                                    LogUtil.i(GlobalConsts.TAG,"ID不相等，不结束");
                                }
                            }*/
                        }

                    }else{

                        LogUtil.i(TAG,"CRC校验不通过");
                    }

                }else{

                    //APRS

                    Ble_message ble_message = new Ble_message();

                    byte[] userID = new byte[4];

                    userID[0] = bytes[8];
                    userID[1] = bytes[9];
                    userID[2] = bytes[10];
                    userID[3] = bytes[11];

                    byte loceions[] = new byte[7];

                    loceions[0] = bytes[12];
                    loceions[1] = bytes[13];
                    loceions[2] = bytes[14];
                    loceions[3] = bytes[15];
                    loceions[4] = bytes[16];
                    loceions[5] = bytes[17];
                    loceions[6] = bytes[18];

                    LogUtil.i(TAG,"位置数据："+Arrays.toString(loceions));

                    Location location = Locations.aprs_dec_dmss(loceions);


                    if(location != null){

                        ble_message.setTime(System.currentTimeMillis());
                        ble_message.setType(Ble_message.aprs);
                        ble_message.setUserId(toInt(userID));
                        ble_message.setLatitude(location.getLatitude());
                        ble_message.setLongitude(location.getLongitude());
                        reqcb.respone(Opcode.MESG,ble_message);

                    }

                }

            }else{

            //    LogUtil.i(GlobalConsts.TAG,"收到语音数据");
                if (aByte == (byte) (CRC.crc8(bytes, bytes.length) & 0xff)) {

                    byte[] uderId = new byte[4];

                    uderId[0] = bytes[7];
                    uderId[1] = bytes[8];
                    uderId[2] = bytes[9];
                    uderId[3] = bytes[10];

                    byte[] prackId = new byte[2];

                    prackId[0] = bytes[11];
                    prackId[1] = bytes[12];

                    byte[] bts=new byte[bytes.length - 13];

                    int k=0;
                    //循环数据剔除 AT 传输协议
                    for(int i=13;i < bytes.length;i++){
                        bts[k++]=bytes[i];
                    }



                    if(isopcode != Opcode.NOYIFY){

                    /*if(voice_data.size() == 0){

                        userID = uderId;

                        prackID = prackId;

                    }*/
                        for(byte bxs : bts){
                            voice_data.add(bxs);
                        }

                        startVoiceIntent(uderId,prackId);

                    /*if(userID != null && prackID != null){

                    }*/

                    }else{

                        time.cancel();
                        time=null;
                        startVoiceIntent(uderId,prackId);
                    /*if(userID != null && prackID != null){

                    }*/

                    }

                } else {
                    //校验失败，返回 no
                    LogUtil.i(TAG, "校验失败");
                }

            }
        }


    }

    /**
     * 接收到的AT1846S消息
     *
     *
     * @param bytes*/

    private Object setBLE_AT1846S(byte[] bytes) {



        LogUtil.i(TAG,"AT1846S接收成功="+Arrays.toString(bytes));

        byte[] date = new byte[bytes.length - 8];

        int k = 0;
        for(int i = 8; i < bytes.length; i++){
            date[k++] = bytes[i];
        }
        byte prica = bytes[7];

        if(prica == Objcode.BLE_GATT_AT1846S_POWER || prica == Objcode.BLE_GATT_AT1846S_BW || prica == Objcode.BLE_GATT_AT1846S_SQ || prica == Objcode.BLE_GATT_AT1846S_VOX){

            return date;

        }else if(prica == Objcode.BLE_GATT_AT1846S_FREQ){

            double a = (double) toInt(date)/((int) Math.pow(10, 6));
            return a;
        }

        return null;
    }

    private Object setFile_GATT_CC2540(byte[] bytes) {

        LogUtil.i(TAG,"CC2540接收成功="+Arrays.toString(bytes));



        byte[] date = new byte[bytes.length - 8];

        int k = 0;
        for(int i = 8; i < bytes.length; i++){
            date[k++] = bytes[i];
        }
        byte prica = bytes[7];

        if(prica == Objcode.BLE_GATT_CC2540_BROADCAST || prica == Objcode.BLE_GATT_CC2540_ADINV || prica == Objcode.BLE_GATT_CC2540_NEINV){               //      POWER 电源模块开关

            return date;

        }else if(prica == Objcode.BLE_GATT_CC2540_NAME){

            return new String(date);
        }

        return null;
    }

    private Object setBLE_GATT_KEY(byte[] bytes) {

        LogUtil.i(TAG,"CC2540接收成功="+Arrays.toString(bytes));



        byte[] date = new byte[bytes.length - 8];

        int k = 0;
        for(int i = 8; i < bytes.length; i++){
            date[k++] = bytes[i];
        }

        return date;
    }

    private Object setBLE_GATT_BK8000L(byte[] bytes) {

        LogUtil.i(TAG,"CC2540接收成功="+Arrays.toString(bytes));



        byte[] date = new byte[bytes.length - 8];

        int k = 0;
        for(int i = 8; i < bytes.length; i++){
            date[k++] = bytes[i];
        }

        return date;

    }

    private Object setBLE_GATT_APRS(byte[] bytes) {

        LogUtil.i(TAG,"CC2540接收成功="+Arrays.toString(bytes));



        byte[] date = new byte[bytes.length - 8];

        int k = 0;

        for(int i = 8; i < bytes.length; i++){
            date[k++] = bytes[i];
        }

        byte prica = bytes[7];

        if(prica == Objcode.BLE_GATT_APRS_POWER){

            return ""+date;

        }else if(prica == Objcode.BLE_GATT_APRS_POSITION){

            byte[] latbys = new byte[date.length - 4];

            int j = 0;
            for(int i = 4;i < date.length;i++){
                latbys[j++] = date[i];
            }

            Location location = Locations.aprs_dec_dmss(latbys);
            return location;

        }

        return null;
    }

    /**
     * 基础信息。。。
     *
     *
     * @param bytes*/
    private void setFile_basuc(byte[] bytes) {


        /*List<String> list=new ArrayList<String>();
        for(byte byteChar : bytes){
            list.add(String.format("%02X ", byteChar));
        }*/

        Interphone phone = new Interphone();
        final StringBuilder stringBuilder = new StringBuilder(bytes.length);
        for(byte byteChar : bytes){
            stringBuilder.append(String.format("%02X ", byteChar));
        }

        LogUtil.i(TAG, "设备信息"+stringBuilder.toString());

        byte [] interphone = new byte[bytes.length-7];

        int j = 0;
        for(int i = 0;i < bytes.length;i++){

            if(i >= 7){
                interphone[j++] = bytes[i];
            }
        }

        int one_lenth = 0;

        int to_length = 0;
        for(int i = 0;i<interphone.length;i++){
            if(interphone[i] == 59){
                if(one_lenth == 0){
                    one_lenth = i;
                }else{
                    to_length = i;
                }

            }
        }


        LogUtil.i(TAG,one_lenth);
        LogUtil.i(TAG,to_length);

        if(one_lenth != 0){

            byte[] one  = new byte[one_lenth];

            for(int i = 0;i < one.length;i++){

                one[i] = interphone[i];
            }



            byte[] to  = new byte[to_length - (one_lenth + 1)];

            int m = 0;
            for(int i = 0;i < interphone.length;i++){

                if(i > one_lenth && i < to_length){
                    to[m++] = interphone[i];
                }
            }

            byte[] im  = new byte[interphone.length - (to_length + 1)];

            int e = 0;

            for(int i = 0;i < interphone.length;i++){

                if(i > to_length){

                    im[e++] = interphone[i];

                }
            }

            LogUtil.i(TAG,Arrays.toString(one));
            LogUtil.i(TAG,Arrays.toString(to));
            LogUtil.i(TAG,Arrays.toString(im));

            if(one[0] == Objcode.BLE_GATT_AT1846S){

                int k = 0;
                List<byte[]> gatt=new ArrayList<>();

                for(int i=1;i < one.length;i++){

                    if(44 == one[i] && k == 0){
                        k=i;
                        byte [] date = new byte[i-1];
                        System.arraycopy(one,1,date,0,i-1);
                        gatt.add(date);
                    }else if(44 == one[i] && k != 0){
                        byte [] date = new byte[i - ++k];
                        System.arraycopy(one,k,date,0,i-k);
                        gatt.add(date);
                        k=i;
                    }else if(i == one.length - 1){
                        byte [] date = new byte[i+1 - ++k];
                        System.arraycopy(one,k,date,0,i+1 - k);
                        gatt.add(date);
                    }
                }

                for(byte[] a : gatt){

                    byte ys = a[0];

                    if(ys == Objcode.BLE_GATT_AT1846S_FREQ){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);

                        phone.setFrequne((int)toInt(byte_data));

                    }else if(ys == Objcode.BLE_GATT_AT1846S_BW){

                        phone.setBw(a[1]);

                    }else if(ys == Objcode.BLE_GATT_AT1846S_SQ){

                        phone.setSq(a[1]);

                    }else if(ys == Objcode.BLE_GATT_AT1846S_VOX){

                        phone.setVox(a[1]);

                    }else if(ys == Objcode.BLE_GATT_AT1846S_TxCode){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);

                        phone.setTxcode((int)toInt(byte_data));

                    }else if(ys == Objcode.BLE_GATT_AT1846S_RxCode){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);
                        phone.setRxcode((int)toInt(byte_data));

                    }
                }



            }

            if(to[0] == Objcode.BLE_INTERPHONE){

                int k = 0;
                List<byte[]> gatt=new ArrayList<>();

                for(int i=1;i < to.length;i++){

                    if(44 == to[i] && k == 0){
                        k=i;
                        byte [] date = new byte[i-1];
                        System.arraycopy(to,1,date,0,i-1);
                        gatt.add(date);
                    }else if(44 == to[i] && k != 0){
                        byte [] date = new byte[i - ++k];
                        System.arraycopy(to,k,date,0,i-k);
                        gatt.add(date);
                        k=i;
                    }else if(i == to.length - 1){
                        byte [] date = new byte[i+1 - ++k];
                        System.arraycopy(to,k,date,0,i+1 - k);
                        gatt.add(date);
                    }
                }

                for(byte[] a : gatt){

                    byte ys = a[0];

                    if(ys == Objcode.BLE_INTERPHONE_MODE){

                        phone.setMode(a[1]);

                    }else if(ys == Objcode.BLE_INTERPHONE_NAME){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);

                        phone.setName(new String(byte_data));

                    }else if(ys == Objcode.BLE_INTERPHONE_HARD_VERSION){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);

                        phone.setHard_version(toInt(byte_data));


                    }else if(ys == Objcode.BLE_INTERPHONE_SOFT_VERSION){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);

                        phone.setSoft_version(toInt(byte_data));

                    }else if(ys == Objcode.BLE_INTERPHONE_USERID){

                        byte[] byte_data = new byte[a.length-1];

                        System.arraycopy(a,1,byte_data,0,a.length-1);


                        phone.setUserId(toInt(byte_data));

                    }else if(ys == Objcode.BLE_INTERPHONE_BLUETOOTH){

                        phone.setBluetooth(a[1]);

                    }
                }

            }
            if(im[0] == Objcode.BLE_IM){

                ImCode imCode = new ImCode();
                int k = 0;
                List<byte[]> gatt=new ArrayList<>();

                for(int i=1;i < im.length;i++){

                    if(44 == im[i] && k == 0){
                        k=i;
                        byte [] date = new byte[i-1];
                        System.arraycopy(im,1,date,0,i-1);
                        gatt.add(date);
                    }else if(44 == im[i] && k != 0){
                        byte [] date = new byte[i - ++k];
                        System.arraycopy(im,k,date,0,i-k);
                        gatt.add(date);
                        k=i;
                    }else if(i == im.length - 1){
                        byte [] date = new byte[i+1 - ++k];
                        System.arraycopy(im,k,date,0,i+1 - k);
                        gatt.add(date);
                    }
                }

                for(int i = 0;i<gatt.size();i++){

                    LogUtil.i(TAG,"数据："+Arrays.toString(gatt.get(i)));

                    if(i == 0){

                        imCode.setId(toInt(gatt.get(i)));

                    }else if(i == 1){

                        if(is_Byte(gatt.get(i))){


                            byte[] is_date = gatt.get(i);

                            int b = 0;

                            for(int l = 0;l<is_date.length;l++){

                                if(is_date[l] == 0){

                                    b = l;

                                    break;
                                }

                            }

                            String a = null;

                            if(b == 0){

                                byte[] data = new byte[is_date.length - 1];

                                System.arraycopy(is_date, 1,data, 0,  data.length);

                                a = new String(data);

                            }else{


                                byte[] data = new byte[b - 1];

                                System.arraycopy(is_date, 1,data, 0,  b-1);

                                a = new String(data);

                            }

                            LogUtil.i(TAG,"群名："+a);

                            imCode.setName(a);

                        }


                    }else if(i == 2){


                        byte[] is_date = gatt.get(i);

                        int b = 0;

                        for(int l = 0;l<is_date.length;l++){

                            if(is_date[l] == 0){

                                b = l;

                                break;
                            }

                        }

                        String a = null;

                        if(b == 0){

                            byte[] data = new byte[is_date.length - 1];

                            System.arraycopy(is_date, 1,data, 0,  data.length);

                            a = new String(data);

                        }else{


                            byte[] data = new byte[b - 1];

                            System.arraycopy(is_date, 1,data, 0,  b-1);

                            a = new String(data);

                        }

                        LogUtil.i(TAG,"群Uuid："+a);


                        imCode.setUuid(a);

                    }
                }

                phone.setImCode(imCode);

            }

        }

        LogUtil.i(TAG,phone.toString());

    //    MyApplicatoin.interphone = phone;

        reqcb.respone(Opcode.NOYIFY,phone);

        //找出分号，分割数组。
/*
        try {
            bleObjects.setName(gatt.get(0));
            bleObjects.setVersion(gatt.get(1));
            bleObjects.setChannel(gatt.get(2));
            bleObjects.setPattern(gatt.get(3));
            bleObjects.setBattery(gatt.get(4));
            MyApplicatoin.bel_Objects=bleObjects;
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }*/

     // LogUtil.i(GlobalConsts.TAG,"processinBle_basuc="+bleObjects.toString());

    }

    private boolean is_Byte(byte[] bytes) {

        boolean is_byte = false;
        for(byte s : bytes){
            if(s != 0){
                is_byte = true;
            }
        }
        return is_byte;
    }

    /**
     * AT命令
     *
     * @param bytes*/
    private void setFile_order(byte[] bytes) {

    }

    /**
     * img类型
     * @param bytes
     */
    private void setFile_img(byte[] bytes) {


        byte[] bxs = new byte[bytes.length - 15];

        int k = 0;
        for(int i = 0;i < bytes.length;i++){
            bxs[k++] = bytes[i];
        }

        if(img_data != null){

            for(byte imgbxs : bxs){

                if(img_data.length <= fire_length){
                    img_data[fire_length++] = imgbxs;
                }

            }

            if(fire_length == img_data.length){

                byte[] id = new byte[4];

                id[0] = bytes[7];
                id[1] = bytes[8];
                id[2] = bytes[9];
                id[3] = bytes[10];

                byte[] userId = new byte[4];

                userId[0] = bytes[11];
                userId[1] = bytes[12];
                userId[2] = bytes[13];
                userId[3] = bytes[14];

                try {
                    new_yes_Img_Date(id,userId,img_data);
                } catch (IOException e) {
                    ExceptionUtil.handleException(e);
                }
            }
        }


    }

    private void new_yes_Img_Date(byte[] id, byte[] userId, byte[] bxs) throws IOException {
        OutputStream out = null;

        String fileName = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/sms_img/";

        File dirFile = new File(fileName);

        if(!dirFile.exists()){
            dirFile.getParentFile().mkdirs();
            dirFile.createNewFile();
        }

        fileName += UUID.randomUUID().toString()+".jpg";
        File files = new File(fileName);
        files.delete();

        try {
            out = new FileOutputStream(files);
            out.write(bxs);
            voice_data.clear();
        } catch (IOException e) {
            ExceptionUtil.handleException(e);
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                ExceptionUtil.handleException(e);
            }
        }
        //更新ListView
        Ble_message ble_message = new Ble_message();

        ble_message.setMessage(files.getAbsolutePath());
        ble_message.setTime(System.currentTimeMillis());
        ble_message.setType(Ble_message.img);
        ble_message.setId(toInt(id));
        ble_message.setUserId(toInt(userId));

     //   reqcb.respone(Objcode.FILE_IMG,Objcode.FILE_IMG,ble_message);
    }


    /**
     * result
     * @param bytes
     */

    private void setFile_result(byte[] bytes) {
        if (ble_service != null) {
            //判断YES of NO
            int ispart_num = Integer.parseInt(String.format("%02X", bytes[bytes.length-1]), 16);
           //    LogUtil.i(GlobalConsts.TAG, "返回结果1=49,0=48.ispart_num=" + ispart_num);
            //判断固件的反馈
            if (ispart_num == 1) {
                //成功，继续发下一条
                startService();
                LogUtil.i(TAG,"反馈成功，继续发送");

            }else{
                LogUtil.i(TAG,"反馈失败，重新发送");
            }
        }

    }


    /**
     * result
     * @param bytes
     */

    private void set_Result(byte bytes) {
        if (ble_service != null) {
            //判断YES of NO
            int ispart_num = Integer.parseInt(String.format("%02X", bytes), 16);
            //    LogUtil.i(GlobalConsts.TAG, "返回结果1=49,0=48.ispart_num=" + ispart_num);
            //判断固件的反馈
            if (ispart_num == 1) {
                //成功，继续发下一条
                startService();
                LogUtil.i(TAG,"反馈成功，继续发送");

            }else{

                LogUtil.i(TAG,"反馈失败，重新发送");

            }
        }

    }

    /**
     * text类型
     * @param bytes
     */
    private Ble_message setFile_text(long userId,long prickId,byte[] bytes) {



        Ble_message ble_message = new Ble_message();

        ble_message.setMessage(new String(bytes));
        ble_message.setTime(System.currentTimeMillis());
        ble_message.setId(prickId);
        ble_message.setUserId(userId);
        ble_message.setType(Ble_message.text);

        LogUtil.i(TAG,"接收到的文本信息："+new String(bytes)+",prickid:"+prickId+",userid:"+userId);

        return ble_message;
    }

    /**
     * part类型
     * @param bytes
     */
    private void setFile_part(byte[] bytes) {

     //   reqcb.respone(Objcode.FILE_PART,Objcode.FILE_PART,bytes);

    }




    /**
     *
     * CRC 校验结果
     *
     * */
    private  Boolean is_crc(BluetoothLeService ble, byte[] bytes, Byte bys, int isobjcode) {

        if (bys == (byte) (CRC.crc8(bytes, bytes.length) & 0xff)) {
            LogUtil.i(TAG, "CRC校验通过，反馈信息，获取结果"+System.currentTimeMillis());
            //CRC校验成功，反馈信息，获取结果
            if(isobjcode == Objcode.FILE_ONE){

                ble.sendWriteByte(Objcode.FRIDY,ATcommand.at_get_file_result(Opcode.RESULT, Objcode.FILE_ONE, GlobalConsts.YES));

            }else{

                startService();
                ble.sendWriteByte(Objcode.FRIDY,ATcommand.at_get_file_result(Opcode.RESULT, Objcode.FILE_RESULT, GlobalConsts.YES));


            }

            return true;
        } else {
            //校验失败，返回 no
            LogUtil.i(TAG,"校验失败返回no");
            if(isobjcode != Objcode.FILE_ONE){
                ble.sendWriteByte(Objcode.FRIDY,ATcommand.at_get_file_result(Opcode.RESULT, Objcode.FILE_RESULT, GlobalConsts.NO));
            }

            return false;
        }
    }


    private TimerTask time;
    private Timer task = new Timer();
    private int recLen = 2;

    private void startVoiceIntent(final byte[] udersID,final byte[] pracksID) {

        if(time == null){
            try {
                recLen = 2;
                time = new TimerTask() {
                    @Override
                    public void run() {
                        recLen--;
                        if(recLen < 0){

                            if(time != null){
                                time.cancel();
                            }

                            time=null;
                            new_yes_voice_data(userID,prackID);
                        }
                    }
                };
                task.schedule(time, interval, interval);

            } catch (Exception e) {

                ExceptionUtil.handleException(e);

            }
        }else{
            recLen=2;
        }

    }


    private void new_yes_voice_data(final long userID, final long prackID) {


        try {

            if(voice_data.size() > 0){

                byte [] voice = new byte[voice_data.size()];
                for(int i = 0;i < voice_data.size();i++){
                    voice[i] = voice_data.get(i);
                }

                voice_data.clear();



                if(voice.length > voiceLenth){

                    LogUtil.i(TAG,"语音数据："+voice.length+","+userID+","+prackID);

                    Ble_message ble_message = new Ble_message();

                    ble_message.setMessage(Base64.encodeToString(voice, Base64.DEFAULT));
                    ble_message.setTime(System.currentTimeMillis());
                    ble_message.setType(MsgResponse.Voice);
                    ble_message.setUserId(userID);
                    ble_message.setId(prackID);

                    reqcb.respone(Opcode.MESG,ble_message);



                }

            }

        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
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

    private byte[] tobyte(long date){

        int i = 0;

        byte[] param = new byte[4];

        param[i++] = (byte) (date & 0xff);
        param[i++] = (byte) (date >> 8);
        param[i++] = (byte) (date >> 16);
        param[i++] = (byte) (date >> 24);

        return param;
    }


    private void startDevice(byte opCode) {

    //    LogUtil.i(TAG,"returnData:"+returnData);

        if(returnData != null){

            byte code = (byte) (returnData[0] & 0x7F);

         //   LogUtil.i(TAG,"opCode:"+opCode+",code:"+code);

            if(opCode == code){

                if(ble_service != null){

                    ble_service.condition = true;
                    ble_service.is_return = true;

                //    LogUtil.i(TAG,"BLE发送模块线程锁。反馈成功"+System.currentTimeMillis());
                }else{
                //    LogUtil.i(TAG,"BLE发送模块线程锁为空。反馈失败");
                }

            }

        }

    }

    private void startService() {

        if(returnData != null){

            if(ble_service != null && ble_service != null){

                ble_service.condition = true;
                ble_service.is_return = true;

                LogUtil.i(TAG,"BLE发送模块线程锁。反馈成功"+System.currentTimeMillis());
            }else{
                LogUtil.i(TAG,"BLE发送模块线程锁为空。反馈失败");
            }

        }



    }

    public static Command_biz dorequest(Context context, Command command, Ble_request reqcb) {

        Command_biz cmd_biz = new Command_biz(context,command,reqcb);

        return  cmd_biz;
    }

    public static respon send_cmd(Context context, final int opcode, final Command cmd) {



        final respon rsp[] = new respon[1];

        rsp[0] = null;

        Command_biz.dorequest(context,cmd, new Ble_request() {
            @Override
            public void respone(byte objcode, Object obj) {

                if(objcode == Opcode.START) {
                    if(Command_biz.notify_listenner!=null)
                    {
                        //如果是状态以及设备属性变化则传进一个对讲机的对象
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.STATE,obj);
                    }

                }else if(objcode == Opcode.ATTR){

                    if(Command_biz.notify_listenner!=null)
                    {
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.ATTR,obj);
                    }
                }else if(objcode == Opcode.MESG){
                    //如果是消息，则传进一个消息的对象
                    if(Command_biz.notify_listenner!=null)
                    {
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.MESG,obj);
                    }
                }else if(objcode == Opcode.RESULT){
                    //如果是消息，则传进一个消息的对象
                    if(Command_biz.notify_listenner!=null)
                    {
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.RESULT,obj);
                    }
                }else if(objcode == Opcode.NOYIFY){
                    //如果是消息，则传进一个消息的对象
                    if(Command_biz.notify_listenner!=null)
                    {
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.NOYIFY,obj);
                    }


                }else if(objcode == Opcode.SCAN){
                    //如果是消息，则传进一个消息的对象
                    if(Command_biz.notify_listenner!=null)
                    {
                        Command_biz.notify_listenner.onNotify(Interphone_notify_listenner.SCAN,obj);
                    }


                }

                rsp[0] = new respon();
                rsp[0].setOpcode(objcode);
                rsp[0].setObj(obj);

                // LogUtil.i(GlobalConsts.TAG,"objcode"+objcode);

            }

        });

        /*try {
            while (rsp[0] == null)

                Thread.sleep(50);

        } catch (InterruptedException e) {

            e.printStackTrace();
        }*/

        return rsp[0];

    }

    boolean isFirmware = true;

    public void setUpdate(byte dataCode, byte[] update) {

        int version = 0;
        int uuid = 0;

        byte [] version_byte = new byte[4];
        byte [] uuid_byte = new byte[4];


        int v = 0;
        int u = 0;
        /*for(int i = 0;i < update.length;i++){
            if(i >= 7 && i <= 10){
                version_byte[v++] = update[i];
            }
            if(i >= 11 && i <=14){
                uuid_byte[u++] = update[i];
            }
        }

        version = (int) (
                (version_byte[0] & 0xFF)
                | ((version_byte[1] & 0xFF)<<8)
                | ((version_byte[2] & 0xFF)<<16)
                | ((version_byte[3] & 0xFF)<<24));

        uuid = (int) (
                (uuid_byte[0] & 0xFF)
                | ((uuid_byte[1] & 0xFF)<<8)
                | ((uuid_byte[2] & 0xFF)<<16)
                | ((uuid_byte[3] & 0xFF)<<24));
*/
        LogUtil.i(TAG,"版本号："+version+",唯一标识："+uuid);


        if(isFirmware){

            isFirmware = false;

            Command cmd = new Command();

            LogUtil.i(TAG,"我要升级:时间="+System.currentTimeMillis());

            String firePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getPath()+"/data/sms_firmware/OTA.bin";

            File file = new File(firePath);

            if (file == null || !file.exists())
            {

                LogUtil.i(TAG,"文件不存在");

                return;
            }

            cmd.setValue(firePath);

            if(dataCode == ble_service.versions_one){

                ble_service.resend_file(Objcode.FRIDY,file_info(dataCode,cmd));

            }else{

                ble_service.resend_file(Objcode.FIRMWARE,file_info(dataCode,cmd));
            }

        }

    }

}
