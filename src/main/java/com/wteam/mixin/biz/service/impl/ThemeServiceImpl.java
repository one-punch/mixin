package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IThemeService;
import com.wteam.mixin.constant.PlatformInfo;
import com.wteam.mixin.constant.State.BBRecordSource;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.vo.BusinessThemeVo;
import com.wteam.mixin.model.vo.ThemeVo;
import com.wteam.mixin.pagination.Pagination;

@Service("themeService")
public class ThemeServiceImpl implements IThemeService{

	@Autowired
	private IBaseDao baseDao;

    @Autowired
    IFinanceService financeService;
    
	@Autowired
	private DozerBeanMapper mapper;

	private static final Logger LOG = LogManager.getLogger(ThemeServiceImpl.class.getName());
	
	@Override
	public void addTheme(ThemeVo theme) {
		LOG.debug(theme.toString());
		baseDao.save(mapper.map(theme, ThemePo.class));
	}

	@Override
	public void deleteTheme(Long themeId) {
		ThemePo _theme = baseDao.find(ThemePo.class, themeId);
		if(_theme == null){
			throw new ServiceException("该主题不存在，无法删除");
		}
		_theme.setIsDelete(true);
		baseDao.update(_theme);
	}

	@Override
	public void updateTheme(ThemeVo theme) {
		ThemePo _theme = baseDao.find(ThemePo.class, theme.getId());
		if(_theme == null){
			throw new ServiceException("该主题不存在，无法更新");
		}
		if(theme.getCost()!=null){
			_theme.setCost(theme.getCost());
		}
		if(theme.getImgId()!=null){
			_theme.setImgId(theme.getImgId());
		}
		if(theme.getName()!=null){
			_theme.setName(theme.getName());
		}
		if(theme.getSign()!=null){
			_theme.setSign(theme.getSign());
		}
		if(theme.getVaildity()!=null){
			_theme.setVaildity(theme.getVaildity());
		}
		if(theme.isDisplay() != null){
		    _theme.setDisplay(theme.isDisplay());
		}
		
		baseDao.update(_theme);
	}

	@Override
	public ThemeVo findTheme(Long themeId) {
		ThemePo _theme = baseDao.find(ThemePo.class, themeId);
		if(_theme ==null){
			throw new ServiceException("该主题不存在");
		}
		
		return mapper.map(_theme, ThemeVo.class);
	}

	@Override
	public Pagination listThemeBySuper(int pageNo, int pageSize) {
		if(pageNo<=0||pageSize<=0){
			throw new ServiceException("错误的页码或者页行");
		}
		//TODO 
		Long count = baseDao.getOnly("select count(*) from ThemePo");
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<ThemePo> poLst = baseDao.find("from ThemePo order by isDelete, createTime DESC", new Object[]{}, page.getPageNo(), pageSize);
		List<ThemeVo> themeLst = poLst.stream()
				.map(po->mapper.map(po, ThemeVo.class))
				.collect(Collectors.toList());
		page.setList(themeLst);
		
		return page; 
	}

	@Override
	public Pagination listThemeByBusiness(int pageNo, int pageSize){
		if(pageNo<=0||pageSize<=0){
			throw new ServiceException("错误的页码或者页行");
		}
		Long count = baseDao.getOnly("select count(*) from ThemePo theme where theme.isDelete=false and theme.display=true");
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<ThemePo> poLst = baseDao.find("from ThemePo theme where theme.isDelete=false and theme.display=true order by theme.createTime desc", new Object[]{}, page.getPageNo(), pageSize);
		List<ThemeVo> themeLst = poLst.stream()
				.map(po->mapper.map(po, ThemeVo.class))
				.collect(Collectors.toList());
		page.setList(themeLst);
		
		return page;
	}
	
	@Override
	public void setDefaultTheme(Long themeId) {
		ThemePo _theme = baseDao.find(ThemePo.class, themeId);
		if(_theme == null){
			throw new ServiceException("该主题不存在或已下架，无法被设置为默认主题");
		}
		ThemePo _defaultedTheme = baseDao.get("from ThemePo theme where theme.defaulted=?", new Object[]{true});
		if(_defaultedTheme!=null){
			_defaultedTheme.setDefaulted(false);
			baseDao.update(_defaultedTheme);
		}
		_theme.setIsDelete(false);
		_theme.setDefaulted(true);
		baseDao.update(_theme);
	}
	
	@Override
	public void setActived(Long themeId, Long businessId) {
		//1. 查询被设置主题是否存在
		ThemePo _theme = baseDao.find(ThemePo.class, themeId);
		if(_theme == null || _theme.getIsDelete()){
			throw new ServiceException("很抱歉，您使用的主题不存在.");
		}
		//2. 将当前正在使用的主题 actived 设置为false
		BusinessThemePo _activedBusiTheme = baseDao.get("from BusinessThemePo busiTheme where busiTheme.businessId=? and busiTheme.actived=?", new Object[]{businessId, true});
		if(_activedBusiTheme!=null){
			_activedBusiTheme.setActived(false);
			baseDao.update(_activedBusiTheme);
		}
		//3. 从商家主题BusinessTheme 中获取指定主题，若该主题不存在则执行插入操作，否则直接设置actived属性为true
		BusinessThemePo _busiTheme = baseDao.get("from BusinessThemePo busiTheme where busiTheme.themeId=? and busiTheme.businessId=?", new Object[]{themeId, businessId});
		if(_busiTheme == null){
			_busiTheme = new BusinessThemePo();
			_busiTheme.setBusinessId(businessId);
			_busiTheme.setThemeId(themeId);
			_busiTheme.setStartAt(new Date());
			_busiTheme.setVaildity(_theme.getVaildity());
			_busiTheme.setActived(true);
			baseDao.save(_busiTheme);
		}else{
			_busiTheme.setActived(true);
			baseDao.update(_busiTheme);
		}
	}
	
	@Override
	public List<BusinessThemeVo> listBusinessTheme(Long businessId) {

//		public BusinessThemeVo(Long id, Long themeId, String name, Long imgId, Long cost, boolean actived, Date startAt,
//				Integer vaildity) {
		//1.获取商家自己的主题
		List<BusinessThemeVo> busiThemeLst = baseDao.find(
		    "select new com.wteam.mixin.model.vo.BusinessThemeVo("
		        + "busiTheme.id, theme.id, theme.name, theme.imgId,"
				+ "theme.cost, busiTheme.actived, "
				+ "busiTheme.startAt, busiTheme.vaildity) "
		  + "from ThemePo theme, BusinessThemePo busiTheme"
				+ " where theme.id=busiTheme.themeId and busiTheme.businessId=? order by busiTheme.createTime DESC", new Object[]{businessId});
		//		2.获取平台的免费主题
		List<BusinessThemeVo> freeThemeLst = baseDao.find("select new com.wteam.mixin.model.vo.BusinessThemeVo(theme.id, theme.name, theme.imgId,"
				+ " theme.cost) from ThemePo theme where theme.cost=0 and theme.isDelete=false and theme.display=true order by createTime DESC");
//		3.排重
		for (BusinessThemeVo busiTheme : busiThemeLst) {
			Long id = busiTheme.getThemeId();
			for (BusinessThemeVo freeTheme : freeThemeLst) {
				if(id - freeTheme.getThemeId()==0){//pay attention: Long is a object means '==' compare with their memory address but not the value.
					freeThemeLst.remove(freeTheme);
					break;
				}
			}
		}
//		4.将最后不重复的免费主题加入到商家的个人主题列表中
		busiThemeLst.addAll(freeThemeLst);
		return busiThemeLst;
	}

	@Override
	public String purchaseTheme(Long themeId, Long businessId){
		
		ThemePo _theme = baseDao.find(ThemePo.class, themeId);
		if(_theme==null || _theme.getIsDelete()){
			throw new ServiceException("很抱歉，您购买的主题不存在或已下架.");
		}
		//	1.判断商家余额是否足够购买该主题
		BusinessInfoPo _info = baseDao.get("from BusinessInfoPo info where info.businessId=?", new Object[]{businessId});
		BigDecimal balance = _info.getBalance();
		if(balance.compareTo(_theme.getCost()) == -1){
			throw new ServiceException("您的余额不足，请及时充值.");
		}
		// 2. 判断商家是否购买同一主题
		BusinessThemePo _busiTheme = baseDao.get("from BusinessThemePo busiTheme where busiTheme.businessId=? and busiTheme.themeId=?", new Object[]{businessId, themeId});
		String result = "";
		if(_busiTheme == null){
			//2.1之前未购买该主题 
			//2.1.1扣除商家该主题费用
			_info.setBalance(balance.subtract(_theme.getCost()));
			//2.1.2 为该商家加入购买的主题
			_busiTheme = new BusinessThemePo();
			_busiTheme.setBusinessId(businessId);
			_busiTheme.setThemeId(themeId);
			_busiTheme.setStartAt(new Date());
			_busiTheme.setVaildity(_theme.getVaildity());
			baseDao.save(_busiTheme);
			
			result = "欢迎您第一次购买该主题";
		}else{
			//2.2之前购买过该主题
			Date startAt = _busiTheme.getStartAt();
			Long validity = _busiTheme.getVaildity()*24*60*60*1000L;
			Long current = System.currentTimeMillis();
			boolean isExpired = current-startAt.getTime()>=validity;
			if(!isExpired){
				//未过期，续费操作
				_info.setBalance(balance.subtract(_theme.getCost()));
				_busiTheme.setVaildity(_busiTheme.getVaildity() + _theme.getVaildity());
				result = "该主题续费成功";
			}else{
				//过期，重新购买
				_info.setBalance(balance.subtract(_theme.getCost()));
				_busiTheme.setStartAt(new Date());
				_busiTheme.setVaildity(_theme.getVaildity());
				result = "欢迎再次购买该主题";
			}
			baseDao.update(_busiTheme);
		}
		
		//3. 生成商家消费记录
		BusinessBalanceRecordPo record = new BusinessBalanceRecordPo();
		record.setBusinessId(businessId);
		record.setMoney(_theme.getCost().negate());
		record.setSource(BBRecordSource.service);
        financeService.saveBusinessBalanceRecord(record);
        
		return result;
	}
}















