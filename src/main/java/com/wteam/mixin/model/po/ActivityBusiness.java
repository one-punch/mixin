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
@Table(name = "activity_business")
public class ActivityBusiness extends BasePo {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = IDENTITY)
    public Long id;

    @Column(name="user_id")
    public Long userId;

    @Column(name="active_id")
    public Long acticeId;

    @Column(name="is_active")
    public boolean isActive;

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
