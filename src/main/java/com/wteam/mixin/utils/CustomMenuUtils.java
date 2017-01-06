package com.wteam.mixin.utils;

import java.util.ArrayList;
import java.util.List;

import com.wteam.mixin.model.po.WechatCustomMenuPo;
import com.wteam.mixin.model.vo.CustomMenuButton;

public class CustomMenuUtils {
	
	/**
	 * 将子菜单和父菜单关联在一起
	 * @param parent
	 * @param childs
	 * @return
	 */
	public static CustomMenuButton translate(WechatCustomMenuPo parent, List<WechatCustomMenuPo> childs){
		
		CustomMenuButton l1;
		if(childs.size()<=0){
			l1 = new CustomMenuButton(parent.getName(), parent.getMenuKey(), parent.getType());
			if(parent.getUrl() != null){
				l1.setUrl(parent.getUrl());
			}
		}else{
			l1 = new CustomMenuButton();
			l1.setName(parent.getName());
			List<CustomMenuButton> l2 = new ArrayList<>();
			for (WechatCustomMenuPo l2Menu : childs) {
				CustomMenuButton e = new CustomMenuButton(l2Menu.getName(), l2Menu.getMenuKey(), l2Menu.getType());
				if(l2Menu.getUrl() != null){
					e.setUrl(l2Menu.getUrl());
				}
				l2.add(e);
			}
			l1.setSub_button(l2);
		}
		
		return l1;
	}
}
