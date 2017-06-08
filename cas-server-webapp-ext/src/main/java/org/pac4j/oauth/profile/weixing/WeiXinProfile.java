
package org.pac4j.oauth.profile.weixing;

import org.pac4j.core.profile.AttributesDefinition;
import org.pac4j.oauth.profile.OAuth20Profile;

/**
 * 用于添加返回用户信息
 * 
 * @author linzl
 *
 *         2016年12月25日
 */
public class WeiXinProfile extends OAuth20Profile {

	private static final long serialVersionUID = 6790248892408246089L;

	@Override
	protected AttributesDefinition getAttributesDefinition() {
		return new WeiXinAttributesDefinition();
	}

}
