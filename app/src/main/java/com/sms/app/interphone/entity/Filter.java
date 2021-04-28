package com.sms.app.interphone.entity;

/**
 * Created by Administrator on 2017/5/8.
 *
 * 过滤器中的消息体
 *
 */

public class Filter {

    public static final byte openfire = 1;

    public static final byte interphone = 2;

    public static final byte phone = 3;




    private long time = 0;
    private Long packetID;
    private Long userId;
    private Byte type;

    private Byte code;  //1.通过网络接收  2.通过对讲机接收

    private String groupName;



    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getPacketID() {
        return packetID;
    }

    public void setPacketID(Long packetID) {
        this.packetID = packetID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Byte getCode() {
        return code;
    }

    public void setCode(Byte code) {
        this.code = code;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    @Override
    public String toString() {
        return "Filter{" +
                "time=" + time +
                ", packetID=" + packetID +
                ", userId=" + userId +
                ", type=" + type +
                ", code=" + code +
                ", groupName='" + groupName + '\'' +
                '}';
    }
}
