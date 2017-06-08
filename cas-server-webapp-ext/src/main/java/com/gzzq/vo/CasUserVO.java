package com.gzzq.vo;

import java.io.Serializable;
import java.util.Map;

public class CasUserVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6098896741380684736L;
	// 唯一标识
	private String uuid;
	// 登录使用的账号
	private String account;
	private String password;
	private String tgc;
	// 对应cas服务配置的白名单
	private String service;

	public CasUserVO() {
	}

	public CasUserVO(Map<String, Object> map) {
		setUuid((String) map.get("uuid"));
		setAccount((String) map.get("account"));
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getTgc() {
		return tgc;
	}

	public void setTgc(String tgc) {
		this.tgc = tgc;
	}

	public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

}
