package com.wteam.mixin.model.query;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;

/**
 * 查询对象
 * @author benko
 */
public class BusinessBlanceRecordQuery extends AbstractQuery implements IValueObject{
    /**ID*/
    private Long id;

    /**所属商家ID*/
    private Long businessId;

    /**增减金额,正为进,负为出*/
    private BigDecimal money;

    /**该项记录的来源:
        0：账户充值
        1：已结算转入
        2：订单成本转出
        3：提现
        4：增值业务
     */
    private Integer source;

    /**来源ID*/
    private Long sourceId;

    /**备注*/
    private String info;

    /**电话*/
    private String tel;

    /** 是否删除 */
    private Boolean isDelete;

    /** 开始时间 */
    private Date startAt;

    /** 结束时间 */
    private Date endAt;

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public BusinessBlanceRecordQuery() {
    }

    public BusinessBlanceRecordQuery(Long businessId) {
        this.businessId = businessId;
    }

    /**
     * 生成hql查询条件
     * @param as 查询对象的别名
     * @return
     */
    @Override
    public String hqlQuery(String as) {

        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (businessId != null) {
            query += String.format(" %sbusinessId=%d ", name,businessId);
        }
        if (source != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %ssource=%d ", relation, name,source);
        }
        if (tel != null && !"".equals(tel)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %stel='%s' ", relation, name,tel);
        }
        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        if (startAt != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %screateTime>='%s' ", relation, name, FORMAT.format(startAt));
        }
        if (endAt != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %screateTime<='%s' ", relation, name, FORMAT.format(endAt));
        }

        return query;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }


}
