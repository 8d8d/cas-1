package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

/**
 * 用于定义获取微信返回的CODE与ACCESS_TOKEN
 * 
 * @author linzl
 *
 */
public class WeiXinApi20 extends DefaultApi20 {

	private static final String WEIXIN_AUTHORIZE_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_login#wechat_redirect";

	/**
	 * 解析返回的token
	 */
	@Override
	public AccessTokenExtractor getAccessTokenExtractor() {
		return new JsonTokenExtractor();
	}

	@Override
	public Verb getAccessTokenVerb() {
		return Verb.POST;
	}

	/**
	 * 通过微信返回的code获取access_token
	 */
	@Override
	public String getAccessTokenEndpoint() {
		return "https://api.weixin.qq.com/sns/oauth2/access_token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(WEIXIN_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	}

}
