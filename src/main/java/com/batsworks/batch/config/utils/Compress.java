package com.batsworks.batch.config.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static java.util.Objects.nonNull;

@Slf4j
@Service
public class Compress {

    private static final int BUFFER = 1048576;

    public byte[] compressData(byte[] data, String fileName) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
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

    public byte[] decompressData(byte[] data) {
        try (ZipInputStream zipInputStream = new ZipInputStream(new ByteArrayInputStream(data));
             ByteArrayOutputStream baos = new ByteArrayOutputStream(BUFFER)) {

            ZipEntry zipEntry = zipInputStream.getNextEntry();
            String fileName = "";
            byte[] buffer = new byte[2048];
            while (nonNull(zipEntry)) {
                int len;
                fileName = zipEntry.getName();
                while ((len = zipInputStream.read(buffer)) > 0) {
                    baos.write(buffer, 0, len);
                }
                zipEntry = zipInputStream.getNextEntry();
            }

//            String fileName = null;
//            ZipEntry entry;
//            while ((entry = zipInputStream.getNextEntry()) != null) {
//                fileName = entry.getName();
//                log.info("ARQUIVO {} sendo descompactado", fileName);
//                byte[] buffer = new byte[BUFFER];
//
//                int size;
//                while ((size = zipInputStream.read(buffer, 0, buffer.length)) != -1) {
//                    baos.write(buffer, 0, size);
//                }
//            }

            zipInputStream.closeEntry();
            zipInputStream.close();

            log.info("ARQUIVO {} descompactado", fileName);
            return baos.toByteArray();
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
            return new byte[0];
        }
    }
}
