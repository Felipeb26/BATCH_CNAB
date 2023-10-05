package com.batsworks.batch.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Status {

    PROCESSANDO(0L),
    PROCESSADO_ERRO(1L),
    PROCESSADO_SUCESSO(2L);

    private final Long code;
}
