package com.batsworks.batch.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum FileType {

    CNAB400("400"),
    CNAB240("240"),
    ZIP("zip");

    private final String type;
}
