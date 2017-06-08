package org.scribe.builder.api;

import org.scribe.extractors.AccessTokenExtractor;
import org.scribe.extractors.JsonTokenExtractor;
import org.scribe.model.OAuthConfig;
import org.scribe.model.Verb;
import org.scribe.utils.OAuthEncoder;

/**
 * 用于定义获取QQ返回的CODE与ACCESS_TOKEN
 * 
 * @author linzl
 *
 */
public class QQApi20 extends DefaultApi20 {

	private static final String QQ_AUTHORIZE_URL = "https://graph.qq.com/oauth2.0/authorize?client_id=%s&response_type=code&scope=get_user_info&redirect_uri=%s";

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
		return "https://graph.qq.com/oauth2.0/token";
	}

	@Override
	public String getAuthorizationUrl(OAuthConfig config) {
		return String.format(QQ_AUTHORIZE_URL, config.getApiKey(), OAuthEncoder.encode(config.getCallback()));
	}

}
