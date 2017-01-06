package com.wteam.mixin.utils;


import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.validation.BindingResult;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.exception.ValidateException;
import com.wteam.mixin.function.IHandle;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.utils.ExcelUtils.ExcelExportData;


/**
 * Spring，主要是控制层相关的工具类
 *
 * @version 1.0
 * @author benko
 * @time
 */
public class SpringUtils {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(SpringUtils.class.getName());

    /**
     * 处理请求数据验证结果, 如果不符合验证要求, 抛出 ValidateException 异常
     * @param results 验证结果
     */
    public static void validate(BindingResult... results) {
        JSONObject json = new JSONObject();
        boolean isValidate = true;
        for (int i = 0; i < results.length; i++ ) {
            BindingResult result = results[i];
            if (result.getErrorCount() != 0) {
                isValidate = false;
                JSONObject jsonErrors = new JSONObject();
                result.getFieldErrors().forEach(error -> {
                    jsonErrors.put(error.getField(), error.getDefaultMessage());
                });
                json.put(result.getObjectName(), jsonErrors);
            }
        }

        // 抛出验证使用异常
        if (!isValidate) throw new ValidateException(json);
    }


    /**
     * 向http输出数据流的通用处理
     * @param result
     * @param response
     * @param handle
     */
    public static void outputHttpHandle(final ResultMessage result,final HttpServletResponse response, IHandle handle) {
        // 错误处理
        Consumer<ResultMessage> errorHandle = SpringUtils.func_errorHandle(response);

        try {
            if (result.getResultCode() != null && !result.isSuccess()) {
                LOG.error("first");
                errorHandle.accept(result);
                return;
            }
            // 业务处理
            handle.handle();

            result.setSuccessInfo("");
        }
        catch (ServiceException e) {
            result.setInfo(e.getCode(), e.getInfo());
            LOG.error(e.getInfo());
        }
        catch (Exception e) {
            result.setInfo(ResultInfo.SYSTEM_FAIL);
            LOG.error("",e);
        }
        finally {
            if (!result.isSuccess()) {
                errorHandle.accept(result);
            }
        }
    }


    /**
     * 将错误信息处理输出到response函数
     * @param response
     * @return
     */
    private static Consumer<ResultMessage> func_errorHandle(final HttpServletResponse response) {
        // 错误处理
        return  result -> {
            try {
                response.getWriter().write(JSON.toJSONString(result));
                response.getWriter().flush();
                response.getWriter().close();
            }
            catch (IOException e) {
                LOG.error("",e);
            }
        };
    }
}
