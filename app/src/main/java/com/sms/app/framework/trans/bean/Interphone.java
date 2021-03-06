package com.sms.app.framework.trans.bean;

import android.location.Location;

import com.sms.app.framework.communication.localayer.ImCode;
import com.sms.app.framework.communication.localayer.MsgResponse;

import java.util.Date;

/**
 *
 * 设备对象
 *
 * */
public class Interphone {

	private String device_key;

	private Long id;
	private Long hard_version;  //硬件版本
	private Long soft_version;  //软件版本
	private Long userId;
	private Date factroy_time;
	private Date active_time;
	private String active_site;
	private String product_model;        //
	private String name;        //名字
	private Integer frequne;        //频率
	private Byte mode;          //数字 or 模拟 模式
	private String power;       //电量
	private Byte bw;            //带宽模式
	private Byte rf;            //RF开关
	private Byte sq;            //模拟对讲机静噪等级
	private Byte vox;           //vox
	private Byte tx_power;           //发射功率
	private Byte tx_hite;           //发送提示音
	private Byte stop_hite;           //尾音消除开关
	private Byte disconnec_hite;           //断开连接提示
	private Byte isplay;           //实时播放

	private Byte plays;           //播放源
	private Byte findDevice;
	private Byte netDisconnect;	//网络断开提醒

	private Integer txcode;        //亚音频
	private Integer rxcode;        //亚音频
	private Byte connect;          //0 = 停止搜索   1 = 开始搜索   2 = 断开连接
	private String address;        //MAC地址
	private Byte bluetooth;        //蓝牙耳机
	private String firmware;        //升级文件
	private MsgResponse msgResponse;
	private Location location;  // 对讲机的位置信息
	private short volt;       //电池电压
	private Byte factory_reset; // 恢复出厂设置
	private Byte state = 0;   // 对讲机可接收状态
	private Byte mState = 0;   // 对讲机可接收状态
	private ImCode imCode;
	private String device_reset; // 设备重置



	public Interphone() {
		// TODO Auto-generated constructor stub
	}


	public Byte getPlays() {
		return plays;
	}

	public void setPlays(Byte plays) {
		this.plays = plays;
	}

	public Byte getStop_hite() {
		return stop_hite;
	}

	public void setStop_hite(Byte stop_hite) {
		this.stop_hite = stop_hite;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getDevice_key() {
		return device_key;
	}

	public void setDevice_key(String device_key) {
		this.device_key = device_key;
	}

	public Long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Long getHard_version() {
		return hard_version;
	}

	public void setHard_version(long hard_version) {
		this.hard_version = hard_version;
	}

	public Long getSoft_version() {
		return soft_version;
	}

	public void setSoft_version(long soft_version) {
		this.soft_version = soft_version;
	}


	public Date getFactroy_time() {
		return factroy_time;
	}

	public void setFactroy_time(Date factroy_time) {
		this.factroy_time = factroy_time;
	}

	public Date getActive_time() {
		return active_time;
	}

	public void setActive_time(Date active_time) {
		this.active_time = active_time;
	}

	public String getActive_site() {
		return active_site;
	}

	public void setActive_site(String active_site) {
		this.active_site = active_site;
	}



	public String getProduct_model() {
		return product_model;
	}

	public void setProduct_model(String product_model) {
		this.product_model = product_model;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getFrequne() {
		return frequne;
	}

	public void setFrequne(Integer frequne) {
		this.frequne = frequne;
	}

	public Byte getMode() {
		return mode;
	}

	public void setMode(Byte mode) {
		this.mode = mode;
	}

	public String getPower() {
		return power;
	}

	public void setPower(String power) {
		this.power = power;
	}

	public Byte getBw() {
		return bw;
	}

	public void setBw(Byte bw) {
		this.bw = bw;
	}

	public Byte getSq() {
		return sq;
	}

	public void setSq(Byte sq) {
		this.sq = sq;
	}

	public Byte getVox() {
		return vox;
	}

	public void setVox(Byte vox) {
		this.vox = vox;
	}

	public Integer getTxcode() {
		return txcode;
	}

	public void setTxcode(Integer txcode) {
		this.txcode = txcode;
	}

	public Integer getRxcode() {
		return rxcode;
	}

	public void setRxcode(Integer rxcode) {
		this.rxcode = rxcode;
	}

	public Byte getConnect() {
		return connect;
	}

	public void setConnect(Byte connect) {
		this.connect = connect;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Byte getBluetooth() {
		return bluetooth;
	}

	public void setBluetooth(Byte bluetooth) {
		this.bluetooth = bluetooth;
	}

	public String getFirmware() {
		return firmware;
	}

	public void setFirmware(String firmware) {
		this.firmware = firmware;
	}

	public MsgResponse getMsgResponse() {
		return msgResponse;
	}

	public void setMsgResponse(MsgResponse msgResponse) {
		this.msgResponse = msgResponse;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Byte getState() {
		return state;
	}

	public void setState(Byte state) {
		this.state = state;
	}

	public ImCode getImCode() {
		return imCode;
	}

	public void setImCode(ImCode imCode) {
		this.imCode = imCode;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setHard_version(Long hard_version) {
		this.hard_version = hard_version;
	}

	public void setSoft_version(Long soft_version) {
		this.soft_version = soft_version;
	}

	public Byte getRf() {
		return rf;
	}

	public void setRf(Byte rf) {
		this.rf = rf;
	}

	public Byte getTx_power() {
		return tx_power;
	}

	public void setTx_power(Byte tx_power) {
		this.tx_power = tx_power;
	}

	public short getVolt() {
		return volt;
	}

	public void setVolt(short volt) {
		this.volt = volt;
	}

	public Byte getFactory_reset() {
		return factory_reset;
	}

	public void setFactory_reset(Byte factory_reset) {
		this.factory_reset = factory_reset;
	}


	public String getDevice_reset() {
		return device_reset;
	}

	public void setDevice_reset(String device_reset) {
		this.device_reset = device_reset;
	}

	public Byte getmState() {
		return mState;
	}

	public void setmState(Byte mState) {
		this.mState = mState;
	}

	public Byte getTx_hite() {
		return tx_hite;
	}

	public void setTx_hite(Byte tx_hite) {
		this.tx_hite = tx_hite;
	}

	public Byte getDisconnec_hite() {
		return disconnec_hite;
	}

	public void setDisconnec_hite(Byte disconnec_hite) {
		this.disconnec_hite = disconnec_hite;
	}

	public Byte getFindDevice() {
		return findDevice;
	}

	public void setFindDevice(Byte findDevice) {
		this.findDevice = findDevice;
	}

	public Byte getNetDisconnect() {
		return netDisconnect;
	}

	public void setNetDisconnect(Byte netDisconnect) {
		this.netDisconnect = netDisconnect;
	}

	public Byte getIsplay() {
		return isplay;
	}

	public void setIsplay(Byte isplay) {
		this.isplay = isplay;
	}

	@Override
	public String toString() {
		return "Interphone{" +
				"device_key='" + device_key + '\'' +
				", id=" + id +
				", hard_version=" + hard_version +
				", soft_version=" + soft_version +
				", userId=" + userId +
				", factroy_time=" + factroy_time +
				", active_time=" + active_time +
				", active_site='" + active_site + '\'' +
				", product_model='" + product_model + '\'' +
				", name='" + name + '\'' +
				", frequne=" + frequne +
				", mode=" + mode +
				", power='" + power + '\'' +
				", bw=" + bw +
				", rf=" + rf +
				", sq=" + sq +
				", vox=" + vox +
				", tx_power=" + tx_power +
				", tx_hite=" + tx_hite +
				", stop_hite=" + stop_hite +
				", disconnec_hite=" + disconnec_hite +
				", isplay=" + isplay +
				", plays=" + plays +
				", findDevice=" + findDevice +
				", netDisconnect=" + netDisconnect +
				", txcode=" + txcode +
				", rxcode=" + rxcode +
				", connect=" + connect +
				", address='" + address + '\'' +
				", bluetooth=" + bluetooth +
				", firmware='" + firmware + '\'' +
				", msgResponse=" + msgResponse +
				", location=" + location +
				", volt=" + volt +
				", factory_reset=" + factory_reset +
				", device_reset=" + device_reset +
				", state=" + state +
				", mState=" + mState +
				", imCode=" + imCode +
				'}';
	}
}
