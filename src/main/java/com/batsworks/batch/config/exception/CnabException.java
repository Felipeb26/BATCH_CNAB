package com.batsworks.batch.config.exception;

import com.batsworks.batch.domain.records.Cnab;
import lombok.Getter;

import static java.lang.Long.parseLong;

@Getter
public class CnabException extends Exception {

    private final Long actualLine;
    private final int size;

    public CnabException(String message, int actualLine, String line) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = line.length();
    }

    public CnabException(String message, int actualLine) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = 0;
    }

    public CnabException(String message, String line) {
        super(message);
        this.actualLine = 0L;
        this.size = line.length();
    }

    public CnabException(String message) {
        super(message);
        this.actualLine = 0L;
        this.size = 0;
    }

    private Long assertToLongNumber(Object o) {
        return parseLong(o.toString());
    }

}
