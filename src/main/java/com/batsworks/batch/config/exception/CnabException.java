package com.batsworks.batch.config.exception;

import lombok.Getter;

import static java.lang.Long.parseLong;

@Getter
public class CnabException extends Exception {

    private Long actualLine;
    private int size;
    private String line;

    public CnabException(String message, int actualLine, String line) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = line.length();
        this.line = line;
    }

    public CnabException(String message, int actualLine) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = 0;
        this.line = "";
    }

    public CnabException(String message, String line) {
        super(message);
        this.actualLine = 0L;
        this.size = line.length();
        this.line = line;
    }

    public CnabException(String message) {
        super(message);
        this.actualLine = 0L;
        this.size = 0;
        this.line = "";
    }

    private Long assertToLongNumber(Object o) {
        return parseLong(o.toString());
    }

}
