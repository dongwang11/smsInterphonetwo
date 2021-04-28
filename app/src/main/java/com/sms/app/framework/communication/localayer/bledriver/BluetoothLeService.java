package com.sms.app.framework.communication.localayer.bledriver;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;
import android.util.SparseArray;

import com.sms.app.framework.communication.localayer.cmd.Command_biz;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.aesutil.EncryptUtil;
import com.sms.app.interphone.util.aesutil.SMSEncrypt;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.cmd.ATcommand;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.util.msnutil.Tools;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

/**
 * Created by Administrator on 2016/7/15.
 *
 * BLE相关业务服务类
 *
 */

public class BluetoothLeService extends Service {

    public static final byte versions_erro = 0;
    public static final byte versions_one = 1;
    public static final byte versions_to = 2;


    private byte code = 0;


    public static byte versions_code = versions_erro;

    //发送间隔
    private static final int interval = 13;

    //发送长度
    public static int ble_length = 20;


    //UUID 过滤 变量
    private static int smsUUID = 0xfee0;

    public static final String TAG="YanShi...Log - BluetoothLeService";

    public final static UUID UUID_DRIVER_VERSIONS_ONE = UUID.fromString("0000ffe0-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_FRIDY = UUID.fromString("0000ffe1-0000-1000-8000-00805f9b34fb");

   /* public final static UUID UUID_DRIVER_VERSIONS_TO = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_COMMON = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_DRIVER = UUID.fromString("0000fee2-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_MESSAGE = UUID.fromString("0000fee3-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_FIRMWARE = UUID.fromString("0000fee4-0000-1000-8000-00805f9b34fb");*/

    public final static UUID UUID_DRIVER_VERSIONS_TO = UUID.fromString("0000fee0-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_COMMON = UUID.fromString("0000fee1-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_DRIVER = UUID.fromString("0000fee2-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_MESSAGE = UUID.fromString("0000fee3-0000-1000-8000-00805f9b34fb");

    public final static UUID UUID_FIRMWARE = UUID.fromString("0000fee4-0000-1000-8000-00805f9b34fb");

    private BluetoothGattCharacteristic gattCharacteristic_Common;

    private BluetoothGattCharacteristic gattCharacteristic_Driver;

    private BluetoothGattCharacteristic gattCharacteristic_Message;

    private BluetoothGattCharacteristic gattCharacteristic_Firmware;


    public static BluetoothLeService le_service = null;

    public static Le_intercface le_cb = null;

    private BluetoothGattCharacteristic gattCharacteristic_Fridy;


    private List<byte[]> sen_date=new ArrayList<>();

    private List<byte[]> write_list=new ArrayList<>();

    private HashMap<String,byte[]> scan_lists = new HashMap<>();

    private List<Byte> code_list = new ArrayList<>();

    private List<Byte> write_code = new ArrayList<>();


    private BluetoothAdapter mBluetoothAdapter = null;

    private BluetoothGatt mBluetoothGatt;

    //是否发送成功
    public boolean condition;

    public boolean write;

    private List<List> list_data = new ArrayList<>();

    private int islength;                                        //BLE接收层包的长度

    public int isConnect = 0;
    public static int isConnTime = 2000;


    private List<Byte> key_date = new ArrayList<Byte>();            //BLE 数据接收层初始集合

    private boolean ismfile;                                     //BLE数据层接收开关

    public boolean is_return = false;

    public boolean CharacteristicCommon = false;
    public boolean CharacteristicDriver = false;
    public boolean CharacteristicMessage = false;
    public boolean CharacteristicFirmware = false;

    public boolean CharacteristicDescriptor = false;

    public boolean CharacteristicMtu = false;





    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void add_le_interface(Le_intercface le_cb) {
        this.le_cb = le_cb;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化一个蓝牙适配器
        final BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();
        startLeScan();
        le_service = BluetoothLeService.this;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.i(TAG,"Service.onCreate");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG,"Service.onDestroy");

        if(mBluetoothGatt != null){

            RemoveData();
            mBluetoothGatt.disconnect();

            LogUtil.i(TAG,"onDestroy:+断开");

        }
    }


    private Runnable sendRunnable = new Runnable() {
        @Override
        public void run() {

            try {
                while (list_data.size() > 0){

                    resendBytes = true;

                    List<byte[]> mData = list_data.remove(0);

                    final byte code = code_list.remove(0);

                    sen_date = mData;

                    if (mData != null) {

                    //    LogUtil.i(TAG, "发送线程ID:"+Thread.currentThread().getName());

                        try {
                            for(final byte[] bxs : mData){

                                byte ret = GlobalConsts.NO;
                                try {
                                    sendWriteByte(code,bxs);
                                    int number = 0;
                                    condition = false;

                                    int sleep = 0;

                                    while (!condition && number < 3) {

                                        while (!condition && sleep < 3000){
                                            Thread.sleep(1);
                                            sleep++;
                                        }

                                        if (!condition &&  number < 2) {

                                            LogUtil.i(TAG, "发送次数："+number);
                                            sendWriteByte(code,bxs);
                                        }
                                        sleep = 0;
                                        number++;
                                    }

                                    if (!condition && number!= 0) {

                                        LogUtil.i(TAG, "硬件无响应，释放BufferedInputStream。");

                                        write = true;
                                    }
                                    if (condition) {
                                        if(is_return){

                                            ret = GlobalConsts.YES;
                                            LogUtil.i(TAG, "硬件正常响应，继续发送");

                                        }else{
                                            LogUtil.i(TAG, "发送成功");
                                        }
                                    }
                                    if(write){

                                        LogUtil.i(TAG, "清空发送数据集合，跳出发送循环");

                                        condition = false;

                                        write_list.clear();
                                        write_code.clear();

                                        list_data.clear();
                                        code_list.clear();

                                        break;

                                    }

                                } catch (InterruptedException e) {
                                    ExceptionUtil.handleException(e);
                                } finally {
                                    //回调发送结果
                                    BluetoothLeService.le_cb.on_send_result(bxs,ret,code);

                                }
                            }
                        } catch (Exception e) {
                            ExceptionUtil.handleException(e);
                        }

                        /*if(write || list_data.size() != code_list.size()){

                            LogUtil.i(TAG, "发送失败，断开连接：");

                            write_list.clear();
                            write_code.clear();

                            list_data.clear();
                            code_list.clear();

                            condition = false;

                            break;

                        }*/

                    }
                }
            } catch (Exception e) {

                e.printStackTrace();

            }finally {

                resendBytes = false;

            }

        }
    };
    public void resend_file(byte code , final List<byte[]> bxs) {

        this.list_data.add(bxs);
        this.code_list.add(code);

        if(!resendBytes){

            resendBytes = true;

            Thread thread = new Thread(sendRunnable,""+System.currentTimeMillis()/1000/60/60/24);

            thread.start();
        }

    }

    // BLE 发送层 数据收集器
    public void sendWriteByte(byte code ,byte[] bxss) {

        this.write_list.add(bxss);
        this.write_code.add(code);

    //    LogUtil.i(TAG, "发送变量:"+writeBytes);

        if(!writeBytes){

            writeBytes = true;

            Thread thread = new Thread(ble_WriteBytes,""+System.currentTimeMillis()/1000/60/60/24);

            thread.start();

            /*try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

        }

    }

    int kk = 0;

    boolean writeBytes = false;
    boolean resendBytes = false;


    private Runnable ble_WriteBytes = new Runnable() {
        @Override
        public void run() {

            /*try {

                write = false;

                while (!write && write_list.size() > 0 && write_code.size() > 0) {

                    writeBytes = true;

                    byte[] bxs = write_list.remove(0);

                    byte code = write_code.remove(0);

                    BluetoothLeService.le_cb.on_data(bxs);

                    //  LogUtil.i(TAG, "发送线程ID:"+Thread.currentThread().getName());

                    LogUtil.i(TAG,"111111发送次数："+kk+++"通道："+code+",发送数据="+System.currentTimeMillis()+"--"+Arrays.toString(bxs));


                    int islength = 0;

                    int j = 0;

                    byte[] WriteBytes;

                    while (!write && islength < bxs.length) {
                        int i = 0;
                        if(bxs.length - (j + 1) < ble_length){
                            WriteBytes = new byte[bxs.length - j];
                        }else{
                            WriteBytes = new byte[ble_length];
                        }
                        while (islength < bxs.length && i < ble_length) {
                            j++;
                            WriteBytes[i++] = bxs[islength++];
                        }

                        LogUtil.i(TAG,"发送的数据="+Arrays.toString(WriteBytes));

                        if (code == Objcode.FRIDY) {

                            if(gattCharacteristic_Fridy != null){

                                gattCharacteristic_Fridy.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                gattCharacteristic_Fridy.setValue(WriteBytes);

                                writeCharacteristic(gattCharacteristic_Fridy);

                            }



                        }else if(code == Objcode.COMMON){

                            if(gattCharacteristic_Common != null){

                                gattCharacteristic_Common.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                gattCharacteristic_Common.setValue(WriteBytes);

                                writeCharacteristic(gattCharacteristic_Common);

                            }




                        }else if(code == Objcode.DRIVER){

                            if(gattCharacteristic_Driver != null){

                                gattCharacteristic_Driver.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                gattCharacteristic_Driver.setValue(WriteBytes);

                                writeCharacteristic(gattCharacteristic_Driver);

                            }



                        }else if(code == Objcode.MESSAGE){

                            if(gattCharacteristic_Message != null){

                                gattCharacteristic_Message.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                gattCharacteristic_Message.setValue(WriteBytes);

                                writeCharacteristic(gattCharacteristic_Message);

                            }



                        }else if(code == Objcode.FIRMWARE){

                            if(gattCharacteristic_Firmware != null){

                                gattCharacteristic_Firmware.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                gattCharacteristic_Firmware.setValue(WriteBytes);

                                writeCharacteristic(gattCharacteristic_Firmware);
                            }

                        }

                        try {
                            Thread.sleep(interval);
                        } catch (InterruptedException e) {
                            ExceptionUtil.handleException(e);
                        }

                        if(write){
                            break;
                        }
                    }


                }

            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            } finally {

                writeBytes = false;
            }*/

            int time = 1;

            while(true) {

                try {
                //    LogUtil.i(TAG,"111111发送通道："+kk+"通道");

                    write = false;

                    while (!write && write_list.size() > 0 && write_code.size() > 0) {
                        time=0;
                        writeBytes = true;

                        byte[] bxs = write_list.remove(0);

                        byte code = write_code.remove(0);

                        BluetoothLeService.le_cb.on_data(bxs);

                        //  LogUtil.i(TAG, "发送线程ID:"+Thread.currentThread().getName());

                        LogUtil.i("aaaaaaa","111111发送次数："+kk+++"通道："+code+",发送数据="+System.currentTimeMillis()+"--"+Arrays.toString(bxs));


                        int islength = 0;

                        int j = 0;

                        byte[] WriteBytes;

                        while (!write && islength < bxs.length) {
                            int i = 0;
                            if(bxs.length - (j + 1) < ble_length){
                                WriteBytes = new byte[bxs.length - j];
                            }else{
                                WriteBytes = new byte[ble_length];
                            }
                            while (islength < bxs.length && i < ble_length) {
                                j++;
                                WriteBytes[i++] = bxs[islength++];
                            }

                        //    LogUtil.i(TAG,"111111发送的数据="+Arrays.toString(WriteBytes));

                            if (code == Objcode.FRIDY) {

                                if(gattCharacteristic_Fridy != null){

                                    gattCharacteristic_Fridy.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Fridy.setValue(WriteBytes);

                                    writeCharacteristic(gattCharacteristic_Fridy);

                                }



                            }else if(code == Objcode.COMMON){

                                if(gattCharacteristic_Common != null){

                                    gattCharacteristic_Common.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Common.setValue(WriteBytes);

                                    writeCharacteristic(gattCharacteristic_Common);

                                }




                            }else if(code == Objcode.DRIVER){

                                if(gattCharacteristic_Driver != null){

                                  // Log.d("aaaaaaaa","WriteBytes    "+"     "+Arrays.toString(WriteBytes));

                                 //没加密发送数据
                                 /*   gattCharacteristic_Driver.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Driver.setValue(WriteBytes);
                                    writeCharacteristic(gattCharacteristic_Driver);*/


                                 //加密发送数据，每次20个数据加密发送，有的20个数据以下，有的20数据以上
                                    int num ;
                                    if(WriteBytes.length > 20 && WriteBytes.length %20 !=0 ) {
                                        num = WriteBytes.length/20 + 1;
                                    } else{
                                        if(WriteBytes.length <= 20) {
                                            num = 1;
                                      }
                                        else{
                                            num = WriteBytes.length/20;
                                        }
                                    }
                                   // Log.d("aaaaaaaa","num    "+num);
                                    byte[] data = new byte[num*20];
                                    byte[] dest = new byte[num*20];

                                    System.arraycopy(WriteBytes, 0 ,dest, 0,  WriteBytes.length);
                                    for(int s = 0; s < num; s++) {
                                        byte[] out = new byte[20];
                                        byte[] buffer = new byte[20];
                                        System.arraycopy(dest, s*20 ,buffer, 0,  buffer.length);
                                       // Log.d("aaaaaaaa","Arrays.toString(buffer)    "+Arrays.toString(buffer));
                                        SMSEncrypt.aes_encode(buffer,out);//加密
                                        System.arraycopy(out, 0 ,data, s*20,  20);
                                      //  Log.d("aaaaaaaa","Arrays.toString(data)    "+Arrays.toString(data));
                                    }
                                    gattCharacteristic_Driver.setValue(data[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Driver.setValue(data);
                                   // Log.d("aaaaaaaa","发送加密    "+"     "+Arrays.toString(data)+"  "+num);
                                   // Log.d("aaaaaaaa","发送解密    "+  EncryptUtil.bytesToHexString(data));
                                   writeCharacteristic(gattCharacteristic_Driver);


                                }



                            }else if(code == Objcode.MESSAGE){
                                //  Log.d("aaaaaaaa","3333333333333333333333333333333333333 ");
                                if(gattCharacteristic_Message != null){

                                    gattCharacteristic_Message.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Message.setValue(WriteBytes);
                                  //  Log.d("dddd","WriteBytes    "+"     "+Arrays.toString(WriteBytes));
                                    /*boolean send = false;

                                    while (!send){

                                        send = mBluetoothGatt.writeCharacteristic(gattCharacteristic_Message);

                                        LogUtil.i(TAG,"语音发送结果："+send+"-"+System.currentTimeMillis()+"-"+Arrays.toString(WriteBytes));

                                    }*/

                                    writeCharacteristic(gattCharacteristic_Message);

                                }

                            }else if(code == Objcode.FIRMWARE){

                                if(gattCharacteristic_Firmware != null){

                                    gattCharacteristic_Firmware.setValue(WriteBytes[0], BluetoothGattCharacteristic.FORMAT_UINT8, 0);
                                    gattCharacteristic_Firmware.setValue(WriteBytes);

                                    writeCharacteristic(gattCharacteristic_Firmware);

                                }

                            }

                            try {
                                Thread.sleep(interval);
                            } catch (InterruptedException e) {
                                ExceptionUtil.handleException(e);
                            }

                            if(write){
                                break;
                            }
                        }


                    }

                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }

                if(time<40)
                {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    time++;

                }else{

                    writeBytes = false;

                    break;

                }
            }

        }
    };

    //搜索附近的设备
    public void startLeScan() {

        if(mBluetoothAdapter == null){
            return;
        }

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                LogUtil.i(TAG,"开始搜索设备");
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }).start();*/

        BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (scanner == null) {
            return;
        }

        scanner.startScan(mScanCallback);



    }
    //搜索附近的设备
    public void stopLeScan() {

     //   mBluetoothAdapter.stopLeScan(mLeScanCallback);

        BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (scanner == null) {
            return;
        }

        scanner.stopScan(mScanCallback);

    }

    //搜索附近的设备
    public void scanLeScan() {

        /*new Thread(new Runnable() {
            @Override
            public void run() {
                isConnect = 0;

                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        }).start();*/

        BluetoothLeScanner scanner = mBluetoothAdapter.getBluetoothLeScanner();

        if (scanner == null) {
            return;
        }

        scanner.startScan(mScanCallback);


    }

    public void closeBluetooth() {

        if(mBluetoothGatt != null){

            code = Opcode.GET;
        //    RemoveData();
            mBluetoothGatt.disconnect();
        //    mBluetoothGatt.close();
            LogUtil.i(TAG,"closeBluetooth:+断开");

        }

    }

    private ScanCallback mScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);

            LogUtil.i(TAG,"mScanCallback:"+result.getDevice().getAddress());

            try {


                List<ParcelUuid> list = result.getScanRecord().getServiceUuids();


                if(list != null){

                    if(list.size() > 0){

                        if(list.get(0).getUuid().equals(UUID_DRIVER_VERSIONS_TO)){


                            LogUtil.i(TAG,"mScanCallback:list:"+result.getDevice().getAddress());


                            SparseArray<byte[]> scanRecords =  result.getScanRecord().getManufacturerSpecificData();


                            byte [] scanRecord = scanRecords.valueAt(0);


                            BluetoothLeService.le_cb.on_scan(result.getDevice(), 0,scanRecord ,le_service);

                            if(scanRecord != null){

                                if(scanRecord.length == 24){

                                    BluetoothDevice device = result.getDevice();

                                //    BluetoothLeService.le_cb.on_scan(device, 0,scanRecord ,le_service);

                                    boolean isDevice = false;

                                    Iterator it = scan_lists.keySet().iterator();

                                    while(it.hasNext()) {

                                        String key = (String)it.next();

                                        if(key.equals(device.getAddress())){

                                            isDevice = true;

                                            break;
                                        }

                                    }

                                    if(!isDevice){

                                        scan_lists.put(device.getAddress(),scanRecord);

                                    }

                                }
                            }


                    }

                    }

                }


                /*if(data.length == 62){


                    int i = data[0];

                    byte[]


                    if(data[3] == 27){

                        byte[] xi = {data[7],data[8],data[9],data[10],data[11],data[12],data[13],data[14],
                                data[15],data[16],data[17],data[18],data[19],data[20],data[21],data[22],data[23],
                                data[24],data[25],data[26],data[27],data[28],data[29],data[30]};

                        byte[] uuid = {data[33],data[34]};


                    }

                }*/

            //    SparseArray<byte[]> scanRecord =  result.getScanRecord().getManufacturerSpecificData();
            //    byte[] scanRecord =  result.getScanRecord().getManufacturerSpecificData(1);


            /*    if(scanRecord != null && scanRecord.size() >0){

                //    LogUtil.i(TAG,"mScanCallback:SparseArray:"+scanRecord.toString());

                    String r = "";

                    for (int i = 0; i < scanRecord.size(); i++) {

                        *//*for(byte b : scanRecord.valueAt(i)){

                            String hex = Integer.toHexString(b & 0xFF);
                            if (hex.length() == 1) {
                                hex = '0' + hex;
                            }
                            r += hex.toUpperCase();

                        }*//*

                        LogUtil.i(TAG,"mScanCallback:"+i+":"+Arrays.toString(scanRecord.valueAt(i)));


                    }



                    *//*BluetoothDevice device = result.getDevice();

                    BluetoothLeService.le_cb.on_scan(device, 0,scanRecord ,le_service);

                    boolean isDevice = false;

                    Iterator it = scan_lists.keySet().iterator();

                    while(it.hasNext()) {

                        String key = (String)it.next();

                        if(key.equals(device.getAddress())){

                            isDevice = true;

                            break;
                        }

                    }*//*

                    *//*if(!isDevice){

                        scan_lists.put(device.getAddress(),scanRecord);

                    }*//*

                }*/
            } catch (Exception e) {
                e.printStackTrace();
            }


            /*for(int k = 0;k<list.size();k++){


                Iterator it = scan_lists.keySet().iterator();

                while(it.hasNext()) {

                    String key = (String)it.next();

                    if(key.equals(device.getAddress())){

                        isDevice = true;

                        break;
                    }

                }

                if(!isDevice){

                    List<byte[]> scan = new ArrayList<>();

                    scan.add(volts);
                    scan.add(freq);
                    scan.add(sp);
                    scan.add(vox);
                    scan.add(tx_code);
                    scan.add(rx_code);
                    scan.add(tx_power);
                    scan.add(rf);
                    scan.add(name);

                    scan_lists.put(device.getAddress(),scan);

                }

            }*/



        }

        @Override
        public void onBatchScanResults(List<ScanResult> results) {
            super.onBatchScanResults(results);

            LogUtil.i(TAG,"之前扫描的设备:"+results.toString());
        }

        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            LogUtil.i(TAG,"扫描失败:");
        }
    };

    /**
     * 搜索附近的BLE设备信息回调
     */
    private BluetoothAdapter.LeScanCallback mLeScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(final BluetoothDevice device, final int rssi, final byte[] scanRecord) {
            /**
             * 搜索附近的BLE设备回调监听方法
             * */

         //   LogUtil.i(TAG,device.toString()+","+Arrays.toString(scanRecord));

            if(le_service != null && BluetoothLeService.le_cb != null){

                try {

                    BluetoothLeService.le_cb.on_scan(device, rssi, scanRecord,le_service);

                    /*if(scanRecord.length == 62){

                        List<byte[]> scan_list = getInformation(scanRecord);

                        if(scan_list != null){

                            int uuid = 0;

                            for(byte[] scan : scan_list){

                                if(scan.length > 0){

                                    if(scan[0] == 2){

                                        uuid = (int) ((scan[1] & 0xFF)
                                                | ((scan[2] & 0xFF)<<8));

                                        break;
                                    }
                                }else{

                                    break;
                                }

                            }


                            if(uuid == smsUUID){

 //                             LogUtil.i(TAG,"BluetoothDevice:"+device.getAddress()+",scanRecord:"+Arrays.toString(scanRecord));

                                for(byte[] interPhones : scan_list){

 //                                   LogUtil.i(TAG,"modes"+Arrays.toString(interPhones));

                                    if(interPhones.length > 7){

                                        if(interPhones[0] == -1 && interPhones.length == 27){

                                            if(interPhones[7] > 10 && interPhones[8] > 10 && interPhones[9] > 10 && interPhones[10] > 10){

                                                return;

                                            }else{

                                                int i = 1;

                                                byte[] volts = {27,interPhones[i++],interPhones[i++]};
                                                byte[] freq = {2,interPhones[i++],interPhones[i++],interPhones[i++],interPhones[i++]};
                                                byte[] sp = {4,interPhones[i++]};
                                                byte[] vox = {5,interPhones[i++]};
                                                byte[] tx_code = {6,interPhones[i++]};
                                                byte[] rx_code = {7,interPhones[i++]};
                                                byte[] tx_power = {13,interPhones[i++]};
                                                byte[] rf = {26,interPhones[i++]};
                                                byte[] name = new byte[interPhones.length - i + 1];

                                                for(int k = 0;k < name.length; k++){

                                                    if(k == 0){
                                                        name[k] = 22;
                                                    }else{
                                                        name[k] = interPhones[i++];
                                                    }

                                                }

                                                boolean isDevice = false;

                                                Iterator it = scan_lists.keySet().iterator();

                                                while(it.hasNext()) {

                                                    String key = (String)it.next();

                                                    if(key.equals(device.getAddress())){

                                                        isDevice = true;

                                                        break;
                                                    }

                                                }

                                                if(!isDevice){

                                                    List<byte[]> scan = new ArrayList<>();

                                                    scan.add(volts);
                                                    scan.add(freq);
                                                    scan.add(sp);
                                                    scan.add(vox);
                                                    scan.add(tx_code);
                                                    scan.add(rx_code);
                                                    scan.add(tx_power);
                                                    scan.add(rf);
                                                    scan.add(name);

                                                    scan_lists.put(device.getAddress(),scan);

                                                }
                                            }
                                        }



                                    }

                                }

                            }
                        }
                    }*/

                } catch (Exception e) {

                    ExceptionUtil.handleException(e);

                }

            }

        }
    };

    private List<byte[]> getInformation(byte[] scanRecord) {

        List<byte[]> list = new ArrayList<>();

        try {

            byte[] modes = new byte[scanRecord[0]];

            int modesLength = 1;

            for(int i = 0;i < modes.length;i++){

                modes[i] = scanRecord[modesLength++];


            }

            list.add(modes);


            byte[] interPhones = new byte[scanRecord[modesLength++]];

            for(int i = 0;i < interPhones.length;i++){

                interPhones[i] = scanRecord[modesLength++];

            }

            list.add(interPhones);

            byte[] uuids = new byte[scanRecord[modesLength++]];

            for(int i = 0;i < uuids.length;i++){

                uuids[i] = scanRecord[modesLength++];

            }

            list.add(uuids);

            byte[] names = new byte[scanRecord[modesLength++]];

            for(int i = 0;i < names.length;i++){

                names[i] = scanRecord[modesLength++];

            }

            list.add(names);

            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    /**
     * 连接设备
     */
    public void connect(byte cd,final String address) {

        code = cd;

        MyApplicatoin.interphone = new Interphone();

                if(mBluetoothGatt != null){

                    RemoveData();

                    try {
                        mBluetoothGatt.close();
                        LogUtil.i(TAG,"connect:+断开");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }

        if (mBluetoothAdapter == null || address == null) {
            LogUtil.i(TAG, "BluetoothAdapter 没有初始化或未指明的地址.");
            return;
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        if (device == null) {
            LogUtil.i(TAG, "设备没有找到。无法连接.");
            return;
        }
        // 我们想直接连接到设备, 所以我们设置符
        // 参数为false.
        mBluetoothGatt = device.connectGatt(le_service, false, mGattCallback);
        LogUtil.i(TAG, "尝试创建一个新的连接.");

        return;


        /*new Thread(new Runnable() {
            @Override
            public void run() {


            }
        }).start();*/
    }


    /**
     * 实现了设备回调事件的回调方法。例如：连接变化和服务发现
     */
    private final BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {


        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {

            LogUtil.i(TAG, "onConnectionStateChange="+gatt.getDevice().getAddress()+",status="+status+",newState="+newState);

            try {
                RemoveData();

                if (newState == BluetoothProfile.STATE_CONNECTED) {

                    code = Opcode.SET;

                    stopLeScan();

                    LogUtil.i(TAG, "连接成功"+System.currentTimeMillis());

                    MyApplicatoin.interphone.setAddress(gatt.getDevice().getAddress());
                    MyApplicatoin.interphone.setName(gatt.getDevice().getName());


                    isConnect = 0;

                    mBluetoothGatt.discoverServices();

                    /*try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }*/

                    if(scan_lists.size() > 0){

                        Iterator it = scan_lists.keySet().iterator();

                        while(it.hasNext()) {

                            String key = (String)it.next();

                            if(key.equals(gatt.getDevice().getAddress())){

                                byte[] list = scan_lists.get(key);

                                if(list.length == 24){


                                    byte[] freq = {2,list[0],list[1],list[2],list[3]};

                                    BluetoothLeService.le_cb.on_result(freq, Objcode.DRIVER);

                                    byte[] volt = {27,list[4],list[5]};


                                    BluetoothLeService.le_cb.on_result(volt, Objcode.DRIVER);

                                    byte[] rx = {7,list[6]};

                                    BluetoothLeService.le_cb.on_result(rx, Objcode.DRIVER);

                                    byte[] tx = {6,list[7]};

                                    BluetoothLeService.le_cb.on_result(tx, Objcode.DRIVER);

                                    byte[] power = {13,list[8]};

                                    BluetoothLeService.le_cb.on_result(power, Objcode.DRIVER);

                                    byte[] rf = {26,list[9]};

                                    BluetoothLeService.le_cb.on_result(rf, Objcode.DRIVER);

                                    try {

                                        SharedPreferences sharedPreferences = BluetoothLeService.this.getSharedPreferences(GlobalConsts.GROUPHITE, Context.MODE_PRIVATE);


                                        String guoupName = sharedPreferences.getString(GlobalConsts.GroupName,null);
                                        String guoupHite = sharedPreferences.getString(GlobalConsts.GroupHite,null);

                                        byte[] name = {list[10],list[11],list[12],list[13],list[14],list[15],list[16],list[17],list[18],list[19],list[20],list[21],list[22],list[23]};

                                    //    long a = Long.valueOf(new String(name));

                                        String a = new String(name,"ISO-8859-1");

                                        a = Tools.getGroupName(a);

                                        LogUtil.i(TAG,"getHite1:"+guoupName);



                                        if(guoupName != null){

                                            LogUtil.i(TAG,"getHite2:"+a);
                                            LogUtil.i(TAG,"getHite3:"+Long.valueOf(guoupName));
                                            LogUtil.i(TAG,"getHite4:"+guoupHite);
                                            if(a.equals(guoupName)){

                                                int l = 0;
                                                byte[] m = guoupHite.getBytes();


                                                byte[] n = {22,list[10],list[11],list[12],list[13],list[14],list[15],list[16],list[17],list[18],list[19],list[20],list[21],list[22],list[23],
                                                        m[l++],m[l++],m[l++],m[l++],m[l++],};

                                                BluetoothLeService.le_cb.on_result(n, Objcode.DRIVER);

                                                byte[] as = new byte[m.length - 5];

                                                int z = 0;

                                                for(int i = 5;i<m.length;i++){

                                                    as[z++] = guoupHite.getBytes()[i];

                                                }

                                                BluetoothLeService.le_cb.on_result(as, Objcode.DRIVER);

                                            }

                                        }

                                    //    byte[] name = {22,list[10],list[11],list[12],list[13],list[14],list[15],list[16],list[17],list[18],list[19],list[20],list[21],list[22],list[23]};
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                break;
                            }

                        }

                        scan_lists.clear();

                    }

                } else {

                    mBluetoothGatt.disconnect();
                    mBluetoothGatt.close();

                    if(code == Opcode.SET){

                        SharedPreferences sharedPreferences = BluetoothLeService.this.getSharedPreferences(GlobalConsts.DEVICE, Context.MODE_PRIVATE);
                        String name = sharedPreferences.getString(GlobalConsts.DEVICE_NAME, "name");
                        String address = sharedPreferences.getString(GlobalConsts.DEVICE_ADDRESS, "address");

                        LogUtil.i(TAG, "address:"+address);

                        if(!address.equals("address")){

                            if(isConnect < 5){

                                isConnect++;

                                try {
                                    Thread.sleep(isConnTime);
                                } catch (InterruptedException e) {
                                    ExceptionUtil.handleException(e);
                                }

                                final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                                if (device == null) {
                                    LogUtil.i(TAG, "设备没有找到。无法连接.");
                                    return;
                                }
                                // 我们想直接连接到设备, 所以我们设置符
                                // 参数为false.
                                mBluetoothGatt = device.connectGatt(le_service, false, mGattCallback);
                                LogUtil.i(TAG, "尝试创建一个新的连接.");
                            }
                        }

                    }

                    LogUtil.i(TAG, "连接断开");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            /**
             * 设备连接变化与服务发现的回调监听
             * */
            BluetoothLeService.le_cb.on_statechange(gatt, status, newState);
        }

        /**
         *
         * 打开设备Notification通道，准备接收数据
         * */
        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {

            LogUtil.i(TAG, "onServicesDiscovered:"+status+",is_Notification():"+is_Notification());

            mBluetoothGatt = gatt;

            try {

                if(status == BluetoothGatt.GATT_SUCCESS){


                    if (is_Notification() == versions_erro) {

                        displayGattServices(getSupportedGattServices(gatt));

                        LogUtil.i(TAG, "setCharacteristicNotification:"+is_Notification());

                        if (is_Notification() != versions_erro) {


                            setCharacteristicNotification(gatt,is_Notification(), mBluetoothGatt.discoverServices());

                            BluetoothLeService.le_cb.on_notification(is_Notification());

                            sen_date.clear();


                            if(is_Notification() == versions_one){

                                //发送获取设备信息命令

                                sen_date.add(ATcommand.at_get_file_result(Opcode.GET, Objcode.FILE_BASUC, GlobalConsts.YES));

                                resend_file(Objcode.FRIDY,sen_date);

                            }

                            //设置高功耗传输
                        //  mBluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);

                            /*try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }*/

                        } else {
                            LogUtil.i(TAG, "尝试Notification.失败");


                            RemoveData();
                            mBluetoothGatt.disconnect();

                            LogUtil.i(TAG,"打开设备Notification通道:+断开");

                        }
                    }


                    if(is_Notification() != versions_one && !CharacteristicDescriptor){

                        DescriptorWrite();
                    }
                }
            } catch (Exception e) {
                ExceptionUtil.handleException(e);
            }


        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
         //   LogUtil.i(TAG, "onCharacteristicRead");
        }

        /**
         * 连接正常服务端有消息进来就会回调此方法
         * */

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        //    LogUtil.i(TAG, "onCharacteristicChanged");

            final byte[] gattData = characteristic.getValue();

            LogUtil.i(TAG,"接收成功:"+Arrays.toString(gattData));
            Log.d("bbbbbb","gattData:   "+Arrays.toString(gattData)+"   leng    "+gattData.length);
            on_data_read(gatt,characteristic);//命令封装


        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        //    LogUtil.i(TAG, "onCharacteristicWrite");


            characteristic.getUuid();
        }


        @Override
        public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
            super.onReliableWriteCompleted(gatt, status);
            LogUtil.i(TAG, "onReliableWriteCompleted");
        }

        @Override
        public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
            super.onMtuChanged(gatt, mtu, status);
            LogUtil.i(TAG, "onMtuChanged："+mtu+","+status);

            if(status == BluetoothGatt.GATT_SUCCESS){

                CharacteristicMtu = true;

                if(mtu == 63){
                    ble_length = 60;
                }else if(mtu == 23){
                    ble_length = 20;
                }

                LogUtil.i(TAG,"获取设备信息：");

                List<byte[]> sen_date = new ArrayList<>();

                byte[] common = {1,2,3,4,5,6,7,8,9,10};

                for(int i = 0; i < common.length;i++ ){

                    byte[] a = {common[i]};

                    sen_date.add(a);
                }
                resend_file(Objcode.COMMON,sen_date);

                byte[] driver = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,22,23,24,25,26,27,28,29,31};

                List<byte[]> sen_date2 = new ArrayList<>();

                for(int i = 0; i < driver.length;i++ ){

                    byte[] a = {driver[i]};

                    sen_date2.add(a);

                }

                resend_file(Objcode.DRIVER,sen_date2);

            }
        }

        @Override
        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
            super.onReadRemoteRssi(gatt, rssi, status);
            LogUtil.i(TAG, "onReadRemoteRssi");
        }

        /**
         *
         *
         * BluetoothGattDescriptor 实行单例打开模式，频繁操作会导致冷却时间刷新并延长，必须打开一个接收到回调再打开下一个。
         *
         * */
        @Override
        public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorWrite(gatt, descriptor, status);
            LogUtil.i(TAG, "onDescriptorWrite:"+status);

            if(status == BluetoothGatt.GATT_SUCCESS){

                DescriptorWrite();

            }

        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            LogUtil.i(TAG, "onDescriptorRead"+status);
        }
    };


    private void DescriptorWrite() {

        if(!CharacteristicCommon){

            CharacteristicCommon = mBluetoothGatt.writeDescriptor(setGattDescriptor(gattCharacteristic_Common));

            LogUtil.i(TAG,"writeDescriptor+CharacteristicCommon:"+ CharacteristicCommon);

            return;
        }

        if(!CharacteristicDriver){
            CharacteristicDriver = mBluetoothGatt.writeDescriptor(setGattDescriptor(gattCharacteristic_Driver));

            LogUtil.i(TAG,"writeDescriptor+CharacteristicDriver:"+ CharacteristicDriver);

            return;
        }
        if(!CharacteristicMessage){
            CharacteristicMessage = mBluetoothGatt.writeDescriptor(setGattDescriptor(gattCharacteristic_Message));
            LogUtil.i(TAG,"writeDescriptor+CharacteristicMessage:"+ CharacteristicMessage);

            return;
        }

        if(!CharacteristicFirmware){
            CharacteristicFirmware = mBluetoothGatt.writeDescriptor(setGattDescriptor(gattCharacteristic_Firmware));
            LogUtil.i(TAG,"writeDescriptor+CharacteristicFirmware:"+ CharacteristicFirmware);

            if(CharacteristicCommon && CharacteristicDriver && CharacteristicMessage && CharacteristicFirmware){

                CharacteristicDescriptor = true;

            }


            if(CharacteristicDescriptor){



                /*if(!CharacteristicMtu){

                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    //MTU 开关

                    boolean ret =   mBluetoothGatt.requestMtu(63);
                    LogUtil.i(TAG, "onMtuChanged："+ret);

                    return;
                }*/

                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                LogUtil.i(TAG,"获取设备信息：");

                List<byte[]> sen_date = new ArrayList<>();

                byte[] common = {1,2,3,4,5,6,7,8,9,10,12};

                for(int i = 0; i < common.length;i++ ){

                    byte[] a = {common[i]};

                    sen_date.add(a);
                }
                resend_file(Objcode.COMMON,sen_date);

                byte[] driver = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,22,23,24,25,26,27,28,29,31};

                List<byte[]> sen_date2 = new ArrayList<>();

                for(int i = 0; i < driver.length;i++ ){

                    byte[] a = {driver[i]};

                    sen_date2.add(a);

                }

                resend_file(Objcode.DRIVER,sen_date2);

                return;
            }
        }
    }


    private BluetoothGattDescriptor setGattDescriptor(BluetoothGattCharacteristic gattCharacteristic) {


        BluetoothGattDescriptor bluetoothGattDescriptor = gattCharacteristic.getDescriptor(UUID.fromString("00002902-0000-1000-8000-00805f9b34fb"));

        LogUtil.i(TAG,"BluetoothGattCharacteristic:"+gattCharacteristic.getUuid().toString()+",BluetoothGattDescriptor:"+bluetoothGattDescriptor.getUuid().toString());

        bluetoothGattDescriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);

        return bluetoothGattDescriptor;
    }


    private void RemoveData() {

        MyApplicatoin.interphone = new Interphone();
        MyApplicatoin.IS_OPEN_JOIN = false;
        BluetoothLeService.ble_length = 20;

        writeBytes = false;
        resendBytes = false;


        LogUtil.i(TAG, "连接改变");

        write_list.clear();
        write_code.clear();

        list_data.clear();
        code_list.clear();

        condition = true;
        is_return = true;
        write = true;

        gattCharacteristic_Fridy = null;
        gattCharacteristic_Common = null;
        gattCharacteristic_Driver = null;
        gattCharacteristic_Message = null;
        gattCharacteristic_Firmware = null;

        CharacteristicCommon = false;
        CharacteristicDriver = false;
        CharacteristicMessage = false;
        CharacteristicFirmware = false;

        CharacteristicDescriptor = false;
        CharacteristicMtu = false;

        versions_code = versions_erro;

    }


    /**
     * Notification给设备打开回调通道
     */

    public void setCharacteristicNotification(BluetoothGatt gatt, byte code, boolean enabled) {

        if (mBluetoothAdapter == null || gatt == null) {
            LogUtil.i(TAG, "BluetoothAdapter没有初始化");
            return;
        }

        if(code == versions_one){

            LogUtil.i(TAG,"Notification:"+mBluetoothGatt.setCharacteristicNotification(gattCharacteristic_Fridy, enabled));

        }else if(code == versions_to){

            LogUtil.i(TAG,"Notification:"+mBluetoothGatt.setCharacteristicNotification(gattCharacteristic_Common, enabled));

            LogUtil.i(TAG,"Notification:"+mBluetoothGatt.setCharacteristicNotification(gattCharacteristic_Driver, enabled));

            LogUtil.i(TAG,"Notification:"+mBluetoothGatt.setCharacteristicNotification(gattCharacteristic_Message, enabled));

            LogUtil.i(TAG,"Notification:"+mBluetoothGatt.setCharacteristicNotification(gattCharacteristic_Firmware, enabled));

        }


    }



    /**
     * 写消息
     */

    public void writeCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            LogUtil.i(TAG, "BluetoothAdapter没有初始化");
            return;
        }
     //   LogUtil.i(TAG, "BluetoothAdapter发送成功");

        boolean send = mBluetoothGatt.writeCharacteristic(characteristic);

        if(!send) {

            LogUtil.i(TAG, "重新发送数据");

        }


     //
    }

    public List<BluetoothGattService> getSupportedGattServices(BluetoothGatt gatt) {

        if (gatt == null) {
            return null;
        }

        return gatt.getServices();

    }


    /**
     *
     * 收到消息，进行协议处理。
     *
     * */

    private void on_data_read(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {

        final byte[] gattData = characteristic.getValue();

    //    LogUtil.i(TAG,"接收成功:"+Arrays.toString(gattData));

        try {

            if(is_Notification() == versions_one){

                if (gattData != null && gattData.length > 0) {
                    List<String> Buff_date = new ArrayList<String>();

                    for (byte byteChar : gattData) {
                        String aa = String.format("%02X", byteChar);
                        Buff_date.add(aa);
                    }

                //  LogUtil.i(TAG, "接收成功，Buff_date=" + Arrays.toString(gattData));

                    if (Buff_date.size() > 6 && Buff_date.get(0).equals("41") && Buff_date.get(1).equals("54") && Buff_date.get(2).equals("2B") && ismfile != true) {
                        //这是AT+开头
                        islength = Integer.parseInt(Buff_date.get(6) + Buff_date.get(5), 16);

                        key_date.clear();
                        ismfile = true;

                        for(byte bytes : gattData){
                            key_date.add(bytes);
                        }

                    } else if(ismfile){
                        for(byte bytes : gattData){
                            key_date.add(bytes);
                        }
                    }
                    //已经接收完一个完整包
                    if (key_date != null && key_date.size() - 8 >= islength) {

                        ismfile = false;

                        byte[] is_Date=new byte[key_date.size()];

                        for(int i=0;i<key_date.size();i++){
                            is_Date[i]=key_date.get(i);
                        }
                        //接收到结果
                    //    LogUtil.i(TAG, "已经接收完一个完整包");
                        BluetoothLeService.le_cb.on_result(is_Date,Objcode.FRIDY);

                        key_date.clear();

                    }
                }

            }else if(is_Notification() == versions_to){

            //    LogUtil.i(TAG, "接收成功，Data:" + Arrays.toString(gattData)+System.currentTimeMillis());

                if(characteristic.getUuid().equals(UUID_COMMON)){

                    byte[] data = new byte[gattData.length];

                //    System.arraycopy(data, 0, gattData, 1, gattData.length - 1);

                    BluetoothLeService.le_cb.on_result(gattData, Objcode.COMMON);


                }else if(characteristic.getUuid().equals(UUID_DRIVER)){

                //    byte[] data = new byte[gattData.length];

                //    System.arraycopy(data, 0, gattData, 1, gattData.length);

                    BluetoothLeService.le_cb.on_result(gattData, Objcode.DRIVER);

                }else if(characteristic.getUuid().equals(UUID_MESSAGE)){

                    setGattData(Objcode.MESSAGE,gattData);

                }else if(characteristic.getUuid().equals(UUID_FIRMWARE)){

                    setGattData(Objcode.FIRMWARE,gattData);
                }

            }

        } catch (NumberFormatException e) {

            ExceptionUtil.handleException(e);

        }


        //  LogUtil.d(TAG, "接收的数据length="+data.length);

    }


    private void setGattData(byte code, byte[] gattData) {

        if (gattData != null && gattData.length > 0) {

            List<String> Buff_date = new ArrayList<String>();

            for (byte byteChar : gattData) {
                String aa = String.format("%02X", byteChar);
                Buff_date.add(aa);
            }

            if (Buff_date.size() > 6 && Buff_date.get(0).equals("41") && Buff_date.get(1).equals("54") && Buff_date.get(2).equals("2B") && ismfile != true) {
                //这是AT+开头
                islength = Integer.parseInt(Buff_date.get(5) + Buff_date.get(4), 16);

                LogUtil.i(TAG, "接收成功:"+islength);

                key_date.clear();

                for (byte bytes : gattData) {
                    key_date.add(bytes);
                }

                ismfile = true;

            } else if (ismfile) {

                for (byte bytes : gattData) {
                    key_date.add(bytes);
                }
            }
            //已经接收完一个完整包
            if (key_date != null && key_date.size() - 6 >= islength) {

                ismfile = false;

                byte[] is_Date = new byte[key_date.size()];

                for (int i = 0; i < key_date.size(); i++) {
                    is_Date[i] = key_date.get(i);
                }

                LogUtil.i(TAG, "已经接收完一个完整包:"+Arrays.toString(is_Date));

                BluetoothLeService.le_cb.on_result(is_Date, code);

                key_date.clear();

            }
        }
    }


    /**
     * 遍历BluetoothGattCharacteristic找到UUID-00001002所属的BluetoothGattCharacteristic对象
     */
    private void displayGattServices(List<BluetoothGattService> gattServices) {

        if (gattServices == null) {
            return;
        }

        // 遍历 GATT Services.
        for (BluetoothGattService service : gattServices) {

            LogUtil.i(TAG,"getServices="+service.getUuid());

            List<BluetoothGattCharacteristic> gattCharacteristics = service.getCharacteristics();

            if(service.getUuid().equals(UUID_DRIVER_VERSIONS_TO)){

                for(BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics){

                    LogUtil.i(TAG,"ToServicesUUID="+gattCharacteristic.getUuid());

                    if(gattCharacteristic.getUuid().equals(UUID_COMMON)) {

                        gattCharacteristic_Common = gattCharacteristic;

                    }else if(gattCharacteristic.getUuid().equals(UUID_DRIVER)){

                        gattCharacteristic_Driver = gattCharacteristic;

                    }else if(gattCharacteristic.getUuid().equals(UUID_MESSAGE)){

                        gattCharacteristic_Message = gattCharacteristic;

                    }else if(gattCharacteristic.getUuid().equals(UUID_FIRMWARE)){

                        gattCharacteristic_Firmware = gattCharacteristic;

                    }

                }
            }else if(service.getUuid().equals(UUID_DRIVER_VERSIONS_ONE)){

                for(BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics){

                    LogUtil.i(TAG,"OneServicesUUID="+gattCharacteristic.getUuid());

                    if(gattCharacteristic.getUuid().equals(UUID_FRIDY)) {

                        gattCharacteristic_Fridy = gattCharacteristic;

                    }

                }
            }
        }
    }


    public byte is_Notification(){

        if(gattCharacteristic_Fridy != null){

            return versions_one;

        }else if(gattCharacteristic_Common != null
                && gattCharacteristic_Driver != null
                && gattCharacteristic_Message != null
                && gattCharacteristic_Firmware != null){

            return versions_to;

        }


        return versions_erro;

    }

    public static BluetoothLeService get_le_service(Context context, Le_intercface le_if) {

        if (le_service == null) {
            Intent intent = new Intent(context, BluetoothLeService.class);
            context.startService(intent);
        }

    //    LogUtil.i(TAG,"le_service="+le_service);

        BluetoothLeService.le_cb = le_if;

        return BluetoothLeService.le_service;
    }

}