package com.fadlurahmanf.starter.history.transfer.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;

import javax.annotation.Nullable;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class HistoryTransferResponse {
    @JsonProperty("id")
    String id;
    @JsonProperty("type")
    String type;
    @Nullable
    @JsonProperty("related_user")
    User relatedUser;
    @JsonProperty("value")
    Double value;
    @JsonProperty("created_at")
    String createdAt;

    public HistoryTransferResponse(){}

    public HistoryTransferResponse(String id, String type, Double value, @Nullable User relatedUser, String createdAt){
        this.id = id;
        this.type = type;
        this.value = value;
        this.relatedUser = relatedUser;
        this.createdAt = createdAt;
    }

    public static class User {
        @JsonProperty("id")
        String id;
        @JsonProperty("email")
        String email;

        public User(String id, String email){
            this.id = id;
            this.email = email;
        }
    }
}
