package com.sms.app.framework.communication.localayer;


import android.content.Context;
import android.location.Location;

import com.sms.app.framework.communication.localayer.cmd.Command;
import com.sms.app.framework.communication.localayer.cmd.Command_biz;
import com.sms.app.framework.communication.localayer.cmd.Objcode;
import com.sms.app.framework.communication.localayer.cmd.Opcode;
import com.sms.app.framework.respon;
import com.sms.app.framework.trans.Post_obj;
import com.sms.app.interphone.services.Open_biz;
import com.sms.app.interphone.util.openutil.Openfrom;

/**
 * Created by Administrator on 2017/3/14.
 */

//本地保存的对讲机设备信息
public class open_res {

    public static open_res itf_driver = null;
    public static open_notify_listenner  notify_listenner = null;
    private Context context=null;


    static public open_res get_driver(Context cnt)
    {
        if(open_res.itf_driver==null)
        {
            open_res.itf_driver = new open_res(cnt);
        }
        return open_res.itf_driver;
    }




    public open_res(Context context) {
        this.context = context;
    }

    public open_res() {

    }

    public static void add_notify_listenner(open_notify_listenner listenner)
    {
        open_res.notify_listenner = listenner;
        Open_biz.add_notify_listenner(listenner);
    }


    public respon setOpenfire(Post_obj obj) {

        respon rsp = null;
        int opcode = obj.getOpcode();
        if(! (obj.getObj() instanceof Openfrom))
        {//传进来的参数不是对讲机对象
            return null;
        }
        Openfrom openfrom = (Openfrom)(obj.getObj());

        return Open_biz.send_open(this.context,opcode,openfrom);
    }

}
