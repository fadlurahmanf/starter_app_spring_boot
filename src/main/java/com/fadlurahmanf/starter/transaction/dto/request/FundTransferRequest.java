package com.fadlurahmanf.starter.transaction.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FundTransferRequest {
    @JsonProperty("pinToken")
    public String pinToken;
    @JsonProperty("balance")
    public Double balance;
    public FundTransferRequest(){}
}
