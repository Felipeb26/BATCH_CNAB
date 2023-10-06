package com.batsworks.batch.domain.records;

import java.math.BigDecimal;

public record Cnab400(
        String identRegistro,
        String agenciaDebito,
        String digitoAgencia,
        String razaoAgencia,
        String contaCorrente,
        String digitoConta,
        String identBeneficiario,
        String controleParticipante,
        String codigoBanco,
        Integer campoMulta,
        BigDecimal percentualMulta,
        Long nossoNumero,
        String digitoConferenciaNumeroBanco,
        Long descontoDia,
        Long condicaoEmpissaoPapeladaCobranca,
        String boletoDebitoAutomatico,
        Long identificacaoOcorrencia,
        String numeroDocumento,
        String dataVencimento,
        BigDecimal valorTitulo,
        Long especieTitulo,
        String dataEmissao,
        String primeiraInstrucao,
        String segundaInstrucao,
        BigDecimal moraDia,
        String dataLimiteDescontoConcessao,
        BigDecimal valorDesconto,
        BigDecimal valorIOF,
        String valorAbatimento,
        Long tipoPagador,
        String nomePagador,
        String endereco,
        String primeiraMensagem,
        String cep,
        String sufixoCEP,
        String segundaMensagem,
        String sequencialRegistro
) {

}