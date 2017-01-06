package com.wteam.mixin.biz.controler;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.biz.service.IWechatService;
import com.wteam.mixin.biz.service.impl.WechatServiceImpl;
import com.wteam.mixin.constant.WechatConfigs;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.constant.WeChatInfoUrls;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.wexin.TokenReqBody;
import com.wteam.mixin.model.wexin.WcPayUniformOrderRespBody;
import com.wteam.mixin.model.wexin.WechatToken;
import com.wteam.mixin.model.wexin.XPayOrderVo;
import com.wteam.mixin.shiro.CustomUsernamePasswordToken;
import com.wteam.mixin.utils.WeChatUtils;
import com.wteam.mixin.utils.XmlRespMsgGenerator;

@Controller
@RequestMapping("/wechat")
public class WechatController
{
	@Autowired
	private IWechatService wechatService; 
	@Autowired
	ILoginService loginService;
	@Autowired
	IUserService userService;
	
	@Autowired
	private CacheManager cacheManager;
	
	 /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(WechatServiceImpl.class.getName());

    private static Properties prop = WechatConfigs.prop;
	
//	/**
//	 * 首次认证的签名处理，用于开发者认证，签名所用
//	 * @param req
//	 * @param resp
//	 * @return
//	 */
//	@ResponseBody()
//	@RequestMapping(method={RequestMethod.GET})
//	public String checkSignature(HttpServletRequest req, HttpServletResponse resp){
//		try {
//			req.setCharacterEncoding("UTF-8");
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		}
//		resp.setCharacterEncoding("UTF-8");
//		resp.setContentType("text/xml;charset=UTF-8");
//		
//		String timestamp = req.getParameter(Wechat.URLParameters.timestamp);
//		String nonce = req.getParameter(Wechat.URLParameters.nonce);
//		String signature = req.getParameter(Wechat.URLParameters.signature);
//		String echostr = req.getParameter(Wechat.URLParameters.echostr);
//		
//		return WeChatUtils.checkSignature("", timestamp, nonce, signature)?echostr:"";
//	}
	
	/**
	 * 接收微信服务器 与授权相关的事件推送：对平台ticket推送、公众号授权与取消授权事件推送进行处理
	 * @param req
	 * @return
	 */
	@RequestMapping(value="/auth",method={RequestMethod.POST})
	public void authEventHandle(HttpServletRequest req, HttpServletResponse response){
		String appId = prop.getProperty(Wechat.ComponentConfigs.appId);
		String encodingAESKey = prop.getProperty(Wechat.ComponentConfigs.encodingAESKey);
		String token = prop.getProperty(Wechat.ComponentConfigs.token);
		
		try {
			req.setCharacterEncoding("UTF-8");
//			1.将微信服务器的推送流解析成Map型数据结构
			Map<String, String> xmlMap = WeChatUtils.readXmlMapFromStream(req.getInputStream());
//			2.生成腾讯提供的微信加解密类WXBizMsgCrypt
			WXBizMsgCrypt pc = new WXBizMsgCrypt(token, encodingAESKey, appId);
//			3.解密得到的加密字段Encrypt
			String decryptMsg = pc.decrypt(xmlMap.get(Wechat.XmlTags.Encrypt));
//			System.out.println(decryptMsg);
			LOG.info(decryptMsg);
//			4.解析解密后的Xml信息
			Map<String, String> map = WeChatUtils.readXmlMapFromStream(new ByteArrayInputStream(decryptMsg.getBytes()));
//			5.获取推送事件类型
			String infoType = map.get(Wechat.XmlTags.InfoType);
			switch (infoType) {
//				5.1 ticket更新事件推送
				case Wechat.InfoTypes.component_verify_ticket:
					wechatService.ticketFresh(map);
					break;
//				5.2 公众号授权事件推送	
				case Wechat.InfoTypes.authorized:
					wechatService.auth(map);
					break;
//				5.3 公众号取消授权事件推送	
				case Wechat.InfoTypes.unauthorized:
					wechatService.unAuth(map);
					break;
//				5.4 公众号重新发起授权推送	
				case Wechat.InfoTypes.updateauthorized:
					wechatService.reAuth(map);
					break;
	
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
            response.getWriter().write("success");
            response.getWriter().flush();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	/**
	 * 处理商家发起公众号绑定请求
	 * @param resp
	 */
	@RequestMapping(value="/bind",method={RequestMethod.GET})
	public void bind(HttpServletResponse resp){
		
		Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
		WechatToken cToken = (WechatToken) wechatCache.get("component_access_token");
		if(cToken==null){
			WechatToken tickey = (WechatToken) wechatCache.get("component_verify_tickey");
			if(tickey == null){
				throw new ServiceException("服务器忙，请您10分钟后再绑定");
			}
			cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
			wechatCache.put("component_access_token", cToken);
		}
		//1. 获取预授权码
		/*
		 * pre——auth——code: 预授权码获取路径：
		 * http请求方式: POST（请使用https协议）
			https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token=xxx
			POST数据示例:
			{
			"component_appid":"appid_value" 
			}
		 */
		HttpURLConnection conn = WeChatUtils.createPostConnection(WeChatInfoUrls.pre_auth_code.url+cToken.getValue());
		try {
			String component_appid = prop.getProperty(Wechat.ComponentConfigs.appId);
			TokenReqBody reqBody = new TokenReqBody(component_appid);
			conn.connect();
			conn.getOutputStream().write(JSON.toJSONString(reqBody).getBytes());
			if(conn.getResponseCode()==200){
				InputStream in = conn.getInputStream();
				String respText = WeChatUtils.readTextFromStream(in, "utf-8");
//				System.out.println(respText);
				LOG.info(respText);
				JSONObject obj = JSON.parseObject(respText);
				String preAuthCode = (String) obj.get(Wechat.JSONKeys.pre_auth_code);
				//2. 跳转至微信授权页面
//				https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid=xxxx&pre_auth_code=xxxxx&redirect_uri=xxxx
				resp.sendRedirect("https://mp.weixin.qq.com/cgi-bin/componentloginpage?component_appid="+component_appid
						+"&pre_auth_code="+preAuthCode
						+"&redirect_uri=" + prop.get(Wechat.host) + "/wechat/bindSuccess");
				return ;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 公众号绑定成功回调接口
	 * @param req
	 * @return
	 */
	@RequestMapping("/bindSuccess")
	public void bindSuccess(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			, HttpServletRequest req
			, HttpServletResponse resp){
		
//		System.out.println("授权成功,准备获取公众号数据");
		LOG.info("授权成功,准备获取公众号数据");
		String tmpCode = req.getParameter(Wechat.URLParameters.auth_code);
		String auth_code = tmpCode.substring(tmpCode.lastIndexOf("@")+1);
		
		
		//在此处处理授权成功后的动作，例如跳转页面。。
		String bind = "";
		try {

	        // TODO
		    if (user != null) {
		        wechatService.bindSuccess(user.getUserId(), auth_code);
            }
		    bind = "success";
		} catch (Exception e) {
			LOG.error("绑定失败",e);
            bind = "failure";
		}
		String url = String.format("%s%s?bind=%s", prop.get(Wechat.host), "/wechatInfo.html",bind);
        try {
            resp.sendRedirect(url);
        }
        catch (IOException e1) {
            throw new ServiceException("重定向失败：" + url);
        }
	}
	
	/**
	 * 处理商家公众号的消息事件
	 * @param req
	 * @param resp
	 * @return
	 */
	@RequestMapping("/{appId}/callback")
	public void callback(@PathVariable("appId") String appId, HttpServletRequest req, HttpServletResponse resp){
//		System.out.println("callback method run &&& appId is " + appId);
		LOG.info("callback method run &&& appId is " + appId);
		try {
			req.setCharacterEncoding("UTF-8");
			resp.setCharacterEncoding("UTF-8");
			
			String respText = wechatService.handleRequest(req);
			resp.getWriter().write(respText);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * 用户手动授权登录
//	 */
//	@RequestMapping("/{appId}/authLogin")
//	public void authLogin(@PathVariable("appId") String appId, HttpServletRequest req, HttpServletResponse resp){
//		
//		String state = String.valueOf(new Random().nextInt(Integer.MAX_VALUE)) + req.getRequestedSessionId();
//		
//		String location;
//		try {
//			location = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+appId
//					+ "&redirect_uri="+URLEncoder.encode("http://mixinwang.com/wechat/authLoginSuccess", "UTF-8")
//					+ "&response_type=code&scope=snsapi_userinfo&state="+state
//					+ "&connect_redirect="+new Random().nextInt()
//					+ "&component_appid="+prop.getProperty(Wechat.ComponentConfigs.appId)+"#wechat_redirect";
//			System.out.println(location);
//			
//			resp.sendRedirect(location);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	/**
	 * 授权登录成功  微信服务器回调方法
	 */
	@RequestMapping("/authLoginSuccess")
	public void authLoginSuccess(@RequestParam("url") String url,
	                             @RequestParam("sessionId") String sessionId,
	                             HttpServletRequest req, HttpServletResponse resp){
	    
	    Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
	    
//		String openId = (String) req.getSession().getAttribute("openId");
        String openId = (String)wechatCache.get(sessionId+"_openId");
        url = url.replace(",", "&");
		//1. 防微信服务器 重复调用该回调 导致 重复用同一个code换取用户信息而报异常
        LOG.debug("sessionId->{}, openid->{}, url->{}",sessionId, openId, url);
		if(openId != null){
			//请求重定向回授权登录的网页
	        try {
                resp.sendRedirect(url);
            }
            catch (IOException e) {
                LOG.error("",e);
            }
	        return;
		}
		
//		获取用户openid
		String appId = req.getParameter("appid");
		String code = req.getParameter("code");
        LOG.debug("appId->{}, code->{}",appId, code);
		String openid = wechatService.authLoginSuccess(appId, code);
        LOG.debug("sessionId->{}, openid->{}",sessionId, openid);
		if(!"".equals(openid)){
//			授权登录
//			req.getSession().setAttribute("openId", openid);
		    wechatCache.put(sessionId+"_openId", openid);
		}
		//请求重定向回授权登录的网页
		try {
            resp.sendRedirect(url);
        }
        catch (IOException e) {
            LOG.error("",e);
        }
	}
	
	/**
	 * 公众号自定义菜单的链接匹配
	 * 微信公众号网页授权
	 * @param appId
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/theme")
	public ModelAndView customerRequestMapper(@RequestParam("businessId") Long businessId
			, @RequestParam("menu") String menu
			, HttpServletRequest req
			, HttpServletResponse resp){

	    // 根据会话ID从缓存中获取openId
        Cache<Object, Object> wechatCache = cacheManager.getCache("wechatCache");
        String sessionId = (String) req.getSession(true).getId();
//        String openId = (String) req.getSession().getAttribute("openId");
        String openId = (String)wechatCache.get(sessionId+"_openId"); // 从缓存中获取openId
        openId = openId != null ? openId : (String) req.getSession().getAttribute("openId"); // 如果没有,从会话中获取openId

        LOG.debug("sessionId->{}, openid->{}, businessId->{}, menu->{}",sessionId, openId, businessId, menu);
//		System.out.println("cutomer request mapping ------appId: "+ appId);
        // 获取商家信息
        BusinessInfoVo infoVo = userService.businessInfo(businessId);
		if(openId == null){ // 缓存中没有openId
			//未登录,进行登录授权
	        String url  = String.format("/wechat/theme/?businessId=%d,menu=%s", businessId,menu); // 不能使用"&"为url的参数分隔符，使用","代替
	        String state = String.valueOf(new Random().nextInt(Integer.MAX_VALUE)) + req.getRequestedSessionId();
			String location;
			String redirect_uri = prop.get(Wechat.host) + prop.getProperty(Wechat.ComponentConfigs.auth_login_success_callback_url);
			// 给授权回调提供显示页面url和当前的会话ID
			redirect_uri = String.format("%s?url=%s&sessionId=%s", redirect_uri,url,sessionId);
			try {
				location = "https://open.weixin.qq.com/connect/oauth2/authorize?appid="+infoVo.getAuthorizerAppid()
						+ "&redirect_uri="+URLEncoder.encode(redirect_uri, "UTF-8")
						+ "&response_type=code&scope=snsapi_userinfo&state="+state
						+ "&component_appid="+prop.getProperty(Wechat.ComponentConfigs.appId)+"#wechat_redirect";
				resp.sendRedirect(location);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}else{
//			已登录返回匹配页面

	        // 登录shiro框架
	        Subject subject = SecurityUtils.getSubject();
	        if (!subject.isAuthenticated()) {
	            CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
	                openId,
	                openId,
	                CustomUsernamePasswordToken.LOGIN_WECHAT_AUTO);
	            subject.login(token);
            }
		    req.getSession().setAttribute("openId",openId);

		    // 进行主题匹配
	        String sign = wechatService.requestMapping(req, businessId);
	        String url = String.format("/customer/%s/index", sign);
	        ModelAndView mav = new ModelAndView();
	        mav.setViewName(url);
	        mav.addObject(menu);
			
			return mav;
		}
	}

    /**
     * 公众号自定义菜单的链接匹配
     * 微信公众号网页授权
     * @param appId
     * @param req
     * @param resp
     */
    @RequestMapping("/customer/index")
    public ModelAndView customerTheme(@RequestParam("businessId") Long businessId,
                                      @RequestParam("menu") String menu,
                                HttpServletRequest req,
                                HttpServletResponse resp){
        
        String sign = wechatService.requestMapping(req, businessId);

        String url = String.format("/customer/%s/index", sign);
        ModelAndView mav = new ModelAndView();
        mav.setViewName(url);
        mav.addObject(menu);
        
        return mav;
    }
	

    
    /**
     * 页面调用微信支付jsapi前的配置注入，如说明将会调用那些接口
     * @param beConfigedUrl
     * @param req
     * @param resp
     */
    @RequestMapping("/setupJSapiConfig")
    @ResponseBody
    public Object setupJSapiConfig(@RequestParam("url") String beConfigedUrl, HttpServletRequest req
            , HttpServletResponse resp){
        
//      System.out.println("js api config setup --");
        LOG.info("js api config setup --");
        String appId = (String) req.getSession().getAttribute("appId");
//      System.out.println("appId" + appId);
//      System.out.println(obj.toJSONString());
        Object respJson = wechatService.setupJsapiConfig(appId, beConfigedUrl);

        return respJson;
    }
    
	/**
	 * 登录验证，自动授权，用户无感知
	 * @param url
	 * @param req
	 * @param resp
	 */
	@RequestMapping("/checkLogin")
	public void checkLogin(@RequestParam("url") String url, HttpServletRequest req, HttpServletResponse resp){
////		Cookie[] cookies = req.getCookies();
////		for (int i = 0; i < cookies.length; i++) {
////			System.out.println("check login method ------cookie :" + cookies[i].getName() + "" + cookies[i].getValue());
////		}
//		System.out.println("check login method ------url :" + url);
//		String openId = (String) req.getSession().getAttribute("openId");
////		System.out.println("openId: " + openId);
//		else{
//			//已经登录，返回 “1”,前台将将根据该值 确定是否进行授权页面的跳转
//			try {
//				resp.getWriter().write("1");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
	}
	
	/**
	 * 用户发起支付动作，在此调用微信统一下单api，并返回前台支付所需参数，如prepay_id
	 * @param req
	 * @param resp
	 */
//    @RequestMapping("/pay")
    public void pay(HttpServletRequest req, HttpServletResponse resp) {

        System.out.println("pay method--");
        /*
         * 1.准备订单信息
         */
        // 1.1 准备订单号
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String tmpString = WeChatUtils.generateRamdomChar();
        String messageDigest = WeChatUtils.messageDigest("MD5", tmpString);
        String orderNum = timestamp + messageDigest.substring(0, 18);
        /*
         * 2.微信统一下单信息准备
         */
        // TODO 3.1 appid
        String appid = prop.getProperty(Wechat.ComponentConfigs.payId);
        // 3.2 body 商品摘要描述
        String body = "支付测试";
        // 3.3 mch_id 平台微信支付id
        String mch_id = prop.getProperty(Wechat.ComponentConfigs.mchId);
        // 3.4 nonce_str 随机串
        String nonce_str = WeChatUtils.generateRamdomChar();
        // 3.5 notify_url 支付回调url
        String notify_url = prop.get(Wechat.host) + prop.getProperty(Wechat.ComponentConfigs.payCallback);
        // 3.6 openid 用户
        String openid = "ofdM-wL3bXidRa796sHDjtURQBb0";
        // 3.7 out_trade_no 商户系统内部的订单号,32个字符内、可包含字母, 其他说明见商户订单号
        String out_trade_no = orderNum;
        // 3.8 spbill_create_ip 客户端ip地址
        String spbill_create_ip = WeChatUtils.getRemoteAddr(req);
        // 3.9 total_fee Int 订单总金额，单位为分，详见支付金额
        Long total_fee = 7L;
        // 3.10 trade_type 取值如下：JSAPI，NATIVE，APP
        String trade_type = "JSAPI";

        String sign = "";
        XPayOrderVo xOrder = new XPayOrderVo(appid, body, mch_id, nonce_str, notify_url, openid,
            out_trade_no, spbill_create_ip, total_fee, trade_type, sign);
        String key = prop.getProperty(Wechat.ComponentConfigs.payKey);

        // 3.11 sign 签名
        sign = WeChatUtils.generateSign(key, xOrder);
        xOrder.setSign(sign);
        /*
         * 4.调用微信统一下单API()
         */
        String requestXmlText = XmlRespMsgGenerator.generate(xOrder);
        System.out.println(requestXmlText);
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
                // TODO 5. 返回前台需要的数据 如：prepay_id...
                if ("SUCCESS".equals(respMap.get(Wechat.XmlTags.return_code))
                    && "SUCCESS".equals(respMap.get(Wechat.XmlTags.result_code))) {
                    /*
                     * 准备支付签名参数: timestamp: 0, //
                     * 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
                     * nonceStr: '', // 支付签名随机串，不长于 32 位 package: '', //
                     * 统一支付接口返回的prepay_id参数值，提交格式如：prepay_id=***） signType: '', //
                     * 签名方式，默认为'SHA1'，使用新版支付需传入'MD5' paySign: '', // 支付签名
                     */
                    Long timeStamp = System.currentTimeMillis();
                    String nonceStr = respMap.get(Wechat.XmlTags.nonce_str);
                    String packagee = Wechat.XmlTags.prepay_id + "="
                                      + respMap.get(Wechat.XmlTags.prepay_id);

                    WcPayUniformOrderRespBody wcPay = new WcPayUniformOrderRespBody(appid,
                        timeStamp, nonceStr, packagee);
                    // 参数签名
                    String paySign = WeChatUtils.generateSign(key, wcPay);
                    wcPay.setPaySign(paySign);
                    resp.getWriter().write(JSON.toJSONString(wcPay));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
	
//	@RequestMapping("/pay/toPay")
	public ModelAndView toPayPage(){
		
		return new ModelAndView("jsp/pay");
	}
}






