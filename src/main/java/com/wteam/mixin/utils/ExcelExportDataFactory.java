package com.wteam.mixin.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.utils.ExcelUtils.ExcelExportData;

/**
 * 创建 不同数据导出的excel表信息
 * {@link ExcelUtils}
 * @author benko
 *
 */
public class ExcelExportDataFactory {

    /**
     * 商家账务记录
     * @param list
     * @param businessId
     * @return
     */
    public static ExcelExportData businessRecord(List<?> list, Long businessId) {

        ExcelExportData setInfo = new ExcelExportData();
        List<String[]> columNames = new ArrayList<String[]>();
        columNames.add(new String[] { "商家编号", "金额","之前的余额", "来源", "来源编号", "电话", "创建时间" });
//        columNames.add(new String[] { "运单号", "代理人" });

        List<String[]> fieldNames = new ArrayList<String[]>();
        fieldNames.add(new String[] { "businessId", "money" , "businessBeforeMoney" , "sourceName" , "sourceId" , "tel", "createTime" });
//        fieldNames.add(new String[] { "awbNumber", "agent" });

        LinkedHashMap<String, List<?>> map = new LinkedHashMap<String, List<?>>();
        map.put("商家"+businessId+"财务报表", list);

        setInfo.setDataMap(map);
        setInfo.setFieldNames(fieldNames);
        setInfo.setTitles(new String[] { "商家"+businessId+"财务报表"});
        setInfo.setColumnNames(columNames);

        return setInfo;
    }

    /**
     * 订单
     * @param list
     * @param businessId 订单所属的商家 可以为null
     * @return
     */
    public static ExcelExportData customerOrder(List<CustomerOrderVo> list, Long businessId, boolean isSuper) {


        ExcelExportData setInfo = new ExcelExportData();
        List<String[]> columNames = new ArrayList<String[]>();
//        columNames.add(new String[] { "运单号", "代理人" });

        List<String[]> fieldNames = new ArrayList<String[]>();
//        fieldNames.add(new String[] { "awbNumber", "agent" });
        list.forEach(order -> {
            if(order.getWechatName() != null)
                order.setAccount(order.getWechatName());
        });
        if (isSuper) {
            columNames.add(new String[] { "订单号","用户名","手机号","充值套餐","真实充值套餐",
                "零售价","成本价","手续费","商家回调","商家编号",
                "失败信息","号段","状态","创建时间"});
            fieldNames.add(new String[] { "orderNum", "account" , "phone" , "productName" , "realProductName" ,
                "retailPrice", "cost", "factorage", "isBusinessCallback", "businessId",
                "failedInfo","haoduan","stateName","createTime",});
        }
        else {
            columNames.add(new String[] { "订单号","用户名","手机号","充值套餐",
                "零售价","成本价","手续费","商家回调","商家编号",
                "号段","状态","创建时间"});
            fieldNames.add(new String[] { "orderNum", "account" , "phone" , "productName" ,
                "retailPrice", "cost", "factorage", "isBusinessCallback", "businessId",
                "haoduan","stateName","createTime"});
        }
        String sheetName = businessId != null ? "商家"+businessId+"订单报表" : "米信订单报表";
        LinkedHashMap<String, List<?>> map = new LinkedHashMap<String, List<?>>();
        map.put(sheetName, list);

        setInfo.setDataMap(map);
        setInfo.setFieldNames(fieldNames);
        setInfo.setTitles(new String[] {sheetName});
        setInfo.setColumnNames(columNames);

        return setInfo;
    }

}
