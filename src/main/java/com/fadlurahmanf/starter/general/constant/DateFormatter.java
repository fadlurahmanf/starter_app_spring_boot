package com.fadlurahmanf.starter.general.constant;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

public class DateFormatter {
    private static final String rawFormat1 = "yyyy-MM-dd HH:mm:ss";
    private static final String rawFormat2 = "yyyyMMddHHmmss";
    private static final String rawFormat3 = "yyyy-MM-dd";
    public static final SimpleDateFormat sdf1 = new SimpleDateFormat(rawFormat1);
    public static final SimpleDateFormat sdf2 = new SimpleDateFormat(rawFormat2);
    public static final SimpleDateFormat sdf3 = new SimpleDateFormat(rawFormat3);
    public static final DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern(rawFormat1);
    public static final DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern(rawFormat2);
    public static final DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern(rawFormat3);
}
