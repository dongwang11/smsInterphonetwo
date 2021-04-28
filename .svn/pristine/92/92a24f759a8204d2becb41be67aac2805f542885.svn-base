package com.sms.app.framework.trans;

import android.content.Context;

import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.framework.RequestCB;
import com.sms.app.framework.communication.localayer.attr_listenner;
import com.sms.app.framework.router.sms_fw_router;
import com.sms.app.framework.sms_request;

/**
 * Created by Administrator on 2017/5/10.
 */

public class sms_fw_api {

    public static final byte RESULT = 0;

    public static final byte START = 1;

    public static final byte ATTR = 2;

    public static final byte MESG = 3;

    public static void do_request(Context context, int opcode, Object obj, RequestCB reqcb) {

        sms_request.dorequest(context, opcode, obj, reqcb);

    }


    public static void add_open_listenner(Context context,attr_listenner listenner)//openfire属性发生改变，会通知到各个监听得界面或者进程等
    {
        listenner.setContext(context);
        sms_fw_router.get_router(context).add_open_listenner(listenner);
    }

    public static void add_attr_listenner(Context context,attr_listenner listenner)//对讲机的属性发生改变，会通知到各个监听得界面或者进程等
    {
        listenner.setContext(context);
        sms_fw_router.get_router(context).add_attr_listenner(listenner);
    }
}
