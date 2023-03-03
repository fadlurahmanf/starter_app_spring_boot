package com.fadlurahmanf.starter.email.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = EntityConstant.emailVerification)
public class EmailVerificationEntity implements Serializable {
    @Column(name = "email")
    public String email;
    @Column(name = "type")
    public String type;

    @Id
    @Column(name = "token")
    public String token;

    @Column(name = "is_verified")
    public Boolean isVerified;

    @Column(name = "expired_at")
    public String expiredAt;

    @Column(name = "created_at")
    public String createdAt;

    public EmailVerificationEntity(){}

    public EmailVerificationEntity(String email, String type, String token, Boolean isVerified, String expiredAt, String createdAt){
        this.email = email;
        this.type = type;
        this.token = token;
        this.isVerified = isVerified;
        this.expiredAt = expiredAt;
        this.createdAt = createdAt;
    }
}
