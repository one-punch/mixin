package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanDao;
import com.wteam.mixin.biz.service.ITrafficService;
import com.wteam.mixin.constant.Provider;
import com.wteam.mixin.constant.Provinces;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.GroupNPlanVo;
import com.wteam.mixin.model.vo.TrafficGroupQueryVo;
import com.wteam.mixin.model.vo.TrafficGroupVo;
import com.wteam.mixin.model.vo.TrafficPlanQueryVo;
import com.wteam.mixin.model.vo.TrafficPlanVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.recharge.RechargeProvider;
import com.wteam.mixin.utils.WhereCauseSqlUtils;

@Service("trafficService")
public class TrafficServiceImpl implements ITrafficService{
    
    private static Logger LOG = LogManager.getLogger(TrafficServiceImpl.class.getName());
    
	@Autowired
	private IBaseDao baseDao;
	

    @Autowired
    private ITrafficPlanDao trafficPlanDao;
	
	
	@Autowired
	private DozerBeanMapper mapper;

    /**套餐转换处理*/
    private Function<TrafficPlanPo, TrafficPlanVo> _planToVo = po -> {
        TrafficPlanVo vo = mapper.map(po, TrafficPlanVo.class);
        if (vo.getTrafficgroupId() != null) {
            TrafficGroupPo groupPo = baseDao.findUniqueByProperty("id", vo.getTrafficgroupId(),
                TrafficGroupPo.class);
            if (groupPo != null) {
                vo.setProvince(groupPo.getProvince());
                vo.setGroupName(groupPo.getName());
            }
        }
        return vo;
    };
    
    /**套餐默认处理块*/
    private Function<List<?>, List<?>> _planDefaultBock = pos -> {
        return pos.stream().map(po -> (TrafficPlanPo)po).map(_planToVo).collect(Collectors.toList());
    };
    
	@Override
	public void addTrafficGroup(TrafficGroupVo group) {
		
		baseDao.save(mapper.map(group, TrafficGroupPo.class));
	}

	@Override
	public void updateTrafficGroup(TrafficGroupVo group) {
		TrafficGroupPo _group = baseDao.find(TrafficGroupPo.class, group.getId());
		if(_group == null){
			throw new ServiceException("被修改的流量分组不存在");
		}
		mapper.map(group, _group);
		baseDao.update(_group);
	}

	@Override
	public List<TrafficGroupVo> listTrafficGroupBySuper(TrafficGroupQueryVo query, Boolean hasInfo) {
		
		String hasInfoPrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id,name, provider, province, display, info, sort)";
		String elsePrefix  = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id,name, provider, province, display, sort)";
		//1. 根据是否输出info设置hql查询前缀
		String hqlPrefix = (hasInfo==null||hasInfo==false)?elsePrefix:hasInfoPrefix;
		//2. 构造查询hql查询语句及参数集合
		List<Object> result = WhereCauseSqlUtils.create("TrafficGroupPo", query);
		String hql = (String) result.get(0);
		//3. 拼凑查询语句
		hql = hqlPrefix + hql;
		@SuppressWarnings("unchecked")
		List<Object> paramLst = (List<Object>) result.get(1);
		//4. 执行hql查询语句
		List<TrafficGroupVo> groupLst = baseDao.find(hql+" order by sort desc", paramLst);
		
		return groupLst;
	}
	
	@Override
	public Pagination listTrafficGroupPageBySuper(TrafficGroupQueryVo query, Boolean hasInfo, Integer pageNo, Integer pageSize) {
	
		if(pageNo<=0 || pageSize<=0){
			throw new ServiceException("页码或页行为负数或零");
		}
		String hasInfoPrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id,name, provider, province, display, info, sort) ";
		String elsePrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id,name, provider, province, display, sort) ";
		//1. 根据是否输出info设置hql查询前缀
		String hqlPrefix = (hasInfo==null||hasInfo==false)?elsePrefix:hasInfoPrefix;
		//2. 构造查询hql查询语句及参数集合
		List<Object> result = WhereCauseSqlUtils.create("TrafficGroupPo", query);
		String hql = (String) result.get(0);
		@SuppressWarnings("unchecked")
		List<Object> paramLst = (List<Object>) result.get(1);
		Long count = baseDao.getOnly("select count(*) "+hql, paramLst);
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		//3. 拼凑查询语句
		hql = hqlPrefix + hql;
//		System.out.println("the last hql: "+hql);
		//4. 执行hql查询语句
		List<TrafficGroupVo> groupLst = baseDao.find(hql+" order by sort desc", paramLst, page.getPageNo(), pageSize);
		page.setList(groupLst);
		
		return page;
	}

	@Override
	public List<TrafficGroupVo> listTrafficGroupByBusiness(TrafficGroupQueryVo query, Boolean hasInfo) {
		
		String hasInfoPrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id, name, provider, province, display, info, sort)";
		String elsePrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id, name, provider, province, display, sort)";
		//1. 根据是否输出info设置hql查询前缀
		String hqlPrefix = (hasInfo==null||hasInfo==false)?elsePrefix:hasInfoPrefix;
		//2. 构造查询hql查询语句及参数集合
		List<Object> result = WhereCauseSqlUtils.create("TrafficGroupPo", query);
		String hql = (String) result.get(0);
		//3. 构造hql后缀
		String hqlPostfix = hql.contains("where")?" and isDelete=false and display=true order by sort desc ":" where isDelete=false and display=true order by sort desc ";
		//4. 拼凑查询语句
		hql = hqlPrefix + hql + hqlPostfix;
		if(LOG.isDebugEnabled()) LOG.debug("hql->{}",hql);
		@SuppressWarnings("unchecked")
		List<Object> paramLst = (List<Object>) result.get(1);
		//5. 执行hql查询语句
		List<TrafficGroupVo> groupLst = baseDao.find(hql, paramLst);
		
		return groupLst;
	}
	
	@Override
	public Pagination listTrafficGroupPageByBusiness(TrafficGroupQueryVo query, Boolean hasInfo, Integer pageNo, Integer pageSize) {
		if(pageNo<=0 || pageSize<=0){
			throw new ServiceException("页码或页行为负数或零");
		}
		String hasInfoPrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id, provider, province, display, info, sort) ";
		
		String elsePrefix = "select new com.wteam.mixin.model.vo.TrafficGroupVo("
				+ "id, provider, province, display, sort) ";
		//1. 根据是否输出info设置hql查询前缀
		String hqlPrefix = (hasInfo==null||hasInfo==false)?elsePrefix:hasInfoPrefix;
		//2. 构造查询hql查询语句及参数集合
		List<Object> result = WhereCauseSqlUtils.create("TrafficGroupPo", query);
		String hql = (String) result.get(0);
		//3. 构造hql后缀
		String hqlPostfix = hql.contains("where")?" and isDelete=false and display=true order by sort desc ":" where isDelete=false and display=true order by sort desc ";
		@SuppressWarnings("unchecked")
		List<Object> paramLst = (List<Object>) result.get(1);
		Long count = baseDao.getOnly("select count(*) "+hql+hqlPostfix, paramLst);
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		//4. 拼凑查询语句
		hql = hqlPrefix + hql + hqlPostfix;
        if(LOG.isDebugEnabled()) LOG.debug("hql->{}",hql);
		//5. 执行hql查询语句
		List<TrafficGroupVo> groupLst = baseDao.find(hql, paramLst, page.getPageNo(), pageSize);
		page.setList(groupLst);
		
		return page;
	}

	@Override
	public void addTrafficPlanIntoGroup(Long trafficpgroupId, Long[] trafficplanIds) {
		TrafficGroupPo _group = baseDao.find(TrafficGroupPo.class, trafficpgroupId);
		if(_group == null){
			throw new ServiceException("很抱歉，该流量分组不存在");
		}
		for (Long planId : trafficplanIds) {
			TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, planId);
			if(_plan == null){
				throw new ServiceException("很抱歉，该流量套餐列表中含有非法数据");
			}
			_plan.setTrafficGroupId(trafficpgroupId);
			baseDao.update(_plan);
		}
	}

	@Override
	public void removeTrafficPlanFromGroup(Long trafficpgroupId, Long[] trafficplanIds) {
		TrafficGroupPo _group = baseDao.find(TrafficGroupPo.class, trafficpgroupId);
		if(_group == null){
			throw new ServiceException("很抱歉，该流量分组不存在");
		}
		for (Long planId : trafficplanIds) {
			TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, planId);
			if(_plan == null){
				throw new ServiceException("很抱歉，该流量套餐列表中含有非法数据");
			}
			_plan.setTrafficGroupId(null);
			baseDao.update(_plan);
		}
	}

    @Override
    public void addTrafficPlanBySuper(TrafficPlanVo plan) {
        // 验证接口商
        if (!RechargeProvider.ALL.contains(plan.getApiProvider())) 
            throw new ServiceException("没有这个接口商");
        TrafficPlanPo planPo = mapper.map(plan, TrafficPlanPo.class);
        baseDao.save(planPo);
    }
    
	@Override
	public void updateTrafficPlanBySuper(TrafficPlanVo plan) {
		TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, plan.getId());
		if(_plan == null){
			throw new ServiceException("很抱歉，该流量套餐不存在，无法修改");
		}
		mapper.map(plan, _plan);
		baseDao.update(_plan);
	}

	@Override
	public void updateTrafficPlanByBusiness(TrafficPlanVo plan, Long businessId) {
		
		// 1. 对修改的流量套餐进行非空判断
		TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, plan.getId());
		if(_plan == null){
			throw new ServiceException("该流量套餐不存在");
		}
//		2. 判断商家是否拥有该流量套餐
		BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=?", new Object[]{plan.getId(), businessId});

        BigDecimal cost = trafficPlanDao.cost(businessId, plan.getId());
        if (cost.compareTo(plan.getRetailPrice()) >= 0) 
            throw new ServiceException("零售价不能小于或等于成本价");
        
		if(_busiPlan == null){
//			2.1 不存在，进行插入操作
			_busiPlan = new BusinessTrafficPlanPo();
			mapper.map(plan, _busiPlan);
			_busiPlan.setBusinessId(businessId);
			_busiPlan.setTrafficplanId(plan.getId());
			baseDao.save(_busiPlan);
		}else{
//			2.2 存在，进行更新操作
			_busiPlan.setDisplay(plan.getDisplay());
			_busiPlan.setTip(plan.getTip());
			_busiPlan.setRetailPrice(plan.getRetailPrice());
			baseDao.update(_busiPlan);
		}
	}

	@Override
	public List<TrafficPlanVo> listTrafficPlanByGroup(Long groupId) {

		List<TrafficPlanPo> poLst = baseDao.find("from TrafficPlanPo where trafficGroupId=?", new Object[]{groupId});
		List<TrafficPlanVo> planLst = poLst.stream()
				.map(po -> mapper.map(po, TrafficPlanVo.class))
				.collect(Collectors.toList());
		
		return planLst;
	}

	@Override
	public Pagination listTrafficPlanBySuper(TrafficPlanQueryVo query, Integer pageNo, Integer pageSize) {

//		1. 判断页码和页行数  大小是否为正数
		if(pageNo<=0 || pageSize<=0){
			throw new ServiceException("页码或页行为负数或零");
		}
        Long count = 0L;
        List<TrafficPlanVo> planLst = new ArrayList<>();
        Pagination page;
        // 查询所有的套餐
        if (query.getProvider() == null && query.getProvince() == null) {
            count = baseDao.getOnly("select count(*) from TrafficPlanPo where  isDelete=false ");
            page = new Pagination(pageNo, pageSize, count.intValue());
            List<TrafficPlanPo> planPos = baseDao.find(" from TrafficPlanPo plan where  plan.isDelete=false order by plan.trafficGroupId",new Object[]{},page.getPageNo(), page.getPageSize());
            planLst = planPos.stream()
            .map(po -> {
                TrafficPlanVo vo =  mapper.map(po,    TrafficPlanVo.class);
                if (vo.getTrafficgroupId() != null) {
                    TrafficGroupPo groupPo = baseDao.findUniqueByProperty("id", vo.getTrafficgroupId(), TrafficGroupPo.class);
                    if (groupPo != null) {
                        vo.setProvince(groupPo.getProvince());
                        vo.setGroupName(groupPo.getName());
                    }
                }
                return vo;
            })
            .collect(Collectors.toList());
        }
        else {

//          2. 判断分组id是否为空，若为空按运营商和省份先查询分组，再查询流量套餐
            List<TrafficGroupVo> groupLst = new ArrayList<>();
            if(query.getTrafficgroupId() == null){
                groupLst = listTrafficGroupBySuper(new TrafficGroupQueryVo(query.getProvider(), query.getProvince()), false);
            }else{
                TrafficGroupVo group = new TrafficGroupVo();
                group.setId(query.getTrafficgroupId());
                groupLst.add(group);
            }
//		    3. 根据分组查询流量
            String hqlquery = "";
            
            for (TrafficGroupVo group : groupLst) {
                Long num = baseDao.getOnly("select count(*) from TrafficPlanPo where isDelete=false and trafficGroupId="+ group.getId());
                count += num;
                hqlquery += " plan.trafficGroupId=" + group.getId() + " or";
            }
            page = new Pagination(pageNo, pageSize, count.intValue());
            if(groupLst.size() > 0) {
                hqlquery =  "(" + hqlquery.substring(0, hqlquery.length() - 2) + ")";
                LOG.debug(page);
                planLst = baseDao.find(TrafficPlanVo.SELECT_1 + " from  TrafficPlanPo plan , TrafficGroupPo groupPo "
                    + "where plan.isDelete=false and plan.trafficGroupId=groupPo.id and " + hqlquery + " order by groupPo.sort desc,plan.trafficGroupId ",new Object[]{},page.getPageNo(), page.getPageSize());
            }
        }
        
		page.setList(planLst);
		
		return page;
	}

    @Override
    public Pagination listTrafficPlanBySuper(TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
                                             Integer currentPage, Integer pageSize) {
        if (planQuery == null || groupQuery == null) {
            throw new ServiceException("查询条件不完整 ");
        }
        planQuery.setIsDelete(false);
        groupQuery.setIsDelete(false);
        planQuery.putSortField("trafficGroupId");
        planQuery.putSortField("isAuto");
        Pagination pagination = trafficPlanDao.find(planQuery, groupQuery, currentPage, pageSize);
        
        return pagination.handle( _planDefaultBock);
    }
    
	@Override
	public Pagination listTrafficPlanByBusiness(TrafficPlanQueryVo query, Integer pageNo, Integer pageSize, Long businessId) {

//		1. 判断页码和页行数  大小是否为正数
		if(pageNo<=0 || pageSize<=0){
			throw new ServiceException("页码或页行为负数或零");
		}
//		2. 判断分组id是否为空，若为空按运营商和省份先查询分组，再查询流量套餐
		List<TrafficGroupVo> groupLst = new ArrayList<>();
		if(query.getTrafficgroupId() == null){
			groupLst = listTrafficGroupByBusiness(new TrafficGroupQueryVo(query.getProvider(), query.getProvince()), false);
		}else{
			TrafficGroupVo group = new TrafficGroupVo();
			group.setId(query.getTrafficgroupId());
			groupLst.add(group);
		}
//		3. 根据分组获取流量套餐列表
		List<TrafficPlanVo> planLst = new ArrayList<>();
		int count = 0;
        String hqlquery = "";
        
        for (TrafficGroupVo group : groupLst) {
            Long num = baseDao.getOnly("select count(*) from TrafficPlanPo where  display=true and isDelete=false and trafficGroupId="+ group.getId());
            count += num;
            hqlquery += " plan.trafficGroupId=" + group.getId() + " or";
        }

        Pagination page = new Pagination(pageNo, pageSize, count);
        if(groupLst.size() > 0) {
            hqlquery =  "(" + hqlquery.substring(0, hqlquery.length() - 2) + ")";
            LOG.debug(page);
            planLst = baseDao.find(TrafficPlanVo.SELECT_1 + " from  TrafficPlanPo as plan , TrafficGroupPo as groupPo"
                + " where plan.display=true and plan.isDelete=false and plan.trafficGroupId=groupPo.id and " + hqlquery + " order by groupPo.sort desc,plan.trafficGroupId,plan.id ",new Object[]{},page.getPageNo(), page.getPageSize());
        }
		
//		4、根据获取到的套餐列表查询商家自己的套餐列表、并添加或替换重名字段（例如，如果是会员，替换成本价cost， 添加商家自己的流量套餐
//		中的tip、零售价、是否上架属性）
//		4.1 获取商家信息
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});

//		4.3 根据以获取的套餐列表查询商家自身的套餐列表，并替换重名字段
		for (TrafficPlanVo plan : planLst) {
			BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=?", new Object[]{plan.getId(), businessId});
//			4.3.1 判断商家是否拥有该套餐，拥有进行字段添加，否则不作处理
			if(_busiPlan != null){
			    LOG.debug(_busiPlan);
				plan.setRetailPrice(_busiPlan.getRetailPrice());
				plan.setDisplay(_busiPlan.getDisplay());
				plan.setTip(_busiPlan.getTip());
			}
//			4.3.2 根据会员有效期是否过期，读取商家会员有效期的流量成本信息（如果该流量存在对应的优惠套餐信息），并替换原有成本信息
			plan.setCost(trafficPlanDao.cost(_business.getBusinessId(), plan.getId()));
		}
		page.setList(planLst);
		
		return page;
	}

	@Override
	public List<GroupNPlanVo> listGroupNPlanByBusiness(Integer provider, String province, Long businessId) {

//		1.根据运营商及省份 获取 流量分组
		List<TrafficGroupVo> groupLst = listTrafficGroupByBusiness(new TrafficGroupQueryVo(provider, province), false);
		System.out.println("groupLst:");
		for (TrafficGroupVo trafficGroupVo : groupLst) {
			System.out.println(trafficGroupVo.toString());
		}
		//		2.根据流量分组获取 流量套餐列表 并将信息封装至GroupNPlanVo中
		List<GroupNPlanVo> gnpLst = new ArrayList<>();
		for (TrafficGroupVo group : groupLst) {
//			2.1 构造GroupNPlanVo对象以保存数据
			GroupNPlanVo gnp = mapper.map(group, GroupNPlanVo.class);
			
			List<TrafficPlanPo> poLst = baseDao.find("from TrafficPlanPo where trafficGroupId=? and isDelete=false and display=true", new Object[]{group.getId()});
			List<TrafficPlanVo> planLst = poLst.stream()
					.map(po -> mapper.map(po, TrafficPlanVo.class))
					.collect(Collectors.toList());
//			2.2、根据获取到的套餐列表查询商家自己的套餐列表、并添加或替换重名字段（例如，如果是会员，替换成本价cost， 添加商家自己的流量套餐
//			中的tip、零售价、是否上架属性）
//			2.2.1 获取商家信息
			BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
//			2.2.3 根据已获取的套餐列表查询商家自身的套餐列表，并替换重名字段
			for (TrafficPlanVo plan : planLst) {
				BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=?", new Object[]{plan.getId(), businessId});
//				2.2.3.1 判断商家是否拥有该套餐，拥有进行字段添加，否则不作处理
				if(_busiPlan != null){
					plan.setRetailPrice(_busiPlan.getRetailPrice());
					plan.setDisplay(_busiPlan.getDisplay());
					plan.setTip(_busiPlan.getTip());
				}
//	          4.3.2 根据会员有效期是否过期，读取商家会员有效期的流量成本信息（如果该流量存在对应的优惠套餐信息），并替换原有成本信息
	            plan.setCost(trafficPlanDao.cost(_business.getBusinessId(), plan.getId()));
			}
			gnp.setTrafficplanList(planLst);
			gnpLst.add(gnp);
		}
		
		return gnpLst;
	}

	@Override
	public List<GroupNPlanVo> listGroupNPlanByCustomer(Integer provider, String province, Long businessId) {
//		1.根据运营商及省份 获取 流量分组
		List<TrafficGroupVo> groupLst = listTrafficGroupByBusiness(new TrafficGroupQueryVo(provider, province), false);
//		2.根据流量分组获取 流量套餐列表 并将信息封装至GroupNPlanVo中
		List<GroupNPlanVo> gnpLst = new ArrayList<>();
		for (TrafficGroupVo group : groupLst) {
//			2.1 构造GroupNPlanVo对象以保存数据
            GroupNPlanVo gnp = mapper.map(group, GroupNPlanVo.class);
			
			List<TrafficPlanPo> poLst = baseDao.find("from TrafficPlanPo where trafficGroupId=? and display=true and isDelete=false", new Object[]{group.getId()});
			List<TrafficPlanVo> planLst = poLst.stream()
					.map(po -> mapper.map(po, TrafficPlanVo.class))
					.collect(Collectors.toList());
//			2.2、根据获取到的套餐列表查询商家自己的套餐列表、并添加或替换重名字段（例如，如果是会员，替换成本价cost， 添加商家自己的流量套餐
//			中的tip、零售价、是否上架属性）
//			2.2.1 获取商家信息
			BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
//			2.2.3 根据已获取的套餐列表查询商家自身的套餐列表，并替换重名字段
			for (TrafficPlanVo plan : planLst) {
				BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=? and display=?", new Object[]{plan.getId(), businessId, true});
//				2.2.3.1 判断商家是否拥有该套餐，拥有进行字段添加，否则不作处理
				if(_busiPlan != null){
					plan.setRetailPrice(_busiPlan.getRetailPrice());
					plan.setDisplay(_busiPlan.getDisplay());
					plan.setTip(_busiPlan.getTip());
				}
//	          4.3.2 根据会员有效期是否过期，读取商家会员有效期的流量成本信息（如果该流量存在对应的优惠套餐信息），并替换原有成本信息
	            //plan.setCost(trafficPlanDao.cost(_business.getBusinessId(), plan.getId()));
			}
			// 过滤 display为false的套餐
			planLst = planLst.stream().filter(TrafficPlanVo::getDisplay).collect(Collectors.toList());
			gnp.setTrafficplanList(planLst);
			gnpLst.add(gnp);
		}
		
		return gnpLst;
	}

    @Override
    public void addApiRechargeTrafficplan(Long planId) {
        // 根据接口商，省份，值生成产品唯一信息
        TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, planId);
        if(_plan == null){
            throw new ServiceException("该流量套餐不存在");
        }
        String provider = Provider.get(_plan.getProvider()).acronym;
        String apiProvider = RechargeProvider.MAP.get(_plan.getApiProvider());
        
        TrafficGroupPo _group = baseDao.find(TrafficGroupPo.class, _plan.getTrafficGroupId());
        if(_group == null){
            throw new ServiceException("流量套餐没有分组不能设置为接口充值套餐");
        }
        
        String province = Provinces.getFieldName(_group.getProvince());
        
        String auto = _plan.getIsAuto() ? "_A" : "";
        
        String productNum = String.format("%s_%s_%s%s_%s", provider, apiProvider, province, auto, _plan.getValue());
        
        // 设置为接口充值流量
        _plan.setProductNum(productNum);
        _plan.setIsApiRecharge(true);
        baseDao.update(_plan);
        
    }

    @Override
    public void deleteApiRechargeTrafficplan(Long planId) {
        TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, planId);
        if(_plan == null){
            throw new ServiceException("该流量套餐不存在");
        }
        // 取消设置
        _plan.setIsApiRecharge(false);
        baseDao.update(_plan);
    }

    @Override
    public Pagination listApiRechargeTrafficplanlistBySuper(TrafficPlanQueryVo query, Integer pageNo,
                                                     Integer pageSize) {

//      1. 判断页码和页行数  大小是否为正数
        if(pageNo<=0 || pageSize<=0){
            throw new ServiceException("页码或页行为负数或零");
        }
        Long count = 0L;
        List<TrafficPlanVo> planLst = new ArrayList<>();
        Pagination page;
        // 查询所有的套餐
        if (query.getProvider() == null && query.getProvince() == null) {
            count = baseDao.getOnly("select count(*) from TrafficPlanPo where  isDelete=false and isApiRecharge=" + query.getIsApiRecharge());
            page = new Pagination(pageNo, pageSize, count.intValue());
            List<TrafficPlanPo> planPos = baseDao.find(" from TrafficPlanPo plan where  plan.isDelete=false and plan.isApiRecharge=? order by plan.trafficGroupId",
                    new Object[]{query.getIsApiRecharge()},page.getPageNo(), page.getPageSize());
            planLst = planPos.stream()
            .map(po -> {
                TrafficPlanVo vo =  mapper.map(po,    TrafficPlanVo.class);
                if (vo.getTrafficgroupId() != null) {
                    TrafficGroupPo groupPo = baseDao.findUniqueByProperty("id", vo.getTrafficgroupId(), TrafficGroupPo.class);
                    if (groupPo != null) {
                        vo.setProvince(groupPo.getProvince());
                        vo.setGroupName(groupPo.getName());
                    }
                }
                return vo;
            })
            .collect(Collectors.toList());
        }
        else {

//          2. 判断分组id是否为空，若为空按运营商和省份先查询分组，再查询流量套餐
            List<TrafficGroupVo> groupLst = new ArrayList<>();
            if(query.getTrafficgroupId() == null){
                groupLst = listTrafficGroupBySuper(new TrafficGroupQueryVo(query.getProvider(), query.getProvince()), false);
            }else{
                TrafficGroupVo group = new TrafficGroupVo();
                group.setId(query.getTrafficgroupId());
                groupLst.add(group);
            }
//          3. 根据分组查询流量
            String hqlquery = "";
            
            for (TrafficGroupVo group : groupLst) {
                Long num = baseDao.get("select count(*) from TrafficPlanPo where isDelete=false and trafficGroupId=? and isApiRecharge=?", new Object[]{group.getId(), query.getIsApiRecharge()});
                count += num;
                hqlquery += " plan.trafficGroupId=" + group.getId() + " or";
            }
            page = new Pagination(pageNo, pageSize, count.intValue());
            if(groupLst.size() > 0) {
                hqlquery =  "(" + hqlquery.substring(0, hqlquery.length() - 2) + ")";
                LOG.debug(page);
                planLst = baseDao.find(TrafficPlanVo.SELECT_1 + " from  TrafficPlanPo plan , TrafficGroupPo groupPo "
                    + "where plan.isDelete=false and plan.trafficGroupId=groupPo.id and plan.isApiRecharge=? and " + hqlquery + " order by groupPo.sort desc,plan.trafficGroupId ",new Object[]{query.getIsApiRecharge()},page.getPageNo(), page.getPageSize());
            }
        }
        
        page.setList(planLst);
        
        return page;
    }

    @Override
    public Pagination listApiRechargeTrafficplanlistByBusiness(TrafficPlanQueryVo query,
                                                               Integer pageNo,
                                                               Integer pageSize, Long businessId) {
//      1. 判断页码和页行数  大小是否为正数
        if(pageNo<=0 || pageSize<=0){
            throw new ServiceException("页码或页行为负数或零");
        }
//      2. 判断分组id是否为空，若为空按运营商和省份先查询分组，再查询流量套餐
        List<TrafficGroupVo> groupLst = new ArrayList<>();
        if(query.getTrafficgroupId() == null){
            groupLst = listTrafficGroupByBusiness(new TrafficGroupQueryVo(query.getProvider(), query.getProvince()), false);
        }else{
            TrafficGroupVo group = new TrafficGroupVo();
            group.setId(query.getTrafficgroupId());
            groupLst.add(group);
        }
//      3. 根据分组获取流量套餐列表
        List<TrafficPlanVo> planLst = new ArrayList<>();
        int count = 0;
        String hqlquery = "";
        
        for (TrafficGroupVo group : groupLst) {
            Long num = baseDao.getOnly("select count(*) from TrafficPlanPo where  display=true and isApiRecharge=true and isDelete=false and trafficGroupId="+ group.getId());
            count += num;
            hqlquery += " plan.trafficGroupId=" + group.getId() + " or";
        }

        Pagination page = new Pagination(pageNo, pageSize, count);
        if(groupLst.size() > 0) {
            hqlquery =  "(" + hqlquery.substring(0, hqlquery.length() - 2) + ")";
            LOG.debug(page);
            planLst = baseDao.find(TrafficPlanVo.SELECT_2 + " from  TrafficPlanPo as plan , TrafficGroupPo as groupPo"
                + " where plan.display=true and plan.isDelete=false and plan.isApiRecharge=true and plan.trafficGroupId=groupPo.id and " + hqlquery + " order by groupPo.sort desc,plan.trafficGroupId,plan.id ",
                new Object[]{},page.getPageNo(), page.getPageSize());
        }
        
//      4、根据获取到的套餐列表查询商家自己的套餐列表、并添加或替换重名字段（例如，如果是会员，替换成本价cost， 添加商家自己的流量套餐
//      中的tip、零售价、是否上架属性）
//      4.1 获取商家信息
        BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
//      4.3 根据以获取的套餐列表查询商家自身的套餐列表，并替换重名字段
        for (TrafficPlanVo plan : planLst) {
            BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=?", new Object[]{plan.getId(), businessId});
//          4.3.1 判断商家是否拥有该套餐，拥有进行字段添加，否则不作处理
            if(_busiPlan != null){
                LOG.debug(_busiPlan);
                plan.setRetailPrice(_busiPlan.getRetailPrice());
                plan.setDisplay(_busiPlan.getDisplay());
                plan.setTip(_busiPlan.getTip());
            }
//          4.3.2 根据会员有效期是否过期，读取商家会员有效期的流量成本信息（如果该流量存在对应的优惠套餐信息），并替换原有成本信息
            plan.setCost(trafficPlanDao.cost(_business.getBusinessId(), plan.getId()));
        }
        page.setList(planLst);
        
        return page;
    }


}	
