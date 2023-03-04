package com.fadlurahmanf.starter.general.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Utility {
    public static LocalDateTime stringToLocalDateTime(String time){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.parse(time, formatter);
    }
}
