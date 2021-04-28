package com.sms.app.framework.trans.bean;

import java.util.Date;



public class Point {
	private Long   id;
	private Double direction;
	private Double longitude;
	private Double latitude;
	private Double speex;
	private Double altitude;
	private Date   pcreate_time;
	
	
	public Long getId() {
		return id;
	}



	public void setId(Long id) {
		this.id = id;
	}


	public Double getDirection() {
		return direction;
	}



	public void setDirection(Double direction) {
		this.direction = direction;
	}



	public Double getLongitude() {
		return longitude;
	}



	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}



	public Double getLatitude() {
		return latitude;
	}



	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}



	public Double getSpeex() {
		return speex;
	}



	public void setSpeex(Double speex) {
		this.speex = speex;
	}



	public Date getPcreate_time() {
		return pcreate_time;
	}



	public void setPcreate_time(Date pcreate_time) {
		this.pcreate_time = pcreate_time;
	}

	




	public Point() {
		// TODO Auto-generated constructor stub
		this.speex = 1.0;
	}



	public Double getAltitude() {
		return altitude;
	}



	public void setAltitude(Double altitude) {
		this.altitude = altitude;
	}



	@Override
	public String toString() {
		return "Point [id=" + id + ", direction=" + direction + ", longitude=" + longitude + ", latitude=" + latitude
				+ ", speex=" + speex + ", altitude=" + altitude + ", pcreate_time=" + pcreate_time + "]";
	}

}
