package com.batsworks.batch.config.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BussinesExceptionEntity {

    private String path;
    private String time;
    private String error;
    private Object[] arguments;
    private StatusEnum status;

    public void setTime(String time) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.time = time.isBlank() ? LocalDateTime.now().format(formatter) : time;
    }

}
