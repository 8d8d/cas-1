package org.pac4j.oauth.client;

import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.weixing.WeiXinProfile;
import org.scribe.builder.api.WeiXinApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.ProxyOAuth20ServiceImpl;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * 此类用于处理CAS与微信的OAUTH通信
 * 
 * @author linzl
 *
 *         2016年11月30日
 */
public class QQClient extends BaseOAuth20Client<WeiXinProfile> {

	public QQClient() {
		setTokenAsHeader(true);
	}

	public QQClient(final String key, final String secret) {
		setKey(key);
		setSecret(secret);
		setTokenAsHeader(true);
	}

	@Override
	protected QQClient newClient() {
		return new QQClient();
	}

	@Override
	protected void internalInit(final WebContext context) {
		super.internalInit(context);
		WeiXinApi20 api = new WeiXinApi20();
		this.service = new ProxyOAuth20ServiceImpl(
				api, new OAuthConfig(this.key, this.secret, computeFinalCallbackUrl(context), SignatureType.Header,
						null, null),
				this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort, false, true);
	}

	@Override
	protected String getProfileUrl(final Token accessToken) {
		return "https://public-api.WeiXin.com/rest/v1/me/?pretty=1";
	}

	@Override
	protected WeiXinProfile extractUserProfile(final String body) {
		final WeiXinProfile profile = new WeiXinProfile();
		JsonNode json = JsonHelper.getFirstNode(body);
		if (json != null) {
			profile.setId(JsonHelper.get(json, "ID"));
			// for (final String attribute :
			// WeiXinAttributesDefinition.getPrincipalAttributes()) {
			// profile.addAttribute(attribute, JsonHelper.get(json, attribute));
			// }
			json = json.get("meta");
			// if (json != null) {
			// final String attribute = WeiXinAttributesDefinition.LINKS;
			// profile.addAttribute(attribute, JsonHelper.get(json, attribute));
			// }
		}
		return profile;
	}

	@Override
	protected boolean hasBeenCancelled(final WebContext context) {
		return false;
	}
}
