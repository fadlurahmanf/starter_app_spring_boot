package com.fadlurahmanf.starter.identity.dto.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public String accessToken;

    public LoginResponse(){}

    public LoginResponse(String token){
        accessToken = token;
    }
}
