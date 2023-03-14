package com.fadlurahmanf.starter.email.helper;

import com.fadlurahmanf.starter.email.constant.EmailConstant;
import com.fadlurahmanf.starter.email.constant.EmailType;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.dto.exception.CustomException;
import com.fadlurahmanf.starter.general.helper.utility.Utility;

import java.time.LocalDateTime;
import java.util.Date;

public class EmailHelper {
    public String getRawString(EmailType type){
        if(type == EmailType.REGISTRATION){
            return EmailConstant.EMAIL_TYPE_REGISTRATION;
        }else if(type == EmailType.BROADCAST){
            return EmailConstant.EMAIL_TYPE_BROADCAST;
        }else{
            return "-";
        }
    }

    public static boolean isExpired(Date expired) throws Exception {
        Date now = new Date();
        if(expired.before(now)){
            throw new CustomException(MessageConstant.EMAIL_EXPIRED);
        }else{
            return false;
        }
    }
}
