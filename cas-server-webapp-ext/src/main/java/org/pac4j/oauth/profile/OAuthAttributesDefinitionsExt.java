package org.pac4j.oauth.profile;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.weixing.WeiXinAttributesDefinition;

/**
 * 参考OAuthAttributesDefinitions
 * 
 * @author linzl
 *
 */
public class OAuthAttributesDefinitionsExt {
	public final static AttributesDefinition weixingDefinition = new WeiXinAttributesDefinition();
}
