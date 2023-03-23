package com.fadlurahmanf.starter.general.helper.validator;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import org.json.JSONObject;

public class RequestBodyValidator {
    public static String validateEmailRequest(JSONObject jsonObject) throws Exception {
        String key = "email";
        String email = jsonObject.optString(key, null);
        if(email != null){
            if(!GeneralValidator.isValidEmail(jsonObject.optString(key, null))){
                throw new CustomException(MessageConstant.WRONG_FORMAT_EMAIL);
            }else{
                return jsonObject.getString(key);
            }
        }else if(jsonObject.optString(key, null).isEmpty()){
            throw new CustomException(MessageConstant.EMAIL_REQUIRED);
        }else{
            throw new CustomException(MessageConstant.EMAIL_REQUIRED);
        }
    }

    public static String validatePasswordRequest(JSONObject jsonObject) throws Exception {
        String key = "password";
        String password = jsonObject.optString(key, null);
        if(password != null){
            return jsonObject.getString(key);
        }else if(jsonObject.optString(key, null).isEmpty()){
            throw new CustomException(MessageConstant.PASSWORD_REQUIRED);
        }else{
            throw new CustomException(MessageConstant.PASSWORD_REQUIRED);
        }
    }

    public static String validateRefreshToken(JSONObject jsonObject) throws Exception {
        String key = "refreshToken";
        String password = jsonObject.optString(key, null);
        if(password != null){
            return jsonObject.getString(key);
        }else if(jsonObject.optString(key, null).isEmpty()){
            throw new CustomException(MessageConstant.REFRESH_TOKEN_REQUIRED);
        }else{
            throw new CustomException(MessageConstant.REFRESH_TOKEN_REQUIRED);
        }
    }

    public static String validateFCMToken(JSONObject jsonObject) throws Exception {
        String key = "fcmToken";
        String token = jsonObject.optString(key, null);
        if(token != null){
            return jsonObject.getString(key);
        }else if(jsonObject.optString(key, null).isEmpty()){
            throw new CustomException(MessageConstant.TOKEN_REQUIRED);
        }else{
            throw new CustomException(MessageConstant.TOKEN_REQUIRED);
        }
    }


}
