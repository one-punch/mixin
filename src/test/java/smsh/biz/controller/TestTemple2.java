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
 * @see TestTemple2
 * @since
 */
@SuppressWarnings("unused")
public class TestTemple2 extends BaseControllerTest {
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

        // 新增的
        LinkHandler.manyToMany(new PermissionPo("/order/list/super/download"), superRole, linkerFactory.permissionToRole);
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
