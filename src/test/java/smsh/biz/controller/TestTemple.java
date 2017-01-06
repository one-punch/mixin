package smsh.biz.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.factory.MockLinkerFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

/**
 *
 * @author benko
 * @version 2016年6月13日
 * @see TestTemple
 * @since
 */
@SuppressWarnings("unused")
public class TestTemple extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;


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
        MockGenerateFactory generateFactory = new MockGenerateFactory(baseDao, mapper);
        /**
         *  shiro集成测试时设置权限，不使用时可将其注释，减少测试时间
         */
//        generateFactory.role.generate(true);
/*
        // 获取角色
        RolePo supermanagerRole = generateFactory.role.generate(0);
        RolePo userRole = generateFactory.role.generate(1);
        List<RolePo> supermanger_user = Arrays.asList(supermanagerRole, userRole);

        // 保存权限关联角色
        LinkHandler.manyToMany(new PermissionPo("/test/user"), userRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/test/supermanager"), supermanagerRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/test/all"), supermanger_user, linkerFactory.permissionToRole);

        // 保存用户关联角色
        UserPo userPo1 = generateFactory.user.generate(0);
        LinkHandler.manyToMany(userPo1, userRole, linkerFactory.userToRole);
        UserPo userPo2 = generateFactory.user.generate(1);
        LinkHandler.manyToMany(userPo2, supermanagerRole, linkerFactory.userToRole);
*/
        /**
         * 测试一个接口时，请注释掉其它生成数据
         */
        generateFactory = new MockGenerateFactory(baseDao, mapper);
        linkerFactory = new MockLinkerFactory();
//        generateFactory.role.generate(true);

        // 获取角色
/*        RolePo superRole = generateFactory.role.generate(0);
        RolePo businessRole = generateFactory.role.generate(1);
        RolePo customerRole = generateFactory.role.generate(2);*/
        RolePo superRole = baseDao.findUniqueByProperty("role", RoleType.supermanager, RolePo.class);
        RolePo businessRole = baseDao.findUniqueByProperty("role", RoleType.bussiness, RolePo.class);
        RolePo customerRole = baseDao.findUniqueByProperty("role", RoleType.customer, RolePo.class);
        RolePo proxyBusinessRole = baseDao.findUniqueByProperty("role", RoleType.proxy_business, RolePo.class);
        RolePo disabledRole = baseDao.findUniqueByProperty("role", RoleType.disabled, RolePo.class);
        List<RolePo> super_businessRole = Arrays.asList(superRole, businessRole);
        List<RolePo> customer_businessRole = Arrays.asList(customerRole, businessRole);
        List<RolePo> proxyBusiness_businessRole = Arrays.asList(proxyBusinessRole, businessRole);
        List<RolePo> super_proxyBusiness_businessRole = Arrays.asList(superRole,proxyBusinessRole, businessRole);
        List<RolePo> customer_proxyBusiness_businessRole = Arrays.asList(customerRole,proxyBusinessRole, businessRole);


        // 会员 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/member/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/vaildity/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/vaildity/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/list"), super_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/list"), super_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/delete"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/buy"), businessRole, linkerFactory.permissionToRole);
        }

        // 流量 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/list"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/trafficplans/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/trafficplans"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/api/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/api/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/api/list/super"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/business/trafficplan/edit"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/traffic/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/api/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/traffic/list/customer"), customerRole, linkerFactory.permissionToRole);
        }
        // 主题 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/theme/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/set_default"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/theme/list/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/theme/list"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/buy"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/theme/business/choose"), businessRole, linkerFactory.permissionToRole);
        }
        // 系统 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/system/dconfig"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/dconfig/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/doc/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/doc/names"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/businessmenu/edit"), superRole, linkerFactory.permissionToRole);
            // "/system/doc/{name}"

            LinkHandler.manyToMany(new PermissionPo("/system/businessmenu"), super_businessRole, linkerFactory.permissionToRole);

        }
        // 微信模块 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/new"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/edit"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/delete"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/list"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/new"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/edit"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/delete"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/change"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/list"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/link/list"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/message/send"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
        }
        // 订单 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/order/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/list/super/download"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/state/change"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/order/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/submit/business/balance"), proxyBusiness_businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/order/order/list/my"), customer_proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/submit"), customer_proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/pay"), customer_proxyBusiness_businessRole, linkerFactory.permissionToRole);
        }
        // 流量充值
        {
            LinkHandler.manyToMany(new PermissionPo("/recharge/api/authoried/change"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/api/config/change/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/api/config/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/api/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/recallback"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/recallback/all"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/shoudan"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/recharge/api/config/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/recharge/api/config/change/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);

        }
        // 账务 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/balance/business/all/refresh"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/business/all/refresh"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/business/refresh"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/super/business"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/super/business/download"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/platform"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/super/business"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/super/business/add_or_subtract"), superRole, linkerFactory.permissionToRole);// -2
            LinkHandler.manyToMany(new PermissionPo("/balance/super/business/delete"), superRole, linkerFactory.permissionToRole);// -2
            LinkHandler.manyToMany(new PermissionPo("/withdraw/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/pass"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/reject"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/balance/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/business/download"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/list/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/submit"), proxyBusiness_businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/balance/list/proxybusiness/download"), super_proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/proxybusiness"), super_proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/proxybusiness"), super_proxyBusiness_businessRole, linkerFactory.permissionToRole);
        }
        // 广告 -1
        {
            LinkHandler.manyToMany(new PermissionPo("/banners/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/banners/business/save"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/banners/business/delete"), businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/banners/customer"), customer_businessRole, linkerFactory.permissionToRole);
        }
        // 用户 -2
        {
            LinkHandler.manyToMany(new PermissionPo("/user/admin"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/list/super"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/business/proxy/list"), super_proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/user/changePassword"), super_proxyBusiness_businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/user/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/payconfig"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/payconfig/edit"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
        }
        // 微信 -2
        {
            LinkHandler.manyToMany(new PermissionPo("/wechat/bind"), proxyBusiness_businessRole, linkerFactory.permissionToRole);
        }
        // 登录
        {
            LinkHandler.manyToMany(new PermissionPo("/create/business"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/create/proxy/business"), proxyBusiness_businessRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/register/business"), disabledRole, linkerFactory.permissionToRole);

        }

/*
        LinkHandler.manyToMany(new PermissionPo(), super_businessRole, linkerFactory.permissionToRole);

        LinkHandler.manyToMany(new PermissionPo(), customer_businessRole, linkerFactory.permissionToRole);

        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), superRole, linkerFactory.permissionToRole);

        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), businessRole, linkerFactory.permissionToRole);

        LinkHandler.manyToMany(new PermissionPo(), customer_businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), customerRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo(), customerRole, linkerFactory.permissionToRole);*/

    }

    /**
     * 向数据库插入数据，不回滚
     */
    @Test
    @Rollback(false)
    public void testNotRollback() {}

    /**
     * Description: 测试模板<br>
     *
     * @throws Exception
     * @see
     */
    @Test
    public void test_() throws Exception {
        // 请求路径
        String url = "/test/user";

        {// 测试成功
            RequestBuilder builder = get(url)
            // .param("category", category + "")
            ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();
        }

/*
        {// 测试失败
            RequestBuilder builder = get(url)
            // .param("category", category + "")
            ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect( jsonPath("$.isSuccess").value(false))
                .andReturn();
        }
*/
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
        String url = "";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("user", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            // .param("category", category + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
/*
        {// 测试失败
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            // .param("category", category + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect( jsonPath("$.isSuccess").value(false))
                .andReturn();
        }
*/
    }
}
