package com.batsworks.batch.config.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.HashMap;
import java.util.Map;

import static com.batsworks.batch.config.exception.StatusEnum.ERROR_WITH_MESSAGE;
import static com.batsworks.batch.utils.Formats.customDateTimeString;
import static org.springframework.http.HttpStatus.BAD_REQUEST;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MissingServletRequestPartException.class)
    private ResponseEntity<Object> response(MissingServletRequestPartException missingParameter, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(BussinesExceptionEntity.builder()
                .path(httpServletRequest.getServletPath())
                .error(String.format("O Parametro %s não foi encontrado durante a operação!", missingParameter.getRequestPartName()))
                .errors(new Object[]{missingParameter.getMessage()})
                .build(), BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    private ResponseEntity<Object> response(MissingServletRequestParameterException missingParameter, HttpServletRequest httpServletRequest) {
        return new ResponseEntity<>(BussinesExceptionEntity.builder()
                .path(httpServletRequest.getServletPath())
                .error(String.format("O Parametro %s não foi encontrado durante a operação!", missingParameter.getParameterName()))
                .errors(new Object[]{missingParameter.getMessage()})
                .build(), BAD_REQUEST);
    }

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> hanfleFileAlreadyImported(JobInstanceAlreadyCompleteException exception) {
        log.error("ERROR FOUND: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(BussinesExceptionEntity.builder()
                .time(customDateTimeString("yyyy-MM-dd HH:ss"))
                .status(ERROR_WITH_MESSAGE)
                .error(exception.getMessage())
                .errors(new Object[]{"Arquivo infomado já importado", exception.getMessage()})
                .build());
    }

    @ExceptionHandler(NonSkippableReadException.class)
    private ResponseEntity<Object> response(NonSkippableReadException nonSkippableReadException) {
        log.info(nonSkippableReadException.getMessage());
        return ResponseEntity.badRequest().body(nonSkippableReadException.getMessage());
    }

    @ExceptionHandler(CnabProcessingException.class)
    private ResponseEntity<Object> cnabLineException(CnabProcessingException cnabProcessingException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", cnabProcessingException.getMessage());
        map.put("linha", cnabProcessingException.getActualLine());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(BussinesException.class)
    private ResponseEntity<Object> response(BussinesException bussinesException, HttpServletRequest httpServletRequest) {

        return new ResponseEntity<>(BussinesExceptionEntity.builder()
                .error(bussinesException.getMessage())
                .status(bussinesException.getStatusEnum())
                .errors(bussinesException.getArgs())
                .path(httpServletRequest.getServletPath())
                .build(), bussinesException.getStatusCode());
    }
}
