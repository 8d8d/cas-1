package org.jasig.cas.client.authentication;

import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 对cas 的filter url 规则进行忽略扩展
 * <p/>
 * FilterRegistration.Dynamic authenticationFilter =
 * servletContet.addFilter("CAS Authentication Filter", new
 * AuthenticationFilter());
 * <p/>
 * 
 * authenticationFilter.setInitParameter("casServerLoginUrl",
 * "https://server.fighting.com:8443/cas/login");
 * <p/>
 * authenticationFilter.setInitParameter("serverName",
 * "http://client1.fighting.com:28082/btdSite/");
 * <p/>
 * authenticationFilter.setInitParameter("ignorePattern", "忽略的url格式,多个用,隔开");
 * <p/>
 * authenticationFilter.setInitParameter("ignoreUrlPatternType","org.jasig.cas.client.authentication.UrlPatternMatcherStrategyExt");
 * <p/>
 * authenticationFilter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.FORWARD,DispatcherType.REQUEST,DispatcherType.INCLUDE),false,"/*");
 * <p/>
 * 
 * @author linzl 2016年11月17日
 */
public class UrlPatternMatcherStrategyExt implements UrlPatternMatcherStrategy {
	private Pattern[] patterns;

	public UrlPatternMatcherStrategyExt() {
	}

	public UrlPatternMatcherStrategyExt(final String pattern) {
		this.setPattern(pattern);
	}

	public boolean matches(String url) {
		for (Pattern pattern : this.patterns) {
			if (pattern.matcher(url).find()) {
				return true;
			}
		}
		return false;
	}

	public void setPattern(String pattern) {
		String[] patternArrs = StringUtils.split(pattern, ",");
		patterns = new Pattern[patternArrs.length];

		for (int index = 0; index < patternArrs.length; index++) {
			// 正则表达式前后要去除空格
			patterns[index] = Pattern.compile(StringUtils.trim(patternArrs[index]));
		}
	}
}
