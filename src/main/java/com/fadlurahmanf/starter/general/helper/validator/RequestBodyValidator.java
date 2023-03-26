package com.fadlurahmanf.starter.general.helper.validator;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import org.json.JSONObject;

import javax.annotation.Nullable;

public class RequestBodyValidator {
    @Deprecated
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

    public static String validateEmailRequest(@Nullable String email) throws Exception {
        if(email != null){
            if(!GeneralValidator.isValidEmail(email)){
                throw new CustomException(MessageConstant.WRONG_FORMAT_EMAIL);
            }else if(email.isEmpty()){
                throw new CustomException(MessageConstant.EMAIL_REQUIRED);
            }else{
                return email;
            }
        }else{
            throw new CustomException(MessageConstant.EMAIL_REQUIRED);
        }
    }

    @Deprecated
    public static String validateCreatePasswordRequest(JSONObject jsonObject) throws Exception {
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

    public static String validateCreatePasswordRequest(@Nullable String password) throws Exception {
        if(password != null){
            if(password.isEmpty()){
                throw new CustomException(MessageConstant.PASSWORD_REQUIRED);
            }else if(password.length() < 8 || password.length() > 12){
                throw new CustomException(MessageConstant.BAD_PASSWORD);
            }else{
                return password;
            }
        }else{
            throw new CustomException(MessageConstant.PASSWORD_REQUIRED);
        }
    }

    public static String validatePinVerificationRequest(@Nullable String pin) throws Exception {
        if(pin != null){
            if(pin.isEmpty()){
                throw new CustomException(MessageConstant.PIN_REQUIRED);
            }else if(pin.length() != 6){
                throw new CustomException(MessageConstant.BAD_PIN);
            }else{
                return pin;
            }
        }else{
            throw new CustomException(MessageConstant.PIN_REQUIRED);
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
