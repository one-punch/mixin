package smsh.biz.dao;


import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.utils.IdWorker;

import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.factory.MockLinkerFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseJunit4Test;


public class TestInit extends BaseJunit4Test {

    MockGenerateFactory generateFactory;

    MockLinkerFactory linkerFactory;

    @Autowired
    IdWorker orderIdWorker;
    
    @Before
    public void setUp() { 
        
        generateFactory = new MockGenerateFactory(baseDao, mapper);
        linkerFactory = new MockLinkerFactory();
        generateFactory.role.generate(true);

        // 获取角色
/*        RolePo superRole = generateFactory.role.generate(0);
        RolePo businessRole = generateFactory.role.generate(1);
        RolePo customerRole = generateFactory.role.generate(2);*/
        RolePo superRole = baseDao.findUniqueByProperty("role", RoleType.supermanager, RolePo.class);
        RolePo businessRole = baseDao.findUniqueByProperty("role", RoleType.bussiness, RolePo.class);
        RolePo customerRole = baseDao.findUniqueByProperty("role", RoleType.customer, RolePo.class);
        List<RolePo> super_businessRole = Arrays.asList(superRole, businessRole);
        List<RolePo> customer_businessRole = Arrays.asList(customerRole, businessRole);
        
        // 会员
        LinkHandler.manyToMany(new PermissionPo("/member/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/delete"), superRole, linkerFactory.permissionToRole); 
        LinkHandler.manyToMany(new PermissionPo("/member/edit"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/vaildity/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/vaildity/delete"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/list"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/list"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/edit"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/delete"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/buy"), businessRole, linkerFactory.permissionToRole);

        // 流量
        {
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/list"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/add"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/trafficplans/delete"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/trafficplans"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/trafficplan/edit"), superRole, linkerFactory.permissionToRole);
            
            LinkHandler.manyToMany(new PermissionPo("/trafficgroup/list/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/trafficplan/edit"), businessRole, linkerFactory.permissionToRole);
            
            LinkHandler.manyToMany(new PermissionPo("/traffic/list/customer"), customerRole, linkerFactory.permissionToRole);
        }
        // 主题
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
        // 系统
        {
            LinkHandler.manyToMany(new PermissionPo("/system/dconfig"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/dconfig/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/businessmenu/edit"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/doc/names"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/system/doc/edit"), superRole, linkerFactory.permissionToRole);
            // "/system/doc/{name}"

            LinkHandler.manyToMany(new PermissionPo("/system/businessmenu"), super_businessRole, linkerFactory.permissionToRole);
            
        }
        // 微信模块
        {
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/new"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/edit"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/delete"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/reply/list"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/new"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/edit"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/delete"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/change"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/list"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/menu/link/list"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/business/wechat/message/send"), businessRole, linkerFactory.permissionToRole);
        }
        // 订单
        {
            LinkHandler.manyToMany(new PermissionPo("/order/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/state/change"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/order/list/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/submit/business/balance"), businessRole, linkerFactory.permissionToRole);
            
            LinkHandler.manyToMany(new PermissionPo("/order/list/my"), customer_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/submit"), customer_businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/order/pay"), customer_businessRole, linkerFactory.permissionToRole);
        }
        // 账务
        {
            LinkHandler.manyToMany(new PermissionPo("/balance/platform"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/list/super"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/pass"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/reject"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/super/business"), superRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/super/business"), superRole, linkerFactory.permissionToRole);

            LinkHandler.manyToMany(new PermissionPo("/balance/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/balance/list/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/list/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/withdraw/submit"), businessRole, linkerFactory.permissionToRole);
        }
        // 广告
        {
            LinkHandler.manyToMany(new PermissionPo("/banners/business"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/banners/business/save"), businessRole, linkerFactory.permissionToRole);
            LinkHandler.manyToMany(new PermissionPo("/banners/business/delete"), businessRole, linkerFactory.permissionToRole);
            
            LinkHandler.manyToMany(new PermissionPo("/banners/customer"), customer_businessRole, linkerFactory.permissionToRole);
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
     * log4j实例对象.
     */
    private static Logger logger = LogManager.getLogger(TestInit.class.getName());

    @Test
    public void test() {
        System.out.println("asdfasdfas");
        System.out.println("TestInit.test()");

        for (int i = 0; i < 2000; i++ ) {
            logger.trace("asdfasdfasdfasdfa");
            logger.debug("sadfasdfasdfasdfasdf");
            logger.info("asdfasdfasdfasdf");

            logger.error("asdfasdfasdfasdf");
            logger.warn("asdfasdfasdfasdfasd");
            logger.fatal("asdfasdfasdfasdfasdfasdf");
        }
    }

    @Test
    public void testLink() {

        List<UserPo> list = baseDao.find("from UserPo");

        System.out.println("\n\n" + JSON.toJSONString(list) + "\n\n");

    }
    
    @Test
    public void testIdWorker() {
        System.out.println(orderIdWorker.nextId());
    }
}
