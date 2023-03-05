package com.fadlurahmanf.starter.general.dto.exception;

import org.springframework.http.HttpStatus;

import java.io.IOException;

public class CustomIOException extends IOException {
    public Integer statusCode;
    public String message;
    public HttpStatus httpStatus;

    public CustomIOException(){}
    public CustomIOException(String message){
        this.message = message;
        this.httpStatus = HttpStatus.OK;
        this.statusCode = HttpStatus.OK.value();
    }

    public CustomIOException(String message, HttpStatus httpStatus){
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = httpStatus.value();
    }

    public CustomIOException(String message, HttpStatus httpStatus, Integer statusCode){
        this.message = message;
        this.httpStatus = httpStatus;
        this.statusCode = statusCode;
    }
}
