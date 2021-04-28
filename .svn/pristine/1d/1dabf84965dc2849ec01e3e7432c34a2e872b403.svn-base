package com.sms.app.framework.communication.localayer.cmd;

/**
 * Created by Administrator on 2017/3/20.
 */

public class Command {
    private byte opcode;
    private byte objcode;
    private byte prica;
    private Object value;


    public Command(byte opcode, byte objcode, byte prica, Object value) {
        this.opcode = opcode;
        this.objcode = objcode;
        this.prica = prica;
        this.value = value;
    }

    public Command() {

    }

    public void setOpcode(byte opcode) {
        this.opcode = opcode;
    }

    public void setObjcode(byte objcode) {
        this.objcode = objcode;
    }

    public void setPrica(byte prica) {
        this.prica = prica;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public byte getOpcode() {
        return opcode;
    }

    public byte getObjcode() {
        return objcode;
    }

    public byte getPrica() {
        return prica;
    }

    public Object getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "Command{" +
                "opcode=" + opcode +
                ", objcode=" + objcode +
                ", prica=" + prica +
                ", value='" + value + '\'' +
                '}';
    }
}
