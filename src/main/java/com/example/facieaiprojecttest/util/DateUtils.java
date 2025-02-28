package com.example.facieaiprojecttest.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {

    public static boolean isValidDate(String dateStr) {
        if (dateStr == null || !dateStr.matches("\\d{8}")) {
            return false;
        }
        try {
            SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMdd");
            DATE_FORMAT.setLenient(false);
            DATE_FORMAT.parse(dateStr);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}

