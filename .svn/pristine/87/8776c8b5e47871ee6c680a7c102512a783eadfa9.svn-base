package com.sms.app.framework;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.sms.app.framework.communication.localayer.bledriver.GlobalConsts;
import com.sms.app.framework.communication.localayer.bledriver.LogUtil;
import com.sms.app.framework.router.sms_fw_router;
import com.sms.app.interphone.ui.MyApplicatoin;

/**
 * Created by Administrator on 2017/3/14.
 */

public class sms_request extends AsyncTask<Object,Integer,respon> {
    public static final int DEFAULT = 0X0;
    public static final int GET = 0X1;
    public static final int SET = 0x2;
    public static final int LOGIN = 0x3;
    public static final int REGISTER = 0x4;
    public static final int RESET_PASSWD = 0x5;
    public static final int ERRO = 0x6;
    public static final String  tag = "sms_request";


    private int opcode  =0;
    private Context cnt = null;
    private Object obj = null;
    private RequestCB reqcb = null;


    protected respon doInBackground(Object... params) {
        Log.e(sms_request.tag,"doInBackground");

        respon rsp = sms_fw_router.get_router(this.cnt).dorequest(this.opcode,this.obj);

        return rsp;
    }

    protected void onPostExecute(respon rsp) {
        Log.e(sms_request.tag,"onPostExecute:"+rsp);
        super.onPostExecute(rsp);
        if(this.reqcb!=null)
        {
            if(rsp != null){
                this.reqcb.respone(rsp.getOpcode(),rsp.getObj());
            }else{
                respon respon = new respon();
                respon.setOpcode(10);
                respon.setObj(new Object());
                this.reqcb.respone(respon.getOpcode(),respon.getObj());
            }
        }
    }

    public sms_request() {
        super();
    }
    public sms_request(Context cnt, int opcode, Object obj , RequestCB reqcb) {
        this.opcode = opcode;
        this.obj = obj;
        this.reqcb = reqcb;
        this.cnt = cnt;
    }
    public static sms_request dorequest(Context context, int opcode, Object obj , RequestCB reqcb) {


        sms_request post = new sms_request(context,opcode,obj, reqcb);
        LogUtil.i(GlobalConsts.TAG,"sms_request");
        LogUtil.i(GlobalConsts.TAG,"context："+context);

        post.execute("hello");
        return  post;
    }
}
