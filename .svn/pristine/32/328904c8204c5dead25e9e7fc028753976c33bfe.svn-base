package com.sms.app.interphone.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Base64;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.interphone.R;
import com.sms.app.interphone.entity.Filter;
import com.sms.app.interphone.entity.Presence_entity;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.interphone.ui.activity.MainActivity;
import com.sms.app.interphone.util.msnutil.ExceptionUtil;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.interphone.util.msnutil.MessageFilter;
import com.sms.app.interphone.util.msnutil.Tools;
import com.sms.app.interphone.util.openutil.Openfrom;
import com.sms.app.interphone.util.openutil.op_intercface;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.dao.bean.DAOmember;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.framework.dao.bean.commom.DAOmemberDao;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.bean.Avatar;
import com.sms.app.framework.trans.bean.User;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.MessageListener;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketInterceptor;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.RosterGroup;
import org.jivesoftware.smack.SASLAuthentication;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.sasl.SASLPlainMechanism;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smack.util.XmlStringBuilder;
import org.jivesoftware.smackx.delay.packet.DelayInformation;
import org.jivesoftware.smackx.hoxt.packet.AbstractHttpOverXmpp;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.Occupant;
import org.jivesoftware.smackx.muc.RoomInfo;
import org.jivesoftware.smackx.ping.PingFailedListener;
import org.jivesoftware.smackx.ping.PingManager;
import org.jivesoftware.smackx.ping.packet.Ping;
import org.jivesoftware.smackx.ping.packet.Pong;
import org.jivesoftware.smackx.time.packet.Time;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Administrator on 2016/12/27.
 */

public class OpenfireService extends Service {

    public final byte Register = 1;
    public final byte Login = 2;
    public final byte Newfrom = 3;
    public final byte Joinfrom = 4;
    public final byte Destroyfrom = 5;
    public final byte SendMessage = 6;
    public final byte Connection = 7;
    public final byte seekfrom = 8;

    public static final byte YES = 1;
    public static final byte NO = 0;
    public static final byte ERRO = 2;

    private int leng = 0;


    public static List<MsgResponse> open_user_join = new ArrayList<>();

    public static List<MsgResponse> open_user = new ArrayList<>();

    private Map<String, Chat> chatManage = new HashMap<String, Chat>();// 聊天窗口管理map集合


    public  static OpenfireService le_service = null;
    private  static op_intercface le_cb = null;

    public static XMPPConnection xmppConnection = null;

    public static OpenfireService instance = null;

    public static ChatManager chatManager = null;

    private static Openfrom openfrom = null;

    private static MsgResponse ownerUser = null;




    private MultiUserChat multiUserChat;

    private String host = "47.56.187.13";//120.25.125.223     183.56.203.167   47.56.187.13
    private String  openfire_host_name = "izj6cdmgl1ayo4alsq5w29z";//iz94owcwnrdz   master-yzjgxh2528152613-1561973149142-0701480.novalocal     izj6cdmgl1ayo4alsq5w29z
    private String isNewVoice;

    private Gson gson = new Gson();


    private List<Byte> voice_data = new ArrayList<>();           // 当前接收到的语音数据集合

    private static String serviceName = "conference.izj6cdmgl1ayo4alsq5w29z";//conference.iz94owcwnrdz    conference.master-yzjgxh2528152613-1561973149142-0701480.novalocal      conference.izj6cdmgl1ayo4alsq5w29z

    public static final String TAG="YanShi...Log - OpenfireService";

    private static final String CHANNEL_ID = "NFCService";

    @Override
    public void onCreate() {
        super.onCreate();
        instance = OpenfireService.this;
        if(instance != null){
            OpenfireService.le_cb.on_context(instance);
        }
        le_service = OpenfireService.this;

    //  connectChatServer();

        LogUtil.i(TAG,"connectChatServer=onCreate");
        //receptionNotification();//弹出一个前台应用通知
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public void connectChatServer() {



    }

    public void registerInterceptorListener() {
        //2.4 让框架中的接口指向实现类
        AllPacketInterceptor allPacketInterceptor = new AllPacketInterceptor();
        xmppConnection.addPacketInterceptor(allPacketInterceptor, null);
        AllPacketListener allPacketListener = new AllPacketListener();
        xmppConnection.addPacketListener(allPacketListener, null);

        chatManager= ChatManager.getInstanceFor(xmppConnection);

    }

    private List<String> nameLists = new ArrayList<>();


    int k = 0;
    int l = 0;

    class AllPacketInterceptor implements PacketInterceptor {
        //发信息给openfire时调用
        @Override
        public void interceptPacket(Packet packet) {
        //    LogUtil.i(TAG,"AllPacketInterceptor:"+packet.toString());

        }
    }



    /**
     * openfire信息返回接收器，就调用processPacket
     */

    class AllPacketListener implements PacketListener {

        @Override
        public void processPacket(Packet packet) {

            LogUtil.i(TAG,"Time:"+System.currentTimeMillis()+",Packet:"+packet.toXML());

            if(packet instanceof IQ){

                //心跳类型

                IQ iq = (IQ) packet;

            //    LogUtil.i(TAG,"IQ:"+iq.getType());

                if(iq.getType() == IQ.Type.RESULT){

                    byte code = GlobalConsts.YES;

                    OpenfireService.le_cb.on_ping(code);


                }

            }

            if (packet instanceof Presence) {

                //成员信息类型

                try {

                    int max=99999;
                    int min=10000;
                    Random random = new Random();

                    int s = random.nextInt(max)%(max-min+1) + min;

                //    LogUtil.i(TAG,"随机数："+s);

                    Presence presence = (Presence) packet;

                //    LogUtil.i(TAG,"Presence:"+presence.toXML());


                    XmlStringBuilder xml = presence.toXML();

                    Presence_entity prs = getPresenceXML(xml.toString());

                //    LogUtil.i(TAG,"Presence.Affiliation:"+prs.getAffiliation());
                    /*LogUtil.i(TAG,"Presence.Jid:"+prs.getJid());
                    LogUtil.i(TAG,"Presence.Role:"+prs.getRole());
                    LogUtil.i(TAG,"Presence.Status:"+prs.getStatus());*/

                    if(prs != null){

                        if(prs.getJid() != null){

                            int code = prs.getJid().indexOf("@");

                            String id = prs.getJid().substring(0, code);

                            if(MyApplicatoin.evenUser != null){


                            //    LogUtil.i(TAG,"Presence.id:"+id);

                                try {
                                    if(Long.valueOf(id) == MyApplicatoin.evenUser.getId()){

                                        if(prs != null && prs.getStatus() != null){

                                            if(prs.getStatus().equals("100")){

                                                String from = presence.getFrom();


                                                int fromNameCode = from.indexOf("@");
                                                int userIDCode = from.indexOf("/");

                                                String fromname = from.substring(0, fromNameCode);

                                                String userID = from.substring(userIDCode+1, from.length());

                                                MsgResponse response = new MsgResponse();

                                                if(openfrom == null){
                                                    return;
                                                }
                                                if(!openfrom.getFromName().equals(fromname)){
                                                    return;
                                                }

                                                if(presence.getType() == Presence.Type.available){

                                                    response.setType(MsgResponse.Join);
                                                    response.setUserId(Long.valueOf(userID));
                                                    response.setFormName(fromname);
                                                    response.setTime(new Date());
                                                    response.setPacketID(s);
                                                    boolean isname = true;

                                                    for(String a : MyApplicatoin.groupMems){
                                                        if(a.equals(s)){
                                                            isname = false;
                                                            break;
                                                        }
                                                    }
                                                    if(isname){
                                                        MyApplicatoin.groupMems.add(userID);
                                                    }

                                                    boolean isf = true;

                                                    for(int i = 0;i < open_user_join.size();i++){

                                                        if(response.getUserId() == open_user_join.get(i).getUserId()){

                                                            open_user_join.remove(i);
                                                            open_user_join.add(response);
                                                            OpenfireService.le_cb.on_message(response);
                                                            isf = false;
                                                            break;

                                                        }

                                                    }

                                                    if(isf){
                                                        open_user_join.add(response);
                                                        OpenfireService.le_cb.on_message(response);
                                                    }

                                                }else if(presence.getType() == Presence.Type.unavailable){

                                                    response.setType(MsgResponse.Exit);

                                                    response.setUserId(Long.valueOf(userID));
                                                    response.setFormName(fromname);
                                                    response.setTime(new Date());
                                                    response.setPacketID(s);

                                                    /*for(int i = 0;i < open_user_join.size();i++){

                                                            if(response.getUserId() == open_user_join.get(i).getUserId()){

                                                                open_user_join.remove(i);

                                                                OpenfireService.le_cb.on_message(response);

                                                                break;
                                                            }
                                                      }*/
                                                }

                                                if(prs.getAffiliation().equals("owner")){

                                                    ownerUser = response;

                                                }
                                            }

                                        }

                                    }else{

                                        String from = presence.getFrom();

                                        int fromNameCode = from.indexOf("@");
                                        int userIDCode = from.indexOf("/");

                                        String fromname = from.substring(0, fromNameCode);

                                        String userID = from.substring(userIDCode+1, from.length());

                                        MsgResponse response = new MsgResponse();

                                        if(openfrom == null){
                                            return;
                                        }
                                        if(!openfrom.getFromName().equals(fromname)){
                                            return;
                                        }

                                        if(presence.getType() == Presence.Type.available){

                                            response.setType(MsgResponse.Join);
                                            response.setUserId(Long.valueOf(userID));
                                            response.setFormName(fromname);
                                            response.setTime(new Date());
                                            response.setPacketID(s);

                                            boolean isname = true;

                                            for(String a : MyApplicatoin.groupMems){
                                                if(a.equals(s)){
                                                    isname = false;
                                                    break;
                                                }
                                            }
                                            if(isname){
                                                MyApplicatoin.groupMems.add(userID);
                                            }

                                            userjoin(response);


                                        }else if(presence.getType() == Presence.Type.unavailable){

                                            response.setType(MsgResponse.Exit);

                                            response.setUserId(Long.valueOf(userID));
                                            response.setFormName(fromname);
                                            response.setTime(new Date());
                                            response.setPacketID(s);



                                            userjoin(response);



                                        //    OpenfireService.le_cb.on_message(response);
                                        }
                                    }
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }

            }


            try {

                if (packet instanceof Message) {


                    //消息类型

                    DelayInformation delay = packet.getExtension("delay","urn:xmpp:delay");

                    if(delay == null){

                        LogUtil.i(TAG,"在线消息：");

                        //如果是在线消息

                        Message message = (Message) packet;

                        //判断是群聊还是私聊
                        Message.Type type = message.getType();

                        if (type == Message.Type.groupchat) {

                            String from = message.getFrom();

                            if (from.contains("/")) {

                                //获取packet 时间

                                /*Date date = null;

                                DelayInformation inf = (DelayInformation) packet.getExtension("x","jabber:x:delay");

                                if (inf != null && inf.getStamp() != null){
                                    date = inf.getStamp();
                                }*/

                                int fromNameCode = from.indexOf("@");

                                String fromname = from.substring(0, fromNameCode);

                                op_Message msg = null;
                                try {
                                    msg = gson.fromJson(message.getBody(), op_Message.class);
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }

                                LogUtil.i(TAG,"在线消息2:"+msg.toString());

                                if(msg == null){
                                    return;
                                }

                                MsgResponse response = new MsgResponse();

                                response.setPacketID(msg.getPacketID());
                                response.setType(msg.getType());
                                response.setFormName(fromname);

                                /*if(date == null){

                                    date = new Date();
                                }*/

                                response.setTime(new Date(msg.getTime()));

                                response.setUserId(msg.getUserID());

                                if(msg.getType() == MsgResponse.Voice){

                                    String bod = msg.getContent();

                                //    int names = bod.indexOf(".pcm");

                                //    String fireName = bod.substring(1,names+4);

                                //    String voiceData = bod.substring(names+4,bod.length());
                                    String voiceData = bod.substring(1,bod.length());



                                    byte[] a = bod.substring(0,1).getBytes();


                                    response.setVoice_code(a[0]);

                                    response.setContent(voiceData);

                                //    response.setFireName(fireName);


                                }else if(msg.getType() == MsgResponse.Img){


                                }else{
                                    response.setContent(msg.getContent());
                                }

                                LogUtil.i(TAG,"MsgResponse:"+response.toString());

                                if(openfrom == null){
                                    return;
                                }
                                if(!openfrom.getFromName().equals(fromname)){
                                    return;
                                }


                                for(int i = 0;i < open_user.size();i++){

                                    if(open_user.get(i).getUserId() == response.getUserId()){

                                        if(response.getType() != MsgResponse.Apply && response.getType() != MsgResponse.Result){

                                            MsgResponse k = open_user.remove(i);

                                            open_user_join.add(k);

                                            boolean is = true;

                                            try {

                                                String path = OpenfireService.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+k.getFormName()+"/sms_user/";

                                                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                                                    File file = new File(path, String.valueOf(k.getUserId()));

                                                    if (!file.exists()) {

                                                        is = false;

                                                    }

                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            LogUtil.i(TAG,"isis:"+is);
                                            if(!is){

                                                k.setType(MsgResponse.Into);

                                            }

                                            OpenfireService.le_cb.on_message(k);

                                            break;
                                        }

                                    }

                                }


                                if(ownerUser != null){

                                    if(response.getType() == MsgResponse.Apply){

                                        OpenfireService.le_cb.on_Apply(response);

                                    }

                                }

                                if(response.getType() != MsgResponse.Apply){

                                    boolean is = false;

                                    for(MsgResponse m : open_user_join) {

                                        if (m.getUserId() == response.getUserId()) {
                                            is = true;
                                            break;
                                        }
                                    }

                                    if(!is){
                                        open_user_join.add(response);
                                    }

                                    OpenfireService.le_cb.on_message(response);

                                    if (nameLists.size() > 0) {

                                        updatalists(response.getFormName());

                                    }

                                }

                                if(response.getType() == MsgResponse.Leave){
                                    //退出聊天室消息
                                    nameLists.add(String.valueOf(msg.getUserID()));

                                    for(int i = 0;i < open_user_join.size();i++) {

                                        if (open_user_join.get(i).getUserId() == response.getUserId()) {

                                            open_user_join.remove(i);

                                            break;
                                        }

                                    }

                                    for(int i = 0;i < MyApplicatoin.groupMems.size();i++){

                                        if(String.valueOf(msg.getUserID()).equals(MyApplicatoin.groupMems.get(i))){
                                            MyApplicatoin.groupMems.remove(i);
                                            i--;
                                        }

                                    }

                                }

                            }

                        }else if(type == Message.Type.error){

                            LogUtil.i(TAG,"Message.Type.error");

                            try {

                                String from = message.getFrom();
                                String to = message.getTo();

                                int fromNameCode = from.indexOf("@");
                                int userIDCode = to.indexOf("@");

                                String fromname = from.substring(0, fromNameCode);

                                String userID = to.substring(0, userIDCode);

                                Openfrom openfrom = new Openfrom();

                                openfrom.setFromName(fromname);
                                openfrom.setUserId(Long.valueOf(userID));

                                joinfrom(openfrom);

                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }else if(type == Message.Type.chat){

                            LogUtil.i(TAG,"Message.Type.chat");

                            try {

                                String from = message.getFrom();

                                if (from.contains("/")) {

                                    //获取packet 时间

                                    /*Date date = null;

                                    DelayInformation inf = (DelayInformation) packet.getExtension("x", "jabber:x:delay");

                                    if (inf != null && inf.getStamp() != null) {
                                        date = inf.getStamp();
                                    }*/

                                    int fromNameCode = from.indexOf("@");

                                    String fromname = from.substring(0, fromNameCode);

                                    op_Message msg = null;
                                    try {
                                        msg = gson.fromJson(message.getBody(), op_Message.class);
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }

                                    if (msg == null) {
                                        return;
                                    }

                                    MsgResponse response = new MsgResponse();

                                    response.setPacketID(msg.getPacketID());
                                    response.setType(msg.getType());
                                    response.setFormName(fromname);
                                    response.setContent(msg.getContent());

                                    /*if (date == null) {

                                        date = new Date();
                                    }*/

                                    response.setTime(new Date(msg.getTime()));

                                    response.setUserId(msg.getUserID());

                                    OpenfireService.le_cb.on_Apply(response);


                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }

                    }else{

                        LogUtil.i(TAG,"离线消息：");
                        //如果是离线消息

                        Message message = (Message) packet;

                        //判断是群聊还是私聊
                        Message.Type type = message.getType();

                        if (type == Message.Type.groupchat) {

                            String from = message.getFrom();

                            if (from.contains("/")) {

                                //    LogUtil.i(GlobalConsts.TAG,"from="+from);

                                String fromname = from.substring(0, from.lastIndexOf("@"));

                                if(openfrom == null){
                                    return;
                                }
                                if(!openfrom.getFromName().equals(fromname)){
                                    return;
                                }

                                op_Message msg = null;
                                try {
                                    msg = gson.fromJson(message.getBody(), op_Message.class);
                                } catch (JsonSyntaxException e) {
                                    e.printStackTrace();
                                }

                                if(msg == null){
                                    return;
                                }


                                if(msg.getType() == MsgResponse.Leave){

                                    boolean isname = true;

                                    for(String s : nameLists){

                                        if(s.equals(String.valueOf(msg.getUserID()))){

                                            isname = false;

                                            break;
                                        }
                                    }


                                    if(isname){

                                        nameLists.add(String.valueOf(msg.getUserID()));

                                    }

                                }else if(msg.getType() == MsgResponse.Apply){

                                    if(ownerUser == null){
                                        return;
                                    }

                                    if(!ownerUser.getFormName().equals(fromname)){
                                        return;
                                    }

                                    MsgResponse response = new MsgResponse();

                                    response.setPacketID(msg.getPacketID());
                                    response.setType(msg.getType());
                                    response.setFormName(fromname);
                                    response.setTime(new Date());

                                    response.setUserId(msg.getUserID());
                                    response.setContent(msg.getContent());

                                    OpenfireService.le_cb.on_Apply(response);

                                }



                            }

                        }else if(type == Message.Type.chat){

                            try {

                                String from = message.getFrom();

                                if (from.contains("/")) {

                                    //获取packet 时间

                                    int fromNameCode = from.indexOf("@");

                                    String fromname = from.substring(0, fromNameCode);

                                    op_Message msg = null;
                                    try {
                                        msg = gson.fromJson(message.getBody(), op_Message.class);
                                    } catch (JsonSyntaxException e) {
                                        e.printStackTrace();
                                    }

                                    if (msg == null) {
                                        return;
                                    }

                                    if(msg.getType() != MsgResponse.Result){

                                        return;
                                    }

                                    MsgResponse response = new MsgResponse();

                                    response.setPacketID(msg.getPacketID());
                                    response.setType(msg.getType());
                                    response.setFormName(fromname);
                                    response.setContent(msg.getContent());

                                    response.setTime(new Date());

                                    response.setUserId(msg.getUserID());

                                    OpenfireService.le_cb.on_Apply(response);

                                }
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void userjoin(MsgResponse response) {

    //    LogUtil.i(TAG,"超时Thread:"+response.toString());

        if(response.getType() == MsgResponse.Exit){

            for(int i = 0;i < open_user_join.size();i++){

                if(response.getUserId() == open_user_join.get(i).getUserId()){

                    if(response.getFormName().equals(open_user_join.get(i).getFormName())){

                        open_user_join.remove(i);

                        OpenfireService.le_cb.on_message(response);

                        break;

                    }


                }

            }

            for(int i = 0;i < open_user.size();i++){

                if(response.getUserId() == open_user.get(i).getUserId()){

                    open_user.remove(i);

                    break;

                }

            }

        }else if(response.getType() == MsgResponse.Join){

            boolean istrue = true;

            for(int i = 0;i < open_user.size();i++){

                if(response.getUserId() == open_user.get(i).getUserId()){

                    open_user.remove(i);

                    open_user.add(response);

                    istrue = false;

                    break;

                }

            }

            if(istrue){
                open_user.add(response);
            }

            if(open_user.size() == 1){

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                    //    LogUtil.i(TAG,"超时Thread："+open_user.toString());
                        while (open_user.size() > 0){

                            try {

                                for(int i = 0;i < open_user.size();i++){

                                    if((open_user.get(i).getTime().getTime()+20000) < System.currentTimeMillis()){

                                        MsgResponse m  = open_user.remove(i);

                                        open_user_join.add(m);

                                        boolean is = true;

                                        try {

                                            String path = OpenfireService.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+m.getFormName()+"/sms_user/";

                                            if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                                                File file = new File(path, String.valueOf(m.getUserId()));

                                                if (!file.exists()) {

                                                    is = false;

                                                }

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        if(!is){

                                            m.setType(MsgResponse.Into);

                                        }

                                        OpenfireService.le_cb.on_message(m);

                                        i--;

                                    }


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                    }
                }).start();

            }

        }


    }

    private void updatalists(String formName) {

        for(String s : nameLists){

            boolean isname = true;

            for(String a : MyApplicatoin.groupMems){
                if(a.equals(s)){
                    isname = false;
                }
            }

            LogUtil.i(TAG,"updatalists:"+MyApplicatoin.groupMems.toString());

            LogUtil.i(TAG,"updatalists:"+nameLists.toString());

            if(isname){
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        try {

                            Filter filter = new Filter();

                            filter.setGroupName(formName);
                            filter.setType(MsgResponse.Leave);
                            filter.setUserId(Long.valueOf(s));

                            MessageFilter.setGroupDBs(OpenfireService.this,filter);

                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
            }
        }

        nameLists.clear();

    }

    /**
     *
     * 解析XML 信息
     *
     * */

    private Presence_entity getPresenceXML(String s) {

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

        StringReader sr = null;

        int code = 1;

        Presence_entity  presen = new Presence_entity();

        try {

            DocumentBuilder db = dbf.newDocumentBuilder();

            sr = new StringReader(s);

            InputSource is = new InputSource(sr);

            Document document = db.parse(is);

            Element root = document.getDocumentElement();

            NodeList students = root.getElementsByTagName("x");

            for (int i=0,leni=students.getLength(); i < leni; i++) {

                Node student = students.item(i);

                NodeList studentInfo = student.getChildNodes();

                for (int j=0,lenj=studentInfo.getLength(); j<lenj; j++) {

                    Node node = studentInfo.item(j);

                //    LogUtil.i(TAG,"XmlStringBuilder:"+node.getNodeName());

                    if("item".equals(node.getNodeName().toString())){

                        NamedNodeMap itemInfo = node.getAttributes();

                        for (int k=0;k<itemInfo.getLength(); k++) {

                            Node itemNode = itemInfo.item(k);

                        //    LogUtil.i(TAG,"XmlStringBuilder.item:"+itemNode.getNodeName());

                            if("affiliation".equals(itemNode.getNodeName().toString())){
                                presen.setAffiliation(itemNode.getNodeValue());
                            }else if("jid".equals(itemNode.getNodeName().toString())){
                                presen.setJid(itemNode.getNodeValue());
                            }else if("role".equals(itemNode.getNodeName().toString())){
                                presen.setRole(itemNode.getNodeValue());
                            }


                        }


                    }else if("status".equals(node.getNodeName().toString())){


                        NamedNodeMap itemInfo = node.getAttributes();

                        for (int k=0;k<itemInfo.getLength(); k++) {

                            Node itemNode = itemInfo.item(k);

                        //    LogUtil.i(TAG,"XmlStringBuilder.status:"+itemNode.getNodeName());

                            if("code".equals(itemNode.getNodeName().toString())){
                                presen.setStatus(itemNode.getNodeValue());
                            }


                        }


                    }
                }

                code = 0;

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

            if(code == 0){

                return presen;
            }


            return null;

        }

    }

    public static OpenfireService get_le_service(Context context, op_intercface le_if) {

            if (le_service == null) {
                Intent intent = new Intent(context, OpenfireService.class);
                context.startService(intent);
            }

            OpenfireService.le_cb = le_if;

            return OpenfireService.le_service;
    }


    /**
     *
     * 新建聊天室
     *
     * */

    public void newfrom(Openfrom openfroms){

        LogUtil.i(TAG,"newfrom:"+openfroms.toString());

        byte code = NO;

        try {

            if(queryChatRoom(openfroms) == NO){

                if (multiUserChat != null) {

                    try {


                        Message message = new Message();

                        message.setTo(multiUserChat.getRoom());

                        op_Message mes = new op_Message();

                        mes.setPacketID(System.currentTimeMillis());
                        mes.setType(MsgResponse.Leave);
                        mes.setUserID(MyApplicatoin.evenUser.getId());
                        mes.setTime(new Date().getTime());
                        mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                        //Log.d("bbbbbbbbbbbbbbbbb","111     :     "+MyApplicatoin.evenUser.getName());
                        String bod = gson.toJson(mes);

                        message.setBody(bod);


                        message.setType(Message.Type.groupchat);

                        /*DelayInformation delay = new DelayInformation(new Date());

                        delay.setReason(String.valueOf(delay.getStamp().getTime()));

                        message.addExtension(delay);*/

                        message.setPacketID(String.valueOf(System.currentTimeMillis()));

                        multiUserChat.sendMessage(message);

                    } catch (XMPPException e) {
                        e.printStackTrace();
                    }

                    multiUserChat.leave();
                    multiUserChat = null;
                }

                multiUserChat = new MultiUserChat(xmppConnection, openfroms.getFromName() + "@conference." + xmppConnection.getServiceName());
                multiUserChat.create(openfroms.getFromName());
                multiUserChat.sendConfigurationForm(new Form(Form.TYPE_SUBMIT));
                //----创建手动配置聊天室----
                //获取聊天室的配置表单
                Form form = multiUserChat.getConfigurationForm();
                //根据原始表单创建一个要提交的新表单
                Form submitForm = form.createAnswerForm();
                //向提交的表单添加默认答复
                List<FormField> fields = (List<FormField>) form.getFields();
                Iterator<FormField> it = fields.iterator();
                while(it.hasNext()) {
                    FormField field = (FormField) it.next();
                    if(!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                        submitForm.setDefaultAnswer(field.getVariable());
                    }
                }
                //重新设置聊天室名称
                submitForm.setAnswer("muc#roomconfig_roomname", openfroms.getFromName());
                //设置聊天室的新拥有者
                List<String> owners = new ArrayList<String>();
                owners.add(openfroms.getUserId()+"@"+openfire_host_name);

                submitForm.setAnswer("muc#roomconfig_roomowners", owners);
                //设置是否使用密码
                /*submitForm.setAnswer("muc#roomconfig_passwordprotectedroom", true);
                //设置密码
                submitForm.setAnswer("muc#roomconfig_roomsecret", "reserved");*/
                //设置描述
                submitForm.setAnswer("muc#roomconfig_roomdesc", openfroms.getUserId());

                //设置允许占有者更改主题

                submitForm.setAnswer("muc#roomconfig_changesubject", true);

                //设置聊天室最大人数
                List list =  new ArrayList();
                list.add("0");
                submitForm.setAnswer("muc#roomconfig_maxusers",list);

                List list2 =  new ArrayList();
                list2.add("anyone");

                submitForm.setAnswer("muc#roomconfig_whois",list2);



                //设置聊天室是持久聊天室，即将要被保存下来
                submitForm.setAnswer("muc#roomconfig_persistentroom", true);
                //发送已完成的表单到服务器配置聊天室
                multiUserChat.sendConfigurationForm(submitForm);

                multiUserChat.join(String.valueOf(openfroms.getUserId()));

                MyApplicatoin.openfrom = openfroms;

                openfrom = openfroms;
            //    seekfrom();

                code = YES;

                open_user_join.clear();
                open_user.clear();

            }else if(queryChatRoom(openfroms) == YES){

                joinfrom(openfroms);

            }


        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
        } catch (SmackException e) {
            e.printStackTrace();
        }finally {
            OpenfireService.le_cb.on_newfrom(code);

        }
    }


    /**
     *
     * 删除聊天室
     *
     * */

    public void destroyfrom(Openfrom openfroms){


        byte code = NO;

        try {

            if(multiUserChat != null){

                try {

                    Message message = new Message();

                    message.setTo(multiUserChat.getRoom());

                    op_Message mes = new op_Message();

                    mes.setPacketID(System.currentTimeMillis());
                    mes.setType(MsgResponse.Leave);
                    mes.setUserID(MyApplicatoin.evenUser.getId());
                    mes.setTime(new Date().getTime());
                    mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                    String bod = gson.toJson(mes);

                    message.setBody(bod);

                    message.setType(Message.Type.groupchat);

                    /*DelayInformation delay = new DelayInformation(new Date());

                    delay.setReason(String.valueOf(delay.getStamp().getTime()));

                    message.addExtension(delay);
*/

                    multiUserChat.sendMessage(message);

                } catch (XMPPException e) {
                    e.printStackTrace();
                }



                multiUserChat.leave();
                multiUserChat = null;
            }

            openfrom = null;

            MyApplicatoin.openfrom = null;

            MyApplicatoin.IS_OPEN_JOIN = false;

            code = YES;

        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();

        }finally {
            OpenfireService.le_cb.on_destroyfrom(code);
        }

    }

    /**
     *
     * 加入聊天室
     *
     * */
    public void joinfrom(final Openfrom openfroms){

        LogUtil.i(TAG,"joinfrom:");

        if(openfroms.getFromName() != null){

            new Thread(new Runnable() {
                @Override
                public void run() {

                    leng = 0;

                    JoinMultiUserChat(openfroms);
                }
            }).start();
        }

    }

    private void JoinMultiUserChat(Openfrom openfroms) {

        if(openfroms.getFromName() != null){

            LogUtil.i(TAG,"JoinMultiUserChat");

            byte code = NO;

            try {

                //遍历每个人所创建的群

                if(xmppConnection != null){

                    if(queryChatRoom(openfroms) == YES){

                        if(multiUserChat != null){

                            try {

                                String fromname = multiUserChat.getRoom().substring(0, multiUserChat.getRoom().lastIndexOf("@"));

                                if(!fromname.equals(openfroms.getFromName())){

                                    Message message = new Message();

                                    message.setTo(multiUserChat.getRoom());

                                    op_Message mes = new op_Message();

                                    mes.setPacketID(System.currentTimeMillis());
                                    mes.setType(MsgResponse.Leave);
                                    mes.setUserID(MyApplicatoin.evenUser.getId());
                                    mes.setTime(new Date().getTime());
                                    mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                                    String bod = gson.toJson(mes);
                                   // Log.d("bbbbbbbbbbbbbbbbb","22222     :     "+MyApplicatoin.evenUser.getName());
                                    message.setBody(bod);

                                    message.setType(Message.Type.groupchat);

                                    /*DelayInformation delay = new DelayInformation(new Date());

                                    delay.setReason(String.valueOf(delay.getStamp().getTime()));

                                    message.addExtension(delay);*/

                                    multiUserChat.sendMessage(message);

                                }

                            } catch (XMPPException e) {
                                e.printStackTrace();
                            }


                            multiUserChat.leave();
                            multiUserChat = null;
                        }



                        multiUserChat = new MultiUserChat(xmppConnection, openfroms.getFromName()+ "@conference." + xmppConnection.getServiceName());

                        //    seekfrom();
                        multiUserChat.join(String.valueOf(openfroms.getUserId()));

                        /*try {

                            multiUserChat.sendRegistrationForm(new Form(Form.TYPE_SUBMIT));

                            Form form = multiUserChat.getRegistrationForm();

                            if(form != null){

                                //根据原始表单创建一个要提交的新表单
                                Form submitForm = form.createAnswerForm();
                                //向提交的表单添加默认答复
                                List<FormField> fields = (List<FormField>) form.getFields();
                                Iterator<FormField> it = fields.iterator();
                                while(it.hasNext()) {
                                    FormField field = (FormField) it.next();
                                    if(!FormField.TYPE_HIDDEN.equals(field.getType()) && field.getVariable() != null) {
                                        submitForm.setDefaultAnswer(field.getVariable());
                                    }
                                    LogUtil.i(TAG,"FormField:"+field.getVariable()+","+field.getType().toString()+","+field.getLabel());
                                }

                                submitForm.setAnswer("muc#register_roomnick", String.valueOf(openfroms.getUserId()));
                                submitForm.setAnswer("muc#register_faqentry", "member");

                                multiUserChat.sendRegistrationForm(submitForm);

                            }
                        } catch (SmackException.NoResponseException e) {
                            e.printStackTrace();
                        } catch (XMPPException.XMPPErrorException e) {
                            e.printStackTrace();
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }*/


                        LogUtil.i(TAG,"加入聊天室成功");
                        openfrom = openfroms;

                        open_user_join.clear();
                        open_user.clear();

                        OpenfireService.le_cb.on_joinfrom(YES);

                        code = YES;

                    }else if(queryChatRoom(openfroms) == NO){

                        newfrom(openfroms);

                        code = YES;

                    }else if(queryChatRoom(openfroms) == ERRO){

                    //    Connect(openfroms);

                        code = NO;

                    }


                }
            } catch (SmackException.NoResponseException e) {

                e.printStackTrace();
                OpenfireService.le_cb.on_joinfrom(NO);

            } catch (XMPPException.XMPPErrorException e) {

                e.printStackTrace();
                OpenfireService.le_cb.on_joinfrom(NO);

            } catch (SmackException.NotConnectedException e) {

                e.printStackTrace();
                OpenfireService.le_cb.on_joinfrom(NO);

            } finally {

                if(code == NO){

                    if(leng < 2){
                        leng++;

                        try {
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        JoinMultiUserChat(openfroms);
                    }

                }else{
                    leng = 0;
                }

                try {
                    //发送smack心跳包
                    PingManager.getInstanceFor(xmppConnection).setPingInterval(5);
                    PingManager.getInstanceFor(xmppConnection).registerPingFailedListener(listener);

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }



        }
    }


    /**
     *
     * 查询聊天室是否存在
     *
     * */
    private Byte queryChatRoom(Openfrom openfroms) {

        byte isThere = NO;

        int times = 3;
        do
        {
            RoomInfo rinfo = null;
        try {


             rinfo =  MultiUserChat.       getRoomInfo(xmppConnection,openfroms.getFromName()+"@conference."+openfire_host_name);

            LogUtil.i("  queryChatRoom",rinfo.getRoom()+"::"+rinfo.getDescription());
            isThere = YES;
           /*  这段代码是由Sam删除，原作者LJK对分组进行查重的时候是采用把服务器上面所有的分组下载下来进行对比的做法，才实际使用过程发现
           当服务器分组超过1000以上的时候，因为数据太多，这种做法会让这里报错导致无法创建加入和创建新群。
            for (HostedRoom host : MultiUserChat.getHostedRooms(xmppConnection,xmppConnection.getServiceName())) {


            //    LogUtil.i(TAG,"xmppConnection.getServiceName():"+xmppConnection.getServiceName());
                if(host.getJid().equals(serviceName)){

                    Collection<HostedRoom> singleHost = MultiUserChat.getHostedRooms(xmppConnection, host.getJid());

                    for (HostedRoom room : singleHost){

                    //    LogUtil.i(TAG,"queryChatRoom:"+room.getJid());
                    //    LogUtil.i(TAG,"queryChatRoom:"+room.getName());

                     //   RoomInfo info = MultiUserChat.getRoomInfo(xmppConnection, host.getJid());

                        if(room.getName().equals(openfroms.getFromName())){

                            isThere = YES;

                            break;
                        }
                    }
                }
            }*/

        } catch (SmackException.NoResponseException e) {
            e.printStackTrace();

            isThere = ERRO;
        } catch (XMPPException.XMPPErrorException e) {
            e.printStackTrace();
            isThere = NO;
        } catch (SmackException.NotConnectedException e) {
            e.printStackTrace();
            isThere = ERRO;
        } finally {
            if(rinfo!=null)
            {
                times = 0;
            }
            else times--;

        }

        }while(times>0);

        return isThere;
    }

    /**
     * open fire 查询群信息
     *
     * */
    public void seekfrom(){

        final List<String> namelist = new ArrayList<>();

        try {

        //    final DAOmemberDao memberDao = DAOService.get(OpenfireService.this).getsession().getDAOmemberDao();

            final List<String> user_list = multiUserChat.getOccupants();

            LogUtil.i(TAG,"群成员List："+multiUserChat.getOccupants().size());


            try {

                LogUtil.i(TAG,"群成员List："+multiUserChat.getMembers().size());

                LogUtil.i(TAG,"群成员List："+multiUserChat.getParticipants().size());

                Collection<Occupant> ss = multiUserChat.getParticipants();

                Iterator it = ss.iterator(); // 获得一个迭代子
                while(it.hasNext()) {
                    Occupant obj = (Occupant) it.next(); // 得到下一个元素

                    LogUtil.i(TAG,"群成员List："+obj.getAffiliation());
                    LogUtil.i(TAG,"群成员List："+obj.getJid());
                    LogUtil.i(TAG,"群成员List："+obj.getNick());
                    LogUtil.i(TAG,"群成员List："+obj.getRole());

                }
            } catch (SmackException.NoResponseException e) {
                e.printStackTrace();
            } catch (XMPPException.XMPPErrorException e) {
                e.printStackTrace();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }


            if(user_list.size() > 0){

                /*int from = user_list.get(0).indexOf("@");

                final String groupNames = user_list.get(0).substring(0,from);

                File[] files = null;

                String path = OpenfireService.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+groupNames+"/sms_user/";

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                    File filePath = new File(path);

                    if (!filePath.exists()) {

                        filePath.mkdirs();
                    }

                    if(filePath.exists()){

                        files = filePath.listFiles();

                    }

                }*/


                if (user_list.size() > 0) {

                    for(final String names : user_list){

                        final String users = names.substring(names.indexOf("/") + 1);

                        int fromCode = names.indexOf("@");

                        final String groupName = names.substring(0,fromCode);

                        LogUtil.i(TAG,"groupName:"+groupName+",name:"+users);

                        MyApplicatoin.groupMems.add(users);

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                try {

                                    Filter filter = new Filter();

                                    filter.setGroupName(groupName);
                                    filter.setType(MsgResponse.Join);
                                    filter.setUserId(Long.valueOf(users));

                                    MessageFilter.setGroupDBs(OpenfireService.this,filter);

                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();

                    }


                    final String users = user_list.get(0).substring(user_list.get(0).indexOf("/") + 1);

                    int fromCode = user_list.get(0).indexOf("@");

                    final String groupName = user_list.get(0).substring(0,fromCode);

                    String path = OpenfireService.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+groupName+"/sms_user/";


                    if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                        File filePath = new File(path);

                        if (!filePath.exists()) {

                            filePath.mkdirs();
                        }

                        File[] files = null;

                        if(filePath.exists()){

                            files = filePath.listFiles();

                        }


                        if(files == null){

                            return;

                        }
                        if(files.length == 0){

                            return;

                        }


                        List<String>  name = new ArrayList<>();

                        for(File f : files){


                            name.add(f.getName());

                        }

                        LogUtil.i(TAG,"团队人数："+name.toString());


                    }

                /*int fromCode = multiUserChat.getRoom().indexOf("@");

                final String groupName = multiUserChat.getRoom().substring(0,fromCode);

                final String users = multiUserChat.getRoom().substring(multiUserChat.getRoom().indexOf("/") + 1);


                String path = OpenfireService.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+groupName+"/sms_user/";

                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                    File filePath = new File(path);
                    if (!filePath.exists()) {

                        filePath.mkdirs();
                    }

                    File file = new File(path, fileName + ".png");


                }*/

                /*new Thread(new Runnable() {
                    @Override
                    public void run() {

                        for(final String names : user_list){

                            final String users = names.substring(names.indexOf("/") + 1);

                            LogUtil.i(TAG,"groupName:"+groupName+",name:"+users);

                            namelist.add(names);

                            final long id = Long.parseLong(users);

                            User user = new User();
                            user.setId(id);

                            geiUser(memberDao,user,id,groupName);

                        }
                    }
                }).start();*/

                }
            }

            LogUtil.i(TAG,"群成员名字："+namelist);
            //   OpenfireService.le_cb.on_seekfrom(namelist);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    /**
     * open fire 查询群信息
     *
     * */
    public void esc(){

        MyApplicatoin.openfrom = null;
        MyApplicatoin.IS_OPEN_LOGIN = false;
        MyApplicatoin.IS_OPEN_JOIN = false;

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(multiUserChat != null){

                    try {

                        multiUserChat.leave();

                        multiUserChat = null;

                    } catch (SmackException.NotConnectedException e) {
                        ExceptionUtil.handleException(e);
                    }

                }

                if(xmppConnection != null){

                    if(xmppConnection.isConnected()){

                        try {

                            xmppConnection.disconnect();

                        } catch (SmackException.NotConnectedException e) {
                            ExceptionUtil.handleException(e);
                        }

                    }

                    xmppConnection = null;

                }


            }
        }).start();

        stopSelf();

    }

    private void geiUser(final DAOmemberDao memberDao, User user, final long id, final String groupName) {

        //查找成员信息
        sms_request.dorequest(OpenfireService.this, sms_request.GET, user , new RequestCB() {
            @Override
            public void respone(int opcode, Object obj) {

                if(opcode == sms_request.GET){

                    final User user = (User) obj;

                    List<DAOmember> daOmemberlist = memberDao.loadAll();

                    DAOmember member = null;

                    for(DAOmember a : daOmemberlist){

                        if(a.getUser_id() == id ){

                            if(a.getGroup_name().equals(groupName)){
                                member = a;
                            }
                        }
                    }



                    if(member == null ){

                        LogUtil.i(TAG,"OpenfireService.DAOmember == null:");

                        final DAOmember omember = new DAOmember();

                        omember.setUser_id(user.getId());
                        omember.setUser_name(user.getName());
                        omember.setSex(user.getSex());
                        omember.setVersion(user.getVersion());
                        omember.setGroup_name(groupName);

                        Avatar avatar = new Avatar();
                        avatar.setUser_id(user.getId());

                        sms_request.dorequest(OpenfireService.this, sms_request.GET, avatar, new RequestCB() {
                            @Override
                            public void respone(int opcode, Object obj) {

                                if(opcode == sms_request.GET){

                                    Avatar s = (Avatar) obj;
                                    LogUtil.i(TAG,"OpenfireService_Avatar:"+s);
                                    try {
                                        if (s.getUrl() != null && s.getData() != null) {

                                            String icon_path = Tools.writeToSdcard(OpenfireService.this,groupName,s.getUser_id().toString(),s.getData());

                                            omember.setAvatar_url(icon_path);

                                            memberDao.insert(omember);

                                        }
                                    } catch (IOException e) {
                                        ExceptionUtil.handleException(e);
                                    }

                                }

                            }
                        });

                    }else if(member.getVersion() != user.getVersion()){

                        LogUtil.i(TAG,"OpenfireService_Version不一样:"+user.getVersion()+",member.getVersion():"+member.getVersion());

                        Avatar avatar = new Avatar();
                        avatar.setUser_id(user.getId());

                        final DAOmember finalMember = member;

                        sms_request.dorequest(OpenfireService.this, sms_request.GET, avatar, new RequestCB() {
                            @Override
                            public void respone(int opcode, Object obj) {
                                LogUtil.i(TAG,"OpenfireService_Avatar:"+opcode);

                                if(opcode == sms_request.GET){

                                    Avatar s = (Avatar) obj;
                                    try {
                                        if (s.getUrl() != null && s.getData() != null) {

                                            String icon_path = Tools.writeToSdcard(OpenfireService.this, groupName, s.getUser_id().toString(),s.getData());

                                            finalMember.setUser_id(user.getId());
                                            finalMember.setUser_name(user.getName());
                                            finalMember.setSex(user.getSex());
                                            finalMember.setVersion(user.getVersion());
                                            finalMember.setAvatar_url(icon_path);

                                            memberDao.update(finalMember);

                                        }
                                    } catch (IOException e) {
                                        ExceptionUtil.handleException(e);
                                    }
                                }

                            }

                        });

                    }

                }
            }

        });

    }


    /**
     * open fire 发送消息
     *
     * */
    public void sendApply(final Openfrom openfroms){

        LogUtil.i(TAG,"sendApply:"+openfroms.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {

                byte code = 0;

                try {

                    String name = null;

                    try {

                        for (HostedRoom host : MultiUserChat.getHostedRooms(xmppConnection,xmppConnection.getServiceName())) {

                            if(host.getJid().equals(serviceName)){

                                Collection<HostedRoom> singleHost = MultiUserChat.getHostedRooms(xmppConnection, host.getJid());

                                for (HostedRoom room : singleHost){

                                    if(room.getName().equals(openfroms.getFromName())){

                                        RoomInfo info = MultiUserChat.getRoomInfo(xmppConnection,room.getName()+"@"+host.getJid());

                                        name = info.getDescription();

                                        break;
                                    }
                                }
                            }
                        }

                    } catch (SmackException.NoResponseException e) {
                        e.printStackTrace();

                    } catch (XMPPException.XMPPErrorException e) {
                        e.printStackTrace();

                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();

                    }

                    if(name != null){

                        MultiUserChat multiChat = new MultiUserChat(xmppConnection, openfroms.getFromName()+ "@conference." + xmppConnection.getServiceName());
                        //    seekfrom();
                        multiChat.join(String.valueOf(openfroms.getUserId()));

                        op_Message mes = new op_Message();

                        mes.setPacketID(System.currentTimeMillis());
                        mes.setType(MsgResponse.Apply);
                        mes.setUserID(openfroms.getUserId());
                        mes.setContent("1");
                        mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                       // Log.d("bbbbbbbbbbbbbbbbb","3333333     :     "+MyApplicatoin.evenUser.getName());
                        String bod = gson.toJson(mes);

                        /*Message message = new Message();

                        message.setType(Message.Type.groupchat);

                        message.setBody(bod);

                        DelayInformation delay = new DelayInformation(new Date());

                        delay.setReason(String.valueOf(delay.getStamp().getTime()));

                        message.addExtension(delay);

                        message.setPacketID(String.valueOf(System.currentTimeMillis()));*/

                        multiChat.sendMessage(bod);

                        multiChat.leave();

                        code = 1;


                        SharedPreferences sharedPreferences = OpenfireService.this.getSharedPreferences(GlobalConsts.ACTION_IM_GROUP, Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("id", openfroms.getFromName());
                        editor.putString("name",name);
                        editor.putString("boolean", "false");

                        editor.commit();

                    }



                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                } catch (XMPPException e) {
                    e.printStackTrace();
                } finally {


                    if(code == 1){

                        MsgResponse response = new MsgResponse();

                        response.setPacketID(System.currentTimeMillis());
                        response.setType(MsgResponse.Apply);
                        response.setContent("1");
                        response.setFormName(openfroms.getFromName());

                        response.setTime(new Date());

                        response.setUserId(openfroms.getUserId());

                        OpenfireService.le_cb.on_Apply(response);

                    }else{

                        LogUtil.i(TAG,"发送至私密群失败");

                    }




                }


                /*String name = null;

                try {

                    for (HostedRoom host : MultiUserChat.getHostedRooms(xmppConnection,xmppConnection.getServiceName())) {

                        if(host.getJid().equals(serviceName)){

                            Collection<HostedRoom> singleHost = MultiUserChat.getHostedRooms(xmppConnection, host.getJid());

                            for (HostedRoom room : singleHost){

                                if(room.getName().equals(openfroms.getFromName())){

                                    RoomInfo info = MultiUserChat.getRoomInfo(xmppConnection,room.getName()+"@"+host.getJid());

                                    name = info.getDescription();

                                    break;
                                }
                            }
                        }
                    }

                } catch (SmackException.NoResponseException e) {
                    e.printStackTrace();

                } catch (XMPPException.XMPPErrorException e) {
                    e.printStackTrace();

                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();

                }

                if(name != null){

                    LogUtil.i(TAG,"sendApply:"+name);

                    try {



                        Chat chat = chatManager.createChat(name + "@"+openfire_host_name+"/Smack",null);

                        Message message = new Message();

                        op_Message mes = new op_Message();

                        mes.setPacketID(System.currentTimeMillis());
                        mes.setType(MsgResponse.Apply);
                        mes.setUserID(openfroms.getUserId());
                        mes.setContent("1");

                        String bod = gson.toJson(mes);

                        message.setType(Message.Type.chat);

                        message.setBody(bod);

                        DelayInformation delay = new DelayInformation(new Date());

                        delay.setReason(String.valueOf(delay.getStamp().getTime()));

                        message.addExtension(delay);

                        message.setPacketID(String.valueOf(System.currentTimeMillis()));

                        chat.sendMessage(message);

                    } catch (SmackException.NotConnectedException e) {

                        e.printStackTrace();

                    } finally {


                        MsgResponse response = new MsgResponse();

                        response.setPacketID(System.currentTimeMillis());
                        response.setType(MsgResponse.Apply);
                        response.setContent("1");
                        response.setFormName(openfroms.getFromName());

                        response.setTime(new Date());

                        response.setUserId(openfroms.getUserId());

                        OpenfireService.le_cb.on_Apply(response);

                    }
                }*/
            }
        }).start();
    }
    /**
     * open fire 发送消息
     *
     * */
    public void ChatMessage(final Openfrom openfroms){

        LogUtil.i(TAG,"ChatMessage:"+openfroms.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {


                    if(openfroms.getType() == MsgResponse.Join){

                        Chat chat = chatManager.createChat(openfroms.getUserId() + "@"+openfire_host_name+"/Smack",null);


                    }else{

                        Chat chat = chatManager.createChat(openfroms.getUserId() + "@"+openfire_host_name+"/Smack",null);

                        Message message = new Message();

                        op_Message mes = new op_Message();

                        mes.setPacketID(openfroms.getMessageID());

                        mes.setType(openfroms.getType());
                        mes.setUserID(MyApplicatoin.evenUser.getId());
                        mes.setContent(openfroms.getMessage());
                        mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                        mes.setTime(new Date().getTime());
                       // Log.d("bbbbbbbbbbbbbbbbb","555555    :     "+MyApplicatoin.evenUser.getName());
                        String bod = gson.toJson(mes);

                        message.setType(Message.Type.chat);

                        message.setBody(bod);

                        /*DelayInformation delay = new DelayInformation(new Date());

                        delay.setReason(String.valueOf(delay.getStamp().getTime()));

                        message.addExtension(delay);*/

                        message.setPacketID(String.valueOf(openfroms.getMessageID()));

                        chat.sendMessage(message);

                    }

                } catch (SmackException.NotConnectedException e) {

                    e.printStackTrace();

                }
            }
        }).start();
    }

    /**
     * open fire 发送消息
     *
     * */
    public void sendMessage(final Openfrom openfroms){

        new Thread(new Runnable() {
            @Override
            public void run() {
                //发送到网上

                /*String friend = "48"+"@conference." + xmppConnection.getServiceName();

                Chat chat = getFriendChat(friend,null);

                String msgjson ="123";
                try {
                    chat.sendMessage(msgjson);
                } catch (XMPPException e) {
                    e.printStackTrace();
                } catch (SmackException.NotConnectedException e) {
                    e.printStackTrace();
                }*/

                if (multiUserChat != null) {

                    try {
                        LogUtil.i(TAG,"sendMessage:"+multiUserChat.getRoom());

                        if (MyApplicatoin.openfrom != null) {

                            Message message = new Message();

                            message.setTo(multiUserChat.getRoom());

                            op_Message mes = new op_Message();

                            mes.setContent(openfroms.getMessage());
                            mes.setPacketID(openfroms.getMessageID());
                            mes.setType(openfroms.getType());
                            mes.setUserID(openfroms.getUserId());
                            mes.setUserName(MyApplicatoin.evenUser.getName());//自己加，无用
                            Time time = null;
                           // Log.d("bbbbbbbbbbbbbbbbb","6666666     :     "+MyApplicatoin.evenUser.getName());
                            if(xmppConnection.isConnected()){

                                try {

                                    Time var2 = new Time();

                                    time = (Time) xmppConnection.createPacketCollectorAndSend(var2).nextResultOrThrow();

                                } catch (SmackException.NoResponseException e) {

                                    e.printStackTrace();

                                }
                            }else{

                                LogUtil.i(TAG,"当前无法连接到openfire:");

                            }

                            if(time != null){
                                mes.setTime(time.getTime().getTime());
                            }else{
                                mes.setTime(new Date().getTime());
                            }

                            LogUtil.i(TAG,mes.toString());
                            String bod = gson.toJson(mes);

                            message.setBody(bod);
                            message.setType(Message.Type.groupchat);


                            /*DelayInformation delay = null;

                            if(time != null){
                                delay = new DelayInformation(time.getTime());
                            }else{
                                delay = new DelayInformation(new Date());
                            }

                            delay.setReason(String.valueOf(delay.getStamp().getTime()));

                            LogUtil.i(TAG,"发送时间:"+delay.getStamp().getTime());

                            message.addExtension(delay);*/

                            multiUserChat.sendMessage(message);

                            LogUtil.i(TAG,"2222222:"+System.currentTimeMillis());


                        }

                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }

                }else{

                    LogUtil.i(TAG,"multiUserChat == null");

                    joinfrom(openfroms);



                }
            }
        }).start();
    }

    /**
     *
     * open fire 登录模块
     *
     * */

    public void Login(final Openfrom openfrom){
        LogUtil.i(TAG,"Login准备连接open fire");

        new Thread(new Runnable() {
            @Override
            public void run() {

                if(xmppConnection != null){

                    LogUtil.i(TAG,"Login连接状态："+xmppConnection.isConnected());

                    try {

                        xmppConnection.connect();

                        LogUtil.i(TAG, "Login连接openfire结果"+ xmppConnection.isConnected());

                        if(xmppConnection.isConnected()){

                            loging(openfrom);
                        }

                    } catch (SmackException e) {

                        e.printStackTrace();

                    } catch (IOException e) {

                        e.printStackTrace();

                    } catch (XMPPException e) {

                        e.printStackTrace();
                    }

                    /*if(xmppConnection.isConnected()){

                        loging(openfrom);

                    }else{

                        try {

                            xmppConnection.connect();

                            LogUtil.i(TAG, "Login连接openfire结果"+ xmppConnection.isConnected());

                            if(xmppConnection.isConnected()){

                                loging(openfrom);
                            }

                        } catch (SmackException e) {

                            e.printStackTrace();

                        } catch (IOException e) {

                            e.printStackTrace();

                        } catch (XMPPException e) {

                            e.printStackTrace();
                        }

                    }*/
                    LogUtil.i("ddddddddddd","88888888888888888");
                }else{
                    LogUtil.i("ddddddddddd","777777777777");
                    Connect(openfrom);

                };
            }
        }).start();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        MyApplicatoin.openfrom = null;
        MyApplicatoin.IS_OPEN_LOGIN = false;
        MyApplicatoin.IS_OPEN_JOIN = false;


    }

    //连接
    private void Connect(final Openfrom openfrom){

        byte code = 0;

        if(xmppConnection != null){

            try {
                xmppConnection.disconnect();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }

        }
        ConnectionConfiguration configuration = new ConnectionConfiguration(host, 5222, serviceName);//9090  5222  5222
        LogUtil.i("ddddddddddd","6666666666666");
        //设置可以重新连接
        configuration.setReconnectionAllowed(true);
        //设置安全模式
        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        configuration.setSendPresence(true);
        //这一个方法和Asmack的老版本不是太一样.
        SASLAuthentication.supportSASLMechanism("PLAIN", 0);

        xmppConnection = new XMPPTCPConnection(configuration);

        registerInterceptorListener();

        try {

            xmppConnection.connect();

            LogUtil.i(TAG, "Connect连接openfire结果"+ xmppConnection.isConnected());
            LogUtil.i("ddddddddddd","5555555555"+"Connect连接openfire结果"+ xmppConnection.isConnected());
            if(xmppConnection.isConnected()){

                code = 1;
                loging(openfrom);

                //发送smack心跳包
                try {
                    PingManager.getInstanceFor(xmppConnection).setPingInterval(5);
                    PingManager.getInstanceFor(xmppConnection).registerPingFailedListener(listener);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }



        } catch (SmackException e) {

            ExceptionUtil.handleException(e);

        } catch (IOException e) {

            ExceptionUtil.handleException(e);

        } catch (XMPPException e) {

            ExceptionUtil.handleException(e);

        }finally {

            if(code == 0){

                if(leng < 2){
                    leng++;

                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Connect(openfrom);
                }

            }else{
                leng = 0;
            }

        }

    }
    private void loging(Openfrom openfrom) {

    //  LogUtil.i(TAG, "loging："+openfrom.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {

                byte code = ERRO;

                boolean loginIsSuccess = xmppConnection.isAuthenticated();

                if (loginIsSuccess) {

                    //将登录状态保存
                    MyApplicatoin.IS_OPEN_LOGIN = true;

                    JoinMultiUserChat(openfrom);

                    code = YES;

                    OpenfireService.le_cb.on_login(code);

                }else{

                    try {


                        xmppConnection.login(String.valueOf(openfrom.getUserId()), openfrom.getPassword());

                        //更改在线状态
                        Presence presence = new Presence(Presence.Type.available);
                        presence.setMode(Presence.Mode.available);

                        xmppConnection.sendPacket(presence);

                        //将登录状态保存
                        MyApplicatoin.IS_OPEN_LOGIN = true;

                        JoinMultiUserChat(openfrom);

                        if(xmppConnection.isAuthenticated()){

                            code = YES;

                        }else {

                            code = NO;

                        }

                        /*TaxiChatManagerListener chatManagerListener = new TaxiChatManagerListener();

                        ChatManager.getInstanceFor(xmppConnection).addChatListener(chatManagerListener);*/

                    } catch (XMPPException e) {
                        e.printStackTrace();
                    } catch (SmackException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }finally {

                        //    LogUtil.i(TAG, "code ："+code);

                        if(code == ERRO){

                        //    Connect(openfrom);

                            Register(openfrom);

                        }

                        OpenfireService.le_cb.on_login(code);

                    }
                }

                LogUtil.i(TAG, "登录结果："+xmppConnection.isAuthenticated());
            }
        }).start();


    }

    private PingFailedListener listener = new PingFailedListener() {
        @Override
        public void pingFailed() {
            LogUtil.i(TAG,"心跳失败");

            byte code = GlobalConsts.NO;

            OpenfireService.le_cb.on_ping(code);
        }
    };


    /**
     *
     * open fire 注册模块
     *
     */

    public void Register(final Openfrom openfrom){
        LogUtil.i(TAG,"Register准备连接open fire");

        if(xmppConnection != null){
            try {
                xmppConnection.disconnect();
            } catch (SmackException.NotConnectedException e) {
                e.printStackTrace();
            }
        }

        ConnectionConfiguration configuration = new ConnectionConfiguration(host, 5222, serviceName);//9090  5222  5222

        configuration.setSecurityMode(ConnectionConfiguration.SecurityMode.disabled);

        SASLAuthentication.registerSASLMechanism("PLAIN", SASLPlainMechanism.class);
        SASLAuthentication.supportSASLMechanism("PLAIN",0);

        xmppConnection = new XMPPTCPConnection(configuration);

        registerInterceptorListener();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    xmppConnection.connect();

                    LogUtil.i(TAG, "连接openfire结果"+ xmppConnection.isConnected());

                    if(xmppConnection.isConnected()){

                        try {

                            Registration reg = new Registration();
                            reg.setType(IQ.Type.SET);
                            Map<String, String> attributes = new HashMap<String, String>();
                            reg.setTo(xmppConnection.getHost());
                            attributes.put("username", String.valueOf(openfrom.getUserId()));
                            attributes.put("password", openfrom.getPassword());
                            reg.setAttributes(attributes);

                            PacketFilter filter = new AndFilter(new PacketIDFilter(reg.getPacketID()), new PacketTypeFilter(IQ.class));

                            PacketCollector collector = xmppConnection.createPacketCollector(filter);

                            xmppConnection.sendPacket(reg);

                            IQ result = (IQ) collector.nextResult(xmppConnection.getPacketReplyTimeout());
                            collector.cancel();

                            LogUtil.i(TAG,"result:"+result.toString());

                            if(result.getType() == IQ.Type.RESULT)
                            {
                                LogUtil.i(TAG,"注册成功");
                                OpenfireService.le_cb.on_Register(YES);

                                Login(openfrom);

                            }else{
                                LogUtil.i(TAG,"注册失败");
                                OpenfireService.le_cb.on_Register(NO);
                            }

                        } catch (Exception e) {
                            //注册openfire失败
                            e.printStackTrace();
                            LogUtil.i(TAG,"注册失败");
                            OpenfireService.le_cb.on_Register(NO);
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtil.i(TAG,"连接openfire结果"+ xmppConnection.isConnected());
                    OpenfireService.le_cb.on_Register(NO);
                }

            }
        }).start();
    }


    private String getUserIDandMessageID(String userId,long messageId){

        String acion = null;

        try {

            long user = Long.parseLong(userId);

            LogUtil.i(TAG,"getUserIDandMessageID:"+user);


            int i = 0;

            byte[] message = new byte[8];

            message[i++] = (byte) (user & 0xff);
            message[i++] = (byte) (user >> 8);
            message[i++] = (byte) (user >> 16);
            message[i++] = (byte) (user >> 24);

            message[i++] = (byte) (messageId & 0xff);
            message[i++] = (byte) (messageId >> 8);
            message[i++] = (byte) (messageId >> 16);
            message[i++] = (byte) (messageId >> 24);

            acion = Base64.encodeToString(message, Base64.DEFAULT);

        } catch (NumberFormatException e) {

            ExceptionUtil.handleException(e);

        }

        //    byte[] datas= Base64.decode(data, Base64.DEFAULT);

        return acion;
    }




    public void receptionNotification(){

        if(Build.VERSION.SDK_INT >= 26){
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            NotificationChannel Channel = new NotificationChannel(CHANNEL_ID,"主服务",NotificationManager.IMPORTANCE_HIGH);
            Channel.enableLights(false);//设置提示灯
            //Channel.setLightColor(Color.RED);//设置提示灯颜色
            Channel.setShowBadge(true);//显示logo
          //  Channel.setDescription("ytzn");//设置描述
            Channel.setBypassDnd(false);
            Channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); //设置锁屏可见 VISIBILITY_PUBLIC=可见
            manager.createNotificationChannel(Channel);

            Notification notification = new Notification.Builder(this)
                    .setChannelId(CHANNEL_ID)
                    .setContentTitle("Global Walkie")//标题
                    .setContentText("运行中...")//内容
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.icon_chat)//小图标一定需要设置,否则会报错(如果不设置它启动服务前台化不会报错,但是你会发现这个通知不会启动),如果是普通通知,不设置必然报错
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_chat))
                    .build();
            startForeground(1,notification);//服务前台化只能使用startForeground()方法,不能使用 notificationManager.notify(1,notification); 这个只是启动通知使用的,使用这个方法你只需要等待几秒就会发现报错了

        }else{

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Global Walkie")//设置标题
                    .setContentText("运行中...")//设置内容
                    .setWhen(System.currentTimeMillis())//设置创建时间
                    .setSmallIcon(R.mipmap.icon_chat)//设置状态栏图标
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.icon_chat))//设置通知栏图标
                    .build();
            startForeground(1,notification);
        }


    }


   /* *//**
     * 加入providers的函数 ASmack在/META-INF缺少一个smack.providers 文件
     *
     * @param pm
     *//*
    public void configureConnection(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

        // Roster Exchange

        pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());

        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());

        // Chat State
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());

        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());

        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());

        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }

        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

        // Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());

        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());

        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

        // Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.SessionExpiredError());
    }*/

}
