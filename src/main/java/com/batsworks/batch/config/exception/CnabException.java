package com.batsworks.batch.config.exception;

import jakarta.annotation.Nullable;
import org.springframework.http.HttpStatusCode;

public class CnabException extends BussinesException {

    public CnabException(HttpStatusCode status, @Nullable StatusEnum statusEnum, String message, Object[] args) {
        super(status, statusEnum, message, args);
    }

    public CnabException(HttpStatusCode status, StatusEnum statusEnum, String message) {
        super(status, statusEnum, message);
    }

    public CnabException(HttpStatusCode status, String message) {
        super(status, message);
    }
}
