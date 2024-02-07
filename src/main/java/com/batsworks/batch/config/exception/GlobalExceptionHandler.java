package com.batsworks.batch.config.exception;

import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import static com.batsworks.batch.utils.Files.deleteFile;
import static java.util.Objects.isNull;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> hanfleFileAlreadyImported(JobInstanceAlreadyCompleteException exception) {
        log.error("ERROR FOUND: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new DefaultMessage("Arquivo infomado já importado", Status.ERROR));
    }

    @ExceptionHandler(NonSkippableReadException.class)
    private ResponseEntity<Object> response(NonSkippableReadException nonSkippableReadException) {
        log.info(nonSkippableReadException.getMessage());
        return ResponseEntity.badRequest().body(nonSkippableReadException.getMessage());
    }

    @ExceptionHandler(CnabException.class)
    private ResponseEntity<Object> cnabLineException(CnabException cnabException) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", cnabException.getMessage());
        map.put("linha", cnabException.getActualLine());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(BussinesException.class)
    private ResponseEntity<Object> response(BussinesException bussinesException, HttpServletRequest httpServletRequest) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss");
        deleteFile(System.getProperty("user.dir").concat("/tmp"));
        return new ResponseEntity<>(BussinesExceptionEntity.builder()
                .error(bussinesException.getMessage())
                .status(isNull(bussinesException.getStatusEnum()) ? StatusEnum.UNKNOW_ERROR : bussinesException.getStatusEnum())
                .errors(bussinesException.getArgs())
                .time(LocalDateTime.now().format(formatter))
                .path(httpServletRequest.getServletPath())
                .build(), bussinesException.getStatusCode());
    }
}
