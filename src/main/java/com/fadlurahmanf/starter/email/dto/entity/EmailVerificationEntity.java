package com.fadlurahmanf.starter.email.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = EntityConstant.VerificationEmail.entity)
public class EmailVerificationEntity implements Serializable {
    @Column(name = EntityConstant.VerificationEmail.email)
    public String email;
    @Column(name = EntityConstant.VerificationEmail.emailType)
    public String type;

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = EntityConstant.VerificationEmail.emailToken)
    public String token;

    @Column(name = EntityConstant.VerificationEmail.isVerified)
    public Boolean isVerified;

    @Column(name = EntityConstant.VerificationEmail.isExpired)
    public Boolean isExpired;

    @Column(name = EntityConstant.VerificationEmail.expiredAt)
    public Date expiredAt;

    @Column(name = EntityConstant.VerificationEmail.createdAt)
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
