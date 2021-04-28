package com.sms.app.interphone.entity;


import java.util.Date;

public class Voicelist {

    private long packetID;
    private long userId;
    private String formName;
    private byte type;
    private Date time;
    private String fireName;
    private String content;

    private byte voice_code;


    public long getPacketID() {
        return packetID;
    }

    public void setPacketID(long packetID) {
        this.packetID = packetID;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getFireName() {
        return fireName;
    }

    public void setFireName(String fireName) {
        this.fireName = fireName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public byte getVoice_code() {
        return voice_code;
    }

    public void setVoice_code(byte voice_code) {
        this.voice_code = voice_code;
    }

    @Override
    public String toString() {
        return "Voicelist{" +
                "packetID=" + packetID +
                ", userId=" + userId +
                ", formName='" + formName + '\'' +
                ", type=" + type +
                ", time=" + time +
                ", fireName='" + fireName + '\'' +
                ", content='" + content + '\'' +
                ", voice_code=" + voice_code +
                '}';
    }
}
