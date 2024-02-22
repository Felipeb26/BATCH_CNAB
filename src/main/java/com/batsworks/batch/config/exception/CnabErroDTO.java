package com.batsworks.batch.config.exception;

import java.time.LocalDateTime;

public record CnabErroDTO(
        Long numeroLinha,
        String linha,
        String erro,
        String message,
        LocalDateTime dataCadastro
) {
}
