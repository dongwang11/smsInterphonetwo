package com.sms.app.interphone.util.maputil;

import android.location.Location;

import com.sms.app.interphone.services.MonitorService;

/**
 * Created by Administrator on 2016/7/15.
 * <p>
 * 回调函数
 */
public interface Map_intercface {
    //位置信息
    void on_Location(Location location_, double length, MonitorService map_service);

}
