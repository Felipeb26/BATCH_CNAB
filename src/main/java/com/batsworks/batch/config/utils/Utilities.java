package com.batsworks.batch.config.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Calendar;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class Utilities {

    public static String randomFileName() {
        return UUID.randomUUID() + ".rem";
    }

    public static String randomFileName(String extension) {
        if (isNull(extension) || extension.isBlank()) return randomFileName();
        if (!extension.startsWith(".")) extension = ".".concat(extension);
        return UUID.randomUUID() + extension;
    }

    public static String actualDateString() {
        var date = Calendar.getInstance();
        return String.format("%s-%s %s:%s", date.get(Calendar.MONTH) + 1, date.get(Calendar.DATE), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE));
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
            byte[] temp = new byte[8 * 1024];
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
            byte[] temp = new byte[8 * 1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(temp);
                baos.write(temp, 0, count);
            }
            return baos.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return data;
    }

    public static String encodeByteToBASE64String(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    public static byte[] decodeBASE64(byte[] data) {
        return Base64.getDecoder().decode(data);
    }

    public static void transferFile(File file, String target) {
        try (InputStream is = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(is);
             OutputStream os = new FileOutputStream(target)) {
            byte[] buffer = new byte[1048576];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (Exception e) {

        }
    }

    public static Boolean transferFile(InputStream inputStream, Path target) {
        try (BufferedInputStream bis = new BufferedInputStream(inputStream);
             OutputStream os = new FileOutputStream(target.toString())) {
            byte[] buffer = new byte[1048576];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, read);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Boolean deleteFile(Object o) {
        try {
            if (o instanceof String stringFilePath)
                Files.deleteIfExists(Paths.get(stringFilePath));
            if (o instanceof Path path)
                Files.deleteIfExists(path);
            if (o instanceof File file)
                Files.deleteIfExists(file.toPath());
        } catch (Exception e) {
            if (e instanceof DirectoryNotEmptyException) {
                return deleteNotEmptyFolder(o);
            } else {
                log.warn("An error has happen {}", e.getMessage(), new RuntimeException(e.getMessage()));
            }
            return false;
        }
        return true;
    }

    private static Boolean deleteNotEmptyFolder(Object o) {
        if (o instanceof Path path) {
            try (Stream<Path> pathStream = Files.walk(path)) {
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {
                return false;
            }
        }

        if (o instanceof String string) {
            try (Stream<Path> pathStream = Files.walk(Path.of(string))) {
                pathStream.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

}
