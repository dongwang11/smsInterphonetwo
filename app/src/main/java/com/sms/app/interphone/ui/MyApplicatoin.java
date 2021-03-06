package com.sms.app.interphone.ui;

import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.location.Location;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.os.Environment;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.ImCode;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.BluetoothLeService;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.bledriver.Message_request;
import com.sms.app.framework.communication.localayer.cmd.ATcommand;
import com.sms.app.framework.communication.localayer.cmd.Ble_message;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.communication.localayer.cmd.Result;
import com.sms.app.framework.communication.remote;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.DAOUser;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOUserDao;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.framework.respon;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.framework.trans.bean.Avatar;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.framework.trans.bean.Production;
import com.sms.app.framework.trans.bean.User;
import com.sms.app.framework.trans.sms_fw_api;
import com.sms.app.interphone.R;
import com.sms.app.interphone.entity.Filter;
import com.sms.app.interphone.entity.Mesage_entity;
import com.sms.app.interphone.entity.Voice;
import com.sms.app.interphone.entity.Voicelist;
import com.sms.app.interphone.services.NetworkStateChangedMsnReceiver;
import com.sms.app.interphone.services.Op_request;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.services.OpenfireService;
import com.sms.app.interphone.ui.activity.BlescanActivity;
import com.sms.app.interphone.ui.activity.LoginActivity;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.interphone.util.msnutil.MessageFilter;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.util.voiceutil.SpeexRecorder.VoiceBleRunnable;
import com.sms.app.interphone.util.voiceutil.SpeexRecorder.Voice_Read_Thread;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smackx.delay.packet.DelayInformation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


/**
 * Created by Administrator on 2016/8/4. 1.0 open fire ?????????
 */


public class MyApplicatoin extends Application {

    public static boolean guolv = false;
    public static boolean esc_sms = false;


    public static int p = 0;
//    public static int ping = 0;

    //???????????????
    public static int voice_size = 90;


    /**????????????*/
    public static Interphone interphone = new Interphone();

    public static Production production= null;

    private static String TAG = "YanShi...Log - MyApplicatoin";

    /**????????????*/
    public static User evenUser = null;

    /**ptt*/
    public static boolean ptt = true;

    /**??????APK ??????*/
    public static boolean isUpdata = false;

    /**??????APK ??????*/
    public static boolean isToos = false;

    /**BLE????????????*/
    public static Boolean IS_BLE = false;

    /**BLE??????????????????*/
    public static Boolean IS_BLE_VOICE = false;

    /**open?????????????????? */
    public static Boolean IS_OPEN_JOIN = false;

    /**???????????????*/
    public static Openfrom openfrom = null;

    /**open???????????? */
    public static Boolean IS_OPEN_LOGIN = false;

    /**??????????????????*/
    public static Boolean BLE_APRS = false;

    /**??????????????????*/
    public static AudioRecord audioRecord = null;
    /**??????????????????*/
    public static AudioTrack audioTrack = null;


    public static List<String> groupMems = new ArrayList<>();

    public static int NETWORK = 0;
    /**???????????????*/
    public static final int NETWORK_TYPE_NONE = 0;
    /**WiFi*/
    public static final int NETWORK_TYPE_WIFI = 1;
    /**??????*/
    public static final int NETWORK_TYPE_MOBILE = 2;
    /**????????????????????????*/
    public static int current_network_type = NETWORK_TYPE_NONE;


    /**??????????????????   false   ????????????  true*/

    public static final boolean isRelease = false;

    public static ImCode imCode = new ImCode();
    /**??????????????????*/

    public static Map<Long,Location> mapList = new HashMap<Long,Location>();

    public static Context mContext = null;

    public static String versionCode = null;

    public static List<LatLng> pointList = new ArrayList<LatLng>();     //????????????????????????????????????

//    public static List<Long> joins = new ArrayList<Long>();


    public static byte interPhoneRF = 1;//1????????????2?????????
    /**??????BLE??????*/
    public static byte dataCode = 0;

    private NetworkStateChangedMsnReceiver receiver;

    public static long startA = 0;
    public static long startB = 0;





    /**
     * ????????????????????????onCreate(),?????????????????????Tappliation??????
     *
     * ??????????????????
     *
     * */

    private static Handler mhandler = new Handler(){
        @Override
        public void handleMessage(android.os.Message msg) {

            switch (msg.what){

                case GlobalConsts.VOICE_BLE:

                    try {
                        Voice voice = (Voice) msg.obj;

                    //    LogUtil.i(TAG,"mhandler VOICE_BLE");

                        if(MyApplicatoin.interphone != null){

                            if(MyApplicatoin.interphone.getRf() == null){

                                return;

                            }

                            newVoiceMessage(voice);

                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                    break;
            }
        }
    };

    public static void getSmsUser(final Context context) {


        SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalConsts.USER, Context.MODE_PRIVATE);
        String userString =sharedPreferences.getString(GlobalConsts.USER_ID, null);

        final DAOUserDao userDao = DAOService.get(context).getsession().getDAOUserDao();

        DAOUser daoUser = null;

        if(userString != null){

            LogUtil.i(TAG,"???????????????????????????");

            final User user = new User();
            user.setId(Long.parseLong(userString));

            try {

                daoUser = userDao.queryBuilder().where(DAOUserDao.Properties.Remote_id.eq(Long.parseLong(userString))).orderAsc(DAOUserDao.Properties.Remote_id).unique();

                if(daoUser != null){

                    user.setAvatar_url(daoUser.getAvatar_url());
                    user.setName(daoUser.getName());

                }

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            MyApplicatoin.evenUser = user;

            sendRequest(sms_request.GET,user);

        }else{

            LogUtil.i(TAG,"?????????");

            int start = GlobalConsts.ACTTION_LOGIN_ERRO;

            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
            intent.putExtra(GlobalConsts.ACTION_USER, start);

            context.sendBroadcast(intent);
        }

    }


    @Override
    public void onCreate() {
        super.onCreate();

        //????????????
       /* Locale locale = Locale.ENGLISH;
         *//*  if (pref_lang.equals(Constants.LANGUAGE_ZH_HK))
            locale = Locale.TRADITIONAL_CHINESE;
        else if (pref_lang.equals(Constants.LANGUAGE_ZH_CN))
            locale = Locale.SIMPLIFIED_CHINESE;*//*
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());*/

        try {

            mContext = getApplicationContext();
            SDKInitializer.initialize(mContext);

            startservices();

            setInterphone(new Interphone());

            p++;

            if(receiver == null){

                receiver = new NetworkStateChangedMsnReceiver();
                registerReceiver(receiver,new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));

            }

            versionCode = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionName;

        }catch(Exception e) {
           ExceptionUtil.handleException(e);
        }
    }

    private void startservices() {

        Open_biz.dorequest(mContext, new Openfrom(), new Op_request() {
            @Override
            public void respone(byte objcode, Object obj) {

            }
        });
    }

    public static void showMessage(final Context context, String message, int type) {

        LogUtil.i(TAG,"?????????????????????");
        Log.d("resultString","a1111111111111111111111111111111???"+type);
        if(current_network_type == NETWORK_TYPE_NONE && type == NETWORK_TYPE_NONE){

            LogUtil.i(TAG,"???????????????????????????");

            OpenfireService.open_user_join.clear();
            OpenfireService.open_user.clear();
            setRF();


            Intent intent_erro = new Intent(GlobalConsts.ACTION_LOGIN);

            intent_erro.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_ERRO_SHOW);

            MyApplicatoin.mContext.sendBroadcast(intent_erro);

            SharedPreferences sharedPreferences = context.getSharedPreferences(GlobalConsts.USER, Context.MODE_PRIVATE);
            String userString =sharedPreferences.getString(GlobalConsts.USER_ID, null);

            final DAOUserDao userDao = DAOService.get(context).getsession().getDAOUserDao();

            DAOUser daoUser = null;

            if(userString != null){

                daoUser = userDao.queryBuilder().where(DAOUserDao.Properties.Remote_id.eq(Long.parseLong(userString))).orderAsc(DAOUserDao.Properties.Remote_id).unique();

            }

            if(daoUser != null) {

                User u = new User();

                u.setId(daoUser.getRemote_id());
                u.setAvatar_url(daoUser.getAvatar_url());
                u.setName(daoUser.getName());

                MyApplicatoin.evenUser = u;

                int start = GlobalConsts.ACTTION_LOGIN_SUCCESS;

                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
                intent.putExtra(GlobalConsts.ACTION_USER, start);

                context.sendBroadcast(intent);

            }else{

                LogUtil.i(TAG,"?????????");

                int start = GlobalConsts.ACTTION_LOGIN_ERRO;

                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
                intent.putExtra(GlobalConsts.ACTION_USER, start);

                context.sendBroadcast(intent);
            }
            Log.d("resultString","a2222222222222222???"+type);
        }else if(current_network_type != NETWORK_TYPE_NONE && type == NETWORK_TYPE_NONE){

            LogUtil.i(TAG,"???????????????????????????");

            Intent intent_erro = new Intent(GlobalConsts.ACTION_LOGIN);

            intent_erro.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_ERRO_SHOW);

            MyApplicatoin.mContext.sendBroadcast(intent_erro);

        //    ping = 0;

            OpenfireService.open_user_join.clear();
            OpenfireService.open_user.clear();
            setRF();
            //?????????????????????????????????
            Interphone mInterphone = new Interphone();
            mInterphone.setNetDisconnect((byte)0x02);
            MyApplicatoin.setInterphone(mInterphone);
            Log.d("resultString","a333333333333333???"+type);
        }else if(current_network_type == NETWORK_TYPE_NONE && type != NETWORK_TYPE_NONE){

            LogUtil.i(TAG,"???????????????????????????");

            getSmsUser(context);

            Intent intent_erro = new Intent(GlobalConsts.ACTION_LOGIN);

            intent_erro.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_ERRO_HITE);

            MyApplicatoin.mContext.sendBroadcast(intent_erro);
            Log.d("resultString","a4444444444444???"+type);
        }else if(current_network_type != NETWORK_TYPE_NONE && type != NETWORK_TYPE_NONE){

            LogUtil.i(TAG,"???????????????????????????");
            Log.d("resultString","a55555555555555???"+type);
        }

        current_network_type = type;


    }
    /**
     *
     * ??????openfire
     *
     * */
    private static void Login_Openfire() {

        Openfrom openfrom = new Openfrom();

        openfrom.setUserId(MyApplicatoin.evenUser.getIm().getUser_id());
        openfrom.setPassword(MyApplicatoin.evenUser.getIm().getIm_passwd());
        openfrom.setCode(Open_biz.Login);

        if(MyApplicatoin.interphone != null){

            if(MyApplicatoin.interphone.getImCode() != null){

                if(MyApplicatoin.interphone.getImCode().getId() != 0){

                    openfrom.setFromName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                }

            }
        }

        setOpenfire(openfrom);

    }


    static Map<Long,Long> listp = new HashMap<>();
    static long stp = 0;
    static long kep = 0;


    /**
     *
     * ???????????????????????????
     *
     * */
    public static void setOpenfire(final Openfrom openfrom) {

        sms_fw_api.do_request(mContext, Open_biz.Login, openfrom, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

            }
        });

        sms_fw_api.add_open_listenner(mContext, new attr_listenner() {
            @Override
            public void onAttributeChang(int objcode, Object obj) {



                if(objcode == Open_biz.Connection){

                    byte type = (byte) obj;

                    if(type == Open_biz.NO){
                        LogUtil.i(TAG,"????????????");
                    //    MyApplicatoin.evenUser = null;
                    }

                }else if(objcode == Open_biz.Register){
                    byte type = (byte) obj;

                    if(type == Open_biz.NO){
                        LogUtil.i(TAG,"????????????");
                    //    MyApplicatoin.evenUser = null;
                    }

                }else if(objcode == Open_biz.Login){

                    byte type = (byte) obj;

                    if(type == Open_biz.NO){
                        LogUtil.i(TAG,"????????????");
                    //    MyApplicatoin.evenUser = null;
                    }

                }else if(objcode == Open_biz.Newfrom){

                    byte type = (byte) obj;

                    if(type == GlobalConsts.YES){

                        //??????????????????????????????????????????????????????
                        MyApplicatoin.IS_OPEN_JOIN = true;

                        LogUtil.i(TAG,"???????????????????????????????????????");


                    }else{

                        LogUtil.i(TAG,"??????????????????");

                    }

                    updataUI(Open_biz.Newfrom,type);

                }else if(objcode == Open_biz.Joinfrom){

                    byte type = (byte) obj;

                    if(type == GlobalConsts.YES){
                        //?????????????????????????????????
                        MyApplicatoin.IS_OPEN_JOIN = true;

                        LogUtil.i(TAG,"????????????");

                    }else{

                        LogUtil.i(TAG,"????????????");
                    }

                    updataUI(Open_biz.Joinfrom,type);

                }else if(objcode == Open_biz.Ping){

                    byte type = (byte) obj;

                    if(type == GlobalConsts.YES){

                        //????????????

                        LogUtil.i(TAG,"????????????");

                      //  ping++;

                        /*if(ping == 3){
                            //????????????????????????

                        }*/

                    }else{

                    //   ping = 0;

                        //????????????????????????

                        OpenfireService.open_user_join.clear();
                        OpenfireService.open_user.clear();
                        setRF();

                        LogUtil.i(TAG,"????????????");


                    }

                    updataUI(Open_biz.Ping,type);


                }else if(objcode == Open_biz.Leavefrom){

                    byte type = (byte) obj;

                    if(type == GlobalConsts.YES){
                        LogUtil.i(TAG,"????????????");
                       /* mesage_entity_list.clear();
                        if (adapter != null) {
                            adapter.notifyDataSetChanged();

                        }*/

                    }else{
                        LogUtil.i(TAG,"????????????");
                    }

                    updataUI(Open_biz.Leavefrom,type);

                }else if(objcode == Open_biz.Apply){

                    MsgResponse response = (MsgResponse) obj;

                    LogUtil.i(TAG,"???????????????" + response);

                    if(interphone.getImCode() != null){

                        Mesage_entity msg = new Mesage_entity();

                        msg.setUser_id(response.getUserId());
                        msg.setMesg_type(response.getType());
                        msg.setFrom_id(Long.valueOf(response.getFormName()));
                        msg.setContent(response.getContent());
                        msg.setMgid(response.getPacketID());

                        updataListView(msg);

                    }else{

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                while (interphone.getImCode() == null){

                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                Mesage_entity msg = new Mesage_entity();

                                msg.setUser_id(response.getUserId());
                                msg.setMesg_type(response.getType());
                                msg.setFrom_id(Long.valueOf(response.getFormName()));
                                msg.setContent(response.getContent());
                                msg.setMgid(response.getPacketID());

                                updataListView(msg);


                            }
                        }).start();

                    }



                }else if(objcode == Open_biz.SendMessage){

                    if(!IS_BLE){
                        return;
                    }

                    try {

                        MsgResponse msgRes  = (MsgResponse) obj;

                        LogUtil.i(TAG,"openfire?????????");

                        //?????????PTT?????? ??????????????????

                        if(msgRes.getType() == MsgResponse.Ptt){

                            long ptt = Long.valueOf(msgRes.getContent());

                                LogUtil.i(TAG,"OpenFire???????????????PTT??????:"+ptt);

                            long time = msgRes.getTime().getTime();

                            if(msgRes.getUserId() != MyApplicatoin.evenUser.getId()){

                                if(ptt == 1){

                                    startB = time;

                                    listp.put(msgRes.getUserId(),time);

                                    if(MyApplicatoin.interphone.getState() != null){

                                        if(startB > startA){

                                            if(MyApplicatoin.interphone.getState() == 3) {

                                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                                intent.putExtra(GlobalConsts.ACTION_TYPE, GlobalConsts.ACTTION_IS_START);
                                                intent.putExtra(GlobalConsts.ACTION_MSG, (byte) 2);

                                                MyApplicatoin.mContext.sendBroadcast(intent);
                                            }

                                        }else{

                                            if(MyApplicatoin.interphone.getState() == 3) {

                                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                                intent.putExtra(GlobalConsts.ACTION_TYPE, GlobalConsts.ACTTION_IS_START);
                                                intent.putExtra(GlobalConsts.ACTION_MSG, (byte) 4);

                                                MyApplicatoin.mContext.sendBroadcast(intent);
                                            }

                                        }

                                    }


                                }else{

                                    Iterator it = listp.keySet().iterator();

                                    while(it.hasNext()) {

                                        Long key = (Long)it.next();

                                        Long times = listp.get(key);

                                        if(stp == 0){

                                            stp = times;
                                            kep = key;

                                        }

                                        if(times < stp){

                                            kep = key;

                                        }else{

                                            if((times - stp) > 500){
                                                kep = key;
                                            }

                                        }

                                    //    LogUtil.i(TAG,"startB:"+key+",times:"+times);

                                    }


                                    if(kep != 0 && kep == msgRes.getUserId()){

                                        if(MyApplicatoin.interphone.getState() != null){

                                            if(MyApplicatoin.interphone.getState() == 3) {

                                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                                intent.putExtra(GlobalConsts.ACTION_TYPE, GlobalConsts.ACTTION_IS_START);
                                                intent.putExtra(GlobalConsts.ACTION_MSG, (byte) 4);



                                                MyApplicatoin.mContext.sendBroadcast(intent);
                                            }

                                        }


                                        listp.clear();

                                        stp = 0;
                                    }

                                }

                            }else {

                                if(ptt == 1){

                                    startA = time;

                                    listp.put(msgRes.getUserId(),time);

                                    if(startA > startB){

                                        if((startA - startB) < 500){

                                            if(MyApplicatoin.interphone.getState() != null){

                                                if(MyApplicatoin.interphone.getState() == 3) {

                                                    Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                                    intent.putExtra(GlobalConsts.ACTION_TYPE, GlobalConsts.ACTTION_IS_START);
                                                    intent.putExtra(GlobalConsts.ACTION_MSG, (byte) 4);

                                                    MyApplicatoin.mContext.sendBroadcast(intent);
                                                }

                                            }

                                        }


                                    }
                                }else{
                                    listp.clear();
                                    stp = 0;

                                }

                            }


                        }else if(msgRes.getType() == MsgResponse.Aprs){


                            //??????????????????

                            if(msgRes.getUserId() != MyApplicatoin.evenUser.getId()){

                                int code = msgRes.getContent().indexOf(",");

                                double latitude = Double.valueOf(msgRes.getContent().substring(0,code));

                                double longitude = Double.valueOf(msgRes.getContent().substring(code+1,msgRes.getContent().length()));

                                Location locion = new Location("");

                                locion.setLatitude(latitude);
                                locion.setLongitude(longitude);


                                boolean isExist  = false;

                                for(Long key : MyApplicatoin.mapList.keySet()){

                                    if(msgRes.getUserId() == key){

                                        isExist = true;
                                        MyApplicatoin.mapList.get(key).set(locion);

                                    }

                                }

                                if(!isExist){

                                    MyApplicatoin.mapList.put(msgRes.getUserId(),locion);

                                }

                                Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                                intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_MSG);

                                MyApplicatoin.mContext.sendBroadcast(intent1);

                                LogUtil.i(TAG,"?????????????????????"+latitude+","+longitude);

                            }

                        }else{


                            Filter filter = new Filter();

                            filter.setPacketID(Long.valueOf(msgRes.getPacketID()));
                            filter.setTime(msgRes.getTime().getTime());
                            filter.setType(msgRes.getType());
                            filter.setUserId(msgRes.getUserId());
                            filter.setCode(Filter.openfire);
                            filter.setGroupName(msgRes.getFormName());

                            if(msgRes.getType() != MsgResponse.Voice){

                                if(MyApplicatoin.evenUser.getId() == msgRes.getUserId()){

                                    LogUtil.i(TAG,"????????????");

                                    Mesage_entity msg = new Mesage_entity();


                                    msg.setUser_id(msgRes.getUserId());
                                    msg.setMgid(msgRes.getPacketID());
                                    msg.setMesg_type(msgRes.getType());

                                    Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                    intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_MSG_OK);
                                    intent.putExtra(GlobalConsts.ACTION_MSG,msg);

                                    MyApplicatoin.mContext.sendBroadcast(intent);

                                }else{

                                    if(msgRes.getType() == MsgResponse.Join || msgRes.getType() == MsgResponse.Into || msgRes.getType() == MsgResponse.Exit || msgRes.getType() == MsgResponse.Leave){

                                        if(interPhoneRF != 1){

                                            //RF?????????????????????

                                            setRF();

                                        }

                                    }
                                }

                                if(msgRes.getType() == MsgResponse.Leave && msgRes.getUserId() == MyApplicatoin.evenUser.getId()){
                                    return;
                                }



                                startMessageFilter(filter,msgRes);

                            }else{

                                setOpenfireVoice(filter,msgRes);

                            }
                        }

                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }
                }
            }
        });

    }


    private static long us = 0;
    private static long ps = 0;

    private static String firename;

    private static List<Byte> voice_pack = new ArrayList();

    private static List<Byte> voice_test = new ArrayList();


//    private static List<Byte> voice_open_pack = new ArrayList();


    private static TimerTask timevoice;
    private static Timer taskvoice = new Timer();
    private static int recLenvoice = 5;

    private static byte voice_code = 0;

    private static MsgResponse msgRess;
    private static Filter filters;

    private static void startVoice(final Filter filter, final MsgResponse msgRes) {

        msgRess = msgRes;
        filters = filter;


        if(timevoice == null){
            try {
                recLenvoice = 5;
                timevoice = new TimerTask() {
                    @Override
                    public void run() {
                        recLenvoice--;
                        if(recLenvoice <= 0){
                            if(timevoice != null){
                                timevoice.cancel();
                            }

                            timevoice=null;

                            voice_code = 120;

                            setVoice(filters,msgRess);

                        }
                    }
                };
                taskvoice.schedule(timevoice, 100, 100);

            } catch (Exception e) {

                ExceptionUtil.handleException(e);

            }
        }else{
            recLenvoice=5;
        }

    }

    private static void setVoice(Filter filter, MsgResponse msgRes) {

    //    LogUtil.i(TAG,"111111OpenfireVoice:??????");



        boolean isk = false;

        for(int i = 0;i<phs.size();i++){

            if(phs.get(i) == msgRes.getPacketID()){
                isk = true;
                phs.remove(i);
                break;
            }

        }


        byte[] bytes = new byte[voice_pack.size()];

        for (int i = 0; i < voice_pack.size(); i++) {

            bytes[i] = voice_pack.get(i);

        }

        voice_pack.clear();

        if(voice_test.size() >= 750 && !isk){

            if(msgRes.getUserId() == MyApplicatoin.evenUser.getId()){

                Openfrom openfrom = new Openfrom();

                openfrom.setType(MsgResponse.Voice);

                String msg = Base64.encodeToString(bytes, Base64.DEFAULT);

                byte[] a = {120};

            //    openfrom.setMessage(new String(a)+msgRes.getFireName() + msg);
                openfrom.setMessage(new String(a) + msg);

                openfrom.setMessageID(msgRes.getPacketID());
                openfrom.setCode(Open_biz.SendMessage);
                openfrom.setUserId(MyApplicatoin.evenUser.getId());

                MyApplicatoin.setOpenfire(openfrom);

            }


            MsgResponse response = new MsgResponse();

            response.setPacketID(msgRes.getPacketID());
            response.setType(msgRes.getType());

            response.setFormName(msgRes.getFormName());

            byte[] bs = new byte[voice_test.size()];

            for(int i = 0;i<voice_test.size()-1;i++){
                bs[i] = voice_test.get(i);
            }

            String msg = Base64.encodeToString(bs, Base64.DEFAULT);
            response.setContent(msg);
            //Log.d("fdfdfdd","addMessageLieret:"+msg+ "                  1?????????             " +Base64.decode(msg, Base64.DEFAULT).length+"            777777777");
            response.setTime(msgRes.getTime());
            response.setUserId(msgRes.getUserId());
        //    response.setFireName(msgRes.getFireName());

            startMessageFilter(filter,response);

        }

        voice_test.clear();

    }

    private static List<Long> phs = new ArrayList<>();
    public static List<Long> packs = new ArrayList<>();


    // ????????? ???????????????
    private static void setInterphoneVoice(Filter filter, MsgResponse msgRes) {


        if(MyApplicatoin.evenUser == null){
            return;
        }


        if(us == msgRes.getUserId() && ps == msgRes.getPacketID()){

        }else{

            /*firename = new String(mContext.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getPath()+"/data/"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName())+"/sms_voice/"+ UUID.randomUUID().toString()+".pcm");

            msgRes.setFireName(firename);*/

            if(voice_test.size() >= 750){

                msgRes.setUserId(us);
                msgRes.setPacketID(ps);

                setVoice(filter,msgRes);

            }



            voice_pack.clear();
            voice_test.clear();

            voice_code = 0;

        }

        us = msgRes.getUserId();
        ps = msgRes.getPacketID();

    //    msgRes.setFireName(firename);

        // ????????????????????????????????????????????????

        startVoice(filter,msgRes);


        try {

            for(byte as : Base64.decode(msgRes.getContent(), Base64.DEFAULT)){

                voice_pack.add(as);
                voice_test.add(as);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        //??????????????????????????????ID????????????????????????????????????????????????Openfire

        if(us == MyApplicatoin.evenUser.getId()){

            try {

                if(voice_pack.size() >= voice_size) {

                    byte[] bytes = new byte[voice_pack.size()];

                    for (int i = 0; i < voice_pack.size(); i++) {

                        bytes[i] = voice_pack.get(i);

                    }

                    voice_pack.clear();

                    //openfire ????????????

                    Openfrom openfrom = new Openfrom();

                    openfrom.setType(MsgResponse.Voice);

                    String msg = Base64.encodeToString(bytes, Base64.DEFAULT);

                    //????????? ??????

                    voice_code++;

                    if(voice_code>=100){
                        voice_code = 0;
                    }
                    byte[] a = {voice_code};

                //    openfrom.setMessage(new String(a)+msgRes.getFireName() + msg);
                    openfrom.setMessage(new String(a) + msg);


                    openfrom.setMessageID(msgRes.getPacketID());
                    openfrom.setCode(Open_biz.SendMessage);
                    openfrom.setUserId(MyApplicatoin.evenUser.getId());

                    MyApplicatoin.setOpenfire(openfrom);

                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{

         //   LogUtil.i(TAG,"InterphoneVoice2:"+voice.size()+"--"+isvoice+"--"+us+"--"+"--"+ps);


            if(voice.size() > 0 || isvoice){

                boolean isk = false;

                for(long l : phs){

                    if(l == msgRes.getPacketID()){
                        isk = true;
                    }

                }
                if(!isk){
                    phs.add(msgRes.getPacketID());
                }
            }
        }
    }


    private static List<Voicelist> voice = new ArrayList<>();


    private static long usid = 0;
    private static long paid = 0;

    private static boolean isvoice = false;

    // ????????????
    public static boolean isstart = false;

    public static int ll = 0;

    //??????????????????
    private static VoiceBleRunnable voiceRunnable = null;

    private static boolean isBleVoice = false;

    public static List<Long> ls = new ArrayList<>();


    // openfire ???????????????
    private static void setOpenfireVoice(Filter filter, MsgResponse msgRes) {

        isvoice = true;

        if(MyApplicatoin.evenUser == null){
            return;
        }


        if(msgRes.getUserId() != MyApplicatoin.evenUser.getId()){

            try {

                byte[] bts = Base64.decode(msgRes.getContent(), Base64.DEFAULT);


                int islength = 0;
                int j = 0;
                byte[] WriteBytes;

                /*for(byte s : bts){
                    voice_open_pack.add(s);
                }*/

                while (islength < bts.length) {

                    int i = 0;

                    if(bts.length - (j + 1) < 45){
                        WriteBytes = new byte[bts.length - j];
                    }else{
                        WriteBytes = new byte[45];
                    }
                    while (islength < bts.length && i < 45) {
                        j++;
                        WriteBytes[i++] = bts[islength++];
                    }

                    if(MyApplicatoin.interphone.getState() == 3 ){

                        //?????????ID???????????????????????????
                        boolean isk = false;
                        for(long l : ls){

                            if(l == msgRes.getPacketID()){
                                isk = true;
                            }

                        }

                        if(!isk){

                            if(!isstart){

                                if(usid == 0 || paid == filter.getPacketID()){

                                    //??????????????????????????????????????????

                                    byte prica = (byte) (1 | 0x80);

                                    byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,msgRes.getUserId(),msgRes.getPacketID(),WriteBytes);

                                //    BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);

                                    putVoiceData(voiceData);

                                    usid = filter.getUserId();
                                    paid = filter.getPacketID();

                                }

                            }else{

                                boolean isks = false;

                                for(long l : ls){

                                    if(l == msgRes.getPacketID()){
                                        isks = true;
                                    }

                                }
                                if(!isks){
                                    ls.add(msgRes.getPacketID());
                                }

                            }

                        }


                    }else{

                        //??????????????????????????????????????????ID?????????????????????
                        boolean isk = false;

                        for(long l : ls){

                            if(l == msgRes.getPacketID()){
                                isk = true;
                            }

                        }
                        if(!isk){
                            ls.add(msgRes.getPacketID());
                        }

                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }


            Voicelist v = new Voicelist();

            v.setUserId(msgRes.getUserId());
            v.setPacketID(msgRes.getPacketID());
        //    v.setFireName(msgRes.getFireName());
            v.setFormName(msgRes.getFormName());
            v.setTime(msgRes.getTime());
            v.setType(msgRes.getType());
            v.setVoice_code(msgRes.getVoice_code());
            v.setContent(msgRes.getContent());

            voice.add(v);

            //Openfire ????????????????????????????????????
            startVoiceOp();

            //????????????????????????????????????
            if(msgRes.getVoice_code() >= 120){

                usid = 0;
                paid = 0;


                //????????????????????????

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        isvoice = false;
                    }
                }).start();





                List<Byte> voices = new ArrayList();

                //????????????????????????????????????????????????

                Iterator it= voice.iterator();

                while(it.hasNext()) {

                    Voicelist voice = (Voicelist) it.next();

                    if(voice.getUserId() == msgRes.getUserId() && voice.getPacketID() == msgRes.getPacketID()){

                        byte[] bts = Base64.decode(voice.getContent(), Base64.DEFAULT);

                        for(byte b : bts){
                            voices.add(b);
                        }

                        it.remove();
                    }

                }

                //??????????????????????????????
                if(MyApplicatoin.interphone.getState() == 3 ){

                    byte prica = (byte) (4 | 0x80);

                    byte[] WriteBytess = new byte[0];

                    boolean isk = false;
                    for(long l : ls){

                        if(l == msgRes.getPacketID()){
                            isk = true;
                        }

                    }

                    if(!isk){

                        //????????????????????????????????????

                        packs.add(msgRes.getPacketID());

                        byte [] voiceDatas = ATcommand.at_set_file_user_chat_versions(prica,msgRes.getUserId(),msgRes.getPacketID(),WriteBytess);

                        putVoiceData(voiceDatas);


                    }
                }


                // ??????ID ????????????????????????
                Iterator its = ls.iterator();

                while(its.hasNext()) {

                    Long l = (Long) its.next();

                    if(l == msgRes.getPacketID()){

                        its.remove();
                    }

                }


                if(voices.size() >= 750){

                    MsgResponse response = new MsgResponse();

                    response.setPacketID(msgRes.getPacketID());
                    response.setType(msgRes.getType());

                    response.setFormName(msgRes.getFormName());

                    byte[] bs = new byte[voices.size()];

                    for(int i = 0;i<voices.size();i++){
                        bs[i] = voices.get(i);
                    }

                    String msg = Base64.encodeToString(bs, Base64.DEFAULT);
                    response.setContent(msg);

                    response.setTime(msgRes.getTime());
                    response.setUserId(msgRes.getUserId());

                    //?????????????????????
                    startMessageFilter(filter,response);

                }

            }

        }else{


            isvoice = false;

            if(msgRes.getVoice_code() >= 120){

                Mesage_entity msgs = new Mesage_entity();

                msgs.setUser_id(msgRes.getUserId());
                msgs.setMgid(msgRes.getPacketID());
                msgs.setMesg_type(MsgResponse.Voice);

                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_MSG_OK);
                intent.putExtra(GlobalConsts.ACTION_MSG,msgs);

                MyApplicatoin.mContext.sendBroadcast(intent);

            }
        }

    }


    private static void putVoiceData(byte[] voiceData) {

        if(voiceRunnable == null){

            voiceRunnable = new VoiceBleRunnable(BluetoothLeService.le_service);
            voiceRunnable.start();
        }

        if (voiceRunnable != null) {

            if(!voiceRunnable.isRecording()){
                voiceRunnable = new VoiceBleRunnable(BluetoothLeService.le_service);
                voiceRunnable.start();
            }

            voiceRunnable.putByte(voiceData);

        }
    }

    private static TimerTask timeop;
    private static Timer taskop = new Timer();
    private static int recLenop = 10;


    private static void startVoiceOp() {

        if(timeop == null){
            try {
                recLenop = 10;
                timeop = new TimerTask() {
                    @Override
                    public void run() {
                        recLenop--;
                        if(recLenop <= 0){

                            if(timeop != null){
                                timeop.cancel();
                            }

                            timeop=null;

                            while (voice.size() > 0){

                                List<Byte> voices = new ArrayList();

                                Voicelist v = voice.get(0);

                                long uid = v.getUserId();
                                long pid = v.getPacketID();


                                //????????????????????????????????????????????????

                                Iterator it= voice.iterator();

                                while(it.hasNext()) {

                                    Voicelist voice = (Voicelist) it.next();

                                    if(voice.getUserId() == uid && voice.getPacketID() == pid){

                                        byte[] bts = Base64.decode(voice.getContent(), Base64.DEFAULT);

                                        for(byte b : bts){
                                            voices.add(b);
                                        }

                                        it.remove();
                                    }
                                }


                                if(voices.size() >= 750){

                                    Filter filter = new Filter();

                                    filter.setPacketID(Long.valueOf(v.getPacketID()));
                                    filter.setTime(v.getTime().getTime());
                                    filter.setType(v.getType());
                                    filter.setUserId(v.getUserId());
                                    filter.setCode(Filter.openfire);
                                    filter.setGroupName(v.getFormName());

                                    MsgResponse response = new MsgResponse();

                                    response.setPacketID(v.getPacketID());
                                    response.setType(v.getType());

                                    response.setFormName(v.getFormName());

                                    byte[] bs = new byte[voices.size()];

                                    for(int i = 0;i<voices.size();i++){
                                        bs[i] = voices.get(i);
                                    }

                                    String msg = Base64.encodeToString(bs, Base64.DEFAULT);

                                    response.setContent(msg);

                                    response.setTime(v.getTime());
                                    response.setUserId(v.getUserId());

                                    //?????????????????????
                                    startMessageFilter(filter,response);



                                }

                                usid = 0;
                                paid = 0;

                                //????????????????????????

                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {

                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                        isvoice = false;
                                    }
                                }).start();


                            }

                        }
                    }
                };
                taskop.schedule(timeop, 1000, 1000);

            } catch (Exception e) {

                ExceptionUtil.handleException(e);

            }
        }else{
            recLenop=10;
        }

    }


    private static TimerTask time;
    private static Timer task = new Timer();
    private static int recLen = 3;

    /**
     *
     * ???????????? ???????????????????????????RF???????????????????????????
     *
     * */
    public static void setRF() {

        if(time == null){

            try {

                recLen = 3;

                time = new TimerTask() {
                    @Override
                    public void run() {
                        recLen--;
                        if(recLen < 0){

                            if(time != null){
                                time.cancel();
                            }

                            time = null;

                            startRF();
                        }
                    }
                };
                task.schedule(time, 1000, 1000);

            } catch (Exception e) {

                ExceptionUtil.handleException(e);

            }

        }else{

            recLen = 3;

        }


    }

    /**
     * openfire ????????????????????????????????? RF??????
     * */
    public static void startRF() {

        try {
        //    LogUtil.i(TAG,"startRF");

            if(OpenfireService.open_user_join.size() > 0){

                try {

                    String path = mContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+OpenfireService.open_user_join.get(0).getFormName()+"/sms_user/";


                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                        File filePath = new File(path);

                        File[] files = null;

                        if (!filePath.exists()) {

                            filePath.mkdirs();
                        }

                        if(filePath.exists()){

                            files = filePath.listFiles();

                        }


                        if(files == null){

                            return;
                        }


                        if(files.length == OpenfireService.open_user_join.size()){

                            //????????????????????????

                            if(MyApplicatoin.interphone.getRf() == 2){

                                Interphone interphone = new Interphone();

                                interphone.setRf((byte)1);

                                MyApplicatoin.setInterphone(interphone);


                            }

                        }else{
                            //???????????????

                            if(MyApplicatoin.interphone.getRf() == 1){

                                Interphone interphone = new Interphone();

                                interphone.setRf((byte)2);

                                MyApplicatoin.setInterphone(interphone);

                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else{

                if(MyApplicatoin.interphone.getRf() == 1){

                    Interphone interphone = new Interphone();

                    interphone.setRf((byte)2);

                    MyApplicatoin.setInterphone(interphone);

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static List<BluetoothDevice> is_device = new ArrayList<BluetoothDevice>();


    public static void sendRequest(int code, Object obj){


        sms_fw_api.do_request(mContext, code, obj, new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                try {

                    if(MyApplicatoin.evenUser == null){
                        return;
                    }

                    final DAOUserDao userDao = DAOService.get(mContext).getsession().getDAOUserDao();

                    final DAOUser daoUser = userDao.queryBuilder().where(DAOUserDao.Properties.Remote_id.eq(MyApplicatoin.evenUser.getId())).orderAsc(DAOUserDao.Properties.Remote_id).unique();

                    if(opcode == sms_request.GET){

                        NETWORK = 0;

                        MyApplicatoin.evenUser = (User) obj;

                        if(MyApplicatoin.IS_BLE){

                            Interphone inter = new Interphone();
                            inter.setUserId(MyApplicatoin.evenUser.getId());
                            setInterphone(inter);

                        }
                        Log.e(GlobalConsts.TAG,"?????????????????????????????????"+MyApplicatoin.evenUser.toString());

                        //????????????SharedPreferences

                        SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConsts.USER, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(GlobalConsts.USER_ID, String.valueOf(MyApplicatoin.evenUser.getId()));
                        editor.putString(GlobalConsts.USER_KEY, MyApplicatoin.evenUser.getPasskey());

                        editor.commit();



                        if (daoUser == null) {
                            final DAOUser users = new DAOUser();
                            users.setRemote_id(MyApplicatoin.evenUser.getId());
                            users.setName(MyApplicatoin.evenUser.getName());
                            users.setNumber(MyApplicatoin.evenUser.getNumber());
                            users.setE_mail(MyApplicatoin.evenUser.getE_mail());
                            users.setSex(MyApplicatoin.evenUser.getSex());
                            users.setVersion(MyApplicatoin.evenUser.getVersion());
                            users.setLast_login_time(MyApplicatoin.evenUser.getLast_login_time());
                            users.setRegister_time(MyApplicatoin.evenUser.getRegister_time());

                            Avatar avatar = new Avatar();
                            avatar.setUser_id(MyApplicatoin.evenUser.getId());

                            sms_request.dorequest(mContext, sms_request.GET, avatar, new RequestCB() {
                                @Override
                                public void respone(int opcode, Object obj) {
                                    if (opcode == sms_request.GET) {
                                        Avatar s = (Avatar) obj;
                                    //    LogUtil.i(TAG,s.toString());

                                        try {
                                            if (s.getUrl() != null && s.getData() != null) {

                                                String icon_path = Tools.writeToSdcard(mContext, "user", s.getUser_id().toString(),s.getData());

                                                MyApplicatoin.evenUser.setAvatar_url(icon_path);

                                                users.setAvatar_url(icon_path);

                                            }

                                            userDao.insert(users);

                                            Login_Openfire();

                                            int start = GlobalConsts.ACTTION_LOGIN_SUCCESS;

                                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                            intent.putExtra(GlobalConsts.ACTION_USER,start);

                                            mContext.sendBroadcast(intent);

                                        } catch (Exception e) {

                                            ExceptionUtil.handleException(e);

                                        }
                                    }
                                }
                            });

                        }else if(daoUser != null && daoUser.getVersion() != MyApplicatoin.evenUser.getVersion()){

                            final DAOUser User = new DAOUser();
                            User.setId(daoUser.getId());
                            User.setRemote_id(MyApplicatoin.evenUser.getId());
                            User.setName(MyApplicatoin.evenUser.getName());
                            User.setNumber(MyApplicatoin.evenUser.getNumber());
                            User.setE_mail(MyApplicatoin.evenUser.getE_mail());
                            User.setSex(MyApplicatoin.evenUser.getSex());
                            User.setVersion(MyApplicatoin.evenUser.getVersion());
                            User.setLast_login_time(MyApplicatoin.evenUser.getLast_login_time());
                            User.setRegister_time(MyApplicatoin.evenUser.getRegister_time());

                            Avatar avatar = new Avatar();

                            avatar.setUser_id(MyApplicatoin.evenUser.getId());

                            sms_request.dorequest(mContext, sms_request.GET, avatar, new RequestCB() {
                                @Override
                                public void respone(int opcode, Object obj) {
                                    if (opcode == sms_request.GET) {
                                        Avatar s = (Avatar) obj;


                                        try {
                                            if (s.getUrl() != null && s.getData() != null) {

                                                //???????????????????????????
                                                String icon_path = Tools.writeToSdcard(mContext, "user", s.getUser_id().toString(),s.getData());

                                                MyApplicatoin.evenUser.setAvatar_url(icon_path);

                                                User.setAvatar_url(icon_path);

                                            }

                                            userDao.update(User);


                                            Login_Openfire();

                                            int start = GlobalConsts.ACTTION_LOGIN_SUCCESS;

                                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
                                            intent.putExtra(GlobalConsts.ACTION_USER,start);

                                            mContext.sendBroadcast(intent);

                                        } catch (Exception e) {
                                            ExceptionUtil.handleException(e);
                                        }
                                    }
                                }
                            });

                        }else{

                            MyApplicatoin.evenUser.setAvatar_url(daoUser.getAvatar_url());

                            Login_Openfire();

                            int start = GlobalConsts.ACTTION_LOGIN_SUCCESS;

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
                            intent.putExtra(GlobalConsts.ACTION_USER,start);

                            mContext.sendBroadcast(intent);


                        }


                    }else if(opcode == sms_request.ERRO){



                        try {

                            int code = (int) obj;
                        //    LogUtil.i(TAG,"erro:"+code);
                            // ?????????????????????
                            if(code == 2){

                                if(NETWORK < 10){


                                    Intent intent_erro = new Intent(GlobalConsts.ACTION_LOGIN);

                                    intent_erro.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_ERRO_SHOW);

                                    MyApplicatoin.mContext.sendBroadcast(intent_erro);

                                    if(daoUser != null) {

                                        User u = new User();

                                        u.setId(daoUser.getRemote_id());
                                        u.setAvatar_url(daoUser.getAvatar_url());
                                        u.setName(daoUser.getName());

                                        MyApplicatoin.evenUser = u;

                                        int start = GlobalConsts.ACTTION_LOGIN_SUCCESS;

                                        Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);
                                        intent.putExtra(GlobalConsts.ACTION_USER, start);

                                        mContext.sendBroadcast(intent);

                                    }

                                    Thread.sleep(500);

                                    NETWORK++;
                                    getSmsUser(mContext);

                                }

                            }else if(code == 1){


                                isToos = true;

                                MyApplicatoin.evenUser = null;

                                Tools.showInfo(mContext,mContext.getResources().getString(R.string.chat_login_text_hint_login_erro_hite));


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });

        sms_fw_api.add_attr_listenner(mContext, new attr_listenner() {
            @Override
            public void onAttributeChang(int code, Object object) {

        //        LogUtil.i(TAG,"33333333:222:"+code);

                if(code == Interphone_notify_listenner.SCAN){

                    BluetoothDevice interphone = (BluetoothDevice) object;


                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConsts.DEVICE, Context.MODE_PRIVATE);
                    String name = sharedPreferences.getString(GlobalConsts.DEVICE_NAME, "name");
                    String address = sharedPreferences.getString(GlobalConsts.DEVICE_ADDRESS, "address");

                    if(interphone != null){

                        if(is_device.size() > 0 ){
                            boolean is_ble = false;

                            for (BluetoothDevice for_device : is_device) {
                                if (for_device.equals(interphone)) {
                                    is_ble = true;
                                }
                            }
                            if (!is_ble) {

                                if(interphone.getName() != null){

                                    if(interphone.getName().length() >= BlescanActivity.NITECORE.length()){

                                        String title = interphone.getName().substring(0,BlescanActivity.NITECORE.length());

                                        if(title.equals(BlescanActivity.NITECORE)){

                                            is_device.add(interphone);

                                        //    LogUtil.i(TAG,"?????????"+interphone.toString());
                                        //    LogUtil.i(TAG,"???????????????"+address);

                                            if(interphone.getAddress().equals(address)){
                                                Interphone interphone1 = new Interphone();
                                                interphone1.setAddress(address);
                                                setInterphone(interphone1);
                                            }


                                        }
                                    }
                                }
                            }

                        }else{

                            if(interphone.getName() != null){

                                    /*is_device.add(interphone);

                                    updateListView(is_device);*/

                                if(interphone.getName().length() >= BlescanActivity.NITECORE.length()){

                                    String title = interphone.getName().substring(0,BlescanActivity.NITECORE.length());

                                    if(title.equals(BlescanActivity.NITECORE)){

                                        is_device.add(interphone);
                                    //    LogUtil.i(TAG,"?????????"+interphone.toString());
                                    //    LogUtil.i(TAG,"???????????????"+address);


                                        if(interphone.getAddress().equals(address)){
                                            Interphone interphone1 = new Interphone();
                                            interphone1.setAddress(address);
                                            setInterphone(interphone1);
                                        }

                                    }

                                }

                            }

                        }


                    }

                }else if(code == Interphone_notify_listenner.ATTR){


                //    ping = 0;


                    try {
                        Interphone interphone = (Interphone) object;

                        if(interphone.getState() == BluetoothProfile.STATE_CONNECTED) {

                            imCode = new ImCode();

                            LogUtil.i(TAG,"????????????");

                            GlobalConsts.connect(true,interphone);

                            // ????????????SharedPreferences
                            SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConsts.DEVICE, Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString(GlobalConsts.DEVICE_NAME, interphone.getName());
                            editor.putString(GlobalConsts.DEVICE_ADDRESS, interphone.getAddress());

                            editor.commit();

                            if(interphone.getName() != null){

                                if(interphone.getName().length() >= BlescanActivity.NITECORE.length()) {

                                    String title = interphone.getName().substring(BlescanActivity.NITECORE.length(), interphone.getName().length());

                                    MyApplicatoin.interphone.setName(title);
                                    MyApplicatoin.interphone.setAddress(interphone.getAddress());
                                }
                            }



                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);


                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);


                        }else{

                            imCode = new ImCode();


                            LogUtil.i(TAG,"????????????");

                            GlobalConsts.connect(false,interphone);

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);


                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);


                        }
                    } catch (Exception e) {

                        ExceptionUtil.handleException(e);
                    }
                }else if(code == Interphone_notify_listenner.MESG){

                //  Ble ????????????????????????

                    try {
                        final Ble_message message = (Ble_message) object;
                        if(message.getType() == MsgResponse.Aprs){

                        }else{

                            Filter filter = new Filter();
                            filter.setPacketID(message.getId());
                            filter.setTime(System.currentTimeMillis());
                            filter.setType(message.getType());
                            filter.setCode(Filter.interphone);
                            filter.setUserId(message.getUserId());

                            filter.setGroupName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                            if(isJoinFrom()){

                                MsgResponse response = new MsgResponse();

                                response.setPacketID(message.getId());
                                response.setType(message.getType());
                                response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                                response.setContent(message.getMessage());
                                response.setTime(new Date());
                                response.setUserId(message.getUserId());

                                if(message.getType() == MsgResponse.Voice){

                                    //????????????????????????????????????
                                    setInterphoneVoice(filter,response);

                                }else{

                                    if(message.getType() == Ble_message.over){

                                        //????????????
                                        recLenvoice = 0;

                                    }else{

                                        //?????????????????????
                                        startMessageFilter(filter,response);

                                        LogUtil.i(TAG,"?????????????????????????????????");
                                    }

                                }

                            }

                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                }else if(code == Interphone_notify_listenner.RESULT){

                    try {
                    //    LogUtil.i(TAG,"Opcode.RESULT");

                        Result res = (Result) object;

                        byte[] data = res.getObjcode();

                        byte resu = res.getResult();

                        byte resCode = res.getCode();

                        if(resCode == Objcode.FRIDY){

                            if(resu == GlobalConsts.YES){

                                byte objcode = data[4];

                                if(objcode == Objcode.FILE_TEXT){

                                    byte[] userId = new byte[4];

                                    userId[0] = data[7];
                                    userId[1] = data[8];
                                    userId[2] = data[9];
                                    userId[3] = data[10];

                                    byte[] prickId = new byte[2];

                                    prickId[0] = data[11];
                                    prickId[1] = data[12];

                                    byte[] bxs = new byte[data.length - 13];

                                    int k = 0;
                                    for(int i=13 ;i < data.length - 1 ;i++){
                                        bxs[k++] = data[i];
                                    }

                                //    LogUtil.i(TAG,"????????????????????????"+new String(bxs));

                                    if(isJoinFrom()){

                                        Filter filter = new Filter();

                                        filter.setPacketID(toInt(prickId));
                                        filter.setTime(System.currentTimeMillis());
                                        filter.setType(Ble_message.text);
                                        filter.setCode(Filter.interphone);
                                        filter.setUserId(toInt(userId));

                                        filter.setGroupName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));


                                        MsgResponse response = new MsgResponse();

                                        response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                                        response.setPacketID(toInt(prickId));
                                        response.setType(Ble_message.text);
                                        response.setContent(new String(bxs));
                                        response.setTime(new Date());
                                        response.setUserId(toInt(userId));

                                        //?????????????????????
                                    //    startMessageFilter(filter,response);

                                    }


                                }else if(objcode == Objcode.BLE_IM){

                                    //??????????????????????????????

                                    byte[] imcodeId = new byte[4];

                                    imcodeId[0] = data[7];
                                    imcodeId[1] = data[8];
                                    imcodeId[2] = data[9];
                                    imcodeId[3] = data[10];

                                    long codeId = toInt(imcodeId);

                                    if(codeId == 0){

                                        //??????????????????????????????

                                    }else{

                                        //???????????????????????????????????????

                                        if(isJoinFrom()){

                                            long time = System.currentTimeMillis();
                                            Filter filter = new Filter();
                                            filter.setPacketID(time);
                                            filter.setTime(time);
                                            filter.setType(MsgResponse.Join);
                                            filter.setCode(Filter.interphone);
                                            filter.setUserId(MyApplicatoin.interphone.getUserId());

                                            filter.setGroupName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));

                                            MsgResponse response = new MsgResponse();

                                            response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                                            response.setPacketID(time);
                                            response.setType(MsgResponse.Join);
                                            response.setContent(new String("????????????"));
                                            response.setTime(new Date());
                                            response.setUserId(MyApplicatoin.interphone.getUserId());

                                            //?????????????????????
                                            startMessageFilter(filter,response);
                                        }
                                    }
                                }

                            }else{

                                LogUtil.i(TAG,"?????????????????????");

                            }


                        }else{

                            if(resCode == Objcode.DRIVER) {

                                byte opCode = (byte) (data[0] & 0x7F);

                                if(opCode == 2 && data.length > 3){

                                    byte[] bytes = new byte[data.length - 1];

                                    System.arraycopy(data, 1, bytes, 0, bytes.length);

                                    long a = toInt(bytes);

                                    Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                    intent.putExtra(GlobalConsts.ACTION_USER,(int)a);

                                    MyApplicatoin.mContext.sendBroadcast(intent);

                                }else if(opCode == 24){

                                    LogUtil.i(TAG,"?????????????????????");

                                    Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                    intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_START);

                                    MyApplicatoin.mContext.sendBroadcast(intent);


                                }


                            }else if(resCode == Objcode.MESSAGE){


                                /*if(resu == GlobalConsts.YES){

                                    byte objcode = data[3];

                                    byte b = (byte) (objcode & 0x7F);


                                    if(b == Objcode.FILE_TEXT){

                                        byte[] userId = new byte[4];

                                        userId[0] = data[6];
                                        userId[1] = data[7];
                                        userId[2] = data[8];
                                        userId[3] = data[9];

                                        byte[] prickId = new byte[4];

                                        prickId[0] = data[10];
                                        prickId[1] = data[11];
                                        prickId[2] = data[12];
                                        prickId[3] = data[13];


                                        byte[] bxs = new byte[data.length - 14];

                                        int k = 0;
                                        for(int i=14 ;i < data.length - 1 ;i++){
                                            bxs[k++] = data[i];
                                        }

                                        LogUtil.i(TAG,"????????????????????????"+Arrays.toString(data));

                                        Filter filter = new Filter();

                                        filter.setPacketID(toInt(prickId));
                                        filter.setTime(System.currentTimeMillis());
                                        filter.setType(MsgResponse.Text);
                                        filter.setCode(Filter.phone);
                                        filter.setUserId(toInt(userId));

                                        filter.setGroupName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));


                                        MsgResponse response = new MsgResponse();

                                        response.setFormName(Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName()));
                                        response.setPacketID(toInt(prickId));
                                        response.setType(MsgResponse.Text);
                                        response.setContent(new String(bxs));
                                        response.setTime(new Date());
                                        response.setUserId(toInt(userId));

                                        //?????????????????????
                                        startMessageFilter(filter,response);


                                    }

                                }else{
                                    LogUtil.i(TAG,"?????????????????????");
                                }*/

                            }


                            /*Interphone phone = interphone;


                            if (res.getResult() == GlobalConsts.YES) {


                                if (phone.getName() != null) {

                                    MyApplicatoin.interphone.setName(phone.getName());

                                }
                                if(phone.getTxcode() != null){

                                    MyApplicatoin.interphone.setTxcode(phone.getTxcode());

                                }
                                if(phone.getRxcode() != null){

                                    MyApplicatoin.interphone.setRxcode(phone.getRxcode());

                                }
                                if(phone.getFrequne() != null){

                                    MyApplicatoin.interphone.setFrequne(phone.getFrequne());

                                }
                                if(phone.getSq() != null){

                                    MyApplicatoin.interphone.setSq(phone.getSq());

                                }
                                if(phone.getVox() != null){

                                    MyApplicatoin.interphone.setVox(phone.getVox());

                                }
                                if(phone.getBw() != null){

                                    MyApplicatoin.interphone.setBw(phone.getBw());

                                }
                                if(phone.getMode() != null){

                                    MyApplicatoin.interphone.setMode(phone.getMode());

                                }
                                if(phone.getBluetooth() != null){

                                    MyApplicatoin.interphone.setBluetooth(phone.getBluetooth());

                                }
                                if(phone.getImCode() != null){

                                    MyApplicatoin.interphone.setImCode(phone.getImCode());

                                }

                            }*/


                            //    LogUtil.i(TAG,"ImCode???NOYIFY:"+MyApplicatoin.interphone.getImCode().toString());


                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }


                }else if(code == Interphone_notify_listenner.NOYIFY){


                    // ?????????????????????????????????????????????
                    try {
                        Interphone phone = (Interphone) object;

                        //    LogUtil.i(TAG,"Opcode.NOYIFY???"+phone.toString());

                        if(phone.getDevice_key() != null){

                            MyApplicatoin.interphone.setDevice_key(phone.getDevice_key());

                        }
                        if(phone.getId() != null){

                            MyApplicatoin.interphone.setId(phone.getId());

                        }
                        if(phone.getHard_version() != null){

                            MyApplicatoin.interphone.setHard_version(phone.getHard_version());

                        }
                        if(phone.getSoft_version() != null){

                            MyApplicatoin.interphone.setSoft_version(phone.getSoft_version());

                        }

                        if(phone.getFactroy_time() != null){

                            MyApplicatoin.interphone.setFactroy_time(phone.getFactroy_time());

                        }
                        if(phone.getActive_time() != null){

                            MyApplicatoin.interphone.setActive_time(phone.getActive_time());

                        }
                        if(phone.getActive_site() != null){

                            MyApplicatoin.interphone.setActive_site(phone.getActive_site());

                        }
                        if(phone.getName() != null){

                            MyApplicatoin.interphone.setName(phone.getName());

                        }
                        if(phone.getFrequne() != null){

                            MyApplicatoin.interphone.setFrequne(phone.getFrequne());


                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,phone.getFrequne());

                            MyApplicatoin.mContext.sendBroadcast(intent);

                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);
                        }
                        if(phone.getMode() != null){

                            MyApplicatoin.interphone.setMode(phone.getMode());

                        }
                    /*if(phone.getVolt() != null){

                        MyApplicatoin.interphone.setMode(phone.getMode());

                    }*/
                        if(phone.getPower() != null){

                            MyApplicatoin.interphone.setPower(phone.getPower());

                        }
                        if(phone.getBw() != null){

                            MyApplicatoin.interphone.setBw(phone.getBw());

                        }
                        if(phone.getRf() != null){

                        //    interPhoneRF = phone.getRf();

                            MyApplicatoin.interphone.setRf(phone.getRf());

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);

                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);

                            if(MyApplicatoin.interPhoneRF == 2){
                                setRF();
                            }


                        }
                        if(phone.getTx_power() != null){

                            MyApplicatoin.interphone.setTx_power(phone.getTx_power());

                        }

                        if(phone.getSq() != null){

                            MyApplicatoin.interphone.setSq(phone.getSq());

                        }
                        if(phone.getVox() != null){

                            MyApplicatoin.interphone.setVox(phone.getVox());

                        }
                        if(phone.getVolt() != 0){



                            MyApplicatoin.interphone.setVolt(phone.getVolt());

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);

                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);

                        }
                        if(phone.getTxcode() != null){

                            MyApplicatoin.interphone.setTxcode(phone.getTxcode());

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);

                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);


                        }
                        if(phone.getPlays() != null){

                            MyApplicatoin.interphone.setPlays(phone.getPlays());

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);

                        }

                        if(phone.getRxcode() != null){

                            MyApplicatoin.interphone.setRxcode(phone.getRxcode());

                            Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                            intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent);

                            Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                            intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                            MyApplicatoin.mContext.sendBroadcast(intent1);

                        }
                        if(phone.getBluetooth() != null){

                            MyApplicatoin.interphone.setBluetooth(phone.getBluetooth());

                        }
                        if(phone.getIsplay() != null){

                            MyApplicatoin.interphone.setIsplay(phone.getIsplay());

                        }
                        if(phone.getStop_hite() != null){

                            MyApplicatoin.interphone.setStop_hite(phone.getStop_hite());

                        }
                        if(phone.getTx_hite() != null){

                            MyApplicatoin.interphone.setTx_hite(phone.getTx_hite());

                        }
                        if(phone.getDisconnec_hite() != null){

                            MyApplicatoin.interphone.setDisconnec_hite(phone.getDisconnec_hite());

                        }

                        if(phone.getImCode() != null){

                            if(phone.getImCode().getId() != null){

                                if(MyApplicatoin.interphone.getImCode() != null){

                                    MyApplicatoin.interphone.getImCode().setId(phone.getImCode().getId());

                                }else{
                                    imCode.setId(phone.getImCode().getId());
                                    MyApplicatoin.interphone.setImCode(imCode);
                                }



                            }
                            if(phone.getImCode().getUuid() != null){

                                if(MyApplicatoin.interphone.getImCode() != null){

                                    MyApplicatoin.interphone.getImCode().setUuid(phone.getImCode().getUuid());

                                }else{
                                    imCode.setUuid(phone.getImCode().getUuid());

                                    MyApplicatoin.interphone.setImCode(imCode);
                                }


                            }
                            if(phone.getImCode().getName() != null){

                                if(MyApplicatoin.interphone.getImCode() != null){

                                    MyApplicatoin.interphone.getImCode().setName(phone.getImCode().getName());

                                }else{
                                    imCode.setName(phone.getImCode().getName());
                                    MyApplicatoin.interphone.setImCode(imCode);
                                }


                                try {

                                    SharedPreferences sharedPreferences = mContext.getSharedPreferences(GlobalConsts.GROUPHITE, Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = sharedPreferences.edit();

                                    editor.putString(GlobalConsts.GroupName, Tools.getGroupName(phone.getImCode().getName()));
                                    editor.putString(GlobalConsts.GroupHite, MyApplicatoin.interphone.getImCode().getHite());

                                    editor.commit();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                try {

                                    if(MyApplicatoin.openfrom == null){

                                        Openfrom openfrom = new Openfrom();

                                        openfrom.setFromName(Tools.getGroupName(phone.getImCode().getName()));

                                        MyApplicatoin.openfrom = openfrom;

                                    }else{

                                        MyApplicatoin.openfrom.setFromName(Tools.getGroupName(phone.getImCode().getName()));

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            //    LogUtil.i(TAG,"getHite:"+MyApplicatoin.interphone.getImCode().getHite());

                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                intent.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_START);

                                MyApplicatoin.mContext.sendBroadcast(intent);

                                Intent intent1 = new Intent(GlobalConsts.ACTION_MAP);

                                intent1.putExtra(GlobalConsts.ACTION_USER,GlobalConsts.ACTTION_IS_SCAN);

                                MyApplicatoin.mContext.sendBroadcast(intent1);




                            }

                        }

                        if(phone.getUserId() != null){

                            if(MyApplicatoin.evenUser != null){

                                LogUtil.i(TAG,"?????????????????????ID:");


                                if(phone.getUserId() != MyApplicatoin.evenUser.getId()){

                                    /**
                                     * ?????????????????????ID
                                     * */

                                    final Interphone inter = new Interphone();

                                    inter.setUserId(MyApplicatoin.evenUser.getId());

                                    setInterphone(inter);

                                    LogUtil.i(TAG,"?????????????????????ID");


                                    MyApplicatoin.interphone.setUserId(MyApplicatoin.evenUser.getId());

                                }else{

                                    MyApplicatoin.interphone.setUserId(phone.getUserId());

                                }


                            }else {

                                LogUtil.i(TAG,"user:null");
                                MyApplicatoin.interphone.setUserId(phone.getUserId());
                            }

                        }
                    } catch (Exception e) {
                        ExceptionUtil.handleException(e);
                    }

                    //    LogUtil.i(TAG,"???????????????"+MyApplicatoin.interphone.toString());


                }else if(code == Interphone_notify_listenner.STATE){

                    try {
                        Interphone phone = (Interphone) object;

                     //   LogUtil.i(TAG,"???????????????1???"+phone.toString());


                        //????????????????????????????????????

                        if(phone.getState() != 0){

                        //    listp.clear();


                            if(MyApplicatoin.interphone != null){

                                MyApplicatoin.interphone.setState(phone.getState());
                                MyApplicatoin.interphone.setmState(phone.getmState());

                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_START);
                                intent.putExtra(GlobalConsts.ACTION_MSG,phone.getState());

                                MyApplicatoin.mContext.sendBroadcast(intent);

                            }

                        }


                        //APP???????????????????????????

                        if(phone.getmState() != 0){

                            MyApplicatoin.interphone.setmState(phone.getmState());

                        //    LogUtil.i(TAG,"???????????????2???"+MyApplicatoin.interphone.toString());

                            if(MyApplicatoin.interphone.getState() == 3){

                                //    MyApplicatoin.interphone.setState(phone.getmState());

                                Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

                                intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_START);
                                intent.putExtra(GlobalConsts.ACTION_MSG,phone.getmState());

                                MyApplicatoin.mContext.sendBroadcast(intent);

                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    //    LogUtil.i(TAG,"???????????????"+MyApplicatoin.interphone.toString());

                }

            }

        });

    };



    public static void setInterphone(final Interphone interphone){

    //    LogUtil.i(TAG,"Interphone:"+interphone.toString());

        sendRequest(sms_request.SET,interphone);


    }





    public static void startMessageFilter(Filter filter, final MsgResponse response) {

        LogUtil.i(TAG,"startMessageTime:"+System.currentTimeMillis());

        MessageFilter.addMessageLieret(mContext,mhandler, filter, response, new Message_request() {

            @Override
            public void respone(final DAOMesg mesg) {

                try {

                    if (mesg != null) {

                        final Mesage_entity msg = new Mesage_entity();

                        msg.setId(mesg.getId());
                        msg.setIs_ok((byte) 1);
                        msg.setContent(mesg.getContent());
                        msg.setMgid(mesg.getMgid());
                        msg.setContent_length(mesg.getCreate_length());
                        msg.setCreate_time(mesg.getCreate_time().getTime());
                        msg.setFrom_id(mesg.getFrom_id());
                        msg.setUser_id(mesg.getUser_id());
                        msg.setMesg_type(mesg.getMesg_type());

                        //????????????????????????????????????????????????????????????????????????

                        final String groupName = response.getFormName();

                    //    LogUtil.i(TAG,"addMessageLieret:"+msg.toString());

                        final DAOmemberDao memberDao = DAOService.get(mContext).getsession().getDAOmemberDao();

                        final List<DAOmember> daOmemberlist = memberDao.loadAll();

                        if(mesg.getUser_id() != 0){

                            User user = new User();
                            user.setId(msg.getUser_id());

                            DAOmember members = null;

                            for(DAOmember a : daOmemberlist){

                            //    LogUtil.i(TAG,"DAOmember:"+a.getUser_id()+","+a.getGroup_name());

                                if(a.getUser_id() == msg.getUser_id()){

                                    if(a.getGroup_name().equals(groupName)){

                                        members = a;

                                        break;
                                    }

                                }

                            }

                            if(members == null){

                                //???????????????
                                setMemberDB(user,groupName);
                                //??????UI
                                updataListView(msg);

                            }else{

                                msg.setSex(members.getSex());
                                msg.setUser_neme(members.getUser_name());
                                msg.setAvatar_url(members.getAvatar_url());


                                //??????UI
                                updataListView(msg);

                            }


                            if(msg.getMesg_type() == 0){

                                //???????????????
                                setMemberDB(user,groupName);

                            }

                        }else{

                            //??????UI
                            updataListView(msg);

                        }
                    }

                } catch (Exception e) {
                    ExceptionUtil.handleException(e);
                }

            }
        });
    }



    private static void setMemberDB(User users, String groupName) {
        //??????????????????
        sms_request.dorequest(mContext, sms_request.GET, users , new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                if(opcode == sms_request.GET){

                    final DAOmemberDao memberDao = DAOService.get(mContext).getsession().getDAOmemberDao();

                    List<DAOmember> daOmemberlist = memberDao.loadAll();

                    DAOmember member = null;

                    for(DAOmember a : daOmemberlist){

                    //    LogUtil.i(TAG,"DAOmember:"+a.getUser_id()+","+a.getGroup_name());

                        if(a.getUser_id() == users.getId()){

                            if(a.getGroup_name().equals(groupName)){

                                member = a;

                                break;
                            }

                        }

                    }


                    final User user = (User) obj;

                    final DAOmember omember = new DAOmember();

                    omember.setUser_id(user.getId());
                    omember.setUser_name(user.getName());
                    omember.setSex(user.getSex());
                    omember.setVersion(user.getVersion());
                    omember.setGroup_name(groupName);


                    if(member == null ){

                        LogUtil.i(TAG,"startMessageFilter = null");


                        Avatar avatar = new Avatar();
                        avatar.setUser_id(user.getId());

                        sms_request.dorequest(mContext, sms_request.GET, avatar, new RequestCB() {
                            @Override
                            public void respone(int opcode, Object obj) {

                                try {
                                    if(opcode == sms_request.GET){

                                        Avatar s = (Avatar) obj;

                                        if (s.getUrl() != null && s.getData() != null) {

                                            String icon_path = null;

                                            try {
                                                icon_path = Tools.writeToSdcard(mContext, groupName, s.getUser_id().toString(),s.getData());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            omember.setAvatar_url(icon_path);

                                        }

                                        memberDao.insert(omember);

                                        return;

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });


                    }else if(user.getVersion() > member.getVersion() ){

                        LogUtil.i(TAG,"startMessageFilter != null");
                        Avatar avatar = new Avatar();
                        avatar.setUser_id(user.getId());

                        sms_request.dorequest(mContext, sms_request.GET, avatar, new RequestCB() {
                            @Override
                            public void respone(int opcode, Object obj) {

                                try {
                                    if(opcode == sms_request.GET){

                                        Avatar s = (Avatar) obj;

                                    //    LogUtil.i(TAG,"seekfrom_Avatar:"+s);

                                        if (s.getUrl() != null && s.getData() != null) {

                                            String icon_path = null;
                                            try {
                                                icon_path = Tools.writeToSdcard(mContext, groupName, s.getUser_id().toString(),s.getData());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }

                                            omember.setAvatar_url(icon_path);

                                        }

                                        memberDao.update(omember);

                                        return;

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                    }
                }

            }

        });

    }


    @Override
    public void onTerminate() {
        // ???????????????????????????
        LogUtil.i(TAG, "onTerminate");

        try {
            if(audioTrack != null){

                if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
                    audioTrack.stop();
                }else{
                    audioTrack = null;
                }

            }

            if(audioRecord != null){

                if(audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    audioRecord.stop();
                }else{
                    audioRecord = null;
                }

            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }


        super.onTerminate();


    }
    @Override
    public void onLowMemory() {
        // ????????????????????????
        LogUtil.i(TAG, "onLowMemory");

        try {
            if(audioTrack != null){

                if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
                    audioTrack.stop();
                }else{
                    audioTrack = null;
                }

            }

            if(audioRecord != null){

                if(audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    audioRecord.stop();
                }else{
                    audioRecord = null;
                }

            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
        super.onLowMemory();

    }
    @Override
    public void onTrimMemory(int level) {
        // ????????????????????????????????????
        LogUtil.i(TAG, "onTrimMemory???"+level);
        try {
            if(audioTrack != null){

                if(audioTrack.getState() == AudioTrack.STATE_INITIALIZED){
                    audioTrack.stop();
                }else{
                    audioTrack = null;
                }

            }

            if(audioRecord != null){

                if(audioRecord.getState() == AudioRecord.STATE_INITIALIZED){
                    audioRecord.stop();
                }else{
                    audioRecord = null;
                }

            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }

        if(esc_sms){
            System.exit(0);
        }

        super.onTrimMemory(level);

    }



    private static void updataListView(Mesage_entity msg) {

        LogUtil.i(TAG,"updataMessageTime:"+msg.toString());

        Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

        intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_MSG);
        intent.putExtra(GlobalConsts.ACTION_MSG,msg);

        MyApplicatoin.mContext.sendBroadcast(intent);

    }

    private static void updataUI(byte objcode, byte type) {

        byte[] open = new byte[2];

        open[0] = objcode;
        open[1] = type;

        Intent intent = new Intent(GlobalConsts.ACTION_LOGIN);

        intent.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_OPENFIRE);
        intent.putExtra(GlobalConsts.ACTION_OPENFIRE,open);

        MyApplicatoin.mContext.sendBroadcast(intent);

    }

    /**
     * ???????????????
     *
     * @author TangRen
     * @param length
     * @return
     * @time 2016-7-7
     */
    public static String Random(int length) {
        String random_str = null;
        try {
            char[] str= new char[length];
            int i = 0;
            int num=2;//???????????????
            while (i < length) {
                int f = (int) (Math.random() * num);
                if (f == 0)
                    str[i] = (char) ('a' + Math.random() * 26);
                else
                    str[i] = (char) ('0' + Math.random() * 10);
                i++;
            }
            random_str = new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return random_str;
    }

    private static long toInt(byte[] date){

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

    private static boolean isJoinFrom(){

        if(!MyApplicatoin.IS_BLE){
            return false;
        }
        if(MyApplicatoin.interphone == null){
            return false;
        }
        if(MyApplicatoin.interphone.getImCode() == null){
            return false;

        }else{

            if(MyApplicatoin.interphone.getImCode().getId() != null){

                if(MyApplicatoin.interphone.getImCode().getId() == 0){
                    return false;
                }else{
                    return true;
                }
            }else{
                return false;
            }
        }
    }


    /**
     *
     * ??????????????????
     *
     * */
    private static void newVoiceMessage(Voice voice) {


        try {
            if(MyApplicatoin.dataCode == BluetoothLeService.versions_one){

                for(byte[] list : voice.getData()){

                    byte [] voiceData = ATcommand.at_set_file_user_chat(Opcode.SET, Objcode.FILE_VOICE,voice.getUserID(),voice.getPackID(),list);

                    BluetoothLeService.le_service.sendWriteByte(Objcode.FRIDY,voiceData);

                }



            }else{


                if(MyApplicatoin.interphone.getState() == 3 && MyApplicatoin.interphone.getmState() == 3){



                    Intent intents = new Intent(GlobalConsts.ACTION_LOGIN);

                    intents.putExtra(GlobalConsts.ACTION_TYPE,GlobalConsts.ACTTION_IS_START);
                    intents.putExtra(GlobalConsts.ACTION_MSG,(byte)2);

                    MyApplicatoin.mContext.sendBroadcast(intents);

                    for(byte[] list : voice.getData()){

                        byte prica = (byte) (1 | 0x80);

                        byte [] voiceData = ATcommand.at_set_file_user_chat_versions(prica,voice.getUserID(),voice.getPackID(),list);

                        BluetoothLeService.le_service.sendWriteByte(Objcode.MESSAGE,voiceData);

                    }

                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
