package com.wteam.mixin.biz.controler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wteam.mixin.biz.service.ITemporaryService;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.UserVo;

/**
 * 测试控制器
 *
 * @version 1.0
 * @author benko
 * @time 2016-5-13 19:49:04
 */
@Controller
@RequestMapping("/temporary")
public class TemporaryController {

    @Autowired
    ITemporaryService temporaryService;

    /**
     * json
     *
     * @param user1
     *            请求参数
     * @param resultMessage
     *            返回参数
     * @return
     */
    @ResponseBody
    @RequestMapping("/order/haoduan/add")
    public Result testJsonToObject(ResultMessage resultMessage) {
        temporaryService.addHaoduanToOrders();
        return resultMessage.setSuccessInfo("成功");
    }


}
