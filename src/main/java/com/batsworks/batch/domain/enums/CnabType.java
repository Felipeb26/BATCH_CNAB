package com.batsworks.batch.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CnabType {

    CNAB400("400"),
    CNAB200("200");

    private final String type;
}
