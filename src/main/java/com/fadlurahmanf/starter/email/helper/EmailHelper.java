package com.fadlurahmanf.starter.email.helper;

import com.fadlurahmanf.starter.email.constant.EmailConstant;
import com.fadlurahmanf.starter.email.constant.EmailType;
import com.fadlurahmanf.starter.general.constant.MessageConstant;
import com.fadlurahmanf.starter.general.helper.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public static boolean isExpired(String time) throws Exception {
        LocalDateTime expired = Utility.stringToLocalDateTime(time);
        LocalDateTime now = LocalDateTime.now();
        if(expired.isBefore(now)){
            throw new Exception(MessageConstant.EMAIL_EXPIRED);
        }else{
            return false;
        }
    }
}
