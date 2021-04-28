package com.sms.app.framework.router;

import android.content.Context;
import android.util.Log;

import com.sms.app.framework.*;
import com.sms.app.framework.communication.local;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.open_notify_listenner;
import com.sms.app.framework.communication.open;
import com.sms.app.framework.communication.remote;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.util.openutil.Openfrom;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/3/14.
 */

public class sms_fw_router
{
    public static sms_fw_router router= null;
    public static Context cnt = null;
    static private final String TAG="smsfw_sms_fw_router";
    public sms_fw_router(Context cnt) {
        this.cnt = cnt;
    }
    public static local mlocal;
    public static open mopen;

    public sms_fw_router() {
    }

    public void add_attr_listenner(attr_listenner listenner)
    {
        this.mlocal.add_attr_listenner(listenner);
    }
    public void add_open_listenner(attr_listenner listenner)
    {
        this.mopen.add_attr_listenner(listenner);
    }

    public respon dorequest( int opcode, Object obj )
    {//在router有一个比较特殊的java bean，就是对讲机的Interphone，如果判断是对讲机的操作我们就走本地无线接口
        Log.e(sms_fw_router.TAG,"dorequest");
        if(obj instanceof Interphone)
        {//是对讲机，则走本地无线通道
            Log.e(sms_fw_router.TAG,"Interphone:");
            Post_obj  post  = new Post_obj();
            post.setClass_str(obj.getClass().toString().substring(6));
            post.setObj(obj);
            post.setOpcode(opcode);

        //  LogUtil.i(GlobalConsts.TAG,"dorequest:"+post.toString());
            return mlocal.dorequest(post);

        } else if(obj instanceof Openfrom) {

            Log.e(sms_fw_router.TAG,"Openfrom:");

            //走openfire通道
            Post_obj  post  = new Post_obj();
            post.setClass_str(obj.getClass().toString().substring(6));
            post.setObj(obj);
            post.setOpcode(opcode);

            //  LogUtil.i(GlobalConsts.TAG,"dorequest:"+post.toString());
            return mopen.dorequest(post);


        }else{

            Log.e(sms_fw_router.TAG,"走服务器通道:");

            //走服务器通道
            Post_obj  post  = new Post_obj();
            post.setClass_str(obj.getClass().toString().substring(6));
            post.setObj(obj);
            post.setOpcode(opcode);
            return remote.get_remote(this.cnt).dorequest(post);

        }


    }

    public static  sms_fw_router  get_router(final Context cnt) {

        if(sms_fw_router.router == null) {
            sms_fw_router.router =  new sms_fw_router(cnt);
        }

        sms_fw_router.router.mlocal =  local.get_local(cnt, new Interphone_notify_listenner() {
            @Override
            public void onNotify(int notify_type, final Object object) {
                //对讲机主动发出的信息全部在这里

                ConcurrentHashMap<String,attr_listenner> lists =  sms_fw_router.mlocal.getListenners();

        //        LogUtil.i(GlobalConsts.TAG,"sms_fw_router：mlocal"+lists.size());

                for ( ConcurrentHashMap.Entry<String, attr_listenner> ltmp : lists.entrySet()) {

                    attr_listenner listenner = ltmp.getValue();

                    if(listenner!=null && listenner.getContext()!=null) {

                        listenner.onAttributeChang(notify_type,object);

                    } else {

                        lists.remove(ltmp.getKey());

                    }

                }
            }
        });

        sms_fw_router.router.mopen =  open.get_local(cnt, new open_notify_listenner() {
            @Override
            public void onNotify(int notify_type, final Object object) {
                //对讲机主动发出的信息全部在这里

                ConcurrentHashMap<String,attr_listenner> lists =  sms_fw_router.mopen.getListenners();

        //        LogUtil.i(GlobalConsts.TAG,"sms_fw_router：mopen"+lists.size());

                for ( ConcurrentHashMap.Entry<String, attr_listenner> ltmp : lists.entrySet()) {

                    attr_listenner listenner = ltmp.getValue();

                    if(listenner!=null && listenner.getContext()!=null) {

                        listenner.onAttributeChang(notify_type,object);

                    } else {

                        lists.remove(ltmp.getKey());

                    }

                }
            }
        });
        return sms_fw_router.router;
    }
}