package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.model.vo.BusinessThemeVo;
import com.wteam.mixin.model.vo.ThemeVo;
import com.wteam.mixin.pagination.Pagination;

public interface IThemeService {
	
	/**
	 * 分页列出所有主题:按isDelete，createTime排序
	 */
	public Pagination listThemeBySuper(int pageNo, int pageSize);
	
	/**
	 * 更新主题信息 
	 */
	public void updateTheme(ThemeVo theme);
	
	/**
	 * 设置默认主题
	 */
	public void setDefaultTheme(Long themeId);
	
	/**
	 * 分页列出所有主题:createTime排序、注意isDelete为true和display为false不能查出来
	 */
	public Pagination listThemeByBusiness(int pageNo, int pageSize);
	
	/**
	 * 添加主题
	 */
	public void addTheme(ThemeVo theme);
	
	/**
	 * 删除主题
	 * @param themeId
	 */
	public void deleteTheme(Long themeId);
	
	
	/**
	 * 查找某一主题
	 * @param themeId
	 */
	public ThemeVo findTheme(Long themeId);
	
	/**
	 * 列出商家自身已有的主题
	 * 	--按createTime排序
	 * @param businessId
	 * @return
	 */
	public List<BusinessThemeVo> listBusinessTheme(Long businessId);
	
	/**
	 * 商家设置 重置被使用（actived）主题
	 * @param themeId
	 * @param businessId
	 */
	public void setActived(Long themeId, Long businessId);

	/**
	 * 购买主题，如果已购买且未过期则为续费操作
	 * @return
	 */
	public String purchaseTheme(Long themeId, Long businessId);
}
