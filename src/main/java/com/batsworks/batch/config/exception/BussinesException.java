package com.batsworks.batch.config.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BussinesException extends ResponseStatusException {

    private final String message;
    private final Object[] args;

    public BussinesException(HttpStatusCode status, String message, Object[] args) {
        super(status);
        this.message = message;
        this.args = args;
    }

    public BussinesException(HttpStatusCode status, String message) {
        super(status);
        this.message = message;
        this.args = new Object[]{};
    }
}