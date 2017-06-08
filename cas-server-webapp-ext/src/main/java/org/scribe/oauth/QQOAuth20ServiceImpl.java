package org.scribe.oauth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.scribe.builder.api.DefaultApi20;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.OAuthConfig;
import org.scribe.model.OAuthConstants;
import org.scribe.model.OAuthConstantsExt;
import org.scribe.model.OAuthRequest;
import org.scribe.model.ProxyOAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用于添加获取ACCESS_TOKEN与用户信息添加参数并请求QQ
 * 
 * @author linzl
 * 
 */
public class QQOAuth20ServiceImpl extends ProxyOAuth20ServiceImpl {
	protected static final Logger logger = LoggerFactory.getLogger(QQOAuth20ServiceImpl.class);

	private static Pattern openIdPattern = Pattern.compile("\"openid\":\\s*\"(\\S*?)\"");

	public QQOAuth20ServiceImpl(DefaultApi20 api, OAuthConfig config, int connectTimeout, int readTimeout,
			String proxyHost, int proxyPort) {
		super(api, config, connectTimeout, readTimeout, proxyHost, proxyPort);
	}

	/**
	 * 第二步 通过code获取access_token
	 */
	@Override
	public Token getAccessToken(final Token requestToken, final Verifier verifier) {
		final OAuthRequest request = new ProxyOAuthRequest(this.api.getAccessTokenVerb(),
				this.api.getAccessTokenEndpoint(), this.connectTimeout, this.readTimeout, this.proxyHost,
				this.proxyPort);
		request.addBodyParameter(OAuthConstants.CLIENT_ID, this.config.getApiKey());
		request.addBodyParameter(OAuthConstants.CLIENT_SECRET, this.config.getApiSecret());
		request.addBodyParameter(OAuthConstantsExt.CODE, verifier.getValue());
		request.addBodyParameter(OAuthConstantsExt.GRANT_TYPE, "authorization_code");
		request.addBodyParameter(OAuthConstants.REDIRECT_URI, this.config.getCallback());
		final Response response = request.send();
		return this.api.getAccessTokenExtractor().extract(response.getBody());
	}

	/**
	 * 第三步：通过access_token获取用户open id
	 * 
	 * 直接在第四步中去获取
	 */
	// https://graph.qq.com/oauth2.0/me?access_token=6CEC25B23A45F47F19B2DD33D93A3374

	/**
	 * 第四步 通过access_token调用接口,获取用户信息
	 */
	@Override
	public void signRequest(final Token accessToken, final OAuthRequest request) {
		request.addQuerystringParameter(OAuthConstants.ACCESS_TOKEN, accessToken.getToken());
		request.addQuerystringParameter(OAuthConstants.CONSUMER_KEY, this.config.getApiKey());
		String response = accessToken.getRawResponse();
		Matcher matcher = openIdPattern.matcher(response);
		if (matcher.find()) {
			request.addQuerystringParameter(OAuthConstantsExt.OPENID, matcher.group(1));
		} else {
			throw new OAuthException("QQ接口返回数据miss openid: " + response);
		}
	}
}