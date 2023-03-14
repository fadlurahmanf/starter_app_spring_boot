package com.fadlurahmanf.starter.email.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = EntityConstant.verificationEmail)
public class EmailVerificationEntity implements Serializable {
    @Column(name = "email")
    public String email;
    @Column(name = "email_type")
    public String type;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "email_token")
    public String token;

    @Column(name = "is_verified")
    public Boolean isVerified;

    @Column(name = "is_expired")
    public Boolean isExpired;

    @Column(name = "expired_at")
    public Date expiredAt;

    @Column(name = "created_at")
    public Date createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }

    public EmailVerificationEntity(){}

    public EmailVerificationEntity(String email, String type, Boolean isVerified, Boolean isExpired, Date expiredAt){
        this.email = email;
        this.type = type;
        this.isVerified = isVerified;
        this.isExpired = isExpired;
        this.expiredAt = expiredAt;
    }

    public EmailVerificationEntity(String email, String type, String token, Boolean isVerified, Boolean isExpired, Date expiredAt){
        this.email = email;
        this.type = type;
        this.token = token;
        this.isVerified = isVerified;
        this.expiredAt = expiredAt;
    }
}
