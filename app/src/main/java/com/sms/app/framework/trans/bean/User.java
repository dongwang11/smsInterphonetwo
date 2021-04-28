package com.sms.app.framework.trans.bean;

import java.util.Date;


/**
 * Created by Administrator on 2017/3/28.
 */

public class User {

    private Long id;
    private String avatar_url;
    private Long version;
    private String name;
    private Date register_time;
    private Date  last_login_time;
	private Byte  sex;
    private String passkey;
    private String passwd;
    private String  number;
    private String  e_mail;
    private Im_account  im;
    private Long dev_type_bmp;
    

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getAvatar_url() {
		return avatar_url;
	}


	public void setAvatar_url(String avatar_url) {
		this.avatar_url = avatar_url;
	}


	public Long getVersion() {
		return version;
	}


	public void setVersion(Long version) {
		this.version = version;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Date getRegister_time() {
		return register_time;
	}


	public void setRegister_time(Date register_time) {
		this.register_time = register_time;
	}


	public Date getLast_login_time() {
		return last_login_time;
	}


	public void setLast_login_time(Date last_login_time) {
		this.last_login_time = last_login_time;
	}


	public Byte getSex() {
		return sex;
	}


	public void setSex(Byte sex) {
		this.sex = sex;
	}


	public String getPasskey() {
		return passkey;
	}


	public void setPasskey(String passkey) {
		this.passkey = passkey;
	}


	public String getPasswd() {
		return passwd;
	}


	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}


	public String getNumber() {
		return number;
	}


	public void setNumber(String number) {
		this.number = number;
	}


	public String getE_mail() {
		return e_mail;
	}


	public void setE_mail(String e_mail) {
		this.e_mail = e_mail;
	}


	public Im_account getIm() {
		return im;
	}


	public void setIm(Im_account im) {
		this.im = im;
	}


	public Long getDev_type_bmp() {
		return dev_type_bmp;
	}


	public void setDev_type_bmp(Long dev_type_bmp) {
		this.dev_type_bmp = dev_type_bmp;
	}


	@Override
	public String toString() {
		return "User [id=" + id + ", avatar_url=" + avatar_url + ", version=" + version + ", name=" + name
				+ ", register_time=" + register_time + ", last_login_time=" + last_login_time + ", sex=" + sex
				+ ", passkey=" + passkey + ", passwd=" + passwd + ", number=" + number + ", e_mail=" + e_mail + ", im="
				+ im + ", dev_type_bmp=" + dev_type_bmp + "]";
	}
    

}
