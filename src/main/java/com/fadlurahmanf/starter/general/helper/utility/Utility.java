package com.fadlurahmanf.starter.general.helper.utility;

import com.fadlurahmanf.starter.general.constant.DateFormatter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    public static LocalDateTime stringToLocalDateTime(String time){
        DateTimeFormatter dtf = DateFormatter.dtf1;
        return LocalDateTime.parse(time, dtf);
    }
}
