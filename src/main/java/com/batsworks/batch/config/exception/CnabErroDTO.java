package com.batsworks.batch.config.exception;

import java.time.LocalDateTime;

public record CnabErroDTO(
        Long number,
        String linhha,
        String erro,
        String message,
        LocalDateTime dataCadastro
) {
}
