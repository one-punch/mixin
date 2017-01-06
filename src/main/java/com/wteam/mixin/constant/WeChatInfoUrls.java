package com.wteam.mixin.constant;

public enum WeChatInfoUrls {
    
    /**在公众号第三方平台创建审核通过后，微信服务器会向其“授权事件接收URL”每隔10分钟定时推送component_verify_ticket。*/
    component_verify_ticket(""),
    /**第三方平台compoment_access_token是第三方平台的下文中接口的调用凭据，也叫做令牌*/
    component_access_token("https://api.weixin.qq.com/cgi-bin/component/api_component_token"),
    /**该API用于获取预授权码。预授权码用于公众号授权时的第三方平台方安全验证。*/
    pre_auth_code("https://api.weixin.qq.com/cgi-bin/component/api_create_preauthcode?component_access_token="),
    /**使用授权码换取公众号的接口调用凭据和授权信息*/
    auth_code("https://api.weixin.qq.com/cgi-bin/component/api_query_auth?component_access_token="),
    /**获取授权方的公众号帐号基本信息
     * {
        "component_appid":"appid_value" ,
        "authorizer_appid": "auth_appid_value" 
       }
     */
    api_get_authorizer_info("https://api.weixin.qq.com/cgi-bin/component/api_get_authorizer_info?component_access_token="),
    /**获取（刷新）授权公众号的接口调用凭据（令牌）*/
    authorizer_access_token("https://api.weixin.qq.com/cgi-bin/component/api_authorizer_token?component_access_token="),
	/**微信统一下单地url*/
	wc_pay_unifiedorder("https://api.mch.weixin.qq.com/pay/unifiedorder"),
	/**创建菜单接口*/
	custom_menu("https://api.weixin.qq.com/cgi-bin/menu/create?access_token="),
	/**消息群发接口*/
	send_mass_message("https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token=")
	;
	
    /**获取token的请求路径*/
    public final String url;

    private WeChatInfoUrls(String url) {
        this.url = url;
    }

}
