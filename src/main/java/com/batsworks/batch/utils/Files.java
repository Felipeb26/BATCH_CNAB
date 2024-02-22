package com.batsworks.batch.utils;


import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class Files {
    private static final int BUFFER = 1024;

    public static Boolean validFile(MultipartFile file, String fileName) {
        try {
            var fileType = fileType(file.getInputStream(), fileName);
            return fileType.equalsIgnoreCase("rem");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static String randomFileName() {
        return UUID.randomUUID() + ".rem";
    }

    public static String randomFileName(String extension) {
        if (isNull(extension) || extension.isBlank()) return randomFileName();
        if (!extension.startsWith(".")) extension = ".".concat(extension);
        return UUID.randomUUID() + extension;
    }

    public static String fileType(InputStream inputStream, String fileName) throws IOException {
        var mimetype = URLConnection.guessContentTypeFromStream(inputStream);
        if (isNull(mimetype) || mimetype.isBlank()) {
            if (isNull(fileName) || fileName.isBlank()) return "txt";
            return fileName.substring(fileName.lastIndexOf(".") + 1);
        }
        return mimetype;
    }

    public static byte[] compressData(byte[] data) {

        try {
            byte[] buffer = new byte[BUFFER];
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ByteArrayInputStream fis = new ByteArrayInputStream(data);

            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }
            fis.close();
            byte[] inputBytes = baos.toByteArray();
            baos.close();

            ByteArrayOutputStream compressedBaos = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipOut = new GZIPOutputStream(compressedBaos)) {
                gzipOut.write(inputBytes);
                gzipOut.finish();
            }

            return compressedBaos.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return data;
    }

    public static byte[] decompressData(byte[] compressedData) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream baos = new ByteArrayOutputStream();
             GZIPInputStream gzipIn = new GZIPInputStream(bais)) {

            byte[] buffer = new byte[BUFFER];
            int bytesRead;

            while ((bytesRead = gzipIn.read(buffer)) != -1) {
                baos.write(buffer, 0, bytesRead);
            }

            return baos.toByteArray();

        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
        return compressedData;
    }

}
