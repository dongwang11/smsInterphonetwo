package com.sms.app.framework.communication.localayer;

/**
 * Created by Administrator on 2017/8/8.
 *
 * 对讲机通讯信息
 *
 */

public class ImCode {

    private Long id;
    private String name;
    private String uuid;
    private String hite;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getHite() {
        return hite;
    }

    public void setHite(String hite) {
        this.hite = hite;
    }

    @Override
    public String toString() {
        return "ImCode{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                ", hite='" + hite + '\'' +
                '}';
    }
}
