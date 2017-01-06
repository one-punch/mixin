package test.hibernate.factory;


import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.po.WechatCustomMenuPo;
import com.wteam.mixin.model.po.WechatReplyPo;
import com.wteam.mixin.model.po.WithdrawPo;

import test.hibernate.link.ILinker;


/**
 * 数据库对象关联工厂
 *
 * @author benko
 * @version 2016年6月17日
 * @see MockLinkerFactory
 * @since
 */
public class MockLinkerFactory {

    // 这是标准写法，下面是简化写法
    // public final ILinker<PermissionPo, RolePo> permissionToRole = new ILinker<PermissionPo, RolePo>() {
    //      @Override
    //      public void link(PermissionPo t, RolePo s) {
    //          t.getRoles().add(s);
    //      }
    // };

    /**
     * 用户限关系角色
     */
    public final ILinker<UserPo, RolePo> userToRole = (t, s) -> {
        t.getRoles().add(s);
    };

    /**
     * 用户限关系角色
     */
    public final ILinker<BusinessInfoPo, UserPo> businessInfo2user = (t, s) -> {
    	t.setBusinessId(s.getUserId());
    };

    /**
     * 权限关系角色
     */
    public final ILinker<PermissionPo, RolePo> permissionToRole = (t, s) -> {
        t.getRoles().add(s);
    };

    /**
     * 商家关系广告
     */
    public final ILinker<UserPo, BusinessBannerPo> userTobusinessBanner = (t, s) -> {
        s.setBusinessId(t.getUserId());
    };


    /**
     * 会员关系会员流量套餐
     */
    public final ILinker<MemberPo, MemberTrafficPlanPo> member2MemPlan = (t, s) -> {
        s.setMemberId(t.getId());
    };

    /**
     * 会员流量套餐关联流量套餐
     */
    public final ILinker<MemberTrafficPlanPo, TrafficPlanPo> MemPlan2plan = (t, s) -> {
    	t.setTrafficplanId(s.getId());
    };
    /**
     * 会员关联有效期
     */
    public final ILinker<MemberVaildityPo, MemberPo> Vail2Member=(t, s)->{
    	t.setMemberId(s.getId());
    };
    /**
     * 商家关联会员有效期
     */
    public final ILinker<BusinessInfoPo, MemberPo> business2Member=(t, s)->{
    	t.setMemberId(s.getId());
    };

    /**
     * 商家主题关联主题
     */
    public final ILinker<BusinessThemePo, ThemePo> businessTheme2Theme=(t, s)->{
    	t.setThemeId(s.getId());
    };
    /**
     * 商家主题关联商家
     */
    public final ILinker<BusinessInfoPo, BusinessThemePo> business2BusinessTheme=(t, s)->{
    	s.setBusinessId(t.getBusinessId());
    };
    /**
     * 流量分组关联流量套餐
     */
    public final ILinker<TrafficGroupPo, TrafficPlanPo> group2plan=(t, s)->{
    	s.setTrafficGroupId(t.getId());
    };
    /**
     * 商家关联商家流量套餐
     */
    public final ILinker<UserPo, BusinessTrafficPlanPo> busi2busiPlan=(t, s)->{
    	s.setBusinessId(t.getUserId());
    };
    /**
     * 商家流量套餐关联流量套餐
     */
    public final ILinker<BusinessTrafficPlanPo, TrafficPlanPo> busiPlan2plan=(t, s)->{
    	t.setTrafficplanId(s.getId());
    };
    /**
     * 商家关联商家财务记录
     */
    public final ILinker<UserPo, BusinessBalanceRecordPo> business2busiBalance=(t, s)->{
    	s.setBusinessId(t.getUserId());
    };
    /**
     * 商家关联提现申请
     */
    public final ILinker<UserPo, WithdrawPo> business2withdraw=(t, s)->{
    	s.setBusinessId(t.getUserId());
    };
    /**
     * 用户关联顾客信息
     */
    public final ILinker<UserPo, CustomerInfoPo> customer2info =(t, s)->{
        s.setCustomerId(t.getUserId());
    };
    /**
     * 用户关联顾客订单
     */
    public final ILinker<UserPo, CustomerOrderPo> customer2order =(t, s)->{
        s.setCustomerId(t.getUserId());
    };
    /**
     * 商家关联顾客订单
     */
    public final ILinker<UserPo, CustomerOrderPo> business2order =(t, s)->{
        s.setBusinessId(t.getUserId());
    };

    /**
     * 商家关联公众号自动回复
     */
    public final ILinker<UserPo, WechatReplyPo> business2reply =(t, s)->{
        s.setBusinessId(t.getUserId());
    };
    /**
     * 商家关联自定义菜单
     */
    public final ILinker<UserPo, WechatCustomMenuPo> business2menu =(t, s)->{
        s.setBusinessId(t.getUserId());
    };
    /**
     * 一级菜单关联二级菜单
     */
    public final ILinker<WechatCustomMenuPo, WechatCustomMenuPo> menuParent2child =(t, s)->{
    	s.setParentId(t.getId());
    };
}
