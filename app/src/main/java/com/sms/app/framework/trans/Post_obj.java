package com.sms.app.framework.trans;

public class Post_obj {

    private String class_str;
    private int opcode;
    private Object obj;
    public Post_obj() {
        // TODO Auto-generated constructor stub
    }
    public String getClass_str() {
        return class_str;
    }
    public void setClass_str(String class_str) {
        this.class_str = class_str;
    }
    public int getOpcode() {
        return opcode;
    }
    public void setOpcode(int opcode) {
        this.opcode = opcode;
    }
    public Object getObj() {
        return obj;
    }
    public void setObj(Object obj) {
        this.obj = obj;
    }
    public Post_obj(String class_str, int opcode, Object obj) {
        super();
        this.class_str = class_str;
        this.opcode = opcode;
        this.obj = obj;
    }

    @Override
    public String toString() {
        return "Post_obj{" +
                "class_str='" + class_str + '\'' +
                ", opcode=" + opcode +
                ", obj=" + obj +
                '}';
    }
}
