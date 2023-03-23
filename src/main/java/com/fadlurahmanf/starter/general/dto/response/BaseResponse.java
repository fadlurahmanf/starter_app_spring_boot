package com.fadlurahmanf.starter.general.dto.response;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.http.HttpStatus;

public class BaseResponse<T> {
    @JsonProperty(namespace = "code")
    private Integer code;
    @JsonProperty(namespace = "message")
    private String message;

//    @JsonInclude(JsonInclude.Include.NON_NULL) // kalo pake ini, data nya ga tampil.
    @JsonInclude(JsonInclude.Include.USE_DEFAULTS) // kalo pake ini, data nya muncul tapi null
    @JsonProperty(namespace = "data")
    private T data;

    public BaseResponse(Integer code, String message){
        this.code = code;
        this.message = message;
        this.data = null;
    }

    public BaseResponse(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(T data){
        this.code = HttpStatus.OK.value();
        this.message = MessageConstant.SUCCESS;
        this.data = data;
    }
}
