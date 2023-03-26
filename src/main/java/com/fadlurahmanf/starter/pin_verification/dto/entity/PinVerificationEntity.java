package com.fadlurahmanf.starter.pin_verification.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = EntityConstant.PinVerification.entity)
public class PinVerificationEntity {
    @Id
    @Column(name = EntityConstant.PinVerification.userId)
    public String userId;
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = EntityConstant.PinVerification.pinToken)
    public String pinToken;
    @Column(name = EntityConstant.PinVerification.isUsed)
    public Boolean isUsed;
    @Column(name = EntityConstant.PinVerification.createdAt)
    public Date createdAt;

    @PrePersist
    protected void onCreate(){
        this.createdAt = new Date();
    }

    public PinVerificationEntity(){}

    public PinVerificationEntity(String userId, String pinToken, Boolean isUsed){
        this.userId = userId;
        this.pinToken = pinToken;
        this.isUsed = isUsed;
    }
}
