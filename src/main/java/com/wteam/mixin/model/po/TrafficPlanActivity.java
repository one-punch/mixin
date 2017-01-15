package com.wteam.mixin.model.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by zbin on 17/1/14.
 */
@Data
@Entity
@Table(name = "traffic_plan_activities")
class TrafficPlanActivity extends BasePo{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name="traffic_paln_id")
    public Long trafficPalnId;

    @Column(name="business_id")
    public Long businessId;

    @Column(name="active_id")
    public Long bargainirgId;

    @Column(name="is_active")
    public boolean isActive;

    @Column(name="low_price")
    public BigDecimal lowPrice;

    @Column(name="limit_number")
    public Integer limitNumber;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;

    @Column(name = "updatedAt")
    @Temporal(TemporalType.TIMESTAMP)
    public Date updatedAt;


    @PrePersist
    void createdAt() {
        this.createdAt = this.updatedAt = new Date();
    }

    @PreUpdate
    void updatedAt() {
        this.updatedAt = new Date();
    }
}