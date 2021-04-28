package com.sms.app.framework.communication.localayer;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

/**
 * Created by Administrator on 2017/5/9.
 *
 * BLE Chat Message
 *
 */

public class MsgResponse {

    //公网在线类型
    public static final byte Join = 0;
    //公网离线类型
    public static final byte Exit = 1;
    //文本类型
    public static final byte Text = 2;
    //图片类型
    public static final byte Img = 3;
    //语音类型
    public static final byte Voice = 4;
    //网络PTT类型
    public static final byte Ptt = 5;
    //位置类型
    public static final byte Aprs = 6;
    //退出聊天室类型
    public static final byte Leave = 7;
    //申请加入类型
    public static final byte Apply = 10;
    //心跳类型
    public static final byte Result = 11;
    //加入聊天室类型
    public static final byte Into = 12;

    private long packetID;
    private String formName;
    private byte type;
    private long userId;
    private String content;
    private Date time;
    private String fireName;
    private byte[] voice;
    private byte voice_code;
    private String userName;

    public long getPacketID() {
        return packetID;
    }

    public void setPacketID(long packetID) {
        this.packetID = packetID;
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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public byte[] getVoice() {
        return voice;
    }

    public void setVoice(byte[] voice) {
        this.voice = voice;
    }

    public byte getVoice_code() {
        return voice_code;
    }

    public void setVoice_code(byte voice_code) {
        this.voice_code = voice_code;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    @Override
    public String toString() {
        return "MsgResponse{" +
                "packetID=" + packetID +
                ", formName='" + formName + '\'' +
                ", type=" + type +
                ", userId=" + userId +
                ", userName=" + userName +
                ", content='" + content + '\'' +
                ", time=" + time +
                ", fireName='" + fireName + '\'' +
                ", voice=" + Arrays.toString(voice) +
                ", voice_code=" + voice_code +
                '}';
    }
}
