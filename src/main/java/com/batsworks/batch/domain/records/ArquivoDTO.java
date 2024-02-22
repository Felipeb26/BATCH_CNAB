package com.batsworks.batch.domain.records;

import com.batsworks.batch.domain.enums.CnabStatus;

import java.math.BigDecimal;

public record ArquivoDTO(
        Long id,
        String nome,
        String extension,
        Long fileSize,
        Long quantidade,
        CnabStatus situacao,
        String observacao,
        BigDecimal valorTotal
) {
}
