package com.batsworks.batch.config.exception;

import lombok.Getter;

@Getter
public enum StatusEnum {

    UNKNOW_ERROR("UNKNOW_ERROR");

    private final String error;

    StatusEnum(String error){
        this.error = error;
    }

}
