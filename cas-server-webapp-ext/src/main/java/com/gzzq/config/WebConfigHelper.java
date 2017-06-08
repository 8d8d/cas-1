package com.gzzq.config;

import java.util.Arrays;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.SystemConfiguration;

/**
 * 配置管理工具,该工具只能获取到
 * 
 * 系统环境、web.properties
 * 
 * @author linzl
 *
 *         2017年1月10日
 */
public class WebConfigHelper {
	private static CompositeConfiguration config = new CompositeConfiguration();

	static {
		config.addConfiguration(new SystemConfiguration());
		// 配置了sso
		try {
			config.addConfiguration(new PropertiesConfiguration("web.properties"));
		} catch (ConfigurationException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取不过滤的正则表达式
	 * 
	 * @return
	 */
	public static String getNotFilter() {
		String notFilter = Arrays.toString(config.getStringArray("casNotFilter"));
		if (notFilter != null && notFilter.matches("\\[.*?\\]")) {
			notFilter = notFilter.replaceAll("\\[|\\]| ", "");
		}
		return notFilter;
	}

	public static String getLoginDefaultRedirectUrl() {
		String defaultUrl = config.getString("cas.login.redirect.url");
		return defaultUrl;
	}

	public static String getLoginIntegralUrl() {
		return config.getString("cas.login.integral.url");
	}

	public static String getAssignName(String name) {
		return null;
	}
}
