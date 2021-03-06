package com.sms.app.interphone.util.msnutil;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import com.sms.app.interphone.entity.Filter;
import com.sms.app.interphone.entity.Voice;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.services.OpenfireService;
import com.sms.app.interphone.ui.MyApplicatoin;
import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.bledriver.Message_request;
import com.sms.app.framework.dao.bean.DAOGroup;
import com.sms.app.framework.dao.bean.DAOMesg;
import com.sms.app.framework.dao.bean.commom.DAOGroupDao;
import com.sms.app.framework.dao.bean.commom.DAOMesgDao;
import com.sms.app.framework.dao.bean.commom.DAOService;
import com.sms.app.interphone.util.openutil.Openfrom;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

/**
 * Created by Administrator on 2017/5/8.
 *
 * Message 过滤器类
 *
 */

public class MessageFilter {

    private static String TAG = "YanShi...Log - MessageFilter";


    //消息过滤集合
    public static List<Filter> filterList = new ArrayList<>();

    public static List<MsgResponse> responseList = new ArrayList<>();


    public static TimerTask timer;

    public static Timer task = new Timer();

    public static TimerTask timerVoice;

    public static Timer taskVoice = new Timer();


    public static Message_request mes_request = null;

    public static Handler handler = null;

    public static Context mContext = null;





    private static int start = 0;
    private static int stop = 120;

    /**
     *
     * 将语音数据写成文件形式
     *
     *
     * @param response*/
    public static void new_yes_voice_data(final MsgResponse response){

        LogUtil.i(GlobalConsts.TAG,"即将写入文件的名字："+response.toString());

        new Thread(new Runnable() {
            @Override
            public void run() {


                File files = new File(response.getFireName());
                LogUtil.i(GlobalConsts.TAG,"即将写入文件的名字："+response.getFireName());

                OutputStream out = null;

                //如果文件夹不存在则创建
                if(files != null){

                    if (!files.exists()) {

                        try {
                            files.getParentFile().mkdirs();
                            files.createNewFile();

                            byte[] data = Base64.decode(response.getContent(), Base64.DEFAULT);

                            LogUtil.i("MessageFilter",data.length);

                            files.delete();

                            out = new FileOutputStream(files);
                            out.write(data);

                            LogUtil.i(GlobalConsts.TAG,"文件不存在");
                            files .mkdir();

                        } catch (IOException e) {
                            ExceptionUtil.handleException(e);
                        } finally {
                            try {

                                if(out != null){
                                    out.close();
                                }

                            } catch (IOException e) {
                                ExceptionUtil.handleException(e);
                            }
                        }
                    } else {
                        LogUtil.i(GlobalConsts.TAG,"文件已存在");
                    }

                }
            }


        }).start();

    }

    /**
     *
     * 轮循过滤器检查缓冲器是否有此数据
     *
     * */
    private static void messagefrlter(Context context, Handler mhandler, Filter filter, MsgResponse response) {

        handler = mhandler;
        mContext = context;

        boolean isFilter = false;

        try {


            try {

                if(filter.getType() == MsgResponse.Join || filter.getType() == MsgResponse.Into){
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            setGroupDBs(context,filter);
                        }
                    }).start();
                }

                for(Filter f : filterList){

                //    LogUtil.i(GlobalConsts.TAG,"response222:"+response.toString());

                    if(f.getPacketID().equals(filter.getPacketID()) && f.getType() == filter.getType() && f.getUserId() == filter.getUserId()){

                        isFilter = true;
                        break;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            LogUtil.i(TAG,"isFilter:"+isFilter);

            if(!isFilter){

                try {

                    int fromCode = response.getFormName().indexOf("@");

                    if(fromCode > 0){
                        response.setFormName(response.getFormName().substring(0,fromCode));
                    }

                    if(response.getType() == MsgResponse.Voice){

                        byte[] data = Base64.decode(response.getContent(), Base64.DEFAULT);

                        if(filter.getCode() != Filter.phone){

                            String firename = new String(mContext.getExternalFilesDir(Environment.DIRECTORY_NOTIFICATIONS).getPath()+"/data/"+Tools.getGroupName(MyApplicatoin.interphone.getImCode().getName())+"/sms_voice/"+ UUID.randomUUID().toString()+".pcm");

                            response.setFireName(firename);

                        }

                        new_yes_voice_data(response);

                        /*LogUtil.i(TAG,"语音消息过滤:"+data.length+",type:"+filter.getCode());

                        if(data != null){

                            boolean isResponse = false;

                            LogUtil.i(TAG,"语音消息过滤："+responseList.size());

                            for(MsgResponse m : responseList){

                                byte[] datas = Base64.decode(m.getContent(), Base64.DEFAULT);

                                if(data.length > datas.length) {

                                    int length = data.length - datas.length;

                                    if (length >= start && length <= stop) {

                                        isResponse = true;

                                    }

                                }else if(data.length < datas.length){

                                    int length = datas.length - data.length;

                                    if (length >= start && length <= stop) {

                                        isResponse = true;

                                    }

                                }else if(data.length == datas.length){
                                    isResponse = true;
                                }

                            }

                            LogUtil.i(TAG,"语音消息过滤："+isResponse);

                            if(MyApplicatoin.evenUser != null){
                                if(MyApplicatoin.evenUser.getId() == response.getUserId()){
                                    isResponse = false;
                                }
                            }


                            if(!isResponse){

                                filterList.add(filter);

                                setDatabase(context,filter,response);

                                new_yes_voice_data(response);

                            }

                            if(timerVoice == null){

                                timerVoice = new TimerTask() {
                                    @Override
                                    public void run() {

                                        try {

                                            if (responseList.size() > 0) {

                                                int length = responseList.size();

                                                for(int i = 0; i<length; i++){

                                                    if (responseList.get(i).getTime() != null) {

                                                        if(responseList.get(i).getTime().getTime() + 20000 < System.currentTimeMillis()){

                                                            LogUtil.i(TAG,"剔除超时语音消息:"+responseList.get(i).getPacketID());
                                                            responseList.remove(i);
                                                            --length;
                                                        }
                                                    }
                                                }

                                                //    Thread.sleep(1000);

                                            }else{

                                                if(timerVoice != null){
                                                    timerVoice.cancel();
                                                }

                                                timerVoice = null;
                                                LogUtil.i(TAG,"没有新语音数据过来。释放计时Task");

                                            }

                                        } catch (Exception e) {
                                            ExceptionUtil.handleException(e);
                                        }


                                    }
                                };

                                taskVoice.schedule(timerVoice,1000,1000);

                            }

                        }*/

                    }


                    filterList.add(filter);

                    //写入本地数据库
                    setDatabase(context,filter,response);


                } catch (Exception e) {
                    e.printStackTrace();
                }



                if(timer == null){

                    timer = new TimerTask() {
                        @Override
                        public void run() {

                            try {

                                if (filterList.size() > 0) {

                                //    LogUtil.i(TAG,"剔除超时消息1:"+filterList.size());

                                    Iterator it= filterList.iterator();

                                    while(it.hasNext()) {

                                        Filter filter = (Filter) it.next();

                                        if(filter.getTime() != null && filter.getTime() + 30000 < System.currentTimeMillis()) {
                                            it.remove();
                                        }
                                    }

                                }else{

                                    if(timer != null){
                                        timer.cancel();
                                    }

                                    timer = null;

                                    LogUtil.i(TAG,"没有新数据过来。释放计时Task");

                                }



                            } catch (Exception e) {
                                ExceptionUtil.handleException(e);
                            }

                        }
                    };

                    task.schedule(timer,1000,1000);

                }

            }else{
                LogUtil.i(TAG,"过滤器中含有此数据");
            }
        } catch (Exception e) {
            ExceptionUtil.handleException(e);
        }
    }

    public static synchronized void setGroupDBs(Context context,Filter filter) {


        LogUtil.i(TAG,"setGroupDBs:"+filter.toString());


        if(filter.getType() == MsgResponse.Join || filter.getType() == MsgResponse.Leave || filter.getType() == MsgResponse.Into){

        //    File[] files = null;

            try {

                String path = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath()+"/data/"+filter.getGroupName()+"/sms_user/";


                if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {

                    File filePath = new File(path);

                    if (!filePath.exists()) {

                        filePath.mkdirs();
                    }

                    /*if(filePath.exists()){

                        files = filePath.listFiles();

                    }*/


                    File file = new File(path, String.valueOf(filter.getUserId()));

                    if(filter.getType() == MsgResponse.Join || filter.getType() == MsgResponse.Into){

                        if(!file.exists()){

                        //    MyApplicatoin.joins.add(filter.getUserId());

                            file.mkdir();

                        }/*else{

                           *//*boolean mk = false;

                            for (File file2 : files) {
                                if(file2.getName().equals(file.getName())){
                                    mk = true;
                                }

                            }*//*

                            if (!file.exists()){

                                file.mkdirs();

                            }

                        }*/

                    }else if(filter.getType() == MsgResponse.Leave){

                        if(file.exists()){

                            file.delete();

                        }/*else{

                            *//*boolean mk = false;

                            for (File file2 : files) {
                                if(file2.getName().equals(file.getName())){
                                    mk = true;
                                }

                            }*//*

                            if (file.exists()){

                                file.delete();

                            }

                        }*/

                    }


                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


    /**
     *
     * 写入database本地数据库
     *
     * */

    private static void setDatabase(Context context, Filter filter, final MsgResponse response) {

    //    LogUtil.i(GlobalConsts.TAG,"MsgResponse:"+response.toString());

        try {

            DAOMesg mesg = new DAOMesg();
            mesg.setMgid(response.getPacketID());
            mesg.setFrom_id(Long.parseLong(response.getFormName()));
            mesg.setUser_id(response.getUserId());
            mesg.setMesg_type(response.getType());
            if(response.getType() == MsgResponse.Voice){

                mesg.setContent_length(Base64.decode(response.getContent(), Base64.DEFAULT).length);
                //Log.d("fdfdfdd","addMessageLieret:"+ "                  555很骄傲             " +Base64.decode(response.getContent(), Base64.DEFAULT).length+"            777777777");
                mesg.setContent(response.getFireName());

                LogUtil.i(GlobalConsts.TAG,"语音数据名字："+response.getFireName());

            }else{

                mesg.setContent(response.getContent());
                mesg.setContent_length(0);
            }

            mesg.setCreate_time(response.getTime());

            MessageFilter.mes_request.respone(mesg);


            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        DAOGroupDao groupDao = DAOService.get(context).getsession().getDAOGroupDao();

                        List<DAOGroup> groupList = groupDao.loadAll();

                        DAOGroup group = null;

                        for(DAOGroup daoGroup : groupList){

                            if(response.getFormName().equals(String.valueOf(daoGroup.getGroupId()))){

                                group = daoGroup;

                            }

                        }

                        if(group == null){


                            if (groupList != null) {
                                for(DAOGroup groupa : groupList){
                                    groupa.setIs_avtive(false);
                                    groupDao.update(groupa);
                                }
                            }

                            DAOGroup groups = new DAOGroup();

                            groups.setGroupId(Long.parseLong(response.getFormName()));


                            if(MyApplicatoin.interphone != null){

                                if(MyApplicatoin.interphone.getImCode() != null){

                                    if(MyApplicatoin.interphone.getImCode().getHite() != null){

                                        groups.setName(MyApplicatoin.interphone.getImCode().getHite());

                                    }else{

                                        groups.setName(response.getFormName());

                                    }

                                }else{

                                    groups.setName(response.getFormName());

                                }
                            }else{

                                groups.setName(response.getFormName());

                            }

                            groups.setRemote_id(response.getUserId());
                            groups.setIs_avtive(true);
                            groupDao.insert(groups);

                            LogUtil.i(GlobalConsts.TAG,"数据库插入聊天室成功");

                            group = groups;

                        }

                        if (group != null) {

                            DAOMesgDao mesgDao = DAOService.get(context).getsession().getDAOMesgDao();

                            DAOMesg mesg = new DAOMesg();
                            mesg.setMgid(response.getPacketID());
                            mesg.setFrom_id(group.getGroupId());
                            LogUtil.i(GlobalConsts.TAG,"聊天室id："+group.getGroupId());
                            mesg.setUser_id(response.getUserId());
                            mesg.setMesg_type(response.getType());
                            if(response.getType() == MsgResponse.Voice){

                                mesg.setContent(response.getFireName());

                                mesg.setContent_length(Base64.decode(response.getContent(), Base64.DEFAULT).length);


                                LogUtil.i(GlobalConsts.TAG,"语音数据名字："+response.getFireName());

                            }else{

                                mesg.setContent_length(0);

                                mesg.setContent(response.getContent());
                            }
                            mesg.setCreate_time(response.getTime());

                            mesgDao.insert(mesg);

                            LogUtil.i(GlobalConsts.TAG,"数据库插入聊天记录成功");

                        }else{
                            LogUtil.i(GlobalConsts.TAG,"查询数据库聊天室为空，请检查。");
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }).start();


        } catch (NumberFormatException e) {
            ExceptionUtil.handleException(e);
        }

    }

    public  static void addMessageLieret(Context context, Handler mhandler, Filter filter, MsgResponse response, Message_request mes_request){

        MessageFilter.mes_request = mes_request;

        if(MessageFilter.mes_request != null){
            MessageFilter.messagefrlter(context,mhandler,filter,response);
        }
    //    LogUtil.i(GlobalConsts.TAG,"mes_request"+System.currentTimeMillis());
    }

}
