package com.sms.app.interphone.services;

import android.content.Context;
import android.util.Log;

import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.bledriver.Ble_request;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.cmd.Command;
import com.sms.app.framework.communication.localayer.cmd.Command_biz;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.communication.localayer.open_notify_listenner;
import com.sms.app.framework.respon;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.util.openutil.op_intercface;

import org.jivesoftware.smack.packet.Packet;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14.
 */

public class Open_biz {


    public static final byte Register = 1;
    public static final byte Login = 2;
    public static final byte Newfrom = 3;
    public static final byte Joinfrom = 4;
    public static final byte Leavefrom = 5;
    public static final byte SendMessage = 6;
    public static final byte Connection = 7;

    public static final byte Ping = 8;
    public static final byte Esc = 9;
    public static final byte Apply = 10;
    public static final byte Result = 11;
    public static final byte ChatMessage = 12;





    public static open_notify_listenner notify_listenner = null;


    public static final byte YES = 1;
    public static final byte NO = 0;


    private OpenfireService service = null;


    private Op_request request = null;

    public static void add_notify_listenner(open_notify_listenner listenner)
    {
        Open_biz.notify_listenner = listenner;
    }

    public static Open_biz dorequest(Context context, Openfrom openfrom, Op_request op_request) {

        Open_biz open_biz = new Open_biz(context,openfrom,op_request);

        return  open_biz;
    }

    public static respon send_open(Context context, final int opcode, final Openfrom openfrom) {


        final respon rsp[] = new respon[1];

        rsp[0] = null;

        Open_biz.dorequest(context,openfrom, new Op_request() {
            @Override
            public void respone(byte objcode, Object obj) {

                if(Open_biz.notify_listenner!=null)
                {
                    //????????????????????????????????????????????????????????????????????????
                    Open_biz.notify_listenner.onNotify(objcode,obj);
                }

                rsp[0] = new respon();
                rsp[0].setOpcode(objcode);
                rsp[0].setObj(obj);

            }
        });

        return rsp[0];

    }

    //??????openfireService????????????

    public Open_biz(Context context, Openfrom openfrom, Op_request op_request){

        this.request = op_request;

        if(service == null){

            service = OpenfireService.get_le_service(context, new op_intercface() {
                @Override
                public void on_context(OpenfireService le_service) {
                    service = le_service;

                }

                @Override
                public void on_state(boolean connection) {

                    if(connection){

                        request.respone(Connection,YES);

                    }else{
                        request.respone(Connection,NO);
                    }

                }

                @Override
                public void on_login(byte type) {

                    request.respone(Login,type);

                }

                @Override
                public void on_Register(byte type) {
                    request.respone(Register,type);

                }

                @Override
                public void on_newfrom(byte type) {

                    request.respone(Newfrom,type);

                }

                @Override
                public void on_joinfrom(byte type) {

                    request.respone(Joinfrom,type);

                }

                @Override
                public void on_destroyfrom(byte type) {

                    request.respone(Leavefrom,type);

                }

                @Override
                public void on_message(MsgResponse response) {

                    request.respone(SendMessage,response);

                }

                @Override
                public void on_ping(byte code) {

                    request.respone(Ping,code);
                }

                @Override
                public void on_Apply(MsgResponse msgResponse) {
                    request.respone(Apply,msgResponse);
                }


            });

        }

        this.setOpenfrom(openfrom);
    }

    public void setOpenfrom(Openfrom openfrom){

        LogUtil.i(GlobalConsts.TAG,"Open_biz:,openfrom:"+openfrom.toString());

        if(OpenfireService.le_service == null){

            return;
        }

        if(openfrom.getCode() == Register){
            LogUtil.i("ddddddddddd","11111111111111");
            OpenfireService.le_service.Register(openfrom);

        } else if(openfrom.getCode() == Login){
            LogUtil.i("ddddddddddd","lolololoololoo");
            OpenfireService.le_service.Login(openfrom);

        } else if(openfrom.getCode() == Newfrom){

            OpenfireService.le_service.newfrom(openfrom);

        } else if(openfrom.getCode() == Joinfrom){

            OpenfireService.le_service.joinfrom(openfrom);

        } else if(openfrom.getCode() == Leavefrom){

            OpenfireService.le_service.destroyfrom(openfrom);

        } else if(openfrom.getCode() == SendMessage){

            OpenfireService.le_service.sendMessage(openfrom);

        }else if(openfrom.getCode() == Apply){

            OpenfireService.le_service.sendApply(openfrom);

        }else if(openfrom.getCode() == Esc){

            OpenfireService.le_service.esc();

        }else if(openfrom.getCode() == ChatMessage){

            OpenfireService.le_service.ChatMessage(openfrom);

        }

        /*new Thread(new Runnable() {
            @Override
            public void run() {

            }
        }).start();*/
    }

}
