package com.fadlurahmanf.starter.identity.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;
import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = EntityConstant.identity)
public class IdentityEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Integer id;

    public String email;

    public String password;

    public String createdAt;

    public IdentityEntity(){}

    public IdentityEntity(Integer id, String email, String password, String createdAt){
        this.id = id;
        this.email = email;
        this.password = password;
        this.createdAt = createdAt;
    }
}
