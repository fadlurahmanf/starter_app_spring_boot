package com.fadlurahmanf.starter.general.helper.utility;

import com.fadlurahmanf.starter.general.constant.DateFormatter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Utility {
    public static LocalDateTime stringToLocalDateTime(String time){
        DateTimeFormatter dtf = DateFormatter.dtf1;
        return LocalDateTime.parse(time, dtf);
    }
    public static Date getExpired(){
        LocalDateTime now = LocalDateTime.now();
        return fromLocalDateTimeToDate(now.plusMinutes(5));
    }
    public static Date getExpired(Long minutes){
        LocalDateTime now = LocalDateTime.now();
        return fromLocalDateTimeToDate(now.plusMinutes(minutes));
    }

    public static Date fromLocalDateTimeToDate(LocalDateTime localDateTime){
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDateTime fromDateToLocalDateTime(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
}
