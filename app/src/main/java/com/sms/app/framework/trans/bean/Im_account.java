package com.sms.app.framework.trans.bean;

/**
 * Created by Administrator on 2017/4/11.
 */

public class Im_account {
    private Long id;
    private Long user_id;
    private String im_account;
    private String im_passwd;
    private Long   group_id;
    private long   mesh_id;
    private Long version;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUser_id() {
		return user_id;
	}
	public void setUser_id(Long user_id) {
		this.user_id = user_id;
	}
	public String getIm_account() {
		return im_account;
	}
	public void setIm_account(String im_account) {
		this.im_account = im_account;
	}
	public String getIm_passwd() {
		return im_passwd;
	}
	public void setIm_passwd(String im_passwd) {
		this.im_passwd = im_passwd;
	}
	public Long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(Long group_id) {
		this.group_id = group_id;
	}
	public long getMesh_id() {
		return mesh_id;
	}
	public void setMesh_id(long mesh_id) {
		this.mesh_id = mesh_id;
	}
	public Long getVersion() {
		return version;
	}
	public void setVersion(long version) {
		this.version = version;
	}


	@Override
	public String toString() {
		return "Im_account{" +
				"id=" + id +
				", user_id=" + user_id +
				", im_account='" + im_account + '\'' +
				", im_passwd='" + im_passwd + '\'' +
				", group_id=" + group_id +
				", mesh_id=" + mesh_id +
				", version=" + version +
				'}';
	}
}
