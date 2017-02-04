package com.wteam.mixin.model.po;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by zbin on 17/1/14.
 */
@Data
@Entity
@Table(name = "traffic_plan_activities")
public class TrafficPlanActivity extends BasePo{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name="traffic_plan_id")
    public Long trafficplanId;

    @Column(name="user_id")
    public Long userId;

    @Column(name="is_active")
    public boolean isActive;

    @Column(name="low_price")
    public BigDecimal lowPrice;

    @Column(name="limit_number")
    public Integer limitNumber;

    @Column(name="active_id")
    public Long activeId;

    @Column(name = "startTime")
    public Date startTime;

    @Column(name = "endTime")
    public Date endTime;

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

    public boolean isAvailable(){
        if(!this.isActive || !Optional.of(this.startTime).isPresent() || !Optional.of(this.endTime).isPresent()){
            return false;
        }else{
            Date now = new Date();
            return now.before(this.endTime) && now.after(this.startTime);
        }
    }

    public boolean isAvailable(int count, BigDecimal retailPrice, BigDecimal totalDiscount){
        if(!this.isActive || !Optional.of(this.startTime).isPresent() || !Optional.of(this.endTime).isPresent() || !Optional.of(this.limitNumber).isPresent()){
            return false;
        }else{
            Date now = new Date();
            return this.lowPrice.add(totalDiscount).compareTo(retailPrice) < 0 && now.before(this.endTime) && now.after(this.startTime) && this.limitNumber > count;
        }
    }
}