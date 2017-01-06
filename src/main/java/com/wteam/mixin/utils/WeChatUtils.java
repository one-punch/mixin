package com.wteam.mixin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.biz.service.impl.WechatServiceImpl;
import com.wteam.mixin.constant.WeChatInfoUrls;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.constant.WechatConfigs;
import com.wteam.mixin.model.wexin.TokenReqBody;
import com.wteam.mixin.model.wexin.WechatToken;

public class WeChatUtils {
	private static final String[] hexCode = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E",
			"F" };
	private static final char[] code = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P',
			'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
			'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z' };

	 /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(WeChatUtils.class.getName());
	
	private static Properties prop = WechatConfigs.prop;

	/**
	 * 签名验证
	 * 
	 * @param token
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 */
	public static boolean checkSignature(String token, String timestamp, String nonce, String signature) {
		String[] arr = { token, timestamp, nonce };
		// ①字典排序
		Arrays.sort(arr);
		StringBuilder builder = new StringBuilder();
		// ②拼成字符串
		for (String string : arr) {
			builder.append(string);
		}
		// ③加密比较
		return signature.equals(messageDigest("sha1", builder.toString()));
	}

	/**
	 * 生成统一下单签名
	 * 
	 * @param key
	 * @param order
	 * @return
	 */
	public static <T> String generateSign(String key, T obj) {

		Class<?> clz = obj.getClass();
		Field[] fields = clz.getDeclaredFields();
		Map<String, String> map = new HashMap<>();
		for (Field field : fields) {
			String fieldName = field.getName();
			String methodName = "get" + fieldName.substring(0, 1).toUpperCase()
					+ fieldName.substring(1, fieldName.length());
			String value = null;
			try {
				value = String.valueOf(clz.getMethod(methodName).invoke(obj, new Object[] {}) + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!"null".equals(value) && !"".equals(value)) {
//				System.out.println(fieldName + ": " + value);
//				微信的签名支付参数中用到了java的package关键字、因而需要进行转换
				if("packagee".equals(fieldName)){
					map.put("package", value);
					continue;
				}
				map.put(fieldName, value);
			}
		}
		Set<String> keySet = map.keySet();
		Iterator<String> iterator = keySet.iterator();
		String[] names = new String[keySet.size()];
		int i = 0;
		while (iterator.hasNext()) {
			names[i++] = iterator.next();
		}
		Arrays.sort(names);
		StringBuilder builder = new StringBuilder();
//		System.out.println("排序后");
		for (String string : names) {
//			System.out.println(string + ": " + map.get(string));
			builder.append(string + "=" + map.get(string) + "&");
		}
		builder.append("key=" + key);

		return messageDigest("MD5", builder.toString());
	}
	
	/**
	 * 生成jsapi接口调用签名
	 * @param params
	 * @return
	 */
	public static String generateTicketSign(Map<String, String> params){
		
		Set<String> keySet = params.keySet();
		Iterator<String> iterator = keySet.iterator();
		String[] keys = new String[keySet.size()];
		int i = 0;
		while (iterator.hasNext()) {
			String string = (String) iterator.next();
			keys[i++] = string;
		}
		Arrays.sort(keys);
		StringBuilder builder = new StringBuilder();
		for (String string : keys) {
			builder.append(string+"="+params.get(string)+"&");
		}
		String tmpString = builder.toString();
		
		return messageDigest("sha1", tmpString.substring(0, tmpString.length()-1)).toLowerCase();
	}

	/**
	 * 生成32位随机字符串
	 * 
	 * @return
	 */
	public static String generateRamdomChar() {
		Random rand = new Random(66);
		int[] ints = new int[32];
		for (int i = 0; i < ints.length; i++) {
			ints[i] = rand.nextInt(32);
		}
		char[] chars = new char[32];
		for (int i = 0; i < chars.length; i++) {
			chars[i] = code[ints[i]];
		}
//		System.out.println("生成的随机串为:" + new String(chars));
		return new String(chars);
	}

	/**
	 * 指定加密策略对input字符串进行加密
	 * 
	 * @param algorithm
	 *            加密策略
	 * @param input
	 *            输入
	 * @return 加密后的字符串
	 */
	public static String messageDigest(String algorithm, String input) {

		MessageDigest instance = null;
		try {
			instance = MessageDigest.getInstance(algorithm);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		byte[] bytes = instance.digest(input.getBytes());
		StringBuilder builder = new StringBuilder();
		for (byte b : bytes) {
			builder.append(hexCode[(b & 0xf0) >>> 4]);
			builder.append(hexCode[b & 0x0f]);
		}

		return builder.toString();
	}
	
	/**
	 * 从输入流中读取xml信息，返回该xml节点结构的map封装
	 * 
	 * @param in
	 * @return
	 */
	public static Map<String, String> readXmlMapFromStream(InputStream in) {
		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(in);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		Element root = document.getRootElement();
		Map<String, String> map = new HashMap<String, String>();
		@SuppressWarnings("unchecked")
		List<Element> elements = root.elements();
		for (Element ele : elements) {
			map.put(ele.getName(), ele.getStringValue());
		}

		return map;
	}

	/**
	 * 返回输入流中的内容
	 * 
	 * @param in
	 * @return
	 */
	public static String readTextFromStream(InputStream in, String charset) {
		
		String defaultCharset = "UTF-8";
		if(!"".equals(charset) && charset != null){
			defaultCharset = charset;
		}
		StringBuilder builder = new StringBuilder();
		try {
			BufferedReader reader= new BufferedReader(new InputStreamReader(in, defaultCharset));
			String lineContext = reader.readLine();
			while (lineContext != null) {
				builder.append(lineContext);
				lineContext = reader.readLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return builder.toString();
	}

	/**
	 * 验证token是否过期
	 * 
	 * @param token
	 * @return
	 */
	public static boolean checkTokensTimeout(WechatToken token) {

		if (token == null) {
//			System.out.println("token 为null");
			LOG.info("token 为null");
			return true;
		}
		Long created = token.getCreated();
		Long expired = token.getExpired() - 20 * 60 * 1000;
		Long elapsed = System.currentTimeMillis() - created;
		if (elapsed >= expired) {
//			System.out.println();
			LOG.info("token 已过期======创建时间:" + new Date(created).toString() + "&&&&预期时间:"
					+ new Date(created + expired) + "&&&&实际过期时间:" + new Date(created + token.getExpired()));
			return true;
		}

		return false;
	}

	/**
	 * 刷新compontent_access_token
	 * 
	 * @return
	 */
	public static WechatToken refreshComponentAccessToken(String tokenName, String tickey) {

		WechatToken cToken = new WechatToken();
		String appId = prop.getProperty(Wechat.ComponentConfigs.appId);
		String appSecret = prop.getProperty(Wechat.ComponentConfigs.appSecret);
		try {
			// 创建连接
			HttpURLConnection conn = createPostConnection(WeChatInfoUrls.component_access_token.url);
			TokenReqBody reqBody = new TokenReqBody(appId, appSecret, tickey);
			conn.connect();
			// 输出请求参数
//			System.out.println("request body: " + JSON.toJSONString(reqBody));
			conn.getOutputStream().write(JSON.toJSONString(reqBody).getBytes());
			// 获取响应码并判断是否成功
			if (conn.getResponseCode() == 200) {
//				System.out.println("请求更新component_access_token成功====" + new Date().toString());
				LOG.info("请求更新component_access_token成功====");
				// 获取并读取响应体
				InputStream is = conn.getInputStream();
				String read = WeChatUtils.readTextFromStream(is, "utf-8");
//				System.out.println(read);
				// 根据响应体构造component_access_token
				JSONObject json = JSON.parseObject(read);
				String token = (String) json.get(Wechat.JSONKeys.component_access_token);
				Integer expired = (Integer) json.get(Wechat.JSONKeys.expires_in);
				cToken.setCreated(new Date().getTime());
				cToken.setExpired(expired * 1000L);
				cToken.setName("component_access_token");
				cToken.setValue(token);
//				System.out.println(cToken.toString());
				return cToken;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 刷新authorizer_access_token
	 * 
	 * @param authorizer_appid
	 * @param authorizer_refresh_token
	 * @return
	 */
	public static WechatToken refreshAuthorizerAccessToken(String authorizer_appid, String authorizer_refresh_token, String component_access_token) {

		String component_appid = prop.getProperty(Wechat.ComponentConfigs.appId);
		try {
			// 创建连接
			/*
			 * http请求方式: POST（请使用https协议） https:// api.weixin.qq.com
			 * /cgi-bin/component/api_authorizer_token?component_access_token=
			 * xxxxx POST数据示例: { "component_appid":"appid_value",
			 * "authorizer_appid":"auth_appid_value",
			 * "authorizer_refresh_token":"refresh_token_value", }
			 */
			HttpURLConnection conn = createPostConnection(WeChatInfoUrls.authorizer_access_token.url + component_access_token);
			TokenReqBody reqBody = new TokenReqBody();
			reqBody.setAuthorizer_appid(authorizer_appid);
			reqBody.setComponent_appid(component_appid);
			reqBody.setAuthorizer_refresh_token(authorizer_refresh_token);

			conn.connect();
			// 输出请求参数
//			System.out.println("request body: " + JSON.toJSONString(reqBody));
			conn.getOutputStream().write(JSON.toJSONString(reqBody).getBytes());
			// 获取响应码并判断是否成功
			if (conn.getResponseCode() == 200) {
//				System.out.println();
				LOG.debug("请求更新authorizer_access_token->商家公众号:{}, params:{}, 更新时间：{},", authorizer_appid,reqBody,new Date().toString());
				// 获取并读取响应体
				/*
				 * 返回结果示例 { "authorizer_access_token":
				 * "aaUl5s6kAByLwgV0BhXNuIFFUqfrR8vTATsoSHukcIGqJgrc4KmMJ-JlKoC_-NKCLBvuU1cWPv4vDcLN8Z0pn5I45mpATruU0b51hzeT1f8",
				 * "expires_in": 7200, "authorizer_refresh_token":
				 * "BstnRqgTJBXb9N2aJq6L5hzfJwP406tpfahQeLNxX0w" }
				 */
				InputStream is = conn.getInputStream();
				String read = WeChatUtils.readTextFromStream(is, "utf-8");
//				System.out.println(read);
				// 根据响应体构造component_access_token
				LOG.debug("result:{}",read);
				JSONObject json = JSON.parseObject(read);
				
				
				String token = (String) json.get(Wechat.JSONKeys.authorizer_access_token);
				Integer expired = json.getInteger(Wechat.JSONKeys.expires_in);
				String refreshToken = (String) json.get(Wechat.JSONKeys.authorizer_refresh_token);

				WechatToken cToken = new WechatToken(authorizer_appid, token, new Date().getTime(), expired * 1000L,
						refreshToken);
//				System.out.println(cToken.toString());

				return cToken;
			}
		} catch (Exception e) {
			LOG.debug("",e);
		}

		return null;
	}
	
	/**
	 * 刷新网页授权调用凭据 jsapi_ticket
	 * @param authAccessToken 调用的该jsapi的公众号授权调用凭据authorization_access_token
	 * @return
	 */
	public static WechatToken refreshJSapiTicket(String authAccessToken){
		
		try {
			String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+authAccessToken+"&type=jsapi";
			HttpURLConnection conn = WeChatUtils.createGetConnection(url);
			conn.connect();
			if(conn.getResponseCode() == 200){
				String jsonText = WeChatUtils.readTextFromStream(conn.getInputStream(), "UTF-8");
				JSONObject obj = JSON.parseObject(jsonText);
				String ticket = (String) obj.get("ticket");
				Integer expired = (Integer) obj.get("expires_in");
				return new WechatToken("jsapi_ticket", ticket, System.currentTimeMillis(), expired*1000L);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	/**
	 * 获取指定url的post连接
	 * 
	 * @param url
	 * @return
	 */
	public static HttpURLConnection createPostConnection(String url) {
		try {
            if(LOG.isDebugEnabled()) LOG.debug("url->{}",url);
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(5000);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setDoInput(true);

			return conn;
		} catch (Exception e) {
            if(LOG.isErrorEnabled()) LOG.error("",e);
		}

		return null;
	}

	/**
	 * 获取指定url的get连接
	 * 
	 * @param url
	 * @return
	 */
	public static HttpURLConnection createGetConnection(String url) {
		try {
		    if(LOG.isDebugEnabled()) LOG.debug("url->{}",url);
			URL u = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) u.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			conn.setDoInput(true);

			return conn;
		} catch (Exception e) {
		    if(LOG.isErrorEnabled()) LOG.error("",e);
		}

		return null;
	}

	/**
	 * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
	 * 
	 * @param request
	 * @return ip
	 */
	public static String getRemoteAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (ip!=null && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (ip!=null && !"".equals(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
