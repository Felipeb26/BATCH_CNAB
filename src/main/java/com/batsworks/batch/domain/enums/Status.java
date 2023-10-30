package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    UNKNOWN(0L),
    COMMON_ERROR(1L),
    COMMON_SUCESS(2L),
    PROCESSANDO(3L),
    PROCESSADO_ERRO(4L),
    PROCESSADO_SUCESSO(5L),
    DOWNLOADING(6L),
    DOWNLOAD_ERROR(7L);

    private final Long code;
}
