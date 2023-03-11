package com.fadlurahmanf.starter.identity.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import org.hibernate.annotations.GenericGenerator;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

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
    public String createdAt;
    public IdentityEntity(){}

    public IdentityEntity(String email, String password){
        this.email = email;
        this.password = password;
        this.status = "";
    }

    public IdentityEntity(String id, String email, String password, String status, Double balance, String createdAt){
        this.id = id;
        this.email = email;
        this.password = password;
        this.status = status;
        this.balance = balance;
        this.createdAt = createdAt;
    }
}
