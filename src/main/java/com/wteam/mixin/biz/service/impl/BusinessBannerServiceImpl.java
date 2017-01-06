package com.wteam.mixin.biz.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IBusinessBannerService;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.vo.BusinessBannerVo;

@Service("bannerService")
public class BusinessBannerServiceImpl implements IBusinessBannerService{
	
	@Autowired
	private IBaseDao baseDao;

	@Autowired
	private DozerBeanMapper mapper;
	
	@Override
	public void add(BusinessBannerVo banner) {
		baseDao.save(mapper.map(banner, BusinessBannerPo.class));
	}

	@Override
	public void delete(Long id) {
		BusinessBannerPo banner = baseDao.find(BusinessBannerPo.class, id);
		if(banner==null){
			throw new ServiceException("该广告不存在，无法删除");
		}
		banner.setIsDelete(true);
		baseDao.update(banner);
	}

	@Override
	public List<BusinessBannerVo> findAllBybusinessId(Long businessId) {
		
		List<BusinessBannerPo> pos = baseDao.find("from BusinessBannerPo where businessId=? and isDelete=false", new Object[]{businessId});
		List<BusinessBannerVo> vos = pos.stream()
				.map(po -> mapper.map(po, BusinessBannerVo.class))
				.collect(Collectors.toList());
		
		return vos;
	}

	@Override
	public List<BusinessBannerVo> findAllByIdNActived(Long businessId, boolean actived) {

		String hql ="from BusinessBannerPo as banner where banner.businessId=? and banner.actived=? and isDelete=false";
		Object[] params = {businessId, actived};
		List<BusinessBannerVo> bannerLst =  baseDao.find(hql, params)
			.stream()
			.map(po -> mapper.map(po, BusinessBannerVo.class))
			.collect(Collectors.toList());
		
		return bannerLst;
	}
}
