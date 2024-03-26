package com.parcelhub.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {
    public static String getNowDateLongStr(String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        Date now = new Date();
        return dateFormat.format(now);
    }
}
