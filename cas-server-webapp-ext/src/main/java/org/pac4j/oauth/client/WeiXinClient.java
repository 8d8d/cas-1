package org.pac4j.oauth.client;

import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.pac4j.core.context.WebContext;
import org.pac4j.oauth.profile.JsonHelper;
import org.pac4j.oauth.profile.OAuthAttributesDefinitionsExt;
import org.pac4j.oauth.profile.weixing.WeiXinAttributesDefinition;
import org.pac4j.oauth.profile.weixing.WeiXinProfile;
import org.scribe.builder.api.WeiXinApi20;
import org.scribe.model.OAuthConfig;
import org.scribe.model.SignatureType;
import org.scribe.model.Token;
import org.scribe.oauth.WeiXinOAuth20ServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gzzq.config.HttpClientUtil;
import com.gzzq.vo.CasUserVO;
import com.gzzq.vo.OAuthUserVO;
import com.gzzq.vo.ResponseResult;

/**
 * 此类用于处理CAS与微信的OAUTH通信,类名必须以Client结尾
 * 
 * @author linzl
 *
 *         2016年11月30日
 */
@Component
public class WeiXinClient extends BaseOAuth20Client<WeiXinProfile> {
	protected static final Logger logger = LoggerFactory.getLogger(WeiXinClient.class);

	private String uuid = "uuid";
	private String account = "account";

	private String wxName = "wechat";

	@Value("{cas.pac4j.wechat.reg.url:https://ucenter.utea20.com/services/user/reg/oauth}")
	private String wxUrl;

	private JdbcTemplate jdbcTemplate;
	@Autowired
	private DataSource dataSource;

	// 判断第三方登录是否已经注册过，注册过返回用户信息
	public Map<String, Object> getRegOauthUser(String unionId, String openId) {
		logger.debug("unionId==>{},openId==>{}", unionId, openId);
		String querySql = null;
		String resultSql = null;

		this.jdbcTemplate = new JdbcTemplate(dataSource);
		if (StringUtils.isNotEmpty(unionId)) {
			querySql = "SELECT count(1) FROM utea_oauth_user oauth where oauth.union_id =?";
			int result = this.jdbcTemplate.queryForObject(querySql, new Object[] { unionId }, Integer.class);

			if (result > 0) {// 说明
				resultSql = "SELECT uuid, account,nickname FROM utea_oauth_user oauth, utea_sys_user sys "
						+ " WHERE oauth.user_uuid = sys.uuid AND oauth.union_id =?";
				final Map<String, Object> obj = this.jdbcTemplate.queryForMap(resultSql, unionId);
				logger.debug("unionId查询结果有值{}", obj);
				return obj;
			}
		} else if (StringUtils.isNotEmpty(openId)) {
			querySql = "SELECT count(1) FROM utea_oauth_user oauth where oauth.open_id = ?";
			int result = this.jdbcTemplate.queryForObject(querySql, new Object[] { openId }, Integer.class);

			if (result > 0) {// 说明
				resultSql = "SELECT uuid, account,nickname FROM utea_oauth_user oauth, utea_sys_user sys "
						+ " WHERE oauth.user_uuid = sys.uuid AND oauth.open_id =?";
				final Map<String, Object> obj = this.jdbcTemplate.queryForMap(resultSql, openId);
				logger.debug("openId查询结果有值{}", obj);
				return obj;
			}
		}

		logger.debug("没有值，哈哈");
		return null;
	}

	public WeiXinClient() {
	}

	public WeiXinClient(final String key, final String secret) {
		setKey(key);
		setSecret(secret);
	}

	@Override
	protected WeiXinClient newClient() {
		WeiXinClient newClient = new WeiXinClient();
		newClient.setName("wx");
		return newClient;
	}

	@Override
	protected void internalInit(final WebContext context) {
		super.internalInit(context);
		WeiXinApi20 api = new WeiXinApi20();
		this.service = new WeiXinOAuth20ServiceImpl(api,
				new OAuthConfig(this.key, this.secret, this.callbackUrl, SignatureType.Header, null, null),
				this.connectTimeout, this.readTimeout, this.proxyHost, this.proxyPort);
	}

	/**
	 * 获取用户信息
	 */
	@Override
	protected String getProfileUrl(final Token accessToken) {
		return "https://api.weixin.qq.com/sns/userinfo";
	}

	/**
	 * 解析微信返回的数据
	 */
	@Override
	protected WeiXinProfile extractUserProfile(String body) {
		WeiXinProfile weiXinProfile = new WeiXinProfile();
		WeiXinProfile resultProfile = null;
		final JsonNode json = JsonHelper.getFirstNode(body);
		if (null != json) {
			for (final String attribute : OAuthAttributesDefinitionsExt.weixingDefinition.getPrincipalAttributes()) {
				logger.debug("用户信息attribute{},值{}", attribute, JsonHelper.get(json, attribute));
				weiXinProfile.addAttribute(attribute, JsonHelper.get(json, attribute));
			}

			/** 绑定账号到系统 */
			String unionid = (String) weiXinProfile.getAttributes().get(WeiXinAttributesDefinition.UNION_ID);
			String openid = (String) weiXinProfile.getAttributes().get(WeiXinAttributesDefinition.OPEN_ID);
			String nickname = (String) weiXinProfile.getAttributes().get(WeiXinAttributesDefinition.NICK_NAME);

			// 判断是否已经存在绑定账号，没有直接注册
			Map<String, Object> map = getRegOauthUser(unionid, openid);
			logger.debug("用户信息map==>{}", map);

			if (map == null) {
				OAuthUserVO vo = new OAuthUserVO();
				vo.setOauthName(wxName);
				vo.setOpenid(openid);
				vo.setNickname(nickname);
				vo.setUnionid(unionid);

				ObjectMapper mapper = new ObjectMapper();
				String value = null;
				try {
					value = mapper.writeValueAsString(vo);
				} catch (JsonProcessingException e) {
					e.printStackTrace();
				}
				String regUser = HttpClientUtil.postJSON(wxUrl, value);
				logger.debug("用户信息 JSONUtils.toJSONString(vo)==>{}", regUser);
				if (StringUtils.isNotEmpty(regUser)) {
					ResponseResult user = null;
					try {
						user = mapper.readValue(regUser, ResponseResult.class);
						CasUserVO result = user.getResult();
						logger.debug("用户信息user==>{}", user);

						resultProfile = new WeiXinProfile();
						resultProfile.addAttribute(uuid, result.getUuid());
						resultProfile.addAttribute(account, result.getAccount());
						resultProfile.addAttribute(WeiXinAttributesDefinition.NICK_NAME, nickname);
						return resultProfile;
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				resultProfile = new WeiXinProfile();
				resultProfile.addAttribute(uuid, map.get("uuid"));
				resultProfile.addAttribute(account, map.get("account"));
				resultProfile.addAttribute(WeiXinAttributesDefinition.NICK_NAME, nickname);
				return resultProfile;
			}
		}
		return null;
	}

	@Override
	protected boolean hasBeenCancelled(final WebContext context) {
		return false;
	}

}
