package com.wteam.mixin.biz.service;

import java.util.List;
import com.wteam.mixin.model.vo.AutoReplyVo;
import com.wteam.mixin.model.vo.CustomMenuButton;
import com.wteam.mixin.model.vo.CustomMenuVo;

public interface ISelfDefineService {
	
	/**
	 * 添加自动回复
	 * @param reply 待添加自动回复内容
	 * @param businessId 所属商家
	 */
	public void addAutoReply(AutoReplyVo reply, Long businessId);

	/**
	 * 更新自动回复
	 * @param reply 更新内容
	 */
	public void updateAutoReply(AutoReplyVo reply);

	/**
	 * 删除自动回复
	 * @param replyId 自动回复id
	 */
	public void deleteAutoReply(Long replyId);

	/**
	 * 列出所有自动回复
	 * @param businessId 所属商家
	 * @return
	 */
	public List<AutoReplyVo> listAutoReply(Long businessId);

	/**
	 * 添加自定义菜单
	 * @param menu 待添加菜单
	 * @param businessId 菜单所属商家
	 * @return
	 */
	public boolean addCustomMenu(CustomMenuVo menu, Long businessId);

	/**
	 * 修改自定义菜单
	 * @param menu 菜单内容
	 * @return
	 */
	public boolean updateCustomMenu(CustomMenuVo menu);

	/**
	 * 删除自定义菜单
	 * @param menuId 待删除菜单id
	 * @return
	 */
	public boolean deleteCustomMenu(Long menuId);
	
	/**
	 * 修改自定义菜单
	 * @param menus
	 */
	public void changeCustomMenus(List<CustomMenuButton> menus, Long businessId);
	
	/**
	 * 列出所有自定义菜单
	 * @param businessId 菜单所属商家
	 * @return
	 */
	public Object listCustomMenu(Long businessId);
    /**
     * 列出所有自定义菜单的链接
     * @param businessId 菜单所属商家
     * @return
     */
    public Object listCustomMenuLink(Long businessId);

	/**
	 * 群发
	 * @param content 群发内容
	 * @param businessId 群发发起者
	 * @return
	 */
	public boolean sendMass(String content, Long businessId);
}
