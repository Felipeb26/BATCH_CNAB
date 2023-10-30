package com.batsworks.batch.config.exception;

import com.batsworks.batch.domain.enums.Status;
import com.batsworks.batch.domain.records.DefaultMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.step.skip.NonSkippableReadException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessagingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(JobInstanceAlreadyCompleteException.class)
    private ResponseEntity<Object> hanfleFileAlreadyImported(JobInstanceAlreadyCompleteException exception) {
        log.error("ERROR FOUND: {}", exception.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new DefaultMessage("Arquivo infomado já importado", Status.COMMON_ERROR));
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
        map.put("line", cnabException.getActualLine());
        return ResponseEntity.badRequest().body(map);
    }

    @ExceptionHandler(MessagingException.class)
    private void s(MessagingException e){
        log.error(e.getMessage());
    }

}
