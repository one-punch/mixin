package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.MemberVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.SpringUtils;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by zbin on 17/1/17.
 */
@RestController
@RequestMapping("/business")
public class ActivityController {

//    Query q = sessionFactory.getCurrentSession().createQuery(hql);
//    if (param != null && param.length > 0) {
//        for (int i = 0; i < param.length; i++) {
//            q.setParameter(i, param[i]);
//        }
//    }
//    return q.list();

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IBaseDao baseDao;

    @RequestMapping(value="/preselection", method={RequestMethod.GET})
    public ResultMessage addMember(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                   ResultMessage resultMessage){

        String sql = "SELECT userId, acount FROM UserPo WHERE userId != :adminId AND userId NOT IN (SELECT userId FROM ActivityBusiness)";
        List<UserVo> preselectionUsers = baseDao.get(sql, new Object[]{user.getUserId()});
        return resultMessage.setSuccessInfo("").putParam("preselectionIds", preselectionUsers);
    }
}
