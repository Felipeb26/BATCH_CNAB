package com.batsworks.batch.config.exception;

import lombok.Getter;

import static java.lang.Long.parseLong;

@Getter
public class CnabProcessingException extends Exception {

    private final Long actualLine;
    private final int size;
    private final String line;
    private final Long arquivo;

    public CnabProcessingException(String message, int actualLine, String line, Long arquivo) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = line.length();
        this.line = line;
        this.arquivo = arquivo;
    }

    public CnabProcessingException(String message, int actualLine, String line) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = line.length();
        this.line = line;
        arquivo = null;
    }

    public CnabProcessingException(String message, int actualLine) {
        super(message);
        this.actualLine = assertToLongNumber(actualLine);
        this.size = 0;
        this.line = "";
        arquivo = null;
    }

    public CnabProcessingException(String message, String line) {
        super(message);
        this.actualLine = 0L;
        this.size = line.length();
        this.line = line;
        arquivo = null;
    }

    public CnabProcessingException(String message) {
        super(message);
        this.actualLine = 0L;
        this.size = 0;
        this.line = "";
        arquivo = null;
    }

    private Long assertToLongNumber(Object o) {
        return parseLong(o.toString());
    }

}
