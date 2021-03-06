package com.sms.app.framework.communication.localayer;


import android.content.Context;
import android.location.Location;

import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Command;
import com.sms.app.framework.communication.localayer.cmd.Command_biz;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.respon;

/**
 * Created by Administrator on 2017/3/14.
 */

//本地保存的对讲机设备信息
public class Interphone_driver {

    public static Interphone_driver  itf_driver = null;
    public static Interphone_notify_listenner  notify_listenner = null;
    private Context context=null;



    private String name;        //名字
    private int frequne;        //频率
    private byte bluetooth;     //蓝牙耳机模式
    private byte mode;          //数字 or 模拟 模式

    private String power;       //电量
    private byte bw;            //带宽模式
    private byte sq;            //模拟对讲机静噪等级
    private byte vox;           //vox
    private byte txcode;        //亚音频
    private byte rxcode;        //亚音频




    static public Interphone_driver get_driver(Context cnt)
    {
        if(Interphone_driver.itf_driver==null)
        {
            Interphone_driver.itf_driver = new Interphone_driver(cnt);
        }
        return Interphone_driver.itf_driver;
    }




    public Interphone_driver(Context context) {
        this.context = context;
    }

    public Interphone_driver() {

    }

    public static void add_notify_listenner(Interphone_notify_listenner listenner)
    {
        Interphone_driver.notify_listenner = listenner;
        Command_biz.add_notify_listenner(listenner);
    }



    public respon getName(int opcode) {

        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_CC2540);
        command.setPrica(Objcode.BLE_GATT_CC2540_NAME);

        return Command_biz.send_cmd(this.context,opcode,command);
    }

    public respon setName(int opcode, String name) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_CC2540);
        command.setPrica(Objcode.BLE_GATT_CC2540_NAME);
        command.setValue(name);

        return Command_biz.send_cmd(this.context,opcode,command);
    }

    public respon getFrequne(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_FREQ);

        return Command_biz.send_cmd(this.context,opcode,command);
    }

    public respon setFrequne(int frequne, Integer interphoneFrequne) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_FREQ);
        command.setValue(interphoneFrequne);

        return Command_biz.send_cmd(this.context,frequne,command);
    }

    public respon getBluetooth(int opcode) {

        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_BLUETOOTH);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setBluetooth(int opcode, byte bluetooth) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_BLUETOOTH);
        command.setValue(bluetooth);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getMode(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_MODE);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setMode(int opcode, byte mode) {

        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_MODE);
        command.setValue(mode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getBw(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_BW);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setBw(int opcode, byte bws) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_BW);
        command.setValue(bws);
        return Command_biz.send_cmd(this.context, opcode, command);

    }
    public respon getDisconnec_hite(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_DisconnecHite);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setDisconnec_hite(int opcode, byte disconnec) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_DisconnecHite);
        command.setValue(disconnec);
        return Command_biz.send_cmd(this.context, opcode, command);

    }

    public respon getRf(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_RF);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setRf(int opcode, byte rf) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_RF);
        command.setValue(rf);
        return Command_biz.send_cmd(this.context, opcode, command);

    }

    public respon getTx_power(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxPower);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setTx_power(int opcode, byte txpower) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxPower);
        command.setValue(txpower);
        return Command_biz.send_cmd(this.context, opcode, command);

    }

    public respon getSq(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_SQ);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setSq(int opcode, byte sq) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_SQ);
        command.setValue(sq);
        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getVox(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_VOX);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setVox(int opcode, byte vox) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_VOX);
        command.setValue(vox);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setFindDevice(int opcode, byte findDevice) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_FindDevice);
        command.setValue(findDevice);

        return Command_biz.send_cmd(this.context, opcode, command);
    }


    public respon setNetDisconnect(int opcode, byte netDisconnect) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_netDisconnect);
        command.setValue(netDisconnect);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setwStart(int opcode, byte vox) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_wStart);
        command.setValue(vox);

        return Command_biz.send_cmd(this.context, opcode, command);
    }


    public respon getTxcode(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxCode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setTxcode(int opcode, Integer txcode) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxCode);
        command.setValue(txcode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setIsplay(int opcode, byte isplay) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_Isplay);
        command.setValue(isplay);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getRxcode(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_RxCode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setRxcode(int opcode, Integer rxcode) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_RxCode);
        command.setValue(rxcode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon start_Service(byte opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_SCNN);
        command.setPrica(opcode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getAddress(int opcode, String address) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_CONNECT);
        command.setValue(address);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setAddress(int opcode, String address) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_CONNECT);
        command.setValue(address);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getTx_hite(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxHite);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setTx_hite(int opcode, byte txhite) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_TxHite);
        command.setValue(txhite);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon getStop_hite(int opcode) {
        Command command = new Command();
        command.setOpcode(Opcode.GET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_StopHite);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setStop_hite(int opcode, byte stophite) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_AT1846S);
        command.setPrica(Objcode.BLE_GATT_AT1846S_StopHite);
        command.setValue(stophite);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setMsgResponse(int opcode, MsgResponse mode) {


        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.FILE_TEXT);
        command.setPrica(Objcode.BLE_GATT_AT1846S_POWER);
        command.setValue(mode);

        return Command_biz.send_cmd(this.context, opcode, command);

        /*if(mode.getType() == MsgResponse.Text){


        }else{

            Command command = new Command();

            command.setOpcode(Opcode.SET);
            command.setObjcode(Objcode.FILE_VOICE);

            command.setValue(mode);

            return Command_biz.send_cmd(this.context, opcode, command);

        }*/

    }

    public respon setFirmware(int opcode, String firepath) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_FIRMWARE);
        command.setValue(firepath);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setAprs(int opcode, Location location) {
        Command command = new Command();
        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_GATT_APRS);
        command.setPrica(Objcode.BLE_GATT_APRS_POSITION);
        command.setValue(location);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setImCode(int opcode, ImCode imCode) {

        Command command = new Command();

        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_IM);
        command.setValue(imCode);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setUserId(int opcode, Long userid) {

        Command command = new Command();

        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_USERID);
        command.setValue(userid);

        return Command_biz.send_cmd(this.context, opcode, command);
    }

    public respon setFactory_reset(int opcode, Byte factory_reset) {

        Command command = new Command();

        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_FACTIRY_RESET);
        command.setValue(factory_reset);

        return Command_biz.send_cmd(this.context, opcode, command);
    }


    public respon setDevice_reset(int opcode, String device_reset) {

        Command command = new Command();

        command.setOpcode(Opcode.SET);
        command.setObjcode(Objcode.BLE_INTERPHONE);
        command.setPrica(Objcode.BLE_INTERPHONE_DEVICE_RESET);
        command.setValue(device_reset);
        return Command_biz.send_cmd(this.context, opcode, command);
    }

}
