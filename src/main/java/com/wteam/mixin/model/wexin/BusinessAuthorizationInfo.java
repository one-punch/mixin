package com.wteam.mixin.model.wexin;

public class BusinessAuthorizationInfo {
//		"authorization_info": {
//		"authorizer_appid": "wxf8b4f85f3a794e77", 
//		"authorizer_access_token": "QXjUqNqfYVH0yBE1iI_7vuN_9gQbpjfK7hYwJ3P7xOa88a89-Aga5x1NMYJyB8G2yKt1KCl0nPC3W9GJzw0Zzq_dBxc8pxIGUNi_bFes0qM", 
//		"expires_in": 7200, 
//		"authorizer_refresh_token": "dTo-YCXPL4llX-u1W1pPpnp8Hgm4wpJtlR6iV0doKdY", 
//		"func_info": []
	/**	公众号信息所属商家*/
	private Long businessId;
	
	/**	公众号id*/
	private String authorizer_appid;
	
	/**	公众号调用凭据*/
	private String authorizer_access_token;
	
	/**	凭据有效期 单位 s*/
	private Integer expires_in;
	
	/**	刷新凭据 ms*/
	private String authorizer_refresh_token;
	
	/**	授权接口*/
	private String func_info;
	
	public BusinessAuthorizationInfo() {	}
	
	public BusinessAuthorizationInfo(Long businessId, String authorizer_appid, String authorizer_access_token, Integer expires_in,
			String authorizer_refresh_token, String func_info) {
		this.businessId = businessId;
		this.authorizer_appid = authorizer_appid;
		this.authorizer_access_token = authorizer_access_token;
		this.expires_in = expires_in;
		this.authorizer_refresh_token = authorizer_refresh_token;
		this.func_info = func_info;
	}
	
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getAuthorizer_appid() {
		return authorizer_appid;
	}

	public void setAuthorizer_appid(String authorizer_appid) {
		this.authorizer_appid = authorizer_appid;
	}

	public String getAuthorizer_access_token() {
		return authorizer_access_token;
	}

	public void setAuthorizer_access_token(String authorizer_access_token) {
		this.authorizer_access_token = authorizer_access_token;
	}

	public Integer getExpires_in() {
		return expires_in;
	}

	public void setExpires_in(Integer expires_in) {
		this.expires_in = expires_in;
	}

	public String getAuthorizer_refresh_token() {
		return authorizer_refresh_token;
	}

	public void setAuthorizer_refresh_token(String authorizer_refresh_token) {
		this.authorizer_refresh_token = authorizer_refresh_token;
	}

	public String getFunc_info() {
		return func_info;
	}

	public void setFunc_info(String func_info) {
		this.func_info = func_info;
	}
	
	@Override
	public String toString() {
		return "BusinessAuthorizationInfo [businessId=" + businessId + ", authorizer_appid=" + authorizer_appid
				+ ", authorizer_access_token=" + authorizer_access_token + ", expires_in=" + expires_in
				+ ", authorizer_refresh_token=" + authorizer_refresh_token + ", func_info=" + func_info + "]";
	}

}
