package com.wteam.mixin.model.wexin;

public class WcUserInfo {
	
	/*
	 * 返回说明
		正确的Json返回结果：
		{ 
		"openid":"OPENID",
		"nickname":"NICKNAME",
		"sex":1,
		"province":"PROVINCE",
		"city":"CITY",
		"country":"COUNTRY",
		"headimgurl": "http://wx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/0",
		"privilege":[
		"PRIVILEGE1", 
		"PRIVILEGE2"
		],
		"unionid": " o6_bmasdasdsad6_2sgVt7hMZOPfL"
		}
	 */
	private String openid;
	
	private String nickname;
	
	private Integer sex;
	
	private String province;
	
	private String city;
	
	private String country;
	
	private String language;
	
	private String headimgurl;
	
	private String[] privilige;
	
	public WcUserInfo() {	}

	public WcUserInfo(String openid, String nickname, Integer sex, String province, String city, String country,
			String headimgurl, String[] privilige) {
		this.openid = openid;
		this.nickname = nickname;
		this.sex = sex;
		this.province = province;
		this.city = city;
		this.country = country;
		this.headimgurl = headimgurl;
		this.privilige = privilige;
	}
	
	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getHeadimgurl() {
		return headimgurl;
	}

	public void setHeadimgurl(String headimgurl) {
		this.headimgurl = headimgurl;
	}

	public String[] getPrivilige() {
		return privilige;
	}

	public void setPrivilige(String[] privilige) {
		this.privilige = privilige;
	}

	@Override
	public String toString() {
		return "WcUserInfo [openid=" + openid + ", nickname=" + nickname + ", sex=" + sex + ", province=" + province
				+ ", city=" + city + ", country=" + country + ", headimgurl=" + headimgurl + ", privilige=" + privilige
				+"]";
	}
}
