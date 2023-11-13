package com.batsworks.batch.utils;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.net.URLConnection;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.isNull;

@Slf4j
@UtilityClass
public class UtilitiesFiles {

    @Value("${configuration.code_prefix}")
    private String prefix;
    private static final int BUFFER = 1048576;

    public static String fileType(InputStream inputStream, String fileName) throws IOException {
        var mimetype = URLConnection.guessContentTypeFromStream(inputStream);
        if (isNull(mimetype) || mimetype.isBlank()) mimetype =
                fileName.substring(fileName.lastIndexOf(".") + 1);
        return mimetype;
    }

    public static void transferFile(File file, String target) {
        try (
                InputStream is = new FileInputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                OutputStream os = new FileOutputStream(target)
        ) {
            byte[] buffer = new byte[BUFFER];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, read);
            }
        } catch (Exception ignored) {
        }
    }

    public static Boolean transferFile(InputStream inputStream, Path target) {
        try (
                BufferedInputStream bis = new BufferedInputStream(inputStream);
                OutputStream os = new FileOutputStream(target.toString().toLowerCase())
        ) {
            byte[] buffer = new byte[BUFFER];
            int read;
            while ((read = bis.read(buffer, 0, buffer.length)) != -1) {
                os.write(buffer, 0, read);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    public static Boolean deleteFile(Object o) {
        try {
            if (o instanceof String stringFilePath) {
                var file = new File(Paths.get(stringFilePath).toString());
                return file.isDirectory()
                        ? deleteNotEmptyFolder(o)
                        : java.nio.file.Files.deleteIfExists(file.toPath());
            }
            if (o instanceof Path path) {
                var file = new File(path.toString());
                return file.isDirectory()
                        ? deleteNotEmptyFolder(o)
                        : java.nio.file.Files.deleteIfExists(file.toPath());
            }
            if (o instanceof File file) {
                return file.isDirectory()
                        ? deleteNotEmptyFolder(o)
                        : java.nio.file.Files.deleteIfExists(file.toPath());
            }
        } catch (DirectoryNotEmptyException e) {
            return deleteNotEmptyFolder(o);
        } catch (Exception e) {
            log.warn(
                    "An error has happen {}",
                    e.getMessage(),
                    new RuntimeException(e.getMessage())
            );
            return false;
        }
        return true;
    }

    private static Boolean deleteNotEmptyFolder(Object o) {
        if (o instanceof Path path) {
            try (Stream<Path> pathStream = java.nio.file.Files.walk(path)) {
                pathStream
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {
                return false;
            }
        }

        if (o instanceof String string) {
            try (Stream<Path> pathStream = java.nio.file.Files.walk(Path.of(string))) {
                pathStream
                        .sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
            } catch (Exception ignored) {
                return false;
            }
        }
        return true;
    }

    public static String findFile() {
        var folder = System.getProperty("user.dir").concat("/tmp");
        try {
            var file = new File(folder);
            if (file.isFile()) return file.getAbsolutePath();
            try (Stream<Path> walked = java.nio.file.Files.walk(file.toPath())) {
                return walked
                        .filter(it -> !regexFile(it, "rem").isBlank())
                        .findFirst()
                        .map(Path::toString)
                        .orElse(null);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String findFile(String extension) {
        var folder = System.getProperty("user.dir").concat("/tmp");
        try {
            var file = new File(folder);
            if (file.isFile()) return file.getAbsolutePath();
            try (Stream<Path> walked = java.nio.file.Files.walk(file.toPath())) {
                return walked
                        .filter(it -> !regexFile(it, extension).isBlank())
                        .findFirst()
                        .map(Path::toString)
                        .orElse(null);
            }
        } catch (Exception e) {
            return null;
        }
    }

    public static String resolveFileName(String file, Boolean findCode) {
        if (file == null) return file;
        var index = file.lastIndexOf(isNull(prefix) ? "_" : prefix);
        if (index == -1) return file;
        if (findCode) {
            return file.substring(index + 1);
        }
        return file.substring(0, index);
    }

    public static String randomFileName() {
        return UUID.randomUUID() + ".rem";
    }

    public String regexFile(Object o, String extension) {
        if (o instanceof String s) {
            var index = s.lastIndexOf(extension);
            if (index == -1) return "";
            index = index + 3;
            s = s.substring(index);
            return s;
        }
        if (o instanceof Path path) {
            var index = path.toString().lastIndexOf(extension);
            if (index == -1) return "";
            index = index + 3;
            return path.toString().substring(0, index);
        }
        return "";
    }

    public static String randomFileName(String extension) {
        if (isNull(extension) || extension.isBlank()) return randomFileName();
        if (!extension.startsWith(".")) extension = ".".concat(extension);
        return UUID.randomUUID() + extension;
    }

    public static byte[] compressData(byte[] data, String fileName) {
        try (
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ZipOutputStream outputStream = new ZipOutputStream(baos)
        ) {
            ZipEntry zipEntry = new ZipEntry(fileName);
            outputStream.putNextEntry(zipEntry);
            outputStream.write(data);
            outputStream.closeEntry();
            return baos.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

}
