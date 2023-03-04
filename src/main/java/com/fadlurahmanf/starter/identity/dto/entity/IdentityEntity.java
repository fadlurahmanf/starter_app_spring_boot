package com.fadlurahmanf.starter.identity.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

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

    public String createdAt;

    public IdentityEntity(){}

    public IdentityEntity(String email, String password){
        this.email = email;
        this.password = password;
    }

    public IdentityEntity(String id, String email, String password, String createdAt){
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
}
