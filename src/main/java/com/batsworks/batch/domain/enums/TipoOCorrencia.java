package com.batsworks.batch.domain.enums;

import lombok.Getter;

@Getter
public enum TipoOCorrencia {

    REMESSA(1),
    PEDIDO_DE_BAIXA(2),
    CONCESSAO_DE_ABATIMENTO(4),
    CANCELAMENTO_DE_ABATIMENTO_CONCEDIDO(5),
    ALTERACAO_DO_VENCIMENTO(6),
    ALTERACAO_DO_CONTROLE_DO_PARTICIPANTE(7),
    ALTERACAO_DE_SEU_NUMERO(8),
    ALTERACAO_DE_VALOR(20);

    private final int ocorrencia;

    TipoOCorrencia(int ocorrencia){
        this.ocorrencia = ocorrencia;
    }
}
