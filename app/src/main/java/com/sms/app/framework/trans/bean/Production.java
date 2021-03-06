package com.sms.app.framework.trans.bean;

public class Production {


	private String modle;//产品型号
	private String hardware_version;//硬件版本


	private String user_gui_version;//用户手册版本
	private String user_gui_url;//用户手册地址
	private String qa_version;//常见问题版本
	private String qa_url;//常见问题地址
	private String firmware_version;//固件版本
	private String firmware_url;
	private Long   firmware_crc;
	private String firmware_description;
	private String state;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getModle() {
		return modle;
	}
	public void setModle(String modle) {
		this.modle = modle;
	}
	public String getHardware_version() {
		return hardware_version;
	}
	public void setHardware_version(String hardware_version) {
		this.hardware_version = hardware_version;
	}
	public String getFirmware_version() {
		return firmware_version;
	}
	public void setFirmware_version(String firmware_version) {
		this.firmware_version = firmware_version;
	}
	public String getUser_gui_version() {
		return user_gui_version;
	}
	public void setUser_gui_version(String user_gui_version) {
		this.user_gui_version = user_gui_version;
	}
	public String getUser_gui_url() {
		return user_gui_url;
	}
	public void setUser_gui_url(String user_gui_url) {
		this.user_gui_url = user_gui_url;
	}
	public String getQa_version() {
		return qa_version;
	}
	public void setQa_version(String qa_version) {
		this.qa_version = qa_version;
	}
	public String getQa_url() {
		return qa_url;
	}
	public void setQa_url(String qa_url) {
		this.qa_url = qa_url;
	}
	public String getFirmware_url() {
		return firmware_url;
	}
	public void setFirmware_url(String firmware_url) {
		this.firmware_url = firmware_url;
	}
	
	
	public Long getFirmware_crc() {
		return firmware_crc;
	}
	public void setFirmware_crc(Long firmware_crc) {
		this.firmware_crc = firmware_crc;
	}
	public String getFirmware_description() {
		return firmware_description;
	}
	public void setFirmware_description(String firmware_description) {
		this.firmware_description = firmware_description;
	}
	@Override
	public String toString() {
		return "Production [modle=" + modle + ", user_gui_version=" + user_gui_version + ", user_gui_url="
				+ user_gui_url + ", qa_version=" + qa_version + ", qa_url=" + qa_url + ", hardware_version="
				+ hardware_version + ", firmware_version=" + firmware_version + ", firmware_url=" + firmware_url
				+ ", firmware_crc=" + firmware_crc + ", firmware_description=" + firmware_description + "]";
	}
	
	
	

	
}
