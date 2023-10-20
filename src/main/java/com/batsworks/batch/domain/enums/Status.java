package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    UNKNOWN(0L),
    COMMON_ERROR(1L),
    PROCESSANDO(2L),
    PROCESSADO_ERRO(3L),
    PROCESSADO_SUCESSO(4L),
    DOWNLOADING(5L),
    DOWNLOAD_ERROR(6L);

    private final Long code;
}
