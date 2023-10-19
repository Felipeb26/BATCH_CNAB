package com.batsworks.batch.config.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.util.Objects.isNull;

@Slf4j
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

    public static String fileType(InputStream inputStream, String fileName) throws IOException {
        var mimetype = URLConnection.guessContentTypeFromStream(inputStream);
        if (isNull(mimetype) || mimetype.isBlank())
            mimetype = fileName.substring(fileName.lastIndexOf(".") + 1);
        return mimetype;
    }

    public static byte[] compressData(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length)) {
            byte[] temp = new byte[4 * 1024];
            while (!deflater.finished()) {
                int size = deflater.deflate(temp);
                baos.write(temp, 0, size);
            }
            return baos.toByteArray();
        } catch (Exception ignored) {
        }
        return data;
    }

    public static byte[] decompressData(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(data.length)) {
            byte[] temp = new byte[4 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(temp);
                baos.write(temp, 0, count);
            }
        } catch (Exception ignored) {
            log.error(ignored.getMessage());
        }
        return data;
    }

    public static String byteToBase64String(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }
    public static byte[] stringBase64ToByte(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

}
