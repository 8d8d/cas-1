package com.gzzq.config;

import java.io.IOException;
import java.util.List;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * httpclient 传输数据工具
 * 
 * @author liny
 * 
 *         2016年11月11日
 */
public class HttpClientUtil {
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtil.class);
	// 设置连接超时时间,单位毫秒
	private static final int connectionTimeout = 6000;
	// 设置套接字超时时间,单位毫秒
	private static final int soTimeout = 6000;
	// 设置每个主机的最大连接数
	private static final int maxConnectionsPerHost = 5;
	// 设置总共的最大连接数
	private static final int maxTotalConnections = 50;
	// 是否允许重连
	private static final boolean requestSentRetryEnabled = false;
	// 连接重试次数
	private static final int retryCount = 5;

	private static CloseableHttpClient httpClient = null;
	private static CloseableHttpResponse httpResponse = null;
	private static HttpPost httpPost = null;
	private static HttpGet httpGet = null;

	/**
	 * 默认配置
	 * 
	 * @return
	 */
	private static RequestConfig defaultConfig() {
		// 初始化客户端连接参数
		Builder builer = RequestConfig.custom();
		// 设置请求超时时间
		builer.setConnectTimeout(connectionTimeout);
		// 设置传输超时时间
		builer.setSocketTimeout(soTimeout);
		RequestConfig requestConfig = builer.build();
		return requestConfig;
	}

	/**
	 * 使用get方法提交参数
	 * 
	 * @param url
	 *            提交到对应的url,url带参数
	 * @return
	 */
	public static String httpGet(String url) {
		String result = "";
		try {
			httpClient = HttpClients.createDefault();
			httpGet = new HttpGet(url);
			httpResponse = httpClient.execute(httpGet);
			result = getResult();
		} catch (Exception e) {
			logger.error("HttpClientService httpGet服务出错:", e);
		} finally {
			closeHttp();
		}
		return result;
	}

	/**
	 * 表单post提交
	 * 
	 * @param url
	 *            提交到对应的url
	 * @param params
	 *            表单参数
	 */
	public static String httpPostForm(String url, List<NameValuePair> params) {
		String result = "";
		try {
			// 创建http服务
			httpClient = HttpClients.createDefault();
			// 提交请求
			httpPost = new HttpPost(url);
			httpPost.setConfig(defaultConfig());

			// 设置传递的参数
			UrlEncodedFormEntity pramEntity = new UrlEncodedFormEntity(params, Consts.UTF_8);
			httpPost.setEntity(pramEntity);
			// 执行post请求
			httpResponse = httpClient.execute(httpPost);
			result = getResult();
		} catch (Exception e) {
			logger.error("HttpClientService httpPostForm服务出错:", e);
		} finally {
			closeHttp();
		}
		return result;
	}

	private static HttpEntity getEntityResult() {
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = httpResponse.getEntity();
			return entity;
		}
		return null;
	}

	private static String getResult() {
		String result = "";
		if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			HttpEntity entity = httpResponse.getEntity();
			try {
				result = EntityUtils.toString(entity, Consts.UTF_8);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	/**
	 * 关闭http请求
	 */
	private static void closeHttp() {
		try {
			if (httpGet != null) {
				httpGet.releaseConnection();
			}
			if (httpPost != null) {
				httpPost.releaseConnection();
			}
			if (httpResponse != null) {
				httpResponse.close();
			}
			if (httpClient != null) {
				httpClient.close();
			}
		} catch (Exception e) {
			logger.error("HttpClientService 关闭资源出错:", e);
		}
	}

	/**
	 * 
	 * @param url
	 *            提交到对应的url
	 * @param json
	 *            需要post的json格式数据
	 * @return
	 */
	public static String postJSON(String url, String json) {
		String result = "";
		try {
			httpClient = HttpClients.createDefault();
			httpPost = new HttpPost(url);

			StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);// 解决中文乱码问题
			httpPost.setEntity(entity);
			httpResponse = httpClient.execute(httpPost);
			result = getResult();
		} catch (Exception e) {
			logger.error("HttpClientService postJSON服务出错:", e);
		} finally {
			closeHttp();
		}
		return result;
	}

	// private static Map<String, Object> postToWecenter(UserVO user) {
	// // 用户注册信息post到圈子
	// String url = "http://quanzi.goetui.com/api/createOrUpdateUser.php";
	// List<NameValuePair> list = new ArrayList<NameValuePair>();
	// list.add(new BasicNameValuePair("account", user.getAccount()));
	// list.add(new BasicNameValuePair("uuid", user.getUuid()));
	// list.add(new BasicNameValuePair("nickname", user.getNickname()));
	// list.add(new BasicNameValuePair("mobile", user.getMobile()));
	// list.add(new BasicNameValuePair("email", user.getEmail()));
	// String resultJson = HttpClientUtil.httpPostForm(url, list);
	// logger.info("post的result:" + resultJson);
	// JSONObject obj = (JSONObject) JSONObject.parse(resultJson);
	// String result = obj.getString("result");
	// logger.info(" result:" + result);
	// return null;
	// }

	public static void main(String[] args) {
		// UserVO user = new UserVO();
		// user.setAccount("linzl12");
		// user.setUuid("112200");
		//
		// List<RoleVO> roleList = new ArrayList<RoleVO>();
		// RoleVO role = new RoleVO();
		// role.setRoleCode("admin");
		// roleList.add(role);
		//
		// RoleVO role1 = new RoleVO();
		// role1.setRoleCode("seller");
		// roleList.add(role1);
		// user.setRole(roleList);
		//
		// UserVO user1 = new UserVO();
		// user1.setAccount("linzl12");
		// user1.setUuid("112200");
		// user1.setRole(roleList);
		//
		// String url = "http://quanzi.goetui.com/api/createOrUpdateUser.php";
		// String url = "http://wecenter.php.com/api/createOrUpdateUser.php";
		//
		// List userList = new ArrayList();
		// userList.add(user);
		// userList.add(user1);
		//
		// JSONObject object = new JSONObject();
		// object.put("user", userList);
		//
		// System.out.println(JSON.toJSONString(object));
		// String result = postJSON(url, JSON.toJSONString(object));
		// System.out.println("result==>" + result);
		// System.out.println(jsonO.get("message"));
	}
}
