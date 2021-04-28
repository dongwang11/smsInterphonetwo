package com.sms.app.interphone.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Administrator on 2017/7/26.
 *
 * 聊天记录自定义消息体
 *
 */

public class Mesage_entity implements Parcelable{

    private Long id;
    private long from_id;  //房间ID
    private long user_id;  //用户ID
    private int content_length;

    private boolean isplay;
    private byte is_ok;

    private long create_time;
    private byte mesg_type;
    private String content;//文本或者语音图片的文件路劲
    private long mgid;  //包ID
    private String user_neme;
    private String avatar_url;
    private byte  sex;//0代表未知，1代表男，2代表女


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getFrom_id() {
        return from_id;
    }

    public void setFrom_id(long from_id) {
        this.from_id = from_id;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public int getContent_length() {
        return content_length;
    }

    public void setContent_length(int content_length) {
        this.content_length = content_length;
    }

    public boolean isIsplay() {
        return isplay;
    }

    public void setIsplay(boolean isplay) {
        this.isplay = isplay;
    }

    public byte getIs_ok() {
        return is_ok;
    }

    public void setIs_ok(byte is_ok) {
        this.is_ok = is_ok;
    }

    public Long getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Long create_time) {
        this.create_time = create_time;
    }

    public byte getMesg_type() {
        return mesg_type;
    }

    public void setMesg_type(byte mesg_type) {
        this.mesg_type = mesg_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMgid() {
        return mgid;
    }

    public void setMgid(long mgid) {
        this.mgid = mgid;
    }

    public String getUser_neme() {
        return user_neme;
    }

    public void setUser_neme(String user_neme) {
        this.user_neme = user_neme;
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }

    public byte getSex() {
        return sex;
    }

    public void setSex(byte sex) {
        this.sex = sex;
    }


    @Override
    public String toString() {
        return "Mesage_entity{" +
                "id=" + id +
                ", from_id=" + from_id +
                ", user_id=" + user_id +
                ", content_length=" + content_length +
                ", isplay=" + isplay +
                ", is_ok=" + is_ok +
                ", create_time=" + create_time +
                ", mesg_type=" + mesg_type +
                ", content='" + content + '\'' +
                ", mgid=" + mgid +
                ", user_neme='" + user_neme + '\'' +
                ", avatar_url='" + avatar_url + '\'' +
                ", sex=" + sex +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }



    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(from_id);
        dest.writeLong(user_id);
        dest.writeInt(content_length);
        dest.writeByte((byte) (isplay ? 1 : 0));
        dest.writeByte(is_ok);
        dest.writeLong(create_time);
        dest.writeByte(mesg_type);
        dest.writeString(content);
        dest.writeLong(mgid);
        dest.writeString(user_neme);
        dest.writeString(avatar_url);
        dest.writeByte(sex);

    }

    public static final Creator<Mesage_entity> CREATOR = new Creator<Mesage_entity>() {
        @Override
        public Mesage_entity createFromParcel(Parcel in) {

            Mesage_entity mesage_entity = new Mesage_entity();

            mesage_entity.from_id = in.readLong();
            mesage_entity.user_id = in.readLong();
            mesage_entity.content_length = in.readInt();
            mesage_entity.isplay = in.readByte() != 0;
            mesage_entity.is_ok = in.readByte();
            mesage_entity.create_time = in.readLong();
            mesage_entity.mesg_type = in.readByte();
            mesage_entity.content = in.readString();
            mesage_entity.mgid = in.readLong();
            mesage_entity.user_neme = in.readString();
            mesage_entity.avatar_url = in.readString();
            mesage_entity.sex = in.readByte();

            return mesage_entity;
        }

        @Override
        public Mesage_entity[] newArray(int size) {
            return new Mesage_entity[size];
        }
    };
}
