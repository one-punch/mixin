package com.wteam.mixin.model.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import com.wteam.mixin.define.IValueObject;

/**
 * 用于包装自定义菜单信息，以生成json字符串
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月7日
 */
public class CustomMenuButton implements IValueObject{
	
	/**该菜单名称*/
    @NotNull
    private String name;
    
    /**该菜单key值*/
    private String key;
    
    /**链接地址*/
    @URL
    private String url;
    
    /**菜单类型:click?view?... ATTENTION: type 的值在提交时 必须小写！！*/
    @NotNull
    private String type;
    
    private List<CustomMenuButton> sub_button;

	public CustomMenuButton() {	}

	public CustomMenuButton(String name, String key, String type) {
		this.name = name;
		this.key = key;
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<CustomMenuButton> getSub_button() {
		return sub_button;
	}

	public void setSub_button(List<CustomMenuButton> sub_button) {
		this.sub_button = sub_button;
	}

	@Override
	public String toString() {
		return "CustomMenuButton [name=" + name + ", key=" + key + ", url=" + url + ", type=" + type + ", sub_button="
				+ sub_button + "]";
	}
}
