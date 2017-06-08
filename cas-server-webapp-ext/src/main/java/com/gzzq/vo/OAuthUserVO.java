package com.gzzq.vo;

import java.io.Serializable;

public class OAuthUserVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5627349049519169640L;

	// 授权应用名称，如微信、QQ等
	private String oauthName;

	// 第三方openid
	private String openid;

	// 昵称
	private String nickname;

	// 微信的统一ID
	private String unionid;

	// 头像
	private String headimgurl;

	public String getOauthName() {
		return oauthName;
	}

	public void setOauthName(String oauthName) {
		this.oauthName = oauthName;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

}
