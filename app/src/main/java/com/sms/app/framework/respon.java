package com.sms.app.framework;

/**
 * Created by Administrator on 2017/3/15.
 */


public class respon {

    private int opcode;//// sms_request的opcode
    private Object obj;

    public Object getObj() {
        return obj;
    }

    public int getOpcode() {
        return opcode;
    }

    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public respon(int opcode, Object obj) {
        this.opcode = opcode;
        this.obj = obj;
    }

    public respon() {
        this.obj = null;
        this.opcode = sms_request.DEFAULT;
    }
}