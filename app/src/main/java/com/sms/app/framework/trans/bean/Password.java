package com.sms.app.framework.trans.bean;

public class Password {
	
	
	public static final int SUCESS=1;
	public static final int OLD_PASSWD_ERRO=2;

	private String old_passd;
	private String passwd;
	private int state;
	public String getOld_passd() {
		return old_passd;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setOld_passd(String old_passd) {
		this.old_passd = old_passd;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	@Override
	public String toString() {
		return "Password{" +
				"old_passd='" + old_passd + '\'' +
				", passwd='" + passwd + '\'' +
				", state=" + state +
				'}';
	}
}
