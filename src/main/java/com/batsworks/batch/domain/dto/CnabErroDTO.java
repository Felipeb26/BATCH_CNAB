package com.batsworks.batch.domain.dto;

import java.time.LocalDateTime;

public record CnabErroDTO(
        Long number,
        String line,
        String erro,
        String message,
        LocalDateTime dataCadastro
) {
}
