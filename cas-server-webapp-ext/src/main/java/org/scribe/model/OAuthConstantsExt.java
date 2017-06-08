package org.scribe.model;

public class OAuthConstantsExt {
	private OAuthConstantsExt() {
	};

	// 微信参数
	// 通过code获取access_token
	public static final String APPID = "appid";
	public static final String SECRET = "secret";
	public static final String CODE = OAuthConstants.CODE;
	public static final String STATE = "state";
	public static final String GRANT_TYPE = "grant_type";

	public static final String REDIRECT_URI = OAuthConstants.REDIRECT_URI;
	// 获取用户信息
	public static final String ACCESS_TOKEN = OAuthConstants.ACCESS_TOKEN;
	public static final String OPENID = "openid";
}
