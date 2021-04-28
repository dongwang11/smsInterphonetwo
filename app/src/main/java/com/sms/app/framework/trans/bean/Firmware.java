package com.sms.app.framework.trans.bean;

public class Firmware {
	private Long id;
	private Long hardware_version;
	private Long software_version;
	private Long   crc;
	private byte[]  data;
	
	
	public Firmware() {
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCrc() {
		return crc;
	}

	public void setCrc(Long crc) {
		this.crc = crc;
	}
  
	public Long getHardware_version() {
		return hardware_version;
	}

	public void setHardware_version(Long hardware_version) {
		this.hardware_version = hardware_version;
	}

	public Long getSoftware_version() {
		return software_version;
	}

	public void setSoftware_version(Long software_version) {
		this.software_version = software_version;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
	
}
