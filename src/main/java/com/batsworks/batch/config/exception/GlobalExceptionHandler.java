package com.batsworks.batch.config.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> hanfleFileAlreadyImported(JobInstanceAlreadyCompleteException exception) {
        log.error("ERROR FOUND: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body("Arquivo infomado já importado");
    }

}