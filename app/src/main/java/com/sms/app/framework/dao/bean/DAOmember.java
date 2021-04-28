package com.sms.app.framework.dao.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.Date;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Administrator on 2017/7/26.
 */
@Entity
public class DAOmember {

    @Id
    private Long id;
    private long user_id;  //用户ID
    private String avatar_url;//头像图片的URL
    private String user_name;
    private String group_name;

    private byte  sex;//0代表未知，1代表男，2代表女
    private long version;

    @Generated(hash = 1769109134)
    public DAOmember(Long id, long user_id, String avatar_url, String user_name,
            String group_name, byte sex, long version) {
        this.id = id;
        this.user_id = user_id;
        this.avatar_url = avatar_url;
        this.user_name = user_name;
        this.group_name = group_name;
        this.sex = sex;
        this.version = version;
    }
    @Generated(hash = 1310481187)
    public DAOmember() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public long getUser_id() {
        return this.user_id;
    }
    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
    public String getAvatar_url() {
        return this.avatar_url;
    }
    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
    public String getUser_name() {
        return this.user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public long getVersion() {
        return this.version;
    }
    public void setVersion(long version) {
        this.version = version;
    }


    @Override
    public String toString() {
        return "DAOmember{" +
                "id=" + id +
                ", user_id=" + user_id +
                ", avatar_url='" + avatar_url + '\'' +
                ", user_name='" + user_name + '\'' +
                ", version=" + version +
                '}';
    }
    public byte getSex() {
        return this.sex;
    }
    public void setSex(byte sex) {
        this.sex = sex;
    }
    public String getGroup_name() {
        return this.group_name;
    }
    public void setGroup_name(String group_name) {
        this.group_name = group_name;
    }
}
