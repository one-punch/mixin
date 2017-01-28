package com.wteam.mixin.model.po;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by zbin on 17/1/14.
 */
@Data
@Entity
@Table(name = "bargainirg")
public class Bargainirg extends BasePo {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name = "createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    public Date createdAt;


    @Column(name="customer_order_id")
    public Long customerOrderId;

    @Column(name="traffic_plan_activity_id")
    public Long trafficPlanActivityId;

    @Column(name="customer_id")
    public Long customerId;

    public Integer state;

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

    public void setState(State state){
        switch (state) {
            case INIT:
                this.state = 0;
                break;

            case FINISH:
                this.state = 1;
                break;

            case CLOSE:
                this.state = 2;
                break;

            default:
                this.state = 0;
                break;
        }
    }

    public  enum State {
        INIT, FINISH, CLOSE
    }

}