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
@Table(name = "bargainirg_record")
class BargainirgRecord extends BasePo{

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name="bargainirg_id")
    public Long bargainirgId;

    @Column(name="customer_id")
    public Long customerId;

    public BigDecimal discount;

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