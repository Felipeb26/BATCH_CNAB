package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    COMMON_ERROR(0L),
    PROCESSANDO(1L),
    PROCESSADO_ERRO(2L),
    PROCESSADO_SUCESSO(3L),
    DOWNLOADING(4L),
    DOWNLOAD_ERROR(5L);

    private final Long code;
}
