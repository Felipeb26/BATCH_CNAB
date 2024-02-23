package com.batsworks.batch.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class Formats {

    public static String actualDateString() {
        var date = Calendar.getInstance();
        return String.format(
                "%s-%s %s:%s:%s",
                date.get(Calendar.DATE),
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.HOUR_OF_DAY),
                date.get(Calendar.MINUTE),
                date.get(Calendar.SECOND)
        );
    }

    public static Date parseDate(String date) throws ParseException {
        var dateFormat = new SimpleDateFormat("ddMMyy");
        if (isNull(date) || date.isBlank()) return null;
        return new Date(dateFormat.parse(date).getTime());
    }

    public static Date parseDate(String date, String pattern) throws ParseException {
        var dateFormat = new SimpleDateFormat(pattern);
        if (isNull(date) || date.isBlank()) return null;
        return new Date(dateFormat.parse(date).getTime());
    }

    public static String customDateTimeString(String pattern, LocalDateTime localDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDate.format(formatter);
    }

    public static String customDateTimeString(String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.now().format(formatter);
    }

}
