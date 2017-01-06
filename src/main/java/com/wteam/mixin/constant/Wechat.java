package com.wteam.mixin.constant;

/**
 * 微信各种消息类型，包括XML信息的标签(XmlTags),JSON消息的Key值(JSONKeys),推送的信息类型(InfoTypes),平台自己的配置名称(ComponentConfigs),
 * 	各种调用凭据类型(TokenTypes), 用户与公众号交互的信息类型(MsgTypes),事件类型(EventTypes),
 *  JSAPI接口名称(JSAPIList)
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月4日
 */
public interface Wechat {

    /**主机网址*/
    static final String host = "host";
    
	interface InfoTypes {
		/**推送tickey类型*/
		static final String component_verify_ticket = "component_verify_ticket";
		/**推送授权类型*/
		static final String authorized = "authorized";
		/**推送取消授权类型*/
		static final String unauthorized = "unauthorized";
		/**推送授权更新类型*/
		static final String updateauthorized = "updateauthorized";
	}
	
	interface XmlTags {
		/**平台Id标签*/
		static final String AppId = "AppId";
		/**加密标签*/
		static final String Encrypt = "Encrypt";
		/**信息类型表片*/
		static final String InfoType = "InfoType";
		/**推送component_verify_tickey时的Tickey标签*/
		static final String ComponentVerifyTicket = "ComponentVerifyTicket";
		/**推送授权信息时的 公众号Id*/
		static final String AuthorizerAppid ="AuthorizerAppid";
		/**用于标记消息类型，用户与公众号交互时产生*/
		static final String MsgType = "MsgType";
		/**用于标记消息事件类型，用户与公众号交互时产生*/
		static final String Event = "Event";
		/**用于标记支付请求是否成功，用户发起微信支付时产生*/
		static final String return_code = "return_code";
		/**用于标记支付结果是否成功，用户发起微信支付时产生*/
		static final String result_code = "result_code";
		/**与微信服务器交互时，服务器返回的随机串, 支付签名时会用到*/
		static final String nonce_str = "nonce_str";
		/**微信支付同意下单后返回的字段，用于支付签名，和前台发起支付请求使用*/
		static final String prepay_id = "prepay_id";
        /**商户订单号*/
        static final String out_trade_no = "out_trade_no";
        /**商户订单号*/
        static final String transaction_id = "transaction_id";
	}
	
	interface ComponentConfigs {
		/**平台AppId*/
		static final String appId = "appId";
		/**平台App安全码*/
		static final String appSecret = "appSecret";
		/**平台消息加解密key*/
		static final String encodingAESKey = "encodingAESKey";
		/**平台签名所用token*/
		static final String token = "token";
		/**平台微信支付商户账号*/
		static final String mchId = "mchId";
		/**平台微信支付商户支付key值*/
		static final String payKey = "payKey";
		/**微信支付公众号id*/
		static final String payId = "payId";
		/**平台微信支付回调接口*/
		static final String payCallback = "payCallback";
		/**与平台绑定的公众号的自定义菜单url地址*/
		static final String customer_menu_url = "customer_menu_url";
		/**用户授权登录成功回调URL*/
		static final String auth_login_success_callback_url = "auth_login_success_callback_url";
	}
	
	interface TokenTypes {
		/**平台验证Tickey,由微信服务器每10分钟推送一次，用于获取component_access_token*/
		static final String component_verify_ticket = "component_verify_ticket";
		/**平台接口调用凭据,用于调用平台API*/
		static final String component_access_token = "component_access_token";
		/**预授权码,用于公众号授权*/
		static final String pre_auth_code = "pre_auth_code";
		/**授权码, 用于获取authorizer_access_token和authorizer_refresh_token*/
		static final String auth_code = "auth_code";
		/**公众号调用凭据, 用于平台替公众号处理业务的API调用凭据*/
		static final String authorizer_access_token = "authorizer_access_token";
//		static final String authorization_info = "authorization_info";
	}
	
	interface JSONKeys {
		/**标记该返回字段是component_access_token，在进行 component_access_token 获取和刷新时返回*/
		static final String component_access_token = "component_access_token";
		/**标记该返回字段是pre_auth_code，在进行 pre_auth_code 获取时返回*/
		static final String pre_auth_code = "pre_auth_code";
        /**公众号帐号基本信息*/
        static final String authorizer_info = "authorizer_info";
		/**用于标记各个token的有效期，单位s，随各个token一起返回*/
		static final String expires_in = "expires_in";
		/**用于标记公众号信息，在用户授权成功时首次获取authorizer_access_token时一起返回;或者获取公众号信息时返回*/
		static final String authorization_info = "authorization_info";
		/**用于标记公众号所授权的权限,获取公众号信息时返回 */
		static final String funcscope_category = "funcscope_category";
		/**用于标记公众号所授权的权限,获取公众号信息时返回 */
		static final String id = "id";
		/**用于标记公众号的调用凭据, 更新该调用凭据时返回*/
		static final String authorizer_access_token = "authorizer_access_token";
		/**用于标记公众号的刷新凭据, 更新公众号调用凭据时返回*/
		static final String authorizer_refresh_token = "authorizer_refresh_token";
	
		/** 当代替公众号调用api时，微信服务器返回的操作结果码，详见官网*/
		static final String errcode = "errcode";
	}
	
	interface URLParameters {
		/**用于原封不动返回给微信服务器,首次验证开发者url时返回*/
		static final String echostr = "echostr";
		/**首次验证开发者url时用于验证签名*/
		static final String signature = "signature";
		/**用于解密消息 ,与微信服务器进行交互时返回*/
		static final String msg_signature = "msg_signature";
		/**用于加解密消息 ,与微信服务器进行交互时返回**/
		static final String timestamp = "timestamp";
		/**用于加解密消息 ,与微信服务器进行交互时返回*/
		static final String nonce = "nonce";
		/**用于获取公众号authorizer_access_token和authorizer_refresh_token, 用户授权成功时通过url参数形式返回，注意及时获取*/
		static final String auth_code = "auth_code";
	}
	
	interface MsgTypes {
		/**用户发送的消息类型为：图片*/
		static final String image = "image";
		/**用户发送的消息类型为：链接，从收藏中发起有效*/
		static final String link = "link";
		/**用户发送的消息类型为：位置信息*/
		static final String location = "location";
		/**用户发送的消息类型为：短视频*/
		static final String shortvideo = "shortvideo";
		/**用户发送的消息类型为：文字*/
		static final String text = "text";
		/**用户发送的消息类型为：视频，暂时没发现发起方式*/
		static final String video = "video";
		/**用户发送的消息类型为：声音，貌似该类型无法响应*/
		static final String voice = "voice";
		/**用户发送的消息类型为：事件*/
		static final String event = "event";
	}
	
	interface EventTypes {
		/**用户发送的消息事件类型为：点击菜单拉取信息*/
		static final String CLICK = "CLICK";
		/**用户发送的消息事件类型为：从菜单中跳转到某个url*/
		static final String VIEW = "VIEW";
		/**用户发送的消息事件类型为：上报地理位置信息*/
		static final String LOCATION = "LOCATION";
		/**用户发送的消息事件类型为：关注*/
		static final String subscribe = "subscribe";
		/**用户发送的消息事件类型为：扫描二维码*/
		static final String SCAN = "SCAN";
		/**用户发送的消息事件类型为：取消关注*/
		static final String unsubscribe = "unsubscribe";
	}
	
	interface JSAPIList{
		/*
		 * onMenuShareTimeline
			onMenuShareAppMessage
			onMenuShareQQ
			onMenuShareWeibo
			onMenuShareQZone
			startRecord
			stopRecord
			onVoiceRecordEnd
			playVoice
			pauseVoice
			stopVoice
			onVoicePlayEnd
			uploadVoice
			downloadVoice
			chooseImage
			previewImage
			uploadImage
			downloadImage
			translateVoice
			getNetworkType
			openLocation
			getLocation
			hideOptionMenu
			showOptionMenu
			hideMenuItems
			showMenuItems
			hideAllNonBaseMenuItem
			showAllNonBaseMenuItem
			closeWindow
			scanQRCode
			chooseWXPay
			openProductSpecificView
			addCard
			chooseCard
			openCard
		 */
		
		/**网页发起微信支付接口*/
		static final String chooseWXPay = "chooseWXPay";
	}
}











