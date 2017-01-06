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

import com.wteam.mixin.constant.State;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.factory.TL_MockGenerateFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

/**
 *
 * @author benko
 * @version 2016年6月13日
 * @see TestOrderController
 * @since
 */
@SuppressWarnings("unused")
public class TestOrderController extends BaseControllerTest {
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
        TL_MockGenerateFactory generateFactory = new TL_MockGenerateFactory(baseDao, mapper);
        /**
         *  shiro集成测试时设置权限，不使用时可将其注释，减少测试时间
         */

        // 获取角色
        RolePo supermanagerRole = generateFactory.role.generate(0);
        RolePo businessRole = generateFactory.role.generate(1);
        RolePo userRole = generateFactory.role.generate(2);
        List<RolePo> business_user = Arrays.asList(businessRole, userRole);

        // 保存权限关联角色
        LinkHandler.manyToMany(new PermissionPo("/order/list/super"), supermanagerRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/order/state/change"), supermanagerRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/order/list/business"), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/order/list/my"), business_user, linkerFactory.permissionToRole);

        // 保存用户关联角色
        userPo = generateFactory.user.generate(2);
        LinkHandler.manyToMany(userPo, userRole, linkerFactory.userToRole);
        businessPo = generateFactory.user.generate(1);
        LinkHandler.manyToMany(businessPo, businessRole, linkerFactory.userToRole);
        UserPo superPo = generateFactory.user.generate(0);
        LinkHandler.manyToMany(superPo, supermanagerRole, linkerFactory.userToRole);

        /**
         * 测试一个接口时，请注释掉其它生成数据
         */
        CustomerInfoPo infoPo = generateFactory.customerInfo.generate(0, true);
        LinkHandler.oneToOne(userPo, infoPo, linkerFactory.customer2info);

        generateFactory.customerOrder.generate(true);
        // 用户关联订单
        orderPos = generateFactory.customerOrder.generate(5, 15);
        LinkHandler.oneToMany(userPo, orderPos, linkerFactory.customer2order);
        // 商家关联订单
        LinkHandler.oneToMany(businessPo, orderPos, linkerFactory.business2order);
    }

    UserPo userPo;
    UserPo businessPo;
    List<CustomerOrderPo> orderPos;
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

    /*
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
    /**
     * 超级管理员按条件分页查询订单
     * @throws Exception
     */
    @Test
    public void testAfterLogin_listBySuper() throws Exception {
        // 请求路径
        String url = "/order/list/super";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("supermanager", MD5Utils.md5("000002")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("customerId", userPo.getUserId()+"")
                .param("businessId", "")
//                .param("tel", 1 + "")
//                .param("state", 1 + "")
                .param("pageNo", 1 + "")
                .param("pageSize", 5 + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }

    }

    /**
     * 商家按条件分页查询自己的订单
     * @throws Exception
     */
    @Test
    public void testAfterLogin_listByBusiness() throws Exception {
        // 请求路径
        String url = "/order/list/business";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("tel", "18320376671")
//                .param("state", 1 + "")
                .param("pageNo", 1 + "")
                .param("pageSize", 5 + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
        /**
         * 测试 没有手机号
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("tel", "1832037671")
//                .param("state", 1 + "")
                .param("pageNo", 1 + "")
                .param("pageSize", 5 + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
    }

    /**
     * 查看自己的订单
     * @throws Exception
     */
    @Test
    public void testAfterLogin_listByMy() throws Exception {
        // 请求路径
        String url = "/order/list/my";
        Cookie cookie = null;

        /**
         * 顾客登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("customer", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("pageNo", 1 + "")
                .param("pageSize", 5 + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.resultParm.orderList.list[0].customerId").value(userPo.getUserId().intValue()))
                .andReturn();
        }

    }

    /**
     * 批量改变订单状态
     * @throws Exception
     */
    @Test
    public void testAfterLogin_stateChange() throws Exception {
        // 请求路径
        String url = "/order/state/change";
        Cookie cookie = null;

        /**
         * 顾客登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("supermanager", MD5Utils.md5("000002")));

        /**
         * 测试 一个订单
         */
        {// 测试成功
            int state = State.CustomerOrder.paySuccess;
            String[] orderIds = {orderPos.get(0).getId().toString()};
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("state", state + "")
                .param("orderIds", orderIds)
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
        /**
         * 测试
         */
        {// 测试成功
            int state = State.CustomerOrder.paySuccess;
            String[] orderIds = orderPos.stream().map(po -> po.getId().toString()).toArray(String[]::new);
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("state", state + "")
                .param("orderIds", orderIds)
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
    }

}
