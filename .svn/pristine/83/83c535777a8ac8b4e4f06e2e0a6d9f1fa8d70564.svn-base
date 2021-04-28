package com.sms.app.interphone.util.openutil;


import com.sms.app.framework.communication.localayer.MsgResponse;
import com.sms.app.interphone.services.OpenfireService;

import org.jivesoftware.smack.packet.Packet;

import java.util.List;

/**
 * Created by Administrator on 2016/7/15.
 * <p>
 * 回调函数
 */
public interface op_intercface {


    //Context对象
    void on_context(OpenfireService le_service);

    //连接状态
    void on_state(final boolean connection);

    //登录状态
    void on_login(byte type);

    //注册状态
    void on_Register(byte type);

    //创建聊天室状态
    void on_newfrom(byte type);

    //加入聊天室状态
    void on_joinfrom(byte type);

    //删除聊天室状态
    void on_destroyfrom(byte type);

    //收到信息
    void on_message(MsgResponse msgResponse);

    //收到信息
    void on_ping(byte code);


    //收到信息
    void on_Apply(MsgResponse msgResponse);

}
