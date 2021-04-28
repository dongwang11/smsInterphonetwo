package com.sms.app.framework.communication.localayer.bledriver;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;


/**
 * Created by Administrator on 2016/7/15.
 * <p>
 * 回调函数
 */
public interface Le_intercface {

    //搜索设备信息
    void on_scan(BluetoothDevice is_device, int rssi, final byte[] scanRecord, BluetoothLeService service);

    //连接状态
    void on_statechange(BluetoothGatt gatt, int status, int newState);

    //接收到结果
    void on_result(byte[] is_Date, Byte aByte);

    //接收到结果
    void on_data(byte[] is_Date);


    void on_notification(byte code);

    //接收到结果
    void on_send_result(byte[] objcode, byte resilt, byte code);

}
