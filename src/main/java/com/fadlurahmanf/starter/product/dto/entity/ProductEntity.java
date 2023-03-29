package com.fadlurahmanf.starter.product.dto.entity;

import com.fadlurahmanf.starter.general.constant.EntityConstant;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = EntityConstant.Product.entity)
public class ProductEntity {
    @Id
    @Column(name = EntityConstant.Product.id)
    public String id;

    @Column(name = EntityConstant.Product.totalProduct)
    public Integer totalProduct;

    public ProductEntity(){}
}
