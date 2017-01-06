package smsh.biz.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.ITrafficService;
import com.wteam.mixin.hibernate.link.LinkerFactory;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.TrafficPlanVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.GWWMockGenerateFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

public class TestTrafficController extends BaseControllerTest{
	
	 /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;
    
    @Autowired
    private ITrafficService trafficService;

    /** web测试环境*/
    private MockMvc mockMvc;
    
    private List<UserPo> userLst;
    
    private List<TrafficGroupPo> groupLst;
    
    private List<TrafficPlanPo> planLst;

	private BusinessInfoPo business;

	private MemberPo member;

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
        
        //插入数据库
        userLst = generateFactory.user.generate(true);
        business = generateFactory.busi.generate(2, true);
        member = generateFactory.member.generate(0, true);
        MemberVaildityPo vail = generateFactory.vail.generate(0, true);
        MemberTrafficPlanPo memPlan = generateFactory.memPlan.generate(0, true);
        BusinessTrafficPlanPo busiPlan = generateFactory.busiPlan.generate(0, true);
      
        groupLst = generateFactory.group.generate(true);
        planLst = generateFactory.plan.generate(true);
        
        LinkHandler.oneToOne(business, userLst.get(1), linkerFactory.businessInfo2user);
        LinkHandler.oneToOne(business, member, linkerFactory.business2Member);
        LinkHandler.oneToOne(member, memPlan, linkerFactory.member2MemPlan);
        LinkHandler.oneToOne(memPlan, planLst.get(0), linkerFactory.MemPlan2plan);
        
        LinkHandler.oneToOne(userLst.get(1), busiPlan, linkerFactory.busi2busiPlan);
        LinkHandler.oneToOne(busiPlan, planLst.get(0), linkerFactory.busiPlan2plan);
   
        //数据关联
        LinkHandler.oneToMany(groupLst.get(0), planLst.subList(0, 4), linkerFactory.group2plan);
        LinkHandler.oneToMany(groupLst.get(4), planLst.subList(4, 7), linkerFactory.group2plan);
        LinkHandler.oneToMany(groupLst.get(7), planLst.subList(7, 9), linkerFactory.group2plan);
    }

    /**
     * 
     * Description: 登录权限才可访问的URL的测试模板<br>
     * 
     * 1、在setUp()向数据库加入用户信息<br>
     * 2、用户登录<br>
     * 3、从用户登录的response获取Cookie值<br>
     * 4、将Cookie值加入测试resquest中<br>
     * 5、执行测试<br>
     * 
     * @throws Exception 
     * @see
     */
    @Test
    public void testAfterLogin_() throws Exception {
        // 请求路径 
        String url = "/banners/business";
        Cookie cookie = null;
        
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
        // .param("category", category + "")
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListTrafficGroupBySuper() throws Exception{
        // 请求路径 
        String url = "/trafficgroup/list";
        Cookie cookie = null;
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        /**
         * 测试
         */
        /*
         * 请求参数：
         * 	*int  provider
			*str  province
			*boolean hasInfo
			* int pageNo 
			* int pageSize 
         */
        /*
         * 分页查询---查询参数全
         * RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("provider", "0")
            .param("province", "广东")
            .param("hasInfo", "true")
            .param("pageNo", "1")
            .param("pageSize", "2")
            ;
         */
        
        /*
         * 分页查询----查询参数不全
         *   RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("province", "广东")
                .param("hasInfo", "true")
                .param("pageNo", "1")
                .param("pageSize", "2")
                ;
         */
        
        /*
         * 非分页查询----查询参数全
         *   RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("provider", "1")
                .param("province", "全国")
                .param("hasInfo", "false")
                ;
         */
        
        /*
         * 非分页查询----查询参数全
         *   RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("provider", "1")
                .param("province", "全国")
                .param("hasInfo", "false")
                ;
         */
        
        /*
         * 非分页查询----查询参数不全
         *    RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                ;
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListTrafficGroupByBusiness() throws Exception {
    	 // 请求路径 
        String url = "/trafficgroup/list/business";
        Cookie cookie = null;
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        /**
         * 测试
         */
        /*
         * 请求参数：
         * 	*int  provider
			*str  province
			*boolean hasInfo
			* int pageNo 
			* int pageSize 
         */
        /*
         * 分页查询---查询参数全
         * RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("provider", "0")
            .param("province", "广东")
            .param("hasInfo", "true")
            .param("pageNo", "1")
            .param("pageSize", "2")
            ;
         */
        /*
         * 分页查询----查询参数不全
         *   RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("hasInfo", "true")
                .param("pageNo", "1")
                .param("pageSize", "10")
                ;
         */
        
        /*
         * 非分页查询----查询参数全
         *   RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("provider", "0")
                .param("province", "广东")
                .param("hasInfo", "false")
                ;
         */
        
        /*
         * 非分页查询----查询参数不全
         *    RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                ;
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("provider", "2")
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testAddTrafficPlanIntoGroup() throws Exception {
        // 请求路径 
        String url = "/trafficgroup/trafficplans/add";
        Cookie cookie = null;
        
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        String[] ids = new String[planLst.size()];
        int i=0;
        for (TrafficPlanPo plan : planLst) {
			ids[i++] = String.valueOf(plan.getId());
		}
        /*
         * 
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("trafficgroupId", String.valueOf(groupLst.get(0).getId()))
            .param("trafficplanIds", ids)
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        List<TrafficPlanVo> planLst = trafficService.listTrafficPlanByGroup(groupLst.get(0).getId());
        for (TrafficPlanVo plan : planLst) {
			System.out.println(plan.toString());
		}
    }
    
    @Test
    public void testRemoveTrafficPlanFromGroup() throws Exception {
        // 请求路径 
        String url = "/trafficgroup/trafficplans/delete";
        Cookie cookie = null;
        
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        String[] ids = new String[5];
        int i=0;
        for (TrafficPlanPo plan : planLst.subList(0, 5)) {
			ids[i++] = String.valueOf(plan.getId());
		}
        List<TrafficPlanVo> beforeLst = trafficService.listTrafficPlanByGroup(groupLst.get(0).getId());
        System.out.println("before: ");
        for (TrafficPlanVo plan : beforeLst) {
			System.out.println(plan.toString());
		}
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("trafficgroupId", String.valueOf(groupLst.get(0).getId()))
            .param("trafficplanIds", ids)
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        List<TrafficPlanVo> planLst = trafficService.listTrafficPlanByGroup(groupLst.get(0).getId());
        System.out.println("after: ");
        for (TrafficPlanVo plan : planLst) {
			System.out.println(plan.toString());
		}
    }
    
    @Test
    public void testListTrafficPlanBySuper() throws Exception {
        // 请求路径 
        String url = "/trafficplan/list";
        Cookie cookie = null;
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        /*
         * 没有分组的情况：其他参数齐全
         *  RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
        	.param("provider", "0")
        	.param("province", "广东")
        	.param("pageNo", "1")
        	.param("pageSize", "10")
            ;
         */
        
        /*
         * 没有分组的情况：只有省份或运营商
         *  RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            	.param("provider", "0")
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;
            	
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            	.param("province", "湖南")
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;
         */
        
        /*
         * 没有分组的情况：参数全没
         *  RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;
         */
        
        /*
         * 有分组的情况：其他参数设置无效
         *  RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("trafficgroupId",String.valueOf(groupLst.get(0).getId()))
        		.param("provider","2")
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("trafficgroupId",String.valueOf(groupLst.get(0).getId()))
        		.param("provider","2")
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testUpdateTrafficPlan() throws Exception {
        // 请求路径 
        String url = "/trafficplan/edit";
        Cookie cookie = null;
        
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        TrafficPlanVo plan = new TrafficPlanVo();
        plan.setId(planLst.get(0).getId());
        plan.setCost(new BigDecimal(99999));
        
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("trafficplan", JSON.toJSONString(plan))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, planLst.get(0).getId());
        System.out.println(_plan.toString());
    }
    
    @Test
    public void testListTrafficPlanByBusiness() throws Exception {
        // 请求路径 
        String url = "/trafficplan/list/business";
        Cookie cookie = null;
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("trafficgroupId",String.valueOf(groupLst.get(0).getId()))
        		.param("provider","2")
            	.param("pageNo", "1")
            	.param("pageSize", "10")
            	;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListPlanNGroup() throws Exception {
        // 请求路径 
        String url = "/traffic/list/business";
        Cookie cookie = null;
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
        		.param("provider","2")
            	;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
