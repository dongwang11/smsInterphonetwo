package com.sms.app.framework.trans.bean;

/**
 * Created by Administrator on 2017/4/17.
 */

public class Avatar {
    private Long user_id;
    private String url;
    private String  formate ;
    private byte[]  data;

 
    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getUser_id() {
		return user_id;
	}

	public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getFormate() {
        return formate;
    }

    public void setFormate(String formate) {
        this.formate = formate;
    }

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

 
}
