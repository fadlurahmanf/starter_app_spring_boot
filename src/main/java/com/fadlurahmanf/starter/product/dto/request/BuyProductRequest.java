package com.fadlurahmanf.starter.product.dto.request;

public class BuyProductRequest {
    public String email;
    public String productId;
    public BuyProductRequest(){}
    public BuyProductRequest(String email, String productId){
        this.email = email;
        this.productId = productId;
    }
}
