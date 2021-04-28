package com.sms.app.framework.communication.localayer;

/**
 * Created by Administrator on 2017/5/10.
 */

public abstract class Interphone_notify_listenner {
    static public final int  RESULT=13;
    static public final int  NOYIFY=12;

    static public final int  STATE=15;
    static public final int  ATTR=16;
    static public final int  MESG=17;
    static public final int  SCAN=18;









    abstract public void onNotify(int notify_type , Object object);

}
