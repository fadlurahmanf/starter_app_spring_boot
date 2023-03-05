package com.fadlurahmanf.starter.identity.dto.response;

import java.io.Serializable;

public class LoginResponse implements Serializable {
    public String jwtToken;

    public LoginResponse(){}

    public LoginResponse(String token){
        jwtToken = token;
    }
}
