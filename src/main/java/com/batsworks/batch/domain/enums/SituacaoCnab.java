package com.batsworks.batch.domain.enums;

import lombok.Getter;

@Getter
public enum SituacaoCnab {

    REGISTRADO("REGISTRADO"),
    AGUARDANDO_REGISTRO("AGUARDANDO_REGISTRO"),
    PAGO("PAGO"),
    AGUARDANDO_PAGAMENTO("AGUARDANDO_PAGAMENTO"),
    BAIXA_CEDENTE("BAIXA_CEDENTE"),
    AGUARDANDO_BAIXA("AGUARDANDO_BAIXA"),
    AGUARDANDO_ALTERACAO("AGUARDANDO_ALTERACAO");

    SituacaoCnab(String situacao) {
        this.situacao = situacao;
    }

    private final String situacao;
}
