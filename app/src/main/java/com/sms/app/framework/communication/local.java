package com.sms.app.framework.communication;

import android.content.Context;

import com.sms.app.framework.communication.localayer.Interphone_driver;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.respon;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.util.openutil.Openfrom;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/3/14.
 */

public class local {

    static String TAG="smsfw_local";
    static public local local_interface = null;
    private Context context=null;

    public  ConcurrentHashMap<String,attr_listenner> getListenners() {
        return listenners;
    }

    private ConcurrentHashMap<String,attr_listenner> listenners =  new ConcurrentHashMap();



    public local()
    {
        super();
    }
    public local(Context cnt)
    {
        super();
        this.context = cnt;
    }

    public void add_attr_listenner(attr_listenner listenner)
    {

        this.listenners.put(listenner.getContext().getClass().toString().substring(6),listenner);
    //    LogUtil.i(GlobalConsts.TAG,"laoshiji"+listenner.getContext().getClass().toString().substring(6));

    }
    public respon dorequest(final Post_obj obj) {

            respon rsp = null;
            int opcode = obj.getOpcode();
            if(! (obj.getObj() instanceof Interphone))
            {//传进来的参数不是对讲机对象
                return null;
            }
            Interphone interphone = (Interphone)(obj.getObj());

            // private String name;        //名字
            if(interphone.getName()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return Interphone_driver.get_driver(this.context).getName(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setName(opcode,interphone.getName());

                    default:
                        break;

                }
            }
            // private Integer frequne;        //频率
            if(interphone.getFrequne()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:

                        return Interphone_driver.get_driver(this.context).getFrequne(opcode);

                    case sms_request.SET:

                        return Interphone_driver.get_driver(this.context).setFrequne(opcode,interphone.getFrequne());

                    default:
                        break;

                }
            }
            // private Byte mode;          //数字 or 模拟 模式
            if(interphone.getMode()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return Interphone_driver.get_driver(this.context).getMode(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setMode(opcode,interphone.getMode());

                    default:
                        break;

                }
            }
            // private Byte bw;            //带宽模式
            if(interphone.getBw()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getBw(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setBw(opcode,interphone.getBw());

                    default:
                        break;

                }
            }

            if(interphone.getDisconnec_hite()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getDisconnec_hite(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setDisconnec_hite(opcode,interphone.getDisconnec_hite());

                    default:
                        break;

                }
            }

            if(interphone.getRf()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getRf(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setRf(opcode,interphone.getRf());

                    default:
                        break;

                }
            }

            if(interphone.getTx_power()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getTx_power(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setTx_power(opcode,interphone.getTx_power());

                    default:
                        break;

                }
            }

            if(interphone.getBluetooth()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getBluetooth(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setBluetooth(opcode,interphone.getBluetooth());

                    default:
                        break;

                }
            }


            // private Byte sq;            //模拟对讲机静噪等级
            if(interphone.getSq()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getSq(opcode);

                    case sms_request.SET:
                        return  Interphone_driver.get_driver(this.context).setSq(opcode,interphone.getSq());

                    default:
                        break;

                }
            }
            // private Byte vox;           //vox
            if(interphone.getVox()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getVox(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setVox(opcode,interphone.getVox());

                    default:
                        break;

                }
            }

        // private Byte vox;           //vox
        if(interphone.getFindDevice()!=null)
        {
            switch (opcode)
            {
                case sms_request.SET:
                    return Interphone_driver.get_driver(this.context).setFindDevice(opcode,interphone.getFindDevice());

                default:
                    break;

            }
        }



        if(interphone.getNetDisconnect()!=null)
        {
            switch (opcode)
            {
                case sms_request.SET:
                    return Interphone_driver.get_driver(this.context).setNetDisconnect(opcode,interphone.getNetDisconnect());

                default:
                    break;

            }
        }

            // private Byte vox;           //vox
            /*if(interphone.getwStart()!=null)
            {
                switch (opcode)
                {

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setwStart(opcode,interphone.getwStart());

                    default:
                        break;

                }
            }*/

            // private Byte txcode;        //亚音频
            if(interphone.getTxcode()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getTxcode(opcode);

                    case sms_request.SET:
                        return  Interphone_driver.get_driver(this.context).setTxcode(opcode,interphone.getTxcode());

                    default:
                        break;

                }
            }
            //private Byte rxcode;        //亚音频
            if(interphone.getRxcode()!=null)
            {
                switch (opcode)
                {
                    case sms_request.GET:
                        return  Interphone_driver.get_driver(this.context).getRxcode(opcode);

                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setRxcode(opcode,interphone.getRxcode());

                    default:
                        break;

                }
            }

        //private Byte rxcode;        //亚音频
        if(interphone.getIsplay()!=null)
        {
            switch (opcode)
            {

                case sms_request.SET:
                    return Interphone_driver.get_driver(this.context).setIsplay(opcode,interphone.getIsplay());

                default:
                    break;

            }
        }


            if(interphone.getAddress()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setAddress(opcode,interphone.getAddress());

                    case sms_request.GET:
                        return Interphone_driver.get_driver(this.context).getAddress(opcode,interphone.getAddress());

                    default:
                        break;
                }


            }


            if(interphone.getTx_hite() != null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setTx_hite(opcode,interphone.getTx_hite());

                    case sms_request.GET:
                        return Interphone_driver.get_driver(this.context).getTx_hite(opcode);

                    default:
                        break;
                }


            }

            if(interphone.getStop_hite() != null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setStop_hite(opcode,interphone.getStop_hite());

                    case sms_request.GET:
                        return Interphone_driver.get_driver(this.context).getStop_hite(opcode);

                    default:
                        break;
                }


            }

            if(interphone.getMsgResponse()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setMsgResponse(opcode,interphone.getMsgResponse());

                    default:
                        break;
                }

            }
            if(interphone.getFirmware() != null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setFirmware(opcode,interphone.getFirmware());

                    default:
                        break;
                }

            }
            if(interphone.getLocation()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setAprs(opcode,interphone.getLocation());

                    default:
                        break;
                }

            }
            if(interphone.getImCode()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:
                        return Interphone_driver.get_driver(this.context).setImCode(opcode,interphone.getImCode());

                    default:
                        break;
                }

            }

            if(interphone.getUserId()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:

                        return Interphone_driver.get_driver(this.context).setUserId(opcode,interphone.getUserId());

                    default:
                        break;
                }

            }

            if(interphone.getFactory_reset()!=null)
            {

                switch (opcode)
                {
                    case sms_request.SET:

                        return Interphone_driver.get_driver(this.context).setFactory_reset(opcode,interphone.getFactory_reset());

                    default:
                        break;
                }

            }


        if(interphone.getDevice_reset()!=null)
        {

            switch (opcode)
            {
                case sms_request.SET:

                    return Interphone_driver.get_driver(this.context).setDevice_reset(opcode,interphone.getDevice_reset());

                default:
                    break;
            }

        }

            if(interphone.getConnect() != null)
            {

                LogUtil.i(TAG,"interphone.getConnect():"+interphone.getConnect());
                return Interphone_driver.get_driver(this.context).start_Service(interphone.getConnect());

            }

            return rsp;


    }
    static public local get_local(Context cnt, Interphone_notify_listenner local_listner)
    {
        if(local.local_interface==null)
        {
            local.local_interface = new local(cnt);
        }
        Interphone_driver.add_notify_listenner(local_listner);
        return local.local_interface;
    }
}
