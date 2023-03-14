package com.fadlurahmanf.starter.identity.dto.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public String accessToken;
    public String refreshToken;

    public LoginResponse(){}

    public LoginResponse(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
