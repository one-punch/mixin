package com.wteam.mixin.biz.service.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.SSLContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.biz.service.IWechatService;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.WeChatInfoUrls;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.constant.WechatConfigs;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UploadFilePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.po.WechatReplyPo;
import com.wteam.mixin.model.vo.CustomerInfoVo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.wexin.BusinessAuthorizationInfo;
import com.wteam.mixin.model.wexin.RequestMessage;
import com.wteam.mixin.model.wexin.ResponseMessage;
import com.wteam.mixin.model.wexin.TokenReqBody;
import com.wteam.mixin.model.wexin.WcPayUniformOrderRespBody;
import com.wteam.mixin.model.wexin.WechatToken;
import com.wteam.mixin.model.wexin.XPayConfig;
import com.wteam.mixin.model.wexin.XPayOrderRefundVo;
import com.wteam.mixin.model.wexin.XPayOrderVo;
import com.wteam.mixin.utils.FileUtils;
import com.wteam.mixin.utils.HttpRequest;
import com.wteam.mixin.utils.Utils;
import com.wteam.mixin.utils.WeChatUtils;
import com.wteam.mixin.utils.XmlRequestMsgMapper;
import com.wteam.mixin.utils.XmlRespMsgGenerator;

@Service("wechatService")
public class WechatServiceImpl implements IWechatService{
	
	@Autowired
	private IBaseDao baseDao;
	
	@Autowired
	private CacheManager cacheManager;
	
	@Autowired
	ILoginService loginService;
	
	@Autowired
	IUserService userService;
	
	@Autowired
	DozerBeanMapper mapper;
	 /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(WechatServiceImpl.class.getName());

    private static Properties prop = WechatConfigs.prop;
	
	@Override
	public void bindSuccess(Long businessId, String authCode) {
		updateBusinessInfo(businessId, authCode);
	}
	
	@Override
	public String authLoginSuccess(String appId, String code) {
		//1. 校验平台调用凭据是否过期
		Cache<Object, Object> cache = cacheManager.getCache("wechatCache");
		WechatToken cToken = (WechatToken) cache.get("component_access_token");
		if(cToken==null){
			WechatToken tickey = (WechatToken) cache.get("component_verify_tickey");
			if(tickey == null){
				throw new ServiceException("服务器忙，请您10分钟再操作");
			}
			cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
			cache.put("component_access_token", cToken);
		}
//		System.out.println("准备获取用户信息..");
		LOG.info("准备获取用户信息..");
		String component_access_token = cToken.getValue();
		String component_appid = prop.getProperty(Wechat.ComponentConfigs.appId);
		String url = "https://api.weixin.qq.com/sns/oauth2/component/access_token?"
				+ "appid=" + appId
				+ "&code=" + code
				+ "&grant_type=authorization_code"
				+ "&component_appid=" + component_appid
				+ "&component_access_token=" + component_access_token;
		//3. 使用code换取用户的调用凭据
		HttpURLConnection conn = WeChatUtils.createGetConnection(url);
		try {
			conn.connect();
			/*
			 * 返回说明
				正确的返回：
				{ 
				"access_token":"ACCESS_TOKEN", 
				"expires_in":7200, 
				"refresh_token":"REFRESH_TOKEN",
				"openid":"OPENID", 
				"scope":"SCOPE" 
				}
			 */
			if(conn.getResponseCode() == 200){
				InputStream in = conn.getInputStream();
				String respText = WeChatUtils.readTextFromStream(in, "utf-8");
//						System.out.println("响应体：");
//						System.out.println(respText);
//				System.out.println("获取用户信息结束..");
				LOG.info("获取用户信息结束.. {}", respText);
				
				JSONObject obj = JSON.parseObject(respText);
				String openid = (String) obj.get("openid");
//				判断数据库中是否已存在 该用户信息
				Long customerId ;
				UserPo userPo = baseDao.get("from UserPo where account=?", new Object[]{openid});
				if (userPo == null) {
				    UserVo userVo = loginService.registerCustomer(openid);
				    customerId = userVo.getUserId();
                } else {
                    customerId = userPo.getUserId();
                }

                // 2.1 使用access_token换取用户信息并保存，返回授权登录
                String accessToken = (String)obj.get("access_token");
                // Integer expiresIn = (Integer) obj.get("expires_in");
                // String refreshToken = (String) obj.get("refresh_token");
                String infoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=" + accessToken + "&openid=" + openid;
                HttpURLConnection infoConn = WeChatUtils.createGetConnection(infoUrl);
                infoConn.connect();
                if (infoConn.getResponseCode() == 200) {
                    InputStream is = infoConn.getInputStream();
                    String infoText = WeChatUtils.readTextFromStream(is, "utf-8");
                    // System.out.println(infoText);
                    // 2.2 创建用户信息
                    CustomerInfoVo customerInfoVo = JSON.parseObject(infoText, CustomerInfoVo.class);
                    LOG.info(customerInfoVo.toString());
                    customerInfoVo.setCustomerId(customerId);
                    // 2.3 关联用户并保存
                    userService.updateInfo(customerInfoVo);

                    // System.out.println("授权登录成功");
                    LOG.info("授权登录成功");

                    return openid;
                }
			}
		} catch (Exception e) {
			LOG.error("",e);
		}
		
		return "";
	}

	@Override
	public void auth(Map<String, String> map) {
		
		String appId = map.get(Wechat.XmlTags.AuthorizerAppid);
//		System.out.println("授权事件=======公众号:"+appId);
		LOG.info("授权事件=======公众号:"+appId);
	}

	@Override
	public void unAuth(Map<String, String> map) {
		
		String appId = map.get(Wechat.XmlTags.AuthorizerAppid);
//		System.out.println("取消授权事件=======公众号:"+appId);
		LOG.info("取消授权事件=======公众号:"+appId);
		
		BusinessInfoPo businessInfo = baseDao.get("from BusinessInfoPo where authorizerAppid=?", new Object[]{appId});

        // TODO
		if (businessInfo != null) {
		    businessInfo.setIsAuthorized(false);
		    baseDao.update(businessInfo);
        }
	}

	@Override
	public void reAuth(Map<String, String> map) {
		String appId = map.get(Wechat.XmlTags.AuthorizerAppid);
//		System.out.println("重新授权事件=======公众号:"+appId);
		LOG.info("重新授权事件=======公众号:"+appId);
		//在此处 应该更新公众号的授权权限信息
		BusinessInfoPo businessInfo = baseDao.get("from BusinessInfoPo where authorizerAppid=?", new Object[]{appId});
		/*
		 * 数据格式：
		 * <xml>
			<AppId>第三方平台appid</AppId>
			<CreateTime>1413192760</CreateTime>
			<InfoType>updateauthorized</InfoType>
			<AuthorizerAppid>公众号appid</AuthorizerAppid>
			<AuthorizationCode>授权码（code）</AuthorizationCode>
			<AuthorizationCodeExpiredTime>过期时间</AuthorizationCodeExpiredTime>
		   </xml>
		 */
		// TODO
        if (businessInfo != null) {
            
            String authCode = map.get("AuthorizationCode");
            updateBusinessInfo(businessInfo.getBusinessId(), authCode);
        }
	}

	@Override
	public void ticketFresh(Map<String, String> map) {
//		1. 从解析到的数据中获取ticket
		String tmp = map.get(Wechat.XmlTags.ComponentVerifyTicket);
		String tickey = tmp.substring(tmp.lastIndexOf("@")+1);
//		2. 更新缓存中的component_verify_ticket
		Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
		wechatCache.put("component_verify_ticket", new WechatToken("component_verify_ticket", tickey, System.currentTimeMillis(), 10*60*1000L));
		/*
		 * 每次微信推送ticket时都会进行 平台调用凭据component_access_token的过期判断，
		 * 预期是当component_access_token有效期剩余20分钟时进行更新
		 */
//		3.判断当前平台的调用凭据component_access_token是否快过期、 过期进行刷新、否则不做处理
		WechatToken cToken = (WechatToken) wechatCache.get("component_access_token");
		if(WeChatUtils.checkTokensTimeout(cToken)){
			wechatCache.put("component_access_token", WeChatUtils.refreshComponentAccessToken("component_access_token", tickey));
		}
	}

	@Override
	public String handleRequest(HttpServletRequest req) {
//		System.out.println("handle started---"+new Date().toString());
		LOG.info("handle started---"+new Date().toString());
		try {
			//获取加解密所需参数:msg_signature, timestamp, nonce
			String msgSignature = req.getParameter(Wechat.URLParameters.msg_signature);
			String timeStamp = req.getParameter(Wechat.URLParameters.timestamp);
			String nonce = req.getParameter(Wechat.URLParameters.nonce);
			
			//获取微信服务器推送过来的数据包
			String readTextFromStream = WeChatUtils.readTextFromStream(req.getInputStream(), "utf-8");
//			System.out.println("解密前:"+readTextFromStream);
			LOG.info("解密前:"+readTextFromStream);
			
			//获取平台信息以生成微信加解密类 WXBizMsgCrypt
			String token = prop.getProperty(Wechat.ComponentConfigs.token);
			String encodingAesKey = prop.getProperty(Wechat.ComponentConfigs.encodingAESKey);
			String appId = prop.getProperty(Wechat.ComponentConfigs.appId);
			//生成加解密类 并解密
			WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
			String decryptMsg = pc.decryptMsg(msgSignature, timeStamp, nonce, readTextFromStream);
//			System.out.println("解密后:"+decryptMsg);
			LOG.info("解密后:"+decryptMsg);
			Map<String, String> xmlMap = WeChatUtils.readXmlMapFromStream(new ByteArrayInputStream(decryptMsg.getBytes()));
			//处理具体的信息类型
			String MsgType = xmlMap.get(Wechat.XmlTags.MsgType);
			
			return handleMsgType(MsgType, xmlMap, timeStamp, nonce);
			
		} catch (Exception e){
			e.printStackTrace();
		}
		return "success";
	}

	@Override
	public JSONObject setupJsapiConfig(String appId, String beConfigedUrl) {
//		1.1  获取该公众号的jsapi_ticket并进行 null和过期判断		
		BusinessInfoPo businessInfo = baseDao.get("from BusinessInfoPo where authorizerAppid=?", new Object[]{appId});
		LOG.debug("appId:{}, beConfigedUrl:{}, businessInfo:{}", appId, beConfigedUrl, businessInfo);
		WechatToken jToken = new WechatToken();
		if (businessInfo.getJsapiTicket() != null && !"".equals(businessInfo.getJsapiTicket()) ) {
		    jToken = new WechatToken("jsapi", businessInfo.getJsapiTicket(), businessInfo.getTicketStartTime().getTime(), businessInfo.getTicketExpired());
        }
		if(businessInfo.getJsapiTicket() == null || "".equals(businessInfo.getJsapiTicket()) || WeChatUtils.checkTokensTimeout(jToken)){
//			 已过期、 刷新jsapi_ticket
//			1.1.1    获取商家公众号授权的调用凭据 authorization_access_token
			WechatToken aToken = new WechatToken("access_token", businessInfo.getAuthorizerAccessToken(), businessInfo.getTokenBeginTime().getTime(), businessInfo.getExpiresIn());
			if(aToken.getValue()==null || "".equals(aToken.getValue())){
				throw new ServiceException("请公众号先行授权..");
			}
//			1.1.2 判断公众号调用凭据是否已过期 
			if(WeChatUtils.checkTokensTimeout(aToken)){
	            //1. 校验平台调用凭据是否过期
	            WechatToken cToken = getComponent_access_token();
//				过期、更新数据库中该公众号的调用凭据
				aToken = WeChatUtils.refreshAuthorizerAccessToken(appId, businessInfo.getRefreshToken(), cToken.getValue());
				businessInfo.setAuthorizerAccessToken(aToken.getValue());
				businessInfo.setRefreshToken(aToken.getRefreshToken());
				businessInfo.setExpiresIn(aToken.getExpired());
				businessInfo.setTokenBeginTime(new Date());
			}
			String authAccessToken = aToken.getValue();
			jToken = WeChatUtils.refreshJSapiTicket(authAccessToken);
			businessInfo.setJsapiTicket(jToken.getValue());
			businessInfo.setTicketStartTime(new Date());
			businessInfo.setTicketExpired(jToken.getExpired());
//			更新该jsapi_ticket
			baseDao.update(businessInfo);
		}
		/*
		 * 2. 进行 jsapi_ticket签名
		 * 	/*
		 * wx.config({
			    debug: true, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
			    appId: '', // 必填，公众号的唯一标识
			    timestamp: , // 必填，生成签名的时间戳
			    nonceStr: '', // 必填，生成签名的随机串
			    signature: '',// 必填，签名，见附录1
			    jsApiList: [] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
			});
		 */
		String noncestr = WeChatUtils.generateRamdomChar();
		String timestamp = String.valueOf((int)(System.currentTimeMillis() / 1000));
		Map<String, String> params = new HashMap<>();
		params.put("noncestr", noncestr);
		params.put("timestamp", timestamp);
		params.put("jsapi_ticket", jToken.getValue());
		params.put("url", beConfigedUrl);
//		签名
		String ticketSign = WeChatUtils.generateTicketSign(params);
		
//		准备前台所需参数
		JSONObject obj = new JSONObject();
		obj.put("nonceStr", noncestr);
		obj.put("signature", ticketSign);
		obj.put("timestamp", timestamp);
		obj.put("appId", appId);
		List<String> jsApiList = new ArrayList<>();
		jsApiList.add(Wechat.JSAPIList.chooseWXPay);
		obj.put("jsApiList", jsApiList);
		
		return obj;
	}
	
	@Override
	public String requestMapping(HttpServletRequest req, Long businessId) {
	    // TODO
//		获取商家的设置的活跃主题
		BusinessThemePo busiTheme = null;
		busiTheme = baseDao.get("from BusinessThemePo where businessId=? and actived=true", new Object[]{businessId});
		String themeName = "";
		// 如果活跃主题为空且不过期、则使用平台的默认主题
		if(busiTheme == null 
                || ( busiTheme.getVaildity() != -1 
		        && !Utils.isMemberVail(busiTheme.getStartAt(), busiTheme.getVaildity().longValue()))){
			ThemePo theme = baseDao.get("from ThemePo where defaulted=?", new Object[]{true});
			themeName = theme.getSign();
		}else{
			ThemePo theme = baseDao.get("from ThemePo where id=?", new Object[]{busiTheme.getThemeId()});
			themeName = theme.getSign();
		}
		
//		设置session参数appId
		BusinessInfoPo business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
		req.getSession().setAttribute("appId", business.getAuthorizerAppid());
		req.getSession().setAttribute("businessId", businessId);
//		返回匹配主题路径
		return themeName;
	}
	

    @Override
    public JSONObject pay(CustomerOrderVo orderVo, HttpServletRequest req) {
        /*
         * 1.准备订单信息
         */
        // 1.1 准备订单号
        String orderNum = orderVo.getOrderNum(), productInfo = "";
        // 
        CustomerInfoPo customerInfoPo = baseDao.findUniqueByProperty("customerId", orderVo.getCustomerId(), CustomerInfoPo.class);
        if (customerInfoPo == null) 
            throw new ServiceException("订单的customerId "+ orderVo.getCustomerId() + "错误 " );
        if (orderVo.getProductType().equals(ProductType.Traffic)) {
            TrafficPlanPo trafficPlanPo = baseDao.findUniqueByProperty("id", orderVo.getProductId(), TrafficPlanPo.class);
            productInfo = trafficPlanPo.getName();
        }
        BusinessInfoPo businessInfoPo = baseDao.findUniqueByProperty("businessId", orderVo.getBusinessId(), BusinessInfoPo.class);
        if (businessInfoPo == null) 
            throw new ServiceException("订单的businessId "+ orderVo.getBusinessId() + "错误 " );

        XPayConfig payConfig = getPayConfig(businessInfoPo, false);
        /*
         * 2.微信统一下单信息准备
         */
        // 3.1 appid
        String appid = payConfig.getAuthorizerAppid();
//        String appid = businessInfoPo.getAuthorizerAppid();
        // 3.2 body 商品摘要描述
        String body = productInfo;
        // 3.3 mch_id 平台微信支付id
        String mch_id = payConfig.getMch_id();
        // 3.4 nonce_str 随机串
        String nonce_str = WeChatUtils.generateRamdomChar();
        // 3.5 notify_url 支付回调url
        String notify_url = prop.get(Wechat.host) + prop.getProperty(Wechat.ComponentConfigs.payCallback);
//        String openid = null;
        // 3.7 out_trade_no 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
        String out_trade_no = orderNum;
        // 3.8 spbill_create_ip 客户端ip地址
        String spbill_create_ip = WeChatUtils.getRemoteAddr(req);
        // 3.9 total_fee Int 订单总金额，单位为分，详见支付金额 转换单位
        Long total_fee = orderVo.getRetailPrice().multiply(new BigDecimal("100")).longValue();
        // 3.10 trade_type 取值如下：JSAPI，NATIVE，APP
//        String trade_type = "JSAPI";
        String trade_type = businessInfoPo.getAuthorizerAppid().equals(appid)
                                || businessInfoPo.getUseBusinessPay()? "JSAPI" : "NATIVE";
        // 3.6 openid 用户
        String openid = "JSAPI".equals(trade_type) ? customerInfoPo.getOpenid() : null;
        String sign = "";
        XPayOrderVo xOrder = new XPayOrderVo(appid, body, mch_id, nonce_str, notify_url, openid,
            out_trade_no, spbill_create_ip, total_fee, trade_type, sign);
        String key = payConfig.getPayKey();

        // 3.11 sign 签名
        sign = WeChatUtils.generateSign(key, xOrder);
        xOrder.setSign(sign);
        /*
         * 4.调用微信统一下单API()
         */
        String requestXmlText = XmlRespMsgGenerator.generate(xOrder);
        LOG.debug(requestXmlText);
        JSONObject wechat_pay_params = null;
        try {
            URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setDoOutput(true);
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setRequestProperty("Content-Length", String.valueOf(requestXmlText.length()));
            conn.setRequestProperty("Content-Type", "text/xml;charset=utf-8");
            conn.connect();
            conn.getOutputStream().write(requestXmlText.getBytes("UTF-8"));
            Map<String, String> respMap = new HashMap<String, String>();
            if (conn.getResponseCode() == 200) {
                respMap = WeChatUtils.readXmlMapFromStream(conn.getInputStream());
                LOG.debug("respMap:{}",respMap);
                //  5. 返回前台需要的数据 如：prepay_id...
                if ("SUCCESS".equals(respMap.get(Wechat.XmlTags.return_code))
                    && "SUCCESS".equals(respMap.get(Wechat.XmlTags.result_code))) {
                    /*
                     * 准备支付签名参数: timestamp: 0, //
                     * 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                     * nonceStr: '', // 支付签名随机串，不长于 32 位 package: '', //
                     * 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***） signType: '', //
                     * 签名方式，默认为'SHA1'，使用新版支付需传入'MD5' paySign: '', // 支付签名
                     */
                    if (trade_type.equals("JSAPI")) {
                        Long timeStamp = System.currentTimeMillis();
                        String nonceStr = respMap.get(Wechat.XmlTags.nonce_str);
                        String packagee = Wechat.XmlTags.prepay_id + "="
                            + respMap.get(Wechat.XmlTags.prepay_id);
                        
                        WcPayUniformOrderRespBody wcPay = new WcPayUniformOrderRespBody(appid,
                            timeStamp, nonceStr, packagee);
                        // 参数签名
                        String paySign = WeChatUtils.generateSign(key, wcPay);
                        wcPay.setPaySign(paySign);
                        
                        wechat_pay_params = (JSONObject)JSON.toJSON(wcPay);
                    }else if (trade_type.equals("NATIVE")) {
                        wechat_pay_params = new JSONObject();
                        wechat_pay_params.put("code_url", respMap.get("code_url"));
                    }
                }
            }
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        LOG.debug("wechat_pay_params : {}",wechat_pay_params);
        return wechat_pay_params;
    }


    @Override
    public boolean payRefund(CustomerOrderVo orderVo) {

        BusinessInfoPo businessInfoPo = baseDao.findUniqueByProperty("businessId", orderVo.getBusinessId(), BusinessInfoPo.class);
        if (businessInfoPo == null) 
            throw new ServiceException("订单的businessId "+ orderVo.getBusinessId() + "错误 " );
        
        XPayConfig payConfig = getPayConfig(businessInfoPo, true);
        
        return payRefund(orderVo, payConfig);
    }

    @Override
    public XPayConfig getPayConfig(Long businessId, boolean isGetApliclient_cert) {
        BusinessInfoPo businessInfoPo = baseDao.findUniqueByProperty("businessId", businessId, BusinessInfoPo.class);
        if (businessInfoPo == null) 
            throw new ServiceException("订单的businessId "+ businessId + "错误 " );
     
        return getPayConfig(businessInfoPo, isGetApliclient_cert);
    }
        
    private boolean payRefund(CustomerOrderVo orderVo, XPayConfig payConfig) {
        
        /*
         * 1. 准备微信请求信息
         */
        // 1.1 appid
        String appid = payConfig.getAuthorizerAppid();
        // 1.2 mch_id 平台微信支付id
        String mch_id = payConfig.getMch_id();
        // 1.3 nonce_str 随机串
        String nonce_str = WeChatUtils.generateRamdomChar();
        // 1.4 out_trade_no 商户订单号
        String out_trade_no = orderVo.getOrderNum();
        // 1.5 商户退款单号   out_refund_no
        String out_refund_no = orderVo.getOrderNum();
        // 1.6 订单金额 total_fee
        Long total_fee = orderVo.getRetailPrice().multiply(new BigDecimal("100")).longValue();
        // 1.7 退款金额   refund_fee
        Long refund_fee = orderVo.getRetailPrice().multiply(new BigDecimal("100")).longValue();
        // 1.8 操作员    op_user_id
        String op_user_id = orderVo.getOrderNum();
        // 签名  sign 是   String(32)  签名，详见签名生成算法
        String sign = "";
        
        XPayOrderRefundVo refundVo = new XPayOrderRefundVo(appid, mch_id, nonce_str, out_trade_no,
            out_refund_no, total_fee, refund_fee, op_user_id, sign);
        String key = payConfig.getPayKey();

        // 2.11 sign 签名
        sign = WeChatUtils.generateSign(key, refundVo);
        refundVo.setSign(sign);
        /*
         * 3.调用微信退款API()
         */
        String requestXmlText = XmlRespMsgGenerator.generate(refundVo);
        LOG.debug(requestXmlText);
        
        try {
            KeyStore keyStore  = KeyStore.getInstance("PKCS12");
            InputStream instream = payConfig.getApiclient_cert();
            try {
                keyStore.load(instream, mch_id.toCharArray());
            } finally {
                instream.close();
            }
            // Trust own CA and all self-signed certs
            SSLContext sslcontext = SSLContexts.custom()
                    .loadKeyMaterial(keyStore, mch_id.toCharArray())
                    .build();
            // Allow TLSv1 protocol only
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[] { "TLSv1" },
                    null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            CloseableHttpClient httpclient = HttpClients.custom()
                    .setSSLSocketFactory(sslsf)
                    .build();
            

            HttpPost httppost = new HttpPost("https://api.mch.weixin.qq.com/secapi/pay/refund");
            InputStreamEntity reqEntity = new InputStreamEntity(
                new ByteArrayInputStream(requestXmlText.getBytes("UTF-8")), -1, ContentType.APPLICATION_OCTET_STREAM);
            reqEntity.setChunked(true);
            httppost.setEntity(reqEntity);
            // 发送请求
            CloseableHttpResponse response = httpclient.execute(httppost);
            String responseXmlText = EntityUtils.toString(response.getEntity());
            LOG.debug("responseXmlText->{}",responseXmlText);
            // 4.解析解密后的Xml信息
            Map<String, String> xmlMap = WeChatUtils.readXmlMapFromStream(new ByteArrayInputStream(responseXmlText.getBytes()));
            String return_code = xmlMap.get(Wechat.XmlTags.return_code);
            
            if ("SUCCESS".equalsIgnoreCase(return_code)) {

            } else {
                throw new ServiceException("微信退款失败:" + xmlMap.get("return_msg"));
            }
            
        }
        catch (Exception e) {
            throw new RuntimeException("微信退款系统错误",e);
        }
        return true;
    }
    
	/**
	 * 更新商家公众号信息，在首次授权成功或下次重新授权时调用
	 * @param businessId
	 * @param authCode
	 */
	private void updateBusinessInfo(Long businessId, String authCode){
		//1. 获取授权码，并用该授权码换取公众号的授权信息，如调用凭据，刷新凭据和授予权限接口。。
		/*
		 * http请求方式: POST（请使用https协议）
			https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token=xxxx
			POST数据示例:
			{
			"component_appid":"appid_value" ,
			"authorization_code": "auth_code_value"
		 */
		Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
		WechatToken cToken = (WechatToken) wechatCache.get("component_access_token");
		if(cToken==null){
			WechatToken tickey = (WechatToken) wechatCache.get("component_verify_tickey");
			if(tickey == null){
				throw new ServiceException("服务器忙，请您10分钟后再绑定");
			}
			cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
			wechatCache.put("component_access_token",cToken);
		}
		if(LOG.isDebugEnabled()) LOG.debug("component_access_token->{}",cToken);
		
        BusinessInfoPo businessInfo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
		// 获取公众号的接口调用凭据和授权信息
		HttpURLConnection conn = WeChatUtils.createPostConnection(WeChatInfoUrls.auth_code.url+cToken.getValue());
		try {
	        TokenReqBody reqBody = new TokenReqBody(prop.getProperty(Wechat.ComponentConfigs.appId), authCode);
			conn.connect();
			conn.getOutputStream().write(JSON.toJSONString(reqBody).getBytes());
			if(conn.getResponseCode()==200){
				InputStream in = conn.getInputStream();
				String respText = WeChatUtils.readTextFromStream(in, "utf-8");
//				System.out.println(respText);
				String jsonString = JSON.parseObject(respText).getJSONObject(Wechat.JSONKeys.authorization_info).toJSONString();
				BusinessAuthorizationInfo info = JSON.parseObject(jsonString, BusinessAuthorizationInfo.class);
//				System.out.println(info.toString());
				LOG.debug("授权信息获取成功 ->{}",respText);
				// 将公众号信息存储到数据库
				businessInfo.setAuthorizerAppid(info.getAuthorizer_appid());
				businessInfo.setAuthorizerAccessToken(info.getAuthorizer_access_token());
				businessInfo.setRefreshToken(info.getAuthorizer_refresh_token());
				businessInfo.setExpiresIn(info.getExpires_in()*1000L);
				businessInfo.setTokenBeginTime(new Date());
				businessInfo.setFuncInfo(info.getFunc_info());
				businessInfo.setAuthorizedTime(new Date());
				businessInfo.setIsAuthorized(true);
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
        // 获取公众号帐号基本信息
		String url = WeChatInfoUrls.api_get_authorizer_info.url+cToken.getValue();
        // 设置请求参数
        TokenReqBody reqBody = new TokenReqBody( prop.getProperty(Wechat.ComponentConfigs.appId));
        reqBody.setAuthorizer_appid(businessInfo.getAuthorizerAppid());
        LOG.debug("url->{},param->{}", url, JSON.toJSONString(reqBody));
		// 发送请求, 并获取返回参数
        String respText =  HttpRequest.sendPostJSON(url, JSON.toJSONString(reqBody));
        LOG.debug("获取公众号帐号基本信息 ->{}", respText);
        // 取出公众号
        String wechat_account = JSON.parseObject(respText)
            .getJSONObject("authorizer_info")
            .getString("nick_name");
        businessInfo.setWechatOfficAccount(wechat_account);
        // 将公众号信息存储到数据库
        businessInfo.setWechatInfo(respText);
        
        // 保存数据
        baseDao.update(businessInfo);
    }
	
	/**
	 * 根据消息类型处理该消息事件
	 * @param msgType
	 * @param map
	 * @return
	 * @throws Exception
	 */
	private String handleMsgType(String msgType, Map<String, String> map, String timeStamp, String nonce) throws Exception {
		RequestMessage msg = XmlRequestMsgMapper.map(RequestMessage.class, map);
		switch (msgType) {
			case Wechat.MsgTypes.text:
				return handleTextMessage(msg,timeStamp, nonce);
            case Wechat.MsgTypes.event:
                return handleEventMessage(msg, timeStamp, nonce);
			default:
				return "success";
		}
	}
	

    /**
     * 处理事件消息
     * @param msg
     * @return
     * @throws Exception
     */
    private String handleEventMessage(RequestMessage msg, String timeStamp, String nonce) throws Exception{
        String appId = msg.getToUserName();
        if (appId.equals("gh_3c884a361561")) { //  TODO 全网发布
            return defaultHander(msg, msg.getEvent() + "from_callback", timeStamp, nonce);
        }
        
        switch (msg.getEvent()) {
            case Wechat.EventTypes.CLICK:
            case Wechat.EventTypes.VIEW:
            case Wechat.EventTypes.LOCATION:
            case Wechat.EventTypes.subscribe:
            case Wechat.EventTypes.SCAN:
            case Wechat.EventTypes.unsubscribe:
            default:
                return "success";
        }

    }
	
	/**
	 * 处理文本内容消息
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	private String handleTextMessage(RequestMessage msg, String timeStamp, String nonce) throws Exception{
		String keyWord = msg.getContent();
		String appId = msg.getToUserName();
		WechatReplyPo _reply = null;
		if (appId.equals("gh_3c884a361561")) { //  TODO 全网发布
            _reply = new WechatReplyPo();
		    if ("TESTCOMPONENT_MSG_TYPE_TEXT".equals(keyWord)) {
		        _reply.setContent("TESTCOMPONENT_MSG_TYPE_TEXT_callback");
            }
		    else if (keyWord.contains("QUERY_AUTH_CODE:")) {
                String query_auth_code = keyWord.split(":")[1];
                //1. 获取授权码，并用该授权码换取公众号的授权信息，如调用凭据，刷新凭据和授予权限接口。。
                Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
                WechatToken cToken = (WechatToken) wechatCache.get("component_access_token");
                if(cToken==null){
                    WechatToken tickey = (WechatToken) wechatCache.get("component_verify_tickey");
                    if(tickey == null){
                        throw new ServiceException("服务器忙，请您10分钟后再绑定");
                    }
                    cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
                    wechatCache.put("component_access_token",cToken);
                }
                // 使用授权码换取公众号的授权信息
                String url = WeChatInfoUrls.auth_code.url+cToken.getValue();
                TokenReqBody reqBody = new TokenReqBody( prop.getProperty(Wechat.ComponentConfigs.appId), query_auth_code);
                // 发送请求, 并获取返回参数
                String respText =  HttpRequest.sendPost(url, JSON.toJSONString(reqBody));
                LOG.info("获取公众号授权信息 ->{}", respText);
                String ACCESS_TOKEN = JSON.parseObject(respText)
                    .getJSONObject("authorization_info").getString("authorizer_access_token");
                
                // 客服消息模板
                url = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=" + ACCESS_TOKEN;
                String temple = "{\"touser\":\"OPENID\",\"msgtype\":\"text\",\"text\":{\"content\":\"CONTENT\"}}";
                String msgstring = temple.replace("OPENID", msg.getFromUserName())
                                       .replace("CONTENT", query_auth_code + "_from_api");
                // 发送客服消息api回复文本消息给粉丝
                HttpRequest.sendPost(url, msgstring);
                // 
                _reply.setContent("");
            }
        }
		else {
		    BusinessInfoPo businessInfo = baseDao.get("from BusinessInfoPo where authorizerAppid=?", new Object[]{appId});
		    _reply = baseDao.get("from WechatReplyPo where businessId=? and keyWord=?", new Object[]{businessInfo.getBusinessId(), keyWord});
        }
		if(_reply == null){
			return "";
		}else{
			return defaultHander(msg, _reply.getContent(),timeStamp, nonce);
		}
	}
	
	/**
	 * 默认处理方法
	 * @param msg
	 * @param content
	 * @return
	 */
	private String defaultHander(RequestMessage msg, String content, String timeStamp, String nonce){
		
		ResponseMessage respMsg = new ResponseMessage();
		respMsg.setCreateTime(new Date().getTime());
		respMsg.setFromUserName(msg.getToUserName());
		respMsg.setToUserName(msg.getFromUserName());
		respMsg.setMsgType("text");
		respMsg.setContent(content);
		
		String token = (String) prop.get(Wechat.ComponentConfigs.token);
		String encodingAesKey = (String) prop.get(Wechat.ComponentConfigs.encodingAESKey);
		String appId = (String) prop.get(Wechat.ComponentConfigs.appId);
		WXBizMsgCrypt pc;
		String encryptMsg = "success";
		try {
			pc = new WXBizMsgCrypt(token, encodingAesKey, appId);
			encryptMsg = pc.encryptMsg(XmlRespMsgGenerator.generate(respMsg), timeStamp, nonce);
		} catch (AesException e) {
			e.printStackTrace();
		}
		
		return encryptMsg;
	}

    /**
     * 获取 component_access_token
     * @return
     */
    private WechatToken getComponent_access_token() {
        //1. 校验平台调用凭据是否过期
       Cache<Object, Object> cache = cacheManager.getCache("wechatCache");
       WechatToken cToken = (WechatToken) cache.get("component_access_token");
       if(cToken==null){
           WechatToken tickey = (WechatToken) cache.get("component_verify_tickey");
           if(tickey == null){
               throw new ServiceException("服务器忙，请您10分钟再操作");
           }
           cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
           cache.put("component_access_token", cToken);
       }
       return cToken;
    }

    private XPayConfig getPayConfig(BusinessInfoPo businessInfoPo, boolean isGetApliclient_cert) {
        if (businessInfoPo.getUseBusinessPay()) {
            XPayConfig payConfig = mapper.map(businessInfoPo, XPayConfig.class);
            if (isGetApliclient_cert) {
                UploadFilePo uploadfilePo = baseDao.findUniqueByProperty("uploadId", payConfig.getApiclient_cert_id(),UploadFilePo.class);
                File file = FileUtils.getResourceFile(uploadfilePo.getUrl());
                try {
                    LOG.debug(file);
                    payConfig.setApiclient_cert(new FileInputStream(file));
                }
                catch (FileNotFoundException e) {
                    new RuntimeException("没有找到 apiclient_cert文件");
                }
            }
            return payConfig;
        } else {
            return deaultPayconfig();
        }
    }
    
    private XPayConfig deaultPayconfig() {

        try {
            return new XPayConfig(false, 
                prop.getProperty(Wechat.ComponentConfigs.payId),
                prop.getProperty(Wechat.ComponentConfigs.mchId),
                prop.getProperty(Wechat.ComponentConfigs.payKey),
                new FileInputStream(WechatConfigs.apiclient_cert));
        }
        catch (FileNotFoundException e) {
            new RuntimeException("没有找到 apiclient_cert文件");
        }
        return null;
    }


}
