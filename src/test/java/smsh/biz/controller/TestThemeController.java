package smsh.biz.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IThemeService;
import com.wteam.mixin.hibernate.link.LinkerFactory;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.BusinessBannerVo;
import com.wteam.mixin.model.vo.BusinessThemeVo;
import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;
import com.wteam.mixin.model.vo.ThemeVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.GWWMockGenerateFactory;
import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

/**
 * 
 * <p>Title:会员模块单元测试</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月27日
 */
@SuppressWarnings("unused")
public class TestThemeController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    @Autowired
    private IBaseDao baseDao;
    
    @Autowired
    private IThemeService themeService;

	private UserPo superUser;

	private UserPo business;

	private List<ThemePo> themeLst;
	
	private List<BusinessThemePo> busiThemeLst;

	private BusinessInfoPo businessInfo;
    
    /**
     * 向数据库测试使用的插入数据
     * @throws ServletException 
     */
    @Before
    public void setUp() throws ServletException {
        // 创建测试环境
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//            .alwaysExpect( content().contentType("application/json;charset=UTF-8"))
            .addFilters(super.characterEncodingFilter())
            .addFilter(super.shiroFilter(wac))
            .build();
        // 必写
        LinkHandler.setBaseDao(baseDao);
        GWWMockGenerateFactory generateFactory = new GWWMockGenerateFactory(baseDao, mapper);
        /**
         *  shiro集成测试时设置权限，不使用时可将其注释，减少测试时间
         */
        // 获取角色
        RolePo superRole = generateFactory.role.generate(0);
        RolePo businessRole = generateFactory.role.generate(1);
        
        //角色关联权限
        LinkHandler.manyToMany(new PermissionPo("/theme/list/super"), superRole, linkerFactory.permissionToRole);
        
        //获取用户
        superUser = generateFactory.user.generate(0);
        business = generateFactory.user.generate(1);
        
//        businessInfo = generateFactory.busi.generate(0);//余额不足:该账号用于模拟购买非免费主题失败情景
        businessInfo = generateFactory.busi.generate(1);//余额充足:该账号用于模拟购买非免费主题成功，续费情景
        
        //用户关联角色
        LinkHandler.manyToMany(superUser, superRole, linkerFactory.userToRole);
        LinkHandler.manyToMany(business, businessRole, linkerFactory.userToRole);
        
        LinkHandler.oneToOne(businessInfo, business, linkerFactory.businessInfo2user);
        
        themeLst = generateFactory.themeLst.generate(true);
        busiThemeLst = generateFactory.busiThemeLst.generate(true);
        //商家主题关联主题
        LinkHandler.oneToOne(busiThemeLst, themeLst.subList(0, 5), linkerFactory.businessTheme2Theme);
        //商家关联商家主题
        LinkHandler.oneToMany(businessInfo, busiThemeLst,  linkerFactory.business2BusinessTheme);
    }
    
    @Test
    public void testListTheme() throws Exception{
    	// 请求路径 
        String url = "/theme/list/super";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("pageNo", String.valueOf(1))
            .param("pageSize", String.valueOf(10));
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        Pagination page = themeService.listThemeBySuper(1, 10);
        System.out.println(page.toString());
    }
    
    @Test
    public void testupdateTheme() throws Exception{
    	// 请求路径 
        String url = "/theme/edit";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        ThemeVo theme = new ThemeVo();
        theme.setId(12121122L);
        theme.setName("这是不存在的主题");
        theme.setCost(new BigDecimal("8888"));
        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("theme", JSON.toJSONString(theme));
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        ThemeVo findTheme = themeService.findTheme(12121122L);
        System.out.println(findTheme.toString());
    }
    
    @Test
    public void testSetDefault() throws Exception{
    	// 请求路径 
        String url = "/theme/set_default";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        /**
         * 测试
         */
        ThemeVo defaultTheme = themeService.findTheme(themeLst.get(1).getId());
        System.out.println("before:"+defaultTheme);
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("themeId", String.valueOf(themeLst.get(0).getId()));
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        ThemePo theme = baseDao.get("from ThemePo theme where theme.defaulted=?", new Object[]{true});
        System.out.println("after:"+theme.toString());
    }
    
    @Test
    public void testListThemeByBusiness() throws Exception{
    	
    	String url = "/theme/list/business";
    	
    	Cookie cookies = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
    	RequestBuilder builder = get(url).cookie(cookies)
    			.param("pageNo", "1")
    			.param("pageSize", "10");
    	
    	MvcResult result = mockMvc.perform(builder)
    			.andDo(print())
    			.andExpect(jsonPath("$.isSuccess").value(true))
    			.andReturn();
    	Pagination page = themeService.listThemeByBusiness(1, 10);
    	System.out.println(page.toString());
    }
    
    @Test
    public void testListBusinessTheme() throws Exception{
    	
    	String url = "/business/theme/list";
    	
    	Cookie cookies = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
    	RequestBuilder builder = get(url).cookie(cookies);
    	
    	MvcResult result = mockMvc.perform(builder)
    			.andDo(print())
    			.andExpect(jsonPath("$.isSuccess").value(true))
    			.andReturn();
    	List<BusinessThemeVo> themeLst = themeService.listBusinessTheme(business.getUserId());
    	
    	for (BusinessThemeVo themeVo : themeLst) {
			System.out.println(themeVo);
		}
    }
    
    @Test
    public void testPurchaseTheme() throws Exception{
    	
    	String url = "/theme/buy";
    	
    	BusinessInfoPo before = baseDao.get("from BusinessInfoPo info where info.businessId=?", new Object[]{business.getUserId()});
    	System.out.println("before business balance is: "+ before.toString());
    	Cookie cookies = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
//    	RequestBuilder builder = post(url).cookie(cookies).param("themeId", String.valueOf(themeLst.get(1).getId()));//过期，重新购买
//    	RequestBuilder builder = post(url).cookie(cookies).param("themeId", String.valueOf(themeLst.get(5).getId()));//未购买，直接购买
    	RequestBuilder builder = post(url).cookie(cookies).param("themeId", String.valueOf(themeLst.get(2).getId()));//未过期续费
    	MvcResult result = mockMvc.perform(builder)
    			.andDo(print())
    			.andExpect(jsonPath("$.isSuccess").value(true))
    			.andReturn();
    	
    	List<BusinessThemeVo> listBusinessTheme = themeService.listBusinessTheme(business.getUserId());
    	for (BusinessThemeVo businessThemeVo : listBusinessTheme) {
			System.out.println(businessThemeVo.toString());
		}
    	BusinessInfoPo after = baseDao.get("from BusinessInfoPo info where info.businessId=?", new Object[]{business.getUserId()});
    	System.out.println("after business balance is: "+ before.toString());
    	BusinessBalanceRecordPo record = baseDao.get("from BusinessBalanceRecordPo record where record.businessId=?", new Object[]{business.getUserId()});
    	System.out.println("record is :" + record.toString());
    }
    

    @Test
    public void testSetupTheme() throws Exception{
    	
    	String url = "/theme/business/choose";
    	BusinessThemePo activedTheme = baseDao.get("from BusinessThemePo busiTheme where busiTheme.businessId=? and busiTheme.actived=?", new Object[]{business.getUserId(), true});
    	System.out.println("before:"+activedTheme.toString());
    	Cookie cookies = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
    	RequestBuilder builder = post(url).cookie(cookies).param("themeId", String.valueOf(themeLst.get(1).getId()));
    	MvcResult result = mockMvc.perform(builder)
    			.andDo(print())
    			.andExpect(jsonPath("$.isSuccess").value(true))
    			.andReturn();
    	BusinessThemePo after = baseDao.get("from BusinessThemePo busiTheme where busiTheme.businessId=? and busiTheme.actived=?", new Object[]{business.getUserId(), true});
    	System.out.println("after:"+after.toString());
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}





















