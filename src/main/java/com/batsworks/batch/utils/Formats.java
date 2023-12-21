package com.batsworks.batch.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.UUID;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class Formats {

    public static String mask(String value, Object... args) {
        for (Object arg : args) {
            value = value.concat("_" + arg);
        }
        value = value.trim();
        return UUID.randomUUID() +"@"+ encodeByteToBASE64String(value.getBytes(StandardCharsets.UTF_8)).concat(".rem");
    }
    public static String actualDateString() {
        var date = Calendar.getInstance();
        return String.format(
                "%s-%s %s:%s:%s",
                date.get(Calendar.MONTH) + 1,
                date.get(Calendar.DATE),
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

    public static String encodeByteToBASE64String(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeBASE64(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

}
