package com.fadlurahmanf.starter.general.dto.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends Exception {
    public Integer statusCode;
    public String message;
    public HttpStatus httpStatus;

    public CustomException(){}
    public CustomException(String message){
        this.message = message;
        this.httpStatus = HttpStatus.OK;
        this.statusCode = HttpStatus.OK.value();
    }

    public CustomException(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
    }

    public CustomException(String message, HttpStatus httpStatus, Integer statusCode){
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }
}
