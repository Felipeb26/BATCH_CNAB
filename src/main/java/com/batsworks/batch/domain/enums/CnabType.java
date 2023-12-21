package com.batsworks.batch.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CnabType {

    CNAB400("400"),
    CNAB240("240");

    private final String type;
}
