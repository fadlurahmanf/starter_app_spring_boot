package com.fadlurahmanf.starter.general.helper;

import com.fadlurahmanf.starter.general.constant.MessageConstant;
import org.json.JSONObject;

public class RequestBodyHelper {
    public static String isEmailExist(JSONObject jsonObject) throws Exception {
        if(jsonObject.optString("email", null) != null){
            return jsonObject.getString("email");
        }else{
            throw new Exception(MessageConstant.EMAIL_REQUIRED);
        }
    }
}
