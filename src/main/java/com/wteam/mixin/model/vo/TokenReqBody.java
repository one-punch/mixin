package com.wteam.mixin.model.vo;
/*
 * 获取component_access_token的请求体实体
 */
public class TokenReqBody {
	
	/*
	 * component_access_token所需参数：
		 * component_appid
		 * component_appsecret
		 * component_verify_ticket
	 */
	String component_appid;
	String component_appsecret;
	String component_verify_ticket;
	/*
	 * 首次获取authorization_access_token所需参数：
		 * component_appid
		 * authorization_code
		 * 
	 */
	String authorization_code;
	
	/*
	 * pre_auth_code所需参数：
		 * component_appid
	 */
	
	/*
	 * 刷新authorization_access_token所需参数：
		 * component_appid
		 * authorizer_appid
		 * authorizer_refresh_token
	 */
	String authorizer_appid;
	
	String authorizer_refresh_token;
	
	public TokenReqBody() {	}
	
	public TokenReqBody(String component_appid, String appSecret, String tickey) {
		this.component_appid = component_appid;
		this.component_appsecret = appSecret;
		this.component_verify_ticket = tickey;
	}
	
	public TokenReqBody(String component_appid, String authorization_code) {
		this.component_appid = component_appid;
		this.authorization_code = authorization_code;
	}
	
	public TokenReqBody(String component_appid) {
		this.component_appid = component_appid;
	}
	
	public String getAuthorizer_appid() {
		return authorizer_appid;
	}

	public void setAuthorizer_appid(String authorizer_appid) {
		this.authorizer_appid = authorizer_appid;
	}

	public String getAuthorizer_refresh_token() {
		return authorizer_refresh_token;
	}

	public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
		this.authorizer_refresh_token = authorizer_refresh_token;
	}

	public String getAuthorization_code() {
		return authorization_code;
	}

	public void setAuthorization_code(String authorization_code) {
		this.authorization_code = authorization_code;
	}

	public String getComponent_appid() {
		return component_appid;
	}

	public void setComponent_appid(String component_appid) {
		this.component_appid = component_appid;
	}

	public String getComponent_appsecret() {
		return component_appsecret;
	}

	public void setComponent_appsecret(String component_appsecret) {
		this.component_appsecret = component_appsecret;
	}

	public String getComponent_verify_ticket() {
		return component_verify_ticket;
	}

	public void setComponent_verify_ticket(String component_verify_ticket) {
		this.component_verify_ticket = component_verify_ticket;
	}

	@Override
	public String toString() {
		return "TokenReqBody [component_appid=" + component_appid + ", component_appsecret=" + component_appsecret
				+ ", component_verify_ticket=" + component_verify_ticket + ", authorization_code=" + authorization_code
				+ ", authorizer_appid=" + authorizer_appid + ", authorizer_refresh_token=" + authorizer_refresh_token
				+ "]";
	}
}
