package com.batsworks.batch.config.exception;

import lombok.Getter;

@Getter
public enum StatusEnum {

    UNKNOW_ERROR("UNKNOW_ERROR"),
    ERROR_WITH_MESSAGE("ERROR_WITH_MESSAGE");

    private final String error;

    StatusEnum(String error){
        this.error = error;
    }

}
