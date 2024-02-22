package com.batsworks.batch.config.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static com.batsworks.batch.utils.Formats.customDateTimeString;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BussinesExceptionEntity {

    private String path;
    @Builder.Default
    private String time = customDateTimeString("yyyy-MM-dd HH:ss");
    private String error;
    private Object[] errors;
    @Builder.Default
    private StatusEnum status = StatusEnum.UNKNOW_ERROR;

    public void setTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.time = time.isBlank() ? LocalDateTime.now().format(formatter) : time;
    }

}
