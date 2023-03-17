package com.fadlurahmanf.starter.identity.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import com.fadlurahmanf.starter.identity.constant.IdentityStatusConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = EntityConstant.identity)
public class IdentityEntity implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    public String id;
    public String email;
    public String password;
    public String status;
    public Double balance;
    @Column(name = "fcm_token")
    public String fcmToken;

    public Date createdAt;
    public IdentityEntity(){}

    @PrePersist()
    protected void onCreate(){
        this.createdAt = new Date();
    }

    public IdentityEntity(String email, String password, Double balance){
        this.email = email;
        this.password = password;
        this.status = IdentityStatusConstant.NEW;
        this.balance = balance;
    }

    public IdentityEntity(String id, String email, String password, String status, Double balance, String fcmToken, Date createdAt){
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.balance = balance;
        this.fcmToken = fcmToken;
        this.createdAt = createdAt;
    }
}
