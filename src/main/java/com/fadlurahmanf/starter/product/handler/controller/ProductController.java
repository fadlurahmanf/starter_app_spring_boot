package com.fadlurahmanf.starter.product.handler.controller;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.dto.response.BaseResponse;
import com.fadlurahmanf.starter.product.constant.ProductURL;
import com.fadlurahmanf.starter.product.dto.request.BuyProductRequest;
import com.fadlurahmanf.starter.product.handler.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = ProductURL.basePrefix)
public class ProductController {
    @Autowired
    ProductService service;

    @PostMapping(path = "/buy-product")
    public ResponseEntity<BaseResponse> buyProduct(@RequestBody BuyProductRequest request){
        try {
            service.buyProduct(request.email, request.productId);
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.OK.value(), MessageConstant.SUCCESS), HttpStatus.OK);
        }catch (CustomException e){
            return new ResponseEntity<>(new BaseResponse<>(e.statusCode, e.message), e.httpStatus);
        }catch (Exception e){
            return new ResponseEntity<>(new BaseResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}