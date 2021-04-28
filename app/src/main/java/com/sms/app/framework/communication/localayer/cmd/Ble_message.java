package com.sms.app.framework.communication.localayer.cmd;

/**
 * Created by Administrator on 2017/3/28.
 *
 * BLE Chat Message
 *
 *
 */

public class Ble_message {

    public static final byte text = 1;
    public static final byte img = 2;
    public static final byte voice = 3;
    public static final byte aprs = 4;
    public static final byte over = 5;





    private Long id;
    private Long userId;
    private String message;
    private String fireName;
    private Byte type;
    private Long Time;
    private Long fromId;
    private Double latitude;
    private Double longitude;




    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Long getTime() {
        return Time;
    }

    public void setTime(Long time) {
        Time = time;
    }

    public Long getFromId() {
        return fromId;
    }

    public void setFromId(Long fromId) {
        this.fromId = fromId;
    }


    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getFireName() {
        return fireName;
    }

    public void setFireName(String fireName) {
        this.fireName = fireName;
    }


    @Override
    public String toString() {
        return "Ble_message{" +
                "id=" + id +
                ", userId=" + userId +
                ", message='" + message + '\'' +
                ", fireName='" + fireName + '\'' +
                ", type=" + type +
                ", Time=" + Time +
                ", fromId=" + fromId +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
