package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public enum JobParamsEnum {

    ID("ID"),
    PATH("PATH"),
    TIME("TIME"),
    FILENAME("FILENAME");

    private final String param;

    public String param() {
        return this.param;
    }

}
