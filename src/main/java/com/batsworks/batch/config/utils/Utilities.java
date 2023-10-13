package com.batsworks.batch.config.utils;

import lombok.experimental.UtilityClass;

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

}
