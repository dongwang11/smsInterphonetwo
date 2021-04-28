package com.sms.app.framework.communication;

import android.content.Context;

import com.sms.app.framework.communication.localayer.Interphone_driver;
import com.sms.app.framework.communication.localayer.Interphone_notify_listenner;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.communication.localayer.open_notify_listenner;
import com.sms.app.framework.communication.localayer.open_res;
import com.sms.app.framework.respon;
import com.sms.app.framework.sms_request;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.framework.trans.bean.Interphone;
import com.sms.app.interphone.util.openutil.Openfrom;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Administrator on 2017/3/14.
 */

public class open {

    static String TAG="smsfw_open";
    static public open local_interface = null;
    private Context context=null;

    public  ConcurrentHashMap<String,attr_listenner> getListenners() {
        return listenners;
    }

    private ConcurrentHashMap<String,attr_listenner> listenners =  new ConcurrentHashMap();



    public open()
    {
        super();
    }
    public open(Context cnt)
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
        if(! (obj.getObj() instanceof Openfrom))
        {//传进来的参数不是对讲机对象
            return null;
        }
        Openfrom openfrom = (Openfrom)(obj.getObj());


        if(openfrom != null)
        {
            return open_res.get_driver(this.context).setOpenfire(obj);

        }

        return rsp;


    }
    static public open get_local(Context cnt, open_notify_listenner local_listner)
    {
        if(open.local_interface==null)
        {
            open.local_interface = new open(cnt);
        }

        open_res.add_notify_listenner(local_listner);

        return open.local_interface;
    }
}
