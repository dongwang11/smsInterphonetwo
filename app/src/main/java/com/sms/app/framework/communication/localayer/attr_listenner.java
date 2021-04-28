package com.sms.app.framework.communication.localayer;

import android.content.Context;

import com.sms.app.framework.trans.bean.Interphone;

/**
 * Created by Administrator on 2017/5/11.
 */

public abstract  class  attr_listenner
{
    private Context context = null;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    abstract public  void onAttributeChang(int code,Object object);


}