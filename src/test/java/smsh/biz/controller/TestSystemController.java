package smsh.biz.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.ISystemService;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.BusinessMenuVo;
import com.wteam.mixin.model.vo.DConfigVo;
import com.wteam.mixin.model.vo.DocumentVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.GWWMockGenerateFactory;
import test.hibernate.factory.MockGenerateFactory;
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
public class TestSystemController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    @Autowired
    private ISystemService systemService;


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

        // 保存用户关联角色
        UserPo superUser = generateFactory.user.generate(0);
        LinkHandler.manyToMany(superUser, businessRole, linkerFactory.userToRole);
        UserPo business = generateFactory.user.generate(1);
        LinkHandler.manyToMany(business, superRole, linkerFactory.userToRole);

//      数据插入
        generateFactory.config.generate(true);
        generateFactory.menu.generate(true);
        generateFactory.doc.generate(true);
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
    public void testGetDConfig() throws Exception {
        // 请求路径
        String url = "/system/dconfig";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
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
    public void testEditDConfig() throws Exception {
        // 请求路径
        String url = "/system/dconfig/edit";
        Cookie cookie = null;

        DConfigVo config = new DConfigVo("0.01", "50000");
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("config", JSON.toJSONString(config))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();

        Map<String, String> dConfig = systemService.getDConfig();
        System.out.println("after:");
        System.out.println(dConfig);
    }

    @Test
    public void testListBusinessMenu() throws Exception {
        // 请求路径
        String url = "/system/businessmenu";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
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
    public void testEditBusinessMenu() throws Exception {
        // 请求路径
        String url = "/system/businessmenu/edit";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        BusinessMenuVo menus = new BusinessMenuVo("a---1", "用户统计", "very hot");
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("menus", JSON.toJSONString(menus));
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        System.out.println("after:");
        List<BusinessMenuVo> listBusinessMenu = systemService.listBusinessMenu();
        for (BusinessMenuVo businessMenuVo : listBusinessMenu) {
			System.out.println(businessMenuVo.toString());
		}
    }

    @Test
    public void testListDocName() throws Exception {
        // 请求路径
        String url = "/system/doc/names";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
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
    public void testgetDocByName() throws Exception {
        // 请求路径
        String url = "/system/doc/卜算子";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
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
    public void testEditDoc() throws Exception {
        // 请求路径
        String url = "/system/doc/edit";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        DocumentVo doc = new DocumentVo("卜算子", "江城子", "十年生死两茫茫不思量 自难忘千里孤坟 无处话凄凉纵使相逢应不识 尘满面 鬓如霜夜来幽梦忽还乡小轩窗 正梳妆相顾无言 惟有泪千行");

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("doc", JSON.toJSONString(doc))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();

        DocumentVo docByName = systemService.getDocByName("卜算子");
        System.out.println("after:"+docByName.toString());
    }

}
