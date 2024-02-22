package com.batsworks.batch.config.exception;

import jakarta.annotation.Nullable;
import lombok.Getter;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class BussinesException extends ResponseStatusException {

    private final String message;
    private final Object[] args;
    private final StatusEnum statusEnum;

    public BussinesException(HttpStatusCode status, @Nullable StatusEnum statusEnum, String message, Object[] args) {
        super(status);
        this.message = message;
        this.statusEnum = statusEnum;
        this.args = args;
    }

    public BussinesException(HttpStatusCode status, StatusEnum statusEnum, String message) {
        super(status);
        this.message = message;
        this.args = new Object[]{};
        this.statusEnum = statusEnum;
    }

    public BussinesException(HttpStatusCode status, String message) {
        super(status);
        this.message = message;
        this.statusEnum = StatusEnum.ERROR_WITH_MESSAGE;
        this.args = new Object[]{};
    }
}
