package com.fadlurahmanf.starter.general.constant;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String rawFormat1 = "yyyy-MM-dd HH:mm:ss";
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat(rawFormat1);
    public static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(rawFormat1);
}
