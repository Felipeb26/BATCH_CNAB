package com.batsworks.batch.config.utils;

import lombok.experimental.UtilityClass;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import static java.util.Objects.isNull;

@UtilityClass
public class Utilities {

    public static String randomName() {
        return UUID.randomUUID() + ".rem";
    }

    public static String randomName(String extension) {
        if (isNull(extension) || extension.isBlank()) return randomName();
        if (!extension.startsWith(".")) extension = ".".concat(extension);
        return UUID.randomUUID() + extension;
    }

    public static Date parseDate(String date) throws ParseException {
        var dateFormat = new SimpleDateFormat("ddMMyy");
        if (isNull(date) || date.isBlank())
            return null;
        return new Date(dateFormat.parse(date).getTime());
    }

    public static Date parseDate(String date, String pattern) throws ParseException {
        var dateFormat = new SimpleDateFormat(pattern);
        if (isNull(date) || date.isBlank())
            return null;
        return new Date(dateFormat.parse(date).getTime());
    }

}
