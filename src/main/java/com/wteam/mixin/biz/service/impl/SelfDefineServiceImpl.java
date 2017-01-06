package com.wteam.mixin.biz.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.biz.controler.WechatController;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.ISelfDefineService;
import com.wteam.mixin.constant.WechatConfigs;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.constant.WeChatInfoUrls;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.WechatCustomMenuPo;
import com.wteam.mixin.model.po.WechatReplyPo;
import com.wteam.mixin.model.vo.AutoReplyVo;
import com.wteam.mixin.model.vo.CustomMenuButton;
import com.wteam.mixin.model.vo.CustomMenuVo;
import com.wteam.mixin.model.wexin.WechatToken;
import com.wteam.mixin.utils.CustomMenuUtils;
import com.wteam.mixin.utils.WeChatUtils;

/**
 * 
 * <p>Title:微信关键字回复、自定义菜单及群发业务接口实现</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月15日
 */
@Service("selfDefineService")
public class SelfDefineServiceImpl implements ISelfDefineService {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(LoginServiceImpl.class.getName());
    
    @Autowired
	private IBaseDao baseDao;
    
    @Autowired
    private CacheManager cacheManager;
	
	@Autowired
	private DozerBeanMapper mapper;
	
	private static Properties prop = WechatConfigs.prop;

	
	@Override
	public void addAutoReply(AutoReplyVo reply, Long businessId) {
		WechatReplyPo _reply = baseDao.get("from WechatReplyPo reply where reply.keyWord=? and reply.businessId=?", new Object[]{reply.getKeyWord(), businessId});
		if(_reply != null){
			throw new ServiceException("已存在同名的自动回复,请勿重复添加");
		}
		WechatReplyPo po = mapper.map(reply, WechatReplyPo.class);
		po.setBusinessId(businessId);
		baseDao.save(po);
	}

	@Override
	public void updateAutoReply(AutoReplyVo reply) {
		WechatReplyPo _reply = baseDao.find(WechatReplyPo.class, reply.getId());
		if(_reply == null){
			throw new ServiceException("被更新关键字自动回复不存在");
		}
		if(reply.getKeyWord()!=null){
			_reply.setKeyWord(reply.getKeyWord());
		}
		if(reply.getContent()!=null){
			_reply.setContent(reply.getContent());
		}
		_reply.setActived(reply.getActived());
		baseDao.update(_reply);
	}

	@Override
	public void deleteAutoReply(Long replyId) {
		WechatReplyPo _reply = baseDao.find(WechatReplyPo.class, replyId);
		if(_reply == null){
			throw new ServiceException("被删除关键字回复不存在");
		}
		baseDao.delete(_reply);
	}

	@Override
	public List<AutoReplyVo> listAutoReply(Long businessId){
		List<WechatReplyPo> replyLst = baseDao.find("from WechatReplyPo reply where reply.businessId=?", new Object[]{businessId});
		List<AutoReplyVo> voLst = replyLst.stream()
				.map(po->mapper.map(po, AutoReplyVo.class))
				.collect(Collectors.toList());
		
		return voLst;
	}

	@Override
	public boolean addCustomMenu(CustomMenuVo menu, Long businessId) {
		//判断是不是二级
		List<Long> params = new ArrayList<>();
		params.add(businessId);
		params.add(menu.getParentId());
		if(menu.getLevel()==2 && menu.getParentId()!= 0){
			//是2级菜单，判断所属一级是否存在
			WechatCustomMenuPo _pMenu = baseDao.find(WechatCustomMenuPo.class, menu.getParentId());
			if(_pMenu == null){
				throw new ServiceException("该二级菜单所属父级菜单不存在");
			}
			//判断是否超出数量限制：一级3个，二级5个
			Long count = baseDao.getOnly("select count(*) from WechatCustomMenuPo menu where menu.businessId=? and menu.parentId=?", params);
			if(count.intValue() >= 5){
				throw new ServiceException("该二级菜单所属父级菜单，所能容纳子菜单数目已达上限：5");
			}
		}else if(menu.getLevel()==1){
			Long count = baseDao.getOnly("select count(*) from WechatCustomMenuPo menu where menu.businessId=? and menu.parentId=?", params);
			if(count.intValue() >= 3){
				throw new ServiceException("很抱歉，该公众号所能容纳一级菜单数目已达上限：3");
			}
		}
		//判断是否重名
		WechatCustomMenuPo _nMenu = baseDao.get("from WechatCustomMenuPo menu where menu.name=? and menu.businessId=?", new Object[]{menu.getName(), businessId});
		if(_nMenu != null){
			throw new ServiceException("已存在同名菜单");
		}
//		如果是view类型、设置url地址
		if("view".equals(menu.getType())){
			menu.setUrl(prop.getProperty(Wechat.ComponentConfigs.customer_menu_url)+menu.getMenuKey());
		}
		//保存至数据库
		WechatCustomMenuPo map = mapper.map(menu, WechatCustomMenuPo.class);
		map.setBusinessId(businessId);
		baseDao.save(map);
		baseDao.flush();
		return sync2WechatServer(businessId);
	}

	@Override
	public boolean updateCustomMenu(CustomMenuVo menu) {
		WechatCustomMenuPo _menu = baseDao.find(WechatCustomMenuPo.class, menu.getId());
		if(_menu == null){
			throw new ServiceException("被更新的自定义菜单不存在");
		}
		if(menu.getUrl()!=null){
			_menu.setUrl(menu.getUrl());
		}
		if(menu.getType()!=null){
			_menu.setType(menu.getType());
		}
		baseDao.update(_menu);
		baseDao.flush();
		return sync2WechatServer(_menu.getBusinessId());
	}

	@Override
	public boolean deleteCustomMenu(Long menuId) {
		WechatCustomMenuPo _menu = baseDao.find(WechatCustomMenuPo.class, menuId);
		if(_menu == null){
			throw new ServiceException("被删除自定义菜单不存在");
		}
		baseDao.delete(_menu);
		baseDao.flush();
		return sync2WechatServer(_menu.getBusinessId());
	}

    @Override
    public void changeCustomMenus(List<CustomMenuButton> menus, Long businessId) {
        List<WechatCustomMenuPo> _l1MenuLst = baseDao.find("from WechatCustomMenuPo where businessId=?", new Object[]{ businessId});
        // 删除之前的自定义菜单
        _l1MenuLst.forEach(po -> baseDao.delete(po));
        
        menus.stream().map(level1 -> {
            checkCustomMenuButton(level1);
            if (level1.getSub_button() != null && level1.getSub_button().size() > 5) {
                throw new ServiceException("一级菜单“"+ level1.getName() + "”的二级子菜单超过5个了");
            }
            if (level1.getSub_button() != null)
                level1.getSub_button().forEach(this::checkCustomMenuButton);
            return level1;
        })
        .forEach(level1 -> {
            WechatCustomMenuPo level1Po = mapper.map(level1, WechatCustomMenuPo.class);
            level1Po.setBusinessId(businessId);
            level1Po.setLevel(1);
            baseDao.save(level1Po);

            if (level1.getSub_button() != null)
                level1.getSub_button().forEach(level2 ->{
                    WechatCustomMenuPo level2Po = mapper.map(level2, WechatCustomMenuPo.class);
                    level2Po.setBusinessId(businessId);
                    level2Po.setParentId(level1Po.getId());
                    level2Po.setLevel(2);
                    baseDao.save(level2Po);
                });
        });
        baseDao.flush();
       
        if (!sync2WechatServer(businessId)) {
            throw new ServiceException("编辑自定义菜单失败");
        }
        
    }
    
    private boolean checkCustomMenuButton(CustomMenuButton menu) {
        if ("click".equals(menu.getType())) {
            if (menu.getKey() == null || "".equals(menu.getKey())) {
                throw new ServiceException("菜单“"+ menu.getName() + "” 为Click类型，key不能为空");
            }
            menu.setUrl(null);
        } else if ("view".equals(menu.getType())) {
            if (menu.getUrl() == null || "".equals(menu.getUrl())) {
                throw new ServiceException("菜单“"+ menu.getName() + "” 为视图类型，链接不能为空");
            }
            menu.setKey(null);
        } else {
            throw new ServiceException("菜单“"+ menu.getName() + "”的类型错误");
        }
        
        return true;
    }
    
	private boolean sync2WechatServer(Long businessId) {
		//**数据封装成json并发送微信服务器
	    
		//1. 数据封装,准备自定义json请求数据
		JSONObject body = new JSONObject();
		List<CustomMenuButton> btnLst = new ArrayList<>();//一个一级菜单，转换后的数据都塞这个列表里
		//1.从数据库中获取一级菜单列表
		List<WechatCustomMenuPo> _l1MenuLst = baseDao.find("from WechatCustomMenuPo menu where menu.level=? and menu.businessId=?", new Object[]{1, businessId});
		for (WechatCustomMenuPo l1Menu : _l1MenuLst) {
			//2.获取相应的子菜单列表
			List<WechatCustomMenuPo> _l2MenuLst = baseDao.find("from WechatCustomMenuPo menu where menu.parentId=? and menu.businessId=?", new Object[]{l1Menu.getId(), businessId});
			//3.进行相应转换
			CustomMenuButton translate = CustomMenuUtils.translate(l1Menu, _l2MenuLst);
			//4.塞
			btnLst.add(translate);
		}
		//这里的“button”不能改，它用于标记json序列这是一个一级菜单列表
		body.put("button", btnLst);
		LOG.debug("body:"+body.toJSONString());
		//2. 调用公众号API发送微信服务器
		//2.1获取公众号调用凭据 authorizaion_access_tooken
		BusinessInfoPo info = baseDao.get("from BusinessInfoPo info where info.businessId=?", new Object[]{businessId});
		LOG.debug("businessInfo:"+ info.toString());
		Long expired = info.getExpiresIn();
		long beginTime = info.getTokenBeginTime().getTime();
		//2.2 判断调用凭据是否过期
		String accessToken = info.getAuthorizerAccessToken();
		if(System.currentTimeMillis()-beginTime>=expired){
		    
		     //1. 校验平台调用凭据是否过期
	        WechatToken cToken = getComponent_access_token();

			//过期，刷新调用凭据
			String authorizer_appid = info.getAuthorizerAppid();
			String authorizer_refresh_token = info.getRefreshToken();
			//刷新
			WechatToken refreshAuthorizerAccessToken = WeChatUtils.refreshAuthorizerAccessToken(authorizer_appid, authorizer_refresh_token, cToken.getValue());
			accessToken = refreshAuthorizerAccessToken.getValue();
			Long newExpired  = refreshAuthorizerAccessToken.getExpired();
			String refreshToken = refreshAuthorizerAccessToken.getRefreshToken();
			//更新
			info.setAuthorizerAccessToken(accessToken);
			info.setRefreshToken(refreshToken);
			info.setTokenBeginTime(new Date());
			info.setExpiresIn(newExpired);
			
			baseDao.update(info);
		}
		//2.3 发起创建自定义菜单请求
		HttpURLConnection conn = WeChatUtils.createPostConnection(WeChatInfoUrls.custom_menu.url+accessToken);
		try {
			conn.connect();
			conn.getOutputStream().write(body.toJSONString().getBytes());
			if(conn.getResponseCode()==200){
				/*
				 * 返回结果
					正确时的返回JSON数据包如下：
					{"errcode":0,"errmsg":"ok"}
					错误时的返回JSON数据包如下（示例为无效菜单名长度）：
					{"errcode":40018,"errmsg":"invalid button name size"}
				 */
				InputStream in = conn.getInputStream();
				JSONObject resp = JSON.parseObject(WeChatUtils.readTextFromStream(in, "UTF-8"));
				Integer errcode = (Integer) resp.get(Wechat.JSONKeys.errcode);
				
				return errcode==0;
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("服务器忙...");
		}
		return false;
	}

	@Override
	public Object listCustomMenu(Long businessId) {
		//**数据封装成json并发送微信服务器
		//1. 数据封装,准备自定义json请求数据
		JSONObject body = new JSONObject();
		List<CustomMenuButton> btnLst = new ArrayList<>();//一个一级菜单，转换后的数据都塞这个列表里
		//1.从数据库中获取一级菜单列表
		List<WechatCustomMenuPo> _l1MenuLst = baseDao.find("from WechatCustomMenuPo menu where menu.level=? and menu.businessId=?", new Object[]{1, businessId});
		for (WechatCustomMenuPo l1Menu : _l1MenuLst) {
			//2.获取相应的子菜单列表
			List<WechatCustomMenuPo> _l2MenuLst = baseDao.find("from WechatCustomMenuPo menu where menu.parentId=? and menu.businessId=?", new Object[]{l1Menu.getId(), businessId});
			//3.进行相应转换
			CustomMenuButton translate = CustomMenuUtils.translate(l1Menu, _l2MenuLst);
			//4.塞
			btnLst.add(translate);
		}
		//这里的“button”不能改，它用于标记json序列表示这是一个一级菜单列表
		body.put("button", btnLst);
		
		return body;
	}
    
    @Override
    public Object listCustomMenuLink(Long businessId) {
        Map<String, String> linkMap = new HashMap<>();
        Properties self_menu_prop = WechatConfigs.self_menu_prop;
        String host = prop.getProperty(Wechat.host);
        String menu_url = prop.getProperty(Wechat.ComponentConfigs.customer_menu_url);
        
        if (self_menu_prop != null && host != null && menu_url != null) {
            self_menu_prop.forEach((key, value) -> {
                linkMap.put((String)key, host + menu_url.replace("{businessId}", businessId + "").replace("{menu}", value + ""));
            });
        } else {
            LOG.error("self_menu_prop->{}, host->{}, menu_url->{}",self_menu_prop, host , menu_url);
            throw new ServiceException(ResultInfo.SYSTEM_FAIL);
        }
        return linkMap;
    }
    
	@Override
	public boolean sendMass(String content, Long businessId) {
		
		//1. 调用公众号API发送给微信服务器
		//1.1获取公众号调用凭据 authorizaion_access_tooken
		BusinessInfoPo info = baseDao.get("from BusinessInfoPo info where info.businessId=?", new Object[]{businessId});
		Long expired = info.getExpiresIn();
		long beginTime = info.getTokenBeginTime().getTime();
		//2.2 判断调用凭据是否过期
		String accessToken = info.getAuthorizerAccessToken();
		if(System.currentTimeMillis()-beginTime>=expired){
            //1. 校验平台调用凭据是否过期
		    WechatToken cToken = getComponent_access_token();
			//过期，刷新调用凭据
			String authorizer_appid = info.getAuthorizerAppid();
			String authorizer_refresh_token = info.getRefreshToken();
			//刷新
			WechatToken refreshAuthorizerAccessToken = WeChatUtils.refreshAuthorizerAccessToken(authorizer_appid, authorizer_refresh_token,cToken.getValue());
			accessToken = refreshAuthorizerAccessToken.getValue();
			Long newExpired  = refreshAuthorizerAccessToken.getExpired();
			String refreshToken = refreshAuthorizerAccessToken.getRefreshToken();
			//更新
			info.setAuthorizerAccessToken(accessToken);
			info.setRefreshToken(refreshToken);
			info.setTokenBeginTime(new Date());
			info.setExpiresIn(newExpired);
			
			baseDao.update(info);
		}
		//2.3 准备请求体内容
		/*
		 * 请求体:
		 * {
		   "filter":{
		      "is_to_all":true
		   },
		   "text":{
		      "content":"CONTENT"
		   },
		    "msgtype":"text"
		   }
		 */
		JSONObject body = new JSONObject();
		JSONObject filter = new JSONObject();
		filter.put("is_to_all", true);
		JSONObject text = new JSONObject();
		text.put("content", content);
		body.put("filter", filter);
		body.put("text", text);
		body.put("msgtype", "text");
		
		//2.4 发起群发消息请求
		HttpURLConnection conn = WeChatUtils.createPostConnection(WeChatInfoUrls.send_mass_message.url+accessToken);
		try {
			conn.connect();
			conn.getOutputStream().write(body.toJSONString().getBytes());
			if(conn.getResponseCode()==200){
				/*
				 * 返回结果
					正确时的返回JSON数据包如下：
					{"errcode":0,"errmsg":"ok"}
					错误时的返回JSON数据包如下（示例为无效菜单名长度）：
					{"errcode":40018,"errmsg":"invalid button name size"}
				 */
				InputStream in = conn.getInputStream();
				JSONObject resp = JSON.parseObject(WeChatUtils.readTextFromStream(in, "UTF-8"));
				String errcode = (String) resp.get(Wechat.JSONKeys.errcode);
				
				return "0".equals(errcode);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException("服务器忙...");
		}
		return false;
	}
	
	/**
	 * 获取 component_access_token
	 * @return
	 */
	private WechatToken getComponent_access_token() {
        //1. 校验平台调用凭据是否过期
       Cache<Object, Object> cache = cacheManager.getCache("wechatCache");
       WechatToken cToken = (WechatToken) cache.get("component_access_token");
       if(cToken==null){
           WechatToken tickey = (WechatToken) cache.get("component_verify_tickey");
           if(tickey == null){
               throw new ServiceException("服务器忙，请您10分钟再操作");
           }
           cToken = WeChatUtils.refreshComponentAccessToken("component_access_token", tickey.getValue());
           cache.put("component_access_token", cToken);
       }
       return cToken;
    }

}
