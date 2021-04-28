package com.sms.app.framework.communication.localayer.cmd;

/**
 * Created by Administrator on 2017/8/14.
 */

public class Result {

    private byte[] objcode;

    private byte result;

    private byte code;



    public byte[] getObjcode() {
        return objcode;
    }

    public void setObjcode(byte[] objcode) {
        this.objcode = objcode;
    }

    public byte getResult() {
        return result;
    }

    public void setResult(byte result) {
        this.result = result;
    }

    public byte getCode() {
        return code;
    }

    public void setCode(byte code) {
        this.code = code;
    }
}
